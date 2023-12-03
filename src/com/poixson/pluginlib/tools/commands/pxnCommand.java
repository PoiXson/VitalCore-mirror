package com.poixson.commonmc.tools.commands;

import java.util.LinkedList;

import org.bukkit.command.CommandSender;

import com.poixson.commonmc.tools.plugin.xJavaPlugin;
import com.poixson.utils.Utils;


public abstract class pxnCommand<T extends xJavaPlugin> {

	protected final T plugin;

	public final String[] labels;
	public final boolean override;



	public pxnCommand(final T plugin, final String...labels) {
		this(plugin, false, labels);
	}
	public pxnCommand(final T plugin, final boolean enableOverride, final String...labels) {
		this.plugin = plugin;
		this.override = enableOverride;
		this.labels   = labels;
	}



	public abstract boolean run(final CommandSender sender, final String label, final String[] args);



	public String[] getMatches(final String arg) {
		final LinkedList<String> list = new LinkedList<String>();
		for (final String lbl : this.labels) {
			if (lbl.startsWith(arg))
				list.add(lbl);
		}
		return list.toArray(new String[0]);
	}
	public boolean match(final String[] args) {
		if (Utils.isEmpty(args))
			return this.isDefault();
		return this.match(args[0]);
	}
	public boolean match(final String match) {
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
		return Utils.isEmpty(this.labels);
	}



}
