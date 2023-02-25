package com.poixson.commonmc;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;

import com.poixson.commonmc.tools.plugin.xJavaPlugin;
import com.poixson.commonmc.tools.updatechecker.UpdateCheckManager;
import com.poixson.tools.AppProps;
import com.poixson.tools.Keeper;


public class pxnCommonPlugin extends xJavaPlugin {
	public static final String LOG_PREFIX = "[pxnCommon] ";
	public static final String CHAT_PREFIX = ChatColor.AQUA + LOG_PREFIX + ChatColor.WHITE;

	protected final Keeper keeper;
	protected final AppProps props;

	protected final AtomicReference<UpdateCheckManager> checkManager = new AtomicReference<UpdateCheckManager>(null);

	@Override public int getSpigotPluginID() { return 107049; }
	@Override public int getBStatsID() {       return 17785;  }



	public pxnCommonPlugin() {
		super(pxnCommonPlugin.class);
		this.keeper = Keeper.get();
		try {
			this.props = AppProps.LoadFromClassRef(pxnCommonPlugin.class);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}



	@Override
	public void onEnable() {
		final ServicesManager services = Bukkit.getServicesManager();
		services.register(pxnCommonPlugin.class, this, this, ServicePriority.Normal);
		// update check manager
		{
			final UpdateCheckManager manager = new UpdateCheckManager(this);
			final UpdateCheckManager previous = this.checkManager.getAndSet(manager);
			if (previous != null)
				previous.stop();
			services.register(UpdateCheckManager.class, manager, this, ServicePriority.Normal);
			manager.addPlugin(this, this.getSpigotPluginID(), this.getPluginVersion());
			// wait for server to start
			manager.startLater();
		}
		super.onEnable();
	}

	@Override
	public void onDisable() {
		super.onDisable();
		// update check manager
		{
			final UpdateCheckManager manager = this.checkManager.getAndSet(null);
			if (manager != null) {
				manager.stop();
			}
		}
	}



	// -------------------------------------------------------------------------------



	public static pxnCommonPlugin GetCommonPlugin() {
		final pxnCommonPlugin plugin = Bukkit.getServicesManager().load(pxnCommonPlugin.class);
		if (plugin == null) throw new RuntimeException("pxnCommonPlugin not loaded");
		return plugin;
	}






}
