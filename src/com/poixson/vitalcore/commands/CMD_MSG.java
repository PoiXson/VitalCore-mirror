package com.poixson.vitalcore.commands;

import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_MSG;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.poixson.tools.commands.PluginCommand;
import com.poixson.vitalcore.VitalCorePlugin;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;


// /msg
public interface CMD_MSG extends PluginCommand {



	default ArgumentBuilder<CommandSourceStack, ?> register_MSG(final VitalCorePlugin plugin) {
		return Commands.literal(CMD_LABELS_MSG.NODE)
			// /msg
			.executes(context -> this.onCommand_MSG(context, plugin));
	}



	default int onCommand_MSG(final CommandContext<CommandSourceStack> context, final VitalCorePlugin plugin) {
//TODO
context.getSource().getSender().sendPlainMessage("MSG!!!!!!!!!!!!!!!!!!!!!");
		return SUCCESS;
	}



}
