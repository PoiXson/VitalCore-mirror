package com.poixson.vitalcore.commands;

import static com.poixson.tools.commands.PluginCommand.ConsoleCannotUse;
import static com.poixson.tools.commands.PluginCommand.GetArg_Players;
import static com.poixson.tools.commands.PluginCommand.HasPermissionUseCMD;
import static com.poixson.tools.commands.PluginCommand.HasPermissionUseOthersCMD;
import static com.poixson.utils.Utils.IsEmpty;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_GM_SPEC;
import static com.poixson.vitalcore.VitalCoreDefines.PERM_CMD_GM_SPEC;
import static com.poixson.vitalcore.VitalCoreDefines.PERM_CMD_GM_SPEC_OTHERS;
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


// /gmspec
public interface CMD_GM_Spec extends PluginCommand {



	default ArgumentBuilder<CommandSourceStack, ?> register_GM_Spec(final VitalCorePlugin plugin) {
		return Commands.literal(CMD_LABELS_GM_SPEC.NODE)
			// /gmspec
			.executes(context -> this.onCommand_GM_SP(context, plugin))
			// /gmspec <players>
			.then(Commands.argument("players", ArgumentTypes.players())
				.executes(context -> this.onCommand_GM_SP(context, plugin))
			);
	}



	default int onCommand_GM_SP(final CommandContext<CommandSourceStack> context, final VitalCorePlugin plugin) {
		final CommandSourceStack source = context.getSource();
		final CommandSender sender = source.getSender();
		final Player[] others = GetArg_Players(context);
		// self
		if (IsEmpty(others)) {
			// no console
			if (ConsoleCannotUse(sender))
				return FAILURE;
			// permission self
			if (!HasPermissionUseCMD(sender, PERM_CMD_GM_SPEC.NODE))
				return FAILURE;
			final Player self = (Player) sender;
			self.setGameMode(GameMode.SPECTATOR);
			sender.sendMessage(Component.text("Set your game mode to "+GameMode.SPECTATOR.name()));
		// other players
		} else {
			// permission others
			if (!HasPermissionUseOthersCMD(sender, PERM_CMD_GM_SPEC_OTHERS.NODE))
				return FAILURE;
			int count = 0;
			for (final Player p : others) {
				if (!GameMode.SPECTATOR.equals(p.getGameMode())) {
					p.setGameMode(GameMode.SPECTATOR);
					p.sendMessage(Component.textOfChildren(
						Component.text("Game mode: "            ).color(NamedTextColor.AQUA),
						Component.text(GameMode.SPECTATOR.name()).color(NamedTextColor.GOLD)
					));
					count++;
				}
			}
			if (count > 0) {
				sender.sendMessage(CHAT_PREFIX.append(Component.text(String.format(
					"Set game mode to %s for %d player%s",
					GameMode.SPECTATOR.name(),
					Integer.valueOf(count),
					(count == 1 ? "" : "s")
				)).color(NamedTextColor.AQUA)));
			}
		}
		return SUCCESS;
	}



}
