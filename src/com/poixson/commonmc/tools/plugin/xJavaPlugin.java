package com.poixson.commonmc.tools.plugin;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import com.poixson.commonmc.pxnCommonPlugin;
import com.poixson.commonmc.tools.updatechecker.UpdateCheckManager;
import com.poixson.tools.AppProps;


public abstract class xJavaPlugin extends JavaPlugin {
	public static final Logger log = Logger.getLogger("Minecraft");
	public static final String LOG_PREFIX = "[pxn] ";

	protected static final AtomicReference<Metrics> metrics = new AtomicReference<Metrics>(null);
	protected final AppProps props;

	protected final AtomicReference<FileConfiguration> config = new AtomicReference<FileConfiguration>(null);



	public xJavaPlugin(final Class<? extends xJavaPlugin> clss) {
		super();
		try {
			this.props = AppProps.LoadFromClassRef(clss);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}



	@Override
	public void onEnable() {
		super.onEnable();
		// load configs
		this.loadConfigs();
		// bStats
		{
			final int id = this.getBStatsID();
			if (id > 0) {
				System.setProperty("bstats.relocatecheck","false");
				metrics.set(new Metrics(this, id));
			}
		}
		// update checker
		{
			final int id = this.getSpigotPluginID();
			if (id > 0) {
				final pxnCommonPlugin common = pxnCommonPlugin.GetPlugin();
				if (common == null) throw new RuntimeException("pxnCommonPluginMC is not available");
				final UpdateCheckManager manager = common.getUpdateCheckManager();
				if (manager == null) throw new RuntimeException("UpdateCheckManager is not available");
				manager.addPlugin(this, id, this.getPluginVersion());
			}
		}
	}
	@Override
	public void onDisable() {
		super.onDisable();
		// update checker
		{
			final int id = this.getSpigotPluginID();
			if (id > 0) {
				pxnCommonPlugin.GetPlugin()
					.getUpdateCheckManager()
						.removePlugin(this.getSpigotPluginID());
			}
		}
		// stop schedulers
		try {
			Bukkit.getScheduler()
				.cancelTasks(this);
		} catch (Exception ignore) {}
		// stop listeners
		HandlerList.unregisterAll(this);
		// save configs
		this.saveConfigs();
		this.config.set(null);
	}



	// -------------------------------------------------------------------------------
	// configs



	protected void loadConfigs() {
	}
	protected void saveConfigs() {
	}
	protected void configDefaults(final FileConfiguration cfg) {
	}



	// -------------------------------------------------------------------------------



	protected int getSpigotPluginID() {
		return 0;
	}
	protected int getBStatsID() {
		return 0;
	}



	public String getPluginVersion() {
		return this.props.version;
	}



	public String getLogPrefix() {
		return LOG_PREFIX;
	}
	public String getChatPrefix() {
		return (new StringBuilder())
			.append(ChatColor.AQUA)
			.append(this.getLogPrefix())
			.append(ChatColor.WHITE)
			.toString();
	}



}
