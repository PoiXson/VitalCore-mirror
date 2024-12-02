package com.poixson.pluginlib.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.poixson.pluginlib.pxnPluginLib;
import com.poixson.tools.commands.pxnCommandRoot;


// /gms
public class Command_GMS extends pxnCommandRoot {



	public Command_GMS(final pxnPluginLib plugin) {
		super(
			plugin,
			"pxn", // namespace
			"Change game mode to Survival.", // desc
			null, // usage
			"pxn.cmd.gm.s", // perm
			new String[] { // labels
				"gms", "gm-s",
				"gmsurvival", "gm-survival"
			}
		);
	}



	@Override
	public boolean onCommand(final CommandSender sender, final String[] args) {
		final Player player = (sender instanceof Player ? (Player)sender : null);
		final int num_args = args.length;
		// other players
		if (num_args > 0) {
			if (!sender.hasPermission("pxn.cmd.gm.s.other"))
				return false;
			int count = 0;
			LOOP_ARGS:
			for (final String arg : args) {
				final Player p = Bukkit.getPlayer(arg);
				if (p == null) {
					sender.sendMessage(String.format("%sPlayer not found: %s", ChatColor.RED, arg));
					continue LOOP_ARGS;
				}
				p.setGameMode(GameMode.SURVIVAL);
				p.sendMessage(ChatColor.GOLD+"Game mode: "+GameMode.SURVIVAL.toString());
				count++;
			}
			if (count > 0) {
				sender.sendMessage(String.format(
					"%sSet game mode to %s for %d player%s",
					ChatColor.AQUA,
					GameMode.SURVIVAL.toString(),
					Integer.valueOf(count),
					(count == 1 ? "" : "s")
				));
				return true;
			}
		// single player
		} else {
			if (player == null)
				return false;
			if (!sender.hasPermission("pxn.cmd.gm.s"))
				return false;
			player.setGameMode(GameMode.SURVIVAL);
			return true;
		}
		return false;
	}



	@Override
	public List<String> onTabComplete(final CommandSender sender, final String[] args) {
		return this.onTabComplete_Players(args);
	}



}
