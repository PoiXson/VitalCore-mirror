package com.poixson.pluginlib.tools.updatechecker;

import static com.poixson.pluginlib.pxnPluginLib.LOG_PREFIX;
import static com.poixson.pluginlib.tools.plugin.xJavaPlugin.LOG;

import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.poixson.pluginlib.pxnPluginLib;
import com.poixson.pluginlib.tools.plugin.xJavaPlugin;
import com.poixson.tools.xTime;
import com.poixson.tools.abstractions.xStartStop;
import com.poixson.utils.ThreadUtils;
import com.poixson.utils.Utils;


public class UpdateCheckManager extends BukkitRunnable implements xStartStop {

	protected final pxnPluginLib plugin;

	protected final long delay  = (new xTime( "5s")).ticks(50L);
	protected final long loop   = (new xTime( "5m")).ticks(50L);
	protected final long period = (new xTime("12h")).ms();

	protected final AtomicLong lastCheck = new AtomicLong(0L);

	protected final ConcurrentHashMap<Integer, UpdateCheckerTask> checkers = new ConcurrentHashMap<Integer, UpdateCheckerTask>();

	protected final PlayerJoinListener listenerPlayerJoin;



	public UpdateCheckManager(final pxnPluginLib plugin) {
		this.plugin = plugin;
		this.listenerPlayerJoin = new PlayerJoinListener(plugin, this);
	}



	@Override
	public void start() {
		this.runTaskTimerAsynchronously(this.plugin, this.delay, this.loop);
		this.listenerPlayerJoin.register();
	}
	public void startLater() {
		(new BukkitRunnable() {
			@Override
			public void run() {
				UpdateCheckManager.this.start();
			}
		}).runTaskLater(this.plugin, 10L);
	}
	@Override
	public void stop() {
		try {
			this.cancel();
		} catch (IllegalStateException ignore) {}
		this.listenerPlayerJoin.unregister();
	}



	@Override
	public void run() {
		final long now = Utils.GetMS();
		final long last = this.lastCheck.get();
		if (now - last >= this.period) {
			this.lastCheck.set(now);
			LOG.info(String.format(
				"%sFetching latest versions for %d plugins..",
				LOG_PREFIX,
				Integer.valueOf(this.checkers.size())
			));
			boolean available = false;
			final Iterator<UpdateCheckerTask> it = this.checkers.values().iterator();
			while (it.hasNext()) {
				final UpdateCheckerTask dao = it.next();
				dao.run();
				if (dao.hasUpdate())
					available = true;
				ThreadUtils.Sleep("1s");
			}
			if (!available) {
				LOG.info(LOG_PREFIX + "You have the latest versions");
			}
		}
	}



	public static UpdateCheckerTask Register(final xJavaPlugin plugin) {
		final int spigot_id = plugin.getSpigotPluginID();
		final String version = plugin.getPluginVersion();
		return Register(plugin, spigot_id, version);
	}
	public static UpdateCheckerTask Register(final JavaPlugin plugin, final int spigot_id, final String version) {
		if (spigot_id <= 0) return null;
		final UpdateCheckManager manager = Bukkit.getServicesManager().load(UpdateCheckManager.class);
		if (manager == null) throw new RuntimeException("UpdateCheckManager is not available");
		return manager.addPlugin(plugin, spigot_id, version);
	}
	public UpdateCheckerTask addPlugin(final JavaPlugin plugin, final int spigot_id, final String plugin_version) {
		if (spigot_id <= 0) {
			LOG.warning(String.format(
				"%sPlugin ID not set in: %s",
				LOG_PREFIX,
				plugin.getName()
			));
			return null;
		}
		final UpdateCheckerTask dao = new UpdateCheckerTask(plugin, spigot_id, plugin_version);
		this.checkers.put(Integer.valueOf(spigot_id), dao);
		return dao;
	}



	public static boolean Unregister(final xJavaPlugin plugin) {
		final int spigot_id  = plugin.getSpigotPluginID();
		return Unregister(spigot_id);
	}
	public static boolean Unregister(final int spigot_id) {
		if (spigot_id <= 0) return false;
		final UpdateCheckManager manager = Bukkit.getServicesManager().load(UpdateCheckManager.class);
		if (manager == null) throw new RuntimeException("UpdateCheckManager is not available");
		return manager.removePlugin(spigot_id);
	}
	public boolean removePlugin(final int spigot_id) {
		final UpdateCheckerTask dao = this.checkers.remove(Integer.valueOf(spigot_id));
		return (dao != null);
	}



	public boolean hasUpdate() {
		final Iterator<UpdateCheckerTask> it = this.checkers.values().iterator();
		while (it.hasNext()) {
			final UpdateCheckerTask dao = it.next();
			if (dao.hasUpdate())
				return true;
		}
		return false;
	}
	public UpdateCheckerTask[] getUpdates() {
		final HashSet<UpdateCheckerTask> updates = new HashSet<UpdateCheckerTask>();
		final Iterator<UpdateCheckerTask> it = this.checkers.values().iterator();
		while (it.hasNext()) {
			final UpdateCheckerTask dao = it.next();
			if (dao.hasUpdate())
				updates.add(dao);
		}
		return updates.toArray(new UpdateCheckerTask[0]);
	}
	public UpdateCheckerTask[] getUpdatesToPlayers() {
		final HashSet<UpdateCheckerTask> updates = new HashSet<UpdateCheckerTask>();
		final Iterator<UpdateCheckerTask> it = this.checkers.values().iterator();
		while (it.hasNext()) {
			final UpdateCheckerTask dao = it.next();
			if (dao.hasUpdate() && dao.isToPlayers())
				updates.add(dao);
		}
		return updates.toArray(new UpdateCheckerTask[0]);
	}



}
