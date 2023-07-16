package com.poixson.commonmc.tools.scripts.screen;

import static com.poixson.commonmc.utils.ItemUtils.SetMapID;
import static com.poixson.commonmc.utils.MapUtils.NearestMapColor;

import java.awt.Color;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Rotation;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.GlowItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.map.MapView.Scale;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.poixson.commonmc.pxnCommonPlugin;
import com.poixson.commonmc.utils.BukkitUtils;
import com.poixson.tools.dao.Iabcd;


public class MapScreen extends MapRenderer implements Runnable, Closeable {
	public static final int    DEFAULT_NEAREST_CHECK_FRAMES = 5;
	public static final double DEFAULT_MAX_DISTANCE         = 5.0;

	protected final JavaPlugin plugin;

	public final AtomicReference<BufferedImage> img_screen_mask = new AtomicReference<BufferedImage>(null);

	public final int  map_id;
	public final int  map_size;

	public final Location  loc;
	public final BlockFace facing;
	public final ItemStack map;
	public final MapView view;
	public final GlowItemFrame frame;

	protected final PixelSource pixel_source;

	protected final BukkitRunnable run;
	protected final AtomicBoolean closed = new AtomicBoolean(false);

	protected final AtomicReference<Player> nearest_player = new AtomicReference<Player>(null);
	protected final AtomicInteger           nearest_check  = new AtomicInteger(0);



	public MapScreen(final JavaPlugin plugin, final int map_id,
			final Location loc, final BlockFace facing,
			final int map_size, final PixelSource source) {
		this.plugin = plugin;
		this.map_id = map_id;
		this.map_size = map_size;
		this.loc    = loc;
		this.facing = facing;
		this.pixel_source = source;
		// map in frame
		this.frame = (GlowItemFrame) loc.getWorld().spawnEntity(loc, EntityType.GLOW_ITEM_FRAME);
		this.frame.setRotation(Rotation.NONE);
		this.frame.setFacingDirection(facing);
		this.frame.setFixed(true);
		this.frame.setPersistent(true);
		this.frame.setInvulnerable(true);
		this.frame.setVisible(false);
		this.map = new ItemStack(Material.FILLED_MAP, 1);
		SetMapID(this.map, this.map_id);
		this.frame.setItem(this.map);
		// map view
		this.view = BukkitUtils.GetMapView(map_id);
		if (this.view == null) throw new RuntimeException(String.format("Failed to get map view: %d", map_id));
//TODO: not working properly
		this.view.setScale(Scale.FARTHEST);
//		this.view.setScale(Scale.CLOSEST);
		this.view.setTrackingPosition(false);
		this.view.setCenterX(0);
		this.view.setCenterZ(0);
		this.view.setLocked(true);
		for (final MapRenderer render : this.view.getRenderers())
			this.view.removeRenderer(render);
		this.view.addRenderer(this);
		this.run = new BukkitRunnable() {
			@Override
			public void run() {
				MapScreen.this.run();
			}
		};
	}



	public void start(final int fps) {
		this.findNearestPlayer();
		if (fps > 1) {
			final int rate = Math.floorDiv(20, fps);
			this.run.runTaskTimer(this.plugin, 2L, rate);
		}
	}

//TODO: remember to close and free maps
	@Override
	public void close() {
		try {
			this.run.cancel();
		} catch (IllegalStateException ignore) {}
		if (!this.closed.getAndSet(true)) {
			this.frame.setItem(null);
			this.frame.remove();
			pxnCommonPlugin.GetFreedMapStore().release(this.map_id);
		}
	}



	@Override
	public void run() {
		this.nearest_check.incrementAndGet();
		final Player player = this.findNearestPlayer();
		if (player != null)
			player.sendMap(this.view);
	}

