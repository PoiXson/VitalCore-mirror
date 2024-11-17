package com.poixson.utils;

import static com.poixson.utils.Utils.IsEmpty;

import java.util.Collection;
import java.util.LinkedList;

import org.bukkit.Axis;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Rotation;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import com.poixson.tools.Keeper;
import com.poixson.tools.dao.Iab;
import com.poixson.tools.dao.Iabc;
import com.poixson.tools.dao.Iabcd;


public final class LocationUtils {
	private LocationUtils() {}
	static { Keeper.add(new LocationUtils()); }



	// -------------------------------------------------------------------------------
	// distance



	public static double Distance2D(final Location locA, final Location locB) {
		if (locA == null) throw new NullPointerException();
		if (locB == null) throw new NullPointerException();
		if (!locA.getWorld().equals(locB.getWorld()))
			return Double.MIN_VALUE;
		return MathUtils.Distance2D(
			locA.getX(), locA.getZ(),
			locB.getX(), locB.getZ()
		);
	}
	public static double Distance3D(final Location locA, final Location locB) {
		if (locA == null) throw new NullPointerException();
		if (locB == null) throw new NullPointerException();
		if (!locA.getWorld().equals(locB.getWorld()))
			return Double.MIN_VALUE;
		return MathUtils.Distance3D(
			locA.getX(), locA.getY(), locA.getZ(),
			locB.getX(), locB.getY(), locB.getZ()
		);
	}

	public static double DistanceFast2D(final Location locA, final Location locB) {
		if (locA == null) throw new NullPointerException();
		if (locB == null) throw new NullPointerException();
		if (!locA.getWorld().equals(locB.getWorld()))
			return Double.MIN_VALUE;
		return MathUtils.DistanceFast2D(
			locA.getX(), locA.getZ(),
			locB.getX(), locB.getZ()
		);
	}
	public static double DistanceFast3D(final Location locA, final Location locB) {
		if (locA == null) throw new NullPointerException();
		if (locB == null) throw new NullPointerException();
		if (!locA.getWorld().equals(locB.getWorld()))
			return Double.MIN_VALUE;
		return MathUtils.DistanceFast3D(
			locA.getX(), locA.getY(), locA.getZ(),
			locB.getX(), locB.getY(), locB.getZ()
		);
	}



	public static void BroadcastNear(final Location loc, final int distance, final String msg) {
		if (loc == null) throw new NullPointerException();
		final Collection<? extends Player> players = Bukkit.getOnlinePlayers();
		for (final Player player : players) {
			if (player == null) continue;
			final Location playerLoc = player.getLocation();
			if (playerLoc != null) {
				final double dist = DistanceFast2D(loc, playerLoc);
				if (dist >= 0.0
				&&  dist < (double)distance)
					player.sendMessage(msg);
			}
		}
	}

	public static void BroadcastWorld(final String world_name, final String msg) {
		BroadcastWorld(Bukkit.getWorld(world_name), msg);
	}
	public static void BroadcastWorld(final World world, final String msg) {
		for (final Player player : world.getPlayers())
			player.sendMessage(msg);
	}



	// -------------------------------------------------------------------------------
	// rotation



	// x, z, w, d
	public static Iabcd Rotate(final Iabcd loc, final BlockFace face) {
		switch (face) {
		case SOUTH: return loc;
		case NORTH: return new Iabcd(loc.c-loc.a, loc.d-loc.b,   loc.c, 0-loc.d);
		case EAST:  return new Iabcd(      loc.b,       loc.a,   loc.d,   loc.c);
		case WEST:  return new Iabcd(loc.d-loc.b, loc.c-loc.a, 0-loc.d,   loc.c);
		default:    return null;
		}
	}



	// rotate BlockFace by double
	public static BlockFace Rotate(final BlockFace face, final double angle) {
		double ang = (angle % 1.0) + (angle<0.0 ? 1.0: 0.0);
		if (ang == 0.0)
			return face;
		BlockFace dir = face;
		while (ang > 0.0) {
			switch (dir) {
			case NORTH:      dir = BlockFace.NORTH_EAST; break;
			case NORTH_EAST: dir = BlockFace.EAST;       break;
			case EAST:       dir = BlockFace.SOUTH_EAST; break;
			case SOUTH_EAST: dir = BlockFace.SOUTH;      break;
			case SOUTH:      dir = BlockFace.SOUTH_WEST; break;
			case SOUTH_WEST: dir = BlockFace.WEST;       break;
			case WEST:       dir = BlockFace.NORTH_WEST; break;
			case NORTH_WEST: dir = BlockFace.NORTH;      break;
			default: throw new RuntimeException("Invalid direction: "+face.toString());
			}
			ang -= 0.125;
		}
		return dir;
	}
	// rotate ax by double
	public static char Rotate(final char ax, final double angle) {
		double ang = (angle % 1.0) + (angle<0.0 ? 1.0: 0.0);
		if (ang == 0.0)
			return ax;
		char dir = ax;
		while (ang > 0.0) {
			switch (dir) {
			case 'u': case 'd':  break;
			case 'n': dir = 'e'; break;
			case 'e': dir = 's'; break;
			case 's': dir = 'w'; break;
			case 'w': dir = 'n'; break;
			default: throw new RuntimeException("Invalid direction: "+dir);
			}
			ang -= 0.25;
		}
		return dir;
	}
	public static char Rotate(final char ax, final BlockFace rotate) {
		return Rotate(ax, FaceToNormAngle(rotate));
	}



