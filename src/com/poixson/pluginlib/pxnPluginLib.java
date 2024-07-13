package com.poixson.pluginlib;

import static com.poixson.utils.Utils.GetMS;

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
import com.poixson.pluginlib.commands.Commands;
import com.poixson.tools.FreedMapStore;
import com.poixson.tools.Keeper;
import com.poixson.tools.PlayerMoveMonitor;
import com.poixson.tools.SaveMonitor;
import com.poixson.tools.xJavaPlugin;
import com.poixson.tools.xTime;
import com.poixson.tools.localchat.LocalChatManager;
import com.poixson.tools.updatechecker.UpdateCheckManager;


public class pxnPluginLib extends xJavaPlugin {
	@Override public int getSpigotPluginID() { return 107049; }
	@Override public int getBStatsID() {       return 20434;  }
	public static final String CHAT_PREFIX = ChatColor.DARK_AQUA+"[pxnPluginLib] "+ChatColor.WHITE;

	public static final double DEFAULT_CHAT_LOCAL_DISTANCE = 120.0;

	protected final Keeper keeper;
	protected final long time_start;

	protected final CopyOnWriteArraySet<xJavaPlugin> plugins = new CopyOnWriteArraySet<xJavaPlugin>();

	protected final AtomicReference<pxnPluginsChart>  pluginsListener = new AtomicReference<pxnPluginsChart>(null);
	protected final AtomicReference<UpdateCheckManager> updateChecker = new AtomicReference<UpdateCheckManager>(null);
	protected final AtomicReference<FreedMapStore>      freedMaps     = new AtomicReference<FreedMapStore>(null);
	protected final AtomicReference<LocalChatManager>   chatManager   = new AtomicReference<LocalChatManager>(null);
	protected final AtomicReference<PlayerMoveMonitor>  moveMonitor   = new AtomicReference<PlayerMoveMonitor> (null);
	protected final AtomicReference<SaveMonitor>        saveMonitor   = new AtomicReference<SaveMonitor>       (null);

	protected final AtomicReference<Commands> commands = new AtomicReference<Commands>(null);



	public pxnPluginLib() {
		super(pxnPluginLib.class);
		this.keeper = Keeper.get();
		this.time_start = GetMS();
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
			listener.register(this);
		}
		// update check manager
		if (this.enableCheckForUpdates()) {
			final UpdateCheckManager manager = new UpdateCheckManager(this);
			final UpdateCheckManager previous = this.updateChecker.getAndSet(manager);
			if (previous != null)
				previous.stop();
			services.register(UpdateCheckManager.class, manager, this, ServicePriority.Normal);
			manager.addPlugin(this, this.getSpigotPluginID(), this.getPluginVersion());
			// wait for server to start
			manager.startLater();
		}
		super.onEnable();
		// player move monitor
		{
			final PlayerMoveMonitor monitor = new PlayerMoveMonitor();
			final PlayerMoveMonitor previous = this.moveMonitor.getAndSet(monitor);
			if (previous != null)
				previous.unregister();
			monitor.register(this);
		}
		// save monitor
		{
			final SaveMonitor monitor = new SaveMonitor();
			final SaveMonitor previous = this.saveMonitor.getAndSet(monitor);
			if (previous != null)
				previous.unregister();
			monitor.register(this);
		}
		// commands
		{
			final Commands commands = new Commands(this);
			final Commands previous = this.commands.getAndSet(commands);
			if (previous != null)
				previous.close();
		}
		// chat
		if (enableLocalChat()) {
			final String chat_format    = this.getChatFormat();
			final double local_distance = this.getChatLocalDistance();
			final LocalChatManager manager = new LocalChatManager(this, chat_format, local_distance);
			final LocalChatManager previous = this.chatManager.getAndSet(manager);
			if (previous != null)
				previous.unregister();
			manager.register();
		}
		// custom stats
		{
			final Metrics metrics = this.metrics.get();
			if (metrics != null) {
				metrics.addCustomChart(pxnPluginsChart.GetChart(this));
			}
		}
		// save
		this.setConfigChanged();
		this.saveConfigs();
	}

	@Override
	public void onDisable() {
		super.onDisable();
		// uptime
		this.log().info("Uptime: "+this.getUptimeFormatted());
		// commands
		{
			final Commands commands = this.commands.getAndSet(null);
			if (commands != null)
				commands.close();
		}
		// chat
		{
			final LocalChatManager manager = this.chatManager.getAndSet(null);
			if (manager != null)
				manager.unregister();
		}
		// save monitor
		{
			final SaveMonitor monitor = this.saveMonitor.getAndSet(null);
			if (monitor != null)
				monitor.unregister();
		}
		// player move monitor
		{
			final PlayerMoveMonitor monitor = this.moveMonitor.getAndSet(null);
			if (monitor != null)
				monitor.unregister();
		}
		// update check manager
		{
			final UpdateCheckManager manager = this.updateChecker.getAndSet(null);
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
		super.loadConfigs();
		// config.yml
		final FileConfiguration cfg = this.getConfig();
		this.config.set(cfg);
		this.configDefaults(cfg);
		cfg.options().copyDefaults(true);
	}
	@Override
	protected void configDefaults(final FileConfiguration config) {
		super.configDefaults(config);
		Commands.ConfigDefaults(config);
		config.addDefault("Check for Updates",      Boolean.TRUE );
		config.addDefault("Chat.Enable Local Chat", Boolean.FALSE);
		config.addDefault("Chat.Format", "<LOCAL><DARK_AQUA>[L]</LOCAL><RADIO><GOLD>[R]</RADIO><DARK_GREEN><<PLAYER>><WHITE> ");
		config.addDefault("Chat.Local Distance", Double.valueOf(DEFAULT_CHAT_LOCAL_DISTANCE));
	}



	public boolean enableCheckForUpdates() {
		return this.getConfig().getBoolean("Check for Updates");
	}



	public boolean enableLocalChat() {
		return this.getConfig().getBoolean("Chat.Enable Local Chat");
	}
	public String getChatFormat() {
		return this.getConfig().getString("Chat.Format");
	}
	public double getChatLocalDistance() {
		return this.getConfig().getDouble("Chat.Local Distance");
	}



	// -------------------------------------------------------------------------------



	public static pxnPluginLib GetCommonPlugin() {
		final pxnPluginLib plugin = Bukkit.getServicesManager().load(pxnPluginLib.class);
		if (plugin == null) throw new RuntimeException("pxnPluginLib not loaded");
		return plugin;
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
	public static PlayerMoveMonitor GetPlayerMoveMonitor() {
		return GetCommonPlugin().moveMonitor.get();
	}



	public static <T extends xJavaPlugin> boolean RegisterPluginPXN(final T plugin) {
		return GetCommonPlugin().registerPluginPXN(plugin);
	}
	public <T extends xJavaPlugin> boolean registerPluginPXN(final T plugin) {
		for (final xJavaPlugin p : this.plugins) {
			if (p.getClass().isInstance(plugin))
				throw new RuntimeException("Plugin already registered? "+plugin.getClass().getName());
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



	public long getUptime() {
		return GetMS() - this.time_start;
	}
	public String getUptimeFormatted() {
		final long uptime = (long) (Math.round(((double)this.getUptime())/1000.0) * 1000.0);
		return xTime.ToString(uptime, true, 3);
	}



}
