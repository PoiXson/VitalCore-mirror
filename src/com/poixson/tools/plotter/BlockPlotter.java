package com.poixson.pluginlib.tools.plotter;

import static com.poixson.pluginlib.utils.LocationUtils.AxToIxyz;
import static com.poixson.pluginlib.utils.LocationUtils.Rotate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;

import com.poixson.tools.dao.Iabc;
import com.poixson.tools.dao.Iabcd;


public class BlockPlotter implements Runnable {

	public final BlockPlacer placer;

	public final BlockMatrix matrix;

	// cached values
	public String axis;
	public int x, y, z;
	public int w, h, d;

	public BlockFace rotation = BlockFace.SOUTH;

	protected final Map<Character, BlockData> types = new HashMap<Character, BlockData>();
//TODO
	protected final Set<Iabc> autoface = new HashSet<Iabc>();



	public BlockPlotter(final BlockPlacer placer,
			final String axis, final int[] locs, final int[] sizes) {
		this.placer = placer;
		this.matrix = new BlockMatrix(axis, locs, sizes);
		this.axis = axis;
		this.x = this.matrix.getX(); this.y = this.matrix.getY(); this.z = this.matrix.getZ();
		this.w = this.matrix.getW(); this.h = this.matrix.getH(); this.d = this.matrix.getD();
	}
	public BlockPlotter(final BlockPlacer placer, final BlockMatrix matrix) {
		this.placer = placer;
		this.matrix = matrix;
		this.axis = matrix.getAxis();
		this.x = matrix.getX(); this.y = matrix.getY(); this.z = matrix.getZ();
		this.w = matrix.getW(); this.h = matrix.getH(); this.d = matrix.getD();
	}



	// -------------------------------------------------------------------------------
	// matrix



	public BlockMatrix getMatrix() {
		return this.matrix;
	}



	public StringBuilder[][] getMatrix3D() {
		final BlockMatrix matrix = this.getMatrix();
		final LinkedList<StringBuilder[]> list = new LinkedList<StringBuilder[]>();
		for (BlockMatrix mtx : matrix.array) {
			final LinkedList<StringBuilder> list2 = new LinkedList<StringBuilder>();
			for (BlockMatrix mx : mtx.array)
				list2.add(mx.row);
			list.add(list2.toArray(new StringBuilder[0]));
		}
		return list.toArray(new StringBuilder[0][0]);
	}

	public StringBuilder[] getMatrix2D() {
		final BlockMatrix matrix = this.getMatrix();
		final LinkedList<StringBuilder> list = new LinkedList<StringBuilder>();
		for (final BlockMatrix mtx : matrix.array)
			list.add(mtx.row);
		return list.toArray(new StringBuilder[0]);
	}

	public StringBuilder getMatrix1D() {
		final BlockMatrix matrix = this.getMatrix();
		return matrix.row;
	}



	// -------------------------------------------------------------------------------
	// place blocks



	@Override
	public void run() {
		run(this.matrix, 0, 0, 0);
	}
	protected void run(final BlockMatrix matrix,
			final int x, final int y, final int z) {
		int xx, yy, zz;
		final Iabc add = AxToIxyz(matrix.ax);
		// more dimensions
		if (matrix.row == null) {
			final int len = matrix.array.length;
			for (int i=0; i<len; i++) {
				xx = (add.a * i) + x;
				yy = (add.b * i) + y;
				zz = (add.c * i) + z;
				this.run(matrix.array[i], xx, yy, zz);
			}
		// last dimension
		} else {
			final String row = matrix.row.toString();
			final int len = row.length();
			char chr;
			BlockData type;
			for (int i=0; i<len; i++) {
				chr = row.charAt(i);
				if (chr != 0 && chr != ' ') {
					xx = (add.a * i) + x;
					yy = (add.b * i) + y;
					zz = (add.c * i) + z;
					type = this.types.get(Character.valueOf(chr));
					if (type == null) throw new RuntimeException("Unknown material: " + Character.toString(chr));
//TODO
//					specials = this.special.get(Character.valueOf(chr));
//					if (specials != null)
//						if (specials.contains("autoface"))
//							this.autoface.add(new Iabc(xx, yy, zz));
//					this.setType(xx, yy, zz, type, specials);
					this.setBlock(xx, yy, zz, type);
				}
			}
		}
	}



	// -------------------------------------------------------------------------------
	// set/get block



	public BlockPlotter type(final char chr, final String type) {
		return this.type(chr, Bukkit.createBlockData(type));
	}
	public BlockPlotter type(final char chr, final Material type) {
		return this.type(chr, Bukkit.createBlockData(type));
	}
	public BlockPlotter type(final char chr, final BlockData block) {
		this.types.put(Character.valueOf(chr), block);
		return this;
	}



	public void setBlock(final int x, final int y, final int z, final BlockData type) {
		final Iabcd loc = Rotate(new Iabcd(x, z, this.w, this.d), this.rotation);
		final int xx = this.x + loc.a;
		final int zz = this.z + loc.b;
		final int yy = this.y + y;
		this.placer.setBlock(xx, yy, zz, type);
	}
	public BlockData getBlock(final int x, final int y, final int z) {
		final Iabcd loc = Rotate(new Iabcd(x, z, this.w, this.d), this.rotation);
		final int xx = this.x + loc.a;
		final int zz = this.z + loc.b;
		final int yy = this.y + y;
		return this.placer.getBlock(xx, yy, zz);
	}



}
