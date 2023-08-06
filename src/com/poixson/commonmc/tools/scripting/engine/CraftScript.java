package com.poixson.commonmc.tools.scripting.engine;

import static com.poixson.utils.Utils.GetMS;
import static com.poixson.utils.Utils.SafeClose;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;

import com.poixson.commonmc.tools.scripting.exceptions.JSFunctionNotFoundException;
import com.poixson.commonmc.tools.scripting.loader.ScriptLoader;
import com.poixson.commonmc.tools.scripting.loader.ScriptSourceDAO;
import com.poixson.tools.xTime;
import com.poixson.tools.abstractions.StaticKeyValue;
import com.poixson.tools.abstractions.xStartStop;
import com.poixson.utils.Utils;


public class CraftScript implements Runnable, xStartStop {
	public static final Logger LOG = Logger.getLogger("Minecraft");
	public static final String LOG_PREFIX = "[Script] ";
	protected static final AtomicBoolean inited = new AtomicBoolean(false);

	public static final long DEFAULT_STALE_TIMEOUT = xTime.ParseToLong("5m");
	public static final int  DEFAULT_PLAYER_DISTANCE = 10;

	protected final CraftScriptManager manager;
	protected final ScriptLoader loader;

	protected final boolean safe;

	protected final Scriptable scope;
	protected final Script[] compiled;

	protected final LinkedBlockingQueue<StaticKeyValue<String, Object[]>> actions = new LinkedBlockingQueue<StaticKeyValue<String, Object[]>>();

	protected final AtomicBoolean stopping = new AtomicBoolean(false);
	protected final AtomicLong last_used = new AtomicLong(0L);



