package com.poixson.tools.worldstore;

import static com.poixson.tools.CacheMap.SECONDS_PER_CYCLE;
import static com.poixson.utils.BukkitUtils.SafeCancel;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicReference;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.poixson.tools.Keeper;
import com.poixson.tools.xListener;
import com.poixson.tools.xTime;
import com.poixson.tools.abstractions.xStartStop;
import com.poixson.tools.events.SaveEvent;


public class WorldStoreTicker extends BukkitRunnable implements xStartStop {

	public static final long DEFAULT_TICKS_PER_CYCLE = xTime.Parse(SECONDS_PER_CYCLE).ticks(50L);

	protected static final AtomicReference<WorldStoreTicker> instance = new AtomicReference<WorldStoreTicker>(null);

	protected final JavaPlugin plugin;

	protected final CopyOnWriteArraySet<WorldStore_HashMap<?, ?>> stores = new CopyOnWriteArraySet<WorldStore_HashMap<?, ?>>();



	public static WorldStoreTicker Get(final JavaPlugin plugin) {
		// existing instance
		{
			final WorldStoreTicker ticker = instance.get();
			if (ticker != null)
				return ticker;
		}
		// new instance
		{
			final WorldStoreTicker ticker = new WorldStoreTicker(plugin);
			if (instance.compareAndSet(null, ticker)) {
				ticker.start();
				return ticker;
			}
			return instance.get();
		}
	}

	protected WorldStoreTicker(final JavaPlugin plugin) {
		this.plugin = plugin;
	}



	protected final xListener listener = new xListener() {
		@EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
		public void onSave(final SaveEvent event) {
			WorldStoreTicker.this
				.saveAll();
		}
	};



	@Override
	public void start() {
		this.runTaskTimerAsynchronously(this.plugin, DEFAULT_TICKS_PER_CYCLE, DEFAULT_TICKS_PER_CYCLE);
		this.listener.register(this.plugin);
		Keeper.Add(this);
	}

	@Override
	public void stop() {
		SafeCancel(this);
		this.listener.unregister();
		Keeper.Remove(this);
	}



	public void register(final WorldStore_HashMap<?, ?> store) {
		this.stores.add(store);
	}
	public void unregister(final WorldStore_HashMap<?, ?> store) {
		this.stores.remove(store);
		if (this.stores.isEmpty())
			this.stop();
	}



	@Override
	public void run() {
		final Iterator<WorldStore_HashMap<?, ?>> it = this.stores.iterator();
		while (it.hasNext()) {
			final WorldStore_HashMap<?, ?> store = it.next();
			store.tick();
		}
	}
	public void saveAll() {
		final Iterator<WorldStore_HashMap<?, ?>> it = this.stores.iterator();
		while (it.hasNext()) {
			final WorldStore_HashMap<?, ?> store = it.next();
			store.saveAll();
		}
	}



}
