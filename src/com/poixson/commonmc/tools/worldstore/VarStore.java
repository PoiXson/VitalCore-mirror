package com.poixson.commonmc.tools.worldstore;

import static com.poixson.utils.Utils.SafeClose;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.poixson.commonmc.utils.BukkitUtils;
import com.poixson.tools.xTime;


public class VarStore extends BukkitRunnable {
	public static final long DEFAULT_SAVE_DELAY = (new xTime("30s")).ticks(50L);

	protected final File path;
	protected final File file;

	protected final ConcurrentHashMap<String, Object> vars = new ConcurrentHashMap<String, Object>();

	protected final AtomicBoolean changed = new AtomicBoolean(false);



	public VarStore(final String worldStr) {
		this(worldStr, "vars.json");
	}
	public VarStore(final String worldStr, final String filename) {
		this.path = new File(BukkitUtils.GetServerPath(), worldStr);
		this.file = new File(this.path, filename);
	}



	public VarStore start(final JavaPlugin plugin) {
		try {
			this.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		this.runTaskTimerAsynchronously(plugin, DEFAULT_SAVE_DELAY, DEFAULT_SAVE_DELAY);
		return this;
	}



	@Override
	public void run() {
		try {
			this.save();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	protected synchronized void load() throws IOException {
		if (this.file.isFile()) {
			synchronized (this.vars) {
				final BufferedReader reader = Files.newBufferedReader(this.file.toPath());
				final Type token = new TypeToken<HashSet<String>>() {}.getType();
				final Map<String, Object> map = (new Gson()).fromJson(reader, token);
				final Iterator<Entry<String, Object>> it = map.entrySet().iterator();
				while (it.hasNext()) {
					final Entry<String, Object> entry = it.next();
					this.vars.put(entry.getKey(), entry.getValue());
				}
				SafeClose(reader);
			}
		}
	}
	public boolean save() throws IOException {
		if (this.changed.getAndSet(false)) {
			synchronized (this.vars) {
				final String data = (new Gson()).toJson(this.vars);
				final BufferedWriter writer = new BufferedWriter(new FileWriter(this.file));
				writer.write(data);
				SafeClose(writer);
				return true;
			}
		}
		return false;
	}



	public Object get(final String key) {
		return this.vars.get(key);
	}
	public String getString(final String key) {
		final Object value = this.get(key);
		if (value == null) return null;
		return (String) value;
	}
	public int getInt(final String key) {
		final Object value = this.get(key);
		if (value != null) {
			if (value instanceof Integer)
				return ((Integer)value).intValue();
		}
		return Integer.MIN_VALUE;
	}
	public long getLong(final String key) {
		final Object value = this.get(key);
		if (value != null) {
			if (value instanceof Long)
				return ((Long)value).intValue();
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
		this.vars.put(key, value);
		this.changed.set(true);
	}
	public void set(final String key, final int value) {
		this.set(key, Integer.valueOf(value));
	}
	public void set(final String key, final long value) {
		this.set(key, Long.valueOf(value));
	}
	public void set(final String key, final double value) {
		this.set(key, Double.valueOf(value));
	}



}
