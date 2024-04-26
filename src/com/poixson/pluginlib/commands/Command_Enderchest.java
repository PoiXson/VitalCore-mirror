package com.poixson.pluginlib.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.poixson.tools.commands.xCMD_Labels;


public class Command_Enderchest extends xCMD_Labels {



	public Command_Enderchest() {
		super("enderchest");
	}



	@Override
	public boolean run(final CommandSender sender, final String[] args) {
		if (sender instanceof Player) {
			final Player player = (Player) sender;
			if (!player.hasPermission(""))
					return false;
			player.closeInventory();
			final Inventory chest = player.getEnderChest();
			player.openInventory(chest);
			return true;
		}
		return false;
	}



}
