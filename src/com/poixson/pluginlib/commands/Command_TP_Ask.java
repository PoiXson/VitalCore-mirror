package com.poixson.pluginlib.commands;

import org.bukkit.command.CommandSender;

import com.poixson.pluginlib.pxnPluginLib;
import com.poixson.tools.commands.pxnCommandRoot;


public class Command_TP_Ask extends pxnCommandRoot {



	public Command_TP_Ask(final pxnPluginLib plugin) {
		super(
			plugin,
			"Teleport to a location or world.", // desc
			null, // usage
			"pxn.cmd.tp.ask", // perm
			"tpa", "tpask", "tp-ask",
			"teleportask", "teleport-ask",
			"askteleport", "ask-teleport"
		);
	}



	@Override
	public boolean onCommand(final CommandSender sender, final String[] args) {
System.out.println("COMMAND:"); for (final String arg : args) System.out.println("  "+arg);
return false;
	}



}
