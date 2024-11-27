package com.poixson.tools.plotter;

import static com.poixson.utils.LocationUtils.AxToIxyz;
import static com.poixson.utils.LocationUtils.AxisToIxyz;
import static com.poixson.utils.LocationUtils.Rotate;
import static com.poixson.utils.Utils.IsEmpty;
import static com.poixson.utils.Utils.SafeClose;
import static com.poixson.utils.gson.GsonUtils.GSON;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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

import com.poixson.tools.abstractions.Triple;
import com.poixson.tools.dao.Iabc;
import com.poixson.tools.dao.Iabcd;
import com.poixson.tools.plotter.placer.BlockPlacer;
import com.poixson.utils.FileUtils;


public class BlockPlotter implements Serializable {
	private static final long serialVersionUID = 1L;

	public transient int x = 0;
	public transient int y = 0;
	public transient int z = 0;
	public int w = 0;
	public int h = 0;
	public int d = 0;

	public String axis = null;
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



	public static Triple<BlockPlotter, StringBuilder[][], String> Load(
			final Class<?> clss, final String file_local, final String file_res)
			throws IOException {
		final InputStream in = FileUtils.OpenLocalOrResource(clss, file_local, file_res);
		return (in==null ? null : Load(in));
	}
	public static Triple<BlockPlotter, StringBuilder[][], String> Load(
			final File file) throws IOException {
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			return Load(in);
		} finally {
			SafeClose(in);
		}
	}
	public static Triple<BlockPlotter, StringBuilder[][], String> Load(
			final InputStream in) throws IOException {
		final String json = FileUtils.ReadInputStream(in);
		return Load(json);
	}
	public static Triple<BlockPlotter, StringBuilder[][], String> Load(
			final String json) {
		final String[] partsAB = json.split("### MATRIX ###", 2);
		if (partsAB.length != 2) throw new RuntimeException("Invalid structure json, missing matrix");
		final BlockPlotter plot = FromJSON(partsAB[0]);
		final String[] partsCD = partsAB[1].split("### SCRIPT ###", 2);
		final String partD = (partsCD.length==2 ? partsCD[1] : null);
		final String[][] arrays = GSON().fromJson(partsCD[0], String[][].class);
		final int d1 = arrays.length;
		final StringBuilder[][] matrix = new StringBuilder[d1][];
		for (int i=0; i<d1; i++) {
			final int d2 = arrays[i].length;
			matrix[i] = new StringBuilder[d2];
			for (int ii=0; ii<d2; ii++) {
				matrix[i][ii] = new StringBuilder();
				matrix[i][ii].append(arrays[i][ii]);
			}
		}
		return new Triple<BlockPlotter, StringBuilder[][], String>(plot, matrix, partD);
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



	public boolean isWithinLocation(final int x, final int y, final int z) {
		if (IsEmpty(this.axis))      throw new RuntimeException("Axis not set");
		if (this.axis.length() != 3) throw new RuntimeException("Invalid axis length");
		final Iabcd loc = Rotate(new Iabcd(this.x, this.z, this.w, this.d), this.rotation);
		final Iabc dir = AxisToIxyz(Rotate(this.axis, this.rotation));
		final int w = loc.c * dir.a;
		final int d = loc.d * dir.c;
		final int h = this.h * dir.b;
		final int min_x = Math.min(loc.a, loc.a+w);
		final int max_x = Math.max(loc.a, loc.a+w);
		final int min_z = Math.min(loc.b, loc.b+d);
		final int max_z = Math.max(loc.b, loc.b+d);
		final int min_y = Math.min(this.y, this.y+h);
		final int max_y = Math.max(this.y, this.y+h);
		return (
			x >= min_x && x < max_x &&
			y >= min_y && y < max_y &&
			z >= min_z && z < max_z
		);
	}
	public boolean isWithinLocation(final int x, final int z) {
		if (IsEmpty(this.axis))      throw new RuntimeException("Axis not set");
		if (this.axis.length() != 2) throw new RuntimeException("Invalid axis length");
		final Iabcd loc = Rotate(new Iabcd(this.x, this.z, this.w, this.d), this.rotation);
		final Iabc dir = AxisToIxyz(Rotate(this.axis, this.rotation));
		final int w = loc.c * dir.a;
		final int d = loc.d * dir.c;
		final int min_x = Math.min(loc.a, loc.a+w);
		final int max_x = Math.max(loc.a, loc.a+w);
		final int min_z = Math.min(loc.b, loc.b+d);
		final int max_z = Math.max(loc.b, loc.b+d);
		return (
			x >= min_x && x < max_x &&
			z >= min_z && z < max_z
		);
	}
	public boolean isWithinChunk() {
		if (IsEmpty(this.axis)) throw new RuntimeException("Axis not set");
		final Iabcd loc = Rotate(new Iabcd(this.x, this.z, this.w, this.d), this.rotation);
		final Iabc dir = AxisToIxyz(Rotate(this.axis, this.rotation));
		final int w = loc.c * dir.a;
		final int d = loc.d * dir.c;
		final int min_x = Math.min(loc.a, loc.a+w);
		final int max_x = Math.max(loc.a, loc.a+w);
		final int min_z = Math.min(loc.b, loc.b+d);
		final int max_z = Math.max(loc.b, loc.b+d);
		return (
			max_x >= 0 && min_x < 16 &&
			max_z >= 0 && min_z < 16
		);
	}



	public int findSurfaceY(final BlockPlacer placer,
			final int y_min, final int y_max, final int search) {
		return this.findSurfaceY(placer, y_min, y_max, 0-search, search, 0-search, search);
	}
	public int findSurfaceY(final BlockPlacer placer, final int y_min, final int y_max,
			final int start_x, final int end_x, final int start_z, final int end_z) {
		final int h = y_max - y_min;
		for (int iy=0; iy<h; iy++) {
			for (int iz=start_z; iz<end_z+1; iz++) {
				for (int ix=start_x; ix<end_x+1; ix++) {
					final BlockData block = this.getBlock(placer, ix, iy, iz);
					final Material type = block.getMaterial();
					if (Material.AIR.equals(type))
						return iy;
				}
			}
		}
		return Integer.MIN_VALUE;
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
		if (IsEmpty(this.axis))      throw new RuntimeException("Axis not set");
		if (this.axis.length() != 3) throw new RuntimeException("Invalid axis length");
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
		if (IsEmpty(this.axis))      throw new RuntimeException("Axis not set");
		if (this.axis.length() != 2) throw new RuntimeException("Invalid axis length");
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
		if (IsEmpty(this.axis))      throw new RuntimeException("Axis not set");
		if (this.axis.length() != 1) throw new RuntimeException("Invalid axis length");
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
	// replace block



	// char => char
	public boolean replaceBlock(final BlockPlacer placer,
			final int x, final int y, final int z,
			final char expect, final char replace) {
		if (this.isType(placer, x, y, z, expect)) {
			this.setBlock(placer, x, y, z, replace);
			return true;
		}
		return false;
	}
	// char => material
	public boolean replaceBlock(final BlockPlacer placer,
			final int x, final int y, final int z,
			final char expect, final Material replace) {
		if (this.isType(placer, x, y, z, expect)) {
			this.setBlock(placer, x, y, z, replace);
			return true;
		}
		return false;
	}
	// char => blockdata
	public boolean replaceBlock(final BlockPlacer placer,
			final int x, final int y, final int z,
			final char expect, final BlockData replace) {
		if (this.isType(placer, x, y, z, expect)) {
			this.setBlock(placer, x, y, z, replace);
			return true;
		}
		return false;
	}

	// material => material
	public boolean replaceBlock(final BlockPlacer placer,
			final int x, final int y, final int z,
			final Material expect, final Material replace) {
		if (this.isType(placer, x, y, z, expect)) {
			this.setBlock(placer, x, y, z, replace);
			return true;
		}
		return false;
	}
	// material => char
	public boolean replaceBlock(final BlockPlacer placer,
			final int x, final int y, final int z,
			final Material expect, final char replace) {
		if (this.isType(placer, x, y, z, expect)) {
			this.setBlock(placer, x, y, z, replace);
			return true;
		}
		return false;
	}
	// material => blockdata
	public boolean replaceBlock(final BlockPlacer placer,
			final int x, final int y, final int z,
			final Material expect, final BlockData replace) {
		if (this.isType(placer, x, y, z, expect)) {
			this.setBlock(placer, x, y, z, replace);
			return true;
		}
		return false;
	}

	// blockdata => blockdata
	public boolean replaceBlock(final BlockPlacer placer,
			final int x, final int y, final int z,
			final BlockData expect, final BlockData replace) {
		if (this.isType(placer, x, y, z, expect)) {
			this.setBlock(placer, x, y, z, replace);
			return true;
		}
		return false;
	}
	// blockdata => char
	public boolean replaceBlock(final BlockPlacer placer,
			final int x, final int y, final int z,
			final BlockData expect, final char replace) {
		if (this.isType(placer, x, y, z, expect)) {
			this.setBlock(placer, x, y, z, replace);
			return true;
		}
		return false;
	}
	// blockdata => material
	public boolean replaceBlock(final BlockPlacer placer,
			final int x, final int y, final int z,
			final BlockData expect, final Material replace) {
		if (this.isType(placer, x, y, z, expect)) {
			this.setBlock(placer, x, y, z, replace);
			return true;
		}
		return false;
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



	// match char
	public boolean isType(final BlockPlacer placer,
			final int x, final int y, final int z,
			final char match) {
		if (match == 0)
			return false;
		final BlockData match_block = this.types.get(match);
		if (match_block == null)
			return false;
		final BlockData actual_block = this.getBlock(placer, x, y, z);
		if (actual_block == null)
			return false;
		final Material actual_mat = actual_block.getMaterial();
		if (!match_block.getMaterial().equals(actual_mat))
			return false;
//TODO: more checks (more in BlockUtils::EqualsBlock())
		return true;
	}
	// match material
	public boolean isType(final BlockPlacer placer,
			final int x, final int y, final int z,
			final Material match) {
		if (match == null)
			return false;
		final BlockData actual_block = this.getBlock(placer, x, y, z);
		if (actual_block == null)
			return false;
		final Material actual_mat = actual_block.getMaterial();
		if (!match.equals(actual_mat))
			return false;
//TODO: more checks
		return true;
	}
	// match blockdata
	public boolean isType(final BlockPlacer placer,
			final int x, final int y, final int z,
			final BlockData match) {
		if (match == null)
			return false;
		final Material match_mat = match.getMaterial();
		if (match_mat == null)
			return false;
		final BlockData actual_block = this.getBlock(placer, x, y, z);
		if (actual_block == null)
			return false;
		final Material actual_mat = actual_block.getMaterial();
		if (!match_mat.equals(actual_mat))
			return false;
//TODO: more checks
		return true;
	}



	// -------------------------------------------------------------------------------
	// matrix



	public StringBuilder[][] getMatrix3D() {
		if (IsEmpty(this.axis))      throw new RuntimeException("Axis not set");
		if (this.axis.length() != 3) throw new RuntimeException("Invalid axis length");
		final int size0 = this.getAxSize(this.axis.charAt(0));
		final int size1 = this.getAxSize(this.axis.charAt(1));
		if (size0 <= 0) throw new IllegalArgumentException("Invalid size0 value: "+Integer.toString(size0));
		if (size1 <= 0) throw new IllegalArgumentException("Invalid size1 value: "+Integer.toString(size1));
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
		if (IsEmpty(this.axis))      throw new RuntimeException("Axis not set");
		if (this.axis.length() != 2) throw new RuntimeException("Invalid axis length");
		final int size = this.getAxSize(this.axis.charAt(0));
		if (size <= 0) throw new IllegalArgumentException("Invalid size value: "+Integer.toString(size));
		final LinkedList<StringBuilder> list = new LinkedList<StringBuilder>();
		for (int i=0; i<size; i++)
			list.addLast(new StringBuilder());
		return list.toArray(new StringBuilder[0]);
	}
	public StringBuilder getMatrix1D() {
		if (IsEmpty(this.axis))      throw new RuntimeException("Axis not set");
		if (this.axis.length() != 1) throw new RuntimeException("Invalid axis length");
		return new StringBuilder();
	}



}
