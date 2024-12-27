package com.poixson.pluginlib.commands;

import static com.poixson.pluginlib.pxnPluginLib.CHAT_PREFIX;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.poixson.pluginlib.pxnPluginLib;
import com.poixson.tools.commands.pxnCommandRoot;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;


// /spawn
public class Command_Spawn extends pxnCommandRoot {



	public Command_Spawn(final pxnPluginLib plugin) {
		super(
			plugin,
			"pxn", // namespace
			"Teleport to the world spawn location.", // desc
			null, // usage
			"pxn.cmd.spawn", // perm
			// labels
			"spawn",
			"spawnpoint"
		);
	}



	@Override
	public boolean onCommand(final CommandSender sender, final String[] args) {
		final Player player = (sender instanceof Player ? (Player)sender : null);
		final int num_args = args.length;
		// self
		if (num_args == 0) {
			if (player == null) {
				sender.sendMessage("Cannot teleport console");
				return true;
			}
			if (!sender.hasPermission("pxn.cmd.spawn"))
				return false;
			final World world = player.getWorld();
			final Location loc = world.getSpawnLocation();
			player.teleport(loc);
			sender.sendMessage(Component.text("Teleported to spawn").color(NamedTextColor.AQUA));
			return true;
		// other players
		} else {
			if (!sender.hasPermission("pxn.cmd.spawn.other"))
				return false;
			int count = 0;
			LOOP_ARGS:
			for (final String arg : args) {
				final Player p = Bukkit.getPlayer(arg);
				if (p == null) {
					sender.sendMessage(CHAT_PREFIX.append(Component.text(
						"Player not found: "+arg).color(NamedTextColor.RED)));
					continue LOOP_ARGS;
				}
				final World world = p.getWorld();
				final Location loc = world.getSpawnLocation();
				p.teleport(loc);
				p.sendMessage(Component.text("Teleported to spawn").color(NamedTextColor.AQUA));
				count++;
			}
			if (count > 0) {
				sender.sendMessage(CHAT_PREFIX.append(Component.text(String.format(
					"Teleported %d player%s to spawn",
					Integer.valueOf(count),
					(count == 1 ? "" : "s")
				)).color(NamedTextColor.AQUA)));
				return true;
			}
		}
		return false;
	}



	@Override
	public List<String> onTabComplete(final CommandSender sender, final String[] args) {
		if (!sender.hasPermission("pxn.cmd.spawn.other"))
			return null;
		return this.onTabComplete_Players(args);
	}



}
