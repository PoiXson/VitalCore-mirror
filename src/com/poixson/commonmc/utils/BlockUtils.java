package com.poixson.commonmc.utils;

import java.util.concurrent.atomic.AtomicReference;

import org.bukkit.Bukkit;
import org.bukkit.block.data.BlockData;

import com.poixson.tools.Keeper;
import com.poixson.utils.Utils;


public class BlockUtils {
	private BlockUtils() {}
	static { Keeper.add(new BlockUtils()); }



	public static BlockData StringToBlockData(final AtomicReference<String> atomic, final String def) {
		final String blockStr = atomic.get();
		if (Utils.notEmpty(blockStr))
			return StringToBlockData(blockStr);
		return StringToBlockData(def);
	}
	public static BlockData StringToBlockData(final String blockStr) {
		return Bukkit.createBlockData(blockStr);
	}



}
