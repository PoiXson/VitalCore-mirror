package com.poixson.commonmc.tools.scripting.engine;

import static com.poixson.commonmc.tools.scripting.engine.CraftScript.LOG;
import static com.poixson.commonmc.tools.scripting.engine.CraftScript.LOG_PREFIX;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import com.poixson.commonmc.tools.scripting.LocalOut;
import com.poixson.commonmc.tools.scripting.events.ScriptLoadedEvent;
import com.poixson.commonmc.tools.scripting.events.ScriptLoadedListener;
import com.poixson.commonmc.tools.scripting.loader.ScriptLoader;
import com.poixson.commonmc.tools.scripting.loader.ScriptSourceDAO;
import com.poixson.tools.CoolDown;
import com.poixson.tools.abstractions.xStartStop;


public class CraftScriptManager implements xStartStop {

	protected final AtomicReference<ScriptLoader> loader = new AtomicReference<ScriptLoader>(null);
	protected final AtomicReference<CraftScript>  script = new AtomicReference<CraftScript>(null);
	protected final AtomicReference<Thread>       thread = new AtomicReference<Thread>(null);

	// params
	protected final AtomicBoolean safe     = new AtomicBoolean(true);
	protected final AtomicBoolean threaded = new AtomicBoolean(false);
	protected final JavaPlugin plugin;
	protected final PrintStream out;

	protected final AtomicReference<Map<String, Object>> vars_out   = new AtomicReference<Map<String, Object>>(null);
	protected final ConcurrentHashMap<String, Object>    vars_in    = new ConcurrentHashMap<String, Object>();
	protected final ConcurrentHashMap<String, Object>    vars_start = new ConcurrentHashMap<String, Object>();
	protected final CopyOnWriteArraySet<String>          exports    = new CopyOnWriteArraySet<String>();
	protected final CopyOnWriteArraySet<String>          imports    = new CopyOnWriteArraySet<String>();

	protected final CoolDown reload_cool = new CoolDown("5s");

	protected final CopyOnWriteArrayList<ScriptLoadedListener> listeners_loaded = new CopyOnWriteArrayList<ScriptLoadedListener>();



//	public CraftScriptManager() {
//		this(null, null);
//	}
	public CraftScriptManager(final JavaPlugin plugin, final Location loc) {
		this.plugin = plugin;
		this.setVariable("plugin", plugin);
		if (loc == null) {
			this.out = null;
			this.setVariable("location_x", Integer.valueOf(0));
			this.setVariable("location_y", Integer.valueOf(0));
			this.setVariable("location_z", Integer.valueOf(0));
		} else {
			this.out = new PrintStream(new LocalOut(loc));
			this.setVariable("out", this.out);
			this.setVariable("location_x", loc.getBlockX());
			this.setVariable("location_y", loc.getBlockY());
			this.setVariable("location_z", loc.getBlockZ());
		}
		this.setVariable("reboot", Boolean.FALSE);
	}



