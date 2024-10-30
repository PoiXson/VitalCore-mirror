package com.poixson.tools.plotter;

import static com.poixson.utils.GsonUtils.GSON;
import static com.poixson.utils.LocationUtils.AxToIxyz;
import static com.poixson.utils.LocationUtils.Rotate;
import static com.poixson.utils.Utils.SafeClose;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.ChunkGenerator.ChunkData;
import org.bukkit.generator.LimitedRegion;

import com.poixson.tools.abstractions.Tuple;
import com.poixson.tools.dao.Iabc;
import com.poixson.tools.dao.Iabcd;
import com.poixson.tools.plotter.placer.BlockPlacer;
import com.poixson.utils.FileUtils;


public class BlockPlotter implements Serializable {
	private static final long serialVersionUID = 1L;

	public transient int x = 0;
	public transient int y = 0;
	public transient int z = 0;
	public int w, h, d;

	public String axis;
	public BlockFace rotation = BlockFace.SOUTH;

	protected final Map<Character, BlockData> types = new HashMap<Character, BlockData>();



	public BlockPlotter() {
	}



	// -------------------------------------------------------------------------------



	public static BlockPlotter FromJSON(final String json) {
		return GSON().fromJson(json, BlockPlotter.class);
	}
	public String toJson() {
		return GSON().toJson(this);
	}



