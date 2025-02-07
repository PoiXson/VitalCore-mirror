package com.poixson.utils;

import static com.poixson.utils.ArrayUtils.ssArrayToMap;
import static com.poixson.utils.StringUtils.ss_ReplaceTags;
import static com.poixson.utils.Utils.IsEmpty;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.poixson.tools.Keeper;


public final class BlockUtils {
	private BlockUtils() {}
	static { Keeper.Add(new BlockUtils()); }



	public static boolean IsEmptyOrAir(final Material type) {
		return (type==null ? true : type.isEmpty());
	}
	public static boolean IsEmptyOrAir(final ItemStack stack) {
		return (stack==null ? true : stack.isEmpty());
	}



	public static boolean EqualsBlock(final BlockData expect, final BlockData actual) {
		if (expect == null || actual == null)
			return false;
		final Material expect_mat = expect.getMaterial();
		final Material actual_mat = actual.getMaterial();
		if (expect_mat == null || actual_mat == null)
			return false;
		if (!expect_mat.equals(actual_mat))
			return false;
//TODO: more checks (more in BlockPlotter->isType())
		return true;
	}
	public static boolean EqualsMaterialBlock(final Material expect, final BlockData actual) {
		return EqualsBlockMaterial(actual, expect);
	}
	public static boolean EqualsBlockMaterial(final BlockData expect, final Material actual) {
		if (expect == null || actual == null)
			return false;
		final Material expect_mat = expect.getMaterial();
		if (expect_mat == null)
			return false;
//TODO: more checks (more in BlockPlotter->isType())
		return expect_mat.equals(actual);
	}
	public static boolean EqualsMaterial(final Material expect, final Material actual) {
		if (expect == null || actual == null)
			return false;
		return expect.equals(actual);
	}



	public static BlockData StringToBlockDataDef(final String mat, final String def, final String...tags) {
		return StringToBlockData((IsEmpty(mat) ? def : mat), tags);
	}
	public static BlockData StringToBlockData(final String mat, final String...tags) {
		if (IsEmpty(mat)) return null;
		return Bukkit.createBlockData(ss_ReplaceTags("<%s>", mat, ssArrayToMap(tags)));
	}
	public static Material StringToMaterial(final String mat, final String def) {
		if (IsEmpty(mat)) return (def==null ? null : StringToMaterial(def, null));
		final int pos = mat.indexOf('[');
		return (pos==-1 ? Material.matchMaterial(mat) : StringToMaterial(mat.substring(0, pos-1), null));
	}



	// custom item model
	public static int GetCustomModel(final ItemStack stack) {
		if (!IsEmptyOrAir(stack)) {
			if (stack.hasItemMeta()) {
				final ItemMeta meta = stack.getItemMeta();
				if (meta.hasCustomModelData())
					return meta.getCustomModelData();
			}
		}
		return 0;
	}



	// persistent leaves
	public static boolean GetLeavesPersistent(final BlockData block) {
		if (block == null) throw new NullPointerException();
		if (block instanceof org.bukkit.block.data.type.Leaves) {
			return ((org.bukkit.block.data.type.Leaves) block)
					.isPersistent();
		}
		return false;
	}
	public static void SetLeavesPersistent(final BlockData block) {
		SetLeavesPersistent(block, true);
	}
	public static void SetLeavesPersistent(final BlockData block, final boolean persist) {
		if (block == null) throw new NullPointerException();
		if (block instanceof org.bukkit.block.data.type.Leaves) {
			((org.bukkit.block.data.type.Leaves) block)
				.setPersistent(persist);
		}
	}

	// leaves distance
	public static int GetLeavesDistance(final BlockData block) {
		if (block == null) throw new NullPointerException();
		if (block instanceof org.bukkit.block.data.type.Leaves) {
			return ((org.bukkit.block.data.type.Leaves) block)
					.getDistance();
		}
		return -1;
	}
	public static void SetLeavesDistance(final BlockData block, final int distance) {
		if (block == null) throw new NullPointerException();
		if (block instanceof org.bukkit.block.data.type.Leaves) {
			((org.bukkit.block.data.type.Leaves) block)
				.setDistance(distance);
		}
	}



	// -------------------------------------------------------------------------------
	// block types



	public static boolean IsSign(final Material type) {
		if (IsWallSign(       type)) return true;
		if (IsStandingSign(   type)) return true;
		if (IsHangingSign(    type)) return true;
		if (IsHangingWallSign(type)) return true;
		return false;
	}
	public static boolean IsSign(final BlockData block) {
		if (IsWallSign(       block)) return true;
		if (IsStandingSign(   block)) return true;
		if (IsHangingSign(    block)) return true;
		if (IsHangingWallSign(block)) return true;
		return false;
	}

