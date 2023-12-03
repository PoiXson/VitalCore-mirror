package com.poixson.pluginlib.tools.updatechecker;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.poixson.pluginlib.pxnPluginLib;
import com.poixson.pluginlib.tools.plugin.xListener;
import com.poixson.utils.Utils;


public class PlayerJoinListener extends xListener<pxnPluginLib> {

	protected final UpdateCheckManager manager;



	public PlayerJoinListener(final pxnPluginLib plugin, final UpdateCheckManager manager) {
		super(plugin);
		this.manager = manager;
	}



	@EventHandler(priority=EventPriority.LOWEST)
	public void onPlayerJoin(final PlayerJoinEvent event) {
		if (this.manager.hasUpdate()) {
			final Player player = event.getPlayer();
			if (player.isOp() || player.hasPermission("pxnpluginlib.updates")) {
				(new BukkitRunnable() {
					@Override
					public void run() {
						final UpdateCheckerTask[] updates = PlayerJoinListener.this.manager.getUpdatesToPlayers();
						String msg;
						for (final UpdateCheckerTask task : updates) {
							msg = task.getUpdateMessage();
							if (Utils.notEmpty(msg))
								player.sendMessage(msg);
						}
					}
				}).runTaskLater(this.plugin, 10L);
			}
		}
	}



}
