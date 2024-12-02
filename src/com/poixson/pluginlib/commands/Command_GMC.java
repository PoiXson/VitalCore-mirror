package com.poixson.pluginlib.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.poixson.pluginlib.pxnPluginLib;
import com.poixson.tools.commands.pxnCommandRoot;


// /gmc
public class Command_GMC extends pxnCommandRoot {



	public Command_GMC(final pxnPluginLib plugin) {
		super(
			plugin,
			"pxn", // namespace
			"Change game mode to Creative.", // desc
			null, // usage
			"pxn.cmd.gm.c", // perm
			new String[] { // labels
				"gmc", "gm-c",
				"gmcreative", "gm-creative"
			}
		);
	}



	@Override
	public boolean onCommand(final CommandSender sender, final String[] args) {
		final Player player = (sender instanceof Player ? (Player)sender : null);
		final int num_args = args.length;
		// other players
		if (num_args > 0) {
			if (!sender.hasPermission("pxn.cmd.gm.c.other"))
				return false;
			int count = 0;
			LOOP_ARGS:
			for (final String arg : args) {
				final Player p = Bukkit.getPlayer(arg);
				if (p == null) {
					sender.sendMessage(String.format("%sPlayer not found: %s", ChatColor.RED, arg));
					continue LOOP_ARGS;
				}
				p.setGameMode(GameMode.CREATIVE);
				p.sendMessage(ChatColor.GOLD+"Game mode: "+GameMode.CREATIVE.toString());
				count++;
			}
			if (count > 0) {
				sender.sendMessage(String.format(
					"%sSet game mode to %s for %d player%s",
					ChatColor.AQUA,
					GameMode.CREATIVE.toString(),
					Integer.valueOf(count),
					(count == 1 ? "" : "s")
				));
				return true;
			}
		// single player
		} else {
			if (player == null)
				return false;
			if (!sender.hasPermission("pxn.cmd.gm.c"))
				return false;
			player.setGameMode(GameMode.CREATIVE);
			return true;
		}
		return false;
	}



	@Override
	public List<String> onTabComplete(final CommandSender sender, final String[] args) {
		return this.onTabComplete_Players(args);
	}



}
