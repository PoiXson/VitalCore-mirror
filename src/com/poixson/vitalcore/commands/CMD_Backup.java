package com.poixson.vitalcore.commands;

import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_BACKUP;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.poixson.tools.commands.PluginCommand;
import com.poixson.vitalcore.VitalCorePlugin;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;


// /backup
public interface CMD_Backup extends PluginCommand {



	default ArgumentBuilder<CommandSourceStack, ?> register_Backup(final VitalCorePlugin plugin) {
		return Commands.literal(CMD_LABELS_BACKUP.NODE)
			// /backup
			.executes(context -> this.onCommand_Backup(context, plugin));
//"Perform a backup of the worlds and configs."
//"pxn.cmd.backup"
	}



	default int onCommand_Backup(final CommandContext<CommandSourceStack> context, final VitalCorePlugin plugin) {
//TODO
context.getSource().getSender().sendPlainMessage("BACKUP!!!!!!!!!!!!!!!!!!!!!");
		return SUCCESS;
	}



}
