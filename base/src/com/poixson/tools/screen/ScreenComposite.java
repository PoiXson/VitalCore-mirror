package com.poixson.tools.screen;

import static com.poixson.utils.BukkitUtils.EqualsLocation;
import static com.poixson.utils.LocationUtils.FaceToIxyz;
import static com.poixson.utils.MathUtils.MinMax;

import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.poixson.tools.abstractions.xStartStop;
import com.poixson.tools.dao.Iabc;
import com.poixson.tools.dao.Iabcd;


public class ScreenComposite extends BukkitRunnable implements xStartStop {
	public static final int MAP_SIZE    = 128;
	public static final int DEFAULT_FPS = 1;

	protected final JavaPlugin plugin;

	protected final Location  location;
	protected final BlockFace facing;

	protected final PixelSource source;
	protected final ScreenPanel[][] panels;
	protected final AtomicReference<BufferedImage> img_mask = new AtomicReference<BufferedImage>(null);

	protected final AtomicInteger fps = new AtomicInteger(DEFAULT_FPS);
	protected final AtomicLong tick_index = new AtomicLong(0L);

	protected final AtomicReference<Iabcd> screen_size = new AtomicReference<Iabcd>(null);
	protected final int screens_width, screens_height;
	protected final boolean perplayer;

	protected final AtomicBoolean stopping = new AtomicBoolean(false);



	public ScreenComposite(final JavaPlugin plugin, final PixelSource source,
			final Location location, final BlockFace facing,
			final int screens_width, final int screens_height, final boolean perplayer) {
		this.plugin         = plugin;
		this.source         = source;
		this.location       = location;
		this.facing         = facing;
		this.screens_width  = screens_width;
		this.screens_height = screens_height;
		this.perplayer      = perplayer;
		// screen panels
		this.panels = new ScreenPanel[screens_height][];
		final Iabc add = FaceToIxyz(facing);
		for (int iy=0; iy<screens_height; iy++) {
			this.panels[iy] = new ScreenPanel[screens_width];
			for (int ix=0; ix<screens_width; ix++) {
				final Location loc = location.clone()
						.add(add.a*ix, iy, add.c*ix);
				this.panels[iy][ix] = new ScreenPanel(loc, facing, ix, iy, source, perplayer);
			}
		}
	}



	@Override
	public void start() {
		if (this.stopping.get()) return;
		this.runTaskTimer(this.plugin, 1L, 1L);
	}
	@Override
	public void stop() {
		this.stopping.set(true);
		for (final ScreenPanel[] panels_y : this.panels) {
			for (final ScreenPanel panel : panels_y)
				panel.close();
		}
		try {
			this.cancel();
		} catch (Exception ignore) {}
	}



	@Override
	public void run() {
		final long tick_index = this.tick_index.incrementAndGet();
		final long tpf = this.getTicksPerFrame();
		// update frame
		if (tick_index % tpf == 0)
			this.source.update();
		// display frame
		if (this.source.isUpdated()) {
			for (final Player player : Bukkit.getOnlinePlayers()) {
				for (final ScreenPanel[] panels_y : ScreenComposite.this.panels) {
					for (final ScreenPanel panel : panels_y)
						panel.send(player);
				}
			}
		}
	}



	public long getTicksPerFrame() {
		return MinMax(Math.floorDiv(20L, (long)this.getFPS()), 1L, 20L);
	}
	public int getFPS() {
		return MinMax(this.fps.get(), 1, 20);
	}
	public void setFPS(final int fps) {
		this.fps.set(fps);
	}



	public Location[] getPanelLocations() {
		final LinkedList<Location> locations = new LinkedList<Location>();
		for (final ScreenPanel[] panels_y : this.panels) {
			for (final ScreenPanel panel : panels_y)
				locations.add(panel.getLocation());
		}
		return locations.toArray(new Location[0]);
	}

	public boolean isLocation(final Location loc) {
		if (loc == null) return false;
		for (final ScreenPanel[] panels_y : this.panels) {
			for (final ScreenPanel panel : panels_y) {
				if (EqualsLocation(loc, panel.getLocation()))
					return true;
			}
		}
		return false;
	}



	public Iabcd getScreenSize() {
//TODO: detect mask
		return new Iabcd(0, 0, this.screens_width*MAP_SIZE, this.screens_height*MAP_SIZE);
	}



}
