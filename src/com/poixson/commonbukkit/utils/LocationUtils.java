package com.poixson.commonbukkit.utils;

import org.bukkit.Axis;
import org.bukkit.block.BlockFace;

import com.poixson.tools.Keeper;
import com.poixson.tools.dao.Ixywd;
import com.poixson.tools.dao.Ixyz;


public final class LocationUtils {
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



	public static Axis DirectionToAxis(final BlockFace direction) {
		switch (direction) {
		case NORTH:
		case SOUTH:
		case NORTH_NORTH_EAST:
		case NORTH_NORTH_WEST:
		case SOUTH_SOUTH_EAST:
		case SOUTH_SOUTH_WEST:
			return Axis.Z;
		case EAST:
		case WEST:
		case EAST_NORTH_EAST:
		case WEST_NORTH_WEST:
		case EAST_SOUTH_EAST:
		case WEST_SOUTH_WEST:
			return Axis.X;
		case NORTH_EAST:
		case NORTH_WEST:
		case SOUTH_EAST:
		case SOUTH_WEST:
		default: break;
		}
		return null;
	}



	public static Ixyz AxisToValue(final String axis) {
		return AxisToValue(axis.charAt(0));
	}
	public static Ixyz AxisToValue(final char axis) {
		switch (axis) {
		case 'z': case 'n': return new Ixyz( 0,  0, -1);
		case 'Z': case 's': return new Ixyz( 0,  0,  1);
		case 'X': case 'e': return new Ixyz( 1,  0,  0);
		case 'x': case 'w': return new Ixyz(-1,  0,  0);
		case 'Y': case 'u': return new Ixyz( 0,  1,  0);
		case 'y': case 'd': return new Ixyz( 0, -1,  0);
		default: break;
		}
		throw new RuntimeException("Invalid axis: "+axis);
	}



	public static String RotateAround00(final int chunkX, final int chunkZ) {
		if (chunkZ < 0) {
			return (chunkX<0 ? "YZX" : "YZx");
		} else {
			return (chunkX<0 ? "YzX" : "Yzx");
		}
	}



}
