package com.poixson.vitalcore.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import com.poixson.tools.commands.pxnCommandRoot;
import com.poixson.vitalcore.VitalCorePlugin;


// /msg
public class CMD_MSG extends pxnCommandRoot {



	public CMD_MSG(final VitalCorePlugin plugin) {
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
