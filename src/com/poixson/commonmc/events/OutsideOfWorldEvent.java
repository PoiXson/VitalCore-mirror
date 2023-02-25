package com.poixson.commonmc.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerMoveEvent;


public class OutsideOfWorldEvent extends PlayerMoveEvent {

	private static final HandlerList handlers = new HandlerList();

	private boolean cancel = false;

	protected final Outside outside;



	public enum Outside {
		SKY,
		VOID
	}



	public OutsideOfWorldEvent(final Outside outside, final Player player,
			final Location from, final Location to) {
		super(player, from, to);
		this.outside = outside;
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
		return this.cancel;
	}
	@Override
	public void setCancelled(final boolean cancel) {
		this.cancel = cancel;
	}



	public Outside getOutsideWhere() {
		return this.outside;
	}



}
