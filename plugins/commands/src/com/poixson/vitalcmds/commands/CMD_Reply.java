package com.poixson.vitalcore.commands;

import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_REPLY;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.poixson.tools.commands.PluginCommand;
import com.poixson.vitalcore.VitalCorePlugin;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;


// /reply
public interface CMD_Reply extends PluginCommand {



	default ArgumentBuilder<CommandSourceStack, ?> register_Reply(final VitalCorePlugin plugin) {
		return Commands.literal(CMD_LABELS_REPLY.NODE)
			// /reply
			.executes(context -> this.onCommand_Reply(context, plugin));
	}



	default int onCommand_Reply(final CommandContext<CommandSourceStack> context, final VitalCorePlugin plugin) {
//TODO
context.getSource().getSender().sendPlainMessage("Command is unfinished!");
return SUCCESS;
	}



}
