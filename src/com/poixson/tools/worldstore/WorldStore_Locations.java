package com.poixson.tools.worldstore;

import static com.poixson.utils.GsonUtils.GSON;

import java.io.File;
import java.util.LinkedList;
import java.util.concurrent.CopyOnWriteArraySet;

import org.bukkit.plugin.java.JavaPlugin;

import com.poixson.tools.dao.Iab;


public class WorldStore_Locations extends WorldStore_Map<Iab, CopyOnWriteArraySet<Iab>> {

	public static final int DEFAULT_REGION_SIZE = 512;

	protected final int size_x, size_z;



	public WorldStore_Locations(final JavaPlugin plugin,
			final String world, final String type) {
		this(plugin, world, type, DEFAULT_REGION_SIZE, DEFAULT_REGION_SIZE);
	}
	public WorldStore_Locations(final JavaPlugin plugin,
			final String world, final String type,
			final int size_x, final int size_z) {
		super(plugin, world, type);
		this.size_x = size_x;
		this.size_z = size_z;
	}



	@Override
	protected CopyOnWriteArraySet<Iab> load_decode(final String json) {
		final CopyOnWriteArraySet<Iab> result = new CopyOnWriteArraySet<Iab>();
		final String[] array = GSON().fromJson(json, String[].class);
		for (final String entry : array) {
			final Iab loc = Iab.FromString(entry);
			result.add(loc);
		}
		return result;
	}

	@Override
	protected String save_encode(final Iab region_loc, final CopyOnWriteArraySet<Iab> locs) {
		final LinkedList<String> result = new LinkedList<String>();
		for (final Iab loc : locs)
			result.addLast(loc.toString());
		return GSON().toJson(result);
	}



	@Override
	public File getFile(final Iab key) {
		String filename = this.type+".<x>.<z>.json";
		filename = filename.replace("<x>", Integer.toString(key.a));
		filename = filename.replace("<z>", Integer.toString(key.b));
		return new File(this.path, filename);
	}



	public CopyOnWriteArraySet<Iab> getRegion(final int region_x, final int region_z) {
		return this.getRegion(region_x, region_z, false);
	}
	public CopyOnWriteArraySet<Iab> getRegion(final Iab region) {
		return this.getRegion(region, false);
	}
	public CopyOnWriteArraySet<Iab> getRegion(final int region_x, final int region_z, final boolean lazy) {
		return this.getRegion(new Iab(region_x, region_z), lazy);
	}
	public CopyOnWriteArraySet<Iab> getRegion(final Iab region, final boolean lazy) {
		// existing
		{
			final CopyOnWriteArraySet<Iab> locs = super.get(region, lazy);
			if (locs != null || lazy)
				return locs;
		}
		// new instance
		{
			final CopyOnWriteArraySet<Iab> locs = new CopyOnWriteArraySet<Iab>();
			final CopyOnWriteArraySet<Iab> existing = this.map.putIfAbsent(region, locs);
			return (existing==null ? locs : existing);
		}
	}

	public void addLocation(final int x, final int z) {
		final int region_x = Math.floorDiv(x, this.size_x);
		final int region_z = Math.floorDiv(z, this.size_z);
		final Iab region = new Iab(region_x, region_z);
		this.mark_changed(region);
		final CopyOnWriteArraySet<Iab> locs = this.getRegion(region, false);
		locs.add(new Iab(x, z));
	}

	public boolean containsLocation(final int x, final int z) {
		return this.containsLocation(x, z, false);
	}
	public boolean containsLocation(final int x, final int z, final boolean lazy) {
		final int region_x = Math.floorDiv(x, this.size_x);
		final int region_z = Math.floorDiv(z, this.size_z);
		final Iab region_loc = new Iab(region_x, region_z);
		final CopyOnWriteArraySet<Iab> locs = super.get(region_loc, lazy);
		if (locs == null) return false;
		return locs.contains(new Iab(x, z));
	}



	public Iab[] near(final int x, final int z) {
		return this.near(x, z, 1, false);
	}
	public Iab[] nearLazy(final int x, final int z) {
		return this.near(x, z, 1, true);
	}
	public Iab[] near(final int x, final int z, final int dist) {
		return this.near(x, z, dist, false);
	}
	public Iab[] nearLazy(final int x, final int z, final int dist) {
		return this.near(x, z, dist, true);
	}

	public Iab[] near(final int x, final int z, final int dist, final boolean lazy) {
		final int region_x = Math.floorDiv(x, this.size_x);
		final int region_z = Math.floorDiv(z, this.size_z);
		// single region
		if (dist <= 0) {
			final CopyOnWriteArraySet<Iab> locs = this.getRegion(region_x, region_z, lazy);
			return (locs==null ? new Iab[0] : locs.toArray(new Iab[0]));
		// multiple regions
		} else {
			final LinkedList<Iab> result = new LinkedList<Iab>();
			for (int iz=0-dist; iz<dist; iz++) {
				for (int ix=0-dist; ix<dist; ix++) {
					final CopyOnWriteArraySet<Iab> locs = this.getRegion(region_x+ix, region_z+iz, lazy);
					if (locs != null) {
						for (final Iab loc : locs)
							result.addLast(loc);
					}
				}
			}
			return result.toArray(new Iab[0]);
		}
	}



}
