package com.poixson.vitalcore.commands;

import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_TP_OFFLINE;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.poixson.tools.commands.PluginCommand;
import com.poixson.vitalcore.VitalCorePlugin;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;


// /tp-offline
public interface CMD_TP_Offline extends PluginCommand {



	default ArgumentBuilder<CommandSourceStack, ?> register_TP_Offline(final VitalCorePlugin plugin) {
		return Commands.literal(CMD_LABELS_TP_OFFLINE.NODE)
			// /tp-offline
			.executes(context -> this.onCommand_TP_Offline(context, plugin));
	}



	default int onCommand_TP_Offline(final CommandContext<CommandSourceStack> context, final VitalCorePlugin plugin) {
//TODO
context.getSource().getSender().sendPlainMessage("Command is unfinished!");
return SUCCESS;
	}



}
