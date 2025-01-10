package com.poixson.vitalcore.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import com.poixson.tools.commands.pxnCommandRoot;
import com.poixson.vitalcore.VitalCorePlugin;


// /delhome
public class CMD_Home_Del extends pxnCommandRoot {



	public CMD_Home_Del(final VitalCorePlugin plugin) {
		super(
			plugin,
			"pxn", // namespace
			"Remove a saved home.", // desc
			null, // usage
			"pxn.cmd.home", // perm
			// labels
			"delhome",
			"del-home",
			"deletehome",
			"delete-home",
			"rmhome",
			"rm-home",
			"removehome",
			"remove-home"
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
