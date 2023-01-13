package com.poixson.commonmc;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import com.poixson.commonmc.tools.updatechecker.UpdateCheckManager;
import com.poixson.tools.AppProps;
import com.poixson.tools.Keeper;


public class pxnCommonPlugin extends JavaPlugin {
	public static final String LOG_PREFIX  = "[pxnCommon] ";
	public static final String CHAT_PREFIX = ChatColor.AQUA + LOG_PREFIX + ChatColor.WHITE;
	public static final Logger log = Logger.getLogger("Minecraft");
	public static final int SPIGOT_PLUGIN_ID = 107049;

	protected static final AtomicReference<pxnCommonPlugin> instance = new AtomicReference<pxnCommonPlugin>(null);
	protected final Keeper keeper;
	protected final AppProps props;

	protected final AtomicReference<UpdateCheckManager> checkManager = new AtomicReference<UpdateCheckManager>(null);



	public static pxnCommonPlugin GetPlugin() {
		return instance.get();
	}

	public pxnCommonPlugin() {
		super();
		this.keeper = Keeper.get();
		try {
			this.props = AppProps.LoadFromClassRef(pxnCommonPlugin.class);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}



	@Override
	public void onEnable() {
		if (!instance.compareAndSet(null, this))
			throw new RuntimeException("Plugin instance already enabled?");
		// update check manager
		{
			final UpdateCheckManager manager = new UpdateCheckManager(this);
			final UpdateCheckManager previous = this.checkManager.getAndSet(manager);
			if (previous != null)
				previous.stop();
			manager.addPlugin(this, SPIGOT_PLUGIN_ID, this.getPluginVersion());
			// wait for server to start
			manager.startLater();
		}
	}

	@Override
	public void onDisable() {
		// update checker
		pxnCommonPlugin.GetPlugin()
			.getUpdateCheckManager()
				.removePlugin(SPIGOT_PLUGIN_ID);
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



	public UpdateCheckManager getUpdateCheckManager() {
		return this.checkManager.get();
	}



	public String getPluginVersion() {
		return this.props.version;
	}



}
