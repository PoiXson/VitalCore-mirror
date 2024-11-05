package com.poixson.tools.worldstore;

import static com.poixson.utils.gson.GsonUtils.GSON;

import java.io.File;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.bukkit.plugin.java.JavaPlugin;

import com.google.gson.reflect.TypeToken;
import com.poixson.tools.abstractions.Triple;


public class WorldStore_KeyVal extends WorldStore_Map<String, Object> {



	public WorldStore_KeyVal(final JavaPlugin plugin,
			final String world) {
		super(
			plugin, world, "state", Integer.MAX_VALUE,
			Long.MAX_VALUE, // never expire
			DEFAULT_CYCLES_SAVE,
			DEFAULT_CYCLES_SAVE_MAX
		);
	}



	@Override
	public void start() {
		super.start();
		this.load(null);
	}



	@Override
	public void tick() {
		this.tick(this.type);
	}



	public void mark_accessed(final String key) {
		super.mark_accessed(this.type);
	}
	@Override
	public void mark_changed(final String key) {
		super.mark_changed(this.type);
	}
	public void mark_saved(final String key) {
		super.mark_saved(this.type);
	}

	@Override
	public Triple<AtomicLong, AtomicLong, AtomicLong> getCycles(final String key) {
		return super.getCycles(this.type);
	}



	// never expire
	@Override
	public void invalidate(final String key) {
	}



	@Override
	protected Object load_decode(final String json) {
		final Type token = new TypeToken<HashMap<String, Object>>() {}.getType();
		final Map<String, Object> map = GSON().fromJson(json, token);
		if (map == null) throw new NullPointerException("Failed to parse json");
		final Iterator<Entry<String, Object>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			final Entry<String, Object> entry = it.next();
			this.put(entry.getKey(), entry.getValue());
		}
		return null;
	}

	@Override
	protected String save_encode(final String key, final Object value) {
		return GSON().toJson(this.map);
	}

	@Override
	public File getFile(final String key) {
		String filename = DEFAULT_NAMED_FILE;
		filename = filename.replace("<name>", this.type);
		return new File(this.path, filename);
	}



	public String getString(final String key) {
		final Object value = this.get(key);
		return (value==null ? null : value.toString());
	}
	public int getInt(final String key) {
		final Object value = this.get(key);
		if (value == null) return Integer.MIN_VALUE;
		if (value instanceof Integer) return ((Integer)value).intValue();
		if (value instanceof Double ) return ((Double )value).intValue();
		return Integer.MIN_VALUE;
	}
	public long getLong(final String key) {
		final Object value = this.get(key);
		if (value == null) return Long.MIN_VALUE;
		if (value instanceof Long  ) return ((Long  )value).intValue();
		if (value instanceof Double) return ((Double)value).intValue();
		return Long.MIN_VALUE;
	}
	public double getDouble(final String key) {
		final Object value = this.get(key);
		if (value == null) return Double.MIN_VALUE;
		if (value instanceof Double)
			return ((Double)value).intValue();
		return Double.MIN_VALUE;
	}



	public void set(final String key, final int value) {
		this.put(key, Integer.valueOf(value));
	}
	public void set(final String key, final long value) {
		this.put(key, Long.valueOf(value));
	}
	public void set(final String key, final double value) {
		this.put(key, Double.valueOf(value));
	}



}
