package com.poixson.vitalcore.commands;

import static com.poixson.tools.commands.PluginCommand.ConsoleCannotUse;
import static com.poixson.tools.commands.PluginCommand.GetArg_Players;
import static com.poixson.tools.commands.PluginCommand.HasPermissionUseCMD;
import static com.poixson.tools.commands.PluginCommand.HasPermissionUseOthersCMD;
import static com.poixson.utils.BukkitUtils.CharToGameMode;
import static com.poixson.utils.Utils.IsEmpty;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_GM;
import static com.poixson.vitalcore.VitalCoreDefines.PERM_CMD_GM;
import static com.poixson.vitalcore.VitalCoreDefines.PERM_CMD_GM_A;
import static com.poixson.vitalcore.VitalCoreDefines.PERM_CMD_GM_A_OTHERS;
import static com.poixson.vitalcore.VitalCoreDefines.PERM_CMD_GM_C;
import static com.poixson.vitalcore.VitalCoreDefines.PERM_CMD_GM_C_OTHERS;
import static com.poixson.vitalcore.VitalCoreDefines.PERM_CMD_GM_OTHERS;
import static com.poixson.vitalcore.VitalCoreDefines.PERM_CMD_GM_S;
import static com.poixson.vitalcore.VitalCoreDefines.PERM_CMD_GM_SPEC;
import static com.poixson.vitalcore.VitalCoreDefines.PERM_CMD_GM_SPEC_OTHERS;
import static com.poixson.vitalcore.VitalCoreDefines.PERM_CMD_GM_S_OTHERS;
import static com.poixson.vitalcore.VitalCorePlugin.CHAT_PREFIX;

import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.poixson.tools.commands.PluginCommand;
import com.poixson.vitalcore.VitalCorePlugin;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;


// /gm
public interface CMD_GM extends PluginCommand {



	default ArgumentBuilder<CommandSourceStack, ?> register_GM(final VitalCorePlugin plugin) {
		return Commands.literal(CMD_LABELS_GM.NODE)
			.then(Commands.argument("gamemode", ArgumentTypes.gameMode())
				// /gm <gamemode>
				.executes(context -> this.onCommand_GM(context, plugin))
				// /gm <gamemode> <players>
				.then(Commands.argument("players", ArgumentTypes.players())
					.executes(context -> this.onCommand_GM(context, plugin))
				)
			);
	}



	default int onCommand_GM(final CommandContext<CommandSourceStack> context, final VitalCorePlugin plugin) {
		final CommandSourceStack source = context.getSource();
		final CommandSender sender = source.getSender();
		final Player[] others = GetArg_Players(context);
		final String mode_str = context.getArgument("gamemode", String.class);
		final GameMode mode = CharToGameMode(mode_str);
//TODO: check null mode
//		if (mode == null) {
//			sender.sendMessage(Component.text("Invalid game mode: "+
//				mode_str).color(NamedTextColor.RED));
//			return FAILURE;
//		}
		// self
		if (IsEmpty(others)) {
			// no console
			if (ConsoleCannotUse(sender))
				return FAILURE;
			// permission self
			if (!HasPermissionUseCMD(sender, PERM_CMD_GM.NODE))
				return FAILURE;
			final String perm_node;
			SWITCH_MODE:
			switch (mode) {
			case CREATIVE:  perm_node = PERM_CMD_GM_C   .NODE; break SWITCH_MODE;
			case SURVIVAL:  perm_node = PERM_CMD_GM_S   .NODE; break SWITCH_MODE;
			case ADVENTURE: perm_node = PERM_CMD_GM_A   .NODE; break SWITCH_MODE;
			case SPECTATOR: perm_node = PERM_CMD_GM_SPEC.NODE; break SWITCH_MODE;
			default: throw new IllegalArgumentException("Unknown game mode: "+mode.toString());
			}
			if (!HasPermissionUseCMD(sender, perm_node))
				return FAILURE;
			final Player self = (Player) sender;
			self.setGameMode(mode);
			sender.sendMessage(Component.text("Set your game mode to "+mode.name()));
		// other players
		} else {
			// permission others
			if (!HasPermissionUseOthersCMD(sender, PERM_CMD_GM_OTHERS.NODE))
				return FAILURE;
			final String perm_node;
			SWITCH_MODE:
			switch (mode) {
			case CREATIVE:  perm_node = PERM_CMD_GM_C_OTHERS   .NODE; break SWITCH_MODE;
			case SURVIVAL:  perm_node = PERM_CMD_GM_S_OTHERS   .NODE; break SWITCH_MODE;
			case ADVENTURE: perm_node = PERM_CMD_GM_A_OTHERS   .NODE; break SWITCH_MODE;
			case SPECTATOR: perm_node = PERM_CMD_GM_SPEC_OTHERS.NODE; break SWITCH_MODE;
			default: throw new IllegalArgumentException("Unknown game mode: "+mode.toString());
			}
			if (!HasPermissionUseOthersCMD(sender, perm_node))
				return FAILURE;
			int count = 0;
			for (final Player p : others) {
				p.setGameMode(mode);
				p.sendMessage(Component.textOfChildren(
					Component.text("Game mode: ").color(NamedTextColor.AQUA),
					Component.text(mode.name()  ).color(NamedTextColor.GOLD)
				));
			}
			if (count > 0) {
				sender.sendMessage(CHAT_PREFIX.append(Component.text(String.format(
					"Set game mode to %s for %d player%s",
					mode.name(),
					Integer.valueOf(count),
					(count == 1 ? "" : "s")
				)).color(NamedTextColor.AQUA)));
			}
		}
		return SUCCESS;
	}



}
