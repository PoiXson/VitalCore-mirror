package com.poixson.pluginlib.commands;

import org.bukkit.command.CommandSender;

import com.poixson.pluginlib.pxnPluginLib;
import com.poixson.tools.commands.pxnCommandRoot;


public class Command_TP_Offline extends pxnCommandRoot {



	public Command_TP_Offline(final pxnPluginLib plugin) {
		super(
			plugin,
			"Teleport to the last known location of a player.", // desc
			null, // usage
			"pxn.cmd.tp.offline", // perm
			"tpoffline", "tp-offline"
		);
	}



	@Override
	public boolean onCommand(final CommandSender sender, final String[] args) {
System.out.println("COMMAND:"); for (final String arg : args) System.out.println("  "+arg);
return false;
	}



}
