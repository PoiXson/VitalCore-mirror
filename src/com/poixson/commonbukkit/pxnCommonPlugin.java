package com.poixson.commonbukkit;

import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;


public class pxnCommonPlugin extends JavaPlugin {
	public static final String LOG_PREFIX  = "[pxnCommon] ";
	public static final String CHAT_PREFIX = ChatColor.AQUA + "[pxnCommon] " + ChatColor.WHITE;
	public static final Logger log = Logger.getLogger("Minecraft");

	protected static final AtomicReference<pxnCommonPlugin> instance = new AtomicReference<pxnCommonPlugin>(null);



	@Override
	public void onEnable() {
		if (!instance.compareAndSet(null, this))
			throw new RuntimeException("Plugin instance already enabled?");
	}

	@Override
	public void onDisable() {
		// stop listeners
		HandlerList.unregisterAll(this);
		// stop schedulers
		try {
			getServer()
				.getScheduler()
					.cancelTasks(this);
		} catch (Exception ignore) {}
		if (!instance.compareAndSet(this, null))
			throw new RuntimeException("Disable wrong instance of plugin?");
	}



}
