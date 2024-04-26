package com.poixson.pluginlib.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.poixson.tools.commands.xCMD_Labels;


public class Command_GC extends xCMD_Labels {



	public Command_GC() {
		super("gc");
	}



	@Override
	public boolean run(final CommandSender sender, final String[] args) {
		final Player player = (sender instanceof Player ? (Player)sender : null);
		if (player != null
		&& !player.hasPermission("pxn.cmd.gc"))
			return false;
		System.gc();
		sender.sendMessage("Ran garbage collection");
		return true;
	}



}
