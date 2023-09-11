package com.poixson.commonmc.tools.scripting.events;

import java.util.concurrent.atomic.AtomicBoolean;

import com.poixson.commonmc.tools.scripting.screen.MapScreen;


public class ScreenFrameEvent {

	public final MapScreen screen;

	protected final AtomicBoolean cancelled = new AtomicBoolean(false);



	public ScreenFrameEvent(final MapScreen screen) {
		this.screen = screen;
	}



	public void call(final ScreenFrameListener[] listeners) {
		for (final ScreenFrameListener listener : listeners)
			listener.onFrame(this);
	}



}
