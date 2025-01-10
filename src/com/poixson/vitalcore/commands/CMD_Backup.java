package com.poixson.vitalcore.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import com.poixson.tools.commands.pxnCommandRoot;
import com.poixson.vitalcore.VitalCorePlugin;


// /backup
public class CMD_Backup extends pxnCommandRoot {



	public CMD_Backup(final VitalCorePlugin plugin) {
		super(
			plugin,
			"pxn", // namespace
			"Perform a backup of the worlds and configs.", // desc
			null, // usage
			"pxn.cmd.backup", // perm
			// labels
			"backup"
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
