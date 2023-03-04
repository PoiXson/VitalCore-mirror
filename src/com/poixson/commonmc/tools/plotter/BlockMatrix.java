package com.poixson.commonmc.tools.plotter;

import java.util.Arrays;


public class BlockMatrix {

	public BlockMatrix[] array;
	public StringBuilder row;
	public int size;



	public BlockMatrix(final int...sizes) {
		this.size = sizes[0];
		if (sizes.length == 1) {
			this.array = null;
			this.row = new StringBuilder();
		} else {
			final int[] siz = Arrays.copyOfRange(sizes, 1, sizes.length);
			this.array = new BlockMatrix[this.size];
			this.array[0] = new BlockMatrix(siz);
			for (int i=1; i<this.size; i++)
				this.array[i] = this.array[0].clone();
			this.row = null;
		}
	}

	public BlockMatrix(final BlockMatrix[] array) {
		this.array = array;
		this.row   = null;
	}
	public BlockMatrix(final StringBuilder row) {
		this.array = null;
		this.row   = row;
	}

	@Override
	public BlockMatrix clone() {
		if (this.row == null) {
			final BlockMatrix[] array = new BlockMatrix[this.size];
			for (int i=0; i<this.array.length; i++)
				array[i] = this.array[i].clone();
			final BlockMatrix matrix = new BlockMatrix(array);
			matrix.size = this.size;
			return matrix;
		} else {
			final BlockMatrix matrix = new BlockMatrix(new StringBuilder());
			matrix.size = this.size;
			matrix.row.append(this.row.toString());
			return matrix;
		}
	}



	public int getDimensions() {
		BlockMatrix matrix = this;
		for (int i=0; i<3; i++) {
			if (matrix.row != null)
				return i;
			matrix = matrix.array[0];
		}
		return 0;
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
