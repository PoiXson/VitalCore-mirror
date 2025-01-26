package com.poixson.tools;

import static com.poixson.utils.GsonProvider.GSON;
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
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.map.MapView;

import com.google.gson.reflect.TypeToken;
import com.poixson.tools.events.SaveEvent;
import com.poixson.vitalcore.VitalCorePlugin;


public class FreedMapStore implements xListener {

	public static final int MAX_MAP_ID = Integer.MAX_VALUE;

	protected final VitalCorePlugin plugin;

	protected final ConcurrentSkipListSet<Integer> freed = new ConcurrentSkipListSet<Integer>();

	protected final File file;
	protected final AtomicBoolean changed = new AtomicBoolean(false);
	protected final ReentrantLock lock = new ReentrantLock(true);



	public FreedMapStore(final VitalCorePlugin plugin, final String path) {
		this.plugin = plugin;
		this.file = new File(path, "freed-maps.json");
	}



	public void register() {
		xListener.super.register(this.plugin);
	}



	public void load() throws IOException {
		this.lock.lock();
		try {
			this.freed.clear();
			if (this.file.isFile()) {
				this.log().info("Loading: freed-maps.json");
				BufferedReader reader = null;
				try {
					reader = Files.newBufferedReader(this.file.toPath());
					final Set<Integer> set = GSON().fromJson(reader, GetMapToken());
					for (final Integer id : set)
						this.freed.add(id);
					this.log().info(String.format("Loaded %d freed maps", Integer.valueOf(this.freed.size())));
					this.changed.set(false);
				} finally {
					SafeClose(reader);
				}
			}
		} finally {
			this.lock.unlock();
		}
	}
	public boolean save() throws IOException {
		this.lock.lock();
		try {
			if (this.changed.getAndSet(false)) {
				final Integer[] list = this.freed.toArray(new Integer[0]);
				final int[] result = new int[list.length];
				if (list.length > 0) {
					int i = 0;
					for (final Integer id : list)
						result[i++] = id.intValue();
				}
				this.log().info(String.format("Saving %d freed maps", Integer.valueOf(result.length)));
				BufferedWriter writer = null;
				try {
					final String json = GSON().toJson(result);
					writer = new BufferedWriter(new FileWriter(this.file));
					writer.write(json);
					return true;
				} finally {
					SafeClose(writer);
				}
			}
		} finally {
			this.lock.unlock();
		}
		return false;
	}

	@EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
	public void onPluginSave(final SaveEvent event) {
		try {
			this.save();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	@Deprecated
	public int get() {
		return this.grab();
	}
	public int grab() {
		final Integer next = this.freed.pollFirst();
		if (next != null) {
			this.changed.set(true);
			return next.intValue();
		}
		final MapView map = Bukkit.createMap(Bukkit.getWorld("world"));
		return map.getId();
	}
	public boolean release(final int map_id) {
		final boolean result = this.freed.add(Integer.valueOf(map_id));
		this.changed.set(true);
		return result;
	}



	// -------------------------------------------------------------------------------
	// gson



	public static Type GetMapToken() {
		return (new TypeToken<HashSet<Integer>>() {}).getType();
	}



	// -------------------------------------------------------------------------------



	public Logger log() {
		return this.plugin.log();
	}



}
