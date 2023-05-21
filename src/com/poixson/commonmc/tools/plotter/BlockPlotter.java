package com.poixson.commonmc.tools.plotter;

import static com.poixson.commonmc.utils.LocationUtils.AxToIxyz;
import static com.poixson.commonmc.utils.LocationUtils.Rotate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;

import com.poixson.commonmc.utils.BlockUtils;
import com.poixson.tools.dao.Iabc;
import com.poixson.tools.dao.Iabcd;
import com.poixson.utils.Utils;


public class BlockPlotter implements Runnable {

	public final BlockPlacer placer;

	public final BlockMatrix matrix;

	// cached values
	public String axis;
	public int x, y, z;
	public int w, h, d;

	public BlockFace rotation = BlockFace.SOUTH;

	protected final Map<Character, Material>    types   = new HashMap<Character, Material>();
	protected final Map<Character, Set<String>> special = new HashMap<Character, Set<String>>();
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
			Material type;
			Set<String> specials;
			for (int i=0; i<len; i++) {
				chr = row.charAt(i);
				if (chr != 0 && chr != ' ') {
					xx = (add.a * i) + x;
					yy = (add.b * i) + y;
					zz = (add.c * i) + z;
					type = this.types.get(Character.valueOf(chr));
					if (type == null) throw new RuntimeException("Unknown material: " + Character.toString(chr));
					specials = this.special.get(Character.valueOf(chr));
					if (specials != null)
						if (specials.contains("autoface"))
							this.autoface.add(new Iabc(xx, yy, zz));
					this.setType(xx, yy, zz, type, specials);
				}
			}
		}
	}



	// -------------------------------------------------------------------------------
	// set/get block



	public void setType(final int x, final int y, final int z, final Material type, final Set<String> special) {
		this.setType(x, y, z, type);
		this.setSpecial(x, y, z, special);
	}
	public void setType(final int x, final int y, final int z, final Material type) {
		final Iabcd loc = Rotate(new Iabcd(x, z, this.w, this.d), this.rotation);
		final int xx = this.x + loc.a;
		final int zz = this.z + loc.b;
		final int yy = this.y + y;
		this.placer.setType(xx, yy, zz, type);
	}
	public void setSpecial(final int x, final int y, final int z, final Set<String> special) {
		if (Utils.isEmpty(special)) return;
		final BlockData data = this.getBlockData(x, y, z);
		if (BlockUtils.ApplyBlockSpecial(data, special))
			this.setBlockData(x, y, z, data);
	}

	public Material getType(final int x, final int y, final int z) {
		final Iabcd loc = Rotate(new Iabcd(x, z, this.w, this.d), this.rotation);
		final int xx = this.x + loc.a;
		final int zz = this.z + loc.b;
		final int yy = this.y + y;
		return this.placer.getType(xx, yy, zz);
	}
	public boolean isType(final int x, final int y, final int z, final Material match) {
		if (match == null) return false;
		final Material existing = this.getType(x, y, z);
		return match.equals(existing);
	}



	public BlockData getBlockData(final int x, final int y, final int z) {
		final Iabcd loc = Rotate(new Iabcd(x, z, this.w, this.d), this.rotation);
		final int xx = this.x + loc.a;
		final int zz = this.z + loc.b;
		final int yy = this.y + y;
		return this.placer.getBlockData(xx, yy, zz);
	}
	public void setBlockData(final int x, final int y, final int z, final BlockData data) {
		final Iabcd loc = Rotate(new Iabcd(x, z, this.w, this.d), this.rotation);
		final int xx = this.x + loc.a;
		final int zz = this.z + loc.b;
		final int yy = this.y + y;
		this.placer.setBlockData(xx, yy, zz, data);
	}



	public BlockPlotter type(final char chr, final Material type, final String...special) {
		return this.type(chr, type)
				.special(chr, special);
	}
	public BlockPlotter type(final char chr, final Material type) {
		this.types.put(Character.valueOf(chr), type);
		return this;
	}

	public BlockPlotter special(final char chr, final String...special) {
		final Set<String> set = this.getSpecialSet(chr);
		for (final String sp : special)
			set.add(sp);
		return this;
	}

	public Set<String> getSpecialSet(final char chr) {
		// existing
		{
			final Set<String> set = this.special.get(Character.valueOf(chr));
			if (set != null)
				return set;
		}
		// new set
		{
			final Set<String> set = new HashSet<String>();
			this.special.put(Character.valueOf(chr), set);
			return set;
		}
	}



}
