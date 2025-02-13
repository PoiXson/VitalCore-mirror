package com.poixson.tools.gson;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.block.data.BlockData;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;


public class GsonAdapter_BlockData extends TypeAdapter<BlockData> {



	public GsonAdapter_BlockData() {
		super();
	}



	@Override
	public BlockData read(final JsonReader in) throws IOException {
		if (in.peek() == JsonToken.NULL) { in.nextNull(); return null; }
		return Bukkit.createBlockData(in.nextString());
	}



	@Override
	public void write(final JsonWriter out, final BlockData block) throws IOException {
		if (block == null) { out.nullValue(); return; }
		out.jsonValue(block.getAsString());
	}



}
