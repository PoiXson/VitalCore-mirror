package com.poixson.pluginlib.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import com.poixson.pluginlib.pxnPluginLib;
import com.poixson.tools.commands.pxnCommandRoot;


// /gmspec
public class Command_GMSpec extends pxnCommandRoot {



	public Command_GMSpec(final pxnPluginLib plugin) {
		super(
			plugin,
			"pxn", // namespace
			"Change game mode to Spectator.", // desc
			null, // usage
			"pxn.cmd.gm.spec", // perm
			new String[] { // labels
				"gmsp",   "gm-sp",
				"gmspec", "gm-spec"
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
