package com.poixson.pluginlib.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import com.poixson.pluginlib.pxnPluginLib;
import com.poixson.tools.commands.pxnCommandRoot;


// /gma
public class Command_GMA extends pxnCommandRoot {



	public Command_GMA(final pxnPluginLib plugin) {
		super(
			plugin,
			"pxn", // namespace
			"Change game mode to Adventure.", // desc
			null, // usage
			"pxn.cmd.gm.a", // perm
			new String[] { // labels
				"gma", "gm-a",
				"gmadventure", "gm-adventure"
			}
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
