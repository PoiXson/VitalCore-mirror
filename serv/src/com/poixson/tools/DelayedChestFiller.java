package com.poixson.tools;

import static com.poixson.utils.BukkitUtils.Log;
import static com.poixson.utils.BukkitUtils.SafeCancel;

import java.util.HashSet;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;


public abstract class DelayedChestFiller extends BukkitRunnable {

	public static final long DEFAULT_DELAY = 20L; // ticks

	protected static CopyOnWriteArraySet<DelayedChestFiller> fillers = new CopyOnWriteArraySet<DelayedChestFiller>();
	protected static final AtomicBoolean stopping = new AtomicBoolean(false);

	protected final xJavaPlugin<?> plugin;

	protected final Location loc;
	protected final String world_name;
	protected final int x, y, z;
	protected final AtomicBoolean done = new AtomicBoolean(false);



	public DelayedChestFiller(final xJavaPlugin<?> plugin,
			final String world_name, final int x, final int y, final int z) {
		this(plugin, null, world_name, x, y, z);
	}
	public DelayedChestFiller(final xJavaPlugin<?> plugin, final Location loc) {
		this(plugin, loc, null, 0, 0, 0);
	}
	protected DelayedChestFiller(final xJavaPlugin<?> plugin, final Location loc,
			final String world_name, final int x, final int y, final int z) {
		this.plugin     = plugin;
		this.loc        = loc;
		this.world_name = world_name;
		this.x          = x;
		this.y          = y;
		this.z          = z;
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
		Log().info("Finishing chest population..");
		int count = 0;
		while (!fillers.isEmpty()) {
			final HashSet<DelayedChestFiller> remove = new HashSet<DelayedChestFiller>();
			for (final DelayedChestFiller filler : fillers) {
				remove.add(filler);
				SafeCancel(filler);
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
			Log().info("Finished populating chests: "+Integer.toString(count));
	}



	public Location getLocation() {
		return this.getBlock().getLocation();
	}
	public Block getBlock() {
		if (this.loc == null) {
			final World world = Bukkit.getWorld(this.world_name);
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



	public Logger log() {
		return this.plugin.log();
	}



}
