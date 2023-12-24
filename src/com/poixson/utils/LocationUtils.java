package com.poixson.utils;

import org.bukkit.Axis;
import org.bukkit.Rotation;
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



	// x, z, w, d
	public static Iabcd Rotate(final Iabcd loc, final BlockFace face) {
		switch (face) {
		case SOUTH: return loc;
		case NORTH: return new Iabcd(loc.c-loc.a, loc.d-loc.b,   loc.c, 0-loc.d);
		case EAST:  return new Iabcd(      loc.b,       loc.a,   loc.d,   loc.c);
		case WEST:  return new Iabcd(loc.d-loc.b, loc.c-loc.a, 0-loc.d,   loc.c);
		default:
			return null;
		}
	}



	// rotate BlockFace by double
	public static BlockFace Rotate(final BlockFace face, final double rotation) {
		if (rotation < 0.0) throw new RuntimeException("Invalid rotation: " + Double.toString(rotation));
		double rot = rotation % 1.0;
		if (rot == 0.0)
			return face;
		BlockFace dir = face;
		while (rot > 0.0) {
			switch (dir) {
			case NORTH:      dir = BlockFace.NORTH_EAST; break;
			case NORTH_EAST: dir = BlockFace.EAST;       break;
			case EAST:       dir = BlockFace.SOUTH_EAST; break;
			case SOUTH_EAST: dir = BlockFace.SOUTH;      break;
			case SOUTH:      dir = BlockFace.SOUTH_WEST; break;
			case SOUTH_WEST: dir = BlockFace.WEST;       break;
			case WEST:       dir = BlockFace.NORTH_WEST; break;
			case NORTH_WEST: dir = BlockFace.NORTH;      break;
			default: throw new RuntimeException("Invalid direction: " + face.toString());
			}
			rot -= 0.125;
		}
		return dir;
	}
	// rotate ax by double
	public static char Rotate(final char ax, final double rotation) {
		if (rotation < 0.0) throw new RuntimeException("Invalid rotation: " + Double.toString(rotation));
		double rot = rotation % 1.0;
		if (rot == 0.0)
			return ax;
		char dir = ax;
		while (rot > 0.0) {
			switch (dir) {
			case 'u': case 'd':  break;
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



	// rotate axis by BlockFace
	public static String Rotate(final String axis, final BlockFace rotate) {
		if (BlockFace.SOUTH.equals(rotate))
			return axis;
		final StringBuilder result = new StringBuilder();
		final int len = axis.length();
		for (int i=0; i<len; i++) {
			switch (rotate) {
			case WEST:  result.append(Rotate(axis.charAt(i), 0.25)); break;
			case NORTH: result.append(Rotate(axis.charAt(i), 0.5 )); break;
			case EAST:  result.append(Rotate(axis.charAt(i), 0.75)); break;
			default: break;
			}
		}
		return result.toString();
	}



	// -------------------------------------------------------------------------------
	// conversion



	public static BlockFace YawToFace(final float yaw) {
		float way = yaw + 22.5f;
		while (way < 0)
			way += 360.0f;
		way = way % 360.0f;
		if (way <  90.0f) return BlockFace.SOUTH;
		if (way < 180.0f) return BlockFace.WEST;
		if (way < 270.0f) return BlockFace.NORTH;
		return BlockFace.EAST;
	}
	public static Rotation YawToRotation(final float yaw) {
		float way = yaw + 22.5f;
		while (way < 0)
			way += 360.0f;
		way = way % 360.0f;
		if (way <  45.0f) return Rotation.NONE;
		if (way <  90.0f) return Rotation.CLOCKWISE_45;
		if (way < 135.0f) return Rotation.CLOCKWISE;
		if (way < 180.0f) return Rotation.CLOCKWISE_135;
		if (way < 225.0f) return Rotation.FLIPPED;
		if (way < 270.0f) return Rotation.FLIPPED_45;
		if (way < 315.0f) return Rotation.COUNTER_CLOCKWISE;
		return Rotation.COUNTER_CLOCKWISE_45;
	}
	public static Rotation YawToRotation90(final float yaw) {
		float way = yaw + 67.5f;
		while (way < 0)
			way += 360.0f;
		way = way % 360.0f;
		if (way <  90.0f) return Rotation.NONE;
		if (way < 180.0f) return Rotation.CLOCKWISE;
		if (way < 270.0f) return Rotation.FLIPPED;
		return Rotation.COUNTER_CLOCKWISE;
	}



	public static Rotation FaceToRotation(final BlockFace face) {
		switch (face) {
		case SOUTH: return Rotation.NONE;
		case WEST:  return Rotation.CLOCKWISE;
		case NORTH: return Rotation.FLIPPED;
		case EAST:  return Rotation.COUNTER_CLOCKWISE;
		default: break;
		}
		return null;
	}
	public static BlockFace RotationToFace(final Rotation rotation) {
		switch (rotation) {
		case NONE:              return BlockFace.SOUTH;
		case CLOCKWISE:         return BlockFace.WEST;
		case FLIPPED:           return BlockFace.NORTH;
		case COUNTER_CLOCKWISE: return BlockFace.EAST;
		default: break;
		}
		return null;
	}



	// BlockFace to Axis
	public static Axis FaceToAxis(final BlockFace face) {
		switch (face) {
		case UP:    case DOWN:  return Axis.Y;
		case NORTH: case SOUTH: return Axis.Z;
		case EAST:  case WEST:  return Axis.X;
		default: break;
		}
		return null;
	}



	// BlockFace to axis char
	public static char FaceToAxChar(final BlockFace face) {
		switch (face) {
		case UP:    return 'u';
		case DOWN:  return 'd';
		case NORTH: return 'n';
		case SOUTH: return 's';
		case EAST:  return 'e';
		case WEST:  return 'w';
		default: return 0;
		}
	}
	// BlockFace to axis string
	public static String FaceToAxString(final BlockFace face) {
		switch (face) {
		case UP:    return "u";
		case DOWN:  return "d";
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
	public static String FaceToAxisString(final BlockFace face) {
		switch (face) {
		case UP:    return "up";
		case DOWN:  return "down";
		case NORTH: return "north";
		case SOUTH: return "south";
		case EAST:  return "east";
		case WEST:  return "west";
		case NORTH_EAST: return "north-east";
		case NORTH_WEST: return "north-west";
		case SOUTH_EAST: return "south-east";
		case SOUTH_WEST: return "south-west";
		default: return null;
		}
	}
	public static String FaceToPillarAxisString(final BlockFace face) {
		switch (face) {
		case UP:
		case DOWN:  return "y";
		case NORTH:
		case SOUTH: return "z";
		case EAST:
		case WEST:  return "x";
		default: return null;
		}
	}

	// axis char to BlockFace
	public static BlockFace AxToFace(final char ax) {
		switch (ax) {
		case 'u': case 'Y': return BlockFace.UP;
		case 'd': case 'y': return BlockFace.DOWN;
		case 'n': case 'z': return BlockFace.NORTH;
		case 's': case 'Z': return BlockFace.SOUTH;
		case 'e': case 'X': return BlockFace.EAST;
		case 'w': case 'x': return BlockFace.WEST;
		default: return null;
		}
	}
	// axis string to BlockFace
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



	// BlockFace to x,y,z
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
	// BlockFace to x,z
	public static Iab FaceToIxz(final BlockFace face) {
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

	// axis char to x,y,z
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

	// axis char to x,z
	public static Iab AxToIxz(final char ax) {
		switch (ax) {
		case 'n': case 'z': return new Iab( 0, -1);
		case 's': case 'Z': return new Iab( 0,  1);
		case 'e': case 'X': return new Iab( 1,  0);
		case 'w': case 'x': return new Iab(-1,  0);
		default: return null;
		}
	}



	// x,z to BlockFace
	public static BlockFace ValueToFaceQuarter(final int x, final int z) {
		if (x < 0) return (z < 0 ? BlockFace.NORTH_WEST : BlockFace.SOUTH_WEST);
		else       return (z < 0 ? BlockFace.NORTH_EAST : BlockFace.SOUTH_EAST);
	}



}
