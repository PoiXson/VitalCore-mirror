package com.poixson.utils.gson;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.block.data.BlockData;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;


public class GsonAdapter_BlockData extends TypeAdapter<BlockData> {



	public void write(final JsonWriter out, final BlockData block) throws IOException {
		out.jsonValue(block.getAsString());
	}
	public BlockData read(final JsonReader in) throws IOException {
		return Bukkit.createBlockData(in.nextString());
	}



}
