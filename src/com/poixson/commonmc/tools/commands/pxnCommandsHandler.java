package com.poixson.commonmc.tools.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicReference;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

import com.poixson.commonmc.tools.plugin.xJavaPlugin;
import com.poixson.commonmc.tools.plugin.xListener;
import com.poixson.utils.Utils;


public abstract class pxnCommandsHandler
extends xListener<xJavaPlugin>
implements CommandExecutor, TabCompleter {

	protected final String[] labels;

	protected final CopyOnWriteArraySet<pxnCommand>   cmds = new CopyOnWriteArraySet<pxnCommand>();
	protected final CopyOnWriteArraySet<PluginCommand> pcs = new CopyOnWriteArraySet<PluginCommand>();
	protected final AtomicReference<TabCompleter>  tabComp = new AtomicReference<TabCompleter>(null);



	public pxnCommandsHandler(final xJavaPlugin plugin, final String...labels) {
		super(plugin);
		this.labels = labels;
	}



	@Override
	public void register() {
		if (this.hasOverrides())
			super.register();
		for (final String label : this.labels) {
			final PluginCommand pc = this.plugin.getCommand(label);
			if (pc == null) continue;
			pc.setExecutor(this);
			this.pcs.add(pc);
		}
	}
	@Override
	public void unregister() {
		super.unregister();
		for (final PluginCommand pc : this.pcs)
			pc.setExecutor(null);
		this.pcs.clear();
		this.cmds.clear();
		this.tabComp.set(null);
	}



	// -------------------------------------------------------------------------------
	// commands/labels



	public void addCommand(final pxnCommand cmd) {
		this.cmds.add(cmd);
	}
	public boolean match(final String match) {
		if (Utils.isEmpty(match))
			return false;
		final String matchLower = match.toLowerCase();
		for (final String label : this.labels) {
			if (matchLower.equals(label))
				return true;
		}
		return false;
	}

	public pxnCommand getDefaultCommand() {
		for (final pxnCommand cmd : this.cmds) {
			if (cmd.isDefault())
				return cmd;
		}
		return null;
	}

	public boolean hasOverrides() {
		for (final pxnCommand cmd : this.cmds) {
			if (cmd.override)
				return true;
		}
		return false;
	}



	// -------------------------------------------------------------------------------
	// listeners



	// standard command listener
	@Override
	public boolean onCommand(final CommandSender sender,
			final Command command, final String label, final String[] args) {
		return this.handleCommand(sender, label, args, false);
	}
	// player command event
	@EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
	public void onPlayerCommand(final PlayerCommandPreprocessEvent event) {
		final String msg = event.getMessage();
		if (msg.startsWith("/")) {
			final String label;
			final String[] args;
			final int pos = msg.indexOf(' ');
			// no arguments
			if (pos == -1) {
				label = msg.substring(1);
				args = new String[0];
			// command with arguments
			} else {
				label = msg.substring(1, pos);
				args = msg.substring(pos+1).split(" ");
			}
			if (this.handleCommand(event.getPlayer(), label, args, true))
				event.setCancelled(true);
		}
	}
	// console command event
	@EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
	public void onSendCommandEvent(final ServerCommandEvent event) {
		final String msg = event.getCommand();
		final String label;
		final String[] args;
		final int pos = msg.indexOf(' ');
		// no arguments
		if (pos == -1) {
			label = msg;
			args  = new String[0];
		// command with arguments
		} else {
			label = msg.substring(0, pos);
			args  = msg.substring(pos+1).split(" ");
		}
		if (this.handleCommand(event.getSender(), label, args, true))
			event.setCancelled(true);
	}

	protected boolean handleCommand(final CommandSender sender,
			final String label, final String[] args, final boolean isOverride) {
		if (this.match(label)) {
			for (final pxnCommand cmd : this.cmds) {
				if (cmd.match(args)) {
					cmd.run(sender, label, args);
					return true;
				}
			}
		}
		return false;
	}



	// -------------------------------------------------------------------------------
	// tab completion



	public void setTabCompleter(final TabCompleter tabComp) {
		this.tabComp.set(tabComp);
		for (final PluginCommand pc : this.pcs)
			pc.setTabCompleter(tabComp);
	}

	@Override
	public List<String> onTabComplete(
			final CommandSender sender, final Command cmd,
			final String label, final String[] args) {
		final List<String> matches = new ArrayList<String>();
		final int size = args.length;
		switch (size) {
		case 1:
			for (final pxnCommand c : this.cmds) {
				for (final String match : c.getMatches(args[0])) {
					matches.add(match);
				}
			}
			break;
		default: break;
		}
		return matches;
	}



}
