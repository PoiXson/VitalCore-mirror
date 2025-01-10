package com.poixson.vitalcore.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import com.poixson.tools.commands.pxnCommandRoot;
import com.poixson.vitalcore.VitalCorePlugin;


// /walkspeed
public class CMD_Speed_Walk extends pxnCommandRoot {



	public CMD_Speed_Walk(final VitalCorePlugin plugin) {
		super(
			plugin,
			"pxn", // namespace
			null, // desc
			null, // usage
			"pxn.cmd.speed.walk", // perm
			// labels
			"walkspeed",
			"walk-speed"
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
