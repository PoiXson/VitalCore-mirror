package com.poixson.pluginlib.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import com.poixson.pluginlib.pxnPluginLib;
import com.poixson.tools.commands.pxnCommandRoot;


// /list
public class Command_List extends pxnCommandRoot {



	public Command_List(final pxnPluginLib plugin) {
		super(
			plugin,
			"pxn", // namespace
			"List the players currently online.", // desc
			null, // usage
			"pxn.cmd.list", // perm
			// labels
			"list",
			"online"
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
