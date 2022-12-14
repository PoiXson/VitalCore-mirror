package com.poixson.commonbukkit.tools;

import static com.poixson.commonbukkit.utils.LocationUtils.AxisToValue;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Lightable;
import org.bukkit.block.data.type.Slab;
import org.bukkit.generator.ChunkGenerator.ChunkData;
import org.bukkit.generator.LimitedRegion;

import com.poixson.exceptions.RequiredArgumentException;
import com.poixson.tools.dao.Ixyz;
import com.poixson.utils.StringUtils;
import com.poixson.utils.Utils;


public class BlockPlotter {

	public final World world;
	public final ChunkData chunk;
	public final LimitedRegion region;

	public int absX, absY, absZ;

	public HashMap<Character, Material> types = new HashMap<Character, Material>();
	public HashMap<Character, String> special = new HashMap<Character, String>();



	public BlockPlotter(final World world) {
		this(world, 0, 0, 0);
	}
	public BlockPlotter(final ChunkData chunk) {
		this(chunk, 0, 0, 0);
	}
	public BlockPlotter(final LimitedRegion region) {
		this(region, 0, 0, 0);
	}

	public BlockPlotter(final World world, final int x, final int y, final int z) {
		if (world == null) throw new NullPointerException();
		this.world  = world;
		this.chunk  = null;
		this.region = null;
		this.absX = x;
		this.absY = y;
		this.absZ = z;
	}
	public BlockPlotter(final ChunkData chunk, final int x, final int y, final int z) {
		if (chunk == null) throw new NullPointerException();
		this.world  = null;
		this.chunk  = chunk;
		this.region = null;
		this.absX = x;
		this.absY = y;
		this.absZ = z;
	}
	public BlockPlotter(final LimitedRegion region, final int x, final int y, final int z) {
		if (region == null) throw new NullPointerException();
		this.world  = null;
		this.chunk  = null;
		this.region = region;
		this.absX = x;
		this.absY = y;
		this.absZ = z;
	}



	public StringBuilder[][] getEmptyMatrix3D(final int size1, final int size2) {
		final StringBuilder[][] matrix = new StringBuilder[size1][size2];
		for (int i=0; i<size1; i++) {
			for (int ii=0; ii<size2; ii++) {
				matrix[i][ii] = new StringBuilder();
			}
		}
		return matrix;
	}
	public StringBuilder[] getEmptyMatrix2D(final int size1) {
		final StringBuilder[] matrix = new StringBuilder[size1];
		for (int i=0; i<size1; i++) {
			matrix[i] = new StringBuilder();
		}
		return matrix;
	}
	public StringBuilder getEmptyMatrix1D() {
		return new StringBuilder();
	}

	public StringBuilder[][] getNewMatrix3D(final int size1, final int size2, final int size3) {
		final StringBuilder[][] matrix = new StringBuilder[size1][size2];
		for (int i=0; i<size1; i++) {
			for (int ii=0; ii<size2; ii++) {
				matrix[i][ii] = new StringBuilder();
				matrix[i][ii].append(StringUtils.Repeat(size3, ' '));
			}
		}
		return matrix;
	}
	public StringBuilder[] getNewMatrix2D(final int size1, final int size2) {
		final StringBuilder[] matrix = new StringBuilder[size1];
		for (int i=0; i<size1; i++) {
			matrix[i] = new StringBuilder();
			matrix[i].append(StringUtils.Repeat(size2, ' '));
		}
		return matrix;
	}
	public StringBuilder getNewMatrix1D(final int size) {
		final StringBuilder matrix = new StringBuilder();
		matrix.append(StringUtils.Repeat(size, ' '));
		return matrix;
	}



