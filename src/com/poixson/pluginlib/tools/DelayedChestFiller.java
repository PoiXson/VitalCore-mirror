package com.poixson.pluginlib.tools;

import static com.poixson.pluginlib.pxnPluginLib.LOG_PREFIX;
import static com.poixson.pluginlib.tools.plugin.xJavaPlugin.LOG;

import java.util.HashSet;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicBoolean;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;


public abstract class DelayedChestFiller extends BukkitRunnable {

	public static final long DEFAULT_DELAY = 20L; // ticks

	protected static CopyOnWriteArraySet<DelayedChestFiller> fillers = new CopyOnWriteArraySet<DelayedChestFiller>();
	protected static final AtomicBoolean stopping = new AtomicBoolean(false);

	protected final JavaPlugin plugin;

	protected final Location loc;
	protected final String worldName;
	protected final int x, y, z;
	protected final AtomicBoolean done = new AtomicBoolean(false);



	public DelayedChestFiller(final JavaPlugin plugin,
			final String worldName, final int x, final int y, final int z) {
		this.plugin = plugin;
		this.loc = null;
		this.worldName = worldName;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public DelayedChestFiller(final JavaPlugin plugin, final Location loc) {
		this.plugin = plugin;
		this.loc = loc;
		this.worldName = null;
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}



	public void start() {
		this.start(DEFAULT_DELAY);
	}
	public void start(final long delay) {
		if (stopping.get())
			return;
		fillers.add(this);
		this.runTaskLater(this.plugin, delay);
	}
	public static void stop() {
		stopping.set(true);
		LOG.info(LOG_PREFIX + "Finishing chest population..");
		int count = 0;
		while (!fillers.isEmpty()) {
			final HashSet<DelayedChestFiller> remove = new HashSet<DelayedChestFiller>();
			for (final DelayedChestFiller filler : fillers) {
				remove.add(filler);
				try {
					filler.cancel();
				} catch (IllegalStateException ignore) {}
				if (!filler.done.get()) {
					filler.run();
					count++;
				}
			}
			for (final DelayedChestFiller filler : remove) {
				fillers.remove(filler);
			}
		}
		if (count > 0)
			LOG.info(LOG_PREFIX + "Finished populating chests: " + Integer.toString(count));
	}



	public Location getLocation() {
		return this.getBlock().getLocation();
	}
	public Block getBlock() {
		if (this.loc == null) {
			final World world = Bukkit.getWorld(this.worldName);
			if (stopping.get()) {
				if (!world.getChunkAt(this.x, this.z).isLoaded())
					return null;
			}
			return world.getBlockAt(this.x, this.y, this.z);
		} else {
			return this.loc.getBlock();
		}
	}



	@Override
	public void run() {
		if (!this.done.compareAndSet(false, true)) return;
		final Block block = this.getBlock();
		if (block != null) {
			final BlockState state = block.getState();
			if (state != null
			&&  state instanceof Container) {
				final Container chest = (Container) state;
				this.fill(chest.getInventory());
			}
		}
	}

	public abstract void fill(final Inventory chest);



}
