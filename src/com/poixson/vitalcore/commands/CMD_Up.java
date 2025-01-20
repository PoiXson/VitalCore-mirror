package com.poixson.vitalcore.commands;

import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_UP;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.poixson.tools.commands.PluginCommand;
import com.poixson.vitalcore.VitalCorePlugin;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;


// /up
public interface CMD_Up extends PluginCommand {



	default ArgumentBuilder<CommandSourceStack, ?> register_Up(final VitalCorePlugin plugin) {
		return Commands.literal(CMD_LABELS_UP.NODE)
			// /up
			.executes(context -> this.onCommand_Up(context, plugin));
	}



	default int onCommand_Up(final CommandContext<CommandSourceStack> context, final VitalCorePlugin plugin) {
//TODO
context.getSource().getSender().sendPlainMessage("UP!!!!!!!!!!!!!!!!!!!!!");
		return SUCCESS;
	}



}
