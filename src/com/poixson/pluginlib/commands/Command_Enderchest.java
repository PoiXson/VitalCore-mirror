package com.poixson.pluginlib.commands;

import static com.poixson.utils.BukkitUtils.OpenEnderchest;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.poixson.pluginlib.pxnPluginLib;
import com.poixson.tools.commands.pxnCommandRoot;


// /enderchest
public class Command_Enderchest extends pxnCommandRoot {



	public Command_Enderchest(final pxnPluginLib plugin) {
		super(
			plugin,
			"pxn", // namespace
			"Open the Enderchest.", // desc
			null, // usage
			"pxn.cmd.enderchest", // perm
			new String[] { // labels
				"enderchest"
			}
		);
	}



	@Override
	public boolean onCommand(final CommandSender sender, final String[] args) {
		final Player player = (sender instanceof Player ? (Player)sender : null);
		final int num_args = args.length;
		// other players
		if (num_args > 0) {
			if (!sender.hasPermission("pxn.cmd.enderchest.other"))
				return false;
			int count = 0;
			LOOP_ARG:
			for (final String arg : args) {
				final Player p = Bukkit.getPlayer(arg);
				if (p == null) {
					sender.sendMessage(String.format("%sPlayer not found: %s", ChatColor.GOLD, arg));
					continue LOOP_ARG;
				}
				OpenEnderchest(p);
				count++;
			}
			if (count > 0) {
				sender.sendMessage(String.format(
					"%sOpened Enderchest for %d player%s",
					ChatColor.AQUA,
					Integer.valueOf(count),
					(count == 1 ? "" : "s")
				));
				return true;
			}
		// single player
		} else {
			if (!sender.hasPermission("pxn.cmd.enderchest"))
				return false;
			OpenEnderchest(player);
			return true;
		}
		return false;
	}



	@Override
	public List<String> onTabComplete(final CommandSender sender, final String[] args) {
		return this.onTabComplete_Players(args);
	}



}
