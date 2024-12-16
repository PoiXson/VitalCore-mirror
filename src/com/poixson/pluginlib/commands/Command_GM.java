package com.poixson.pluginlib.commands;

import static com.poixson.pluginlib.pxnPluginLib.CHAT_PREFIX;
import static com.poixson.utils.ArrayUtils.MergeLists;
import static com.poixson.utils.BukkitUtils.CharToGameMode;
import static com.poixson.utils.BukkitUtils.GameModeToChar;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.poixson.pluginlib.pxnPluginLib;
import com.poixson.tools.commands.pxnCommandRoot;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;


// /gm
public class Command_GM extends pxnCommandRoot {



	public Command_GM(final pxnPluginLib plugin) {
		super(
			plugin,
			"pxn", // namespace
			"Change game mode.", // desc
			null, // usage
			"pxn.cmd.gm", // perm
			new String[] { // labels
				"gm", "gamemode", "game-mode"
			}
		);
	}



	@Override
	public boolean onCommand(final CommandSender sender, final String[] args) {
		final Player player = (sender instanceof Player ? (Player)sender : null);
		final int num_args = args.length;
		// self
		if (num_args == 1) {
			if (player == null) {
				sender.sendMessage("Cannot change game mode for console");
				return true;
			}
			if (!sender.hasPermission("pxn.cmd.gm"))
				return false;
			final GameMode mode = CharToGameMode(args[0]);
			if (mode == null) {
				sender.sendMessage(Component.text("Invalid game mode: "+
					args[0]).color(NamedTextColor.RED));
				return true;
			}
			final char m = GameModeToChar(mode);
			if (!sender.hasPermission("pxn.cmd.gm."+m))
				return false;
			player.setGameMode(mode);
			player.sendMessage(Component.textOfChildren(
				Component.text("Game mode: "  ).color(NamedTextColor.AQUA),
				Component.text(mode.toString()).color(NamedTextColor.GOLD)
			));
			return true;
		} else
		// other players
		if (num_args > 1) {
			if (!sender.hasPermission("pxn.cmd.gm.other"))
				return false;
			int count = 0;
			GameMode mode = null;
			LOOP_ARGS:
			for (final String arg : args) {
				if (mode == null) {
					mode = CharToGameMode(arg);
					if (mode == null) {
						sender.sendMessage(CHAT_PREFIX.append(Component.text(
							"Invalid game mode: "+arg).color(NamedTextColor.RED)));
						return true;
					}
					final char m = GameModeToChar(mode);
					if (!sender.hasPermission("pxn.cmd.gm."+m+".other"))
						return false;
					continue LOOP_ARGS;
				}
				final Player p = Bukkit.getPlayer(arg);
				if (p == null) {
					sender.sendMessage(CHAT_PREFIX.append(Component.text(
						"Player not found: "+arg).color(NamedTextColor.RED)));
					continue LOOP_ARGS;
				}
				p.setGameMode(mode);
				player.sendMessage(Component.textOfChildren(
					Component.text("Game mode: "  ).color(NamedTextColor.AQUA),
					Component.text(mode.toString()).color(NamedTextColor.GOLD)
				));
				count++;
			}
			if (count > 0) {
				sender.sendMessage(CHAT_PREFIX.append(Component.text(String.format(
					"Set game mode to %s for %d player%s",
					mode.toString(),
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
		List<String> result = new LinkedList<String>();
		if (sender.hasPermission("pxn.cmd.gm.c")) result = MergeLists(result, this.onTabComplete_Array(args, "c", "creative"                           ));
		if (sender.hasPermission("pxn.cmd.gm.s")) result = MergeLists(result, this.onTabComplete_Array(args, "s", "survival"                           ));
		if (sender.hasPermission("pxn.cmd.gm.a")) result = MergeLists(result, this.onTabComplete_Array(args, "a", "adv", "adventure"                   ));
		if (sender.hasPermission("pxn.cmd.gm.sp"   )) result = MergeLists(result, this.onTabComplete_Array(args, "p", "sp", "spec", "spectate", "spectator"));
		if (sender.hasPermission("pxn.cmd.gm.other")) result = MergeLists(result, this.onTabComplete_Players(args));
		return result;
	}



}
