package com.poixson.pluginlib.commands;

import org.bukkit.command.CommandSender;

import com.poixson.tools.commands.xCMD_Labels;


public class Command_Jump extends xCMD_Labels {



	public Command_Jump() {
		super(
			"j",  "jump", "jumpto",
			"jd", "jumpdown",
			"ji", "jumpinto",
			"jt", "jumpthrough"
		);
	}



	@Override
	public boolean run(final CommandSender sender, final String[] args) {
//TODO
return false;
	}



}
