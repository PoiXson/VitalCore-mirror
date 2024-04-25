package com.poixson.tools.commands;

import org.bukkit.command.CommandSender;


public interface xCMD {


	public boolean match(final String label);
	public String[] getLabels();

	public boolean hasOverrides();

	public boolean run(final CommandSender sender, final String[] args);


}
