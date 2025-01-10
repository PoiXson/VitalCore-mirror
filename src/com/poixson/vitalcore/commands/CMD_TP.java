package com.poixson.vitalcore.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import com.poixson.tools.commands.pxnCommandRoot;
import com.poixson.vitalcore.VitalCorePlugin;


// /tp
public class CMD_TP extends pxnCommandRoot {



	public CMD_TP(final VitalCorePlugin plugin) {
		super(
			plugin,
			"pxn", // namespace
			"Teleport to a location or world.", // desc
			null, // usage
			"pxn.cmd.tp", // perm
			// labels
			"tp"
//TODO
//			"tpconfirm",
//			"tpcancel",
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
