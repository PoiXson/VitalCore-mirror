package com.poixson.vitalcore.commands;

import static com.poixson.tools.commands.PluginCommand.HasPermissionUseCMD;
import static com.poixson.utils.BukkitUtils.GarbageCollect;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_GC;
import static com.poixson.vitalcore.VitalCoreDefines.PERM_CMD_GC;
import static com.poixson.vitalcore.VitalCorePlugin.CHAT_PREFIX;

import org.bukkit.command.CommandSender;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.poixson.tools.commands.PluginCommand;
import com.poixson.vitalcore.VitalCorePlugin;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;


// /gc
public interface CMD_GC extends PluginCommand {



	default ArgumentBuilder<CommandSourceStack, ?> register_GC(final VitalCorePlugin plugin) {
		// /gc
		return Commands.literal(CMD_LABELS_GC.NODE)
			.executes(context -> this.onCommand_GC(context, plugin));
	}



	default int onCommand_GC(final CommandContext<CommandSourceStack> context, final VitalCorePlugin plugin) {
		final CommandSourceStack source = context.getSource();
		final CommandSender sender = source.getSender();
		// permission
		if (!HasPermissionUseCMD(sender, PERM_CMD_GC.NODE))
			return FAILURE;
		final int freed_mb = GarbageCollect();
		Component msg = CHAT_PREFIX.append(Component.text("Garbage collected").color(NamedTextColor.AQUA));
		if (freed_mb >= 10) {
			msg = msg
				.append(Component.text("; freed: "))
				.append(Component.text(Integer.toString(freed_mb)+"MB").color(NamedTextColor.GREEN));
		}
		sender.sendMessage(msg);
		return SUCCESS;
	}



}
