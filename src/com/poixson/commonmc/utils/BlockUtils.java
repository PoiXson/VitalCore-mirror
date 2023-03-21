package com.poixson.commonmc.utils;

import java.util.Set;

import org.bukkit.Axis;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Levelled;
import org.bukkit.block.data.Lightable;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.block.data.Openable;
import org.bukkit.block.data.Orientable;
import org.bukkit.block.data.Rotatable;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.block.data.type.Bed;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.Door.Hinge;
import org.bukkit.block.data.type.Light;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Wall;

import com.poixson.utils.Utils;


public final class BlockUtils {
	private BlockUtils() {}



	public static void SetBlockSpecial(final Location loc, final Set<String> special) {
		if (Utils.isEmpty(special)) return;
		final Block block = loc.getBlock();
		final BlockData data = block.getBlockData();
		if (ApplyBlockSpecial(data, special))
			block.setBlockData(data);
	}
	public static boolean ApplyBlockSpecial(final BlockData data, final Set<String> special) {
		if (Utils.isEmpty(special)) return false;
		boolean changed = false;
		if (data instanceof Slab) {
			if (special.contains("top"   )) { changed = true; ((Slab)data).setType(Slab.Type.TOP   ); } else
			if (special.contains("bottom")) { changed = true; ((Slab)data).setType(Slab.Type.BOTTOM); } else
			if (special.contains("double")) { changed = true; ((Slab)data).setType(Slab.Type.DOUBLE); }
		}
		if (data instanceof Bisected) {
			if (special.contains("top"   )) { changed = true; ((Bisected)data).setHalf(Bisected.Half.TOP   ); } else
			if (special.contains("bottom")) { changed = true; ((Bisected)data).setHalf(Bisected.Half.BOTTOM); }
		}
		if (data instanceof Directional) {
			if (special.contains("up"   ) || special.contains("u")) { changed = true; ((Directional)data).setFacing(BlockFace.UP   ); } else
			if (special.contains("down" ) || special.contains("d")) { changed = true; ((Directional)data).setFacing(BlockFace.DOWN ); } else
			if (special.contains("north") || special.contains("n")) { changed = true; ((Directional)data).setFacing(BlockFace.NORTH); } else
			if (special.contains("south") || special.contains("s")) { changed = true; ((Directional)data).setFacing(BlockFace.SOUTH); } else
			if (special.contains("east" ) || special.contains("e")) { changed = true; ((Directional)data).setFacing(BlockFace.EAST ); } else
			if (special.contains("west" ) || special.contains("w")) { changed = true; ((Directional)data).setFacing(BlockFace.WEST ); }
		}
		if (data instanceof MultipleFacing) {
			if (special.contains("top"   )) { changed = true; ((MultipleFacing)data).setFace(BlockFace.UP,    true); }
			if (special.contains("bottom")) { changed = true; ((MultipleFacing)data).setFace(BlockFace.DOWN,  true); }
			if (special.contains("north" )) { changed = true; ((MultipleFacing)data).setFace(BlockFace.NORTH, true); }
			if (special.contains("south" )) { changed = true; ((MultipleFacing)data).setFace(BlockFace.SOUTH, true); }
			if (special.contains("east"  )) { changed = true; ((MultipleFacing)data).setFace(BlockFace.EAST,  true); }
			if (special.contains("west"  )) { changed = true; ((MultipleFacing)data).setFace(BlockFace.WEST,  true); }
		}
		if (data instanceof Orientable) {
			if (special.contains("x") || special.contains("e") || special.contains("w")) { changed = true; ((Orientable)data).setAxis(Axis.X); } else
			if (special.contains("y") || special.contains("u") || special.contains("d")) { changed = true; ((Orientable)data).setAxis(Axis.Y); } else
			if (special.contains("z") || special.contains("n") || special.contains("s")) { changed = true; ((Orientable)data).setAxis(Axis.Z); }
		}
		if (data instanceof Rotatable) {
			if (special.contains("north"           ) || special.contains("n"  )) { changed = true; ((Rotatable)data).setRotation(BlockFace.NORTH           ); } else
			if (special.contains("south"           ) || special.contains("s"  )) { changed = true; ((Rotatable)data).setRotation(BlockFace.SOUTH           ); } else
			if (special.contains("east"            ) || special.contains("e"  )) { changed = true; ((Rotatable)data).setRotation(BlockFace.EAST            ); } else
			if (special.contains("west"            ) || special.contains("w"  )) { changed = true; ((Rotatable)data).setRotation(BlockFace.WEST            ); } else
			if (special.contains("north-east"      ) || special.contains("ne" )) { changed = true; ((Rotatable)data).setRotation(BlockFace.NORTH_EAST      ); } else
			if (special.contains("north-west"      ) || special.contains("nw" )) { changed = true; ((Rotatable)data).setRotation(BlockFace.NORTH_WEST      ); } else
			if (special.contains("south-east"      ) || special.contains("se" )) { changed = true; ((Rotatable)data).setRotation(BlockFace.SOUTH_EAST      ); } else
			if (special.contains("south-west"      ) || special.contains("sw" )) { changed = true; ((Rotatable)data).setRotation(BlockFace.SOUTH_WEST      ); } else
			if (special.contains("north-north-east") || special.contains("nne")) { changed = true; ((Rotatable)data).setRotation(BlockFace.NORTH_NORTH_EAST); } else
			if (special.contains("north-north-west") || special.contains("nnw")) { changed = true; ((Rotatable)data).setRotation(BlockFace.NORTH_NORTH_WEST); } else
			if (special.contains("south-south-east") || special.contains("sse")) { changed = true; ((Rotatable)data).setRotation(BlockFace.SOUTH_SOUTH_EAST); } else
			if (special.contains("south-south-west") || special.contains("ssw")) { changed = true; ((Rotatable)data).setRotation(BlockFace.SOUTH_SOUTH_WEST); } else
			if (special.contains("east-north-east" ) || special.contains("ene")) { changed = true; ((Rotatable)data).setRotation(BlockFace.EAST_NORTH_EAST ); } else
			if (special.contains("west-north-west" ) || special.contains("wnw")) { changed = true; ((Rotatable)data).setRotation(BlockFace.WEST_NORTH_WEST ); } else
			if (special.contains("east-south-east" ) || special.contains("ese")) { changed = true; ((Rotatable)data).setRotation(BlockFace.EAST_SOUTH_EAST ); } else
			if (special.contains("west-south-west" ) || special.contains("wsw")) { changed = true; ((Rotatable)data).setRotation(BlockFace.WEST_SOUTH_WEST ); }
		}
		if (data instanceof Wall) {
			if (special.contains("north")) { changed = true; ((Wall)data).setHeight(BlockFace.NORTH, Wall.Height.LOW ); } else
			if (special.contains("NORTH")) { changed = true; ((Wall)data).setHeight(BlockFace.NORTH, Wall.Height.TALL); } else
			if (special.contains("south")) { changed = true; ((Wall)data).setHeight(BlockFace.SOUTH, Wall.Height.LOW ); } else
			if (special.contains("SOUTH")) { changed = true; ((Wall)data).setHeight(BlockFace.SOUTH, Wall.Height.TALL); } else
			if (special.contains("east" )) { changed = true; ((Wall)data).setHeight(BlockFace.EAST,  Wall.Height.LOW ); } else
			if (special.contains("EAST" )) { changed = true; ((Wall)data).setHeight(BlockFace.EAST,  Wall.Height.TALL); } else
			if (special.contains("west" )) { changed = true; ((Wall)data).setHeight(BlockFace.WEST,  Wall.Height.LOW ); } else
			if (special.contains("WEST" )) { changed = true; ((Wall)data).setHeight(BlockFace.WEST,  Wall.Height.TALL); }
			if (special.contains("up"   )) { changed = true; ((Wall)data).setUp(true); }
		} else
		if (data instanceof Door) {
			if (special.contains("left" )) { changed = true; ((Door)data).setHinge(Hinge.LEFT ); } else
			if (special.contains("right")) { changed = true; ((Door)data).setHinge(Hinge.RIGHT); }
		} else
		if (data instanceof Bed) {
			if (special.contains("head")) { changed = true; ((Bed)data).setPart(Bed.Part.HEAD); } else
			if (special.contains("foot")) { changed = true; ((Bed)data).setPart(Bed.Part.FOOT); }
		}
		if (data instanceof Openable) {
			if (special.contains("open"  )) { changed = true; ((Openable)data).setOpen(true ); } else
			if (special.contains("closed")) { changed = true; ((Openable)data).setOpen(false); }
		}
		if (data instanceof Lightable) {
			if (special.contains("on" )) { changed = true; ((Lightable)data).setLit(true ); } else
			if (special.contains("off")) { changed = true; ((Lightable)data).setLit(false); }
		}
		if (data instanceof Light) {
			if (special.contains("15")) { changed = true; ((Light)data).setLevel(15); } else
			if (special.contains("14")) { changed = true; ((Light)data).setLevel(14); } else
			if (special.contains("13")) { changed = true; ((Light)data).setLevel(13); } else
			if (special.contains("12")) { changed = true; ((Light)data).setLevel(12); } else
			if (special.contains("11")) { changed = true; ((Light)data).setLevel(11); } else
			if (special.contains("10")) { changed = true; ((Light)data).setLevel(10); } else
			if (special.contains("9" )) { changed = true; ((Light)data).setLevel( 9); } else
			if (special.contains("8" )) { changed = true; ((Light)data).setLevel( 8); } else
			if (special.contains("7" )) { changed = true; ((Light)data).setLevel( 7); } else
			if (special.contains("6" )) { changed = true; ((Light)data).setLevel( 6); } else
			if (special.contains("5" )) { changed = true; ((Light)data).setLevel( 5); } else
			if (special.contains("4" )) { changed = true; ((Light)data).setLevel( 4); } else
			if (special.contains("3" )) { changed = true; ((Light)data).setLevel( 3); } else
			if (special.contains("2" )) { changed = true; ((Light)data).setLevel( 2); } else
			if (special.contains("1" )) { changed = true; ((Light)data).setLevel( 1); } else
			if (special.contains("0" )) { changed = true; ((Light)data).setLevel( 0); }
		}
		if (data instanceof Levelled) {
			if (special.contains("0" )) { changed = true; ((Levelled)data).setLevel( 0); } else // highest level
			if (special.contains("1" )) { changed = true; ((Levelled)data).setLevel( 1); } else
			if (special.contains("2" )) { changed = true; ((Levelled)data).setLevel( 2); } else
			if (special.contains("3" )) { changed = true; ((Levelled)data).setLevel( 3); } else
			if (special.contains("4" )) { changed = true; ((Levelled)data).setLevel( 4); } else
			if (special.contains("5" )) { changed = true; ((Levelled)data).setLevel( 5); } else
			if (special.contains("6" )) { changed = true; ((Levelled)data).setLevel( 6); } else
			if (special.contains("7" )) { changed = true; ((Levelled)data).setLevel( 7); } else // lowest level
			if (special.contains("8" )) { changed = true; ((Levelled)data).setLevel( 8); } else // flowing down
			if (special.contains("9" )) { changed = true; ((Levelled)data).setLevel( 9); } else
			if (special.contains("10")) { changed = true; ((Levelled)data).setLevel(10); } else
			if (special.contains("11")) { changed = true; ((Levelled)data).setLevel(11); } else
			if (special.contains("12")) { changed = true; ((Levelled)data).setLevel(12); } else
			if (special.contains("13")) { changed = true; ((Levelled)data).setLevel(13); } else
			if (special.contains("14")) { changed = true; ((Levelled)data).setLevel(14); } else
			if (special.contains("15")) { changed = true; ((Levelled)data).setLevel(15); }
		}
		if (data instanceof Waterlogged) {
			if (special.contains("logged")) { changed = true; ((Waterlogged)data).setWaterlogged(true); }
		}
		return changed;
	}



