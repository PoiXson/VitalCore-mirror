package com.poixson.vitalcore.commands;

import static com.poixson.tools.commands.PluginCommand.ConsoleCannotUse;
import static com.poixson.tools.commands.PluginCommand.GetArg_Players;
import static com.poixson.tools.commands.PluginCommand.HasPermissionUseCMD;
import static com.poixson.tools.commands.PluginCommand.HasPermissionUseOthersCMD;
import static com.poixson.utils.BukkitUtils.AllowFlyPlayer;
import static com.poixson.utils.Utils.IsEmpty;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_FLY;
import static com.poixson.vitalcore.VitalCoreDefines.PERM_CMD_FLY;
import static com.poixson.vitalcore.VitalCoreDefines.PERM_CMD_FLY_OTHERS;
import static com.poixson.vitalcore.VitalCorePlugin.CHAT_PREFIX;

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


// /fly
public interface CMD_Fly extends PluginCommand {



	default ArgumentBuilder<CommandSourceStack, ?> register_Fly(final VitalCorePlugin plugin) {
		return Commands.literal(CMD_LABELS_FLY.NODE)
			// /fly
			.executes(context -> this.onCommand_Fly(context, plugin))
			// /fly <players>
			.then(Commands.argument("players", ArgumentTypes.players())
				.executes(context -> this.onCommand_Fly(context, plugin))
			);
	}



	default int onCommand_Fly(final CommandContext<CommandSourceStack> context, final VitalCorePlugin plugin) {
		final CommandSourceStack source = context.getSource();
		final CommandSender sender = source.getSender();
		final Player[] others = GetArg_Players(context);
		// self
		if (IsEmpty(others)) {
			// no console
			if (ConsoleCannotUse(sender))
				return FAILURE;
			// permission self
			if (!HasPermissionUseCMD(sender, PERM_CMD_FLY.NODE))
				return FAILURE;
			final Player self = (Player) sender;
			final boolean fly = ! self.getAllowFlight();
			AllowFlyPlayer(self, fly);
			sender.sendMessage(Component.text(
				fly ? "You can fly" : "Flying disabled"
			).color(NamedTextColor.GOLD));
		// other players
		} else {
			// permission others
			if (!HasPermissionUseOthersCMD(sender, PERM_CMD_FLY_OTHERS.NODE))
				return FAILURE;
			// find fly state
			boolean can_fly = true;
			for (final Player p : others) {
				if (!p.getAllowFlight())
					can_fly = false;
			}
			// set fly state
			int count = 0;
			final boolean set_fly = !can_fly;
			final Component msg = CHAT_PREFIX.append(Component.text(
				set_fly
				? "You can fly"
				: "Flying disabled"
			).color(NamedTextColor.GOLD));
			for (final Player p : others) {
				if (p.getAllowFlight() != set_fly) {
					AllowFlyPlayer(p, set_fly);
					p.sendMessage(msg);
					count++;
				}
			}
			if (count > 0) {
				sender.sendMessage(CHAT_PREFIX.append(Component.text(String.format(
					"Flying %s for %d player%s",
					(set_fly ? "enabled" : "disabled"),
					Integer.valueOf(count),
					(count == 1 ? "" : "s")
				)).color(NamedTextColor.AQUA)));
			}
		}
		return SUCCESS;
	}



}
