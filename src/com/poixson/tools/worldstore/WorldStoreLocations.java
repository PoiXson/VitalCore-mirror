package com.poixson.tools.worldstore;

import java.util.HashSet;

import com.poixson.tools.xJavaPlugin;
import com.poixson.tools.dao.Iab;
import com.poixson.utils.MathUtils;


public class LocationStoreManager extends WorldStoreManager<LocationStore> {

	public static final int REGION_SIZE = 512;



	public LocationStoreManager(final xJavaPlugin plugin,
			final String world, final String type) {
		super(plugin, REGION_SIZE, world, type);
	}



	@Override
	public LocationStore build_store(final int region_x, final int region_z) {
		return new LocationStore(
			this.build_file_path(
				this.path, this.type,
				region_x, region_z
			)
		);
	}



	public Iab[] getAllNear(final int x, final int z, final int region_radius) {
		final int region_x = Math.floorDiv(x, this.region_size);
		final int region_z = Math.floorDiv(z, this.region_size);
		final HashSet<Iab> found = new HashSet<Iab>();
		final int from = 1 - region_radius;
		for (int iz=from; iz<region_radius; iz++) {
			final int zz = region_z + iz;
			for (int ix=from; ix<region_radius; ix++) {
				final int xx = region_x + ix;
				final LocationStore store = this.data.get(new Iab(xx, zz));
				if (store != null) {
					for (final Iab loc : store.getLocations())
						found.add(loc);
				}
			}
		}
		return found.toArray(new Iab[0]);
	}
	public Iab findNearest(final int x, final int z) {
		final Iab[] near = this.getAllNear(x, z, 2);
		if (near.length == 0)
			return null;
		double distance = Double.MAX_VALUE;
		Iab nearest = null;
		for (final Iab loc : near) {
			final double dist = MathUtils.Distance2D(x, z, loc.a, loc.b);
			if (distance > dist) {
				distance = dist;
				nearest  = loc;
			}
		}
		return nearest;
	}



	public void add(final int x, final int z) {
		final int region_x = Math.floorDiv(x, 512);
		final int region_z = Math.floorDiv(z, 512);
		final LocationStore store = this.getRegion(region_x, region_z);
		store.add(x, z);
	}



}
