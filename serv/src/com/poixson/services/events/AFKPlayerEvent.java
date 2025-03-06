package com.poixson.tools.events;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class AFKPlayerEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	protected final AtomicBoolean cancel = new AtomicBoolean(false);

	protected final boolean isAFK;

	protected final UUID uuid;



	public AFKPlayerEvent(final boolean isAFK, final UUID uuid) {
		this.isAFK = isAFK;
		this.uuid  = uuid;
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



	public boolean isAFK() {
		return this.isAFK;
	}
	public boolean isActive() {
		return ! this.isAFK;
	}



	public Player getPlayer() {
		return Bukkit.getPlayer(this.uuid);
	}
	public UUID getUUID() {
		return this.uuid;
	}



}
