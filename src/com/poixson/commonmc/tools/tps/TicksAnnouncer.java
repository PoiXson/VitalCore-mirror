package com.poixson.commonmc.tools.tps;

import static com.poixson.commonmc.tools.tps.TicksPerSecond.DisplayTPS;

import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicBoolean;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.poixson.commonmc.pxnCommonPlugin;
import com.poixson.tools.abstractions.xStartStop;


public class TicksAnnouncer extends BukkitRunnable implements xStartStop {

	protected final pxnCommonPlugin plugin;

	protected final CopyOnWriteArraySet<ViewerDAO> viewers = new CopyOnWriteArraySet<ViewerDAO>();

	protected final AtomicBoolean running = new AtomicBoolean(false);



	public TicksAnnouncer(final pxnCommonPlugin plugin) {
		this.plugin = plugin;
	}



	@Override
	public void start() {
		if (this.running.compareAndSet(false, true)) {
			this.runTaskTimer(this.plugin, 20L, 20L);
		}
	}
	@Override
	public void stop() {
		if (this.running.compareAndSet(true, false)) {
			try {
				this.cancel();
			} catch (IllegalStateException ignore) {}
		}
	}



	@Override
	public void run() {
		final TicksPerSecond manager = pxnCommonPlugin.GetTicksManager();
		final double[] tps = manager.getTPS();
		for (final ViewerDAO viewer : this.viewers) {
			if (viewer.again())
				DisplayTPS(viewer.uuid, tps);
		}
	}



	public void add(final Player player) {
		this.add(player, 0);
	}
	public void add(final Player player, final int interval) {
		for (final ViewerDAO viewer : this.viewers) {
			if (viewer.isPlayer(player)) {
				viewer.interval = interval;
				return;
			}
		}
		final ViewerDAO viewer = new ViewerDAO(player, interval);
		this.viewers.add(viewer);
		this.start();
	}
	public boolean remove(final Player player) {
		for (final ViewerDAO viewer : this.viewers) {
			if (viewer.isPlayer(player)) {
				this.viewers.remove(viewer);
				if (this.viewers.isEmpty())
					this.stop();
				return true;
			}
		}
		return false;
	}
	public boolean toggle(final Player player) {
		if (!this.remove(player)) {
			this.add(player);
			return true;
		}
		return false;
	}



	protected class ViewerDAO {
		public final UUID uuid;
		public int interval;
		public long counter = 0L;

		public ViewerDAO(final Player player, final int interval) {
			this(player==null ? null : player.getUniqueId(), interval);
		}
		public ViewerDAO(final UUID uuid, final int interval) {
			this.uuid     = uuid;
			this.interval = interval;
		}

		public boolean isPlayer(final Player player) {
			return this.isPlayer(player==null ? null : player.getUniqueId());
		}
		public boolean isPlayer(final UUID uuid) {
			if (uuid == null)
				return (this.uuid == null);
			return (uuid.equals(this.uuid));
		}

		public boolean again() {
			final long count = ++this.counter;
			if (this.interval <= 1)
				return true;
			return (count % this.interval == 0);
		}

	}



}
