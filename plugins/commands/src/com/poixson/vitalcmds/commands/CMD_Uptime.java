package com.poixson.vitalcore.commands;

import static com.poixson.tools.commands.PluginCommand.HasPermissionUseCMD;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_UPTIME;
import static com.poixson.vitalcore.VitalCoreDefines.PERM_CMD_UPTIME;
import static com.poixson.vitalcore.VitalCorePlugin.CHAT_PREFIX;

import org.bukkit.command.CommandSender;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.poixson.tools.commands.PluginCommand;
import com.poixson.vitalcore.VitalCorePlugin;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;


// /uptime
public interface CMD_Uptime extends PluginCommand {



	default ArgumentBuilder<CommandSourceStack, ?> register_Uptime(final VitalCorePlugin plugin) {
		// /uptime
		return Commands.literal(CMD_LABELS_UPTIME.NODE)
			.executes(context -> this.onCommand_Uptime(context, plugin));
	}



	default int onCommand_Uptime(final CommandContext<CommandSourceStack> context, final VitalCorePlugin plugin) {
		final CommandSourceStack source = context.getSource();
		final CommandSender sender = source.getSender();
		if (!HasPermissionUseCMD(sender, PERM_CMD_UPTIME.NODE))
			return FAILURE;
		sender.sendMessage(CHAT_PREFIX
			.append(Component.text("Uptime: "                 ).color(NamedTextColor.AQUA))
			.append(Component.text(plugin.getUptimeFormatted()).color(NamedTextColor.GOLD))
		);
		return SUCCESS;
	}



}
