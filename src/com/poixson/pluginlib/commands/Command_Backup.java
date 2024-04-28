package com.poixson.pluginlib.commands;

import org.bukkit.command.CommandSender;

import com.poixson.pluginlib.pxnPluginLib;
import com.poixson.tools.commands.pxnCommandRoot;


public class Command_Backup extends pxnCommandRoot {



	public Command_Backup(final pxnPluginLib plugin) {
		super(
			plugin,
			"Perform a backup of the worlds and configs.", // desc
			null, // usage
			"pxn.cmd.backup", // perm
			"backup"
		);
	}



	@Override
	public boolean onCommand(final CommandSender sender, final String[] args) {
System.out.println("COMMAND:"); for (final String arg : args) System.out.println("  "+arg);
return false;
	}



}
