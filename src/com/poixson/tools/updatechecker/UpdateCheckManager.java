package com.poixson.tools.updatechecker;

import static com.poixson.utils.BukkitUtils.SafeCancel;
import static com.poixson.utils.Utils.GetMS;
import static com.poixson.utils.Utils.IsEmpty;

import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.poixson.tools.xJavaPlugin;
import com.poixson.tools.xListener;
import com.poixson.tools.xTime;
import com.poixson.tools.abstractions.xStartStop;
import com.poixson.utils.ThreadUtils;
import com.poixson.vitalcore.VitalCorePlugin;


public class UpdateCheckManager extends BukkitRunnable implements xStartStop, xListener {

	protected final VitalCorePlugin plugin;

	protected final long delay  = (new xTime( "5s")).ticks(50L);
	protected final long loop   = (new xTime( "5m")).ticks(50L);
	protected final long period = (new xTime("12h")).ms();

	protected final AtomicLong lastCheck = new AtomicLong(0L);

	protected final ConcurrentHashMap<Integer, UpdateCheckerTask> checkers = new ConcurrentHashMap<Integer, UpdateCheckerTask>();



	public UpdateCheckManager(final VitalCorePlugin plugin) {
		this.plugin = plugin;
	}



	@Override
	public void start() {
		this.runTaskTimerAsynchronously(this.plugin, this.delay, this.loop);
		this.register(this.plugin);
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
		SafeCancel(this);
		this.unregister();
	}



	@Override
	public void run() {
		final long now = GetMS();
		final long last = this.lastCheck.get();
		if (now - last >= this.period) {
			this.lastCheck.set(now);
			this.log().info(String.format(
				"Fetching latest versions for %d plugins..",
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
			if (!available)
				this.log().info("You have the latest versions");
		}
	}



	@EventHandler(priority=EventPriority.LOWEST)
	public void onPlayerJoin(final PlayerJoinEvent event) {
		if (this.hasUpdate()) {
			final Player player = event.getPlayer();
			if (player.isOp()
			|| player.hasPermission("pxn.updates")) {
				(new BukkitRunnable() {
					@Override
					public void run() {
						final UpdateCheckerTask[] updates = UpdateCheckManager.this.getUpdatesToPlayers();
						String msg;
						for (final UpdateCheckerTask task : updates) {
							msg = task.getUpdateMessage();
							if (!IsEmpty(msg))
								player.sendMessage(msg);
						}
					}
				}).runTaskLater(this.plugin, 10L);
			}
		}
	}



	public static UpdateCheckerTask RegisterPlugin(final xJavaPlugin plugin) {
		final int spigot_id = plugin.getSpigotPluginID();
		final String version = plugin.getVersion();
		return RegisterPlugin(plugin, spigot_id, version);
	}
	public static UpdateCheckerTask RegisterPlugin(final xJavaPlugin plugin, final int spigot_id, final String version) {
		if (spigot_id <= 0) return null;
		final UpdateCheckManager manager = Bukkit.getServicesManager().load(UpdateCheckManager.class);
		if (manager == null) return null;
		return manager.addPlugin(plugin, spigot_id, version);
	}
	public UpdateCheckerTask addPlugin(final xJavaPlugin plugin, final int spigot_id, final String plugin_version) {
		if (spigot_id <= 0) {
			this.log().warning("Plugin ID not set in: "+plugin.getName());
			return null;
		}
		final UpdateCheckerTask dao = new UpdateCheckerTask(plugin, spigot_id, plugin_version);
		this.checkers.put(Integer.valueOf(spigot_id), dao);
		return dao;
	}



	public static boolean UnregisterPlugin(final xJavaPlugin plugin) {
		final int spigot_id  = plugin.getSpigotPluginID();
		return UnregisterPlugin(spigot_id);
	}
	public static boolean UnregisterPlugin(final int spigot_id) {
		if (spigot_id <= 0) return false;
		final UpdateCheckManager manager = Bukkit.getServicesManager().load(UpdateCheckManager.class);
		if (manager == null) return false;
		else                 return manager.removePlugin(spigot_id);
	}
	public boolean removePlugin(final int spigot_id) {
		return (null != this.checkers.remove(Integer.valueOf(spigot_id)));
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



	public Logger log() {
		return this.plugin.log();
	}



}
