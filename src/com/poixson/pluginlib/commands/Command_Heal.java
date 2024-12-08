package com.poixson.pluginlib.commands;

import static com.poixson.pluginlib.pxnPluginLib.CHAT_PREFIX;
import static com.poixson.utils.BukkitUtils.HealPlayer;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.poixson.pluginlib.pxnPluginLib;
import com.poixson.tools.commands.pxnCommandRoot;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;


// /heal
public class Command_Heal extends pxnCommandRoot {



	public Command_Heal(final pxnPluginLib plugin) {
		super(
			plugin,
			"pxn", // namespace
			"Restore full health.", // desc
			null, // usage
			"pxn.cmd.heal", // perm
			new String[] { // labels
				"heal"
			}
		);
	}



	@Override
	public boolean onCommand(final CommandSender sender, final String[] args) {
		final Player player = (sender instanceof Player ? (Player)sender : null);
		final int num_args = args.length;
		// other players
		if (num_args > 0) {
			if (!sender.hasPermission("pxn.cmd.heal.other"))
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
				HealPlayer(p);
				sender.sendMessage(CHAT_PREFIX.append(Component.text(
					"You are healed").color(NamedTextColor.GOLD)));
				count++;
			}
			if (count > 0) {
				sender.sendMessage(CHAT_PREFIX.append(Component.text(String.format(
					"Healed %d player%s",
					Integer.valueOf(count),
					(count == 1 ? "" : "s")
				)).color(NamedTextColor.AQUA)));
				return true;
			}
		// single player
		} else {
			if (!sender.hasPermission("pxn.cmd.heal"))
				return false;
			if (player == null) {
				sender.sendMessage("Cannot heal console");
				return true;
			}
			HealPlayer(player);
			sender.sendMessage(CHAT_PREFIX.append(Component.text(
				"You are healed").color(NamedTextColor.GOLD)));
			return true;
		}
		return false;
	}



	@Override
	public List<String> onTabComplete(final CommandSender sender, final String[] args) {
		if (!sender.hasPermission("pxn.cmd.heal.other"))
			return null;
		return this.onTabComplete_Players(args);
	}



}
