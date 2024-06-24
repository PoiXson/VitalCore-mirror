package com.poixson.tools;

import static com.poixson.utils.Utils.GetMS;

import java.util.concurrent.atomic.AtomicLong;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.WorldSaveEvent;

import com.poixson.tools.events.SaveActiveEvent;
import com.poixson.tools.events.SaveEvent;
import com.poixson.tools.events.xListener;


public class SaveMonitor implements xListener {

	protected final AtomicLong last = new AtomicLong(-1L);



	public SaveMonitor() {
		this.last.set(GetMS());
	}



	@EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
	public void onWorldSave(final WorldSaveEvent event) {
		final long time = GetMS();
		final long last = this.last.get();
		if (time - last >= 60000L) {
			this.last.set(time);
			final SaveEvent event_save = new SaveEvent(time - last);
			Bukkit.getPluginManager()
				.callEvent(event_save);
		}
		final SaveActiveEvent event_active = new SaveActiveEvent(time, last, event.getWorld());
		Bukkit.getPluginManager()
			.callEvent(event_active);
	}



}
