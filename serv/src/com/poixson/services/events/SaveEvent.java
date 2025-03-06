package com.poixson.tools.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class SaveEvent extends Event {

	private static final HandlerList handlers = new HandlerList();

	public final long time_since_last;



	public SaveEvent(final long time_since_last) {
		super();
		this.time_since_last = time_since_last;
	}



	public long getTimeSinceLast() {
		return this.time_since_last;
	}



	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	public static HandlerList getHandlerList() {
		return handlers;
	}



}
