package com.poixson.pluginlib.commands;

import org.bukkit.command.CommandSender;

import com.poixson.pluginlib.pxnPluginLib;
import com.poixson.tools.commands.pxnCommandRoot;


public class Command_List extends pxnCommandRoot {



	public Command_List(final pxnPluginLib plugin) {
		super(
			plugin,
			"List the players currently online.", // desc
			null, // usage
			"pxn.cmd.list", // perm
			"list",
			"online"
		);
	}



	@Override
	public boolean onCommand(final CommandSender sender, final String[] args) {
System.out.println("COMMAND:"); for (final String arg : args) System.out.println("  "+arg);
return false;
	}



}
