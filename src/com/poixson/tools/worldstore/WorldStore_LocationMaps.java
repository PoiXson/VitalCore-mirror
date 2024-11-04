package com.poixson.tools.worldstore;

import static com.poixson.utils.gson.GsonUtils.GSON;

import java.io.File;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.plugin.java.JavaPlugin;

import com.google.gson.reflect.TypeToken;
import com.poixson.tools.dao.Iab;


public class WorldStore_LocationMaps extends WorldStore_Map<Iab, Map<Iab, Map<String, Object>>> {

	public static final int DEFAULT_REGION_SIZE = 512;

	protected final int size_x, size_z;



	public WorldStore_LocationMaps(final JavaPlugin plugin,
			final String world, final String type) {
		this(plugin, world, type, DEFAULT_REGION_SIZE, DEFAULT_REGION_SIZE);
	}
	public WorldStore_LocationMaps(final JavaPlugin plugin,
			final String world, final String type,
			final int size_x, final int size_z) {
		super(plugin, world, type);
		this.size_x = size_x;
		this.size_z = size_z;
	}



	@Override
	protected Map<Iab, Map<String, Object>> load_decode(final String json) {
		final Map<Iab, Map<String, Object>> result = new ConcurrentHashMap<Iab, Map<String, Object>>();
		final Type type = new TypeToken<Map<String, Map<String, Object>>>() {}.getType();
		final Map<String, Map<String, Object>> map = GSON().fromJson(json, type);
		if (map == null) throw new NullPointerException("Failed to parse json");
		for (final Entry<String, Map<String, Object>> entry : map.entrySet()) {
			final Iab loc = Iab.FromString(entry.getKey());
			final ConcurrentHashMap<String, Object> keyvals = new ConcurrentHashMap<String, Object>();
			for (final Entry<String, Object> ent : entry.getValue().entrySet())
				keyvals.put(ent.getKey(), ent.getValue());
			result.put(loc, keyvals);
		}
		return result;
	}

	@Override
	protected String save_encode(final Iab region_loc, final Map<Iab, Map<String, Object>> map) {
		return GSON().toJson(map);
	}

	@Override
	public File getFile(final Iab key) {
		String filename = DEFAULT_LOCATION_FILE;
		filename = filename.replace("<name>", this.type);
		filename = filename.replace("<x>", Integer.toString(key.a));
		filename = filename.replace("<z>", Integer.toString(key.b));
		return new File(this.path, filename);
	}



	public Map<Iab, Map<String, Object>> getRegion(final int region_x, final int region_z) {
		return this.getRegion(region_x, region_z, false);
	}
	public Map<Iab, Map<String, Object>> getRegion(final Iab region) {
		return this.getRegion(region, false);
	}
	public Map<Iab, Map<String, Object>> getRegion(final int region_x, final int region_z, final boolean lazy) {
		return this.getRegion(new Iab(region_x, region_z), lazy);
	}
	public Map<Iab, Map<String, Object>> getRegion(final Iab region, final boolean lazy) {
		// existing
		{
			final Map<Iab, Map<String, Object>> map = super.get(region, lazy);
			if (map != null || lazy)
				return map;
		}
		// new instance
		{
			final Map<Iab, Map<String, Object>> map = new ConcurrentHashMap<Iab, Map<String, Object>>();
			final Map<Iab, Map<String, Object>> existing = this.map.putIfAbsent(region, map);
			return (existing==null ? map : existing);
		}
	}

	public Map<String, Object> addLocation(final int x, final int z) {
		return this.addLocation(x, z, new ConcurrentHashMap<String, Object>());
	}
	public Map<String, Object> addLocation(final int x, final int z,
			final Map<String, Object> m) {
		final int region_x = Math.floorDiv(x, this.size_x);
		final int region_z = Math.floorDiv(z, this.size_z);
		final Iab region = new Iab(region_x, region_z);
		this.mark_changed(region);
		final Map<Iab, Map<String, Object>> map = this.getRegion(region, false);
		map.put(new Iab(x, z), m);
		return m;
	}

	public boolean containsLocation(final int x, final int z) {
		return this.containsLocation(x, z, false);
	}
	public boolean containsLocation(final int x, final int z, final boolean lazy) {
		final int region_x = Math.floorDiv(x, this.size_x);
		final int region_z = Math.floorDiv(z, this.size_z);
		final Iab region_loc = new Iab(region_x, region_z);
		final Map<Iab, Map<String, Object>> map = super.get(region_loc, lazy);
		if (map == null) return false;
		return map.containsKey(new Iab(x, z));
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
			final Map<Iab, Map<String, Object>> map = this.getRegion(region_x, region_z, lazy);
			return (map==null ? new Iab[0] : map.keySet().toArray(new Iab[0]));
		// multiple regions
		} else {
			final LinkedList<Iab> result = new LinkedList<Iab>();
			for (int iz=0-dist; iz<dist; iz++) {
				for (int ix=0-dist; ix<dist; ix++) {
					final Map<Iab, Map<String, Object>> map = this.getRegion(region_x+ix, region_z+iz, lazy);
					if (map != null) {
						for (final Iab loc : map.keySet())
							result.addLast(loc);
					}
				}
			}
			return result.toArray(new Iab[0]);
		}
	}



}
