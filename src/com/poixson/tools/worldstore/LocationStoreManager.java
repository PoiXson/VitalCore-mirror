package com.poixson.tools.worldstore;

import static com.poixson.utils.BukkitUtils.GetServerPath;
import static com.poixson.utils.Utils.GetMS;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.scheduler.BukkitRunnable;

import com.poixson.tools.xJavaPlugin;
import com.poixson.tools.xTime;
import com.poixson.tools.dao.Iab;
import com.poixson.tools.events.PluginSaveEvent;
import com.poixson.tools.events.xListener;


public class LocationStoreManager extends BukkitRunnable {

	public static final long DEFAULT_INTERVAL     = xTime.Parse("10s").ticks(50L);
	public static final long DEFAULT_DELAY_UNLOAD = xTime.ParseToLong("3m");
	public static final long DEFAULT_DELAY_SAVE   = xTime.ParseToLong("30s");

	protected final xJavaPlugin plugin;
	protected final xListener listener_save;

	protected final String type;

	protected final File path;
	protected final AtomicBoolean inited = new AtomicBoolean(false);

	protected final ConcurrentHashMap<Iab, LocationStore> regions = new ConcurrentHashMap<Iab, LocationStore>();



	public LocationStoreManager(final xJavaPlugin plugin, final String worldStr, final String type) {
		this.plugin = plugin;
		this.type = type;
		this.path = new File(GetServerPath(), worldStr+"/locs");
		this.listener_save = new xListener(plugin) {
			@EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
			public void onPluginSave(final PluginSaveEvent event) {
				LocationStoreManager.this.save();
			}
		};
	}



	public void init() {
		if (this.inited.compareAndSet(false, true)) {
			if (!this.path.isDirectory()) {
				if (!this.path.mkdir())
					throw new RuntimeException("Failed to create directory: " + this.path.toString());
				this.log().info("Created directory: " + this.path.toString());
			}
		}
	}

	public LocationStoreManager start() {
		this.listener_save.register();
		this.runTaskTimerAsynchronously(this.plugin, DEFAULT_INTERVAL, DEFAULT_INTERVAL);
		return this;
	}
	public void stop() {
		try {
			this.cancel();
		} catch (Exception ignore) {}
		this.listener_save.unregister();
		this.save();
	}



	@Override
	public void run() {
		final long time = GetMS();
		final Iterator<Entry<Iab, LocationStore>> it = this.regions.entrySet().iterator();
		while (it.hasNext()) {
			final Entry<Iab, LocationStore> entry = it.next();
			if (entry.getValue().isStale(time)) {
				try {
					entry.getValue().save();
				} catch (IOException e) {
					e.printStackTrace();
				}
				this.regions.remove(entry.getKey());
			}
		}
	}
	public void save() {
		init();
		for (final LocationStore store : this.regions.values()) {
			try {
				store.save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}



	public boolean add(final int x, final int z) {
		final LocationStore region = this.getRegion(x, z);
		return region.add(x, z);
	}
	public boolean remove(final int x, final int z) {
		final LocationStore region = this.getRegion(x, z);
		return region.remove(x, z);
	}
	public boolean contains(final int x, final int z) {
		final LocationStore region = this.getRegion(x, z);
		return region.contains(x, z);
	}



	public LocationStore getRegion(final int x, final int z) {
		init();
		final int regionX = Math.floorDiv(x, 512);
		final int regionZ = Math.floorDiv(z, 512);
		final Iab loc = new Iab(regionX, regionZ);
		// cached
		synchronized (this.regions) {
			final LocationStore store = this.regions.get(loc);
			if (store != null) {
				store.markAccessed();
				return store;
			}
		}
		// load existing or new
		{
			final String fileStr = String.format(
				"%s.%d.%d.json",
				this.type,
				Integer.valueOf(regionX),
				Integer.valueOf(regionZ)
			);
			final File file = new File(this.path, fileStr);
			final LocationStore store = new LocationStore(file);
			try {
				store.load(regionX, regionZ);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
			final LocationStore existing = this.regions.putIfAbsent(loc, store);
			if (existing == null)
				return store;
			existing.markAccessed();
			return existing;
		}
	}



	public Iab[] findNear(final int x, final int z) {
		// single region
		{
			final LocationStore store = this.getRegion(x, z);
			if (store != null) {
				if (store.locations.size() > 0)
					return store.locations.toArray(new Iab[0]);
			}
		}
		// multiple regions
		{
			int xx, zz;
			final HashSet<Iab> result = new HashSet<Iab>();
			for (int iz=-1; iz<2; iz++) {
				zz = (iz * 512) + z;
				for (int ix=-1; ix<2; ix++) {
					if (ix == 0 && iz == 0) continue;
					xx = (ix * 512) + x;
					final LocationStore store = this.getRegion(xx, zz);
					for (final Iab loc : store.locations)
						result.add(loc);
				}
			}
			return result.toArray(new Iab[0]);
		}
	}
	public Iab findNearest(final int x, final int z) {
		final Iab[] near = this.findNear(x, z);
		if (near.length > 0) {
			double distance = Double.MAX_VALUE;
			double dist;
			Iab nearest = null;
			for (final Iab loc : near) {
				dist = Math.sqrt(
					Math.pow( (double)(x-loc.a), 2.0) +
					Math.pow( (double)(z-loc.b), 2.0)
				);
				if (distance > dist) {
					distance = dist;
					nearest  = loc;
				}
			}
			return nearest;
		}
		return null;
	}



	public Logger log() {
		return Logger.getLogger("Minecraft");
	}



}
