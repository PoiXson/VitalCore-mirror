package com.poixson.commonmc.tools.scripting.screen;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.map.MapView;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.poixson.tools.abstractions.xStartStop;


public class MapSenderTask extends BukkitRunnable implements xStartStop {
	protected static final long DEFAULT_SEND_RATE = 5L;

	protected final JavaPlugin plugin;

	protected final MapView view;



	public MapSenderTask(final JavaPlugin plugin, final MapView view) {
		this.plugin = plugin;
		this.view   = view;
	}



	@Override
	public void run() {
		for (final Player player : Bukkit.getOnlinePlayers())
			player.sendMap(this.view);
	}



	@Override
	public void start() {
		this.start(DEFAULT_SEND_RATE);
	}
	public void start(final int fps) {
		this.start( (long)Math.floorDiv(20, fps) );
	}
	public void start(final long rate) {
		this.runTaskTimer(this.plugin, 2L, rate);
	}

	@Override
	public void stop() {
		try {
			this.cancel();
		} catch (IllegalStateException ignore) {}
	}



}
