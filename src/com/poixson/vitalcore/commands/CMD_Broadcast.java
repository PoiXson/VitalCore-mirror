package com.poixson.vitalcore.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import com.poixson.tools.commands.pxnCommandRoot;
import com.poixson.vitalcore.VitalCorePlugin;


// /broadcast
public class CMD_Broadcast extends pxnCommandRoot {



	public CMD_Broadcast(final VitalCorePlugin plugin) {
		super(
			plugin,
			"pxn", // namespace
			null, // desc
			null, // usage
			"pxn.cmd.broadcast", // perm
			// labels
			"broadcast",
			"announce"
		);
	}



	@Override
	public boolean onCommand(final CommandSender sender, final String[] args) {
//TODO
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
