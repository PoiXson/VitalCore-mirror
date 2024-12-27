package com.poixson.tools;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.poixson.tools.events.SaveEvent;
import com.poixson.tools.updatechecker.UpdateCheckManager;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.milkbowl.vault.economy.Economy;


public abstract class xJavaPlugin extends JavaPlugin implements AppProperties {
	public static final Component CHAT_PREFIX = Component.text("[pxn] ").color(NamedTextColor.DARK_AQUA);

	protected final AtomicReference<Metrics> metrics = new AtomicReference<Metrics>(null);
	protected final AppPropsDAO props = AppPropsDAO.LoadSafe();

	protected final AtomicReference<FileConfiguration> config = new AtomicReference<FileConfiguration>(null);
	protected final AtomicBoolean config_changed = new AtomicBoolean(false);

	// vault
	protected final AtomicReference<Economy> economy = new AtomicReference<Economy>(null);

	// listeners
	protected final AtomicReference<LocalPluginSaveListener> saveListener = new AtomicReference<LocalPluginSaveListener>(null);



	public xJavaPlugin() {
		super();
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
		UpdateCheckManager.RegisterPlugin(this);
		// save listener
		{
			final LocalPluginSaveListener listener = new LocalPluginSaveListener();
			final LocalPluginSaveListener previous = this.saveListener.getAndSet(listener);
			if (previous != null)
				previous.unregister();
			listener.register(this);
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
		// stop schedulers
		try {
			Bukkit.getScheduler()
				.cancelTasks(this);
		} catch (Exception ignore) {}
		// stop listeners
		HandlerList.unregisterAll(this);
		// save configs
		if (this.config_changed.getAndSet(false))
			this.saveConfigs();
		this.config.set(null);
		// services
		Bukkit.getServicesManager()
			.unregisterAll(this);
		// vault
		this.economy.set(null);
	}



	// -------------------------------------------------------------------------------
	// configs



	protected void loadConfigs() {
		this.mkPluginDir();
	}
	protected void saveConfigs() {
		super.saveConfig();
		this.config_changed.set(false);
	}
	protected void configDefaults(final FileConfiguration cfg) {
	}

	public void setConfigChanged() {
		this.config_changed.set(true);
	}



	protected void mkPluginDir() {
		final File path = this.getDataFolder();
		if (!path.isDirectory()) {
			if (!path.mkdir())
				throw new RuntimeException("Failed to create directory: "+path.toString());
			this.log().info("Created directory: "+path.toString());
		}
	}



	// -------------------------------------------------------------------------------
	// vault economy



	public static Economy SetupVaultEconomy() {
		if (Bukkit.getPluginManager().getPlugin("Vault") == null)
			return null;
		final RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
		if (rsp == null)
			return null;
		return rsp.getProvider();
	}



	public Economy getEconomy() {
		// existing instance
		{
			final Economy economy = this.economy.get();
			if (economy != null)
				return economy;
		}
		// new instance
		{
			final Economy economy= SetupVaultEconomy();
			if (this.economy.compareAndSet(null, economy))
				return economy;
		}
		return this.economy.get();
	}



	// -------------------------------------------------------------------------------
	// properties



	public int getSpigotPluginID() {
		return 0;
	}
	public int getBStatsID() {
		return 0;
	}



	public AppPropsDAO getProps() {
		return this.props;
	}



	// -------------------------------------------------------------------------------
	// listeners



	class LocalPluginSaveListener implements xListener {

		@EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
		public void onPluginSave(final SaveEvent event) {
			if (xJavaPlugin.this.config_changed.getAndSet(false))
				xJavaPlugin.this.saveConfigs();
		}

	}



	public Logger log() {
		return super.getLogger();
	}



}
