package com.poixson.vitalcore.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import com.poixson.tools.commands.pxnCommandRoot;
import com.poixson.vitalcore.VitalCorePlugin;


// /list-homes
public class CMD_Home_List extends pxnCommandRoot {



	public CMD_Home_List(final VitalCorePlugin plugin) {
		super(
			plugin,
			"pxn", // namespace
			"List your saved homes.", // desc
			null, // usage
			"pxn.cmd.home", // perm
			// labels
			"homes",
			"listhomes",
			"list-homes",
			"homelist",
			"home-list",
			"homeslist",
			"homes-list"
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
