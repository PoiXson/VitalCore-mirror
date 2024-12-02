package com.poixson.tools.worldstore;

import static com.poixson.utils.gson.GsonUtils.GSON;

import java.io.File;
import java.util.LinkedList;
import java.util.concurrent.CopyOnWriteArraySet;

import org.bukkit.plugin.java.JavaPlugin;

import com.poixson.tools.dao.Iab;
import com.poixson.utils.MathUtils;


public class WorldStore_Locations extends WorldStore_HashMap<Iab, CopyOnWriteArraySet<Iab>> {



	public WorldStore_Locations(final JavaPlugin plugin,
			final String world, final String type) {
		super(plugin, world, type);
	}
	public WorldStore_Locations(final JavaPlugin plugin,
			final String world, final String type, final int group_size) {
		super(plugin, world, type, group_size);
	}



	@Override
	protected CopyOnWriteArraySet<Iab> load_decode(final String json) {
		final CopyOnWriteArraySet<Iab> result = new CopyOnWriteArraySet<Iab>();
		final String[] array = GSON().fromJson(json, String[].class);
		if (array == null) throw new NullPointerException("Failed to parse json");
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
		String filename = DEFAULT_LOCATION_FILE;
		filename = filename.replace("<name>", this.type);
		filename = filename.replace("<x>", Integer.toString(key.a));
		filename = filename.replace("<z>", Integer.toString(key.b));
		return new File(this.path, filename);
	}



	public CopyOnWriteArraySet<Iab> getGroup(final int group_x, final int group_z,
			final boolean lazy, final boolean create) {
		return this.getGroup(new Iab(group_x, group_z), lazy, create);
	}
	public CopyOnWriteArraySet<Iab> getGroup(final Iab group_loc,
			final boolean lazy, final boolean create) {
		// existing
		{
			final CopyOnWriteArraySet<Iab> locs = super.get(group_loc, lazy, create);
			if (locs != null || lazy)
				return locs;
		}
		// new instance
		if (create) {
			final CopyOnWriteArraySet<Iab> locs = new CopyOnWriteArraySet<Iab>();
			final CopyOnWriteArraySet<Iab> existing = this.map.putIfAbsent(group_loc, locs);
			return (existing==null ? locs : existing);
		}
		return null;
	}

	public void addLocation(final int x, final int z) {
		final int group_x = this.loc_to_group(x);
		final int group_z = this.loc_to_group(z);
		final Iab group_loc = new Iab(group_x, group_z);
		this.mark_changed(group_loc);
		final CopyOnWriteArraySet<Iab> locs = this.getGroup(group_loc, false, true);
		locs.add(new Iab(x, z));
	}

	public boolean containsLocation(final int x, final int z) {
		return this.containsLocation(x, z, true, false);
	}
	public boolean containsLocation(final int x, final int z,
			final boolean lazy, final boolean create) {
		final int group_x = this.loc_to_group(x);
		final int group_z = this.loc_to_group(z);
		final Iab group_loc = new Iab(group_x, group_z);
		final CopyOnWriteArraySet<Iab> locs = super.get(group_loc, lazy, create);
		return (locs==null ? false : locs.contains(new Iab(x, z)));
	}



	public Iab[] near(final int x, final int z, final int dist,
			final boolean lazy, final boolean create) {
		final int group_x = this.loc_to_group(x);
		final int group_z = this.loc_to_group(z);
		// single region
		if (dist <= 0) {
			final CopyOnWriteArraySet<Iab> locs =
				this.getGroup(group_x, group_z, lazy, create);
			return (locs==null ? new Iab[0] : locs.toArray(new Iab[0]));
		// multiple regions
		} else {
			final LinkedList<Iab> result = new LinkedList<Iab>();
			for (int iz=0-dist; iz<dist; iz++) {
				for (int ix=0-dist; ix<dist; ix++) {
					final CopyOnWriteArraySet<Iab> locs =
						this.getGroup(group_x+ix, group_z+iz, lazy, create);
					if (locs != null) {
						for (final Iab loc : locs)
							result.addLast(loc);
					}
				}
			}
			return result.toArray(new Iab[0]);
		}
	}



	public Iab nearest(final int x, final int z, final int dist,
			final boolean lazy, final boolean create) {
		final Iab[] near = this.near(x, z, dist, lazy, create);
		Iab nearest_loc = null;
		double nearest_dist = Double.MAX_VALUE;
		for (final Iab entry : near) {
			final double d = MathUtils.Distance2D(x, z, entry.a, entry.b);
			if (nearest_dist > d) {
				nearest_dist = d;
				nearest_loc = entry;
			}
		}
		return nearest_loc;
	}



}
