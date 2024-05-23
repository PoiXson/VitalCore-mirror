package com.poixson.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.poixson.tools.dao.Iab;
import com.poixson.tools.dao.Iabcd;


public class CraftScriptUtils {
	private CraftScriptUtils() {}

	public static final int MAP_SIZE = 128;



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



	public static String GetLocationScriptFile(final Location loc) {
		return String.format(
			"%s_%dx_%dy_%dz.js",
			loc.getWorld().getName(),
			loc.getBlockX(),
			loc.getBlockY(),
			loc.getBlockZ()
		);
	}



	public static Iab FixCursorPosition(final Vector vec,
			final Iabcd screen_size, final BlockFace facing) {
		final int y = ((int)Math.round( MAP_SIZE * (1.0-(vec.getY()%1.0)) )) - screen_size.b;
		final double vec_x;
		SWITCH_FACING:
		switch (facing) {
		case NORTH: case SOUTH: vec_x = vec.getX(); break SWITCH_FACING;
		case EAST:  case WEST:  vec_x = vec.getZ(); break SWITCH_FACING;
		default: throw new RuntimeException("Unknown cursor direction: " + facing.toString());
		}
		int x = ((int)Math.round( MAP_SIZE * (vec_x%1.0) )) - screen_size.a;
		// reverse direction
		SWITCH_FACING:
		switch (facing) {
		case NORTH: case EAST: x = screen_size.c - x; break SWITCH_FACING;
		case SOUTH: case WEST:                        break SWITCH_FACING;
		default: throw new RuntimeException("Unknown cursor direction: " + facing.toString());
		}
		return new Iab(x, y);
	}
//TODO: doesn't work properly with angles over 45 degrees
	public static Iab FixClickPosition(final Vector vec,
			final Iabcd screen_size, final BlockFace facing, final Location player_loc) {
		int y = ((int)Math.round( MAP_SIZE * (0.5-(vec.getY()%1.0)) )) - screen_size.b - 1;
		final double vec_x;
		SWITCH_FACING:
		switch (facing) {
		case NORTH: case SOUTH: vec_x = vec.getX() + 0.5; break SWITCH_FACING;
		case EAST:  case WEST:  vec_x = vec.getZ() + 0.5; break SWITCH_FACING;
		default: throw new RuntimeException("Unknown click direction: " + facing.toString());
		}
		int x = ((int)Math.round( MAP_SIZE * (vec_x%1.0) )) - screen_size.a;
		// reverse direction
		SWITCH_FACING:
		switch (facing) {
		case NORTH: case EAST: x = screen_size.c - x; break SWITCH_FACING;
		case SOUTH: case WEST:                        break SWITCH_FACING;
		default: throw new RuntimeException("Unknown click direction: " + facing.toString());
		}
		// correct for angle and map distance from wall
		{
			final double angle_x = 45.0 - ((player_loc.getYaw()   + 225.0) % 90.0);
			final double angle_y = 45.0 - ((player_loc.getPitch() + 225.0) % 90.0);
			x += (int)Math.round(0.0 - (Math.tan(angle_x / 45.0) * (0.03125 * MAP_SIZE)));
			y += (int)Math.round(0.0 - (Math.tan(angle_y / 45.0) * (0.03125 * MAP_SIZE)));
		}
		return new Iab(x, y);
	}



}
