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
			final BufferedImage img, final int x, final int y) {
		DrawImagePixels(pixels, img, null, x, y);
	}
	public static void DrawImagePixels(final Color[][] pixels,
			final BufferedImage img, final BufferedImage mask,
			final int x, final int y) {
		final int w = pixels[0].length - 1;
		final int h = pixels.length    - 1;
		final int img_w = img.getWidth(null);
		final int img_h = img.getHeight(null);
		int xx, yy;
		for (int iy=0; iy<img_h; iy++) {
			yy = iy + y;
			for (int ix=0; ix<img_w; ix++) {
				xx = ix + x;
				if (xx >= 0 && xx < w
				&&  yy >= 0 && yy < h) {
					if (mask == null
					|| Color.WHITE.equals(new Color(mask.getRGB(ix, iy))))
						pixels[iy+y][ix+x] = new Color(img.getRGB(ix, iy));
//TODO: remove this?
//System.out.println( img.getColorModel().hasAlpha() ? "YES" : "NO" );
//					if (!Color.BLACK.equals(color))
//					if (color.getTransparency() == Transparency.OPAQUE)
//System.out.println(color.getAlpha());
//					if (color.getAlpha() >= 255)
				}
			}
		}
	}



}
