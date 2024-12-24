package com.poixson.tools.gson;

import static com.poixson.utils.Utils.IsEmpty;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.poixson.tools.plotter.BlockPlotter;
import com.poixson.tools.plotter.BlockPlotterHolder;
import com.poixson.utils.MathUtils;


public class GsonAdapter_BlockPlotterHolder extends TypeAdapter<BlockPlotterHolder> {



	@Override
	public BlockPlotterHolder read(final JsonReader in) throws IOException {
		if (in.peek() == JsonToken.NULL) { in.nextNull(); return null; }
		Map<Character, BlockData> types  = null;
		StringBuilder[][]         matrix = null;
		String script = null;
		String title  = null;
		String author = null;
		String axis   = null;
		String rot    = null;
		int x = Integer.MIN_VALUE;
		int y = Integer.MIN_VALUE;
		int z = Integer.MIN_VALUE;
		in.beginObject();
		//LOOP_KEYS:
		while (in.hasNext()) {
			SWITCH_KEY:
			switch (in.nextName()) {
			case "types": {
				types = new HashMap<Character, BlockData>();
				in.beginObject();
				while (in.hasNext()) {
					final String key = in.nextName();
					final String val = in.nextString();
					final char chr = key.charAt(0);
					final BlockData block = Bukkit.createBlockData(val);
					types.put(Character.valueOf(chr), block);
				}
				in.endObject();
				break SWITCH_KEY;
			}
			case "matrix": {
				final LinkedList<StringBuilder[]> listA = new LinkedList<StringBuilder[]>();
				in.beginArray();
				//LOOP_A:
				while (in.hasNext()) {
					final LinkedList<StringBuilder> listB = new LinkedList<StringBuilder>();
					in.beginArray();
					//LOOP_B:
					while (in.hasNext()) {
						final StringBuilder row = new StringBuilder();
						final String line = in.nextString();
						if (!IsEmpty(line))
							row.append(line);
						listB.addLast(row);
					} // end LOOP_B
					in.endArray();
					listA.addLast(listB.toArray(new StringBuilder[0]));
				} // end LOOP_A
				in.endArray();
				matrix = listA.toArray(new StringBuilder[0][]);
				break SWITCH_KEY;
			}
			case "title":  title  = in.nextString(); break SWITCH_KEY;
			case "author": author = in.nextString(); break SWITCH_KEY;
			case "axis":                               axis = in.nextString(); break SWITCH_KEY;
			case "rot": case "rotate": case "rotation": rot = in.nextString(); break SWITCH_KEY;
			case "x": x = MathUtils.ToInteger(in.nextString(), Integer.MIN_VALUE); break SWITCH_KEY;
			case "y": y = MathUtils.ToInteger(in.nextString(), Integer.MIN_VALUE); break SWITCH_KEY;
			case "z": z = MathUtils.ToInteger(in.nextString(), Integer.MIN_VALUE); break SWITCH_KEY;
			default: throw new RuntimeException("Invalid json: "+in.nextName());
			} // end SWITCH_KEY
		} // end LOOP_KEYS
		in.endObject();
		BlockPlotter plot = null;
		if (!IsEmpty(axis) || !IsEmpty(rot)
		|| x != Integer.MIN_VALUE || y != Integer.MIN_VALUE || z != Integer.MIN_VALUE) {
			plot = new BlockPlotter();
			if (!IsEmpty(axis)) plot.axis     = axis;
			if (!IsEmpty(rot )) plot.rotation = BlockFace.valueOf(rot);
			if (x != Integer.MIN_VALUE) plot.x = x;
			if (y != Integer.MIN_VALUE) plot.y = y;
			if (z != Integer.MIN_VALUE) plot.z = z;
		}
		return new BlockPlotterHolder(
			plot,
			types,
			matrix,
			script,
			title,
			author
		);
	}



	@Override
	public void write(final JsonWriter out, final BlockPlotterHolder dao) throws IOException {
		if (dao == null) { out.nullValue(); return; }
		out.beginObject();
		// block plotter
		if (dao.plot != null) {
			// axis
			if (!IsEmpty(dao.plot.axis) && dao.plot.axis != BlockPlotter.DEFAULT_AXIS_3D) {
				out.name("axis"); out.value(dao.plot.axis); }
			// rotation
			if (dao.plot.rotation != null && dao.plot.rotation != BlockPlotter.DEFAULT_ROTATION) {
				out.name("rotation"); out.value(dao.plot.rotation.toString()); }
			// x/y/z
			if (dao.plot.x != 0 || dao.plot.y != 0 || dao.plot.z != 0) {
				out.name("x"); out.value(dao.plot.x);
				out.name("y"); out.value(dao.plot.y);
				out.name("z"); out.value(dao.plot.z);
			}
		}
		// types
		if (dao.types != null) {
			out.name("types");
			out.beginObject();
			for (final Entry<Character, BlockData> entry : dao.types.entrySet()) {
				out.name(entry.getKey().toString());
				out.value(entry.getValue().getAsString());
			}
			out.endObject();
		}
		// matrix
		if (dao.matrix != null) {
			out.name("matrix");
			out.beginArray();
			for (final StringBuilder[] list : dao.matrix) {
				out.beginArray();
				for (final StringBuilder line : list)
					out.value(line.toString());
				out.endArray();
			}
			out.endArray();
		}
		out.endObject();
	}



}
