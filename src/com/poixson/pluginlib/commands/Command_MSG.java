package com.poixson.pluginlib.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import com.poixson.pluginlib.pxnPluginLib;
import com.poixson.tools.commands.pxnCommandRoot;


// /msg
public class Command_MSG extends pxnCommandRoot {



	public Command_MSG(final pxnPluginLib plugin) {
		super(
			plugin,
			"pxn", // namespace
			"Send a private message to a player.", // desc
			null, // usage
			"pxn.cmd.msg", // perm
			// labels
			"m",
			"msg"
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
