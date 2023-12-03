package com.poixson.pluginlib.tools.plotter;

import static com.poixson.pluginlib.utils.BlockMatrixUtils.LocsToArray;
import static com.poixson.pluginlib.utils.BlockMatrixUtils.SizesToArray;

import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.ChunkGenerator.ChunkData;
import org.bukkit.generator.LimitedRegion;

import com.poixson.exceptions.RequiredArgumentException;
import com.poixson.utils.Utils;


public class PlotterFactory {

	public BlockPlacer placer = null;

	public String axis = null;
	public BlockFace rotation = null;

	public int x = 0;
	public int y = 0;
	public int z = 0;

	public int w = Integer.MIN_VALUE;
	public int h = Integer.MIN_VALUE;
	public int d = Integer.MIN_VALUE;



	public PlotterFactory() {
	}



	public BlockPlotter build() {
		if (this.placer == null)         throw new RequiredArgumentException("placer");
		if (Utils.isEmpty(this.axis))    throw new RequiredArgumentException("axis");
		if (this.axis.contains("x") || this.axis.contains("X")
		||  this.axis.contains("e") || this.axis.contains("w")) {
			if (this.x == Integer.MIN_VALUE) throw new RequiredArgumentException("x");
			if (this.w == Integer.MIN_VALUE) throw new RequiredArgumentException("w");
		}
		if (this.axis.contains("y") || this.axis.contains("Y")
		||  this.axis.contains("u") || this.axis.contains("d")) {
			if (this.y == Integer.MIN_VALUE) throw new RequiredArgumentException("y");
			if (this.h == Integer.MIN_VALUE) throw new RequiredArgumentException("h");
		}
		if (this.axis.contains("z") || this.axis.contains("Z")
		||  this.axis.contains("n") || this.axis.contains("s")) {
			if (this.z == Integer.MIN_VALUE) throw new RequiredArgumentException("z");
			if (this.d == Integer.MIN_VALUE) throw new RequiredArgumentException("d");
		}
		final int[] locs  = LocsToArray( this.axis, this.x, this.y, this.z);
		final int[] sizes = SizesToArray(this.axis, this.w, this.h, this.d);
		final BlockMatrix matrix = new BlockMatrix(this.axis, locs, sizes);
		final BlockPlotter plot = new BlockPlotter(this.placer, matrix);
		if (this.rotation != null)
			plot.rotation = this.rotation;
		return plot;
	}



	public PlotterFactory placer(final BlockPlacer placer) {
		this.placer = placer;
		return this;
	}
	public PlotterFactory placer(final World world) {
		this.placer = new BlockPlacer(world);
		return this;
	}
	public PlotterFactory placer(final ChunkData chunk) {
		this.placer = new BlockPlacer(chunk);
		return this;
	}
	public PlotterFactory placer(final LimitedRegion region) {
		this.placer = new BlockPlacer(region);
		return this;
	}
	public PlotterFactory placer(final BlockPlacer_WorldEdit worldedit) {
		this.placer = new BlockPlacer(worldedit);
		return this;
	}



	public PlotterFactory axis(final String axis) {
		this.axis = axis;
		return this;
	}
	public PlotterFactory rotate(final BlockFace rotation) {
		this.rotation = rotation;
		return this;
	}



	public PlotterFactory xyz(final int x, final int y, final int z) {
		this.x = x; this.y = y; this.z = z;
		return this;
	}
	public PlotterFactory xy(final int x, final int y) {
		this.x = x; this.y = y;
		return this;
	}
	public PlotterFactory xz(final int x, final int z) {
		this.x = x; this.z = z;
		return this;
	}

	public PlotterFactory x(final int x) { this.x = x; return this; }
	public PlotterFactory y(final int y) { this.y = y; return this; }
	public PlotterFactory z(final int z) { this.z = z; return this; }



	public PlotterFactory whd(final int w, final int h, final int d) {
		this.w = w; this.h = h; this.d = d;
		return this;
	}
	public PlotterFactory wh(final int w, final int h) {
		this.w = w; this.h = h;
		return this;
	}
	public PlotterFactory wd(final int w, final int d) {
		this.w = w; this.d = d;
		return this;
	}

	public PlotterFactory w(final int w) { this.w = w; return this; }
	public PlotterFactory h(final int h) { this.h = h; return this; }
	public PlotterFactory d(final int d) { this.d = d; return this; }



}
