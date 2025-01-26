package com.poixson.vitalcore.commands;

import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_WARPS;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.poixson.tools.commands.PluginCommand;
import com.poixson.vitalcore.VitalCorePlugin;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;


// /warps
public interface CMD_Warps extends PluginCommand {



	default ArgumentBuilder<CommandSourceStack, ?> register_Warps(final VitalCorePlugin plugin) {
		return Commands.literal(CMD_LABELS_WARPS.NODE)
			// /warps
			.executes(context -> this.onCommand_Warps(context, plugin));
	}



	default int onCommand_Warps(final CommandContext<CommandSourceStack> context, final VitalCorePlugin plugin) {
//TODO
context.getSource().getSender().sendPlainMessage("Command is unfinished!");
return SUCCESS;
	}



}
