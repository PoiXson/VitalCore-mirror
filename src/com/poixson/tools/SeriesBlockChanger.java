package com.poixson.tools;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.poixson.tools.abstractions.Tuple;
import com.poixson.tools.abstractions.xStartStop;


public class SeriesBlockChanger extends BukkitRunnable implements xStartStop {

	protected final JavaPlugin plugin;

	public final LinkedList<Tuple<Location, BlockData>> queue = new LinkedList<Tuple<Location, BlockData>>();

	protected final long ticks;
	protected AtomicInteger index = new AtomicInteger(0);



	public SeriesBlockChanger(final JavaPlugin plugin, final long ticks) {
		super();
		this.plugin = plugin;
		this.ticks  = ticks;
	}



	@Override
	public void start() {
		this.runTaskTimer(this.plugin, this.ticks, this.ticks);
	}
	@Override
	public void stop() {
		try {
			this.cancel();
		} catch (Exception ignore) {}
	}



	@Override
	public void run() {
		final Tuple<Location, BlockData> entry = this.queue.pollFirst();
		if (entry == null) {
			this.stop();
			return;
		}
		final Block block = entry.key.getBlock();
		block.setBlockData(entry.val);
	}



	public void add(final Location location, final BlockData block) {
		this.queue.add(new Tuple<Location, BlockData>(location, block));
	}



}
