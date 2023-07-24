package com.poixson.commonmc.utils;

import java.awt.Color;
import java.awt.image.BufferedImage;

import org.bukkit.map.MapPalette;


public final class MapUtils {
	private MapUtils() {}



	@SuppressWarnings("deprecation")
	public static Color NearestMapColor(final Color color) {
		return MapPalette.getColor(MapPalette.matchColor(color));
	}



	public static void DrawImagePixels(final int[][] pixels,
			final int x, final int y, final BufferedImage img) {
		DrawImagePixels(pixels, x, y, img, (BufferedImage)null);
	}
	public static void DrawImagePixels(final int[][] pixels,
			final int x, final int y, final BufferedImage img,
			final BufferedImage mask) {
		final int w = pixels[0].length - 1;
		final int h = pixels.length    - 1;
		final int img_w = img.getWidth(null);
		final int img_h = img.getHeight(null);
		int xx, yy;
		//LOOP_Y:
		for (int iy=0; iy<img_h; iy++) {
			yy = iy + y;
			LOOP_X:
			for (int ix=0; ix<img_w; ix++) {
				xx = ix + x;
				if (xx < 0 || xx > w
				||  yy < 0 || yy > h)
					continue LOOP_X;
				if (mask != null && Color.WHITE.getRGB() != mask.getRGB(ix, iy))
					continue LOOP_X;
				pixels[yy][xx] = img.getRGB(ix, iy);
			} // end LOOP_X
		} // end LOOP_Y
	}
	public static void DrawImagePixels(final int[][] pixels,
			final int x, final int y, final BufferedImage img,
			final int mask) {
		final int w = pixels[0].length - 1;
		final int h = pixels.length    - 1;
		final int img_w = img.getWidth(null);
		final int img_h = img.getHeight(null);
		int xx, yy;
		int c;
		//LOOP_Y:
		for (int iy=0; iy<img_h; iy++) {
			yy = iy + y;
			LOOP_X:
			for (int ix=0; ix<img_w; ix++) {
				xx = ix + x;
				if (xx < 0 || xx > w
				||  yy < 0 || yy > h)
					continue LOOP_X;
				c = img.getRGB(ix, iy);
				if (c != mask)
					pixels[yy][xx] = c;
			} // end LOOP_X
		} // end LOOP_Y
	}



}
