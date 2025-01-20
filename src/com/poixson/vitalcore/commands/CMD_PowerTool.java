package com.poixson.vitalcore.commands;

import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_POWERTOOL;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.poixson.tools.commands.PluginCommand;
import com.poixson.vitalcore.VitalCorePlugin;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;


// /powertool
public interface CMD_PowerTool extends PluginCommand {



	default ArgumentBuilder<CommandSourceStack, ?> register_PowerTool(final VitalCorePlugin plugin) {
		return Commands.literal(CMD_LABELS_POWERTOOL.NODE)
			// /powertool
			.executes(context -> this.onCommand_PowerTool(context, plugin));
	}



	default int onCommand_PowerTool(final CommandContext<CommandSourceStack> context, final VitalCorePlugin plugin) {
//TODO
context.getSource().getSender().sendPlainMessage("POWERTOOL!!!!!!!!!!!!!!!!!!!!!");
		return SUCCESS;
	}



}
