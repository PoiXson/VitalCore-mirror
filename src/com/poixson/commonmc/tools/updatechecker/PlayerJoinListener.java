package com.poixson.commonmc.tools.updatechecker;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.poixson.commonmc.pxnCommonPlugin;
import com.poixson.utils.Utils;


public class PlayerJoinListener implements Listener {

	protected final pxnCommonPlugin plugin;
	protected final UpdateCheckManager manager;



	public PlayerJoinListener(final pxnCommonPlugin plugin, final UpdateCheckManager manager) {
		this.plugin  = plugin;
		this.manager = manager;
	}



	@EventHandler(priority=EventPriority.LOWEST)
	public void onPlayerJoin(final PlayerJoinEvent event) {
		if (this.manager.hasUpdate()) {
			final Player player = event.getPlayer();
			if (player.isOp() || player.hasPermission("pxncommon.updates")) {
				(new BukkitRunnable() {
					@Override
					public void run() {
						final UpdateCheckerDAO[] updates = PlayerJoinListener.this.manager.getUpdatesToPlayers();
						String msg;
						for (final UpdateCheckerDAO dao : updates) {
							msg = dao.getUpdateMessage();
							if (Utils.notEmpty(msg))
								player.sendMessage(msg);
						}
					}
				}).runTaskLater(this.plugin, 10L);
			}
		}
	}



	public void register() {
		Bukkit.getPluginManager()
			.registerEvents(this, this.plugin);
	}
	public void unregister() {
		HandlerList.unregisterAll(this);
	}



}
