package com.poixson.utils;

import static com.poixson.utils.Utils.IsEmpty;

import java.util.concurrent.atomic.AtomicReference;

import org.bukkit.Bukkit;
import org.bukkit.block.data.BlockData;

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



}
