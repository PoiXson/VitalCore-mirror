package com.poixson.pluginlib.commands;

import static com.poixson.pluginlib.pxnPluginLib.CHAT_PREFIX;
import static com.poixson.utils.BukkitUtils.OpenEnderchest;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.poixson.pluginlib.pxnPluginLib;
import com.poixson.tools.commands.pxnCommandRoot;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;


// /enderchest
public class Command_Enderchest extends pxnCommandRoot {



	public Command_Enderchest(final pxnPluginLib plugin) {
		super(
			plugin,
			"pxn", // namespace
			"Open the Enderchest.", // desc
			null, // usage
			"pxn.cmd.enderchest", // perm
			// labels
			"enderchest"
		);
	}



	@Override
	public boolean onCommand(final CommandSender sender, final String[] args) {
		final Player player = (sender instanceof Player ? (Player)sender : null);
		final int num_args = args.length;
		// self
		if (num_args == 0) {
			if (player == null) {
				sender.sendMessage("Console cannot open an enderchest");
				return true;
			}
			if (!sender.hasPermission("pxn.cmd.enderchest"))
				return false;
			OpenEnderchest(player);
			return true;
		// other players
		} else {
			if (!sender.hasPermission("pxn.cmd.enderchest.other"))
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
				OpenEnderchest(p);
				count++;
			}
			if (count > 0) {
				sender.sendMessage(CHAT_PREFIX.append(Component.text(String.format(
					"Opened Enderchest for %d player%s",
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
		if (!sender.hasPermission("pxn.cmd.enderchest.other"))
			return null;
		return this.onTabComplete_Players(args);
	}



}
