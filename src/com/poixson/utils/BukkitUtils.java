package com.poixson.utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapView;
import org.bukkit.potion.PotionEffect;

import com.poixson.tools.Keeper;


public final class BukkitUtils {
	private BukkitUtils() {}
	static { Keeper.add(new BukkitUtils()); }



	public static boolean EqualsUUID(final UUID uuidA, final UUID uuidB) {
		return Utils.EqualsUUID(uuidA, uuidB);
	}
	public static boolean EqualsPlayer(final Player playerA, final Player playerB) {
		if (playerA == null || playerB == null)
			return (playerA == null && playerB == null);
		return Utils.EqualsUUID(playerA.getUniqueId(), playerB.getUniqueId());
	}

	public static boolean EqualsPotionEffect(final PotionEffect effectA, final PotionEffect effectB) {
		if (effectA == null || effectB == null)
			return (effectA == null && effectB == null);
		return effectA.equals(effectB);
	}

	public static boolean EqualsLocation(final Location locA, final Location locB) {
		if (locA == null || locB == null)
			return (locA == null && locB == null);
		if (!EqualsWorld(locA.getWorld(), locB.getWorld()))
			return false;
		if (locA.getBlockX() != locB.getBlockX()) return false;
		if (locA.getBlockY() != locB.getBlockY()) return false;
		if (locA.getBlockZ() != locB.getBlockZ()) return false;
		return true;
	}

	public static boolean EqualsWorld(final Location locA, final Location locB) {
		return EqualsWorld(locA.getWorld(), locB.getWorld());
	}
	public static boolean EqualsWorld(final World worldA, final World worldB) {
		if (worldA == null || worldB == null)
			return (worldA == null && worldB == null);
		return worldA.equals(worldB);
	}



	public static void BroadcastNear(final Location loc, final int distance, final String msg) {
		if (loc == null) throw new NullPointerException();
		final World world = loc.getWorld();
		final Collection<? extends Player> players = Bukkit.getOnlinePlayers();
		for (final Player player : players) {
			if (player == null)
				continue;
			final Location playerLoc = player.getLocation();
			if (playerLoc == null)
				continue;
			if (!EqualsWorld(world, playerLoc.getWorld()))
				continue;
			final double playerDist = playerLoc.distance(loc);
			if (playerDist <= distance)
				player.sendMessage(msg);
		}
	}

	public static void BroadcastWorld(final String worldName, final String msg) {
		BroadcastWorld(Bukkit.getWorld(worldName), msg);
	}
	public static void BroadcastWorld(final World world, final String msg) {
		for (final Player player : world.getPlayers())
			player.sendMessage(msg);
	}



	// -------------------------------------------------------------------------------
	// file paths



	public static String GetServerPath() {
		final File path = Bukkit.getWorldContainer();
		try {
			return path.getCanonicalPath();
		} catch (IOException ignore) {}
		return path.getAbsolutePath();
	}



	// -------------------------------------------------------------------------------
	// maps



	@SuppressWarnings("deprecation")
	public static MapView GetMapView(final int mapid) {
		return Bukkit.getMap(mapid);
	}

	@SuppressWarnings("deprecation")
	public static void SetMapID(final ItemStack map, final int id) {
		final MapMeta meta = (MapMeta) map.getItemMeta();
		meta.setMapId(id);
		map.setItemMeta(meta);
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
