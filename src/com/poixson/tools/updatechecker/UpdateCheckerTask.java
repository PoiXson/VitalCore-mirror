package com.poixson.tools.updatechecker;

import static com.poixson.utils.Utils.IsEmpty;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.util.concurrent.AtomicDouble;
import com.poixson.exceptions.RequiredArgumentException;
import com.poixson.utils.StringUtils;


public class UpdateCheckerTask implements Runnable {

	protected final JavaPlugin plugin;
	protected final int plugin_id;
	protected final String plugin_version;

	protected final AtomicLong check_count = new AtomicLong(0L);
	protected final AtomicDouble version_diff = new AtomicDouble(Double.MIN_NORMAL);
	protected final AtomicReference<String> updateMsg = new AtomicReference<String>(null);
	protected final AtomicBoolean msgToPlayers = new AtomicBoolean(false);



	public UpdateCheckerTask(final JavaPlugin plugin,
			final int plugin_id, final String plugin_version) {
		if (plugin == null) throw new NullPointerException();
		if (plugin_id <= 0) throw new RuntimeException("Plugin ID not set in: " + plugin.getName());
		if (IsEmpty(plugin_version)) throw new RequiredArgumentException("plugin_version");
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
			this.updateMsg.set(msg);
			// console message
			{
				final String str = msg.toString()
					.replace(""+ChatColor.RED,   "")
					.replace(""+ChatColor.WHITE, "");
				for (final String line : str.split("\n"))
					this.log().info(line);
			}
			// message to players
			if (this.check_count.get() == 1) {
				if (this.msgToPlayers.get()) {
					for (final Player p : Bukkit.getOnlinePlayers()) {
						if (p.isOp() || p.hasPermission("pxnpluginlib.updates"))
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



	public Logger log() {
		return this.plugin.getLogger();
	}



}
