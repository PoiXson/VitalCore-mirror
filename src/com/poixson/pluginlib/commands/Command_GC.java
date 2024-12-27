package com.poixson.pluginlib.commands;

import static com.poixson.pluginlib.pxnPluginLib.CHAT_PREFIX;
import static com.poixson.utils.BukkitUtils.GarbageCollect;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.poixson.pluginlib.pxnPluginLib;
import com.poixson.tools.commands.pxnCommandRoot;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;


// /gc
public class Command_GC extends pxnCommandRoot {



	public Command_GC(final pxnPluginLib plugin) {
		super(
			plugin,
			"pxn", // namespace
			"System.gc() and unload unused chunks.", // desc
			null, // usage
			"pxn.cmd.gc", // perm
			// labels
			"gc"
		);
	}



	@Override
	public boolean onCommand(final CommandSender sender, final String[] args) {
		final Player player = (sender instanceof Player ? (Player)sender : null);
		if (player != null
		&& !player.hasPermission("pxn.cmd.gc"))
			return false;
		final int freed_mb = GarbageCollect();
		Component msg = CHAT_PREFIX.append(Component.text("Garbage collected").color(NamedTextColor.AQUA));
		if (freed_mb > 0) {
			msg = Component.textOfChildren(
				Component.text("; freed: "),
				Component.text(Integer.toString(freed_mb)+"MB").color(NamedTextColor.GREEN)
			);
		}
		sender.sendMessage(msg);
		return true;
	}



	@Override
	public List<String> onTabComplete(final CommandSender sender, final String[] args) {
		return Collections.emptyList();
	}



}
