package com.poixson.vitalcore.commands;

import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_ME;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.poixson.tools.commands.PluginCommand;
import com.poixson.vitalcore.VitalCorePlugin;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;


// /me
public interface CMD_Me extends PluginCommand {



	default ArgumentBuilder<CommandSourceStack, ?> register_Me(final VitalCorePlugin plugin) {
		return Commands.literal(CMD_LABELS_ME.NODE)
			// /me
			.executes(context -> this.onCommand_Me(context, plugin));
	}



	default int onCommand_Me(final CommandContext<CommandSourceStack> context, final VitalCorePlugin plugin) {
//TODO
context.getSource().getSender().sendPlainMessage("ME!!!!!!!!!!!!!!!!!!!!!");
		return SUCCESS;
	}



}
