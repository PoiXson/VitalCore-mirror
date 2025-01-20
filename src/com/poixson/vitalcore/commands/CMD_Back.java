package com.poixson.vitalcore.commands;

import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_BACK;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.poixson.tools.commands.PluginCommand;
import com.poixson.vitalcore.VitalCorePlugin;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;


// /back
public interface CMD_Back extends PluginCommand {



	default ArgumentBuilder<CommandSourceStack, ?> register_Back(final VitalCorePlugin plugin) {
		return Commands.literal(CMD_LABELS_BACK.NODE)
			// /back
			.executes(context -> this.onCommand_Back(context, plugin));
	}



	default int onCommand_Back(final CommandContext<CommandSourceStack> context, final VitalCorePlugin plugin) {
//TODO
context.getSource().getSender().sendPlainMessage("BACK!!!!!!!!!!!!!!!!!!!!!");
		return SUCCESS;
	}



}
