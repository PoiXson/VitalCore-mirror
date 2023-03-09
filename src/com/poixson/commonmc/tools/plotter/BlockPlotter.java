package com.poixson.commonmc.tools.plotter;

import static com.poixson.commonmc.utils.LocationUtils.AxToIxyz;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator.ChunkData;
import org.bukkit.generator.LimitedRegion;

import com.poixson.tools.dao.Iabc;
import com.poixson.utils.Utils;


public class BlockPlotter extends BlockPlacer implements Runnable {

	public final BlockMatrix matrix;
	public String axis = "";

	protected final Map<Character, Material>    types   = new HashMap<Character, Material>();
	protected final Map<Character, Set<String>> special = new HashMap<Character, Set<String>>();

	protected final Set<Iabc> autoface = new HashSet<Iabc>();



	public BlockPlotter(final World world, final int...sizes) {
		this(world, new BlockMatrix(sizes));
	}
	public BlockPlotter(final ChunkData chunk, final int...sizes) {
		this(chunk, new BlockMatrix(sizes));
	}
	public BlockPlotter(final LimitedRegion region, final int...sizes) {
		this(region, new BlockMatrix(sizes));
	}

	public BlockPlotter(final World world, final BlockMatrix matrix) {
		super(world);
		this.matrix = matrix;
	}
	public BlockPlotter(final ChunkData chunk, final BlockMatrix matrix) {
		super(chunk);
		this.matrix = matrix;
	}
	public BlockPlotter(final LimitedRegion region, final BlockMatrix matrix) {
		super(region);
		this.matrix = matrix;
	}



	public StringBuilder[][] getMatrix3D() {
		final LinkedList<StringBuilder[]> list = new LinkedList<StringBuilder[]>();
		for (BlockMatrix matrix : this.matrix.array) {
			final LinkedList<StringBuilder> list2 = new LinkedList<StringBuilder>();
			for (BlockMatrix mx : matrix.array)
				list2.add(mx.row);
			list.add(list2.toArray(new StringBuilder[0]));
		}
		return list.toArray(new StringBuilder[0][0]);
	}
	public StringBuilder[] getMatrix2D() {
		final LinkedList<StringBuilder> list = new LinkedList<StringBuilder>();
		for (final BlockMatrix matrix : this.matrix.array)
			list.add(matrix.row);
		return list.toArray(new StringBuilder[0]);
	}
	public StringBuilder getMatrix1D() {
		return this.matrix.row;
	}



	public BlockPlotter location(final int...locs) {
		this.x = locs[0];
		if (locs.length == 2) {
			this.y = 0;
			this.z = locs[1];
		} else
		if (locs.length == 3) {
			this.y = locs[1];
			this.z = locs[2];
		}
		return this;
	}
	public BlockPlotter size(final int...sizes) {
		this.w = sizes[0];
		if (sizes.length == 2) {
			this.h = 0;
			this.d = sizes[1];
		} else
		if (sizes.length == 3) {
			this.h = sizes[1];
			this.d = sizes[2];
		}
		return this;
	}
	public BlockPlotter axis(final String axis) {
		this.axis = axis;
		return this;
	}



	public int getDimensions() {
		return this.matrix.getDimensions();
	}



	// -------------------------------------------------------------------------------
	// place blocks



	@Override
	public void run() {
		if (Utils.isEmpty(this.axis)) {
			switch (this.getDimensions()) {
			case 1: this.axis = "e";   break;
			case 2: this.axis = "ue";  break;
			case 3: this.axis = "use"; break;
			default: throw new RuntimeException("Axis not set for block plotter");
			}
		}
		run(this.axis, this.matrix, 0, 0, 0);
	}

	protected void run(final String axis, final BlockMatrix matrix,
			final int x, final int y, final int z) {
		int xx, yy, zz;
		final Iabc add = AxToIxyz(axis.charAt(0));
		// more dimensions
		if (matrix.row == null) {
			final String ax = axis.substring(1);
			final int len = matrix.array.length;
			for (int i=0; i<len; i++) {
				xx = (add.a * i) + x;
				yy = (add.b * i) + y;
				zz = (add.c * i) + z;
				this.run(ax, matrix.array[i], xx, yy, zz);
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



	public void doAutoFace() {
//TODO
	}



	// -------------------------------------------------------------------------------
	// block types



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
