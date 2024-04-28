package com.poixson.pluginlib.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import com.poixson.pluginlib.pxnPluginLib;
import com.poixson.tools.commands.pxnCommandRoot;


// /tp-asl-all-here
public class Command_TP_Ask_All_Here extends pxnCommandRoot {



	public Command_TP_Ask_All_Here(final pxnPluginLib plugin) {
		super(
			plugin,
			"Ask all players to teleport to your current location.", // desc
			null, // usage
			"pxn.cmd.tp.askallhere", // perm
			"tpaa", "tpaah",
			"tpaskall",   "tpaskallhere",
			"tp-ask-all", "tp-ask-all-here"
		);
	}



	@Override
	public boolean onCommand(final CommandSender sender, final String[] args) {
System.out.println("COMMAND:"); for (final String arg : args) System.out.println("  "+arg);
return false;
	}



	@Override
	public List<String> onTabComplete(final CommandSender sender, final String[] args) {
//TODO
System.out.println("TAB:"); for (final String arg : args) System.out.println("  "+arg);
return null;
	}



}
