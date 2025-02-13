package com.poixson.vitalcore.commands;

import static com.poixson.tools.commands.PluginCommand.ConsoleCannotUse;
import static com.poixson.tools.commands.PluginCommand.GetArg_Players;
import static com.poixson.tools.commands.PluginCommand.HasPermissionUseCMD;
import static com.poixson.tools.commands.PluginCommand.HasPermissionUseOthersCMD;
import static com.poixson.utils.Utils.IsEmpty;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_SPAWN;
import static com.poixson.vitalcore.VitalCoreDefines.PERM_CMD_SPAWN;
import static com.poixson.vitalcore.VitalCoreDefines.PERM_CMD_SPAWN_OTHERS;
import static com.poixson.vitalcore.VitalCorePlugin.CHAT_PREFIX;

import org.bukkit.Bukkit;
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
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;


// /spawn
public interface CMD_Spawn extends PluginCommand {



	default ArgumentBuilder<CommandSourceStack, ?> register_Spawn(final VitalCorePlugin plugin) {
		return Commands.literal(CMD_LABELS_SPAWN.NODE)
			// /spawn
			.executes(context -> this.onCommand_Spawn(context, plugin))
			// /spawn <world> <players>
			.then(Commands.argument("world", ArgumentTypes.world())
				.then(Commands.argument("players", ArgumentTypes.players())
					.executes(context -> this.onCommand_Spawn(context, plugin))
				)
			);
	}



	default int onCommand_Spawn(final CommandContext<CommandSourceStack> context, final VitalCorePlugin plugin) {
		final CommandSourceStack source = context.getSource();
		final CommandSender sender = source.getSender();
		final Player[] others = GetArg_Players(context);
		final String world_str = context.getArgument("world", String.class);
		final World world = (IsEmpty(world_str) ? null : Bukkit.getWorld(world_str));
		// self
		if (IsEmpty(others)) {
			// no console
			if (ConsoleCannotUse(sender))
				return FAILURE;
			// permission self
			if (!HasPermissionUseCMD(sender, PERM_CMD_SPAWN.NODE))
				return FAILURE;
			final Player self = (Player) sender;
			final World w = (world==null ? self.getWorld() : world);
			final Location loc = w.getSpawnLocation();
			self.teleport(loc);
			sender.sendMessage(Component.text("*poof* spawn").color(NamedTextColor.DARK_PURPLE));
		// other players
		} else {
			// permission others
			if (!HasPermissionUseOthersCMD(sender, PERM_CMD_SPAWN_OTHERS.NODE))
				return FAILURE;
			if (world == null) {
				sender.sendMessage(Component.text("Invalid world: "+world_str));
				return FAILURE;
			}
			final Location loc = world.getSpawnLocation();
			int count = 0;
			for (final Player player : others) {
				player.teleport(loc);
				sender.sendMessage(Component.text("*poof* spawn").color(NamedTextColor.DARK_PURPLE));
				count++;
			}
			if (count > 0) {
				sender.sendMessage(CHAT_PREFIX.append(Component.text(String.format(
					"Teleported %d player%s to spawn",
					Integer.valueOf(count),
					(count == 1 ? "" : "s")
				)).color(NamedTextColor.AQUA)));
			}
		}
		return SUCCESS;
	}



}
