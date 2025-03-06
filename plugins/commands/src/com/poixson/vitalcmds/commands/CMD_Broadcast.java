package com.poixson.vitalcore.commands;

import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_BROADCAST;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.poixson.tools.commands.PluginCommand;
import com.poixson.vitalcore.VitalCorePlugin;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;


// /broadcast
public interface CMD_Broadcast extends PluginCommand {



	default ArgumentBuilder<CommandSourceStack, ?> register_Broadcast(final VitalCorePlugin plugin) {
		return Commands.literal(CMD_LABELS_BROADCAST.NODE)
			// /broadcast
			.executes(context -> this.onCommand_Broadcast(context, plugin));
	}



	default int onCommand_Broadcast(final CommandContext<CommandSourceStack> context, final VitalCorePlugin plugin) {
//TODO
context.getSource().getSender().sendPlainMessage("Command is unfinished!");
return SUCCESS;
	}



}
