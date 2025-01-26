package com.poixson.vitalcore.commands;

import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_BOTTOM;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.poixson.tools.commands.PluginCommand;
import com.poixson.vitalcore.VitalCorePlugin;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;


// /bottom
public interface CMD_Bottom extends PluginCommand {



	default ArgumentBuilder<CommandSourceStack, ?> register_Bottom(final VitalCorePlugin plugin) {
		return Commands.literal(CMD_LABELS_BOTTOM.NODE)
			// /bottom
			.executes(context -> this.onCommand_Bottom(context, plugin));
	}



	default int onCommand_Bottom(final CommandContext<CommandSourceStack> context, final VitalCorePlugin plugin) {
//TODO
context.getSource().getSender().sendPlainMessage("Command is unfinished!");
return SUCCESS;
	}



}
