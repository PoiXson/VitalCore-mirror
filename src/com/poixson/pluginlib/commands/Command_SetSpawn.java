package com.poixson.pluginlib.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.poixson.pluginlib.pxnPluginLib;
import com.poixson.tools.commands.pxnCommandRoot;


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
		if (sender instanceof Player) {
			final Player player = (Player) sender;
			if (!player.hasPermission("pxn.cmd.setspawn"))
				return false;
			final Location loc = player.getLocation();
			final World world = loc.getWorld();
			world.setSpawnLocation(loc);
			player.sendMessage(ChatColor.GOLD+"Set world spawn to your current location");
			return true;
		}
		return false;
	}



}
