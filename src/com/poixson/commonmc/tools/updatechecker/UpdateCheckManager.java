package com.poixson.commonmc.tools.updatechecker;

import static com.poixson.commonmc.pxnCommonPlugin.LOG_PREFIX;
import static com.poixson.commonmc.tools.plugin.xJavaPlugin.LOG;

import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.poixson.commonmc.pxnCommonPlugin;
import com.poixson.tools.xTime;
import com.poixson.tools.abstractions.xStartStop;
import com.poixson.utils.ThreadUtils;
import com.poixson.utils.Utils;


public class UpdateCheckManager extends BukkitRunnable implements xStartStop {

	protected final pxnCommonPlugin plugin;

	protected final long delay  = (new xTime( "5s")).ticks(50L);
	protected final long loop   = (new xTime( "5m")).ticks(50L);
	protected final long period = (new xTime("12h")).ms();

	protected final AtomicLong lastCheck = new AtomicLong(0L);

	protected final ConcurrentHashMap<Integer, UpdateCheckerDAO> checkers = new ConcurrentHashMap<Integer, UpdateCheckerDAO>();

	protected final PlayerJoinListener listenerPlayerJoin;



	public UpdateCheckManager(final pxnCommonPlugin plugin) {
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
			final Iterator<UpdateCheckerDAO> it = this.checkers.values().iterator();
			while (it.hasNext()) {
				final UpdateCheckerDAO dao = it.next();
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



	public UpdateCheckerDAO addPlugin(final JavaPlugin plugin, final int plugin_id, final String plugin_version) {
		if (plugin_id <= 0) {
			LOG.warning(String.format(
				"%sPlugin ID not set in: %s",
				LOG_PREFIX,
				plugin.getName()
			));
			return null;
		}
		final UpdateCheckerDAO dao = new UpdateCheckerDAO(plugin, plugin_id, plugin_version);
		this.checkers.put(Integer.valueOf(plugin_id), dao);
		return dao;
	}
	public boolean removePlugin(final int plugin_id) {
		final UpdateCheckerDAO dao = this.checkers.remove(Integer.valueOf(plugin_id));
		return (dao != null);
	}



	public boolean hasUpdate() {
		final Iterator<UpdateCheckerDAO> it = this.checkers.values().iterator();
		while (it.hasNext()) {
			final UpdateCheckerDAO dao = it.next();
			if (dao.hasUpdate())
				return true;
		}
		return false;
	}
	public UpdateCheckerDAO[] getUpdates() {
		final HashSet<UpdateCheckerDAO> updates = new HashSet<UpdateCheckerDAO>();
		final Iterator<UpdateCheckerDAO> it = this.checkers.values().iterator();
		while (it.hasNext()) {
			final UpdateCheckerDAO dao = it.next();
			if (dao.hasUpdate())
				updates.add(dao);
		}
		return updates.toArray(new UpdateCheckerDAO[0]);
	}
	public UpdateCheckerDAO[] getUpdatesToPlayers() {
		final HashSet<UpdateCheckerDAO> updates = new HashSet<UpdateCheckerDAO>();
		final Iterator<UpdateCheckerDAO> it = this.checkers.values().iterator();
		while (it.hasNext()) {
			final UpdateCheckerDAO dao = it.next();
			if (dao.hasUpdate() && dao.isToPlayers())
				updates.add(dao);
		}
		return updates.toArray(new UpdateCheckerDAO[0]);
	}



}
