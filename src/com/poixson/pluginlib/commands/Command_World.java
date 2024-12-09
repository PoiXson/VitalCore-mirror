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
import com.poixson.utils.ArrayUtils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;


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
		// current world
		if (num_args == 0) {
			if (player == null) {
				sender.sendMessage("Cannot find world for console");
				return true;
			}
			final World world = player.getWorld();
			final String world_name = world.getName();
			player.sendMessage(Component.empty()
				.append(Component.text("World: " ).color(NamedTextColor.AQUA))
				.append(Component.text(world_name).color(NamedTextColor.GOLD))
			);
			return true;
		}
		final String world_name = args[0];
		final World world = Bukkit.getWorld(world_name);
		if (world == null) {
			sender.sendMessage(CHAT_PREFIX.append(Component.text(
				"Unknown world: "+world_name).color(NamedTextColor.RED)));
			return true;
		}
		final Location loc = world.getSpawnLocation();
		// self
		if (num_args == 1) {
			if (player == null) {
				sender.sendMessage("Cannot teleport console");
				return true;
			}
			if (!player.hasPermission("pxn.cmd.world"))
				return false;
			player.teleport(loc);
			player.sendMessage(Component.empty()
				.append(Component.text("Teleported to world: ").color(NamedTextColor.AQUA))
				.append(Component.text(world_name             ).color(NamedTextColor.GOLD))
			);
			return true;
		// other players
		} else {
			if (!sender.hasPermission("pxn.cmd.world.other"))
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
				p.teleport(loc);
				p.sendMessage(Component.empty()
					.append(Component.text("Teleported to world: ").color(NamedTextColor.AQUA))
					.append(Component.text(world_name             ).color(NamedTextColor.GOLD))
				);
				count++;
			}
			if (count > 0) {
				sender.sendMessage(CHAT_PREFIX.append(Component.text(String.format(
					"Teleported %d player%s to world: %s",
					Integer.valueOf(count),
					(count == 1 ? "" : "s"),
					world_name
				)).color(NamedTextColor.AQUA)));
				return true;
			}
		}
		return false;
	}



	@Override
	public List<String> onTabComplete(final CommandSender sender, final String[] args) {
		if (!sender.hasPermission("pxn.cmd.world"))
			return null;
		if (sender.hasPermission("pxn.cmd.world.other")) {
			return ArrayUtils.MergeLists(
				this.onTabComplete_Worlds(args),
				this.onTabComplete_Players(args)
			);
		}
		return this.onTabComplete_Worlds(args);
	}



}
