package com.poixson.tools.commands;

import java.util.LinkedList;

import org.bukkit.command.CommandSender;


public class xCMD_Children extends xCMD_Labels {

	protected final LinkedList<xCMD> commands = new LinkedList<xCMD>();



	public xCMD_Children(final String...labels) {
		super(labels);
	}



	@Override
	public boolean run(final CommandSender sender, final String[] args) {
		final String first = args[0];
		for (final xCMD cmd : this.commands) {
			if (cmd.match(first)) {
				final LinkedList<String> labels = new LinkedList<String>();
				for (final String arg : args)
					labels.add(arg);
				labels.removeFirst();
				return cmd.run(sender, labels.toArray(new String[0]));
			}
		}
		return false;
	}



	public void addCommand(final xCMD cmd) {
		this.commands.add(cmd);
	}



}
