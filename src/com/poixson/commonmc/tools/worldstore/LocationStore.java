package com.poixson.commonmc.tools.locationstore;

import static com.poixson.commonmc.tools.plugin.xJavaPlugin.LOG;
import static com.poixson.commonmc.tools.plugin.xJavaPlugin.LOG_PREFIX;
import static com.poixson.utils.Utils.SafeClose;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.poixson.tools.xTime;
import com.poixson.tools.dao.Iab;


public class LocationStore {

	public static final int DELAY_SAVE   = (int) (new xTime("10s")).get(TimeUnit.SECONDS);
	public static final int DELAY_UNLOAD = (int) (new xTime( "3m")).get(TimeUnit.SECONDS);

	protected final File file;

	public final CopyOnWriteArraySet<Iab> locations = new CopyOnWriteArraySet<Iab>();

	protected final AtomicBoolean changed = new AtomicBoolean(false);
	protected final AtomicInteger state   = new AtomicInteger(0);



	public LocationStore(final File file) {
		this.file = file;
	}



	public synchronized void load(final int regionX, final int regionZ) throws IOException {
		this.locations.clear();
		if (file.isFile()) {
			final BufferedReader reader = Files.newBufferedReader(this.file.toPath());
			final Type token = new TypeToken<HashSet<String>>() {}.getType();
			final Set<String> set = (new Gson()).fromJson(reader, token);
			String[] split;
			int x, z;
			for (final String entry : set) {
				try {
					split = entry.split(",");
					if (split.length != 2) throw new NumberFormatException();
					x = Integer.parseInt(split[0].trim());
					z = Integer.parseInt(split[1].trim());
					this.locations.add(new Iab(x, z));
				} catch (NumberFormatException e) {
					LOG.warning(String.format(
						"%sInvalid entry '%s' in file: %s",
						LOG_PREFIX, entry, this.file.toString()
					));
					continue;
				}
			}
			SafeClose(reader);
		}
	}
	public boolean save() throws IOException {
		if (this.changed.getAndSet(false)) {
			final Set<String> result = new HashSet<String>();
			final Iab[] set = this.locations.toArray(new Iab[0]);
			for (final Iab loc : set)
				result.add(loc.toString());
			if (result.size() > 0) {
				final String data = (new Gson()).toJson(result);
				final BufferedWriter writer = new BufferedWriter(new FileWriter(this.file));
				writer.write(data);
				SafeClose(writer);
				return true;
			}
		}
		return false;
	}



	protected boolean should_unload() {
		final int state = this.state.incrementAndGet();
		// save
		if (this.changed.get()) {
			if (state == DELAY_SAVE) {
				try {
					this.save();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return false;
		}
		// unload
		if (state > DELAY_UNLOAD) {
			try {
				this.save();
			} catch (IOException ignore) {}
			return true;
		}
		return false;
	}
	public void markAccessed() {
		this.state.set(0);
	}



	public boolean add(final int x, final int z) {
		this.state.set(0);
		final boolean result = this.locations.add(new Iab(x, z));
		this.changed.set(true);
		return result;
	}
	public boolean remove(final int x, final int z) {
		this.state.set(0);
		final boolean result = this.locations.remove(new Iab(x, z));
		this.changed.set(true);
		return result;
	}
	public boolean contains(final int x, final int z) {
		this.state.set(0);
		return this.locations.contains(new Iab(x, z));
	}



}
