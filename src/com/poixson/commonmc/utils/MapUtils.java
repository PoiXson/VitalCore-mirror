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
	@SuppressWarnings("deprecation")
	public static int NearestMapColor(final int color) {
		return MapPalette.getColor(MapPalette.matchColor(new Color(color))).getRGB();
	}



	public static void DrawImagePixels(final Color[][] pixels,
			final int x, final int y, final BufferedImage img) {
		DrawImagePixels_ImgMask(pixels, x, y, img, null);
	}
	public static void DrawImagePixels_ImgMask(final Color[][] pixels,
			final int x, final int y, final BufferedImage img,
			final BufferedImage mask) {
		final int w = pixels[0].length - 1;
		final int h = pixels.length    - 1;
		final int img_w = img.getWidth(null);
		final int img_h = img.getHeight(null);
		final int color_white = Color.WHITE.getRGB();
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
				if (mask != null && mask.getRGB(ix, iy) != color_white)
					continue LOOP_X;
				pixels[yy][xx] = new Color(img.getRGB(ix, iy));
			} // end LOOP_X
		} // end LOOP_Y
	}
	public static void DrawImagePixels_ColorMask(final Color[][] pixels,
			final int x, final int y, final BufferedImage img,
			final Color mask) {
		final int w = pixels[0].length - 1;
		final int h = pixels.length    - 1;
		final int img_w = img.getWidth(null);
		final int img_h = img.getHeight(null);
		final int color_mask = mask.getRGB();
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
				if (c != color_mask)
					pixels[yy][xx] = new Color(c);
			} // end LOOP_X
		} // end LOOP_Y
	}



}
