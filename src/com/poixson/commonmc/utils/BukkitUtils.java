package com.poixson.commonmc.utils;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Axis;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Lightable;
import org.bukkit.block.data.Openable;
import org.bukkit.block.data.Orientable;
import org.bukkit.block.data.Rotatable;
import org.bukkit.block.data.type.Slab;
import org.bukkit.entity.Player;
import org.bukkit.map.MapView;
import org.bukkit.potion.PotionEffect;

import com.poixson.tools.Keeper;
import com.poixson.utils.Utils;


public final class BukkitUtils {
	private BukkitUtils() {}
	static { Keeper.add(new BukkitUtils()); }

	public static ConcurrentHashMap<String, WeakReference<BlockData>> blocksCache =
			new ConcurrentHashMap<String, WeakReference<BlockData>>();



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
		for (final Player player : world.getPlayers()) {
			player.sendMessage(msg);
		}
	}



	public static World GetPlayerWorld(final UUID uuid) {
		final Player player = Bukkit.getPlayer(uuid);
		if (player == null) return null;
		return player.getWorld();
	}



	public static Location LocationFromArray(final World world, final int[] tup) {
		if (tup.length != 3) {
			(new RuntimeException("Invalid array length")).printStackTrace();
			return null;
		}
		final Block block = world.getBlockAt(tup[0], tup[1], tup[2]);
		if (block == null) {
			(new RuntimeException("Failed to find block location")).printStackTrace();
			return null;
		}
		return block.getLocation();
	}
	public static Location[] LocationsFromArrays(final World world, final int[][] tups) {
		final LinkedList<Location> list = new LinkedList<Location>();
		for (final int[] arr : tups) {
			final Location loc = LocationFromArray(world, arr);
			if (loc != null)
				list.add(loc);
		}
		return list.toArray(new Location[0]);
	}



	@SuppressWarnings("deprecation")
	public static MapView GetMapView(final int mapid) {
		return Bukkit.getMap(mapid);
	}



	public static BlockData ParseBlockType(final String typeStr) {
		// cached
		{
			final WeakReference<BlockData> ref = blocksCache.get(typeStr);
			if (ref != null) {
				final BlockData block = ref.get();
				if (block != null)
					return block;
				blocksCache.remove(typeStr);
			}
		}

		final String[] parts = typeStr.split("[\\[]", 2);
		final Material mat = Material.matchMaterial(parts[0]);
		if (mat == null)
			return null;
		final BlockData data = mat.createBlockData();
		if (parts.length < 2)
			return data;
		final String paramsStr = parts[1].substring(0, parts[1].length()-1);
		final HashMap<String, String> params = new HashMap<String, String>();
		for (final String parm : paramsStr.split("[,;]")) {
			final String[] prm = parm.split("[=]", 2);
			params.put(prm[0], prm[1]);
		}

		// directional
		if (data instanceof Directional) {
			if (params.containsKey("facing")) {
				final String val = params.get("facing");
				switch (val) {
				case "north": case "n": ((Directional)data).setFacing(BlockFace.NORTH); params.remove("facing"); break;
				case "south": case "s": ((Directional)data).setFacing(BlockFace.SOUTH); params.remove("facing"); break;
				case "east":  case "e": ((Directional)data).setFacing(BlockFace.EAST ); params.remove("facing"); break;
				case "west":  case "w": ((Directional)data).setFacing(BlockFace.WEST ); params.remove("facing"); break;
				default: break;
				}
			}
		}

		// rotatable
		if (data instanceof Rotatable) {
			if (params.containsKey("rotation")) {
				final String val = params.get("rotation");
				switch (val) {
				case "north":           case "n":   ((Rotatable)data).setRotation(BlockFace.NORTH);            params.remove("rotation"); break;
				case "north-northeast": case "nne": ((Rotatable)data).setRotation(BlockFace.NORTH_NORTH_EAST); params.remove("rotation"); break;
				case "northeast":       case "ne":  ((Rotatable)data).setRotation(BlockFace.NORTH_EAST);       params.remove("rotation"); break;
				case "east-northeast":  case "ene": ((Rotatable)data).setRotation(BlockFace.EAST_NORTH_EAST);  params.remove("rotation"); break;
				case "east":            case "e":   ((Rotatable)data).setRotation(BlockFace.EAST);             params.remove("rotation"); break;
				case "east-southeast":  case "ese": ((Rotatable)data).setRotation(BlockFace.EAST_SOUTH_EAST);  params.remove("rotation"); break;
				case "southeast":       case "se":  ((Rotatable)data).setRotation(BlockFace.SOUTH_EAST);       params.remove("rotation"); break;
				case "south-southeast": case "sse": ((Rotatable)data).setRotation(BlockFace.SOUTH_SOUTH_EAST); params.remove("rotation"); break;
				case "south":           case "s":   ((Rotatable)data).setRotation(BlockFace.SOUTH);            params.remove("rotation"); break;
				case "south-southwest": case "ssw": ((Rotatable)data).setRotation(BlockFace.SOUTH_SOUTH_WEST); params.remove("rotation"); break;
				case "southwest":       case "sw":  ((Rotatable)data).setRotation(BlockFace.SOUTH_WEST);       params.remove("rotation"); break;
				case "west-southwest":  case "wsw": ((Rotatable)data).setRotation(BlockFace.WEST_SOUTH_WEST);  params.remove("rotation"); break;
				case "west":            case "w":   ((Rotatable)data).setRotation(BlockFace.WEST);             params.remove("rotation"); break;
				case "west-northwest":  case "wnw": ((Rotatable)data).setRotation(BlockFace.WEST_NORTH_WEST);  params.remove("rotation"); break;
				case "northwest":       case "nw":  ((Rotatable)data).setRotation(BlockFace.NORTH_WEST);       params.remove("rotation"); break;
				case "north-northwest": case "nnw": ((Rotatable)data).setRotation(BlockFace.NORTH_NORTH_WEST); params.remove("rotation"); break;
				default: break;
				}
			}
		}

		// trapdoor top/bottom
		if (data instanceof Bisected) {
			final String val = params.get("half");
			switch (val) {
			case "top":                ((Bisected)data).setHalf(Half.TOP);    params.remove("half"); break;
			case "bot": case "bottom": ((Bisected)data).setHalf(Half.BOTTOM); params.remove("half"); break;
			default: break;
			}
		}

		// slab
		if (data instanceof Slab) {
			if (params.containsKey("type")) {
				final String val = params.get("type");
				switch (val) {
				case "top":    case "t": case "up":   ((Slab)data).setType(Slab.Type.TOP   ); params.remove("type"); break;
				case "bottom": case "b": case "down": ((Slab)data).setType(Slab.Type.BOTTOM); params.remove("type"); break;
				case "double": case "d": case "full": ((Slab)data).setType(Slab.Type.DOUBLE); params.remove("type"); break;
				default: break;
				}
			}
		}

		// trapdoor open/closed
		if (data instanceof Openable) {
			if (params.containsKey("open")) {
				final String val = params.get("open");
				switch (val) {
				case "true":  case "open":   ((Openable)data).setOpen(true);  params.remove("open"); break;
				case "false": case "closed": ((Openable)data).setOpen(false); params.remove("open"); break;
				default: break;
				}
			}
		}

		// lamp on/off
		if (data instanceof Lightable) {
			if (params.containsKey("lit")) {
				final String val = params.get("lit");
				switch (val) {
				case "true":  case "on":  ((Lightable)data).setLit(true);  params.remove("lit"); break;
				case "false": case "off": ((Lightable)data).setLit(false); params.remove("lit"); break;
				default: break;
				}
			}
		}

		// chain axis
		if (data instanceof Orientable) {
			if (params.containsKey("axis")) {
				final String val = params.get("axis");
				switch (val) {
				case "x": case "ew": ((Orientable)data).setAxis(Axis.X); params.remove("axis"); break;
				case "y": case "ud": ((Orientable)data).setAxis(Axis.Y); params.remove("axis"); break;
				case "z": case "ns": ((Orientable)data).setAxis(Axis.Z); params.remove("axis"); break;
				default: break;
				}
			}
		}

		// unknown parameter
		for (final Entry<String, String> entry : params.entrySet()) {
			System.out.println("Unknown block parameter: " + entry.getKey() + "=" + entry.getValue());
		}

		// add to cache
		{
			final WeakReference<BlockData> ref = new WeakReference<BlockData>(data);
			final WeakReference<BlockData> existing = blocksCache.putIfAbsent(typeStr, ref);
			if (existing == null)
				return data;
			{
				final BlockData block = existing.get();
				if (block != null)
					return block;
			}
			return ParseBlockType(typeStr);
		}
	}



}
