package com.poixson.pluginlib.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import com.poixson.pluginlib.pxnPluginLib;
import com.poixson.tools.commands.pxnCommandRoot;


// /gm
public class Command_GM extends pxnCommandRoot {



	public Command_GM(final pxnPluginLib plugin) {
		super(
			plugin,
			"pxn", // namespace
			"Change game mode.", // desc
			null, // usage
			"pxn.cmd.gm", // perm
			new String[] { // labels
				"gm"
			}
			"gm"
//			"gmc",
//			"gms",
//			"gma",
//			"gmsp", "gmspec"
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
