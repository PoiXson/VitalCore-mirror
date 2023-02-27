package com.poixson.commonmc.utils;

import org.bukkit.Axis;
import org.bukkit.block.BlockFace;

import com.poixson.tools.Keeper;
import com.poixson.tools.dao.Iab;
import com.poixson.tools.dao.Iabc;
import com.poixson.tools.dao.Iabcd;


public final class LocationUtils {
	private LocationUtils() {}
	static { Keeper.add(new LocationUtils()); }



	// -------------------------------------------------------------------------------
	// rotation



	public static Iabcd Rotate(final Iabcd loc, final BlockFace face) {
		switch (face) {
		case NORTH: return loc;
		case SOUTH: return new Iabcd(loc.a-loc.b-1, loc.d-loc.c-1, loc.a, loc.d);
		case EAST:  return new Iabcd(loc.d-loc.c-1, loc.b,         loc.d, loc.a);
		case WEST:  return new Iabcd(loc.c,         loc.a-loc.b-1, loc.d, loc.a);
		default:
			return null;
		}
	}



	public static BlockFace Rotate(final BlockFace face, final double rotation) {
		if (rotation < 0.0) throw new RuntimeException("Invalid rotation: " + Double.toString(rotation));
		double rot = rotation % 1.0;
		if (rot == 0.0)
			return face;
		BlockFace dir = face;
		while (rot > 0.0) {
			switch (dir) {
			case NORTH: dir = BlockFace.EAST;  break;
			case EAST:  dir = BlockFace.SOUTH; break;
			case SOUTH: dir = BlockFace.WEST;  break;
			case WEST:  dir = BlockFace.NORTH; break;
			default: throw new RuntimeException("Invalid direction: " + face.toString());
			}
			rot -= 0.25;
		}
		return dir;
	}
	public static char Rotate(final char ax, final double rotation) {
		if (rotation < 0.0) throw new RuntimeException("Invalid rotation: " + Double.toString(rotation));
		double rot = rotation % 1.0;
		if (rot == 0.0)
			return ax;
		char dir = ax;
		while (rot > 0.0) {
			switch (dir) {
			case 'n': dir = 'e'; break;
			case 'e': dir = 's'; break;
			case 's': dir = 'w'; break;
			case 'w': dir = 'n'; break;
			default: throw new RuntimeException("Invalid direction: " + dir);
			}
			rot -= 0.25;
		}
		return dir;
	}



	// -------------------------------------------------------------------------------
	// conversion



	public static Axis FaceToAxis(final BlockFace face) {
		switch (face) {
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



	public static String FaceToAx(final BlockFace face) {
		switch (face) {
		case NORTH: return "n";
		case SOUTH: return "s";
		case EAST:  return "e";
		case WEST:  return "w";
		case NORTH_EAST: return "ne";
		case NORTH_WEST: return "nw";
		case SOUTH_EAST: return "se";
		case SOUTH_WEST: return "sw";
		default: return null;
		}
	}
	public static BlockFace AxToFace(final char ax) {
		switch (ax) {
		case 'n': return BlockFace.NORTH;
		case 's': return BlockFace.SOUTH;
		case 'e': return BlockFace.EAST;
		case 'w': return BlockFace.WEST;
		default: return null;
		}
	}
	public static BlockFace AxToFace(final String ax) {
		switch (ax) {
		case "u": case "Y": return BlockFace.UP;
		case "d": case "y": return BlockFace.DOWN;
		case "n": case "z": return BlockFace.NORTH;
		case "s": case "Z": return BlockFace.SOUTH;
		case "e": case "X": return BlockFace.EAST;
		case "w": case "x": return BlockFace.WEST;
		case "ne": return BlockFace.NORTH_EAST;
		case "nw": return BlockFace.NORTH_WEST;
		case "se": return BlockFace.SOUTH_EAST;
		case "sw": return BlockFace.SOUTH_WEST;
		default: return null;
		}
	}



	public static Iabc FaceToIxyz(final BlockFace face) {
		switch (face) {
		case NORTH: return new Iabc( 0,  0, -1);
		case SOUTH: return new Iabc( 0,  0,  1);
		case EAST:  return new Iabc( 1,  0,  0);
		case WEST:  return new Iabc(-1,  0,  0);
		case UP:    return new Iabc( 0,  1,  0);
		case DOWN:  return new Iabc( 0, -1,  0);
		case NORTH_EAST: return new Iabc( 1, 0, -1);
		case NORTH_WEST: return new Iabc(-1, 0, -1);
		case SOUTH_EAST: return new Iabc( 1, 0,  1);
		case SOUTH_WEST: return new Iabc(-1, 0,  1);
		default: return null;
		}
	}
	public static Iab FaceToIxy(final BlockFace face) {
		switch (face) {
		case NORTH: return new Iab( 0, -1);
		case SOUTH: return new Iab( 0,  1);
		case EAST:  return new Iab( 1,  0);
		case WEST:  return new Iab(-1,  0);
		case NORTH_EAST: return new Iab( 1, -1);
		case NORTH_WEST: return new Iab(-1, -1);
		case SOUTH_EAST: return new Iab( 1,  1);
		case SOUTH_WEST: return new Iab(-1,  1);
		default: return null;
		}
	}

	public static Iabc AxToIxyz(final String ax) {
		return AxToIxyz(ax.charAt(0));
	}
	public static Iabc AxToIxyz(final char ax) {
		switch (ax) {
		case 'n': case 'z': return new Iabc( 0,  0, -1);
		case 's': case 'Z': return new Iabc( 0,  0,  1);
		case 'e': case 'X': return new Iabc( 1,  0,  0);
		case 'w': case 'x': return new Iabc(-1,  0,  0);
		case 'u': case 'Y': return new Iabc( 0,  1,  0);
		case 'd': case 'y': return new Iabc( 0, -1,  0);
		default: return null;
		}
	}

	public static Iab AxToIxy(final String ax) {
		return AxToIxy(ax.charAt(0));
	}
	public static Iab AxToIxy(final char ax) {
		switch (ax) {
		case 'n': case 'z': return new Iab( 0, -1);
		case 's': case 'Z': return new Iab( 0,  1);
		case 'e': case 'X': return new Iab( 1,  0);
		case 'w': case 'x': return new Iab(-1,  0);
		default: return null;
		}
	}



	public static BlockFace ValueToFaceQuarter(final int x, final int z) {
		if (x < 0) return (z < 0 ? BlockFace.NORTH_WEST : BlockFace.SOUTH_WEST);
		else       return (z < 0 ? BlockFace.NORTH_EAST : BlockFace.SOUTH_EAST);
	}



}
