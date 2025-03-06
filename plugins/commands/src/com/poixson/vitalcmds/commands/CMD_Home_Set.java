package com.poixson.vitalcore.commands;

import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_HOME_SET;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.poixson.tools.commands.PluginCommand;
import com.poixson.vitalcore.VitalCorePlugin;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;


// /sethome
public interface CMD_Home_Set extends PluginCommand {



	default ArgumentBuilder<CommandSourceStack, ?> register_Home_Set(final VitalCorePlugin plugin) {
		return Commands.literal(CMD_LABELS_HOME_SET.NODE)
			// /sethome
			.executes(context -> this.onCommand_Home_Set(context, plugin));
	}



	default int onCommand_Home_Set(final CommandContext<CommandSourceStack> context, final VitalCorePlugin plugin) {
//TODO
context.getSource().getSender().sendPlainMessage("Command is unfinished!");
return SUCCESS;
	}



}
