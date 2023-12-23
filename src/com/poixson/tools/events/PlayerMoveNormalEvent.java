package com.poixson.tools.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerMoveEvent;


public class PlayerMoveNormalEvent extends PlayerMoveEvent {

	private static final HandlerList handlers = new HandlerList();

	private boolean cancel = false;



	public PlayerMoveNormalEvent(final Player player,
			final Location from, final Location to) {
		super(player, from, to);
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



}
