package com.poixson.vitalcore.commands;

import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_DOWN;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.poixson.tools.commands.PluginCommand;
import com.poixson.vitalcore.VitalCorePlugin;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;


// /down
public interface CMD_Down extends PluginCommand {




	default ArgumentBuilder<CommandSourceStack, ?> register_Down(final VitalCorePlugin plugin) {
		return Commands.literal(CMD_LABELS_DOWN.NODE)
			// /down
			.executes(context -> this.onCommand_Down(context, plugin));
	}



	default int onCommand_Down(final CommandContext<CommandSourceStack> context, final VitalCorePlugin plugin) {
//TODO
context.getSource().getSender().sendPlainMessage("DOWN!!!!!!!!!!!!!!!!!!!!!");
		return SUCCESS;
	}



}