	public static Tuple<BlockPlotter, StringBuilder[][]> Load(final File file)
			throws IOException {
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			final String json = FileUtils.ReadInputStream(in);
			return Load(json);
		} finally {
			SafeClose(in);
		}
	}
	public static Tuple<BlockPlotter, StringBuilder[][]> Load(final String json) {
		final String[] parts = json.split("###", 2);
		final BlockPlotter plot = FromJSON(parts[0]);
		final String[][] arrays = GSON().fromJson(parts[1], String[][].class);
		final int d1 = arrays.length;
		final int d2 = arrays[0].length;
		final StringBuilder[][] matrix = new StringBuilder[d1][];
		for (int i=0; i<d1; i++) {
			matrix[i] = new StringBuilder[d2];
			for (int ii=0; ii<d2; ii++) {
				matrix[i][ii] = new StringBuilder();
				matrix[i][ii].append(arrays[i][ii]);
			}
		}
		return new Tuple<BlockPlotter, StringBuilder[][]>(plot, matrix);
	}



	// -------------------------------------------------------------------------------
	// position



	public BlockPlotter axis(final String axis) {
		this.axis = axis;
		return this;
	}
	public BlockPlotter rotate(final BlockFace rotation) {
		this.rotation = rotation;
		return this;
	}



	public BlockPlotter x(final int x) { this.x = x; return this; }
	public BlockPlotter y(final int y) { this.y = y; return this; }
	public BlockPlotter z(final int z) { this.z = z; return this; }

	public BlockPlotter xyz(final int x, final int y, final int z) {
		this.x = x; this.y = y; this.z = z;
		return this;
	}
	public BlockPlotter xy(final int x, final int y) {
		this.x = x; this.y = y;
		return this;
	}
	public BlockPlotter xz(final int x, final int z) {
		this.x = x; this.z = z;
		return this;
	}

	public BlockPlotter loc(final Location loc) {
		this.x = loc.getBlockX();
		this.y = loc.getBlockY();
		this.z = loc.getBlockZ();
		return this;
	}



	public BlockPlotter w(final int w) { this.w = w; return this; }
	public BlockPlotter h(final int h) { this.h = h; return this; }
	public BlockPlotter d(final int d) { this.d = d; return this; }

	public BlockPlotter whd(final int w, final int h, final int d) {
		this.w = w; this.h = h; this.d = d;
		return this;
	}
	public BlockPlotter wh(final int w, final int h) {
		this.w = w; this.h = h;
		return this;
	}
	public BlockPlotter wd(final int w, final int d) {
		this.w = w; this.d = d;
		return this;
	}



	public int getAxSize(final char ax) {
		switch (ax) {
		case 'e': case 'w': case 'x': case 'X': return this.w;
		case 'u': case 'd': case 'y': case 'Y': return this.h;
		case 'n': case 's': case 'z': case 'Z': return this.d;
		default: throw new RuntimeException("Unknown ax size for: "+Character.toString(ax));
		}
	}



	// -------------------------------------------------------------------------------
	// place blocks



	public void run(final World world, final StringBuilder[][] matrix) {
		this.run(new BlockPlacer(world), matrix);
	}
	public void run(final World world, final StringBuilder[] matrix) {
		this.run(new BlockPlacer(world), matrix);
	}
	public void run(final World world, final StringBuilder matrix) {
		this.run(new BlockPlacer(world), matrix);
	}



	public void run(final ChunkData chunk, final StringBuilder[][] matrix) {
		this.run(new BlockPlacer(chunk), matrix);
	}
	public void run(final ChunkData chunk, final StringBuilder[] matrix) {
		this.run(new BlockPlacer(chunk), matrix);
	}
	public void run(final ChunkData chunk, final StringBuilder matrix) {
		this.run(new BlockPlacer(chunk), matrix);
	}



	public void run(final LimitedRegion region, final StringBuilder[][] matrix) {
		this.run(new BlockPlacer(region), matrix);
	}
	public void run(final LimitedRegion region, final StringBuilder[] matrix) {
		this.run(new BlockPlacer(region), matrix);
	}
	public void run(final LimitedRegion region, final StringBuilder matrix) {
		this.run(new BlockPlacer(region), matrix);
	}



	public void run(final BlockPlacer placer, final StringBuilder[][] matrix) {
		this.run(placer, matrix, 0, 0, 0);
	}
	public void run(final BlockPlacer placer, final StringBuilder[] matrix) {
		this.run(placer, matrix, 0, 0, 0);
	}
	public void run(final BlockPlacer placer, final StringBuilder matrix) {
		this.run(placer, matrix, 0, 0, 0);
	}



	public void run(final BlockPlacer placer, final StringBuilder[][] matrix,
			final int x, final int y, final int z) {
		final Iabc add0 = AxToIxyz(this.axis.charAt(0));
		final Iabc add1 = AxToIxyz(this.axis.charAt(1));
		final Iabc add2 = AxToIxyz(this.axis.charAt(2));
		final int len0 = matrix.length;
		int len1, len2;
		int xx,   yy,   zz;
		int xxx,  yyy,  zzz;
		int xxxx, yyyy, zzzz;
		for (int i=0; i<len0; i++) {
			xx = (add0.a * i) + x;
			yy = (add0.b * i) + y;
			zz = (add0.c * i) + z;
			len1 = matrix[i].length;
			for (int ii=0; ii<len1; ii++) {
				len2 = matrix[i][ii].length();
				xxx = (add1.a * ii) + xx;
				yyy = (add1.b * ii) + yy;
				zzz = (add1.c * ii) + zz;
				for (int iii=0; iii<len2; iii++) {
					xxxx = (add2.a * iii) + xxx;
					yyyy = (add2.b * iii) + yyy;
					zzzz = (add2.c * iii) + zzz;
					this.setBlock(placer, xxxx, yyyy, zzzz, matrix[i][ii].charAt(iii));
				}
			}
		}
	}
	public void run(final BlockPlacer placer, final StringBuilder[] matrix,
			final int x, final int y, final int z) {
		final Iabc add0 = AxToIxyz(this.axis.charAt(0));
		final Iabc add1 = AxToIxyz(this.axis.charAt(1));
		final int len0 = matrix.length;
		int len1;
		int xx,  yy,  zz;
		int xxx, yyy, zzz;
		for (int i=0; i<len0; i++) {
			xx = (add0.a * i) + x;
			yy = (add0.b * i) + y;
			zz = (add0.c * i) + z;
			len1 = matrix[i].length();
			for (int ii=0; ii<len1; ii++) {
				xxx = (add1.a * ii) + xx;
				yyy = (add1.b * ii) + yy;
				zzz = (add1.c * ii) + zz;
				this.setBlock(placer, xxx, yyy, zzz, matrix[i].charAt(ii));
			}
		}
	}
	public void run(final BlockPlacer placer, final StringBuilder matrix,
			final int x, final int y, final int z) {
		final Iabc add = AxToIxyz(this.axis.charAt(0));
		int xx, yy, zz;
		final int len = matrix.length();
		for (int i=0; i<len; i++) {
			xx = (add.a * i) + x;
			yy = (add.b * i) + y;
			zz = (add.c * i) + z;
			this.setBlock(placer, xx, yy, zz, matrix.charAt(i));
		}
	}



	public void setBlock(final BlockPlacer placer,
			final int x, final int y, final int z, final char chr) {
		final BlockData type = this.types.get(Character.valueOf(chr));
		if (type != null)
			this.setBlock(placer, x, y, z, type);
	}
	public void setBlock(final BlockPlacer placer,
			final int x, final int y, final int z, final String type) {
		if (type.length() == 1) {
			this.setBlock(placer, x, y, z, type.charAt(0));
		} else {
			final BlockData block = Bukkit.createBlockData(type);
			if (block != null)
				this.setBlock(placer, x, y, z, block);
		}
	}
	public void setBlock(final BlockPlacer placer,
			final int x, final int y, final int z, final BlockData type) {
		final Iabcd loc = Rotate(new Iabcd(x, z, this.w, this.d), this.rotation);
		final int xx = this.x + loc.a;
		final int zz = this.z + loc.b;
		final int yy = this.y + y;
		placer.setBlock(xx, yy, zz, type);
	}
	public void setBlock(final BlockPlacer placer,
			final int x, final int y, final int z, final Material type) {
		final Iabcd loc = Rotate(new Iabcd(x, z, this.w, this.d), this.rotation);
		final int xx = this.x + loc.a;
		final int zz = this.z + loc.b;
		final int yy = this.y + y;
		placer.setBlock(xx, yy, zz, type);
	}

	public BlockData getBlock(final BlockPlacer placer,
			final int x, final int y, final int z) {
		final Iabcd loc = Rotate(new Iabcd(x, z, this.w, this.d), this.rotation);
		final int xx = this.x + loc.a;
		final int zz = this.z + loc.b;
		final int yy = this.y + y;
		return placer.getBlock(xx, yy, zz);
	}



	// -------------------------------------------------------------------------------
	// block types



	public BlockPlotter type(final char chr, final String type) {
		return this.type(chr, Bukkit.createBlockData(type));
	}
	public BlockPlotter type(final char chr, final Material type) {
		return this.type(chr, Bukkit.createBlockData(type));
	}
	public BlockPlotter type(final char chr, final Material type, final String params) {
		return this.type(chr, Bukkit.createBlockData(type, params));
	}
	public BlockPlotter type(final char chr, final BlockData block) {
		this.types.put(Character.valueOf(chr), block);
		return this;
	}



	// -------------------------------------------------------------------------------
	// matrix



	public StringBuilder[][] getMatrix3D() {
		final int size0 = this.getAxSize(this.axis.charAt(0));
		final int size1 = this.getAxSize(this.axis.charAt(1));
		final LinkedList<StringBuilder[]> list0 = new LinkedList<StringBuilder[]>();
		for (int i=0; i<size0; i++) {
			final LinkedList<StringBuilder> list1 = new LinkedList<StringBuilder>();
			for (int ii=0; ii<size1; ii++)
				list1.addLast(new StringBuilder());
			list0.addLast(list1.toArray(new StringBuilder[0]));
		}
		return list0.toArray(new StringBuilder[0][0]);
	}
	public StringBuilder[] getMatrix2D() {
		final int size = this.getAxSize(this.axis.charAt(0));
		final LinkedList<StringBuilder> list = new LinkedList<StringBuilder>();
		for (int i=0; i<size; i++)
			list.addLast(new StringBuilder());
		return list.toArray(new StringBuilder[0]);
	}
	public StringBuilder getMatrix1D() {
		return new StringBuilder();
	}



}
