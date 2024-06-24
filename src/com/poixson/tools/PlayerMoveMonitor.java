package com.poixson.tools;

import java.util.LinkedList;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;

import com.poixson.tools.events.OutsideOfWorldEvent;
import com.poixson.tools.events.OutsideOfWorldEvent.OutsideOfWorld;
import com.poixson.tools.events.PlayerMoveNormalEvent;


public class PlayerMoveMonitor implements xListener {

	public static final int WORLD_MIN_Y = -64;
	public static final int WORLD_MAX_Y = 319;

	public final ConcurrentHashMap<UUID, LocationSafe> locations = new ConcurrentHashMap<UUID, LocationSafe>();



	public PlayerMoveMonitor() {
	}



	public LocationSafe getLocation(final Player player) {
		return this.getLocation(player.getUniqueId());
	}
	public LocationSafe getLocation(final UUID uuid) {
		return this.locations.get(uuid);
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
			final UUID uuid = player.getUniqueId();
			{
				final LocationSafe loc = this.locations.get(uuid);
				if (loc == null) {
					this.locations.put(uuid, new LocationSafe(to.getWorld(), to.getX(), to.getY(), to.getZ()));
				} else {
					loc.setXYZ(to.getX(), to.getY(), to.getZ());
				}
			}
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

	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
	public void onPlayerChangeWorld(final PlayerChangedWorldEvent event) {
		final Player player = event.getPlayer();
		final UUID uuid = player.getUniqueId();
		final LocationSafe loc = new LocationSafe(player.getLocation());
		this.locations.put(uuid, loc);
	}



	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
	public void onPlayerJoin(final PlayerJoinEvent event) {
		final Player player = event.getPlayer();
		final UUID uuid = player.getUniqueId();
		this.locations.put(uuid, new LocationSafe(player.getLocation()));
	}

	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
	public void onPlayerLeave(final PlayerQuitEvent event) {
		this.locations.remove(event.getPlayer().getUniqueId());
		{
			final LinkedList<UUID> list = new LinkedList<UUID>();
			for (final UUID uuid : this.locations.keySet())
				list.add(uuid);
			for (final Player player : Bukkit.getOnlinePlayers())
				list.remove(player.getUniqueId());
			for (final UUID uuid : list)
				this.locations.remove(uuid);
		}
	}



}