	public static boolean isSign(final Material type) {
		if (isWallSign(type))        return true;
		if (isStandingSign(type))    return true;
		if (isHangingSign(type))     return true;
		if (isHangingWallSign(type)) return true;
		return false;
	}
	public static boolean isWallSign(final Material type) {
		switch (type) {
		case ACACIA_WALL_SIGN:
		case BAMBOO_WALL_SIGN:
		case BIRCH_WALL_SIGN:
		case CRIMSON_WALL_SIGN:
		case DARK_OAK_WALL_SIGN:
		case JUNGLE_WALL_SIGN:
		case MANGROVE_WALL_SIGN:
		case OAK_WALL_SIGN:
		case SPRUCE_WALL_SIGN:
		case WARPED_WALL_SIGN:
			return true;
		default: break;
		}
		return false;
	}
	public static boolean isStandingSign(final Material type) {
		switch (type) {
		case ACACIA_SIGN:
		case BAMBOO_SIGN:
		case BIRCH_SIGN:
		case CRIMSON_SIGN:
		case DARK_OAK_SIGN:
		case JUNGLE_SIGN:
		case MANGROVE_SIGN:
		case OAK_SIGN:
		case SPRUCE_SIGN:
		case WARPED_SIGN:
			return true;
		default: break;
		}
		return false;
	}
	public static boolean isHangingSign(final Material type) {
		switch (type) {
		case ACACIA_HANGING_SIGN:
		case BAMBOO_HANGING_SIGN:
		case BIRCH_HANGING_SIGN:
		case CRIMSON_HANGING_SIGN:
		case DARK_OAK_HANGING_SIGN:
		case JUNGLE_HANGING_SIGN:
		case MANGROVE_HANGING_SIGN:
		case OAK_HANGING_SIGN:
		case SPRUCE_HANGING_SIGN:
		case WARPED_HANGING_SIGN:
			return true;
		default: break;
		}
		return false;
	}
	public static boolean isHangingWallSign(final Material type) {
		switch (type) {
		case ACACIA_WALL_HANGING_SIGN:
		case BAMBOO_WALL_HANGING_SIGN:
		case BIRCH_WALL_HANGING_SIGN:
		case CRIMSON_WALL_HANGING_SIGN:
		case DARK_OAK_WALL_HANGING_SIGN:
		case JUNGLE_WALL_HANGING_SIGN:
		case MANGROVE_WALL_HANGING_SIGN:
		case OAK_WALL_HANGING_SIGN:
		case SPRUCE_WALL_HANGING_SIGN:
		case WARPED_WALL_HANGING_SIGN:
			return true;
		default: break;
		}
		return false;
	}



}
