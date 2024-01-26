package com.poixson.tools.events;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.plugin.PluginManager;

import com.poixson.pluginlib.pxnPluginLib;


public class PluginSaveManager extends xListener {



	public PluginSaveManager(pxnPluginLib plugin) {
		super(plugin);
	}



	@EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
	public void onWorldSave(final WorldSaveEvent event) {
		final World world = event.getWorld();
		if ("world".equals(world.getName())) {
			final PluginManager pm = Bukkit.getPluginManager();
			final PluginSaveEvent eventSave = new PluginSaveEvent();
			pm.callEvent(eventSave);
		}
	}



}
