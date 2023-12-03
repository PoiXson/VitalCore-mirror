package com.poixson.pluginlib;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicReference;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;

import com.poixson.pluginlib.charts.pxnPluginsChart;
import com.poixson.pluginlib.events.PlayerMoveManager;
import com.poixson.pluginlib.events.PluginSaveManager;
import com.poixson.pluginlib.tools.FreedMapStore;
import com.poixson.pluginlib.tools.plugin.xJavaPlugin;
import com.poixson.pluginlib.tools.updatechecker.UpdateCheckManager;
import com.poixson.tools.Keeper;


public class pxnPluginLib extends xJavaPlugin {
	@Override public int getSpigotPluginID() { return 107049; }
	@Override public int getBStatsID() {       return 17785;  }
	public static final String LOG_PREFIX = "[pxnCommon] ";
	public static final String CHAT_PREFIX = ChatColor.AQUA + LOG_PREFIX + ChatColor.WHITE;

	protected final Keeper keeper;

	protected final CopyOnWriteArraySet<xJavaPlugin> plugins = new CopyOnWriteArraySet<xJavaPlugin>();
	protected final AtomicReference<pxnPluginsChart> pluginsListener = new AtomicReference<pxnPluginsChart>(null);
	protected final AtomicReference<UpdateCheckManager> checkManager = new AtomicReference<UpdateCheckManager>(null);
	protected final AtomicReference<PlayerMoveManager>  moveManager  = new AtomicReference<PlayerMoveManager>(null);
	protected final AtomicReference<FreedMapStore>         freedMaps = new AtomicReference<FreedMapStore>(null);
	protected final AtomicReference<PluginSaveManager>  saveListener = new AtomicReference<PluginSaveManager>(null);

	// ticks per second
	protected final AtomicReference<TicksPerSecond> tpsManager   = new AtomicReference<TicksPerSecond>(null);



	public pxnPluginLib() {
		super(pxnPluginLib.class);
		this.keeper = Keeper.get();
	}



	@Override
	public void onEnable() {
		final ServicesManager services = Bukkit.getServicesManager();
		services.register(pxnPluginLib.class, this, this, ServicePriority.Normal);
		// plugins listener
		{
			final pxnPluginsChart listener = new pxnPluginsChart(this);
			final pxnPluginsChart previous = this.pluginsListener.getAndSet(listener);
			if (previous != null)
				previous.unregister();
			listener.register();
		}
		// ticks monitor
		{
			final TicksPerSecond manager = new TicksPerSecond(this);
			final TicksPerSecond previous = this.tpsManager.getAndSet(manager);
			if (previous != null)
				previous.stop();
			manager.start();
			services.register(TicksPerSecond.class, manager, this, ServicePriority.Normal);
		}
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
		// player move listeners
		{
			final PlayerMoveManager manager = new PlayerMoveManager(this);
			final PlayerMoveManager previous = this.moveManager.getAndSet(manager);
			if (previous != null)
				previous.unregister();
			manager.register();
		}
		// save listener
		{
			final PluginSaveManager listener = new PluginSaveManager(this);
			final PluginSaveManager previous = this.saveListener.getAndSet(listener);
			if (previous != null)
				previous.unregister();
			listener.register();
		}
		// custom stats
		{
			final Metrics metrics = this.metrics.get();
			if (metrics != null) {
				metrics.addCustomChart(pxnPluginsChart.GetChart(this));
			}
		}
	}

	@Override
	public void onDisable() {
		super.onDisable();
		final ServicesManager services = Bukkit.getServicesManager();
		// save listener
		{
			final PluginSaveManager listener = this.saveListener.getAndSet(null);
			if (listener != null)
				listener.unregister();
		}
		// ticks monitor
		{
			final TicksPerSecond manager = this.tpsManager.getAndSet(null);
			if (manager != null) {
				manager.stop();
				services.unregister(manager);
			}
		}
		// update check manager
		{
			final UpdateCheckManager manager = this.checkManager.getAndSet(null);
			if (manager != null)
				manager.stop();
		}
		// freed map store
		{
			final FreedMapStore store = this.freedMaps.getAndSet(null);
			if (store != null) {
				store.unregister();
				try {
					store.save();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		// plugins listener
		{
			final pxnPluginsChart listener = this.pluginsListener.getAndSet(null);
			if (listener != null)
				listener.unregister();
		}
	}



	// -------------------------------------------------------------------------------
	// configs



	@Override
	protected void loadConfigs() {
		this.mkPluginDir();
		// config.yml
		{
			final FileConfiguration cfg = this.getConfig();
			this.config.set(cfg);
			this.configDefaults(cfg);
			cfg.options().copyDefaults(true);
			super.saveConfig();
		}
	}
	@Override
	protected void saveConfigs() {
		// config.yml
		super.saveConfig();
	}
	@Override
	protected void configDefaults(final FileConfiguration cfg) {
	}



	// -------------------------------------------------------------------------------



	public static pxnPluginLib GetCommonPlugin() {
		final pxnPluginLib plugin = Bukkit.getServicesManager().load(pxnPluginLib.class);
		if (plugin == null) throw new RuntimeException("pxnPluginLib not loaded");
		return plugin;
	}
	public static TicksPerSecond GetTicksManager() {
		final pxnCommonPlugin plugin = GetCommonPlugin();
		return plugin.tpsManager.get();
	}
	public static FreedMapStore GetFreedMapStore() {
		final pxnPluginLib plugin = GetCommonPlugin();
		// already loaded
		{
			final FreedMapStore store = plugin.freedMaps.get();
			if (store != null)
				return store;
		}
		// load map store
		{
			final String path = plugin.getDataFolder().getAbsolutePath();
			final FreedMapStore store = new FreedMapStore(plugin, path);
			if (plugin.freedMaps.compareAndSet(null, store)) {
				try {
					store.load();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				store.register();
				final ServicesManager services = Bukkit.getServicesManager();
				services.register(FreedMapStore.class, store, plugin, ServicePriority.Normal);
				return store;
			}
		}
		return GetFreedMapStore();
	}



	public static <T extends xJavaPlugin> boolean RegisterPluginPXN(final T plugin) {
		return GetCommonPlugin().registerPluginPXN(plugin);
	}
	public <T extends xJavaPlugin> boolean registerPluginPXN(final T plugin) {
		for (final xJavaPlugin p : this.plugins) {
			if (p.getClass().isInstance(plugin))
				throw new RuntimeException("Plugin already registered? " + plugin.getClass().getName());
		}
		return this.plugins.add(plugin);
	}

	public static <T extends xJavaPlugin> boolean UnregisterPluginPXN(final T plugin) {
		return GetCommonPlugin().unregisterPluginPXN(plugin);
	}
	public <T extends xJavaPlugin> boolean unregisterPluginPXN(final T plugin) {
		return this.plugins.remove(plugin);
	}

	public int getPluginsCount() {
		return this.plugins.size();
	}



}