	public static boolean IsWallSign(final Material type) {
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
	public static boolean IsWallSign(final BlockData block) {
		return (block instanceof org.bukkit.block.data.type.WallSign);
	}

	public static boolean IsStandingSign(final Material type) {
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
	public static boolean IsStandingSign(final BlockData block) {
		return (block instanceof org.bukkit.block.data.type.Sign);
	}

	public static boolean IsHangingSign(final Material type) {
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
	public static boolean IsHangingSign(final BlockData block) {
		return (block instanceof org.bukkit.block.data.type.HangingSign);
	}

	public static boolean IsHangingWallSign(final Material type) {
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
	public static boolean IsHangingWallSign(final BlockData block) {
		return (block instanceof org.bukkit.block.data.type.WallHangingSign);
	}



	public static boolean IsButton(final Material type) {
		switch (type) {
		case STONE_BUTTON:
		case OAK_BUTTON:
		case SPRUCE_BUTTON:
		case BIRCH_BUTTON:
		case JUNGLE_BUTTON:
		case ACACIA_BUTTON:
		case DARK_OAK_BUTTON:
		case MANGROVE_BUTTON:
		case CHERRY_BUTTON:
		case BAMBOO_BUTTON:
		case CRIMSON_BUTTON:
		case WARPED_BUTTON:
		case POLISHED_BLACKSTONE_BUTTON:
			return true;
		default: break;
		}
		return false;
	}
	public static boolean IsButton(final BlockData block) {
		return (block==null ? false : IsButton(block.getMaterial()));
	}



	public static boolean IsPuressurePlate(final Material type) {
		switch (type) {
		case HEAVY_WEIGHTED_PRESSURE_PLATE:
		case LIGHT_WEIGHTED_PRESSURE_PLATE:
		case STONE_PRESSURE_PLATE:
		case OAK_PRESSURE_PLATE:
		case SPRUCE_PRESSURE_PLATE:
		case BIRCH_PRESSURE_PLATE:
		case JUNGLE_PRESSURE_PLATE:
		case ACACIA_PRESSURE_PLATE:
		case DARK_OAK_PRESSURE_PLATE:
		case MANGROVE_PRESSURE_PLATE:
		case CHERRY_PRESSURE_PLATE:
		case BAMBOO_PRESSURE_PLATE:
		case CRIMSON_PRESSURE_PLATE:
		case WARPED_PRESSURE_PLATE:
		case POLISHED_BLACKSTONE_PRESSURE_PLATE:
			return true;
		default: break;
		}
		return false;
	}
	public static boolean IsPuressurePlate(final BlockData block) {
		return (block==null ? false : IsPuressurePlate(block.getMaterial()));
	}



	public static boolean IsCopper(final Material type) {
		switch (type) {
		case COPPER_BLOCK:      case COPPER_INGOT:              case RAW_COPPER:                 case WAXED_COPPER_BLOCK:      case RAW_COPPER_BLOCK:                case COPPER_ORE:                       case DEEPSLATE_COPPER_ORE:
		case COPPER_BULB:       case EXPOSED_COPPER_BULB:       case OXIDIZED_COPPER_BULB:       case WAXED_COPPER_BULB:       case WAXED_EXPOSED_COPPER_BULB:       case WAXED_OXIDIZED_COPPER_BULB:       case WAXED_WEATHERED_COPPER_BULB:       case WEATHERED_COPPER_BULB:
		case COPPER_GRATE:      case EXPOSED_COPPER_GRATE:      case OXIDIZED_COPPER_GRATE:      case WAXED_COPPER_GRATE:      case WAXED_EXPOSED_COPPER_GRATE:      case WAXED_OXIDIZED_COPPER_GRATE:      case WAXED_WEATHERED_COPPER_GRATE:      case WEATHERED_COPPER_GRATE:
		case COPPER_DOOR:       case EXPOSED_COPPER_DOOR:       case OXIDIZED_COPPER_DOOR:       case WAXED_COPPER_DOOR:       case WAXED_EXPOSED_COPPER_DOOR:       case WAXED_OXIDIZED_COPPER_DOOR:       case WAXED_WEATHERED_COPPER_DOOR:       case WEATHERED_COPPER_DOOR:
		case COPPER_TRAPDOOR:   case EXPOSED_COPPER_TRAPDOOR:   case OXIDIZED_COPPER_TRAPDOOR:   case WAXED_COPPER_TRAPDOOR:   case WAXED_EXPOSED_COPPER_TRAPDOOR:   case WAXED_OXIDIZED_COPPER_TRAPDOOR:   case WAXED_WEATHERED_COPPER_TRAPDOOR:   case WEATHERED_COPPER_TRAPDOOR:
		case CUT_COPPER_SLAB:   case EXPOSED_CUT_COPPER_SLAB:   case OXIDIZED_CUT_COPPER_SLAB:   case WAXED_CUT_COPPER_SLAB:   case WAXED_EXPOSED_CUT_COPPER_SLAB:   case WAXED_OXIDIZED_CUT_COPPER_SLAB:   case WAXED_WEATHERED_CUT_COPPER_SLAB:   case WEATHERED_CUT_COPPER_SLAB:
		case CUT_COPPER_STAIRS: case EXPOSED_CUT_COPPER_STAIRS: case OXIDIZED_CUT_COPPER_STAIRS: case WAXED_CUT_COPPER_STAIRS: case WAXED_EXPOSED_CUT_COPPER_STAIRS: case WAXED_OXIDIZED_CUT_COPPER_STAIRS: case WAXED_WEATHERED_CUT_COPPER_STAIRS: case WEATHERED_CUT_COPPER_STAIRS:
			return true;
		default: break;
		}
		return false;
	}
	public static boolean IsCopper(final BlockData block) {
		return (block==null ? false : IsCopper(block.getMaterial()));
	}



	public static boolean IsCopperBulb(final Material type) {
		switch (type) {
		case COPPER_BULB:       case EXPOSED_COPPER_BULB:       case OXIDIZED_COPPER_BULB:       case WEATHERED_COPPER_BULB:
		case WAXED_COPPER_BULB: case WAXED_EXPOSED_COPPER_BULB: case WAXED_OXIDIZED_COPPER_BULB: case WAXED_WEATHERED_COPPER_BULB:
			return true;
		default: break;
		}
		return false;
	}
	public static boolean IsCopperBulb(final BlockData block) {
		return (block==null ? false : IsCopperBulb(block.getMaterial()));
	}



}
