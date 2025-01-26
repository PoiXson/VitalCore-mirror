package com.poixson.vitalcore.commands;

import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_HELP;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.poixson.tools.commands.PluginCommand;
import com.poixson.vitalcore.VitalCorePlugin;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;


// /help
public interface CMD_Help extends PluginCommand {



	default ArgumentBuilder<CommandSourceStack, ?> register_Help(final VitalCorePlugin plugin) {
		return Commands.literal(CMD_LABELS_HELP.NODE)
			// /help
			.executes(context -> this.onCommand_Help(context, plugin));
	}



	default int onCommand_Help(final CommandContext<CommandSourceStack> context, final VitalCorePlugin plugin) {
//TODO
context.getSource().getSender().sendPlainMessage("Command is unfinished!");
return SUCCESS;
	}



}
