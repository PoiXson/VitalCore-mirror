package com.poixson.commonmc.tools.tps;

import static com.poixson.commonmc.tools.tps.TicksPerSecond.DisplayTPS;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.poixson.commonmc.pxnCommonPlugin;
import com.poixson.tools.abstractions.xStartStop;


public class TicksAnnouncer extends BukkitRunnable implements xStartStop {

	protected static final ConcurrentHashMap<UUID, TicksAnnouncer> instances =
			new ConcurrentHashMap<UUID, TicksAnnouncer>();

	protected final pxnCommonPlugin plugin;
	protected final Player player;



	public static TicksAnnouncer Start(final pxnCommonPlugin plugin, final Player player) {
		final UUID uuid = player.getUniqueId();
		// existing instance
		{
			final TicksAnnouncer announcer = instances.get(uuid);
			if (announcer != null)
				return announcer;
		}
		// new instance
		{
			final TicksAnnouncer announcer = new TicksAnnouncer(plugin, player);
			final TicksAnnouncer existing =
					instances.putIfAbsent(uuid, announcer);
			if (existing == null) {
				announcer.start();
				return announcer;
			}
			return existing;
		}
	}
	public static boolean Stop(final Player player) {
		final TicksAnnouncer announcer = instances.remove(player.getUniqueId());
		if (announcer != null) {
			announcer.stop();
			return true;
		}
		return false;
	}
	public static TicksAnnouncer Toggle(final pxnCommonPlugin plugin, final Player player) {
		if (Stop(player))
			return null;
		return Start(plugin, player);
	}



	public TicksAnnouncer(final pxnCommonPlugin plugin, final Player player) {
		this.plugin = plugin;
		this.player = player;
	}



	@Override
	public void start() {
		this.runTaskTimer(this.plugin, 20L, 20L);
	}
	@Override
	public void stop() {
		try {
			this.cancel();
		} catch (IllegalStateException ignore) {}
	}



	@Override
	public void run() {
		final TicksPerSecond manager = pxnCommonPlugin.GetTicksManager();
		final double[] tps = manager.getTPS();
		DisplayTPS(this.player.getUniqueId(), tps);
	}



}
