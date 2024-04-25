package com.poixson.tools.commands;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import com.poixson.tools.xJavaPlugin;
import com.poixson.tools.events.xListener;


public class xCMD_Handler implements xCMD, xListener, CommandExecutor, TabCompleter {

	protected final xJavaPlugin plugin;

	protected final LinkedList<xCMD> commands = new LinkedList<xCMD>();

	protected final CopyOnWriteArraySet<PluginCommand> pcs = new CopyOnWriteArraySet<PluginCommand>();



	public xCMD_Handler(final xJavaPlugin plugin) {
		this.plugin = plugin;
	}



	public void register() {
		this.register(this.plugin);
	}
	@Override
	public void register(final JavaPlugin plugin) {
		if (this.hasOverrides())
			xListener.super.register(plugin);
		for (final String label : this.getLabels()) {
			final PluginCommand pc = plugin.getCommand(label);
			if (pc == null) {
				this.plugin.log().warning("Command not found: "+label);
				continue;
			}
			pc.setExecutor(this);
			this.pcs.add(pc);
		}
	}
	@Override
	public void unregister() {
		xListener.super.unregister();
		for (final PluginCommand pc : this.pcs)
			pc.setExecutor(null);
		this.pcs.clear();
	}



	@Override
	public boolean onCommand(final CommandSender sender, final Command command,
			final String label, final String[] args) {
		return this.run(sender, args);
	}

	@Override
	public boolean run(final CommandSender sender, final String[] args) {
		for (final xCMD cmd : this.commands) {
			if (cmd.run(sender, args))
				return true;
		}
		return false;
	}



	public void addCommand(final xCMD cmd) {
		this.commands.addLast(cmd);
	}
	@Override
	public boolean hasOverrides() {
		return false;
	}



	@Override
	public String[] getLabels() {
		final LinkedList<String> labels = new LinkedList<String>();
		for (final xCMD cmd : this.commands) {
			for (final String label : cmd.getLabels())
				labels.add(label);
		}
		return labels.toArray(new String[0]);
	}

	@Override
	public boolean match(final String label) {
		for (final xCMD cmd : this.commands) {
			if (cmd.match(label))
				return true;
		}
		return false;
	}



	@Override
	public List<String> onTabComplete(final CommandSender sender, final Command command,
			final String label, final String[] args) {
	}



}
