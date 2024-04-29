package com.poixson.pluginlib.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import com.poixson.pluginlib.pxnPluginLib;
import com.poixson.tools.commands.pxnCommandRoot;


// /flyspeed
public class Command_FlySpeed extends pxnCommandRoot {



	public Command_FlySpeed(final pxnPluginLib plugin) {
		super(
			plugin,
			"pxn", // namespace
			null, // desc
			null, // usage
			"pxn.cmd.speed.fly", // perm
			new String[] { // labels
				"flyspeed", "fly-speed"
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