	public CraftScript(final CraftScriptManager manager, final boolean safe)
			throws FileNotFoundException {
		if (inited.compareAndSet(false, true))
			ScriptContextFactory.init();
		this.manager = manager;
		this.loader  = manager.getLoader();
		this.safe    = safe;
		this.resetLastUsed();
		final ScriptSourceDAO[] sources = this.getSources();
		// top level scope
		{
			final Context context = Context.enter();
			try {
				if (safe) this.scope = context.initStandardObjects(null, true);
				else      this.scope = new ImporterTopLevel(context);
				this.scope.put("out",  this.scope, System.out);
				this.scope.put("stop", this.scope, Boolean.FALSE);
			} finally {
				SafeClose(context);
			}
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
				this.compiled = list.toArray(new Script[0]);
			} finally {
				SafeClose(context);
			}
		}
	}



	@Override
	public void start() {
		if (this.stopping.get()) return;
		this.resetLastUsed();
		this.run();
	}

	@Override
	public void stop() {
		this.stopping.set(true);
		this.setVariable("stop", Boolean.TRUE);
	}



	@Override
	public void run() {
		if (this.stopping.get()) return;
		this.resetLastUsed();
		// run script
		this.pushVars();
		try {
			final Context context = Context.enter();
			try {
				for (final Script src : this.compiled) {
					src.exec(context, this.scope);
					this.resetLastUsed();
				}
			} catch (Exception e) {
				this.stop();
				e.printStackTrace();
				return;
			} finally {
				SafeClose(context);
				this.resetLastUsed();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.pullVars();
	}
	public void runLoop() {
		RUN_LOOP:
		while (!this.stopping.get()) {
			try {
				final StaticKeyValue<String, Object[]> entry = this.actions.poll(100L, TimeUnit.MILLISECONDS);
				if (entry != null) {
					KEY_SWITCH:
					switch (entry.key) {
					case "loop": this.call("loop");                           break KEY_SWITCH;
					default: {
						final LinkedList<Object> args = new LinkedList<Object>();
						args.add(entry.key);
						for (final Object arg : entry.value)
							args.add(arg);
						this.call("action", args.toArray(new Object[0]));
						break KEY_SWITCH;
					}
					}
				}
			} catch (JSFunctionNotFoundException ignore) {
				continue RUN_LOOP;
			} catch (InterruptedException e) {
				break RUN_LOOP;
			} catch (Exception e) {
				e.printStackTrace();
				this.stop();
				break RUN_LOOP;
			}
		}
	}

	public Object call(final String funcName, final Object...args)
			throws JSFunctionNotFoundException {
		if (this.stopping.get()) return null;
		this.resetLastUsed();
		if (Utils.isEmpty(funcName)) throw new RuntimeException("Cannot call function, no name provided");
		this.pushVars();
		final Context context = Context.enter();
		final Object result;
		try {
			final Object funcObj = this.scope.get(funcName, this.scope);
			if (  funcObj == null             ) throw new JSFunctionNotFoundException(this.loader.getName(), funcName, funcObj);
			if (!(funcObj instanceof Function)) throw new JSFunctionNotFoundException(this.loader.getName(), funcName, funcObj);
			final Function func = (Function) funcObj;
			result = func.call(context, this.scope, this.scope, args);
		} catch (JSFunctionNotFoundException e) {
			throw e;
		} catch (Exception e) {
			this.stop();
			e.printStackTrace();
			return e;
		} finally {
			SafeClose(context);
			this.resetLastUsed();
		}
		this.pullVars();
		return result;
	}



	public ScriptSourceDAO[] getSources() throws FileNotFoundException {
		if (this.stopping.get()) return null;
		try {
			return this.loader.getSources();
		} catch (FileNotFoundException e) {
			this.stop();
			throw e;
		}
	}



	public void addAction(final String key, final Object...args) {
		final int size = this.actions.size();
		if (size > 0 && size % 100 == 0)
			throw new RuntimeException("Script actions queue overloaded! " + Integer.toString(size));
		this.actions.offer(new StaticKeyValue<String, Object[]>(key, args));
	}

	public void tick() {
		this.addAction("loop");
	}



	// -------------------------------------------------------------------------------
	// variables



	public Object getVariable(final String name) {
		return this.scope.get(name, this.scope);
	}
	public void setVariable(final String name, final Object value) {
		this.scope.put(name, this.scope, value);
	}



	// push variables to script
	protected void pushVars() {
		final LinkedList<String> removing = new LinkedList<String>();
		final Iterator<Entry<String, Object>> it = this.manager.vars_in.entrySet().iterator();
		while (it.hasNext()) {
			final Entry<String, Object> entry = it.next();
			this.setVariable(entry.getKey(), entry.getValue());
			removing.add(entry.getKey());
		}
		for (final String key : removing)
			this.manager.vars_in.remove(key);
	}

	// pull variables from script
	protected void pullVars() {
		final ConcurrentMap<String, Object> vars_out = new ConcurrentHashMap<String, Object>();
		for (final String key : this.manager.exports)
			vars_out.put(key, ConvertThreadSafe(this.getVariable(key)));
		this.manager.vars_out.set(vars_out);
	}



	// -------------------------------------------------------------------------------
	// stale



	public void resetLastUsed() {
		this.last_used.set(GetMS());
	}

//TODO: use this
	public boolean isStale() {
		return this.isStale(GetMS());
	}
	public boolean isStale(final long time) {
		final long since = this.getSinceLastUsed(time);
		if (since == -1L)
			return false;
		return (since > DEFAULT_STALE_TIMEOUT);
	}

	public long getSinceLastUsed() {
		return this.getSinceLastUsed(GetMS());
	}
	public long getSinceLastUsed(final long time) {
		final long last = this.last_used.get();
		return (last > 0L ? time-last : -1L);
	}



	public static Object ConvertThreadSafe(final Object obj) {
		final String type = obj.getClass().getName();
		TYPE_SWITCH:
		switch (type) {
		// int[][]
		case "[[I": {
			final LinkedTransferQueue<LinkedTransferQueue<Integer>> list =
					new LinkedTransferQueue<LinkedTransferQueue<Integer>>();
			final int[][] array = (int[][]) obj;
			for (final int[] arr : array) {
				final LinkedTransferQueue<Integer> lst = new LinkedTransferQueue<Integer>();
				for (final int entry : arr)
					lst.add(Integer.valueOf(entry));
				list.add(lst);
			}
			return list;
		}
		// Color[][]
		case "[[Ljava.awt.Color;": {
			final LinkedTransferQueue<LinkedTransferQueue<Integer>> list =
					new LinkedTransferQueue<LinkedTransferQueue<Integer>>();
			final Color[][] array = (Color[][]) obj;
			for (final Color[] arr : array) {
				final LinkedTransferQueue<Integer> lst = new LinkedTransferQueue<Integer>();
				for (final Color entry : arr)
					lst.add(Integer.valueOf(entry.getRGB()));
				list.add(lst);
			}
			return list;
		}
		// unhandled type
		default:
			LOG.warning(String.format("%sUnhandled export variable type: %s", LOG_PREFIX, type));
			break TYPE_SWITCH;
		}
		return obj;
	}



}
