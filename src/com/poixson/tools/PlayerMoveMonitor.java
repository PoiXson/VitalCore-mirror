package com.poixson.tools;

import static com.poixson.utils.BukkitUtils.SafeCancel;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
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
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.poixson.tools.events.AFKPlayerEvent;
import com.poixson.tools.events.OutsideOfWorldEvent;
import com.poixson.tools.events.OutsideOfWorldEvent.OutsideOfWorld;
import com.poixson.tools.events.PlayerMoveNormalEvent;


public class PlayerMoveMonitor implements xListener {

	public static final int WORLD_MIN_Y = -64;
	public static final int WORLD_MAX_Y = 319;

	protected final ConcurrentHashMap<UUID, LocationSafe> locations = new ConcurrentHashMap<UUID, LocationSafe>();

	protected final HashMap<UUID, Long> afk = new HashMap<UUID, Long>();

	protected final BukkitRunnable task_afk;
	protected final long afk_interval = 20L;  // 1 second
	protected final long afk_thresh   = 900L; // 15 minutes



	public PlayerMoveMonitor() {
		this.task_afk = new BukkitRunnable() {
			@Override
			public void run() {
				PlayerMoveMonitor.this.updateAFK();
			}
		};
	}



	@Override
	public void register(final JavaPlugin plugin) {
		xListener.super.register(plugin);
		this.task_afk.runTaskTimer(plugin, 40L, this.afk_interval);
	}
	@Override
	public void unregister() {
		xListener.super.unregister();
		SafeCancel(this.task_afk);
	}



	public LocationSafe getLocation(final Player player) {
		return this.getLocation(player.getUniqueId());
	}
	public LocationSafe getLocation(final UUID uuid) {
		return this.locations.get(uuid);
	}



	public HashMap<UUID, LocationSafe> snapshot() {
		final HashMap<UUID, LocationSafe> map = new HashMap<UUID, LocationSafe>();
		for (final Entry<UUID, LocationSafe> entry : this.locations.entrySet())
			map.put(entry.getKey(), entry.getValue());
		return map;
	}



	// -------------------------------------------------------------------------------
	// afk



	public void updateAFK() {
		//LOOP_AFK:
		for (final UUID uuid : this.afk.keySet()) {
			final Long sleeping = this.afk.get(uuid);
			if (sleeping != null) {
				final long slept = sleeping.longValue() + 1L;
				this.afk.put(uuid, Long.valueOf(slept));
				// go afk
				if (slept == this.afk_thresh) {
					final AFKPlayerEvent event = new AFKPlayerEvent(false, uuid);
					Bukkit.getPluginManager().callEvent(event);
				}
			}
		} // end LOOP_AFK
	}

	public void voidAFK(final UUID uuid) {
		final Long sleeping = this.afk.replace(uuid, Long.valueOf(0L));
		if (sleeping != null) {
			final long slept = sleeping.longValue();
			// go active
			if (slept >= this.afk_thresh) {
				final AFKPlayerEvent event = new AFKPlayerEvent(false, uuid);
				Bukkit.getPluginManager().callEvent(event);
			}
		}
	}



	// -------------------------------------------------------------------------------
	// listeners



	@EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
	public void onPlayerMove(final PlayerMoveEvent event) {
		final Player player = event.getPlayer();
		final UUID uuid = player.getUniqueId();
		this.voidAFK(uuid);
		final Location from = event.getFrom();
		final Location to   = event.getTo();
		if ( (from.getBlockX() != to.getBlockX())
		||   (from.getBlockY() != to.getBlockY())
		||   (from.getBlockZ() != to.getBlockZ()) ) {
			final PluginManager pm = Bukkit.getPluginManager();
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
		this.voidAFK(uuid);
		final LocationSafe loc = new LocationSafe(player.getLocation());
		this.locations.put(uuid, loc);
	}



	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
	public void onPlayerJoin(final PlayerJoinEvent event) {
		final Player player = event.getPlayer();
		final UUID uuid = player.getUniqueId();
		this.afk.put(uuid, Long.valueOf(0L));
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
			for (final UUID uuid : list) {
				this.afk.remove(uuid);
				this.locations.remove(uuid);
			}
		}
	}



}
