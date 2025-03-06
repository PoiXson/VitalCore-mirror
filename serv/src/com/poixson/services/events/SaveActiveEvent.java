package com.poixson.tools.events;

import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class SaveActiveEvent extends Event {

	private static final HandlerList handlers = new HandlerList();

	public final long time_now;
	public final long time_last;

	public final World world;



	public SaveActiveEvent(final long time_now, final long time_last, final World world) {
		this.time_now  = time_now;
		this.time_last = time_last;
		this.world     = world;
	}



	public long getTimeNow() {
		return this.time_now;
	}
	public long getTimeLast() {
		return this.time_last;
	}
	public long getTimeSinceLast() {
		return this.time_now - this.time_last;
	}



	public World getWorld() {
		return this.world;
	}



	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	public static HandlerList getHandlerList() {
		return handlers;
	}



}
