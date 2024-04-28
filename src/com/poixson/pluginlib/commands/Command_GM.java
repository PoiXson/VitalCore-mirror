package com.poixson.pluginlib.commands;

import org.bukkit.command.CommandSender;

import com.poixson.pluginlib.pxnPluginLib;
import com.poixson.tools.commands.pxnCommandRoot;


public class Command_GM extends pxnCommandRoot {



	public Command_GM(final pxnPluginLib plugin) {
		super(
			plugin,
			"Change game mode.", // desc
			null, // usage
			"pxn.cmd.gm", // perm
			"gm"
//			"gmc",
//			"gms",
//			"gma",
//			"gmsp", "gmspec"
		);
	}



	@Override
	public boolean onCommand(final CommandSender sender, final String[] args) {
System.out.println("COMMAND:"); for (final String arg : args) System.out.println("  "+arg);
return false;
	}



}
