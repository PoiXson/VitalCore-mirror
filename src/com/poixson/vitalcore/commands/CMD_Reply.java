package com.poixson.vitalcore.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import com.poixson.tools.commands.pxnCommandRoot;
import com.poixson.vitalcore.VitalCorePlugin;


// /reply
public class CMD_Reply extends pxnCommandRoot {



	public CMD_Reply(final VitalCorePlugin plugin) {
		super(
			plugin,
			"pxn", // namespace
			"Reply to the last player to send you a message.", // desc
			null, // usage
			"pxn.cmd.msg", // perm
			// labels
			"r",
			"reply"
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
