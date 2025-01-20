package com.poixson.vitalcore.commands;

import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_HOME;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.poixson.tools.commands.PluginCommand;
import com.poixson.vitalcore.VitalCorePlugin;
import com.poixson.vitalcore.commands.types.ArgumentType_HomeName;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;


// /home
public interface CMD_Home extends PluginCommand {



	default ArgumentBuilder<CommandSourceStack, ?> register_Home(final VitalCorePlugin plugin) {
		return Commands.literal(CMD_LABELS_HOME.NODE)
			// /home
			.executes(context -> this.onCommand_Home(context, plugin))
			// /home <name>
			.then(Commands.argument("home", ArgumentType_HomeName.Create(plugin))
				.executes(context -> this.onCommand_Home(context, plugin))
			);
	}



	default int onCommand_Home(final CommandContext<CommandSourceStack> context, final VitalCorePlugin plugin) {
//TODO
context.getSource().getSender().sendPlainMessage("HOME!!!!!!!!!!!!!!!!!!!!!");
		return SUCCESS;
	}



}
