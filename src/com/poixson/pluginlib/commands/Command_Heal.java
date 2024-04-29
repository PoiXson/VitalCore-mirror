package com.poixson.pluginlib.commands;

import static com.poixson.utils.BukkitUtils.HealPlayer;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.poixson.pluginlib.pxnPluginLib;
import com.poixson.tools.commands.pxnCommandRoot;


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
//TODO: rest
	}



	@Override
	public boolean onCommand(final CommandSender sender, final String[] args) {
		final Player player = (sender instanceof Player ? (Player)sender : null);
		final int num_args = args.length;
		// other players
		if (num_args > 0) {
			int count = 0;
			ARG_LOOP:
			for (final String arg : args) {
				final Player p = Bukkit.getPlayer(arg);
				if (p == null) {
					sender.sendMessage(String.format("%sPlayer not found: %s", ChatColor.GOLD, arg));
					continue ARG_LOOP;
				}
				HealPlayer(p);
				p.sendMessage(ChatColor.GOLD+"You are healed");
				count++;
			}
			if (count > 0) {
				sender.sendMessage(String.format(
					"Healed %d player%s",
					Integer.valueOf(count),
					(count == 1 ? "" : "s")
				));
				return true;
			}
		// single player
		} else {
			HealPlayer(player);
			sender.sendMessage(ChatColor.GOLD+"You are healed");
			return true;
		}
		return false;
	}



	@Override
	public List<String> onTabComplete(final CommandSender sender, final String[] args) {
//TODO
System.out.println("TAB:"); for (final String arg : args) System.out.println("  "+arg);
return null;
	}



}
