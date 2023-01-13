package com.poixson.commonbukkit.tools.updatechecker;

import java.util.concurrent.atomic.AtomicLong;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.util.concurrent.AtomicDouble;
import com.poixson.exceptions.RequiredArgumentException;
import com.poixson.utils.StringUtils;
import com.poixson.utils.Utils;


public class UpdateCheckerDAO implements Runnable {

	protected final JavaPlugin plugin;
	protected final int plugin_id;
	protected final String plugin_version;

	protected final AtomicLong check_count = new AtomicLong(0L);
	protected final AtomicDouble version_diff = new AtomicDouble(Double.MIN_NORMAL);



	public UpdateCheckerDAO(final JavaPlugin plugin,
			final int plugin_id, final String plugin_version) {
		if (plugin == null) throw new NullPointerException();
		if (plugin_id <= 0) throw new RuntimeException("Plugin ID not set in: " + plugin.getName());
		if (Utils.isEmpty(plugin_version)) throw new RequiredArgumentException("plugin_version");
		this.plugin         = plugin;
		this.plugin_id      = plugin_id;
		this.plugin_version = plugin_version;
	}



	@Override
	public void run() {
		this.check_count.incrementAndGet();
		final SpigotWebAPI api = SpigotWebAPI.Get(this.plugin_id);
		if (api == null)
			return;
		final double diff = StringUtils.CompareVersions(this.plugin_version, api.current_version);
		this.version_diff.set(diff);
		// newer version available
		if (diff > 0.0) {
			final ConsoleCommandSender console = Bukkit.getConsoleSender();
			final double server_diff = api.diffServerVersion();
			if (server_diff == 0.0) {
				console.sendMessage(String.format(
					ChatColor.GOLD + "[%s] New version available: %s",
					api.title,
					api.current_version
				));
				console.sendMessage("Available at: " + String.format(
					SpigotWebAPI.SPIGOT_RES_URL,
					api.id
				));
			} else {
				console.sendMessage(String.format(
					"[%s] New version available but requires a %s server version",
					api.title,
					(server_diff>0.0 ? "newer" : "older")
				));
			}
		}
	}



}
