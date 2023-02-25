package com.poixson.commonmc.events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.PluginManager;

import com.poixson.commonmc.pxnCommonPlugin;
import com.poixson.commonmc.events.OutsideOfWorldEvent.Outside;
import com.poixson.commonmc.tools.plugin.xListener;


public class PlayerMoveManager extends xListener<pxnCommonPlugin> {

	public static final int WORLD_MIN_Y = -64;
	public static final int WORLD_MAX_Y = 319;



	public PlayerMoveManager(final pxnCommonPlugin plugin) {
		super(plugin);
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
				final OutsideOfWorldEvent eventOut =
					new OutsideOfWorldEvent(
						Outside.VOID,
						player,
						from, to
					);
				pm.callEvent(eventOut);
			} else
			// sky event
			if (to.getBlockY() > WORLD_MAX_Y) {
				final OutsideOfWorldEvent eventOut =
					new OutsideOfWorldEvent(
						Outside.SKY,
						player,
						from, to
					);
				pm.callEvent(eventOut);
			}
		}
	}



}
