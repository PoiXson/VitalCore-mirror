package com.poixson.commonbukkit.utils;

import org.bukkit.block.BlockFace;

import com.poixson.tools.Keeper;
import com.poixson.tools.dao.Ixywd;


public class LocationUtils {
	private LocationUtils() {}
	static { Keeper.add(new LocationUtils()); }



	public static Ixywd RotateXZ(final Ixywd loc, final BlockFace direction) {
		switch (direction) {
		case NORTH: return loc;
		case SOUTH: return new Ixywd(loc.w-loc.x-1, loc.d-loc.y-1, loc.w, loc.d);
		case EAST:  return new Ixywd(loc.d-loc.y-1, loc.x,         loc.d, loc.w);
		case WEST:  return new Ixywd(loc.y,         loc.w-loc.x-1, loc.d, loc.w);
		default:
			return null;
		}
	}



	public static BlockFace Rotate(final BlockFace direction, final double rotation) {
		if (rotation < 0.0) throw new RuntimeException("Invalid rotation: "+Double.toString(rotation));
		double rot = rotation % 1.0;
		if (rot == 0.0)
			return direction;
		if (rot == 0.25) {
			switch (direction) {
			case NORTH: return BlockFace.EAST;
			case EAST:  return BlockFace.SOUTH;
			case SOUTH: return BlockFace.WEST;
			case WEST:  return BlockFace.NORTH;
			default: throw new RuntimeException("Invalid direction: "+direction.toString());
			}
		}
		if (rot == 0.5) {
			switch (direction) {
			case NORTH: return BlockFace.SOUTH;
			case EAST:  return BlockFace.WEST;
			case SOUTH: return BlockFace.NORTH;
			case WEST:  return BlockFace.EAST;
			default: throw new RuntimeException("Invalid direction: "+direction.toString());
			}
		}
		if (rot == 0.75) {
			switch (direction) {
			case NORTH: return BlockFace.WEST;
			case EAST:  return BlockFace.NORTH;
			case SOUTH: return BlockFace.EAST;
			case WEST:  return BlockFace.SOUTH;
			default: throw new RuntimeException("Invalid direction: "+direction.toString());
			}
		}
		BlockFace dir = direction;
		while (rot > 0.0) {
			switch (dir) {
			case NORTH: dir = BlockFace.EAST;  break;
			case EAST:  dir = BlockFace.SOUTH; break;
			case SOUTH: dir = BlockFace.WEST;  break;
			case WEST:  dir = BlockFace.NORTH; break;
			default: throw new RuntimeException("Invalid direction: "+direction.toString());
			}
			rot -= 0.25;
		}
		return dir;
	}



}
