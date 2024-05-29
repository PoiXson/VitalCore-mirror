package com.poixson.tools.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class SaveEvent extends Event {

	private static final HandlerList handlers = new HandlerList();



	public SaveEvent() {
		super();
	}



	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	public static HandlerList getHandlerList() {
		return handlers;
	}



}
