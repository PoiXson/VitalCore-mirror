package com.poixson.tools.worldstore;

import static com.poixson.utils.Utils.SafeClose;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.poixson.tools.xJavaPlugin;


public class WorldKeyStore extends WorldStoreManager<WorldStoreData> {

	protected final ConcurrentHashMap<String, Object> data = new ConcurrentHashMap<String, Object>();

	protected final File file;

	protected final AtomicBoolean changed = new AtomicBoolean(false);



	public WorldKeyStore(final xJavaPlugin plugin,
			final String world) {
		this(plugin, world, "data");
	}
	public WorldKeyStore(final xJavaPlugin plugin,
			final String world, final String type) {
		super(plugin, 0, world, type);
		this.file = this.build_file_path(this.path, type, 0, 0);
	}



	@Override
	public void load() throws IOException {
		if (this.file.isFile()) {
			BufferedReader reader = null;
			final HashMap<String, Object> map;
			try {
				reader = Files.newBufferedReader(this.file.toPath());
				final Type token = new TypeToken<HashMap<String, Object>>() {}.getType();
				map = (new Gson()).fromJson(reader, token);
			} finally {
				SafeClose(reader);
			}
			synchronized (this.data) {
				this.data.clear();
				if (!map.isEmpty()) {
					final Iterator<Entry<String, Object>> it = map.entrySet().iterator();
					while (it.hasNext()) {
						final Entry<String, Object> entry = it.next();
						this.data.put(entry.getKey(), entry.getValue());
					}
				}
				this.changed.set(true);
			}
		}
	}
	@Override
	public void save() throws IOException {
		if (this.changed.compareAndSet(true, false)) {
			this.init();
			final Gson gson = (new GsonBuilder()).setPrettyPrinting().create();
			final String json = gson.toJson(this.data);
			BufferedWriter writer = null;
			try {
				writer = new BufferedWriter(new FileWriter(this.file));
				writer.write(json);
			} finally {
				SafeClose(writer);
			}
		}
	}



	@Override
	public void run() {
	}



	@Override
	public File build_file_path(final File path, final String type,
			final int region_x, final int region_z) {
		final String filename = type + ".json";
		return new File(path, filename);
	}



	@Override
	public WorldStoreData build_store(final int region_x, final int region_z) {
		throw new UnsupportedOperationException();
	};

	@Override
	public WorldStoreData getRegion(final int region_x, final int region_z) {
		throw new UnsupportedOperationException();
	}



	public Object get(final String key) {
		return this.data.get(key);
	}
	public String getString(final String key) {
		final Object value = this.get(key);
		return (value==null ? null : (String)value);
	}
	public int getInt(final String key) {
		final Object value = this.get(key);
		if (value != null) {
			if (value instanceof Integer) return ((Integer)value).intValue();
			if (value instanceof Double ) return ((Double )value).intValue();
		}
		return Integer.MIN_VALUE;
	}
	public long getLong(final String key) {
		final Object value = this.get(key);
		if (value != null) {
			if (value instanceof Long  ) return ((Long  )value).intValue();
			if (value instanceof Double) return ((Double)value).intValue();
		}
		return Long.MIN_VALUE;
	}
	public double getDouble(final String key) {
		final Object value = this.get(key);
		if (value != null) {
			if (value instanceof Double)
				return ((Double)value).intValue();
		}
		return Double.MIN_VALUE;
	}



	public void set(final String key, final Object value) {
		this.data.put(key, value);
		this.changed.set(true);
	}
	public void set(final String key, final int value) {
		this.set(key, Integer.valueOf(value));
		this.changed.set(true);
	}
	public void set(final String key, final long value) {
		this.set(key, Long.valueOf(value));
		this.changed.set(true);
	}
	public void set(final String key, final double value) {
		this.set(key, Double.valueOf(value));
		this.changed.set(true);
	}



}
