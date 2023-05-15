package com.poixson.commonmc.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class PluginSaveEvent extends Event {

	private static final HandlerList handlers = new HandlerList();



	public PluginSaveEvent() {
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
