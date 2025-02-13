package com.poixson.vitalcore.commands;

import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_WORLD;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.poixson.tools.commands.PluginCommand;
import com.poixson.vitalcore.VitalCorePlugin;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;


// /world
public interface CMD_World extends PluginCommand {



	default ArgumentBuilder<CommandSourceStack, ?> register_World(final VitalCorePlugin plugin) {
		return Commands.literal(CMD_LABELS_WORLD.NODE)
			// /world
			.executes(context -> this.onCommand_World(context, plugin));
	}



	default int onCommand_World(final CommandContext<CommandSourceStack> context, final VitalCorePlugin plugin) {
//TODO
context.getSource().getSender().sendPlainMessage("Command is unfinished!");
return SUCCESS;
	}



}
/*
	@Override
	public boolean onCommand(final CommandSender sender, final String[] args) {
		final Player player = (sender instanceof Player ? (Player)sender : null);
		final int num_args = args.length;
		// current world
		if (num_args == 0) {
			if (player == null) {
				sender.sendMessage("Cannot find world for console");
				return true;
			}
			final World world = player.getWorld();
			final String world_name = world.getName();
			player.sendMessage(Component.empty()
				.append(Component.text("World: " ).color(NamedTextColor.AQUA))
				.append(Component.text(world_name).color(NamedTextColor.GOLD))
			);
			return true;
		}
		final String world_name = args[0];
		final World world = Bukkit.getWorld(world_name);
		if (world == null) {
			sender.sendMessage(CHAT_PREFIX.append(Component.text(
				"Unknown world: "+world_name).color(NamedTextColor.RED)));
			return true;
		}
		final Location loc = world.getSpawnLocation();
		// self
		if (num_args == 1) {
			if (player == null) {
				sender.sendMessage("Cannot teleport console");
				return true;
			}
			if (!player.hasPermission("pxn.cmd.world"))
				return false;
			player.teleport(loc);
			player.sendMessage(Component.empty()
				.append(Component.text("Teleported to world: ").color(NamedTextColor.AQUA))
				.append(Component.text(world_name             ).color(NamedTextColor.GOLD))
			);
			return true;
		// other players
		} else {
			if (!sender.hasPermission("pxn.cmd.world.other"))
				return false;
			int count = 0;
			LOOP_ARGS:
			for (final String arg : args) {
				final Player p = Bukkit.getPlayer(arg);
				if (p == null) {
					sender.sendMessage(CHAT_PREFIX.append(Component.text(
						"Player not found: "+arg).color(NamedTextColor.RED)));
					continue LOOP_ARGS;
				}
				p.teleport(loc);
				p.sendMessage(Component.empty()
					.append(Component.text("Teleported to world: ").color(NamedTextColor.AQUA))
					.append(Component.text(world_name             ).color(NamedTextColor.GOLD))
				);
				count++;
			}
			if (count > 0) {
				sender.sendMessage(CHAT_PREFIX.append(Component.text(String.format(
					"Teleported %d player%s to world: %s",
					Integer.valueOf(count),
					(count == 1 ? "" : "s"),
					world_name
				)).color(NamedTextColor.AQUA)));
				return true;
			}
		}
		return false;
	}
*/
