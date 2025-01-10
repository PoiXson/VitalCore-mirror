package com.poixson.vitalcore.charts;

import java.util.concurrent.Callable;

import org.bstats.charts.SimplePie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

import com.poixson.tools.xJavaPlugin;
import com.poixson.tools.xListener;
import com.poixson.vitalcore.VitalCorePlugin;


public class PluginsCountChart implements Callable<String>, xListener {

	protected final VitalCorePlugin plugin;



	public PluginsCountChart(final VitalCorePlugin plugin) {
		this.plugin = plugin;
	}



	public static SimplePie GetChart(final VitalCorePlugin plugin) {
		return new SimplePie(
			"plugins_count",
			new PluginsCountChart(plugin)
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
