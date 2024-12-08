package com.poixson.pluginlib.commands;

import static com.poixson.pluginlib.pxnPluginLib.CHAT_PREFIX;
import static com.poixson.utils.Utils.IsEmpty;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.poixson.pluginlib.pxnPluginLib;
import com.poixson.tools.commands.pxnCommandRoot;
import com.poixson.utils.ArrayUtils;

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
		// other players
		if (num_args > 1) {
			if (!sender.hasPermission("pxn.cmd.gm.other"))
				return false;
			int count = 0;
			GameMode mode = null;
			LOOP_ARGS:
			for (final String arg : args) {
				if (mode == null) {
					mode = ShortToGameMode(arg);
					if (mode == null) {
						sender.sendMessage(CHAT_PREFIX.append(Component.text(
							"Invalid game mode: "+arg).color(NamedTextColor.RED)));
						return true;
					}
					final String md = GameModeToShort(mode);
					if (!sender.hasPermission("pxn.cmd.gm."+md+".other"))
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
				sender.sendMessage(CHAT_PREFIX.append(Component.text(
					"Game mode: "+mode.toString()).color(NamedTextColor.GOLD)));
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
		} else
		// single player
		if (num_args == 1) {
			if (player == null)
				return false;
			if (!sender.hasPermission("pxn.cmd.gm"))
				return false;
			final GameMode mode = ShortToGameMode(args[0]);
			if (mode == null) {
				sender.sendMessage(CHAT_PREFIX.append(Component.text(
					"Invalid game mode: "+args[0]).color(NamedTextColor.RED)));
				return true;
			}
			final String md = GameModeToShort(mode);
			if (!sender.hasPermission("pxn.cmd.gm."+md))
				return false;
			player.setGameMode(mode);
			return true;
		}
		return false;
	}



	@Override
	public List<String> onTabComplete(final CommandSender sender, final String[] args) {
		return ArrayUtils.MergeLists(
			this.onTabComplete_Players(args),
			this.onTabComplete_Array(args,
				"c", "creative",
				"s", "survival",
				"a", "adventure", "adv",  "advent",
				"sp","spectator", "spec", "spectate"
			)
		);
	}



	public static String GameModeToShort(final GameMode mode) {
		switch (mode) {
		case CREATIVE:  return "c";
		case SURVIVAL:  return "s";
		case ADVENTURE: return "a";
		case SPECTATOR: return "sp";
		default: throw new RuntimeException("Invalid game mode: "+mode.toString());
		}
	}
	public static GameMode ShortToGameMode(final String arg) {
		if (IsEmpty(arg)) return null;
		final String lower = arg.toLowerCase();
		if (lower.startsWith("sp")) return GameMode.SPECTATOR;
		if (lower.startsWith("c" )) return GameMode.CREATIVE;
		if (lower.startsWith("s" )) return GameMode.SURVIVAL;
		if (lower.startsWith("a" )) return GameMode.ADVENTURE;
		return null;
	}



}
