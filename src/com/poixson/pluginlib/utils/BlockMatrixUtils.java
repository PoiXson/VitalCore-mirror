package com.poixson.pluginlib.utils;


public final class BlockMatrixUtils {
	private BlockMatrixUtils() {}



	public static int[] LocsToArray(final String axis,
			final int x, final int y, final int z) {
		final int dims = axis.length();
		final int[] locs = new int[dims];
		char ax;
		for (int i=0; i<dims; i++) {
			ax = axis.charAt(i);
			switch (ax) {
			case 'u': case 'Y':
			case 'd': case 'y': locs[i] = y; break;
			case 'n': case 'z':
			case 's': case 'Z': locs[i] = z; break;
			case 'e': case 'X':
			case 'w': case 'x': locs[i] = x; break;
			default: throw new RuntimeException("Unknown axis: "+Character.toString(ax));
			}
		}
		return locs;
	}
	public static int[] SizesToArray(final String axis,
			final int w, final int h, final int d) {
		final int dims = axis.length();
		final int[] sizes = new int[dims];
		char ax;
		for (int i=0; i<dims; i++) {
			ax = axis.charAt(i);
			switch (ax) {
			case 'u': case 'Y':
			case 'd': case 'y': sizes[i] = h; break;
			case 'n': case 'z':
			case 's': case 'Z': sizes[i] = d; break;
			case 'e': case 'X':
			case 'w': case 'x': sizes[i] = w; break;
			default: throw new RuntimeException("Unknown axis: "+Character.toString(ax));
			}
		}
		return sizes;
	}



}
