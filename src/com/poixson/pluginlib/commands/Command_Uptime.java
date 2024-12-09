package com.poixson.pluginlib.commands;

import static com.poixson.pluginlib.pxnPluginLib.CHAT_PREFIX;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.poixson.pluginlib.pxnPluginLib;
import com.poixson.tools.commands.pxnCommandRoot;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;


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
		final Player player = (sender instanceof Player ? (Player)sender : null);
		if (player != null) {
			if (!player.hasPermission("pxn.cmd.uptime"))
				return false;
		}
		sender.sendMessage(CHAT_PREFIX
			.append(Component.text("Uptime: "                      ).color(NamedTextColor.AQUA))
			.append(Component.text(this.plugin.getUptimeFormatted()).color(NamedTextColor.GOLD))
		);
		return true;
	}



}
