package com.poixson.tools;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Powerable;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;


public class DelayedLever extends BukkitRunnable {

	protected final JavaPlugin plugin;
	protected final Location loc;

	protected final boolean powered;
	protected final long    delay;



	public DelayedLever(final JavaPlugin plugin, final Location loc,
			final boolean powered, final long delay) {
		this.plugin  = plugin;
		this.loc     = loc;
		this.powered = powered;
		this.delay   = delay;
	}



	public void start() {
		this.runTaskLater(this.plugin, this.delay);
	}



	@Override
	public void run() {
		final Block block = this.loc.getBlock();
		final BlockData blockdata = block.getBlockData();
		if (blockdata instanceof Powerable) {
			final Powerable power = (Powerable) blockdata;
			power.setPowered(this.powered);
			block.setBlockData(power);
		}
	}



}
