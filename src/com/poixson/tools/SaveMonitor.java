package com.poixson.tools;

import static com.poixson.utils.Utils.GetMS;

import java.util.concurrent.atomic.AtomicLong;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.WorldSaveEvent;

import com.poixson.tools.events.SaveActiveEvent;
import com.poixson.tools.events.SaveEvent;


public class SaveMonitor implements xListener {
	public static final long DEFAULT_GRACE = xTime.ParseToLong("5s");

	protected final AtomicLong last = new AtomicLong(0L);



	public SaveMonitor() {
	}



	@EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
	public void onWorldSave(final WorldSaveEvent event) {
		final long time = GetMS();
		final long last = this.last.getAndSet(time);
		final long since = time - last;
		if (since > DEFAULT_GRACE) {
			this.last.set(time);
			final SaveEvent event_save = new SaveEvent(since);
			Bukkit.getPluginManager()
				.callEvent(event_save);
		}
		final SaveActiveEvent event_active = new SaveActiveEvent(time, last, event.getWorld());
		Bukkit.getPluginManager()
			.callEvent(event_active);
	}



}
