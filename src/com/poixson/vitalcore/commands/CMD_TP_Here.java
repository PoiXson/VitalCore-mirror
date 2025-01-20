package com.poixson.vitalcore.commands;

import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_TP_HERE;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.poixson.tools.commands.PluginCommand;
import com.poixson.vitalcore.VitalCorePlugin;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;


// /tp-here
public interface CMD_TP_Here extends PluginCommand {



	default ArgumentBuilder<CommandSourceStack, ?> register_TP_Here(final VitalCorePlugin plugin) {
		return Commands.literal(CMD_LABELS_TP_HERE.NODE)
			// /tp-here
			.executes(context -> this.onCommand_TP_Here(context, plugin));
	}



	default int onCommand_TP_Here(final CommandContext<CommandSourceStack> context, final VitalCorePlugin plugin) {
//TODO
context.getSource().getSender().sendPlainMessage("TP-HERE!!!!!!!!!!!!!!!!!!!!!");
		return SUCCESS;
	}



}
