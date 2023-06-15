package com.poixson.commonmc.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MapMeta;


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



	@SuppressWarnings("deprecation")
	public static void SetMapID(final ItemStack map, final int id) {
		final MapMeta meta = (MapMeta) map.getItemMeta();
		meta.setMapId(id);
		map.setItemMeta(meta);
	}



}
