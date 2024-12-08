package com.poixson.utils.gson;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;

import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;


public class GsonAdapter_BlockData extends TypeAdapter<BlockData> {



	public static void Register(final GsonBuilder builder) {
		builder.registerTypeAdapter(Location.class, new GsonAdapter_Location());
		builder.registerTypeAdapter(BlockData.class, new GsonAdapter_BlockData());
	}



	public BlockData read(final JsonReader in) throws IOException {
		if (in.peek() == JsonToken.NULL) { in.nextNull(); return null; }
		return Bukkit.createBlockData(in.nextString());
	}

	public void write(final JsonWriter out, final BlockData block) throws IOException {
		if (block == null) { out.nullValue(); return; }
		out.jsonValue(block.getAsString());
	}



}
