package com.poixson.tools.chat;

import static com.poixson.utils.BukkitUtils.ComponentToString;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.poixson.vitalcore.VitalCorePlugin;

import io.papermc.paper.event.player.AsyncChatEvent;


public class ChatMessage extends BukkitRunnable {

	protected final VitalCorePlugin plugin;
	protected final ChatManager manager;

	protected final Player player_from;
	protected final Location loc_from;
	protected final String msg;



	public ChatMessage(final ChatManager manager, final AsyncChatEvent event) {
		this.plugin  = manager.plugin;
		this.manager = manager;
		this.player_from = event.getPlayer();
		this.loc_from    = this.player_from.getLocation();
		this.msg = ComponentToString(event.message());
	}



	@Override
	public void run() {
		// message to self
		this.sendMessage(this.player_from, null);
		final PlayerGroupDAO group = this.manager.local.getGroup(this.player_from.getUniqueId());
		if (group != null) {
			final HashMap<Player, ChatDelivery> delivery = new HashMap<Player, ChatDelivery>();
			for (final UUID uuid : group.players) {
				final Player player = Bukkit.getPlayer(uuid);
				if (player != null
				&& !player.equals(this.player_from))
					delivery.put(player, ChatDelivery.DELIVER_LOCAL);
			}
			// send messages
			if (!delivery.isEmpty()) {
				final Iterator<Entry<Player, ChatDelivery>> it = delivery.entrySet().iterator();
				while (it.hasNext()) {
					final Entry<Player, ChatDelivery> entry = it.next();
					this.sendMessage(entry.getKey(), entry.getValue());
				}
			}
		}
	}



	protected void sendMessage(final Player player_to, final ChatDelivery delivery) {
		player_to.sendMessage(
			ChatFormatter.Format(
				this.plugin.getChatFormatter(),
				this.player_from,
				this.msg,
				delivery
			)
		);
	}



}
