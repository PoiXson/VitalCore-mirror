package com.poixson.vitalcore.commands;

import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_HOME_DEL;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.poixson.tools.commands.PluginCommand;
import com.poixson.vitalcore.VitalCorePlugin;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;


// /delhome
public interface CMD_Home_Del extends PluginCommand {



	default ArgumentBuilder<CommandSourceStack, ?> register_Home_Del(final VitalCorePlugin plugin) {
		return Commands.literal(CMD_LABELS_HOME_DEL.NODE)
			// /delhome
			.executes(context -> this.onCommand_Home_Del(context, plugin));
	}



	default int onCommand_Home_Del(final CommandContext<CommandSourceStack> context, final VitalCorePlugin plugin) {
//TODO
context.getSource().getSender().sendPlainMessage("DEL-HOME!!!!!!!!!!!!!!!!!!!!!");
		return SUCCESS;
	}



}
