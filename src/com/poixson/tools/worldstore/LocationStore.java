package com.poixson.tools.worldstore;

import static com.poixson.tools.worldstore.LocationStoreManager.DEFAULT_DELAY_UNLOAD;
import static com.poixson.tools.worldstore.LocationStoreManager.DEFAULT_DELAY_SAVE;
import static com.poixson.tools.xJavaPlugin.LOG_PREFIX;
import static com.poixson.utils.Utils.GetMS;
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
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.poixson.tools.dao.Iab;


public class LocationStore {

	protected final File file;

	public final CopyOnWriteArraySet<Iab> locations = new CopyOnWriteArraySet<Iab>();

	protected final AtomicLong last_used    = new AtomicLong(0L);
	protected final AtomicLong last_changed = new AtomicLong(0L);



	public LocationStore(final File file) {
		this.file = file;
	}



	public void load(final int regionX, final int regionZ) throws IOException {
		this.locations.clear();
		if (this.file.isFile()) {
			synchronized (this.locations) {
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
						this.log().warning(String.format(
							"%sInvalid entry '%s' in file: %s",
							LOG_PREFIX, entry, this.file.toString()
						));
						continue;
					}
				}
				SafeClose(reader);
			}
		}
	}
	public boolean save() throws IOException {
		this.last_changed.set(0L);
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
		return false;
	}



	public boolean isStale() {
		return this.isStale(GetMS());
	}
	public boolean isStale(final long time) {
		boolean saved = false;
		// last changed
		{
			final long last = this.last_changed.get();
			if (last > 0L) {
				final long since = time - last;
				if (since > DEFAULT_DELAY_SAVE) {
					saved = true;
					try {
						this.save();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		// last accessed
		{
			final long last = this.last_used.get();
			if (last <= 0L) {
				this.last_used.compareAndSet(last, time);
			} else {
				final long since = time - last;
				if (since > DEFAULT_DELAY_UNLOAD) {
					if (!saved) {
						try {
							this.save();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					return true;
				}
			}
		}
		return false;
	}



	public void markAccessed() {
		this.markAccessed(GetMS());
	}
	public void markAccessed(final long time) {
		this.last_used.set(time);
	}

	public void markChanged() {
		this.markChanged(GetMS());
	}
	public void markChanged(final long time) {
		this.markAccessed(time);
		this.last_changed.set(time);
	}



	public boolean add(final int x, final int z) {
		final boolean result = this.locations.add(new Iab(x, z));
		this.markChanged();
		return result;
	}
	public boolean remove(final int x, final int z) {
		final boolean result = this.locations.remove(new Iab(x, z));
		this.markChanged();
		return result;
	}
	public boolean contains(final int x, final int z) {
		final boolean result = this.locations.contains(new Iab(x, z));
		this.markAccessed();
		return result;
	}



	public Logger log() {
		return Logger.getLogger("Minecraft");
	}



}
