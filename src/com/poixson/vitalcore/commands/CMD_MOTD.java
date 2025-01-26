package com.poixson.vitalcore.commands;

import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_MOTD;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.poixson.tools.commands.PluginCommand;
import com.poixson.vitalcore.VitalCorePlugin;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;


// /motd
public interface CMD_MOTD extends PluginCommand {



	default ArgumentBuilder<CommandSourceStack, ?> register_MOTD(final VitalCorePlugin plugin) {
		return Commands.literal(CMD_LABELS_MOTD.NODE)
			// /motd
			.executes(context -> this.onCommand_MOTD(context, plugin));
	}



	default int onCommand_MOTD(final CommandContext<CommandSourceStack> context, final VitalCorePlugin plugin) {
//TODO
context.getSource().getSender().sendPlainMessage("Command is unfinished!");
return SUCCESS;
	}



}
