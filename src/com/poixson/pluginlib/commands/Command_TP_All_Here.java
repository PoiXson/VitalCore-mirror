package com.poixson.pluginlib.commands;

import org.bukkit.command.CommandSender;

import com.poixson.pluginlib.pxnPluginLib;
import com.poixson.tools.commands.pxnCommandRoot;


public class Command_TP_All_Here extends pxnCommandRoot {



	public Command_TP_All_Here(final pxnPluginLib plugin) {
		super(
			plugin,
			"Teleport all other players to your current location.", // desc
			null, // usage
			"pxn.cmd.tp.allhere", // perm
			"tpah",
			"tpall",  "tpallhere",
			"tp-all", "tp-all-here"
		);
	}



	@Override
	public boolean onCommand(final CommandSender sender, final String[] args) {
System.out.println("COMMAND:"); for (final String arg : args) System.out.println("  "+arg);
return false;
	}



}