	// rotate axis by BlockFace
	public static String Rotate(final String axis, final BlockFace rotate) {
		if (BlockFace.SOUTH.equals(rotate))
			return axis;
		final StringBuilder result = new StringBuilder();
		final int len = axis.length();
		for (int i=0; i<len; i++)
			result.append(Rotate(axis.charAt(i), FaceToNormAngle(rotate)));
		return result.toString();
	}



	// -------------------------------------------------------------------------------
	// conversion



	public static double FaceToNormAngle(final BlockFace facing) {
		switch (facing) {
		case WEST:  return 0.25;
		case NORTH: return 0.5;
		case EAST:  return 0.75;
		default:    return 0.0;
		}
	}
	public static BlockFace YawToFace(final float yaw) {
		float way = yaw + 22.5f;
		way = (way % 360.0f) + (way<0.0f ? 360.0f : 0.0f);
		if (way <  90.0f) return BlockFace.SOUTH;
		if (way < 180.0f) return BlockFace.WEST;
		if (way < 270.0f) return BlockFace.NORTH;
		return BlockFace.EAST;
	}
	public static Rotation YawToRotation(final float yaw) {
		float way = yaw + 22.5f;
		way = (way % 360.0f) + (way<0.0f ? 360.0f : 0.0f);
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
		way = (way % 360.0f) + (way<0.0f ? 360.0f : 0.0f);
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
		return face.toString().toLowerCase();
	}

	public static char FaceToPillarAx(final BlockFace face) {
		switch (face) {
		case UP:    case DOWN:  return 'y';
		case NORTH: case SOUTH: return 'z';
		case EAST:  case WEST:  return 'x';
		default: return 0;
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
	public static BlockFace[] AxisToFaces(final String axis) {
		final LinkedList<BlockFace> result = new LinkedList<BlockFace>();
		final int len = axis.length();
		for (int i=0; i<len; i++)
			result.addLast(AxToFace(axis.charAt(i)));
		return result.toArray(new BlockFace[0]);
	}

	// rotate a block in a 2x2 area
	public static BlockFace[] AxisToFaces2x2(final String rots) {
		if (IsEmpty(rots)
		|| "-"   .equals(rots)
		|| "none".equals(rots)
		|| "null".equals(rots))
			return null;
		if (rots.length() != 4) throw new RuntimeException("Invalid block rotations: "+rots);
		final String lower = rots.toLowerCase();
		final LinkedList<BlockFace> list = new LinkedList<BlockFace>();
		for (int i=0; i<4; i++) {
			final BlockFace face = AxToFace(lower.charAt(i));
			if (face == null) throw new RuntimeException("Invalid carpet rotation: "+lower.charAt(i));
			list.addLast(face);
		}
		return list.toArray(new BlockFace[0]);
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



	public static Iab AxisToIxz(final String axis) {
		int x = 0;
		int z = 0;
		final int len = axis.length();
		for (int i=0; i<len; i++) {
			final Iab dir = AxToIxz(axis.charAt(i));
			x += dir.a;
			z += dir.b;
		}
		return new Iab(x, z);
	}
	public static Iabc AxisToIxyz(final String axis) {
		int x = 0;
		int y = 0;
		int z = 0;
		final int len = axis.length();
		for (int i=0; i<len; i++) {
			final Iabc dir = AxToIxyz(axis.charAt(i));
			x += dir.a;
			y += dir.b;
			z += dir.c;
		}
		return new Iabc(x, y, z);
	}



	// x,z to BlockFace
	public static BlockFace ValueToFaceQuarter(final int x, final int z) {
		if (x < 0) return (z < 0 ? BlockFace.NORTH_WEST : BlockFace.SOUTH_WEST);
		else       return (z < 0 ? BlockFace.NORTH_EAST : BlockFace.SOUTH_EAST);
	}



}
