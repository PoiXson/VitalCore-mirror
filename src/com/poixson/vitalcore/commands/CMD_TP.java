package com.poixson.vitalcore.commands;

import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_TP;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.poixson.tools.commands.PluginCommand;
import com.poixson.vitalcore.VitalCorePlugin;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;


// /tp
public interface CMD_TP extends PluginCommand {



	default ArgumentBuilder<CommandSourceStack, ?> register_TP(final VitalCorePlugin plugin) {
		return Commands.literal(CMD_LABELS_TP.NODE)
			// /tp
			.executes(context -> this.onCommand_TP(context, plugin));
	}



	default int onCommand_TP(final CommandContext<CommandSourceStack> context, final VitalCorePlugin plugin) {
//TODO
context.getSource().getSender().sendPlainMessage("TP!!!!!!!!!!!!!!!!!!!!!");
		return SUCCESS;
	}



}
