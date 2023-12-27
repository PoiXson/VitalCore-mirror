package com.poixson.utils;

import static com.poixson.utils.Utils.IsEmpty;

import java.util.concurrent.atomic.AtomicReference;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.poixson.tools.Keeper;


public final class BlockUtils {
	private BlockUtils() {}
	static { Keeper.add(new BlockUtils()); }



	public static BlockData StringToBlockData(final AtomicReference<String> atomic, final String def) {
		final String blockStr = atomic.get();
		return (IsEmpty(blockStr) ? StringToBlockData(def) : StringToBlockData(blockStr));
	}
	public static BlockData StringToBlockData(final String blockStr) {
		return Bukkit.createBlockData(blockStr);
	}



	// custom item model
	public static int GetCustomModel(final ItemStack stack) {
		if (stack.hasItemMeta()) {
			final ItemMeta meta = stack.getItemMeta();
			if (meta.hasCustomModelData())
				return meta.getCustomModelData();
		}
		return 0;
	}



	public static boolean isSign(final Material type) {
		if (isWallSign(type))        return true;
		if (isStandingSign(type))    return true;
		if (isHangingSign(type))     return true;
		if (isHangingWallSign(type)) return true;
		return false;
	}
	public static boolean isWallSign(final Material type) {
		switch (type) {
		case ACACIA_WALL_SIGN:
		case BAMBOO_WALL_SIGN:
		case BIRCH_WALL_SIGN:
		case CRIMSON_WALL_SIGN:
		case DARK_OAK_WALL_SIGN:
		case JUNGLE_WALL_SIGN:
		case MANGROVE_WALL_SIGN:
		case OAK_WALL_SIGN:
		case SPRUCE_WALL_SIGN:
		case WARPED_WALL_SIGN:
			return true;
		default: break;
		}
		return false;
	}
	public static boolean isStandingSign(final Material type) {
		switch (type) {
		case ACACIA_SIGN:
		case BAMBOO_SIGN:
		case BIRCH_SIGN:
		case CRIMSON_SIGN:
		case DARK_OAK_SIGN:
		case JUNGLE_SIGN:
		case MANGROVE_SIGN:
		case OAK_SIGN:
		case SPRUCE_SIGN:
		case WARPED_SIGN:
			return true;
		default: break;
		}
		return false;
	}
	public static boolean isHangingSign(final Material type) {
		switch (type) {
		case ACACIA_HANGING_SIGN:
		case BAMBOO_HANGING_SIGN:
		case BIRCH_HANGING_SIGN:
		case CRIMSON_HANGING_SIGN:
		case DARK_OAK_HANGING_SIGN:
		case JUNGLE_HANGING_SIGN:
		case MANGROVE_HANGING_SIGN:
		case OAK_HANGING_SIGN:
		case SPRUCE_HANGING_SIGN:
		case WARPED_HANGING_SIGN:
			return true;
		default: break;
		}
		return false;
	}
	public static boolean isHangingWallSign(final Material type) {
		switch (type) {
		case ACACIA_WALL_HANGING_SIGN:
		case BAMBOO_WALL_HANGING_SIGN:
		case BIRCH_WALL_HANGING_SIGN:
		case CRIMSON_WALL_HANGING_SIGN:
		case DARK_OAK_WALL_HANGING_SIGN:
		case JUNGLE_WALL_HANGING_SIGN:
		case MANGROVE_WALL_HANGING_SIGN:
		case OAK_WALL_HANGING_SIGN:
		case SPRUCE_WALL_HANGING_SIGN:
		case WARPED_WALL_HANGING_SIGN:
			return true;
		default: break;
		}
		return false;
	}



}
