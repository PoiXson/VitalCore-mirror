package com.poixson.vitalcore.commands;

import static com.poixson.tools.commands.PluginCommand.ConsoleCannotUse;
import static com.poixson.tools.commands.PluginCommand.HasPermissionUseCMD;
import static com.poixson.utils.BukkitUtils.OpenWorkbench;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_WORKBENCH;
import static com.poixson.vitalcore.VitalCoreDefines.PERM_CMD_WORKBENCH;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.poixson.tools.commands.PluginCommand;
import com.poixson.vitalcore.VitalCorePlugin;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;


// /workbench
public interface CMD_Workbench extends PluginCommand {



	default ArgumentBuilder<CommandSourceStack, ?> register_Workbench(final VitalCorePlugin plugin) {
		return Commands.literal(CMD_LABELS_WORKBENCH.NODE)
			// /workbench
			.executes(context -> this.onCommand_Workbench(context, plugin));
	}



	default int onCommand_Workbench(final CommandContext<CommandSourceStack> context, final VitalCorePlugin plugin) {
		final CommandSourceStack source = context.getSource();
		final CommandSender sender = source.getSender();
		// no console
		if (ConsoleCannotUse(sender))
			return FAILURE;
		// permission self
		if (!HasPermissionUseCMD(sender, PERM_CMD_WORKBENCH.NODE))
			return FAILURE;
		final Player self = (Player) sender;
		OpenWorkbench(self);
		return SUCCESS;
	}



}
