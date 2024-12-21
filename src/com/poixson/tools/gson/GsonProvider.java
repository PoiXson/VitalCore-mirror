package com.poixson.tools.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.poixson.tools.Keeper;


public final class GsonProvider {
	private GsonProvider() {}
	static { Keeper.add(new GsonProvider()); }



	public static Gson GSON() {
		final GsonBuilder builder =
			new GsonBuilder()
			.disableHtmlEscaping()
			.setPrettyPrinting();
		GsonAdapter_BlockData         .Register(builder);
		GsonAdapter_Location          .Register(builder);
		GsonAdapter_BlockPlotterHolder.Register(builder);
		return builder.create();
	}



}
