package com.poixson.tools.events;

import java.util.concurrent.atomic.AtomicBoolean;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerMoveEvent;


public class OutsideOfWorldEvent extends PlayerMoveEvent {

	private static final HandlerList handlers = new HandlerList();

	protected final AtomicBoolean cancel = new AtomicBoolean(false);

	protected final OutsideOfWorld outside;
	protected final int distance;



	public static enum OutsideOfWorld {
		SKY,
		VOID
	}



	public OutsideOfWorldEvent(final Player player,
			final Location from, final Location to,
			final OutsideOfWorld outside, final int distance) {
		super(player, from, to);
		this.outside  = outside;
		this.distance = distance;
	}



	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	public static HandlerList getHandlerList() {
		return handlers;
	}



	@Override
	public boolean isCancelled() {
		return this.cancel.get();
	}
	@Override
	public void setCancelled(final boolean cancel) {
		this.cancel.set(cancel);
	}
	public void setCancelled() {
		this.setCancelled(true);
	}



	public OutsideOfWorld getOutsideWhere() {
		return this.outside;
	}



	public int getOutsideDistance() {
		return this.distance;
	}



}
