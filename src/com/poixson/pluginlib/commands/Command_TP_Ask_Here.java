package com.poixson.pluginlib.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import com.poixson.pluginlib.pxnPluginLib;
import com.poixson.tools.commands.pxnCommandRoot;


// /tp-ask-here
public class Command_TP_Ask_Here extends pxnCommandRoot {



	public Command_TP_Ask_Here(final pxnPluginLib plugin) {
		super(
			plugin,
			"Ask other players to teleport to your current location.", // desc
			null, // usage
			"pxn.cmd.tp.askhere", // perm
			"tpah", "tpahere",
			"tpaskhere", "tp-ask-here"
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
