package com.poixson.vitalcore.commands;

import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_JUMP_INTO;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.poixson.tools.commands.PluginCommand;
import com.poixson.vitalcore.VitalCorePlugin;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;


// /jump-into
public interface CMD_Jump_Into extends PluginCommand {



	default ArgumentBuilder<CommandSourceStack, ?> register_Jump_Into(final VitalCorePlugin plugin) {
		return Commands.literal(CMD_LABELS_JUMP_INTO.NODE)
			// /jump-into
			.executes(context -> this.onCommand_Jump_Into(context, plugin));
	}



	default int onCommand_Jump_Into(final CommandContext<CommandSourceStack> context, final VitalCorePlugin plugin) {
//TODO
context.getSource().getSender().sendPlainMessage("JUMP-INTO!!!!!!!!!!!!!!!!!!!!!");
		return SUCCESS;
	}



}
