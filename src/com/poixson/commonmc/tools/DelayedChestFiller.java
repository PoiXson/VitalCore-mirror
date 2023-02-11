package com.poixson.commonmc.tools;

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
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.poixson.commonmc.tools.plugin.xJavaPlugin;


public abstract class DelayedChestFiller extends BukkitRunnable {
	protected static final String LOG_PREFIX = xJavaPlugin.LOG_PREFIX;
	protected static final Logger log = Logger.getLogger("Minecraft");

	protected static CopyOnWriteArraySet<DelayedChestFiller> fillers = new CopyOnWriteArraySet<DelayedChestFiller>();
	protected static final AtomicBoolean stopping = new AtomicBoolean(false);

	protected final JavaPlugin plugin;

	protected final Location loc;
	protected final String worldName;
	protected final int x, y, z;



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
		if (stopping.get())
			return;
		fillers.add(this);
		this.runTaskLater(this.plugin, 60L);
	}
	public static void stop() {
		stopping.set(true);
		log.info(LOG_PREFIX + "Finishing chest population..");
		int count = 0;
		while (!fillers.isEmpty()) {
			final HashSet<DelayedChestFiller> remove = new HashSet<DelayedChestFiller>();
			for (final DelayedChestFiller filler : fillers) {
				remove.add(filler);
				try {
					filler.cancel();
					filler.run();
				} catch (IllegalStateException ignore) {}
			}
			for (final DelayedChestFiller filler : remove) {
				fillers.remove(filler);
			}
		}
		if (count > 0)
			log.info(LOG_PREFIX + "Finished populating chests: " + Integer.toString(count));
	}



	public Location getLocation() {
		return this.getBlock().getLocation();
	}
	public Block getBlock() {
		if (this.loc == null) {
			final World world = Bukkit.getWorld(this.worldName);
			return world.getBlockAt(this.x, this.y, this.z);
		} else {
			return this.loc.getBlock();
		}
	}



	@Override
	public void run() {
		final Block block = this.getBlock();
		final BlockState state = block.getState();
		if (state instanceof Container) {
			final Container chest = (Container) state;
			this.fill(chest.getInventory());
		}
	}

	public abstract void fill(final Inventory chest);



}
