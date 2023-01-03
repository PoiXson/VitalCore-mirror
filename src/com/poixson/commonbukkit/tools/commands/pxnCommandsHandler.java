package com.poixson.commonbukkit.tools.commands;

import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicReference;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import com.poixson.utils.Utils;


public abstract class pxnBukkitCommandsHelper implements CommandExecutor {

	protected final JavaPlugin plugin;

	protected final String[] labels;

	protected final CopyOnWriteArraySet<pxnBukkitCommand> cmds = new CopyOnWriteArraySet<pxnBukkitCommand>();

	protected final CopyOnWriteArraySet<PluginCommand> pcs = new CopyOnWriteArraySet<PluginCommand>();
	protected final AtomicReference<TabCompleter> tabComp = new AtomicReference<TabCompleter>(null);



	public pxnBukkitCommandsHelper(final JavaPlugin plugin, final String...labels) {
		this.plugin = plugin;
		// labels
		final HashSet<String> set = new HashSet<String>();
		for (final String label : labels)
			set.add(label.toLowerCase());
		this.labels = set.toArray(new String[0]);
	}



	public void register() {
		for (final String label : this.labels) {
			final PluginCommand pc = this.plugin.getCommand(label);
			pc.setExecutor(this);
			this.pcs.add(pc);
		}
	}
	public void unregister() {
		final Iterator<PluginCommand> it = this.pcs.iterator();
		while (it.hasNext()) {
			final PluginCommand pc = it.next();
			pc.setExecutor(null);
		}
		this.pcs.clear();
	}



	public void setTabCompleter(final TabCompleter tabComp) {
		this.tabComp.set(tabComp);
		final Iterator<PluginCommand> it = this.pcs.iterator();
		while (it.hasNext()) {
			final PluginCommand pc = it.next();
			pc.setTabCompleter(tabComp);
		}
	}



	public void addCommand(final pxnBukkitCommand cmd) {
		if (cmd == null) throw new NullPointerException("cmd");
		this.cmds.add(cmd);
	}

	public pxnBukkitCommand getDefaultCommand() {
		final Iterator<pxnBukkitCommand> it = this.cmds.iterator();
		while (it.hasNext()) {
			final pxnBukkitCommand cmd = it.next();
			if (cmd.isDefault())
				return cmd;
		}
		return null;
	}



	@Override
	public boolean onCommand(final CommandSender sender, final Command command,
			final String label, final String[] args) {
		// no arguments
		if (Utils.isEmpty(args)) {
			final pxnBukkitCommand cmd = this.getDefaultCommand();
			if (cmd != null)
				return cmd.run(sender, command, args);
			return false;
		}
		// find command
		final Iterator<pxnBukkitCommand> it = this.cmds.iterator();
		while (it.hasNext()) {
			final pxnBukkitCommand cmd = it.next();
			if (cmd.matchArgs(args)) {
				final boolean result = cmd.run(sender, command, args);
				if (result)
					return true;
			}
		}
		return false;
	}



}
