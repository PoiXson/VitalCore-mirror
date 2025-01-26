package com.poixson.vitalcore.commands;

import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_WORLDS;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.poixson.tools.commands.PluginCommand;
import com.poixson.vitalcore.VitalCorePlugin;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;


// /worlds
public interface CMD_Worlds extends PluginCommand {



	default ArgumentBuilder<CommandSourceStack, ?> register_Worlds(final VitalCorePlugin plugin) {
		return Commands.literal(CMD_LABELS_WORLDS.NODE)
			// /worlds
			.executes(context -> this.onCommand_Worlds(context, plugin));
	}



	default int onCommand_Worlds(final CommandContext<CommandSourceStack> context, final VitalCorePlugin plugin) {
//TODO
context.getSource().getSender().sendPlainMessage("Command is unfinished!");
return SUCCESS;
	}



}
