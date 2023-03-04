package com.poixson.commonmc.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public final class ItemUtils {
	private ItemUtils() {}



	// custom item model
	public static int GetCustomModel(final ItemStack stack) {
		if (stack.hasItemMeta()) {
			final ItemMeta meta = stack.getItemMeta();
			if (meta.hasCustomModelData())
				return meta.getCustomModelData();
		}
		return 0;
	}



}
