package com.poixson.tools.gson;


import static com.poixson.utils.MathUtils.ToInteger;
import static com.poixson.utils.Utils.IsEmpty;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;


public class GsonAdapter_Location extends TypeAdapter<Location> {



	public static void Register(final GsonBuilder builder) {
		builder.registerTypeAdapter(Location.class, new GsonAdapter_Location());
	}



	public Location read(final JsonReader in) throws IOException {
		if (in.peek() == JsonToken.NULL) { in.nextNull(); return null; }
		final String str = in.nextString();
		final int pos_space = str.lastIndexOf(' ');
		final String world_str = str.substring(0, pos_space).trim(); if (IsEmpty(world_str)) return null;
		final World world = Bukkit.getWorld(world_str);              if (world == null)      return null;
		final String loc_str = str.substring(pos_space+1);           if (IsEmpty(loc_str))   return null;
		final String[] parts = loc_str.split(",", 3);                if (parts.length != 3)  return null;
		final int x = ToInteger(parts[0]);
		final int y = ToInteger(parts[1]);
		final int z = ToInteger(parts[2]);
		final Block block = world.getBlockAt(x, y, z);               if (block == null)      return null;
		return block.getLocation();
	}

	public void write(final JsonWriter out, final Location loc) throws IOException {
		if (loc == null) { out.nullValue(); return; }
		out.value(String.format("%s %d,%d,%d",
			loc.getWorld().getName(),
			Integer.valueOf(loc.getBlockX()),
			Integer.valueOf(loc.getBlockY()),
			Integer.valueOf(loc.getBlockZ())
		));
	}



}
