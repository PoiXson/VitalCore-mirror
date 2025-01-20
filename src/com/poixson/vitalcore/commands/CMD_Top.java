package com.poixson.vitalcore.commands;

import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_TOP;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.poixson.tools.commands.PluginCommand;
import com.poixson.vitalcore.VitalCorePlugin;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;


// /top
public interface CMD_Top extends PluginCommand {



	default ArgumentBuilder<CommandSourceStack, ?> register_Top(final VitalCorePlugin plugin) {
		return Commands.literal(CMD_LABELS_TOP.NODE)
			// /top
			.executes(context -> this.onCommand_Top(context, plugin));
	}



	default int onCommand_Top(final CommandContext<CommandSourceStack> context, final VitalCorePlugin plugin) {
//TODO
context.getSource().getSender().sendPlainMessage("TOP!!!!!!!!!!!!!!!!!!!!!");
		return SUCCESS;
	}



}
