package com.poixson.commonmc.tools.plotter;

import static com.poixson.commonmc.utils.LocationUtils.AxToIxyz;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.ChunkGenerator.ChunkData;
import org.bukkit.generator.LimitedRegion;

import com.poixson.tools.dao.Iabc;
import com.poixson.utils.Utils;


public class BlockPlotter extends BlockPlacer implements Runnable {

	public BlockMatrix matrix = null;
	public String axis = "";
	public int[] sizes;

	protected final Map<Character, Material>    types   = new HashMap<Character, Material>();
	protected final Map<Character, Set<String>> special = new HashMap<Character, Set<String>>();

	protected final Set<Iabc> autoface = new HashSet<Iabc>();



	public BlockPlotter(final World                 world    ) { super(world    ); }
	public BlockPlotter(final ChunkData             chunk    ) { super(chunk    ); }
	public BlockPlotter(final LimitedRegion         region   ) { super(region   ); }
	public BlockPlotter(final BlockPlacer_WorldEdit worldedit) { super(worldedit); }



	public BlockMatrix getMatrix() {
		if (this.matrix == null)
			this.matrix = new BlockMatrix(this.sizes);
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



	public BlockPlotter location(final int...locs) {
		char chr;
		final int len = this.axis.length();
		for (int i=0; i<len; i++) {
			chr = this.axis.charAt(i);
			switch (chr) {
			case 'u': case 'Y': this.y = locs[i]; break;
			case 'd': case 'y': this.y = locs[i]; break;
			case 'n': case 'z': this.z = locs[i]; break;
			case 's': case 'Z': this.z = locs[i]; break;
			case 'e': case 'X': this.x = locs[i]; break;
			case 'w': case 'x': this.x = locs[i]; break;
			default: throw new RuntimeException("Unknown axis: "+Character.toString(chr));
			}
		}
		return this;
	}
	public BlockPlotter size(final int...sizes) {
		if (this.sizes == null)
			this.sizes = new int[sizes.length];
		this.sizes = sizes;
		char chr;
		final int len = this.axis.length();
		for (int i=0; i<len; i++) {
			chr = this.axis.charAt(i);
			switch (chr) {
			case 'u': case 'Y': this.h = sizes[i]; break;
			case 'd': case 'y': this.h = sizes[i]; break;
			case 'n': case 'z': this.d = sizes[i]; break;
			case 's': case 'Z': this.d = sizes[i]; break;
			case 'e': case 'X': this.w = sizes[i]; break;
			case 'w': case 'x': this.w = sizes[i]; break;
			default: throw new RuntimeException("Unknown axis: "+Character.toString(chr));
			}
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



	// -------------------------------------------------------------------------------
	// location/size



	@Override public BlockPlotter x(final int x) { this.x = x; return this; }
	@Override public BlockPlotter y(final int y) { this.y = y; return this; }
	@Override public BlockPlotter z(final int z) { this.z = z; return this; }

	@Override
	public BlockPlotter w(final int w) {
		this.w = w;
		char chr;
		final int len = this.axis.length();
		if (this.sizes == null)
			this.sizes = new int[len];
		for (int i=0; i<len; i++) {
			chr = this.axis.charAt(i);
			switch (chr) {
			case 'e': case 'X':
			case 'w': case 'x': this.sizes[i] = w; return this;
			case 'u': case 'Y':
			case 'd': case 'y':
			case 'n': case 'z':
			case 's': case 'Z': continue;
			default: throw new RuntimeException("Unknown axis: "+Character.toString(chr));
			}
		}
		return this;
	}
	@Override
	public BlockPlotter h(final int h) {
		this.h = h;
		char chr;
		final int len = this.axis.length();
		if (this.sizes == null)
			this.sizes = new int[len];
		for (int i=0; i<len; i++) {
			chr = this.axis.charAt(i);
			switch (chr) {
			case 'u': case 'Y':
			case 'd': case 'y': this.sizes[i] = h; return this;
			case 'n': case 'z':
			case 's': case 'Z':
			case 'e': case 'X':
			case 'w': case 'x': continue;
			default: throw new RuntimeException("Unknown axis: "+Character.toString(chr));
			}
		}
		return this;
	}
	@Override
	public BlockPlotter d(final int d) {
		this.d = d;
		char chr;
		final int len = this.axis.length();
		if (this.sizes == null)
			this.sizes = new int[len];
		for (int i=0; i<len; i++) {
			chr = this.axis.charAt(i);
			switch (chr) {
			case 'n': case 'z':
			case 's': case 'Z': this.sizes[i] = d; return this;
			case 'u': case 'Y':
			case 'd': case 'y':
			case 'e': case 'X':
			case 'w': case 'x': continue;
			default: throw new RuntimeException("Unknown axis: "+Character.toString(chr));
			}
		}
		return this;
	}

	@Override
	public BlockPlotter wrap(final boolean wrap) {
		this.wrap = wrap;
		return this;
	}

	@Override
	public BlockPlotter rotate(final BlockFace rotate) {
		this.rotation = rotate;
		return this;
	}



}
