package com.poixson.tools.plotter;

import static com.poixson.utils.BlockUtils.EqualsBlockMaterial;
import static com.poixson.utils.BlockUtils.EqualsBlockType;
import static com.poixson.utils.LocationUtils.AxToIxyz;
import static com.poixson.utils.LocationUtils.AxisToIxyz;
import static com.poixson.utils.LocationUtils.Rotate;
import static com.poixson.utils.StringUtils.SplitKeyVal;
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

	public int x = 0;
	public int y = 0;
	public int z = 0;
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
		if (IsEmpty(json)) return null;
		final Map<String, String> parts =
			SplitKeyVal(
				json,
				"### PARAMS ###",
				"### MATRIX ###",
				"### SCRIPT ###"
			);
		final String json_params = parts.get("### PARAMS ###");
		final String json_matrix = parts.get("### MATRIX ###");
		final String json_script = parts.get("### SCRIPT ###");
		final BlockPlotter plot = FromJSON(json_params);
		final String[][] arrays = GSON().fromJson(json_matrix, String[][].class);
		final int n = arrays.length;
		final StringBuilder[][] matrix = new StringBuilder[n][];
		for (int i=0; i<n; i++) {
			final int nn = arrays[i].length;
			matrix[i] = new StringBuilder[nn];
			for (int ii=0; ii<nn; ii++) {
				matrix[i][ii] = new StringBuilder();
				matrix[i][ii].append(arrays[i][ii]);
			}
		}
		return new Triple<BlockPlotter, StringBuilder[][], String>(plot, matrix, json_script);
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
			for (int iz=start_z; iz<=end_z; iz++) {
				for (int ix=start_x; ix<=end_x; ix++) {
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
		final int n = matrix.length;
		for (int i=0; i<n; i++) {
			final int xx = (add0.a * i) + x;
			final int yy = (add0.b * i) + y;
			final int zz = (add0.c * i) + z;
			final int nn = matrix[i].length;
			for (int ii=0; ii<nn; ii++) {
				final int nnn = matrix[i][ii].length();
				final int xxx = (add1.a * ii) + xx;
				final int yyy = (add1.b * ii) + yy;
				final int zzz = (add1.c * ii) + zz;
				for (int iii=0; iii<nnn; iii++) {
					this.setBlock(placer,
						(add2.a*iii)+xxx, (add2.b*iii)+yyy, (add2.c*iii)+zzz,
						matrix[i][ii].charAt(iii)
					);
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
		final int n = matrix.length;
		for (int i=0; i<n; i++) {
			final int xx = (add0.a * i) + x;
			final int yy = (add0.b * i) + y;
			final int zz = (add0.c * i) + z;
			final int nn = matrix[i].length();
			for (int ii=0; ii<nn; ii++) {
				this.setBlock(placer,
					(add1.a*ii)+xx, (add1.b*ii)+yy, (add1.c*ii)+zz,
					matrix[i].charAt(ii)
				);
			}
		}
	}
	public void run(final BlockPlacer placer, final StringBuilder matrix,
			final int x, final int y, final int z) {
		if (IsEmpty(this.axis))      throw new RuntimeException("Axis not set");
		if (this.axis.length() != 1) throw new RuntimeException("Invalid axis length");
		final Iabc add = AxToIxyz(this.axis.charAt(0));
		final int n = matrix.length();
		for (int i=0; i<n; i++) {
			this.setBlock(placer,
				(add.a*i)+x, (add.b*i)+y, (add.c*i)+z,
				matrix.charAt(i)
			);
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
		placer.setBlock(this.x+loc.a, this.y+y, this.z+loc.b, type);
	}
	public void setBlock(final BlockPlacer placer,
			final int x, final int y, final int z, final Material type) {
		final Iabcd loc = Rotate(new Iabcd(x, z, this.w, this.d), this.rotation);
		placer.setBlock(this.x+loc.a, this.y+y, this.z+loc.b, type);
	}

	public BlockData getBlock(final BlockPlacer placer,
			final int x, final int y, final int z) {
		final Iabcd loc = Rotate(new Iabcd(x, z, this.w, this.d), this.rotation);
		return placer.getBlock(this.x+loc.a, this.y+y, this.z+loc.b);
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
		if (match == 0) return false;
		return EqualsBlockType(
			this.types.get(match),         // expect
			this.getBlock(placer, x, y, z) // actual
		);
	}
	// match material
	public boolean isType(final BlockPlacer placer,
			final int x, final int y, final int z,
			final Material match) {
		return EqualsBlockMaterial(
			match,                         // expect
			this.getBlock(placer, x, y, z) // actual
		);
	}
	// match blockdata
	public boolean isType(final BlockPlacer placer,
			final int x, final int y, final int z,
			final BlockData match) {
		return EqualsBlockType(
			match,                         // expect
			this.getBlock(placer, x, y, z) // actual
		);
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
		return this.getMatrix3D(size0, size1, 0);
	}
	public StringBuilder[] getMatrix2D() {
		if (IsEmpty(this.axis))      throw new RuntimeException("Axis not set");
		if (this.axis.length() != 2) throw new RuntimeException("Invalid axis length");
		final int size0 = this.getAxSize(this.axis.charAt(0));
		if (size0 <= 0) throw new IllegalArgumentException("Invalid size value: "+Integer.toString(size0));
		return this.getMatrix2D(size0, 0);
	}
	public StringBuilder getMatrix1D() {
		return this.getMatrix1D(0);
	}



	public StringBuilder[][] getMatrix3D(final int size0, final int size1, final int size2) {
		final String fill = " ".repeat(size2);
		final LinkedList<StringBuilder[]> list0 = new LinkedList<StringBuilder[]>();
		for (int i=0; i<size0; i++) {
			final LinkedList<StringBuilder> list1 = new LinkedList<StringBuilder>();
			for (int ii=0; ii<size1; ii++)
				list1.addLast( (new StringBuilder()).append(fill) );
			list0.addLast(list1.toArray(new StringBuilder[0]));
		}
		return list0.toArray(new StringBuilder[0][0]);
	}
	public StringBuilder[] getMatrix2D(final int size0, final int size1) {
		final String fill = " ".repeat(size1);
		final LinkedList<StringBuilder> list = new LinkedList<StringBuilder>();
		for (int i=0; i<size0; i++)
			list.addLast( (new StringBuilder()).append(fill) );
		return list.toArray(new StringBuilder[0]);
	}
	public StringBuilder getMatrix1D(final int size) {
		return (new StringBuilder()).repeat(' ', size);
	}



}
