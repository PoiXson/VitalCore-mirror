package com.poixson.pluginlib.commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.poixson.pluginlib.pxnPluginLib;
import com.poixson.tools.commands.pxnCommandRoot;


// /enderchest
public class Command_Enderchest extends pxnCommandRoot {



	public Command_Enderchest(final pxnPluginLib plugin) {
		super(
			plugin,
			"pxn", // namespace
			"Open the Enderchest.", // desc
			null, // usage
			"pxn.cmd.enderchest", // perm
			new String[] { // labels
				"enderchest"
			}
		);
	}



	@Override
	public boolean onCommand(final CommandSender sender, final String[] args) {
		if (sender instanceof Player) {
			final Player player = (Player) sender;
			if (!player.hasPermission("pxn.cmd.enderchest"))
					return false;
			player.closeInventory();
			final Inventory chest = player.getEnderChest();
			player.openInventory(chest);
			return true;
		}
		return false;
	}



	@Override
	public List<String> onTabComplete(final CommandSender sender, final String[] args) {
//TODO
System.out.println("TAB:"); for (final String arg : args) System.out.println("  "+arg);
return null;
	}



}
