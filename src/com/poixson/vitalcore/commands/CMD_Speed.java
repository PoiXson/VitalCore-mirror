package com.poixson.vitalcore.commands;

import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_SPEED;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.poixson.tools.commands.PluginCommand;
import com.poixson.vitalcore.VitalCorePlugin;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;


// /speed
public interface CMD_Speed extends PluginCommand {



	default ArgumentBuilder<CommandSourceStack, ?> register_Speed(final VitalCorePlugin plugin) {
		return Commands.literal(CMD_LABELS_SPEED.NODE)
			// /speed
			.executes(context -> this.onCommand_Speed(context, plugin));
	}



	default int onCommand_Speed(final CommandContext<CommandSourceStack> context, final VitalCorePlugin plugin) {
//TODO
context.getSource().getSender().sendPlainMessage("Command is unfinished!");
return SUCCESS;
	}



}
