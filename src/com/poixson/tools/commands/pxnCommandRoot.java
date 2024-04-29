package com.poixson.tools.commands;

import static com.poixson.utils.BukkitUtils.GetCommand;

import java.io.Closeable;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;

import com.poixson.tools.xJavaPlugin;


public class pxnCommandRoot extends pxnCommand
implements CommandExecutor, TabCompleter, Closeable {

	public final PluginCommand plugin_command;



	public pxnCommandRoot(final xJavaPlugin plugin, final String namespace,
			final String desc, final String usage, final String perm,
			final String[] labels) {
		super();
		this.plugin_command = GetCommand(plugin, namespace, labels, desc, usage, perm);
		this.plugin_command.setExecutor(this);
	}



	@Override
	public void close() {
		this.plugin_command.setExecutor(null);
	}



	@Override
	public boolean onCommand(final CommandSender sender,
			final Command command, final String label, final String[] args) {
		return this.onCommand(sender, args);
	}
	@Override
	public List<String> onTabComplete(final CommandSender sender,
			final Command command, final String label, final String[] args) {
		return this.onTabComplete(sender, args);
	}



}
