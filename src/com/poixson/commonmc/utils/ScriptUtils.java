package com.poixson.commonmc.utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapPalette;
import org.bukkit.util.Vector;

import com.poixson.tools.dao.Iab;
import com.poixson.tools.dao.Iabcd;


public final class ScriptUtils {
	private ScriptUtils() {}



	public static Map<String, Object> PlayerToHashMap(final Player player) {
		final Map<String, Object> map = new ConcurrentHashMap<String, Object>();
		final Location loc = player.getLocation();
		map.put("name", player.getName());
		map.put("uuid", player.getUniqueId().toString());
		map.put("x", Double.valueOf(loc.getX()));
		map.put("y", Double.valueOf(loc.getY()));
		map.put("z", Double.valueOf(loc.getZ()));
		map.put("yaw",   Float.valueOf(loc.getYaw()));
		map.put("pitch", Float.valueOf(loc.getPitch()));
		return map;
	}



	// -------------------------------------------------------------------------------
	// map scale
	public static Iab FixCursorPosition(final Vector vec, final int map_size,



	@SuppressWarnings("deprecation")
	public static void SetMapID(final ItemStack map, final int id) {
		final MapMeta meta = (MapMeta) map.getItemMeta();
		meta.setMapId(id);
		map.setItemMeta(meta);
	}
			final Iabcd screen_size, final BlockFace facing) {
		final int y = ((int)Math.round( map_size * (1.0-(vec.getY()%1.0)) )) - screen_size.b;
		final double vec_x;
		FACING_SWITCH:
		switch (facing) {
		case NORTH: case SOUTH: vec_x = vec.getX(); break FACING_SWITCH;
		case EAST:  case WEST:  vec_x = vec.getZ(); break FACING_SWITCH;
		default: throw new RuntimeException("Unknown cursor direction: " + facing.toString());
		}
		int x = ((int)Math.round( map_size * (vec_x%1.0) )) - screen_size.a;
		// reverse direction
		FACING_SWITCH:
		switch (facing) {
		case NORTH: case EAST: x = screen_size.c - x; break FACING_SWITCH;
		case SOUTH: case WEST: break FACING_SWITCH;
		default: throw new RuntimeException("Unknown cursor direction: " + facing.toString());
		}
		return new Iab(x, y);
	}
	public static Iab FixClickPosition(final Vector vec, final int map_size,
			final Iabcd screen_size, final BlockFace facing, final Location player_loc) {
		int y = ((int)Math.round( map_size * (0.5-(vec.getY()%1.0)) )) - screen_size.b - 1;
		final double vec_x;
		FACING_SWITCH:
		switch (facing) {
		case NORTH: case SOUTH: vec_x = vec.getX() + 0.5; break FACING_SWITCH;
		case EAST:  case WEST:  vec_x = vec.getZ() + 0.5; break FACING_SWITCH;
		default: throw new RuntimeException("Unknown click direction: " + facing.toString());
		}
		int x = ((int)Math.round( map_size * (vec_x%1.0) )) - screen_size.a;
		// reverse direction
		FACING_SWITCH:
		switch (facing) {
		case NORTH: case EAST: x = screen_size.c - x; break FACING_SWITCH;
		case SOUTH: case WEST: break FACING_SWITCH;
		default: throw new RuntimeException("Unknown click direction: " + facing.toString());
		}
		// correct for angle and map distance from wall
		{
			final double angle_x = 45.0 - ((player_loc.getYaw()   + 225.0) % 90.0);
			final double angle_y = 45.0 - ((player_loc.getPitch() + 225.0) % 90.0);
			x += (int)Math.round(0.0 - (Math.tan(angle_x / 45.0) * (0.03125 * map_size)));
			y += (int)Math.round(0.0 - (Math.tan(angle_y / 45.0) * (0.03125 * map_size)));
		}
		return new Iab(x, y);
	}



	@SuppressWarnings("deprecation")
	public static Color NearestMapColor(final Color color) {
		return MapPalette.getColor(MapPalette.matchColor(color));
	}
	public static int NearestMapColor(final int color) {
		return NearestMapColor(new Color(color)).getRGB();
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
				if (mask != null
				&&  mask.getRGB(ix, iy) != color_white)
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
