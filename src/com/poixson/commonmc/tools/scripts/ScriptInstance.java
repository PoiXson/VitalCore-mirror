package com.poixson.commonmc.tools.scripts;

import static com.poixson.utils.Utils.GetMS;
import static com.poixson.utils.Utils.SafeClose;

import java.io.Closeable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;

import com.poixson.commonmc.tools.scripts.exceptions.JSFunctionNotFoundException;
import com.poixson.commonmc.tools.scripts.loader.ScriptLoader;
import com.poixson.tools.xTime;
import com.poixson.utils.Utils;


public class ScriptInstance implements Closeable {

	protected final CraftScript craftscript;
	protected final ScriptLoader loader;
	protected final Script[] compiled;
	protected final Scriptable scope;

	protected static final long STALE_TIMEOUT = xTime.ParseToLong("5m");
	protected final AtomicLong lastUsed = new AtomicLong(0L);

	protected final AtomicBoolean crashed = new AtomicBoolean(false);



	public ScriptInstance(final CraftScript craftscript, final ScriptLoader loader,
			final Scriptable scope, final Script[] compiled) {
		this.craftscript = craftscript;
		this.loader      = loader;
		this.compiled    = compiled;
		final Context context = Context.enter();
		try {
			this.scope = context.newObject(scope);
			this.scope.setPrototype(scope);
			this.scope.setParentScope(null);
		} finally {
			SafeClose(context);
		}
		this.resetLastUsed();
	}



	@Override
	public void close() {
		this.setCrashed();
	}



	public Object run() {
		if (this.isCrashed())
			return null;
		this.resetLastUsed();
		final Context context = Context.enter();
		Object result = null;
		Object res;
		try {
			for (final Script src : this.compiled) {
				res = src.exec(context, this.scope);
				if (res != null) {
					if (result == null)
						result = res;
				}
				this.resetLastUsed();
			}
			return result;
		} catch (Exception e) {
			this.setCrashed();
			e.printStackTrace();
			return e;
		} finally {
			SafeClose(context);
		}
	}

	public Object call(final String funcName, final Object...args) {
		if (this.isCrashed())
			return null;
		if (Utils.isEmpty(funcName)) throw new RuntimeException("Cannot call function, no name provided");
		this.resetLastUsed();
		final Context context = Context.enter();
		try {
			final Object funcObj = this.scope.get(funcName, this.scope);
			if (funcObj == null) throw new JSFunctionNotFoundException(this.loader.getName(), funcName, funcObj);
			final Function func = (Function) funcObj;
			return func.call(context, this.scope, this.scope, args);
		} catch (Exception e) {
			this.setCrashed();
			e.printStackTrace();
			return e;
		} finally {
			SafeClose(context);
		}
	}



	public void resetLastUsed() {
		this.lastUsed.set(GetMS());
	}

	public boolean isStale() {
		return this.isStale(GetMS());
	}
	public boolean isStale(final long time) {
		final long since = this.getSinceLastUsed(time);
		if (since == -1L)
			return false;
		return (since > STALE_TIMEOUT);
	}

	public long getSinceLastUsed() {
		return this.getSinceLastUsed(GetMS());
	}
	public long getSinceLastUsed(final long time) {
		final long last = this.lastUsed.get();
		if (last <= 0L)
			return -1L;
		return time - last;
	}



	public boolean isCrashed() {
		return this.crashed.get();
	}
	public boolean setCrashed() {
		return this.setCrashed(true);
	}
	public boolean setCrashed(final boolean crashed) {
		return this.crashed.getAndSet(crashed);
	}



}
