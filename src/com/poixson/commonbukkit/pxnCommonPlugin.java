package com.poixson.commonbukkit;

import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import com.poixson.tools.Keeper;


public class pxnCommonPlugin extends JavaPlugin {
	public static final String LOG_PREFIX  = "[pxnCommon] ";
	public static final String CHAT_PREFIX = ChatColor.AQUA + LOG_PREFIX + ChatColor.WHITE;
	public static final Logger log = Logger.getLogger("Minecraft");

	protected static final AtomicReference<pxnCommonPlugin> instance = new AtomicReference<pxnCommonPlugin>(null);
	protected final Keeper keeper;



	public static pxnCommonPlugin GetPlugin() {
		return instance.get();
	}

	public pxnCommonPlugin() {
		super();
		this.keeper = Keeper.get();
	}



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
