package com.poixson.tools.events;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.plugin.PluginManager;


public class PluginSaveManager implements xListener {



	public PluginSaveManager() {
	}



	@EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
	public void onWorldSave(final WorldSaveEvent event) {
		final World world = event.getWorld();
		if ("world".equals(world.getName())) {
			final PluginManager pm = Bukkit.getPluginManager();
			pm.callEvent(new PluginSaveEvent());
		}
	}



}
