package com.poixson.tools.screen;

import static com.poixson.tools.xJavaPlugin.Log;
import static com.poixson.utils.BukkitUtils.EqualsLocation;
import static com.poixson.utils.CraftScriptUtils.FixCursorPosition;
import static com.poixson.utils.CraftScriptUtils.PlayerToHashMap;
import static com.poixson.utils.LocationUtils.DistanceFast3D;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import com.poixson.scripting.xScriptThreadSafe;
import com.poixson.tools.dao.Iab;
import com.poixson.tools.dao.Iabcd;


public class PixelSource_Script extends PixelSource {
	public static final int DEFAULT_FPS    = 1;
	public static final int DEFAULT_RADIUS = 10;

	protected final xScriptThreadSafe script;

	public final AtomicBoolean import_players = new AtomicBoolean(false);
	public final AtomicBoolean export_pixels  = new AtomicBoolean(false);

	protected final Location location;
	protected final BlockFace facing;

	protected final AtomicReference<Iabcd> screen_size = new AtomicReference<Iabcd>(new Iabcd(0, 0, 128, 128));
	protected final AtomicReference<BufferedImage> mask = new AtomicReference<BufferedImage>(null);

	protected final ConcurrentLinkedQueue<AtomicReferenceArray<AtomicIntegerArray>> frames =
			new ConcurrentLinkedQueue<AtomicReferenceArray<AtomicIntegerArray>>();



	public PixelSource_Script(final xScriptThreadSafe script,
			final Location location, final BlockFace facing) {
		super();
		this.script   = script;
		this.location = location;
		this.facing   = facing;
	}



	@Override
	public void update() {
		final boolean import_players = this.import_players.get();
		final boolean export_pixels  = this.export_pixels.get();
		final Iabcd screen_size = this.screen_size.get();
		final int width  = screen_size.c - screen_size.a;
		final int height = screen_size.d - screen_size.b;
		// import players
		if (import_players) {
			final Map<String, Object> players = new ConcurrentHashMap<String, Object>();
			//LOOP_PLAYERS:
			for (final Player player : Bukkit.getOnlinePlayers()) {
				final int distance = (int) DistanceFast3D(this.location, player.getLocation());
				if (distance < DEFAULT_RADIUS) {
					final RayTraceResult ray = player.rayTraceBlocks(DEFAULT_RADIUS);
					if (ray != null) {
						if (EqualsLocation(ray.getHitBlock().getLocation(), this.location)) {
							final Vector vec = ray.getHitPosition();
							final Iab pos = FixCursorPosition(vec, screen_size, this.facing);
							if (pos != null) {
								final Map<String, Object> player_info = PlayerToHashMap(player);
								player_info.put("cursor_x", Integer.valueOf(pos.a));
								player_info.put("cursor_y", Integer.valueOf(pos.b));
								players.put(player.getName(), player_info);
							}
						}
					} // end ray
				} // end distance
			} // end LOOP_PLAYERS
			this.script.setVariable("players", players);
		}
		// export pixels
		final Color[][] pixels_last = new Color[height][];
		if (export_pixels) {
			final Color[][] pixels = (Color[][]) this.script.getVariable("pixels");
			if (pixels != null) {
				for (int iy=0; iy<height; iy++) {
					pixels_last[iy] = new Color[width];
					for (int ix=0; ix<width; ix++)
						pixels_last[iy][ix] = pixels[iy][ix];
				}
			}
		}
		// run script loop
		this.script.call("loop");
		// export pixels
		if (export_pixels && pixels_last != null) {
			final Color[][] pixels = (Color[][]) this.script.getVariable("pixels");
			if (pixels != null) {
				final AtomicReferenceArray<AtomicIntegerArray> frame = new AtomicReferenceArray<AtomicIntegerArray>(height);
				for (int iy=0; iy<height; iy++) {
					final AtomicIntegerArray row = new AtomicIntegerArray(width);
					for (int ix=0; ix<width; ix++) {
						if (pixels[iy][ix] != null)
							row.set(ix, pixels[iy][ix].getRGB());
					}
					frame.set(iy, row);
				}
				this.frames.add(frame);
//TODO: how to handle this?
				if (this.frames.size() > 10
				&&  this.frames.size() % 10 == 0)
					Log().warning(String.format("Frame buffer is filling?! [%d]", Integer.valueOf(this.frames.size())));
			}
		}
	}

	@Override
	public boolean isUpdated() {
		return !this.frames.isEmpty();
	}



	@Override
	public Color[][] getPixels() {
		final AtomicReferenceArray<AtomicIntegerArray> frame = this.frames.poll();
		if (frame != null) {
			final Iabcd screen_size = this.screen_size.get();
			final int width  = screen_size.c - screen_size.a;
			final int height = screen_size.d - screen_size.b;
			final Color[][] pixels = new Color[height][];
			for (int iy=0; iy<height; iy++) {
				pixels[iy] = new Color[width];
				final AtomicIntegerArray row = frame.get(iy);
				for (int ix=0; ix<width; ix++) {
					final int c = row.get(ix);
					if (c != Integer.MIN_VALUE)
						pixels[iy][ix] = new Color(c);
				}
			}
			return pixels;
		}
		return null;
	}



}
