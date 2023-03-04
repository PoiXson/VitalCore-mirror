package com.poixson.commonmc.utils;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
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



}
