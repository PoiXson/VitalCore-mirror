package com.poixson.pluginlib.commands;

import static com.poixson.pluginlib.pxnPluginLib.CHAT_PREFIX;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.poixson.pluginlib.pxnPluginLib;
import com.poixson.tools.commands.pxnCommandRoot;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;


// /setspawn
public class Command_SetSpawn extends pxnCommandRoot {



	public Command_SetSpawn(final pxnPluginLib plugin) {
		super(
			plugin,
			"pxn", // namespace
			"Set the world spawn to your current location.", // desc
			null, // usage
			"pxn.cmd.setspawn", // perm
			new String[] { // labels
				"setspawn"
			}
		);
	}



	@Override
	public boolean onCommand(final CommandSender sender, final String[] args) {
		final Player player = (sender instanceof Player ? (Player)sender : null);
		if (player != null) {
			if (!player.hasPermission("pxn.cmd.setspawn"))
				return false;
			final Location loc = player.getLocation();
			final World world = loc.getWorld();
			world.setSpawnLocation(loc);
			sender.sendMessage(CHAT_PREFIX.append(Component.text(
				"World spawn location set").color(NamedTextColor.GOLD)));
			return true;
		}
		return false;
	}



}
