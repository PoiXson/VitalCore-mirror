package com.poixson.pluginlib.commands;

import static com.poixson.pluginlib.pxnPluginLib.CHAT_PREFIX;
import static com.poixson.utils.BukkitUtils.RestPlayer;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.poixson.pluginlib.pxnPluginLib;
import com.poixson.tools.commands.pxnCommandRoot;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;


// /rest
public class Command_Rest extends pxnCommandRoot {



	public Command_Rest(final pxnPluginLib plugin) {
		super(
			plugin,
			"pxn", // namespace
			"Restore full rest.", // desc
			null, // usage
			"pxn.cmd.rest", // perm
			new String[] { // labels
				"rest"
			}
		);
	}



	@Override
	public boolean onCommand(final CommandSender sender, final String[] args) {
		final Player player = (sender instanceof Player ? (Player)sender : null);
		final int num_args = args.length;
		// other players
		if (num_args > 0) {
			if (!sender.hasPermission("pxn.cmd.rest.other"))
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
				RestPlayer(p);
				p.sendMessage(CHAT_PREFIX.append(Component.text(
					"You are rested").color(NamedTextColor.AQUA)));
				count++;
			}
			if (count > 0) {
				sender.sendMessage(CHAT_PREFIX.append(Component.text(String.format(
					"Rested %d player%s",
					Integer.valueOf(count),
					(count == 1 ? "" : "s")
				)).color(NamedTextColor.AQUA)));
				return true;
			}
		// single player
		} else {
			if (!sender.hasPermission("pxn.cmd.rest"))
				return false;
			if (player == null) {
				sender.sendMessage("Cannot rest console");
				return true;
			}
			RestPlayer(player);
			sender.sendMessage(CHAT_PREFIX.append(Component.text(
				"You are rested").color(NamedTextColor.GOLD)));
			return true;
		}
		return false;
	}



	@Override
	public List<String> onTabComplete(final CommandSender sender, final String[] args) {
		return this.onTabComplete_Players(args);
	}



}
