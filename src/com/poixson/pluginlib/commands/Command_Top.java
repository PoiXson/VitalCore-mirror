package com.poixson.pluginlib.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import com.poixson.pluginlib.pxnPluginLib;
import com.poixson.tools.commands.pxnCommandRoot;


// /top
public class Command_Top extends pxnCommandRoot {



	public Command_Top(final pxnPluginLib plugin) {
		super(
			plugin,
			"pxn", // namespace
			"Teleport to the highest block.", // desc
			null, // usage
			"pxn.cmd.top", // perm
			new String[] { // labels
				"top"
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
