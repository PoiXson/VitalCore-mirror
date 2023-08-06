package com.poixson.commonmc.tools.scripting.engine;

//import static com.poixson.commonmc.tools.scripting.engine.CraftScript.LOG;
//import static com.poixson.commonmc.tools.scripting.engine.CraftScript.LOG_PREFIX;

import java.io.FileNotFoundException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

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

	protected final AtomicReference<Map<String, Object>> vars_out = new AtomicReference<Map<String, Object>>(null);
	protected final ConcurrentHashMap<String, Object>    vars_in  = new ConcurrentHashMap<String, Object>();
	protected final CopyOnWriteArraySet<String>          exports  = new CopyOnWriteArraySet<String>();
	protected final CopyOnWriteArraySet<String>          imports  = new CopyOnWriteArraySet<String>();

	protected final CoolDown reload_cool = new CoolDown("5s");



	public CraftScriptManager() {
	}



	@Override
	public void start() {
		// threaded
		if (this.threaded.get()) {
			final Thread thread = new Thread() {
				@Override
				public void run() {
					CraftScriptManager.this.doStart();
					CraftScriptManager.this.getScript()
						.runLoop();
				}
			};
			if (this.thread.compareAndSet(null, thread)) {
				final ScriptLoader loader = this.loader.get();
				thread.setName(thread.getName() + "-Script-" + loader.getName());
				thread.start();
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
				script.start();
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
	}

	public void reload() {
		final CraftScript script = this.script.getAndSet(null);
		if (script != null)
			this.stop();
		this.start();
	}



	public CraftScript getScript() {
//		if (this.stopping.get()) return null;
		return this.script.get();
	}



	public ScriptSourceDAO[] getSources() throws FileNotFoundException {
//		if (this.stopping.get()) return null;
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
//		if (this.stopping.get()) return this;
		final CraftScript script = this.getScript();
		if (script != null)
			script.addAction(key, args);
		return this;
	}

	public CraftScriptManager tick() {
//		if (this.stopping.get()) return this;
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
		if (this.threaded.get()) {
			this.vars_in.put(name, value);
		} else {
			final CraftScript script = this.getScript();
			if (script != null)
				script.setVariable(name, value);
		}
		return this;
	}



}
/*
//TODO: use this
	public void checkState() {
		if (this.stopping.get()) return;
		// check modified files
		synchronized (this.reload_cool) {
			if (this.reload_cool.again()) {
				final ScriptLoader loader = this.loader.get();
				if (loader != null) {
					if (loader.hasChanged()) {
						LOG.info(String.format("%sReloading script: %s", LOG_PREFIX, loader.getName()));
						this.reload();
						return;
					}
				}
			}
		}
	}
*/
