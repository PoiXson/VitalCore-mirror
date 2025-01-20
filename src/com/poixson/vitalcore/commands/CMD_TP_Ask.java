package com.poixson.vitalcore.commands;

import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_TP_ASK;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.poixson.tools.commands.PluginCommand;
import com.poixson.vitalcore.VitalCorePlugin;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;


// /tp-ask
public interface CMD_TP_Ask extends PluginCommand {



	default ArgumentBuilder<CommandSourceStack, ?> register_TP_Ask(final VitalCorePlugin plugin) {
		return Commands.literal(CMD_LABELS_TP_ASK.NODE)
			// /tp-ask
			.executes(context -> this.onCommand_TP_Ask(context, plugin));
	}



	default int onCommand_TP_Ask(final CommandContext<CommandSourceStack> context, final VitalCorePlugin plugin) {
//TODO
context.getSource().getSender().sendPlainMessage("TP-ASK!!!!!!!!!!!!!!!!!!!!!");
		return SUCCESS;
	}



}
