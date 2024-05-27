package com.poixson.pluginlib.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.poixson.pluginlib.pxnPluginLib;
import com.poixson.tools.commands.pxnCommandRoot;


// /spawn
public class Command_Spawn extends pxnCommandRoot {



	public Command_Spawn(final pxnPluginLib plugin) {
		super(
			plugin,
			"pxn", // namespace
			"Teleport to the world spawn location.", // desc
			null, // usage
			"pxn.cmd.spawn", // perm
			new String[] { // labels
				"spawn",
				"spawnpoint"
			}
		);
	}



	@Override
	public boolean onCommand(final CommandSender sender, final String[] args) {
		final Player player = (sender instanceof Player ? (Player)sender : null);
		final int num_args = args.length;
		// other players
		if (num_args > 0) {
			if (!sender.hasPermission("pxn.cmd.spawn.other"))
				return false;
			int count = 0;
			LOOP_ARG:
			for (final String arg : args) {
				final Player p = Bukkit.getPlayer(arg);
				if (p == null) {
					sender.sendMessage(String.format("%sPlayer not found: %s", ChatColor.GOLD, arg));
					continue LOOP_ARG;
				}
				final World world = p.getWorld();
				final Location loc = world.getSpawnLocation();
				p.teleport(loc);
				count++;
			}
			if (count > 0) {
				sender.sendMessage(String.format(
					"%sTeleported %d player%s to spawn",
					ChatColor.AQUA,
					Integer.valueOf(count),
					(count == 1 ? "" : "s")
				));
				return true;
			}
		// single player
		} else {
			if (!sender.hasPermission("pxn.cmd.spawn"))
				return false;
			final World world = player.getWorld();
			final Location loc = world.getSpawnLocation();
			player.teleport(loc);
			return true;
		}
		return false;
	}



}
