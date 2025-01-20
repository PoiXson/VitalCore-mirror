package com.poixson.vitalcore.commands;

import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_SPEED_WALK;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.poixson.tools.commands.PluginCommand;
import com.poixson.vitalcore.VitalCorePlugin;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;


// /walkspeed
public interface CMD_Speed_Walk extends PluginCommand {



	default ArgumentBuilder<CommandSourceStack, ?> register_Speed_Walk(final VitalCorePlugin plugin) {
		return Commands.literal(CMD_LABELS_SPEED_WALK.NODE)
			// /walkspeed
			.executes(context -> this.onCommand_SpeedWalk(context, plugin));
	}



	default int onCommand_SpeedWalk(final CommandContext<CommandSourceStack> context, final VitalCorePlugin plugin) {
//TODO
context.getSource().getSender().sendPlainMessage("WALK-SPEED!!!!!!!!!!!!!!!!!!!!!");
		return SUCCESS;
	}



}
