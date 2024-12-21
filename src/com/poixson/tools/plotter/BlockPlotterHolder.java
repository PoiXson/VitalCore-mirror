package com.poixson.tools.plotter;

import static com.poixson.tools.gson.GsonProvider.GSON;
import static com.poixson.utils.Utils.IsEmpty;

import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.block.data.BlockData;


public class BlockPlotterHolder {

	public final BlockPlotter              plot;
	public final Map<Character, BlockData> types;
	public final StringBuilder[][]         matrix;
	public final String                    script;
	public final String                    title;
	public final String                    author;



	public static BlockPlotterHolder FromJson(final String json) {
		return FromJson(json, null);
	}
	protected static BlockPlotterHolder FromJson(final String json, final String script) {
		if (IsEmpty(json)) return null;
		if (IsEmpty(script)) {
			final String[] parts = json.split("### SCRIPT ###");
			if (parts.length > 1)
				return FromJson(parts[0], parts[1]);
		}
		final BlockPlotterHolder holder = GSON().fromJson(json, BlockPlotterHolder.class);
		return (
			IsEmpty(script)
			? holder
			: holder.setScript(script)
		);
	}



	public BlockPlotterHolder(final BlockPlotter plot,
			final StringBuilder[][] matrix) {
		this(plot, null, matrix, null);
	}
	public BlockPlotterHolder(final Map<Character, BlockData> types,
			final StringBuilder[][] matrix) {
		this(null, types, matrix, null);
	}
	public BlockPlotterHolder(final Map<Character, BlockData> types,
			final StringBuilder[][] matrix, final String script) {
		this(null, types, matrix, script);
	}
	public BlockPlotterHolder(final BlockPlotter plot, final Map<Character, BlockData> types,
			final StringBuilder[][] matrix, final String script) {
		this(plot, types, matrix, script, null, null);
	}
	public BlockPlotterHolder(final BlockPlotter plot, final Map<Character, BlockData> types,
			final StringBuilder[][] matrix, final String script,
			final String title, final String author) {
		this.plot   = plot;
		this.types  = types;
		this.matrix = matrix;
		this.script = script;
		this.title  = title;
		this.author = author;
	}



	@Override
	public BlockPlotterHolder clone() {
		return new BlockPlotterHolder(
			this.plot.clone(),
			this.types,
			this.matrix,
			this.script,
			this.title,
			this.author
		);
	}
	public BlockPlotterHolder setScript(final String script) {
		return new BlockPlotterHolder(
			this.plot,
			this.types,
			this.matrix,
			script,
			this.title,
			this.author
		);
	}



	public BlockPlotter getPlotter() {
		if (this.plot == null) {
			final BlockPlotter plot = new BlockPlotter();
			// axis
			plot.axis = BlockPlotter.DEFAULT_AXIS_3D;
			// w/h/d
			if (this.matrix != null)
				plot.setAxisSizes(this.matrix);
			// types
			for (final Entry<Character, BlockData> entry : this.types.entrySet())
				plot.type(entry.getKey().charValue(), entry.getValue());
			return plot;
		}
		return this.plot;
	}



}
