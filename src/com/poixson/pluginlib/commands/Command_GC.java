package com.poixson.pluginlib.commands;

import static com.poixson.utils.BukkitUtils.GarbageCollect;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.poixson.pluginlib.pxnPluginLib;
import com.poixson.tools.commands.pxnCommandRoot;


// /gc
public class Command_GC extends pxnCommandRoot {



	public Command_GC(final pxnPluginLib plugin) {
		super(
			plugin,
			"pxn", // namespace
			"System.gc() and unload unused chunks.", // desc
			null, // usage
			"pxn.cmd.gc", // perm
			new String[] { // labels
				"gc"
			}
		);
	}



	@Override
	public boolean onCommand(final CommandSender sender, final String[] args) {
		final Player player = (sender instanceof Player ? (Player)sender : null);
		if (player != null
		&& !player.hasPermission("pxn.cmd.gc"))
			return false;
		final int freed_mb = GarbageCollect();
		final StringBuilder msg = new StringBuilder();
		msg.append(ChatColor.AQUA)
			.append("Garbage collected");
		if (freed_mb > 0) {
			msg.append("; freed: ")
				.append(ChatColor.GREEN)
				.append(freed_mb)
				.append("MB");
		}
		sender.sendMessage(msg.toString());
		return true;
	}



	@Override
	public List<String> onTabComplete(final CommandSender sender, final String[] args) {
		return new ArrayList<String>();
	}



}
