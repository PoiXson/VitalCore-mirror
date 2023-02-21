package com.poixson.commonmc.tools.api;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import com.poixson.commonmc.pxnCommonPlugin;
import com.poixson.commonmc.tools.plugin.xJavaPlugin;
import com.poixson.commonmc.tools.updatechecker.UpdateCheckManager;
import com.poixson.utils.ObjectUtils;


public class pxnAPI {



	public static <T extends xJavaPlugin> T GetPlugin(final Class<T> clss) {
		final PluginManager pm = Bukkit.getPluginManager();
		for (final Plugin plugin : pm.getPlugins()) {
			if (clss.isInstance(plugin))
				return ObjectUtils.Cast(plugin, clss);
		}
		return null;
	}



	// -------------------------------------------------------------------------------
	// update checker



	public static UpdateCheckManager GetUpdateCheckManager() {
		final pxnCommonPlugin common = GetPlugin(pxnCommonPlugin.class);
		if (common == null) throw new RuntimeException("pxnCommonPluginMC is not available");
		return common.getUpdateCheckManager();
	}

	public static boolean RegisterUpdateChecker(final xJavaPlugin plugin) {
		final int spigot_id = plugin.getSpigotPluginID();
		if (spigot_id > 0) {
			final UpdateCheckManager manager = GetUpdateCheckManager();
			if (manager == null) throw new RuntimeException("UpdateCheckManager is not available");
			final String version = plugin.getPluginVersion();
			return (null != manager.addPlugin(plugin, spigot_id, version));
		}
		return false;
	}

	public static boolean UnregisterUpdateChecker(final xJavaPlugin plugin) {
		final int spigot_id = plugin.getSpigotPluginID();
		if (spigot_id > 0) {
			final UpdateCheckManager manager = GetUpdateCheckManager();
			if (manager == null) throw new RuntimeException("UpdateCheckManager is not available");
			return manager.removePlugin(spigot_id);
		}
		return false;
	}



}
