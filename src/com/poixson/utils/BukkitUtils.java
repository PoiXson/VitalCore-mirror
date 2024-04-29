package com.poixson.utils;

import static com.poixson.tools.xJavaPlugin.Log;
import static com.poixson.utils.Utils.IsEmpty;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapView;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

import com.poixson.tools.Keeper;


public final class BukkitUtils {
	private BukkitUtils() {}
	static { Keeper.add(new BukkitUtils()); }



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
		for (final Player player : world.getPlayers())
			player.sendMessage(msg);
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



	@SuppressWarnings("deprecation")
	public static MapView GetMapView(final int mapid) {
		return Bukkit.getMap(mapid);
	}

	@SuppressWarnings("deprecation")
	public static void SetMapID(final ItemStack map, final int id) {
		final MapMeta meta = (MapMeta) map.getItemMeta();
		meta.setMapId(id);
		map.setItemMeta(meta);
	}



	@SuppressWarnings("deprecation")
	public static Color NearestMapColor(final Color color) {
		return MapPalette.getColor(MapPalette.matchColor(color));
	}
	public static int NearestMapColor(final int color) {
		return NearestMapColor(new Color(color)).getRGB();
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
		final long freed = mem - Runtime.getRuntime().freeMemory();
		// >10MB
		final int freed_mb = (int) (freed / 1024L / 1024L);
		if (freed_mb >= 10)
			Log().info(String.format("Freed memory: %dMB", Integer.valueOf(freed_mb)));
		return freed_mb;
	}



	// -------------------------------------------------------------------------------
	// commands



	public static PluginCommand GetCommand(final JavaPlugin plugin, final String namespace,
			final String[] labels, final String desc, final String usage, final String perm) {
		if (IsEmpty(namespace))
			return GetCommand(plugin, labels[0], labels, desc, usage, perm);
		final LinkedList<String> list = new LinkedList<String>();
		for (final String label : labels)
			list.addLast(label);
		final String first = list.removeFirst();
		PluginCommand plugin_command;
		// existing command
		plugin_command = plugin.getCommand(first);
		// register new command
		if (plugin_command == null)
			plugin_command = NewCommand(plugin, namespace, labels);
		if (plugin_command == null)
			return null;
		if (!IsEmpty(list)) plugin_command.setAliases(list);
		if (!IsEmpty(desc)) plugin_command.setDescription(desc);
		if (IsEmpty(plugin_command.getUsage())) {
			if (IsEmpty(usage)) plugin_command.setUsage("Invalid command");
			else                plugin_command.setUsage(usage);
		}
		if (!IsEmpty(perm)) plugin_command.setPermission(perm);
		return plugin_command;
	}

	public static PluginCommand NewCommand(final Plugin plugin,
			final String namespace, final String[] labels) {
		if (IsEmpty(namespace))
			return NewCommand(plugin, labels[0], labels);
		try {
			final Constructor<PluginCommand> construct =
				PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
			construct.setAccessible(true);
			final PluginManager manager = Bukkit.getPluginManager();
			if (manager instanceof SimplePluginManager) {
				final Field field = SimplePluginManager.class.getDeclaredField("commandMap");
				field.setAccessible(true);
				final CommandMap map = (CommandMap) field.get(Bukkit.getPluginManager());
				final ArrayList<String> list = new ArrayList<String>();
				int index = 0;
				final String first = labels[0];
				for (final String label : labels) {
					if (index++ == 0) continue;
					list.add(label);
				}
				final PluginCommand plugin_command = construct.newInstance(first, plugin);
				if (!IsEmpty(list))
					plugin_command.setAliases(list);
				map.register(namespace, plugin_command);
				return plugin_command;
			}
		} catch (NoSuchFieldException      e) { throw new RuntimeException(e);
		} catch (NoSuchMethodException     e) { throw new RuntimeException(e);
		} catch (SecurityException         e) { throw new RuntimeException(e);
		} catch (InstantiationException    e) { throw new RuntimeException(e);
		} catch (IllegalAccessException    e) { throw new RuntimeException(e);
		} catch (IllegalArgumentException  e) { throw new RuntimeException(e);
		} catch (InvocationTargetException e) { throw new RuntimeException(e); }
		return null;
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
		final PluginManager pm = Bukkit.getPluginManager();
		pm.callEvent(event);
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
		final PluginManager pm = Bukkit.getPluginManager();
		pm.callEvent(event);
		if (!event.isCancelled()) {
			player.setFoodLevel(Math.min(event.getFoodLevel(), 20));
			player.setSaturation(10f);
			player.setExhaustion( 0f);
		}
	}



}
