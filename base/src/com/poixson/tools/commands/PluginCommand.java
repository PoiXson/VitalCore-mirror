package com.poixson.tools.commands;

import static com.poixson.utils.Utils.IsEmpty;
import static com.poixson.vitalcore.VitalCoreDefines.LANG_CONSOLE_CANNOT_USE_CMD;
import static com.poixson.vitalcore.VitalCoreDefines.LANG_NO_PERMISSION_USE_CMD;
import static com.poixson.vitalcore.VitalCoreDefines.LANG_NO_PERMISSION_USE_CMD_ON_OTHERS;
import static com.poixson.vitalcore.VitalCoreDefines.Lang;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;


public interface PluginCommand {

	public static int SUCCESS = Command.SINGLE_SUCCESS;
	public static int FAILURE = -1;



	// -------------------------------------------------------------------------------
	// command argument getters



	// player
	public static Player GetArg_Player(final CommandContext<CommandSourceStack> context) {
		return GetArg_Player(context, "player");
	}
	public static Player GetArg_Player(final CommandContext<CommandSourceStack> context, final String name) {
		final String value = context.getArgument(name, String.class);
		return (IsEmpty(value) ? null : Bukkit.getPlayer(value));
	}



	// players
	public static Player[] GetArg_Players(final CommandContext<CommandSourceStack> context) {
		return GetArg_Players(context, "players");
	}
	public static Player[] GetArg_Players(final CommandContext<CommandSourceStack> context, final String name) {
		try {
			final PlayerSelectorArgumentResolver select_players =
				context.getArgument(name, PlayerSelectorArgumentResolver.class);
			final List<Player> players = select_players.resolve(context.getSource());
			return players.toArray(new Player[0]);
		} catch (CommandSyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}



	// -------------------------------------------------------------------------------
	// command logic helpers



	// only players - no console
	public static Player PlayerOnlyNoConsole(final CommandSender sender) {
		if (sender instanceof Player) {
			return (Player) sender;
		} else {
			sender.sendMessage(Lang.getPhrase(LANG_CONSOLE_CANNOT_USE_CMD.NODE));
			return null;
		}
	}



	// has permission
	public static boolean HasPermissionUseCMD(final CommandSender sender, final String node) {
		return HasPermissionUseCMD(sender, node, Lang.getPhrase(LANG_NO_PERMISSION_USE_CMD.NODE));
	}
	public static boolean HasPermissionUseOthersCMD(final CommandSender sender, final String node) {
		return HasPermissionUseCMD(sender, node, Lang.getPhrase(LANG_NO_PERMISSION_USE_CMD_ON_OTHERS.NODE));
	}
	public static boolean HasPermissionUseCMD(final CommandSender sender, final String node, final String msg) {
		if (IsEmpty(msg))
			return HasPermissionUseCMD(sender, node, "Permission Denied.");
		if (sender.hasPermission(node)) {
			return true;
		} else {
			sender.sendMessage(Component.text(msg).color(NamedTextColor.DARK_RED));
			return false;
		}
	}



	// console cannot use
	public static boolean ConsoleCannotUse(final CommandSender sender) {
		if (sender instanceof Player) {
			return false;
		} else {
			sender.sendMessage(Component.text(Lang.getPhrase(LANG_CONSOLE_CANNOT_USE_CMD.NODE)));
			return true;
		}
	}



}
