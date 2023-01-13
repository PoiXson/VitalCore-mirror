package com.poixson.commonmc.tools.updatechecker;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.poixson.commonmc.pxnCommonPlugin;
import com.poixson.tools.xTime;
import com.poixson.tools.abstractions.xStartStop;
import com.poixson.utils.ThreadUtils;
import com.poixson.utils.Utils;


public class UpdateCheckManager extends BukkitRunnable implements xStartStop {
	public static final Logger log = pxnCommonPlugin.log;

	protected final pxnCommonPlugin plugin;

	protected final long delay  = (new xTime("5s")).ticks(50L);
	protected final long loop   = (new xTime("5m")).ticks(50L);
	protected final long period = (new xTime("3h")).ticks(50L);

	protected final AtomicLong lastCheck = new AtomicLong(0L);

	protected final ConcurrentHashMap<Integer, UpdateCheckerDAO> checkers = new ConcurrentHashMap<Integer, UpdateCheckerDAO>();



	public UpdateCheckManager(final pxnCommonPlugin plugin) {
		this.plugin = plugin;
	}



	@Override
	public void start() {
		this.runTaskTimerAsynchronously(this.plugin, this.delay, this.loop);
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
		} catch (Exception igore) {}
	}



	@Override
	public void run() {
		final long now = Utils.GetMS();
		final long last = this.lastCheck.get();
		if (now - last >= this.period) {
			this.lastCheck.set(now);
			final Iterator<UpdateCheckerDAO> it = this.checkers.values().iterator();
			while (it.hasNext()) {
				final UpdateCheckerDAO dao = it.next();
				dao.run();
				ThreadUtils.Sleep("1s");
			}
		}
	}



	public UpdateCheckerDAO addPlugin(final JavaPlugin plugin, final int plugin_id, final String plugin_version) {
		if (plugin_id <= 0) {
			log.warning("Plugin ID not set in: " + plugin.getName());
			return null;
		}
		final UpdateCheckerDAO dao = new UpdateCheckerDAO(plugin, plugin_id, plugin_version);
		this.checkers.put(Integer.valueOf(plugin_id), dao);
		return dao;
	}
	public boolean removePlugin(final int plugin_id) {
		final UpdateCheckerDAO dao = this.checkers.remove(plugin_id);
		return (dao != null);
	}



}
