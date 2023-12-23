package com.poixson.tools.plotter;

import java.util.Arrays;


public class BlockMatrix {

	public final BlockMatrix[] array;
	public final StringBuilder row;

	public final char ax;
	public final int  loc;
	public final int  size;



	public BlockMatrix(final String axis, final int[] locs, final int[] sizes) {
		if (axis.length() != locs.length ) throw new RuntimeException("axis length doesn't match locs length");
		if (axis.length() != sizes.length) throw new RuntimeException("axis length doesn't match sizes length");
		this.loc  = locs[0];
		this.size = sizes[0];
		// last dimension
		if (sizes.length == 1) {
			this.array = null;
			this.row = new StringBuilder();
			this.ax  = axis.charAt(0);
		} else {
			this.row = null;
			final int[] locsNew  = Arrays.copyOfRange(locs,   1, locs.length );
			final int[] sizesNew = Arrays.copyOfRange(sizes,  1, sizes.length);
			this.array = new BlockMatrix[this.size];
			this.array[0] = new BlockMatrix(axis.substring(1), locsNew, sizesNew);
			for (int i=1; i<this.size; i++)
				this.array[i] = this.array[0].clone();
			this.ax = axis.charAt(0);
		}
	}
	public BlockMatrix(final BlockMatrix[] array,
			final char ax, final int loc, final int size) {
		this.array = array;
		this.row   = null;
		this.ax    = ax;
		this.loc   = loc;
		this.size  = array.length;
	}
	public BlockMatrix(final StringBuilder row,
			final char ax, final int loc, final int size) {
		this.array = null;
		this.row   = row;
		this.ax    = ax;
		this.loc   = loc;
		this.size  = size;
	}

	@Override
	public BlockMatrix clone() {
		if (this.row == null) {
			final BlockMatrix[] array = new BlockMatrix[this.size];
			for (int i=0; i<this.array.length; i++)
				array[i] = this.array[i].clone();
			final BlockMatrix matrix = new BlockMatrix(array, this.ax, this.loc, this.size);
			return matrix;
		} else {
			final BlockMatrix matrix = new BlockMatrix(new StringBuilder(), this.ax, this.loc, this.size);
			matrix.row.append(this.row.toString());
			return matrix;
		}
	}



	public int getDimensions() {
		BlockMatrix matrix = this;
		for (int i=0; i<3; i++) {
			if (matrix.row != null)
				return i + 1;
			matrix = matrix.array[0];
		}
		return 0;
	}
	public String getAxis() {
		final int dims = this.getDimensions();
		final StringBuilder axis = new StringBuilder();
		BlockMatrix matrix = this;
		for (int i=0; i<dims; i++) {
			axis.append(matrix.ax);
			if (matrix.row != null)
				break;
			matrix = matrix.array[0];
		}
		return axis.toString();
	}
	public int[] getLocsArray() {
		final int dims = this.getDimensions();
		final int[] locs = new int[dims];
		BlockMatrix matrix = this;
		for (int i=0; i<dims; i++) {
			locs[i] = matrix.loc;
			if (matrix.row != null)
				break;
			matrix = matrix.array[0];
		}
		return locs;
	}
	public int[] getSizesArray() {
		final int dims = this.getDimensions();
		final int[] sizes = new int[dims];
		BlockMatrix matrix = this;
		for (int i=0; i<dims; i++) {
			sizes[i] = matrix.size;
			if (matrix.row != null)
				break;
			matrix = matrix.array[0];
		}
		return sizes;
	}

	public int getX() {
		final int dims = this.getDimensions();
		BlockMatrix matrix = this;
		for (int i=0; i<dims; i++) {
			switch (matrix.ax) {
			case 'e': case 'X':
			case 'w': case 'x': return matrix.loc;
			}
			if (matrix.row != null)
				break;
			matrix = matrix.array[0];
		}
		return Integer.MIN_VALUE;
	}
	public int getY() {
		final int dims = this.getDimensions();
		BlockMatrix matrix = this;
		for (int i=0; i<dims; i++) {
			switch (matrix.ax) {
			case 'u': case 'Y':
			case 'd': case 'y': return matrix.loc;
			}
			if (matrix.row != null)
				break;
			matrix = matrix.array[0];
		}
		return Integer.MIN_VALUE;
	}
	public int getZ() {
		final int dims = this.getDimensions();
		BlockMatrix matrix = this;
		for (int i=0; i<dims; i++) {
			switch (matrix.ax) {
			case 'n': case 'z':
			case 's': case 'Z': return matrix.loc;
			}
			if (matrix.row != null)
				break;
			matrix = matrix.array[0];
		}
		return Integer.MIN_VALUE;
	}

	public int getW() {
		final int dims = this.getDimensions();
		BlockMatrix matrix = this;
		for (int i=0; i<dims; i++) {
			switch (matrix.ax) {
			case 'e': case 'X':
			case 'w': case 'x': return matrix.size;
			}
			if (matrix.row != null)
				break;
			matrix = matrix.array[0];
		}
		return Integer.MIN_VALUE;
	}
	public int getH() {
		final int dims = this.getDimensions();
		BlockMatrix matrix = this;
		for (int i=0; i<dims; i++) {
			switch (matrix.ax) {
			case 'u': case 'Y':
			case 'd': case 'y': return matrix.size;
			}
			if (matrix.row != null)
				break;
			matrix = matrix.array[0];
		}
		return Integer.MIN_VALUE;
	}
	public int getD() {
		final int dims = this.getDimensions();
		BlockMatrix matrix = this;
		for (int i=0; i<dims; i++) {
			switch (matrix.ax) {
			case 'n': case 'z':
			case 's': case 'Z': return matrix.size;
			}
			if (matrix.row != null)
				break;
			matrix = matrix.array[0];
		}
		return Integer.MIN_VALUE;
	}



	public char get(final int...pos) {
		if (this.row == null) {
			return this.array[pos[0]].get(Arrays.copyOfRange(pos, 1, pos.length));
		} else {
			return this.row.charAt(pos[0]);
		}
	}
	public String getRow(final int...pos) {
		if (this.row == null) {
			return this.array[pos[0]].getRow(Arrays.copyOfRange(pos, 1, pos.length));
		} else {
			return this.row.toString();
		}
	}

	public void set(final char chr, final int...pos) {
		if (this.row == null) {
			this.array[pos[0]].set(chr, Arrays.copyOfRange(pos, 1, pos.length));
		} else {
			if (pos[0] >= this.row.length()) {
				while (pos[0] > this.row.length())
					this.row.append(' ');
				this.row.append(chr);
			} else {
				this.row.setCharAt(pos[0], chr);
			}
		}
	}



	public void append(final char chr, final int...pos) {
		if (this.row == null) {
			this.array[pos[0]].append(chr, Arrays.copyOfRange(pos, 1, pos.length));
		} else {
			this.row.append(chr);
		}
	}
	public void append(final String str, final int...pos) {
		if (this.row == null) {
			this.array[pos[0]].append(str, Arrays.copyOfRange(pos, 1, pos.length));
		} else {
			this.row.append(str);
		}
	}



	public void fill() {
		this.fill(' ');
	}
	public void fill(final char chr) {
		if (this.row == null) {
			for (final BlockMatrix matrix : this.array)
				matrix.fill(chr);
		} else {
			while (this.size > this.row.length())
				this.row.append(chr);
		}
	}



}