	@Override
	public void start() {
		// threaded
		if (this.threaded.get()) {
			final Thread existing = this.thread.get();
			if (existing == null) {
				final Thread thread = new Thread() {
					@Override
					public void run() {
						CraftScriptManager.this.doStart();
					}
				};
				if (this.thread.compareAndSet(null, thread)) {
					final ScriptLoader loader = this.loader.get();
					// script loaded event
					(new ScriptLoadedEvent(this))
						.call(this.listeners_loaded.toArray(new ScriptLoadedListener[0]));
					thread.setName(thread.getName() + "-Script-" + loader.getName());
					thread.start();
				}
			}
		// not threaded
		} else {
			this.doStart();
		}
	}
	protected boolean doStart() {
		this.updateImportsExports();
		try {
			final boolean safe = this.safe.get();
			final CraftScript script = new CraftScript(this, safe);
			if (this.script.compareAndSet(null, script)) {
				final Iterator<Entry<String, Object>> it = this.vars_start.entrySet().iterator();
				while (it.hasNext()) {
					final Entry<String, Object> entry = it.next();
					this.setVariable(entry.getKey(), entry.getValue());
				}
				script.start();
				this.getScript()
					.runLoop();
				return true;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void stop() {
//TODO: which thread
//TODO: use stop action
		final CraftScript script = this.script.getAndSet(null);
		if (script != null)
			script.stop();
		this.thread.set(null);
	}

	public void reload() {
		final CraftScript script = this.script.getAndSet(null);
		if (script != null)
			script.stop();
		final ScriptLoader loader = this.loader.get();
		loader.reload();
		// script loaded event
		(new ScriptLoadedEvent(this))
			.call(this.listeners_loaded.toArray(new ScriptLoadedListener[0]));
		this.thread.set(null);
		this.start();
	}



	public CraftScript getScript() {
		return this.script.get();
	}



	public ScriptSourceDAO[] getSources() throws FileNotFoundException {
		final CraftScript script = this.script.get();
		if (script != null) {
			try {
				return script.getSources();
			} catch (FileNotFoundException e) {
				this.stop();
				throw e;
			}
		}
		return null;
	}



	public CraftScriptManager addAction(final String key, final Object...args) {
		this.checkState();
		final CraftScript script = this.getScript();
		if (script != null)
			script.addAction(key, args);
		return this;
	}

	public CraftScriptManager tick() {
		this.checkState();
		final CraftScript script = this.getScript();
		if (script != null)
			script.tick();
		return this;
	}



	// -------------------------------------------------------------------------------



	public ScriptLoader getLoader() {
		return this.loader.get();
	}
	public CraftScriptManager setLoader(final ScriptLoader loader) {
		this.loader.set(loader);
		this.updateImportsExports();
		return this;
	}

	public CraftScriptManager setSafeScope(final boolean safe) {
		this.safe.set(safe);
		return this;
	}

	public boolean isThreaded() {
		return this.threaded.get();
	}
	public CraftScriptManager setThreaded(final boolean threaded) {
		this.threaded.set(threaded);
		return this;
	}



	public boolean hasFlag(final String key) {
		final ScriptLoader loader = this.loader.get();
		return (loader == null ? false : loader.hasFlag(key));
	}
	public String getFlag(final String key) {
		final ScriptLoader loader = this.loader.get();
		return (loader == null ? null : loader.getFlag(key));
	}
	public int getFlagInt(final String key) {
		final String str = this.getFlag(key);
		return (str == null ? Integer.MIN_VALUE : Integer.parseInt(str));
	}
	public void updateImportsExports() {
		final ScriptLoader loader = this.loader.get();
		if (loader != null) {
			for (final String var : loader.getExports()) this.exports.add(var);
			for (final String var : loader.getImports()) this.imports.add(var);
		}
	}

	public String[] getImports() {
		return this.imports.toArray(new String[0]);
	}
	public String[] getExports() {
		return this.exports.toArray(new String[0]);
	}

	public boolean hasImport(final String key) {
		return this.imports.contains(key);
	}
	public boolean hasExport(final String key) {
		return this.exports.contains(key);
	}



	// -------------------------------------------------------------------------------
	// variables



	public Object getVariable(final String name) {
		if (this.threaded.get()) {
			final Map<String, Object> vars = this.vars_out.get();
			if (vars != null)
				return vars.get(name);
		} else {
			final CraftScript script = this.getScript();
			if (script != null)
				script.getVariable(name);
		}
		return null;
	}
	public CraftScriptManager setVariable(final String name, final Object value) {
		this.vars_start.put(name, value);
		if (this.threaded.get()) {
			this.vars_in.put(name, value);
		} else {
			final CraftScript script = this.getScript();
			if (script != null)
				script.setVariable(name, value);
		}
		return this;
	}



	public void checkState() {
		// reboot flagged
		final CraftScript script = this.script.get();
		if (script != null) {
			if (script.isRebooting()) {
				this.reload();
				return;
			}
		}
		// check modified files
		if (this.reload_cool.again()) {
			final ScriptLoader loader = this.loader.get();
			if (loader != null
			&&  loader.hasChanged()) {
				LOG.info(String.format("%sReloading script: %s", LOG_PREFIX, loader.getName()));
				this.reload();
				return;
			}
		}
	}



	// -------------------------------------------------------------------------------
	// event listeners



	public void register(final ScriptLoadedListener listener) {
		this.listeners_loaded.add(listener);
	}
	public void unregister(final ScriptLoadedListener listener) {
		this.listeners_loaded.remove(listener);
	}



}
