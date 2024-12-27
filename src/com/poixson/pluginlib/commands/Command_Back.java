package com.poixson.pluginlib.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import com.poixson.pluginlib.pxnPluginLib;
import com.poixson.tools.commands.pxnCommandRoot;


// /back
public class Command_Back extends pxnCommandRoot {



	public Command_Back(final pxnPluginLib plugin) {
		super(
			plugin,
			"pxn", // namespace
			"Teleport back to your previous location.", // desc
			null, // usage
			"pxn.cmd.back", // perm
			// labels
			"back"
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
