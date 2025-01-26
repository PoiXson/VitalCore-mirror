package com.poixson.vitalcore.commands;

import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_UPDATES;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.poixson.tools.commands.PluginCommand;
import com.poixson.vitalcore.VitalCorePlugin;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;


// /updates
public interface CMD_Updates extends PluginCommand {



	default ArgumentBuilder<CommandSourceStack, ?> register_Updates(final VitalCorePlugin plugin) {
		return Commands.literal(CMD_LABELS_UPDATES.NODE)
			// /updates
			.executes(context -> this.onCommand_Updates(context, plugin));
	}



	default int onCommand_Updates(final CommandContext<CommandSourceStack> context, final VitalCorePlugin plugin) {
//TODO
context.getSource().getSender().sendPlainMessage("Command is unfinished!");
return SUCCESS;
	}



}
