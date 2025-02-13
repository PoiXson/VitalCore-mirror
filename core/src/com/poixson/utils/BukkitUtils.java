package com.poixson.utils;

import static com.poixson.utils.Utils.IsEmpty;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import com.poixson.tools.Keeper;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;


public final class BukkitUtils {
	private BukkitUtils() {}
	static { Keeper.Add(new BukkitUtils()); }



	public static String ComponentToString(final Component msg) {
		if (msg == null) return null;
		final PlainTextComponentSerializer serial = PlainTextComponentSerializer.plainText();
		return (serial==null ? null : serial.serialize(msg));
	}



	// -------------------------------------------------------------------------------



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
		if (!EqualsWorld(locA, locB))
			return false;
		if (locA.getX() != locB.getX()) return false;
		if (locA.getY() != locB.getY()) return false;
		if (locA.getZ() != locB.getZ()) return false;
		return true;
	}

	public static boolean EqualsWorld(final Location locA, final Location locB) {
		return EqualsWorld(locA.getWorld(), locB.getWorld());
	}
	public static boolean EqualsWorld(final World worldA, final World worldB) {
		if (worldA == null || worldB == null)
			return (worldA == null && worldB == null);
		return EqualsUUID(worldA.getUID(), worldB.getUID());
	}



	public static boolean SafeCancel(final BukkitRunnable run) {
		try {
			run.cancel();
			return true;
		} catch (IllegalStateException ignore) {
			return false;
		}
	}



	// -------------------------------------------------------------------------------
	// file paths



	public static String GetServerPath() {
		final File path = Bukkit.getWorldContainer();
		try {
			return path.getCanonicalPath();
		} catch (IOException ignore) {}
		return path.getAbsolutePath();
	}



	// -------------------------------------------------------------------------------
	// maps



	public static MapView GetMapView(final int mapid) {
		return Bukkit.getMap(mapid);
	}

	@SuppressWarnings("deprecation")
	public static void SetMapID(final ItemStack map, final int id) {
		final MapMeta meta = (MapMeta) map.getItemMeta();
		meta.setMapId(id);
		map.setItemMeta(meta);
	}



	public static void DrawImagePixels(final Color[][] pixels,
			final int x, final int y, final BufferedImage img) {
		DrawImagePixels_ImgMask(pixels, x, y, img, null);
	}
	public static void DrawImagePixels_ImgMask(final Color[][] pixels,
			final int x, final int y, final BufferedImage img,
			final BufferedImage mask) {
		final int img_w = img.getWidth(null);
		final int img_h = img.getHeight(null);
		final int color_white = Color.WHITE.getRGB();
		int xx, yy;
		//LOOP_Y:
		for (int iy=0; iy<img_h; iy++) {
			yy = iy + y;
			LOOP_X:
			for (int ix=0; ix<img_w; ix++) {
				xx = ix + x;
				if (xx < 0 || xx >= pixels[iy].length
				||  yy < 0 || yy >= pixels.length)
					continue LOOP_X;
				if (mask != null
				&&  mask.getRGB(ix, iy) != color_white)
					continue LOOP_X;
				pixels[yy][xx] = new Color(img.getRGB(ix, iy));
			} // end LOOP_X
		} // end LOOP_Y
	}
	public static void DrawImagePixels_ColorMask(final Color[][] pixels,
			final int x, final int y, final BufferedImage img,
			final Color mask) {
		final int img_w = img.getWidth(null);
		final int img_h = img.getHeight(null);
		final int color_mask = mask.getRGB();
		int xx, yy;
		int c;
		//LOOP_Y:
		for (int iy=0; iy<img_h; iy++) {
			yy = iy + y;
			LOOP_X:
			for (int ix=0; ix<img_w; ix++) {
				xx = ix + x;
				if (xx < 0 || xx >= pixels[iy].length
				||  yy < 0 || yy >= pixels.length)
					continue LOOP_X;
				c = img.getRGB(ix, iy);
				if (c != color_mask)
					pixels[yy][xx] = new Color(c);
			} // end LOOP_X
		} // end LOOP_Y
	}



	// -------------------------------------------------------------------------------
	// garbage collection



	public static int GarbageCollect() {
		final long mem = Runtime.getRuntime().freeMemory();
		int count = 0;
		for (final World world : Bukkit.getWorlds()) {
			final Chunk[] chunks = world.getLoadedChunks();
			for (final Chunk chunk : chunks) {
				if (chunk.isForceLoaded())
					continue;
				if (chunk.unload(true))
					count++;
			}
		}
		System.gc();
		if (count > 0)
			Log().info(String.format("Unloaded %d chunks", Integer.valueOf(count)));
		final long freed = Runtime.getRuntime().freeMemory() - mem;
		final int freed_mb = (int) Math.floorDiv(freed, 1024L*1024L);
		if (freed_mb > 0)
			Log().info(String.format("Freed memory: %dMB", Integer.valueOf(freed_mb)));
		return freed_mb;
	}



	// -------------------------------------------------------------------------------
	// gui



	public static void OpenWorkbench(final Player player) {
		player.closeInventory();
		final InventoryView gui = player.openWorkbench(null, true);
		player.openInventory(gui);
	}



	public static void OpenEnderchest(final Player player) {
		player.closeInventory();
		final Inventory chest = player.getEnderChest();
		player.openInventory(chest);
	}



	public static void HealPlayer(final Player player) {
		if (player.getHealth() == 0) return;
		@SuppressWarnings("deprecation")
		final double max_health = player.getMaxHealth();
		final double amount = max_health - player.getHealth();
		final EntityRegainHealthEvent event = new EntityRegainHealthEvent(player, amount, RegainReason.CUSTOM);
		Bukkit.getPluginManager()
			.callEvent(event);
		if (!event.isCancelled()) {
			double amount_new = player.getHealth() + event.getAmount();
			if (amount_new > max_health)
				amount_new = max_health;
			player.setHealth(amount_new);
			if (player.getFoodLevel() < 20)
				player.setFoodLevel(20);
			player.setFireTicks(0);
			for (final PotionEffect effect : player.getActivePotionEffects())
				player.removePotionEffect(effect.getType());
		}
	}



	public static void FeedPlayer(final Player player) {
		final int amount = 30;
		final FoodLevelChangeEvent event = new FoodLevelChangeEvent(player, amount);
		Bukkit.getPluginManager()
			.callEvent(event);
		if (!event.isCancelled()) {
			player.setFoodLevel(Math.min(event.getFoodLevel(), 20));
			player.setSaturation(10f);
			player.setExhaustion( 0f);
		}
	}



	public static void RestPlayer(final Player player) {
		player.setStatistic(Statistic.TIME_SINCE_REST, 0);
	}



	public static void AllowFlyPlayer(final Player player, final boolean can_fly) {
		player.setAllowFlight(can_fly);
		if (can_fly) player.setFallDistance(0f);
		else         player.setFlying(false);
	}



	// -------------------------------------------------------------------------------
	// game mode



	public static char GameModeToChar(final GameMode mode) {
		switch (mode) {
		case CREATIVE:  return 'c';
		case SURVIVAL:  return 's';
		case ADVENTURE: return 'a';
		case SPECTATOR: return 'p';
		default: break;
		}
		return 0;
	}
	public static GameMode CharToGameMode(final String arg) {
		if (IsEmpty(arg)) return null;
		final String lower = arg.toLowerCase();
		if (lower.startsWith("sp")
		||  lower.startsWith("p")) return GameMode.SPECTATOR;
		if (lower.startsWith("c")) return GameMode.CREATIVE;
		if (lower.startsWith("s")) return GameMode.SURVIVAL;
		if (lower.startsWith("a")) return GameMode.ADVENTURE;
		return null;
	}
	public static GameMode CharToGameMode(final char arg) {
		switch (arg) {
		case 'p': return GameMode.SPECTATOR;
		case 'c': return GameMode.CREATIVE;
		case 's': return GameMode.SURVIVAL;
		case 'a': return GameMode.ADVENTURE;
		default: break;
		}
		return null;
	}



	// -------------------------------------------------------------------------------
	// logger



	public static Logger Log() {
		return Logger.getLogger("Minecraft");
	}



}
