package com.poixson.pluginlib.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.poixson.pluginlib.pxnPluginLib;
import com.poixson.tools.commands.pxnCommandRoot;


// /gmspec
public class Command_GMSpec extends pxnCommandRoot {



	public Command_GMSpec(final pxnPluginLib plugin) {
		super(
			plugin,
			"pxn", // namespace
			"Change game mode to Spectator.", // desc
			null, // usage
			"pxn.cmd.gm.spec", // perm
			new String[] { // labels
				"gmsp",   "gm-sp",
				"gmspec", "gm-spec"
			}
		);
	}



	@Override
	public boolean onCommand(final CommandSender sender, final String[] args) {
		final Player player = (sender instanceof Player ? (Player)sender : null);
		final int num_args = args.length;
		// other players
		if (num_args > 0) {
			if (!sender.hasPermission("pxn.cmd.gm.sp.other"))
				return false;
			int count = 0;
			LOOP_ARGS:
			for (final String arg : args) {
				final Player p = Bukkit.getPlayer(arg);
				if (p == null) {
					sender.sendMessage(String.format("%sPlayer not found: %s", ChatColor.RED, arg));
					continue LOOP_ARGS;
				}
				p.setGameMode(GameMode.SPECTATOR);
				p.sendMessage(ChatColor.GOLD+"Game mode: "+GameMode.SPECTATOR.toString());
				count++;
			}
			if (count > 0) {
				sender.sendMessage(String.format(
					"%sSet game mode to %s for %d player%s",
					ChatColor.AQUA,
					GameMode.SPECTATOR.toString(),
					Integer.valueOf(count),
					(count == 1 ? "" : "s")
				));
				return true;
			}
		// single player
		} else {
			if (player == null)
				return false;
			if (!sender.hasPermission("pxn.cmd.gm.sp"))
				return false;
			player.setGameMode(GameMode.SPECTATOR);
			return true;
		}
		return false;
	}



	@Override
	public List<String> onTabComplete(final CommandSender sender, final String[] args) {
		return this.onTabComplete_Players(args);
	}



}
