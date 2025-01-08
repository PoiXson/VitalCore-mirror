package com.poixson.pluginlib.commands;

import static com.poixson.pluginlib.pxnPluginLib.CHAT_PREFIX;
import static com.poixson.utils.BukkitUtils.FeedPlayer;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.poixson.pluginlib.pxnPluginLib;
import com.poixson.tools.commands.pxnCommandRoot;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;


// /feed
public class Command_Feed extends pxnCommandRoot {



	public Command_Feed(final pxnPluginLib plugin) {
		super(
			plugin,
			"pxn", // namespace
			"Restore full hunger.", // desc
			null, // usage
			"pxn.cmd.feed", // perm
			// labels
			"feed"
		);
	}



	@Override
	public boolean onCommand(final CommandSender sender, final String[] args) {
		final Player player = (sender instanceof Player ? (Player)sender : null);
		final int num_args = args.length;
		// self
		if (num_args == 0) {
			if (player == null) {
				sender.sendMessage("Cannot feed console");
				return true;
			}
			if (!sender.hasPermission("pxn.cmd.feed"))
				return false;
			FeedPlayer(player);
			sender.sendMessage(Component.text("You are fed").color(NamedTextColor.GREEN));
			return true;
		// other players
		} else {
			if (!sender.hasPermission("pxn.cmd.feed.other"))
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
				FeedPlayer(p);
				p.sendMessage(Component.text("You are fed").color(NamedTextColor.GREEN));
				count++;
			}
			if (count > 0) {
				sender.sendMessage(CHAT_PREFIX.append(Component.text(String.format(
					"Fed %d player%s",
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
		if (!sender.hasPermission("pxn.cmd.feed.other"))
			return null;
		return this.onTabComplete_Players(args);
	}



}
