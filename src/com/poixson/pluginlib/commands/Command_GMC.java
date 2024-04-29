package com.poixson.pluginlib.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import com.poixson.pluginlib.pxnPluginLib;
import com.poixson.tools.commands.pxnCommandRoot;


// /gmc
public class Command_GMC extends pxnCommandRoot {



	public Command_GMC(final pxnPluginLib plugin) {
		super(
			plugin,
			"pxn", // namespace
			"Change game mode to Creative.", // desc
			null, // usage
			"pxn.cmd.gm.c", // perm
			new String[] { // labels
				"gmc", "gm-c",
				"gmcreative", "gm-creative"
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
