package com.poixson.pluginlib.tools.scripting.events;

import java.util.concurrent.atomic.AtomicBoolean;

import com.poixson.scripting.xScriptManager;


public class ScriptLoadedEvent {

	public final CraftScriptManager manager;

	protected final AtomicBoolean cancelled = new AtomicBoolean(false);



	public ScriptLoadedEvent(final CraftScriptManager manager) {
		this.manager = manager;
	}



	public void call(final ScriptLoadedListener[] listeners) {
		for (final ScriptLoadedListener listener : listeners)
			listener.onLoaded(this);
	}



}
