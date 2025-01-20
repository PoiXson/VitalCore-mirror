package com.poixson.vitalcore.commands;

import static com.poixson.tools.commands.PluginCommand.ConsoleCannotUse;
import static com.poixson.tools.commands.PluginCommand.HasPermissionUseCMD;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_SPAWN_SET;
import static com.poixson.vitalcore.VitalCoreDefines.PERM_CMD_SPAWN_SET;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.poixson.tools.commands.PluginCommand;
import com.poixson.vitalcore.VitalCorePlugin;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;


// /setspawn
public interface CMD_Spawn_Set extends PluginCommand {



	default ArgumentBuilder<CommandSourceStack, ?> register_Spawn_Set(final VitalCorePlugin plugin) {
		return Commands.literal(CMD_LABELS_SPAWN_SET.NODE)
			// /setspawn
			.executes(context -> this.onCommand_Spawn_Set(context, plugin));
	}



	default int onCommand_Spawn_Set(final CommandContext<CommandSourceStack> context, final VitalCorePlugin plugin) {
		final CommandSourceStack source = context.getSource();
		final CommandSender sender = source.getSender();
		// no console
		if (ConsoleCannotUse(sender))
			return FAILURE;
		// permission
		if (!HasPermissionUseCMD(sender, PERM_CMD_SPAWN_SET.NODE))
			return FAILURE;
		final Player self = (Player) sender;
		final World world = self.getWorld();
		final Location loc = world.getSpawnLocation();
		world.setSpawnLocation(loc);
		sender.sendMessage(Component.text("World spawn location set").color(NamedTextColor.GOLD));
		return SUCCESS;
	}



}
