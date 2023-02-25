package com.poixson.commonmc.charts;

import java.util.concurrent.Callable;

import org.bstats.charts.SimplePie;

import com.poixson.commonmc.pxnCommonPlugin;


public class pxnPluginsChart implements Callable<String> {

	protected final pxnCommonPlugin plugin;



	public static SimplePie GetChart(final pxnCommonPlugin plugin) {
		return new SimplePie(
			"plugins_count",
			new pxnPluginsChart(plugin)
		);
	}
	public pxnPluginsChart(final pxnCommonPlugin plugin) {
		this.plugin = plugin;
	}



	@Override
	public String call() throws Exception {
		final int count = this.plugin.getPluginsCount();
		return Integer.toString(count);
	}



}