	public void place3D(final String axis, final StringBuilder[][] matrix) {
		if (Utils.isEmpty(axis)) throw new RequiredArgumentException("axis");
		if (axis.length() != 3) throw new RuntimeException("Invalid axis length: "+axis);
		final Ixyz add1 = AxisToValue(axis.charAt(0));
		final Ixyz add2 = AxisToValue(axis.charAt(1));
		final Ixyz add3 = AxisToValue(axis.charAt(2));
		final int size1 = matrix.length;
		int x = this.absX;
		int y = this.absY;
		int z = this.absZ;
		int xx, yy, zz;
		int xxx, yyy, zzz;
		int size2, size3;
		char c;
		for (int i=0; i<size1; i++) {
			xxx = x; yyy = y; zzz = z;
			size2 = matrix[i].length;
			for (int ii=0; ii<size2; ii++) {
				xx = x; yy = y; zz = z;
				size3 = matrix[i][ii].length();
				for (int iii=0; iii<size3; iii++) {
					c = matrix[i][ii].charAt(iii);
					if (c != 0 && c != ' ') {
						final Material m = this.types.get(Character.valueOf(c));
						if (m == null) throw new RuntimeException(String.format("Warning, unknown material: %c", c));
						final String s = this.special.get(Character.valueOf(c));
						this.setAbsBlock(x, y, z, m, s);
					}
					x += add3.x; y += add3.y; z += add3.z;
				}
				x = xx + add2.x; y = yy + add2.y; z = zz + add2.z;
			}
			x = xxx + add1.x; y = yyy + add1.y; z = zzz + add1.z;
		}
	}
	public void place2D(final String axis, final StringBuilder[] matrix) {
		if (Utils.isEmpty(axis)) throw new RequiredArgumentException("axis");
		if (axis.length() != 2) throw new RuntimeException("Invalid axis length: "+axis);
		final Ixyz add1 = AxisToValue(axis.charAt(0));
		final Ixyz add2 = AxisToValue(axis.charAt(1));
		final int size1 = matrix.length;
		int x = this.absX;
		int y = this.absY;
		int z = this.absZ;
		int xx, yy, zz;
		int size2;
		char c;
		for (int i=0; i<size1; i++) {
			xx = x; yy = y; zz = z;
			size2 = matrix[i].length();
			for (int ii=0; ii<size2; ii++) {
				c = matrix[i].charAt(ii);
				if (c != 0 && c != ' ') {
					final Material m = this.types.get(Character.valueOf(c));
					if (m == null) throw new RuntimeException(String.format("Warning, unknown material: %c", c));
					final String s = this.special.get(Character.valueOf(c));
					this.setAbsBlock(x, y, z, m, s);
				}
				x += add2.x; y += add2.y; z += add2.z;
			}
			x = xx + add1.x; y = yy + add1.y; z = zz + add1.z;
		}
	}
	public void place1D(final char axis, final StringBuilder matrix) {
		final Ixyz add = AxisToValue(axis);
		final int size = matrix.length();
		int x = this.absX;
		int y = this.absY;
		int z = this.absZ;
		char c;
		for (int i=0; i<size; i++) {
			c = matrix.charAt(i);
			if (c != 0 && c != ' ') {
				final Material m = this.types.get(Character.valueOf(c));
				if (m == null) throw new RuntimeException(String.format("Warning, unknown material: %c", c));
				final String s = this.special.get(Character.valueOf(c));
				this.setAbsBlock(x, y, z, m, s);
			}
			x += add.x; y += add.y; z += add.z;
		}
	}



	public void type(final char c, final Material m) {
		this.types.put(Character.valueOf(c), m);
	}
	public void type(final char c, final Material m, final String special) {
		this.type(c, m);
		this.special.put(Character.valueOf(c), special);
	}



	public void setRelBlock(final int x, final int y, final int z, final Material type, final String special) {
		this.setAbsBlock(x+this.absX, y+this.absY, z+this.absZ, type, special);
	}
	public void setRelBlock(final int x, final int y, final int z, final Material type) {
		this.setAbsBlock(x+this.absX, y+this.absY, z+this.absZ, type);
	}

	public BlockData getRelBlockData(final int x, final int y, final int z) {
		return this.getAbsBlockData(x+this.absX, y+this.absY, z+this.absZ);
	}
	public void setRelBlockData(final int x, final int y, final int z, final BlockData block) {
		this.setAbsBlockData(x+this.absX, y+this.absY, z+this.absZ, block);
	}



	public void setAbsBlock(final int x, final int y, final int z, final Material type, final String special) {
		this.setAbsBlock(x, y, z, type);
		if (Utils.notEmpty(special)) {
			boolean changed = false;
			final String sp = (new StringBuilder())
					.append(',').append(special).append(',')
					.toString();
			final BlockData data = this.getAbsBlockData(x, y, z);
			// slab
			if (data instanceof Slab) {
				if (sp.contains(",top,")) {
					changed = true; ((Slab)data).setType(Slab.Type.TOP);
				} else
				if (sp.contains(",bottom,")) {
					changed = true; ((Slab)data).setType(Slab.Type.BOTTOM);
				}
			}
			// lamp
			if (data instanceof Lightable) {
				if (sp.contains(",on,")) {
					changed = true; ((Lightable)data).setLit(true);
				} else
				if (sp.contains(",off,")) {
					changed = true; ((Lightable)data).setLit(false);
				}
			}
			if (changed) {
				this.setAbsBlockData(x, y, z, data);
			}
		}
	}
	public void setAbsBlock(final int x, final int y, final int z, final Material type) {
		if (this.world != null) {
			this.world.setType(x, y, z, type);
		} else
		if (this.chunk != null) {
			this.chunk.setBlock(Math.abs(x%16), y, Math.abs(z%16), type);
		} else
		if (this.region != null) {
			this.region.setType(x, y, z, type);
		}
	}

	public BlockData getAbsBlockData(final int x, final int y, final int z) {
		if (this.world != null) {
			return this.world.getBlockData(x, y, z);
		} else
		if (this.chunk != null) {
			return this.chunk.getBlockData(Math.abs(x%16), y, Math.abs(z%16));
		} else
		if (this.region != null) {
			return this.region.getBlockData(x, y, z);
		}
		return null;
	}
	public void setAbsBlockData(final int x, final int y, final int z, final BlockData block) {
		if (this.world != null) {
			this.world.setBlockData(x, y, z, block);
		} else
		if (this.chunk != null) {
			this.chunk.setBlock(Math.abs(x%16), y, Math.abs(z%16), block);
		} else
		if (this.region != null) {
			this.region.setBlockData(x, y, z, block);
		}
	}



}
