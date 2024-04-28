package com.poixson.pluginlib.commands;

import org.bukkit.command.CommandSender;

import com.poixson.pluginlib.pxnPluginLib;
import com.poixson.tools.commands.pxnCommandRoot;


public class Command_Reply extends pxnCommandRoot {



	public Command_Reply(final pxnPluginLib plugin) {
		super(
			plugin,
			"Reply to the last player to send you a message.", // desc
			null, // usage
			"pxn.cmd.msg", // perm
			"r", "reply"
		);
	}



	@Override
	public boolean onCommand(final CommandSender sender, final String[] args) {
System.out.println("COMMAND:"); for (final String arg : args) System.out.println("  "+arg);
return false;
	}



}
