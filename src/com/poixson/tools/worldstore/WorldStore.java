package com.poixson.tools.worldstore;

import static com.poixson.utils.BukkitUtils.GetServerPath;
import static com.poixson.utils.Utils.GetMS;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.scheduler.BukkitRunnable;

import com.poixson.tools.xJavaPlugin;
import com.poixson.tools.xListener;
import com.poixson.tools.xTime;
import com.poixson.tools.abstractions.xStartStop;
import com.poixson.tools.dao.Iab;
import com.poixson.tools.events.SaveEvent;
import com.poixson.utils.BukkitUtils;


public abstract class WorldStoreManager<T extends WorldStoreData> implements xStartStop, Runnable {
	public static final long INTERVAL     = xTime.Parse("10s").ticks(50L);
	public static final long DELAY_SAVE   = xTime.ParseToLong("30s");
	public static final long DELAY_UNLOAD = xTime.ParseToLong("3m");
	public static final String DEFAULT_DIR = "/pxn";

	protected final AtomicBoolean inited = new AtomicBoolean(false);

	protected final xJavaPlugin plugin;

	protected final ConcurrentHashMap<Iab, T> data = new ConcurrentHashMap<Iab, T>();

	protected final int region_size;

	protected final String world;
	protected final String type;
	protected final File   path;



	public WorldStoreManager(final xJavaPlugin plugin,
			final int region_size,
			final String world, final String type) {
		this.plugin      = plugin;
		this.region_size = region_size;
		this.world       = world;
		this.type        = type;
		this.path = new File(GetServerPath(), world+DEFAULT_DIR);
	};



	protected final xListener listener = new xListener() {
		@EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
		public void onPluginSave(final SaveEvent event) {
			try {
				WorldStoreManager.this.save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};

	public BukkitRunnable ticker = new BukkitRunnable() {
		@Override
		public void run() {
			WorldStoreManager.this.run();
		}
	};



	public void init() {
		if (this.inited.compareAndSet(false, true)) {
			if (!this.path.isDirectory()) {
				if (!this.path.mkdir())
					throw new RuntimeException("Failed to create directory: "+this.path.toString());
				this.log().info("Created directory: "+this.path.toString());
			}
		}
	}



	@Override
	public void start() {
		this.init();
		try {
			this.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		this.listener.register(this.plugin);
		this.ticker.runTaskTimerAsynchronously(this.plugin, INTERVAL, INTERVAL);
	}
	@Override
	public void stop() {
		this.listener.unregister();
		BukkitUtils.SafeCancel(this.ticker);
		try {
			this.save();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	public void load() throws IOException {
	}
	public void save() throws IOException {
		this.init();
		IOException err = null;
		for (final T store : this.data.values()) {
			try {
				store.save();
			} catch (IOException e) {
				if (err == null)
					err = e;
			}
		}
		if (err != null)
			throw err;
	}



	@Override
	public void run() {
		final long time = GetMS();
		final Set<Iab> stale = new HashSet<Iab>();
		final Iterator<Entry<Iab, T>> it = this.data.entrySet().iterator();
		while (it.hasNext()) {
			final Entry<Iab, T> entry = it.next();
			if (entry.getValue().isStale(time))
				stale.add(entry.getKey());
		}
		for (final Iab key : stale) {
			final T store = this.data.get(key);
			if (store != null) {
				try {
					store.save();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			this.data.remove(key);
		}
	}



	public File build_file_path(final File path, final String type,
			final int region_x, final int region_z) {
		final String filename = String.format(
			"%s.%d.%d.json",
			type,
			Integer.valueOf(region_x),
			Integer.valueOf(region_z)
		);
		return new File(path, filename);
	}



	public abstract T build_store(final int region_x, final int region_z);



	public T getRegion(final int region_x, final int region_z) {
		final T store = this.data.get(new Iab(region_x, region_z));
		return (
			store == null
			? this.build_store(region_x, region_z)
			: store
		);
	}



	public Logger log() {
		return this.plugin.log();
	}



}
