package com.poixson.commonmc.tools.updatechecker;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
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
	protected final AtomicReference<String> updateMsg = new AtomicReference<String>(null);
	protected final AtomicBoolean msgToPlayers = new AtomicBoolean(false);



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
			String msg;
			// unsupported server version
			if (Math.abs(server_diff) > 100) {
				this.msgToPlayers.set(false);
				msg = String.format(
					"%s[%s] New version available but requires a %s server version",
					ChatColor.WHITE,
					api.title,
					(server_diff>0.0 ? "newer" : "older")
				);
			// new supported version
			} else {
				this.msgToPlayers.set(true);
				msg = String.format(
					"%s[%s]%s New version available: %s\n  %sAvailable at:%s %s",
					ChatColor.RED,   api.title,
					ChatColor.WHITE, api.current_version,
					ChatColor.RED, ChatColor.WHITE,
					String.format(SpigotWebAPI.SPIGOT_RES_URL, Integer.valueOf(api.id))
				);
			}
			console.sendMessage(msg);
			this.updateMsg.set(msg);
			if (this.check_count.get() == 1) {
				if (this.msgToPlayers.get()) {
					for (final Player p : Bukkit.getOnlinePlayers()) {
						if (p.isOp() || p.hasPermission("pxncommon.updates"))
							p.sendMessage(msg);
					}
				}
			}
		} else {
			this.updateMsg.set(null);
		}
	}



	public boolean hasUpdate() {
		return (this.updateMsg.get() != null);
	}

	public boolean isToPlayers() {
		return this.msgToPlayers.get();
	}

	public String getUpdateMessage() {
		return this.updateMsg.get();
	}



}