	@Override
	public void render(final MapView view, final MapCanvas canvas, final Player player) {
		if (this.closed.get()) return;
		final Image img_mask = this.img_screen_mask.get();
		if (img_mask == null) {
			for (int iy=0; iy<this.map_size; iy++) {
				for (int ix=0; ix<this.map_size; ix++)
					canvas.setPixelColor(ix, iy, Color.BLACK);
			}
		} else {
			canvas.drawImage(0, 0, img_mask);
		}
		// run script
		final Color[][] pixels = this.pixel_source.getPixels();
		// draw map screen
		{
			Color c;
			for (int iy=0; iy<this.map_size; iy++) {
				for (int ix=0; ix<this.map_size; ix++) {
					c = canvas.getPixelColor(ix, iy);
					if (c != null) {
						if (c.getTransparency() == Transparency.OPAQUE) {
							if (pixels[iy][ix] == null) canvas.setPixelColor(ix, iy, Color.BLACK);
							else                        canvas.setPixelColor(ix, iy, NearestMapColor(pixels[iy][ix]));
						}
					}
				}
			}
		}
//TODO
//canvas.drawText(15, 15, MinecraftFont.Font, ChatColor.BLUE + "Abcdefg;");
	}



	public static void DrawImagePixels(final Color[][] pixels,
			final BufferedImage img, final int x, final int y) {
		DrawImagePixels(pixels, img, null, x, y);
	}
	public static void DrawImagePixels(final Color[][] pixels,
			final BufferedImage img, final BufferedImage mask,
			final int x, final int y) {
		final int w = pixels[0].length - 1;
		final int h = pixels.length    - 1;
		final int img_w = img.getWidth(null);
		final int img_h = img.getHeight(null);
		int xx, yy;
		for (int iy=0; iy<img_h; iy++) {
			yy = iy + y;
			for (int ix=0; ix<img_w; ix++) {
				xx = ix + x;
				if (xx >= 0 && xx < w
				&&  yy >= 0 && yy < h) {
					if (mask == null
					|| Color.WHITE.equals(new Color(mask.getRGB(ix, iy))))
						pixels[iy+y][ix+x] = new Color(img.getRGB(ix, iy));
//TODO: remove this?
//System.out.println( img.getColorModel().hasAlpha() ? "YES" : "NO" );
//					if (!Color.BLACK.equals(color))
//					if (color.getTransparency() == Transparency.OPAQUE)
//System.out.println(color.getAlpha());
//					if (color.getAlpha() >= 255)
				}
			}
		}
	}



	public Iabcd findScreenSize() {
		int min_x = Integer.MAX_VALUE;
		int min_y = Integer.MAX_VALUE;
		int max_x = Integer.MIN_VALUE;
		int max_y = Integer.MIN_VALUE;
		{
			final BufferedImage img = this.img_screen_mask.get();
			final int half_w = 64;
			final int half_h = 64;
			for (int ix=0; ix<half_w; ix++) {
				if (Color.BLACK.equals(new Color(img.getRGB(half_w-ix, half_h)))) {
					min_x = (half_w - ix) + 1;
					break;
				}
			}
			for (int ix=0; ix<half_w; ix++) {
				if (Color.BLACK.equals(new Color(img.getRGB(half_w+ix, half_h)))) {
					max_x = half_w + ix;
					break;
				}
			}
			for (int iy=0; iy<half_h; iy++) {
				if (Color.BLACK.equals(new Color(img.getRGB(half_w, half_h-iy)))) {
					min_y = (half_h - iy) + 1;
					break;
				}
			}
			for (int iy=0; iy<half_h; iy++) {
				if (Color.BLACK.equals(new Color(img.getRGB(half_w, half_h+iy)))) {
					max_y = half_h + iy;
					break;
				}
			}
		}
		return new Iabcd(min_x, min_y, max_x-min_x, max_y-min_y);
	}



	public Player findNearestPlayer() {
		if (this.nearest_check.get() >= DEFAULT_NEAREST_CHECK_FRAMES) {
			this.nearest_check.set(0);
			final World world = this.loc.getWorld();
			double nearest_dist   = Double.MAX_VALUE;
			Player nearest_player = null;
			double dist;
			for (final Player player : Bukkit.getOnlinePlayers()) {
				if (world.equals(player.getWorld())) {
					dist = this.loc.distance(player.getLocation());
					if (nearest_dist > dist) {
						nearest_dist   = dist;
						nearest_player = player;
					}
				}
			}
			if (nearest_dist <= DEFAULT_MAX_DISTANCE) {
				this.nearest_player.set(nearest_player);
				return nearest_player;
			} else {
				this.nearest_player.set(null);
				return null;
			}
		}
		return this.nearest_player.get();
	}



}
