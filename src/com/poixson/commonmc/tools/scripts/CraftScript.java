package com.poixson.commonmc.tools.scripts;

import static com.poixson.commonmc.pxnCommonPlugin.LOG_PREFIX;
import static com.poixson.utils.Utils.SafeClose;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;

import com.poixson.commonmc.tools.scripts.loader.ScriptLoader;
import com.poixson.commonmc.tools.scripts.loader.ScriptSourceDAO;
import com.poixson.tools.CoolDown;


public class CraftScript {
	protected static final Logger LOG = Logger.getLogger("Minecraft");

	protected final ScriptLoader loader;
	protected final Scriptable scope;

	protected final ConcurrentHashMap<Thread, ScriptInstance> instances = new ConcurrentHashMap<Thread, ScriptInstance>();
	protected final AtomicReference<Script[]> compiled = new AtomicReference<Script[]>(null);

	protected final CoolDown reloadCool = new CoolDown("5s");



	private static final AtomicBoolean inited = new AtomicBoolean(false);
	static {
		if (inited.compareAndSet(false, true))
			pxnContextFactory.init();
	}



	public CraftScript(final ScriptLoader loader) {
		this(loader, true);
	}
	public CraftScript(final ScriptLoader loader, final boolean safe) {
		this.loader = loader;
		// top level scope
		{
			final Context context = Context.enter();
			try {
				if (safe) this.scope = context.initStandardObjects(null, true);
				else      this.scope = new ImporterTopLevel(context);
				this.scope.put("out", this.scope, System.out);
			} finally {
				SafeClose(context);
			}
		}
	}



	public Object getVariable(final String name) {
		return this.scope.get(name, this.scope);
	}
	public void setVariable(final String name, final Object value) {
		this.scope.put(name, this.scope, value);
	}



	public void run() {
		this.getScriptInstance();
	}
	public Object call(final String funcName, final Object...args) {
		final ScriptInstance instance = this.getScriptInstance();
		return instance.call(funcName, args);
	}

	public ScriptInstance getScriptInstance() {
		// check modified files
		synchronized (this.reloadCool) {
			if (this.reloadCool.again()
			&&  this.loader.hasChanged()) {
				LOG.info(String.format("%sReloading script: %s", LOG_PREFIX, this.loader.getName()));
				this.reload();
			}
		}
		// script instance
		{
			final Thread thread = Thread.currentThread();
			// existing instance
			{
				final ScriptInstance script = this.instances.get(thread);
				if (script != null)
					return script;
			}
			// new instance
			{
				ScriptSourceDAO[] sources;
				try {
					sources = this.loader.getSources();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					return null;
				}
				final Script[] compiled = this.getCompiledScripts(sources);
				final ScriptInstance instance =
					new ScriptInstance(
						this,
						this.loader,
						this.scope,
						compiled
					);
				final ScriptInstance existing = this.instances.putIfAbsent(thread, instance);
				if (existing == null) {
					// initial run
					instance.run();
					return instance;
				}
				return existing;
			}
		}
	}

	protected Script[] getCompiledScripts(final ScriptSourceDAO[] sources) {
		// existing
		{
			final Script[] compiled = this.compiled.get();
			if (compiled != null)
				return compiled;
		}
		// compile
		{
			final Context context = Context.enter();
			context.setOptimizationLevel(9);
			context.setLanguageVersion(Context.VERSION_ES6);
			try {
				final LinkedList<Script> list = new LinkedList<Script>();
				for (final ScriptSourceDAO src : sources) {
					final Script script = context.compileString(src.code, src.filename, 1, null);
					list.add(script);
				}
				final Script[] compiled = list.toArray(new Script[0]);
				if (this.compiled.compareAndSet(null, compiled))
					return compiled;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} finally {
				SafeClose(context);
			}
		}
		return this.getCompiledScripts(sources);
	}



	public void reload() {
		synchronized (this.instances) {
			this.reloadCool.reset();
			this.loader.reload();
			this.compiled.set(null);
			// unload instances
			final Iterator<Thread> it = this.instances.keySet().iterator();
			while (it.hasNext()) {
				final Thread key = it.next();
				final ScriptInstance instance = this.instances.remove(key);
				SafeClose(instance);
			}
		}
	}



}
