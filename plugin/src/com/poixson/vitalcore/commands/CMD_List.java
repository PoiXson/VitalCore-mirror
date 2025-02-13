package com.poixson.vitalcore.commands;

import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_LIST;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.poixson.tools.commands.PluginCommand;
import com.poixson.vitalcore.VitalCorePlugin;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;


// /list
public interface CMD_List extends PluginCommand {



	default ArgumentBuilder<CommandSourceStack, ?> register_List(final VitalCorePlugin plugin) {
		return Commands.literal(CMD_LABELS_LIST.NODE)
			// /list
			.executes(context -> this.onCommand_List(context, plugin));
	}



	default int onCommand_List(final CommandContext<CommandSourceStack> context, final VitalCorePlugin plugin) {
//TODO
context.getSource().getSender().sendPlainMessage("Command is unfinished!");
return SUCCESS;
	}



}
