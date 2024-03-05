package com.poixson.tools.events;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;


public interface xListener extends Listener {



	public default void register(final JavaPlugin plugin) {
		Bukkit.getPluginManager()
			.registerEvents(this, plugin);
	}
	public default void unregister() {
		HandlerList.unregisterAll(this);
	}



}
