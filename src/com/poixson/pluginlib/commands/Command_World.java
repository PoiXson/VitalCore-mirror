package com.poixson.pluginlib.commands;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.poixson.pluginlib.pxnPluginLib;
import com.poixson.tools.commands.pxnCommandRoot;


// /world
public class Command_World extends pxnCommandRoot {



	public Command_World(final pxnPluginLib plugin) {
		super(
			plugin,
			"pxn", // namespace
			"Teleport to a world.", // desc
			null, // usage
			"pxn.cmd.world", // perm
			new String[] { // labels
				"world"
			}
		);
	}



	@Override
	public boolean onCommand(final CommandSender sender, final String[] args) {
		final Player player = (sender instanceof Player ? (Player)sender : null);
		final int num_args = args.length;
		if (num_args == 0)
			return false;
		final String world_name = args[0];
		final World world = Bukkit.getWorld(world_name);
		if (world == null) {
			sender.sendMessage(String.format("%sUnknown world: %s", ChatColor.RED, world_name));
			return true;
		}
		final Location loc = world.getSpawnLocation();
		// self
		if (num_args == 1) {
			if (!sender.hasPermission("pxn.cmd.world"))
				return false;
			if (player == null) {
				sender.sendMessage("Cannot teleport console");
				return true;
			}
			player.teleport(loc);
			sender.sendMessage(String.format(
				"%sTeleported to world: %s",
				ChatColor.GOLD,
				world_name
			));
			return true;
		// other players
		} else {
			if (!sender.hasPermission("pxn.cmd.world.other"))
				return false;
			int count = 0;
			for (int i=1; i<num_args; i++) {
				final Player p = Bukkit.getPlayer(args[i]);
				p.teleport(loc);
				p.sendMessage(String.format(
					"%sTeleported to world: %s",
					ChatColor.GOLD,
					world_name
				));
				count++;
			}
			if (count > 0) {
				sender.sendMessage(String.format(
					"%sTeleported %d player%s to world: %s",
					ChatColor.AQUA,
					Integer.valueOf(count),
					(count == 1 ? "" : "s"),
					world_name
				));
				return true;
			}
		}
		return false;
	}



	@Override
	public List<String> onTabComplete(final CommandSender sender, final String[] args) {
		final LinkedList<String> list = new LinkedList<String>();
		final int num_args = args.length;
		if (num_args == 1) {
			final String arg = args[0].toLowerCase();
			for (final World world : Bukkit.getWorlds()) {
				final String name = world.getName();
				if (name.toLowerCase().startsWith(arg))
					list.add(name);
			}
		} else
		if (num_args > 1) {
			return this.onTabComplete_Players(args);
		}
		return list;
	}



}
