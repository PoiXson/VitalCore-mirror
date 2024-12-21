package com.poixson.tools.chat;

import static com.poixson.utils.BukkitUtils.EqualsUUID;
import static com.poixson.utils.LocationUtils.DistanceVectorial;

import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.poixson.pluginlib.pxnPluginLib;
import com.poixson.tools.LocationSafe;
import com.poixson.tools.PlayerMoveMonitor;
import com.poixson.tools.xListener;
import com.poixson.tools.xRand;
import com.poixson.tools.abstractions.xStartStop;
import com.poixson.tools.chat.PlayerLocalGroupEvent.LocalNearFar;
import com.poixson.tools.events.PlayerMoveNormalEvent;
import com.poixson.utils.BukkitUtils;


public class PlayerLocalGroups implements xListener, xStartStop {

	protected final pxnPluginLib plugin;
	protected final PlayerMoveMonitor monitor;

	protected final AtomicReference<PlayerGroupDAO[]> groups = new AtomicReference<PlayerGroupDAO[]>(null);

	public final double range_low;
	public final double range_high;

	protected final xRand random = (new xRand()).seed_time();

	protected final AtomicBoolean update = new AtomicBoolean(true);
	protected final AtomicReference<BukkitRunnable> run = new AtomicReference<BukkitRunnable>(null);



	public PlayerLocalGroups(final pxnPluginLib plugin,
			final double range, final double grace) {
		this.plugin  = plugin;
		this.monitor = plugin.getPlayerMoveMonitor();
		this.range_low  = range;
		this.range_high = range + grace;
	}



	@Override
	public void start() {
		if (this.run.get() == null) {
			final BukkitRunnable run = new BukkitRunnable() {
				@Override
				public void run() {
					if (PlayerLocalGroups.this.update.getAndSet(false))
						PlayerLocalGroups.this.doUpdate();
				}
			};
			if (this.run.compareAndSet(null, run)) {
				run.runTaskTimerAsynchronously(this.plugin, 20L, 20L);
				xListener.super.register(this.plugin);
			}
		}
	}
	@Override
	public void stop() {
		xListener.super.unregister();
		final BukkitRunnable run = this.run.getAndSet(null);
		if (run != null)
			BukkitUtils.SafeCancel(run);
	}



