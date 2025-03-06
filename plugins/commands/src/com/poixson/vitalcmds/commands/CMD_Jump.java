package com.poixson.vitalcore.commands;

import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_JUMP;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.poixson.tools.commands.PluginCommand;
import com.poixson.vitalcore.VitalCorePlugin;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;


// /jump
public interface CMD_Jump extends PluginCommand {



	default ArgumentBuilder<CommandSourceStack, ?> register_Jump(final VitalCorePlugin plugin) {
		return Commands.literal(CMD_LABELS_JUMP.NODE)
			// /jump
			.executes(context -> this.onCommand_Jump(context, plugin));
	}



	default int onCommand_Jump(final CommandContext<CommandSourceStack> context, final VitalCorePlugin plugin) {
//TODO
context.getSource().getSender().sendPlainMessage("Command is unfinished!");
return SUCCESS;
	}



}
