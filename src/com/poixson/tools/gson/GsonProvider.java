package com.poixson.utils.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.poixson.tools.Keeper;


public final class GsonUtils {
	private GsonUtils() {}
	static { Keeper.add(new GsonUtils()); }



	public static Gson GSON() {
		final GsonBuilder builder =
			new GsonBuilder()
			.disableHtmlEscaping()
			.setPrettyPrinting();
		GsonAdapter_BlockData.Register(builder);
		GsonAdapter_BlockData.Register(builder);
		return builder.create();
	}



}
