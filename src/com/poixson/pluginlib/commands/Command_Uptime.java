package com.poixson.pluginlib.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.poixson.pluginlib.pxnPluginLib;
import com.poixson.tools.commands.pxnCommandRoot;


// /uptime
public class Command_Uptime extends pxnCommandRoot {

	protected final pxnPluginLib plugin;



	public Command_Uptime(final pxnPluginLib plugin) {
		super(
			plugin,
			"pxn", // namespace
			"Show the total time the server has been running.", // desc
			null, // usage
			"pxn.cmd.uptime", // perm
			new String[] { // labels
				"uptime"
			}
		);
		this.plugin = plugin;
	}



	@Override
	public boolean onCommand(final CommandSender sender, final String[] args) {
		if (sender instanceof Player) {
			if (!sender.hasPermission("pxn.cmd.uptime"))
				return false;
		}
		sender.sendMessage(String.format("%sUptime: %s", ChatColor.GOLD, this.plugin.getUptimeFormatted()));
		return true;
	}



}
