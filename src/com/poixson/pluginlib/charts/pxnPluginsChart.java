package com.poixson.pluginlib.charts;

import java.util.concurrent.Callable;

import org.bstats.charts.SimplePie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

import com.poixson.pluginlib.pxnPluginLib;
import com.poixson.tools.xJavaPlugin;
import com.poixson.tools.events.xListener;


public class pxnPluginsChart implements Callable<String>, xListener {

	protected final pxnPluginLib plugin;



	public pxnPluginsChart(final pxnPluginLib plugin) {
		this.plugin = plugin;
	}



	public static SimplePie GetChart(final pxnPluginLib plugin) {
		return new SimplePie(
			"plugins_count",
			new pxnPluginsChart(plugin)
		);
	}



	@Override
	public String call() throws Exception {
		final int count = this.plugin.getPluginsCount();
		return Integer.toString(count);
	}



	@EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
	public void onPluginEnable(final PluginEnableEvent event) {
		final Plugin p = event.getPlugin();
		if (p instanceof xJavaPlugin)
			this.plugin.registerPluginPXN((xJavaPlugin)p);
	}
	@EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
	public void onPluginDisable(final PluginDisableEvent event) {
		final Plugin p = event.getPlugin();
		if (p instanceof xJavaPlugin)
			this.plugin.unregisterPluginPXN((xJavaPlugin)p);
	}



}
