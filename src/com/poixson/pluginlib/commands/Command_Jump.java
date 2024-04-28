package com.poixson.pluginlib.commands;

import org.bukkit.command.CommandSender;

import com.poixson.pluginlib.pxnPluginLib;
import com.poixson.tools.commands.pxnCommandRoot;


public class Command_Jump extends pxnCommandRoot {



	public Command_Jump(final pxnPluginLib plugin) {
		super(
			plugin,
			"Teleport to the location you are looking at.", // desc
			null, // usage
			"pxn.cmd.jump", // perm
			"j",  "jump",
			"jumpto", "jump-to"
		);
	}



	@Override
	public boolean onCommand(final CommandSender sender, final String[] args) {
System.out.println("COMMAND:"); for (final String arg : args) System.out.println("  "+arg);
return false;
	}



}
