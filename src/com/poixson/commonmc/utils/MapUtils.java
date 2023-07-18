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



	public static void DrawImagePixels(final Color[][] pixels,
			final int x, final int y, final BufferedImage img) {
		DrawImagePixels(pixels, x, y, img, (BufferedImage)null);
	}
	public static void DrawImagePixels(final Color[][] pixels,
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
				if (mask != null && !Color.WHITE.equals(new Color(mask.getRGB(ix, iy))))
					continue LOOP_X;
				pixels[yy][xx] = new Color(img.getRGB(ix, iy));
			} // end LOOP_X
		} // end LOOP_Y
	}
	public static void DrawImagePixels(final Color[][] pixels,
			final int x, final int y, final BufferedImage img,
			final Color mask) {
		final int w = pixels[0].length - 1;
		final int h = pixels.length    - 1;
		final int img_w = img.getWidth(null);
		final int img_h = img.getHeight(null);
		int xx, yy;
		Color c;
		//LOOP_Y:
		for (int iy=0; iy<img_h; iy++) {
			yy = iy + y;
			LOOP_X:
			for (int ix=0; ix<img_w; ix++) {
				xx = ix + x;
				if (xx < 0 || xx > w
				||  yy < 0 || yy > h)
					continue LOOP_X;
				c = new Color(img.getRGB(ix, iy));
				if (!mask.equals(c))
					pixels[yy][xx] = c;
			} // end LOOP_X
		} // end LOOP_Y
	}



}
