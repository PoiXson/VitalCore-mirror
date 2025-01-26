package com.poixson.vitalcore.commands;

import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_HOMES;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.poixson.tools.commands.PluginCommand;
import com.poixson.vitalcore.VitalCorePlugin;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;


// /homes
public interface CMD_Homes extends PluginCommand {



	default ArgumentBuilder<CommandSourceStack, ?> register_Homes(final VitalCorePlugin plugin) {
		return Commands.literal(CMD_LABELS_HOMES.NODE)
			// /homes
			.executes(context -> this.onCommand_Homes(context, plugin));
	}



	default int onCommand_Homes(final CommandContext<CommandSourceStack> context, final VitalCorePlugin plugin) {
//TODO
context.getSource().getSender().sendPlainMessage("Command is unfinished!");
return SUCCESS;
	}



}
