package com.poixson.pluginlib.commands;

import static com.poixson.utils.BukkitUtils.OpenWorkbench;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.poixson.pluginlib.pxnPluginLib;
import com.poixson.tools.commands.pxnCommandRoot;


// /workbench
public class Command_Workbench extends pxnCommandRoot {



	public Command_Workbench(final pxnPluginLib plugin) {
		super(
			plugin,
			"pxn", // namespace
			null, // desc
			null, // usage
			"pxn.cmd.workbench", // perm
			new String[] { // labels
				"workbench",
				"craftingtable"
			}
		);
	}



	@Override
	public boolean onCommand(final CommandSender sender, final String[] args) {
		final Player player = (sender instanceof Player ? (Player)sender : null);
		final int num_args = args.length;
		// other players
		if (num_args > 0) {
			if (!sender.hasPermission("pxn.cmd.workbench.other"))
				return false;
			int count = 0;
			ARG_LOOP:
			for (final String arg : args) {
				final Player p = Bukkit.getPlayer(arg);
				if (p == null) {
					sender.sendMessage(String.format("%sPlayer not found: %s", ChatColor.GOLD, arg));
					continue ARG_LOOP;
				}
				OpenWorkbench(p);
				count++;
			}
			if (count > 0) {
				sender.sendMessage(String.format(
					"%sOpened Workbench for %d player%s",
					ChatColor.AQUA,
					Integer.valueOf(count),
					(count == 1 ? "" : "s")
				));
				return true;
			}
		// single player
		} else {
			if (!sender.hasPermission("pxn.cmd.workbench"))
				return false;
			OpenWorkbench(player);
			return true;
		}
		return false;
	}



	@Override
	public List<String> onTabComplete(final CommandSender sender, final String[] args) {
		return this.onTabComplete_Players(args);
	}



}
