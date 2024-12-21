package com.poixson.tools.worldstore;

import static com.poixson.tools.gson.GsonProvider.GSON;
import static com.poixson.utils.MathUtils.DistanceLinear;
import static com.poixson.utils.Utils.IsEmpty;

import java.io.File;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.reflect.TypeToken;
import com.poixson.tools.xJavaPlugin;
import com.poixson.tools.abstractions.Triple;
import com.poixson.tools.abstractions.Tuple;
import com.poixson.tools.dao.Iab;


public class WorldStore_LocationMaps extends WorldStore_HashMap<Iab, Map<Iab, Map<String, Object>>> {



	public WorldStore_LocationMaps(final xJavaPlugin plugin,
			final String world, final String type) {
		super(plugin, world, type);
	}
	public WorldStore_LocationMaps(final xJavaPlugin plugin,
			final String world, final String type, final int group_size) {
		super(plugin, world, type, group_size);
	}



	@Override
	public Map<Iab, Map<String, Object>> create(final Iab key) {
		final Map<Iab, Map<String, Object>> map = new ConcurrentHashMap<Iab, Map<String, Object>>();
		map.put(key, new ConcurrentHashMap<String, Object>());
		return map;
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
		// clean empty maps
		Map<Iab, Map<String, Object>> result = new HashMap<Iab, Map<String, Object>>();
		for (final Entry<Iab, Map<String, Object>> entry : map.entrySet()) {
			if (!IsEmpty(entry.getValue()))
				result.put(entry.getKey(), entry.getValue());
		}
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



	public Map<String, Object> addLocation(final int x, final int z) {
		return this.addLocation(x, z, new ConcurrentHashMap<String, Object>());
	}
	public Map<String, Object> addLocation(final int x, final int z,
			final Map<String, Object> m) {
		final int group_x = this.loc_to_group(x);
		final int group_z = this.loc_to_group(z);
		final Iab group_loc = new Iab(group_x, group_z);
		this.mark_changed(group_loc);
		final Map<Iab, Map<String, Object>> map = this.get(group_loc, false, true);
		if (map == null) throw new NullPointerException("Failed to get region");
		map.put(new Iab(x, z), m);
		return m;
	}

	public boolean containsLocation(final int x, final int z,
			final boolean lazy, final boolean create) {
		final int group_x = this.loc_to_group(x);
		final int group_z = this.loc_to_group(z);
		final Iab group_loc = new Iab(group_x, group_z);
		final Map<Iab, Map<String, Object>> map = super.get(group_loc, lazy, create);
		if (map == null) return false;
		return map.containsKey(new Iab(x, z));
	}



	public Map<String, Object> getKeyValMap(final int x, final int z,
			final boolean lazy, final boolean create) {
		final int group_x = this.loc_to_group(x);
		final int group_z = this.loc_to_group(z);
		final Iab group_loc = new Iab(group_x, group_z);
		final Iab loc = new Iab(x, z);
		// get/load file
		final Map<Iab, Map<String, Object>> map = super.get(group_loc, lazy, create);
		if (map != null) {
			// get key/val map
			{
				final Map<String, Object> keyval = map.get(loc);
				if (keyval != null)
					return keyval;
			}
			// new key/val map
			if (create) {
				final Map<String, Object> keyval = new ConcurrentHashMap<String, Object>();
				final Map<String, Object> existing = map.putIfAbsent(loc, keyval);
				return (existing==null ? keyval : existing);
			}
		}
		return null;
	}



	public Tuple<Iab, Map<String, Object>>[] near(final int x, final int z, final int dist,
			final boolean lazy, final boolean create) {
		final int group_dist = Math.ceilDiv(dist, this.group_size);
		final LinkedList<Tuple<Iab, Map<String, Object>>> result = new LinkedList<Tuple<Iab, Map<String, Object>>>();
		for (int iz=0-group_dist; iz<group_dist; iz++) {
			for (int ix=0-group_dist; ix<group_dist; ix++) {
				final int group_x = this.loc_to_group(x+ix);
				final int group_z = this.loc_to_group(z+iz);
				final Map<Iab, Map<String, Object>> map = this.get(new Iab(group_x, group_z), lazy, create);
				if (map != null) {
					for (final Entry<Iab, Map<String, Object>> entry : map.entrySet()) {
						final Iab loc = entry.getKey();
						final Map<String, Object> m = entry.getValue();
						final double d = DistanceLinear(x, z, loc.a, loc.b);
						if (dist >= d)
							result.addLast(new Tuple<Iab, Map<String, Object>>(loc, m));
					}
				}
			}
		}
		return this._nearTupleArray(result);
	}
	@SuppressWarnings("unchecked")
	protected Tuple<Iab, Map<String, Object>>[] _nearTupleArray(final List<Tuple<Iab, Map<String, Object>>> list) {
		return list.toArray( (Tuple<Iab, Map<String, Object>>[]) new Tuple[0] );
	}

	public Triple<Double, Iab, Map<String, Object>> nearest(
			final int x, final int z, final int dist,
			final boolean lazy, final boolean create) {
		final Tuple<Iab, Map<String, Object>>[] near = this.near(x, z, dist, lazy, create);
		Iab nearest_loc = null;
		double nearest_dist = Double.MAX_VALUE;
		Map<String, Object> nearest_map = null;
		for (final Tuple<Iab, Map<String, Object>> entry : near) {
			final double d = DistanceLinear(x, z, entry.key.a, entry.key.b);
			if (nearest_dist > d) {
				nearest_dist = d;
				nearest_loc = entry.key;
				nearest_map = entry.val;
			}
		}
		if (nearest_loc == null) return null;
		return new Triple<Double, Iab, Map<String, Object>>(
			Double.valueOf(nearest_dist),
			nearest_loc,
			nearest_map
		);
	}



}
