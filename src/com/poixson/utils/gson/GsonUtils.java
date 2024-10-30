package com.poixson.utils.gson;

import org.bukkit.block.data.BlockData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.poixson.tools.Keeper;


public final class GsonUtils {
	private GsonUtils() {}
	static { Keeper.add(new GsonUtils()); }



	public static Gson GSON() {
		return (new GsonBuilder()
			.registerTypeAdapter(BlockData.class, new GsonAdapter_BlockData())
			.disableHtmlEscaping()
			.setPrettyPrinting()
			.create());
	}



}