	public void update() {
		this.update.set(true);
	}
	protected void doUpdate() {
		final LinkedList<PlayerGroupDAO> groups = new LinkedList<PlayerGroupDAO>();
		final LinkedList<PlayerLocalGroupEvent> events = new LinkedList<PlayerLocalGroupEvent>();
		// clone groups
		{
			final PlayerGroupDAO[] array = this.groups.get();
			if (array != null) {
				for (final PlayerGroupDAO dao : array)
					groups.add(dao);
			}
		}
		final Map<UUID, LocationSafe> online = this.monitor.snapshot();
		//LOOP_ONLINE:
		for (final Entry<UUID, LocationSafe> entry_online : online.entrySet()) {
			final UUID        uuid = entry_online.getKey();
			final LocationSafe loc = entry_online.getValue();
			PlayerGroupDAO group = null;
			// current group
			LOOP_GROUPS:
			for (final PlayerGroupDAO dao : groups) {
				if (dao.players.contains(uuid)) {
					group = dao;
					break LOOP_GROUPS;
				}
			}
			boolean found = false;
			for (final Entry<UUID, LocationSafe> entry_online2 : online.entrySet()) {
				final UUID        uuid2 = entry_online2.getKey();
				final LocationSafe loc2 = entry_online2.getValue();
				if (!EqualsUUID(uuid, uuid2)) {
					final double distance = DistanceVectorial(loc, loc2);
					// add to group
					if (distance < this.range_low) {
						if (group == null) {
							group = this.getNewGroup(groups);
							group.players.add(uuid);
							groups.add(group);
							events.add(
								new PlayerLocalGroupEvent(group.channel, uuid, LocalNearFar.NEAR)
							);
System.out.println("NEAR: "+uuid.toString()+" "+Bukkit.getPlayer(uuid).getName());
						}
						if (group.players.add(uuid2)) {
							events.add(
								new PlayerLocalGroupEvent(group.channel, uuid2, LocalNearFar.NEAR)
							);
System.out.println("NEAR: "+uuid2.toString()+" "+Bukkit.getPlayer(uuid2).getName());
						}
					}
					if (distance < this.range_high)
						found = true;
				}
			}
			// remove from group
			if (!found && group != null) {
				events.add(
					new PlayerLocalGroupEvent(group.channel, uuid, LocalNearFar.FAR)
				);
				group.players.remove(uuid);
System.out.println("FAR: "+uuid.toString()+" "+Bukkit.getPlayer(uuid).getName());
				if (group.players.isEmpty())
					groups.remove(group);
			}
		} // end LOOP_ONLINE
		for (final PlayerGroupDAO dao : groups)
			dao.updateCenter(online);
		this.groups.set(groups.toArray(new PlayerGroupDAO[0]));
		// trigger events
		if (!events.isEmpty()) {
			final BukkitRunnable run = new BukkitRunnable() {
				private final AtomicReference<PlayerLocalGroupEvent[]> events = new AtomicReference<PlayerLocalGroupEvent[]>(null);
				public BukkitRunnable init(final PlayerLocalGroupEvent[] events) {
					this.events.set(events);
					return this;
				}
				@Override
				public void run() {
					final PlayerLocalGroupEvent[] events = this.events.get();
					// far
					//LOOP_FAR:
					for (final PlayerLocalGroupEvent event : events) {
						if (event.isLeaveGroup()) {
							// ignore if joining a group
							LOOP_NEAR:
							for (final PlayerLocalGroupEvent evt : events) {
								if (event.uuid.equals(evt.uuid)
								&&  evt.isJoinGroup()) {
									event.setCancelled();
									break LOOP_NEAR;
								}
							}
							Bukkit.getPluginManager().callEvent(event);
						}
					}
					// near
					for (final PlayerLocalGroupEvent event : events) {
						if (event.isJoinGroup())
							Bukkit.getPluginManager().callEvent(event);
					}
				}
			}.init(events.toArray(new PlayerLocalGroupEvent[0]));
			run.runTask(this.plugin);
		}
	}



	protected PlayerGroupDAO getNewGroup(final LinkedList<PlayerGroupDAO> groups) {
		final String channel = this.getNewGroupName(groups);
		return new PlayerGroupDAO(channel);
	}
	protected String getNewGroupName(final LinkedList<PlayerGroupDAO> groups) {
		int i = 0;
		LOOP_CHANNEL:
		while (i < 1000) {
			final String channel = "ch" + this.random.nextInt(i, i+10);
			for (final PlayerGroupDAO dao : groups) {
				if (channel.equals(dao.channel)) {
					i++;
					continue LOOP_CHANNEL;
				}
			}
			return channel;
		}
		throw new RuntimeException("Failed to find a new unique channel name");
	}

	public PlayerGroupDAO getGroup(final UUID uuid) {
		final PlayerGroupDAO[] groups = this.groups.get();
		if (groups != null) {
			for (final PlayerGroupDAO group : groups) {
				if (group.players.contains(uuid))
					return group;
			}
		}
		return null;
	}



	// -------------------------------------------------------------------------------
	// listeners



	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
	public void onPlayerMoveNormal(final PlayerMoveNormalEvent event) {
		this.update();
	}
	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
	public void onPlayerChangeWorld(final PlayerChangedWorldEvent event) {
		this.update();
	}



	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
	public void onPlayerJoin(final PlayerJoinEvent event) {
		this.update();
	}
	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
	public void onPlayerLeave(final PlayerQuitEvent event) {
		this.update();
	}



}
