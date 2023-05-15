package com.poixson.commonmc.events;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.plugin.PluginManager;

import com.poixson.commonmc.pxnCommonPlugin;
import com.poixson.commonmc.tools.plugin.xListener;


public class SaveListener extends xListener<pxnCommonPlugin> {



	public SaveListener(pxnCommonPlugin plugin) {
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
