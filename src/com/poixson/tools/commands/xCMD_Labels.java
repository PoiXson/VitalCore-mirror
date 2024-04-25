package com.poixson.tools.commands;

import org.bukkit.command.CommandSender;

import com.poixson.utils.StringUtils;


public abstract class xCMD_Labels implements xCMD {

	protected final String[] labels;



	public xCMD_Labels(final String...labels) {
		this.labels = labels;
	}



	@Override
	public boolean match(final String label) {
		for (final String lbl : this.labels) {
			if (StringUtils.MatchString(lbl, label))
				return true;
		}
		return false;
	}

	@Override
	public String[] getLabels() {
		return this.labels;
	}

	@Override
	public boolean hasOverrides() {
		return false;
	}



	@Override
	public abstract boolean run(final CommandSender sender, final String[] args);



}
