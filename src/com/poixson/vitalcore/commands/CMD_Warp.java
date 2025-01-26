package com.poixson.vitalcore.commands;

import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_WARP;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.poixson.tools.commands.PluginCommand;
import com.poixson.vitalcore.VitalCorePlugin;
import com.poixson.vitalcore.commands.types.ArgumentType_WarpName;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;


// /warp
public interface CMD_Warp extends PluginCommand {



	default ArgumentBuilder<CommandSourceStack, ?> register_Warp(final VitalCorePlugin plugin) {
		return Commands.literal(CMD_LABELS_WARP.NODE)
			// /warp
			.executes(context -> this.onCommand_Warp(context, plugin))
			// /warp <name>
			.then(Commands.argument("warp", new ArgumentType_WarpName(plugin))
				.executes(context -> this.onCommand_Warp(context, plugin))
			);
	}



	default int onCommand_Warp(final CommandContext<CommandSourceStack> context, final VitalCorePlugin plugin) {
//TODO
context.getSource().getSender().sendPlainMessage("Command is unfinished!");
return SUCCESS;
	}



}
