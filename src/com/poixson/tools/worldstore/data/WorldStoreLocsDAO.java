package com.poixson.tools.worldstore;

import static com.poixson.tools.worldstore.WorldStoreManager.DELAY_SAVE;
import static com.poixson.tools.worldstore.WorldStoreManager.DELAY_UNLOAD;
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
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicLong;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.poixson.tools.dao.Iab;


public class LocationStore implements WorldStoreData {

	protected final CopyOnWriteArraySet<Iab> locations = new CopyOnWriteArraySet<Iab>();

	protected final File file;

	protected final AtomicLong last_accessed = new AtomicLong(0L);
	protected final AtomicLong last_changed  = new AtomicLong(0L);



	public LocationStore(final File file) {
		this.file = file;
	}



	@Override
	public boolean load() throws IOException {
		if (this.file.isFile()) {
			BufferedReader reader = null;
			final Set<String> set;
			try {
				reader = Files.newBufferedReader(this.file.toPath());
				final Type token = new TypeToken<HashSet<String>>() {}.getType();
				set = (new Gson()).fromJson(reader, token);
			} finally {
				SafeClose(reader);
			}
			final LinkedList<Iab> locs = new LinkedList<Iab>();
			for (final String entry : set)
				locs.addLast(Iab.FromString(entry));
			synchronized (this.locations) {
				this.last_accessed.set(0L);
				this.last_changed.set(0L);
				this.locations.clear();
				if (!locs.isEmpty()) {
					for (final Iab loc : locs)
						this.locations.add(loc);
				}
			}
			return !locs.isEmpty();
		}
		return false;
	}

	@Override
	public boolean save() throws IOException {
		this.last_changed.set(0L);
		final LinkedList<String> data = new LinkedList<String>();
		for (final Iab loc : this.locations)
			data.add(loc.toString());
		final String json = (new Gson()).toJson(data);
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(this.file));
			writer.write(json);
		} finally {
			SafeClose(writer);
		}
		return !data.isEmpty();
	}



	@Override
	public boolean isStale(final long time) {
		boolean stale = false;
		// last accessed
		{
			final long last = this.last_accessed.get();
			if (last <= 0L) {
				this.last_accessed.compareAndSet(last, time);
			} else {
				final long since = time - last;
				if (since > DELAY_UNLOAD)
					stale = true;
			}
		}
		// last changed
		if (!stale) {
			final long last = this.last_changed.get();
			if (last > 0L) {
				final long since = time - last;
				if (since > DELAY_SAVE) {
					try {
						this.save();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return stale;
	}

	public void mark_accessed() {
		this.last_accessed.set(GetMS());
	}
	public void mark_changed() {
		this.mark_accessed();
		this.last_changed.compareAndSet(0L, GetMS());
	}



	public Iab[] getLocations() {
		return this.locations.toArray(new Iab[0]);
	}
	public boolean contains(final int x, final int z) {
		final boolean result = this.locations.contains(new Iab(x, z));
		this.mark_accessed();
		return result;
	}

	public boolean add(final int x, final int z) {
		final boolean result = this.locations.add(new Iab(x, z));
		if (result)
			this.mark_changed();
		return result;
	}
	public boolean remove(final int x, final int z) {
		final boolean result = this.locations.remove(new Iab(x, z));
		if (result)
			this.mark_changed();
		return result;
	}



}
