package com.poixson.tools.screen;

import static com.poixson.tools.screen.ScreenComposite.MAP_SIZE;
import static com.poixson.utils.BukkitUtils.GetMapView;
import static com.poixson.utils.BukkitUtils.SetMapID;
import static com.poixson.vitalcore.VitalCorePlugin.GetFreedMapStore;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Rotation;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.GlowItemFrame;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import com.poixson.tools.FreedMapStore;
import com.poixson.tools.dao.Iabcd;


public class ScreenPanel extends MapRenderer implements Closeable {
	public static final boolean DEBUG = false;

	protected final Location location;
	protected final BlockFace facing;

	protected final int screen_x, screen_y;
	protected final PixelSource source;

	protected final AtomicReference<BufferedImage> mask = new AtomicReference<BufferedImage>(null);
	protected final AtomicReference<Iabcd> mask_area = new AtomicReference<Iabcd>(null);

	protected final boolean perplayer;

	protected final int       map_id;
	protected final ItemStack map;
	protected final MapView   view;
	protected final ItemFrame frame;

	protected final AtomicBoolean stopping = new AtomicBoolean(false);



	public ScreenPanel(final Location location, final BlockFace facing,
			final int screen_x, final int screen_y,
			final PixelSource source, final boolean perplayer) {
		super(perplayer);
		this.location  = location;
		this.facing    = facing;
		this.screen_x  = screen_x;
		this.screen_y  = screen_y;
		this.source    = source;
		this.perplayer = perplayer;
		// map
		final FreedMapStore store = GetFreedMapStore();
		this.map_id = store.grab();
		this.map = new ItemStack(Material.FILLED_MAP, 1);
		SetMapID(this.map, this.map_id);
		// item frame
		this.frame = (GlowItemFrame) location.getWorld().spawnEntity(location, EntityType.GLOW_ITEM_FRAME);
		this.frame.setRotation(Rotation.NONE);
		this.frame.setFacingDirection(facing);
		this.frame.setFixed(true);
		this.frame.setPersistent(true);
		this.frame.setInvulnerable(true);
		if (!DEBUG)
			this.frame.setVisible(false);
		this.frame.setItem(this.map);
		// map view
		this.view = GetMapView(this.map_id);
		if (this.view == null) throw new RuntimeException("Failed to get map view: "+Integer.toString(this.map_id));
		this.view.setLocked(true);
		this.view.setTrackingPosition(false);
		this.view.setCenterX(0);
		this.view.setCenterZ(0);
		this.view.setWorld(location.getWorld());
		for (final MapRenderer render : this.view.getRenderers())
			this.view.removeRenderer(render);
		this.view.addRenderer(this);
	}



	public void send(final Player player) {
		if (this.stopping.get()) return;
		player.sendMap(this.view);
	}

	@Override
	public void render(final MapView map, final MapCanvas canvas, final Player player) {
		if (this.stopping.get()) return;
		final Color[][] pixels = this.source.getPixels();
		if (pixels != null) {
			final int base_x = this.screen_x * MAP_SIZE;
			final int base_y = this.screen_y * MAP_SIZE;
			final BufferedImage img_mask = this.mask.get();
			final Iabcd area = this.getMaskArea();
			final int color_white = Color.WHITE.getRGB();
			int xx, yy;
			for (int iy=0; iy<MAP_SIZE; iy++) {
				yy = iy + base_y;
				for (int ix=0; ix<MAP_SIZE; ix++) {
					xx = ix + base_x;
					if (img_mask == null || img_mask.getRGB(xx, yy) == color_white) {
						final Color pixel = pixels[yy][xx];
						if (pixel != null)
							canvas.setPixelColor(ix+area.a, iy+area.b, pixel);
					}
				}
			}
		}
	}



	@Override
	public void close() {
		if (this.stopping.compareAndSet(false, true)) {
			// remove the item frame
			this.frame.setItem(null);
			this.frame.remove();
			// free the map id
			final FreedMapStore mapstore = GetFreedMapStore();
			mapstore.release(this.map_id);
		}
	}



	public Location getLocation() {
		return this.location;
	}



	public Iabcd getMaskArea() {
		final Iabcd area = this.mask_area.get();
		return (area==null ? new Iabcd(0, 0, 128, 128) : area);
	}
	public void setMask(final BufferedImage mask) {
		this.mask.set(mask);
		// find screen size
		{
			int min_x = Integer.MAX_VALUE;
			int min_y = Integer.MAX_VALUE;
			int max_x = Integer.MIN_VALUE;
			int max_y = Integer.MIN_VALUE;
			// no screen mask
			if (mask == null) {
				min_x = min_y = 0;
				max_x = max_y = MAP_SIZE - 1;
				// find mask size in + shape
			} else {
				final int half = Math.floorDiv(MAP_SIZE, 2);
				for (int ix=0; ix<half; ix++) {
					if (Color.BLACK.equals(new Color(mask.getRGB(half-ix, half)))) {
						min_x = (half - ix) + 1;
						break;
					}
				}
				for (int ix=0; ix<half; ix++) {
					if (Color.BLACK.equals(new Color(mask.getRGB(half+ix, half)))) {
						max_x = half + ix;
						break;
					}
				}
				for (int iy=0; iy<half; iy++) {
					if (Color.BLACK.equals(new Color(mask.getRGB(half, half-iy)))) {
						min_y = (half - iy) + 1;
						break;
					}
				}
				for (int iy=0; iy<half; iy++) {
					if (Color.BLACK.equals(new Color(mask.getRGB(half, half+iy)))) {
						max_y = half + iy;
						break;
					}
				}
			}
			final Iabcd size = new Iabcd(min_x, min_y, max_x-min_x, max_y-min_y);
			this.mask_area.compareAndSet(null, size);
		}
	}



}
