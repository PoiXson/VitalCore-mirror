package com.poixson.pluginlib.commands;

import org.bukkit.command.CommandSender;

import com.poixson.pluginlib.pxnPluginLib;
import com.poixson.tools.commands.pxnCommandRoot;


public class Command_Heal extends pxnCommandRoot {



	public Command_Heal(final pxnPluginLib plugin) {
		super(
			plugin,
			"Restore full health.", // desc
			null, // usage
			"pxn.cmd.heal", // perm
			"heal"
		);
//TODO: rest
	}



	@Override
	public boolean onCommand(final CommandSender sender, final String[] args) {
System.out.println("COMMAND:"); for (final String arg : args) System.out.println("  "+arg);
return false;
	}



}
