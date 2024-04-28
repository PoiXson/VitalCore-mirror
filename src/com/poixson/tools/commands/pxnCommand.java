package com.poixson.tools.commands;

import static com.poixson.utils.Utils.IsEmpty;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.command.CommandSender;


public class pxnCommand {

	protected final String[] labels;

	protected final LinkedList<pxnCommand> children = new LinkedList<pxnCommand>();



	public pxnCommand(final String...labels) {
		this.labels = labels;
	}



	// override for end point
	public boolean onCommand(final CommandSender sender, final String[] args) {
		final LinkedList<String> list = new LinkedList<String>();
		for (final String label : args)
			list.addLast(label);
		final String first = list.removeFirst();
		for (final pxnCommand cmd : this.children) {
			if (cmd.match(first) != null)
				return cmd.onCommand(sender, list.toArray(new String[0]));
		}
		return false;
	}



	public List<String> onTabComplete(final CommandSender sender, final String[] args) {
		if (IsEmpty(args)) return null;
		final LinkedList<String> found = new LinkedList<String>();
		final LinkedList<String> list = new LinkedList<String>();
		for (final String arg : args)
			list.addLast(arg);
		final String first = list.removeFirst();

		if (list.isEmpty()) {
			for (final pxnCommand command : this.children) {
				final String match = command.matchPart(first);
				if (match != null)
					found.addLast(match);
			}
		} else {
			for (final pxnCommand command : this.children) {
				if (command.match(first) != null) {
					final List<String> result = command.onTabComplete(sender, list.toArray(new String[0]));
					if (result != null) {
						for (final String str : result)
							found.addLast(str);
					}
				}
			}
		}
		return found;
	}



	public void addCommand(final pxnCommand command) {
		this.children.addLast(command);
	}



	public String match(final String match) {
		for (final String label : this.labels) {
			if (label.equals(match))
				return label;
		}
		return null;
	}
	public String matchPart(final String match) {
		for (final String label : this.labels) {
			if (label.startsWith(match))
				return label;
		}
		return null;
	}



}
