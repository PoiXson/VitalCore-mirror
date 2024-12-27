package com.poixson.pluginlib.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import com.poixson.pluginlib.pxnPluginLib;
import com.poixson.tools.commands.pxnCommandRoot;


// /jump-down
public class Command_Jump_Down extends pxnCommandRoot {



	public Command_Jump_Down(final pxnPluginLib plugin) {
		super(
			plugin,
			"pxn", // namespace
			"Teleport into the nearest space below the location you are looking at.", // desc
			null, // usage
			"pxn.cmd.jump", // perm
			// labels
			"jd",
			"jumpdown",
			"jump-down"
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
