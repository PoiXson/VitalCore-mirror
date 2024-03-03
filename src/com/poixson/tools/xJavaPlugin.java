package com.poixson.tools;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import com.poixson.tools.events.PluginSaveEvent;
import com.poixson.tools.events.xListener;
import com.poixson.tools.updatechecker.UpdateCheckManager;


public abstract class xJavaPlugin extends JavaPlugin {
	public static final String CHAT_PREFIX = ChatColor.AQUA + "[pxn] " + ChatColor.WHITE;

	protected final AtomicReference<Metrics> metrics = new AtomicReference<Metrics>(null);
	protected final AppProps props;

	protected final AtomicReference<FileConfiguration> config = new AtomicReference<FileConfiguration>(null);

	// listeners
	protected final AtomicReference<LocalPluginSaveListener> saveListener = new AtomicReference<LocalPluginSaveListener>(null);



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
				this.metrics.set(new Metrics(this, id));
			}
		}
		// update checker
		UpdateCheckManager.Register(this);
		// save listener
		{
			final LocalPluginSaveListener listener = new LocalPluginSaveListener(this);
			final LocalPluginSaveListener previous = this.saveListener.getAndSet(listener);
			if (previous != null)
				previous.unregister();
			listener.register();
		}
	}
	@Override
	public void onDisable() {
		// save listener
		{
			final LocalPluginSaveListener listener = this.saveListener.getAndSet(null);
			if (listener != null)
				listener.unregister();
		}
		super.onDisable();
		this.metrics.set(null);
		// update checker
		UpdateCheckManager.Unregister(this);
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
		// services
		Bukkit.getServicesManager()
			.unregisterAll(this);
	}



	public void onSave() {
		this.saveConfigs();
	}



	// -------------------------------------------------------------------------------
	// configs



	protected void loadConfigs() {
	}
	protected void saveConfigs() {
	}
	protected void configDefaults(final FileConfiguration cfg) {
	}



	protected void mkPluginDir() {
		final File path = this.getDataFolder();
		if (!path.isDirectory()) {
			if (!path.mkdir())
				throw new RuntimeException("Failed to create directory: " + path.toString());
			this.log().info("Created directory: " + path.toString());
		}
	}



	// -------------------------------------------------------------------------------



	public int getSpigotPluginID() {
		return 0;
	}
	public int getBStatsID() {
		return 0;
	}



	public String getPluginVersion() {
		return this.props.version;
	}



	// -------------------------------------------------------------------------------



	class LocalPluginSaveListener extends xListener {

		public LocalPluginSaveListener(final JavaPlugin plugin) {
			super(plugin);
		}

		@EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
		public void onPluginSave(final PluginSaveEvent event) {
			xJavaPlugin.this.onSave();
		}

	}



	public Logger log() {
		return this.getLogger();
	}
	public static Logger Log() {
		return Logger.getLogger("Minecraft");
	}



}
