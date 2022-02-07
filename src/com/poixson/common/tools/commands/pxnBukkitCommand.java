package com.poixson.common.tools.commands;

import java.util.HashSet;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.poixson.utils.Utils;


public abstract class pxnBukkitCommand {

	public final String[] labels;



	public pxnBukkitCommand(final String...labels) {
		final HashSet<String> set = new HashSet<String>();
		for (final String label : labels)
			set.add(label.toLowerCase());
		this.labels = set.toArray(new String[0]);
	}



	public abstract boolean run(final CommandSender sender,
			final Command cmd, final String[] args);



	public boolean matchArgs(final String[] args) {
		if (Utils.isEmpty(args))
			return this.isDefault();
		return this.matchLabel(args[0]);
	}
	public boolean matchLabel(final String match) {
		if (Utils.isEmpty(match))
			return this.isDefault();
		final String matchLower = match.toLowerCase();
		for (final String label : this.labels) {
			if (matchLower.equals(label))
				return true;
		}
		return false;
	}
	public boolean isDefault() {
		return Utils.isEmpty(labels);
	}



}
