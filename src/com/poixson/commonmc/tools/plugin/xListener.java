package com.poixson.commonmc.tools.plugin;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;


public abstract class xListener<T extends xJavaPlugin> implements Listener {

	protected final T plugin;



	public xListener(final T plugin) {
		this.plugin = plugin;
	}



	public void register() {
		Bukkit.getPluginManager()
			.registerEvents(this, this.plugin);
	}
	public void unregister() {
		HandlerList.unregisterAll(this);
	}



}
