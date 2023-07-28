package com.poixson.commonmc.tools.scripting.screen;

import static com.poixson.commonmc.utils.ItemUtils.SetMapID;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
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
import org.bukkit.map.MapView.Scale;
import org.bukkit.plugin.java.JavaPlugin;

import com.poixson.commonmc.pxnCommonPlugin;
import com.poixson.commonmc.tools.mapstore.FreedMapStore;
import com.poixson.commonmc.utils.BukkitUtils;
import com.poixson.tools.abstractions.xStartStop;
import com.poixson.tools.dao.Iabcd;


//TODO: check fps changed and reload task_sender
public class MapScreen extends MapRenderer implements xStartStop {

	protected final JavaPlugin plugin;

	protected final int  map_id;
	protected final AtomicInteger map_size = new AtomicInteger(128);
	protected final AtomicReference<Iabcd> screen_size = new AtomicReference<Iabcd>(null);

	protected final Location  loc;
	protected final BlockFace facing;
	protected final ItemStack map;
	protected final MapView   view;
	protected final ItemFrame frame;

	protected final AtomicInteger fps      = new AtomicInteger(1);
	protected final AtomicInteger fps_last = new AtomicInteger(1);

	public final AtomicReference<BufferedImage> img_screen_mask = new AtomicReference<BufferedImage>(null);

	protected final AtomicReference<MapSenderTask> task_sender   = new AtomicReference<MapSenderTask>(null);
	protected final AtomicReference<Runnable>      tick_listener = new AtomicReference<Runnable>(null);
	protected final AtomicReference<PixelSource>   pixel_source  = new AtomicReference<PixelSource>(null);
	protected final AtomicBoolean stopping = new AtomicBoolean(false);



	public MapScreen(final JavaPlugin plugin,
			final Location loc, final BlockFace facing,
			final int map_id, final int map_size) {
		super(true); // contextual - per player
		this.plugin = plugin;
		this.loc    = loc;
		this.facing = facing;
		this.map_id = map_id;
		if (map_size > 0)
			this.map_size.set(map_size);
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
	}



	// -------------------------------------------------------------------------------



	@Override
	public void start() {
		if (this.stopping.get()) return;
		// check fps changed
		final int fps = this.fps.get();
		if (fps != this.fps_last.getAndSet(fps)) {
			final MapSenderTask task = this.task_sender.getAndSet(null);
			if (task != null)
				task.stop();
		}
		this.getMapSenderTask();
	}
	@Override
	public void stop() {
		// stop map sender
		{
			final MapSenderTask task = this.task_sender.get();
			if (task != null)
				task.stop();
		}
		// remove the item frame
		if (this.stopping.compareAndSet(false, true)) {
			this.frame.setItem(null);
			this.frame.remove();
		}
		// free the map id
		{
			final FreedMapStore mapstore = pxnCommonPlugin.GetFreedMapStore();
			if (mapstore != null)
				mapstore.release(this.map_id);
		}
	}



	@Override
	public void render(final MapView view, final MapCanvas canvas, final Player player) {
		if (this.stopping.get()) return;
		final BufferedImage img_mask = this.img_screen_mask.get();
		final Iabcd size = this.getScreenSize();
		final Color[][] pixels = this.getPixels(player);
		if (pixels != null) {
			final int color_white = Color.WHITE.getRGB();
			final int w = pixels[0].length;
			final int h = pixels   .length;
			int xx, yy;
			for (int iy=0; iy<h; iy++) {
				yy = iy + size.b;
				for (int ix=0; ix<w; ix++) {
					xx = ix + size.a;
					if (img_mask == null || img_mask.getRGB(xx, yy) == color_white)
						canvas.setPixelColor(xx, yy, pixels[iy][ix]==null ? Color.BLACK : pixels[iy][ix]);
				}
			}
		}
	}



	public MapSenderTask getMapSenderTask() {
		if (this.stopping.get()) return null;
		// existing
		{
			final MapSenderTask task = this.task_sender.get();
			if (task != null)
				return task;
		}
		// new instance
		{
			final MapSenderTask task = new MapSenderTask(this.plugin, this.view) {
				@Override
				public void run() {
					super.run();
					MapScreen.this.tick();
				}
			};
			if (this.task_sender.compareAndSet(null, task)) {
				task.start(this.fps.get());
				return task;
			}
		}
		return this.getMapSenderTask();
	}



	// -------------------------------------------------------------------------------
	// listeners



	// tick
	public MapScreen setTickListener(final Runnable listener) {
		this.tick_listener.set(listener);
		return this;
	}
	protected void tick() {
		final Runnable listener = this.tick_listener.get();
		if (listener != null)
			listener.run();
	}

	// pixels
	public MapScreen setPixelSource(final PixelSource source) {
		this.pixel_source.set(source);
		return this;
	}
	public Color[][] getPixels(final Player player) {
		final PixelSource source = this.pixel_source.get();
		if (source != null)
			return source.getPixels(player);
		return null;
	}



	// -------------------------------------------------------------------------------



	public Location getLocation() {
		return this.loc;
	}



	public int getMapID() {
		return this.map_id;
	}
	public int getMapSize() {
		return this.map_size.get();
	}



	public int getFPS() {
		return this.fps.get();
	}
	public void setFPS(final int fps) {
		this.fps.set(fps);
	}



	public void setScreenMask(final BufferedImage img) {
		this.img_screen_mask.set(img);
		this.screen_size.set(null);
	}

	public Iabcd getScreenSize() {
		// existing
		{
			final Iabcd size = this.screen_size.get();
			if (size != null)
				return size;
		}
		// find screen size
		{
			int min_x = Integer.MAX_VALUE;
			int min_y = Integer.MAX_VALUE;
			int max_x = Integer.MIN_VALUE;
			int max_y = Integer.MIN_VALUE;
			final int map_size = this.map_size.get();
			final BufferedImage img = this.img_screen_mask.get();
			if (img == null) {
				min_x = 0; max_x = map_size - 1;
				min_y = 0; max_y = map_size - 1;
			} else {
				final int half = Math.floorDiv(map_size, 2);
				for (int ix=0; ix<half; ix++) {
					if (Color.BLACK.equals(new Color(img.getRGB(half-ix, half)))) {
						min_x = (half - ix) + 1;
						break;
					}
				}
				for (int ix=0; ix<half; ix++) {
					if (Color.BLACK.equals(new Color(img.getRGB(half+ix, half)))) {
						max_x = half + ix;
						break;
					}
				}
				for (int iy=0; iy<half; iy++) {
					if (Color.BLACK.equals(new Color(img.getRGB(half, half-iy)))) {
						min_y = (half - iy) + 1;
						break;
					}
				}
				for (int iy=0; iy<half; iy++) {
					if (Color.BLACK.equals(new Color(img.getRGB(half, half+iy)))) {
						max_y = half + iy;
						break;
					}
				}
			}
			final Iabcd size = new Iabcd(min_x, min_y, max_x-min_x, max_y-min_y);
			if (this.screen_size.compareAndSet(null, size))
				return size;
		}
		return this.getScreenSize();
	}



}
