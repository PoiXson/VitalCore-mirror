package com.poixson.pluginlib.commands;

import static com.poixson.utils.BukkitUtils.AllowFlyPlayer;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.poixson.pluginlib.pxnPluginLib;
import com.poixson.tools.commands.pxnCommandRoot;


// /fly
public class Command_Fly extends pxnCommandRoot {



	public Command_Fly(final pxnPluginLib plugin) {
		super(
			plugin,
			"pxn", // namespace
			"Toggle allow flying.", // desc
			null, // usage
			"pxn.cmd.fly", // perm
			new String[] { // labels
				"fly"
			}
		);
	}



	@Override
	public boolean onCommand(final CommandSender sender, final String[] args) {
		final Player player = (sender instanceof Player ? (Player)sender : null);
		final int num_args = args.length;
		// other players
		if (num_args > 0) {
			if (!sender.hasPermission("pxn.cmd.fly.other"))
				return false;
			int count = 0;
			boolean can_fly = true;
			LOOP_ARG:
			for (final String arg : args) {
				final Player p = Bukkit.getPlayer(arg);
				if (p == null) {
					sender.sendMessage(String.format("%sPlayer not found: %s", ChatColor.GOLD, arg));
					continue LOOP_ARG;
				}
				if (count == 0)
					can_fly = ! p.getAllowFlight();
				AllowFlyPlayer(p, can_fly);
				if (can_fly) p.sendMessage(ChatColor.GOLD+"You can fly");
				else         p.sendMessage(ChatColor.GOLD+"Flying disabled");
				count++;
			}
			if (count > 0) {
				sender.sendMessage(String.format(
					(can_fly
						? "%sFlying enabled for %d player%s"
						: "%sFlying disabled for %d player%s" ),
					ChatColor.AQUA,
					Integer.valueOf(count),
					(count == 1 ? "" : "s")
				));
				return true;
			}
		// single player
		} else {
			if (!sender.hasPermission("pxn.cmd.fly"))
				return false;
			final boolean can_fly = ! player.getAllowFlight();
			AllowFlyPlayer(player, can_fly);
			if (can_fly) sender.sendMessage(ChatColor.GOLD+"You can fly");
			else         sender.sendMessage(ChatColor.GOLD+"Flying disabled");
			return true;
		}
		return false;
	}



	@Override
	public List<String> onTabComplete(final CommandSender sender, final String[] args) {
		return this.onTabComplete_Players(args);
	}



}
