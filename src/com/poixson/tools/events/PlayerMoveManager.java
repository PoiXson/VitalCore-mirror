package com.poixson.tools.events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.PluginManager;

import com.poixson.tools.events.OutsideOfWorldEvent.OutsideOfWorld;


public class PlayerMoveManager implements xListener {

	public static final int WORLD_MIN_Y = -64;
	public static final int WORLD_MAX_Y = 319;



	public PlayerMoveManager() {
	}



	@EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
	public void onPlayerMove(final PlayerMoveEvent event) {
		final Location from = event.getFrom();
		final Location to   = event.getTo();
		if ( (from.getBlockX() != to.getBlockX())
		||   (from.getBlockY() != to.getBlockY())
		||   (from.getBlockZ() != to.getBlockZ()) ) {
			final PluginManager pm = Bukkit.getPluginManager();
			final Player player = event.getPlayer();
			// normal move event
			final PlayerMoveNormalEvent eventNormal =
				new PlayerMoveNormalEvent(
					player,
					from, to
				);
			pm.callEvent(eventNormal);
			// void event
			if (to.getBlockY() < WORLD_MIN_Y) {
				pm.callEvent(
					new OutsideOfWorldEvent(
						player,
						from, to,
						OutsideOfWorld.VOID,
						WORLD_MIN_Y - to.getBlockY()
					)
				);
			} else
			// sky event
			if (to.getBlockY() > WORLD_MAX_Y) {
				pm.callEvent(
					new OutsideOfWorldEvent(
						player,
						from, to,
						OutsideOfWorld.SKY,
						to.getBlockY() - WORLD_MAX_Y
					)
				);
			}
		}
	}



}
