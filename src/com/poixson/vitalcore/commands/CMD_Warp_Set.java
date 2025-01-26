package com.poixson.vitalcore.commands;

import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_WARP_SET;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.poixson.tools.commands.PluginCommand;
import com.poixson.vitalcore.VitalCorePlugin;
import com.poixson.vitalcore.commands.types.ArgumentType_HomeName;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;


// /setwarp
public interface CMD_Warp_Set extends PluginCommand {



	default ArgumentBuilder<CommandSourceStack, ?> register_Warp_Set(final VitalCorePlugin plugin) {
		return Commands.literal(CMD_LABELS_WARP_SET.NODE)
			// /setwarp <name>
			.then(Commands.argument("warp", new ArgumentType_HomeName(plugin))
				.executes(context -> this.onCommand_Warp_Set(context, plugin))
			);
	}



	default int onCommand_Warp_Set(final CommandContext<CommandSourceStack> context, final VitalCorePlugin plugin) {
//TODO
context.getSource().getSender().sendPlainMessage("Command is unfinished!");
return SUCCESS;
	}



}
