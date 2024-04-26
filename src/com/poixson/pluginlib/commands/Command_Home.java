package com.poixson.pluginlib.commands;

import org.bukkit.command.CommandSender;

import com.poixson.tools.commands.xCMD_Labels;


public class Command_Home extends xCMD_Labels {



	public Command_Home() {
		super(
			"home",
			"homes", "listhomes", "homelist", "homeslist",
			"sethome",
			"delhome", "rmhome"
		);
	}



	@Override
	public boolean run(final CommandSender sender, final String[] args) {
//TODO
return false;
	}



}
