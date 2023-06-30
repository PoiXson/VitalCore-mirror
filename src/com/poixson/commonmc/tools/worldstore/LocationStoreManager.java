package com.poixson.commonmc.tools.locationstore;

import static com.poixson.commonmc.tools.plugin.xJavaPlugin.LOG;
import static com.poixson.commonmc.tools.plugin.xJavaPlugin.LOG_PREFIX;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.poixson.commonmc.utils.BukkitUtils;
import com.poixson.tools.dao.Iab;


public class LocationStoreManager extends BukkitRunnable {

	protected final String type;
	protected final File path;

	protected final AtomicBoolean loaded = new AtomicBoolean(false);

	protected final ConcurrentHashMap<Iab, LocationStore> regions = new ConcurrentHashMap<Iab, LocationStore>();



	public LocationStoreManager(final String worldStr, final String type) {
		this.type = type;
		this.path = new File(BukkitUtils.GetServerPath(), worldStr+"/locs");
	}



	public void load() {
		if (this.loaded.compareAndSet(false, true)) {
			if (!this.path.isDirectory()) {
				if (!this.path.mkdir())
					throw new RuntimeException("Failed to create directory: " + this.path.toString());
				LOG.info(  String.format("%sCreated directory: %s", LOG_PREFIX, this.path.toString()));
			}
		}
	}

	public LocationStoreManager start(final JavaPlugin plugin) {
		this.runTaskTimerAsynchronously(plugin, 20L, 20L);
		return this;
	}



	@Override
	public void run() {
		final Iterator<Entry<Iab, LocationStore>> it = this.regions.entrySet().iterator();
		while (it.hasNext()) {
			final Entry<Iab, LocationStore> entry = it.next();
			if (entry.getValue().should_unload())
				this.regions.remove(entry.getKey());
		}
	}
	public void saveAll() {
		load();
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
		load();
		final int regionX = Math.floorDiv(x, 512);
		final int regionZ = Math.floorDiv(z, 512);
		final Iab loc = new Iab(regionX, regionZ);
		// cached
		{
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



}
