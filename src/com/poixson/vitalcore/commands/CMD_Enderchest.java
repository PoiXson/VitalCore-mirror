package com.poixson.vitalcore.commands;

import static com.poixson.tools.commands.PluginCommand.ConsoleCannotUse;
import static com.poixson.tools.commands.PluginCommand.HasPermissionUseCMD;
import static com.poixson.utils.BukkitUtils.OpenEnderchest;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_ENDERCHEST;
import static com.poixson.vitalcore.VitalCoreDefines.PERM_CMD_ENDERCHEST;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.poixson.tools.commands.PluginCommand;
import com.poixson.vitalcore.VitalCorePlugin;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;


// /enderchest
public interface CMD_Enderchest extends PluginCommand {



	default ArgumentBuilder<CommandSourceStack, ?> register_Enderchest(final VitalCorePlugin plugin) {
		return Commands.literal(CMD_LABELS_ENDERCHEST.NODE)
			// /enderchest
			.executes(context -> this.onCommand_Enderchest(context, plugin));
	}



	default int onCommand_Enderchest(final CommandContext<CommandSourceStack> context, final VitalCorePlugin plugin) {
		final CommandSourceStack source = context.getSource();
		final CommandSender sender = source.getSender();
		// no console
		if (ConsoleCannotUse(sender))
			return FAILURE;
		// permission self
		if (!HasPermissionUseCMD(sender, PERM_CMD_ENDERCHEST.NODE))
			return FAILURE;
		final Player self = (Player) sender;
		OpenEnderchest(self);
		return SUCCESS;
	}



}
