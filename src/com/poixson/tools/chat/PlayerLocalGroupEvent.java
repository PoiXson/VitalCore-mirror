package com.poixson.tools.chat;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class PlayerLocalGroupEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	public final String channel;
	public final UUID   uuid;

	public final LocalNearFar isNearFar;
	public enum LocalNearFar {
		NEAR,
		FAR
	}

	protected final AtomicBoolean cancelled = new AtomicBoolean(false);



	public PlayerLocalGroupEvent(final String channel,
			final UUID uuid, final LocalNearFar isNearFar) {
		this.channel   = channel;
		this.uuid      = uuid;
		this.isNearFar = isNearFar;
	}



	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	public static HandlerList getHandlerList() {
		return handlers;
	}



	public Player getPlayer() {
		return Bukkit.getPlayer(this.uuid);
	}



	public boolean isJoinGroup() {
		return LocalNearFar.NEAR.equals(this.isNearFar);
	}
	public boolean isLeaveGroup() {
		return LocalNearFar.FAR.equals(this.isNearFar);
	}



	@Override
	public boolean isCancelled() {
		return this.cancelled.get();
	}
	@Override
	public void setCancelled(final boolean cancel) {
		this.cancelled.set(true);
	}
	public void setCancelled() {
		this.setCancelled(true);
	}



}
