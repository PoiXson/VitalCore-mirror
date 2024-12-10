package com.poixson.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import io.papermc.paper.plugin.configuration.PluginMeta;


public final class PermissionsReload {
	private PermissionsReload() {}



	public static void Reload()
			throws NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException,
			NoSuchMethodException, InvocationTargetException {
		final Server server = Bukkit.getServer();
		final PluginManager pm = Bukkit.getPluginManager();
		final Field field = pm.getClass().getDeclaredField("permissions");
		field.setAccessible(true);
		field.set(pm, new HashMap<String, Permission>());
		final Method meth = server.getClass().getDeclaredMethod("loadCustomPermissions");
		meth.setAccessible(true);
		meth.invoke(server);
		for (final Plugin plugin : pm.getPlugins()) {
			final PluginMeta meta = plugin.getPluginMeta();
			for (final Permission perm : meta.getPermissions()) {
				try {
					pm.addPermission(perm);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			}
		}
	}



}
