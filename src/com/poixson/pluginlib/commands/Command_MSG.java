package com.poixson.pluginlib.commands;

import org.bukkit.command.CommandSender;

import com.poixson.pluginlib.pxnPluginLib;
import com.poixson.tools.commands.pxnCommandRoot;


public class Command_MSG extends pxnCommandRoot {



	public Command_MSG(final pxnPluginLib plugin) {
		super(
			plugin,
			"Send a private message to a player.", // desc
			null, // usage
			"pxn.cmd.msg", // perm
			"m", "msg"
		);
	}



	@Override
	public boolean onCommand(final CommandSender sender, final String[] args) {
System.out.println("COMMAND:"); for (final String arg : args) System.out.println("  "+arg);
return false;
	}



}
