package com.poixson.tools.localchat;

import static com.poixson.utils.LocationUtils.DistanceFast3D;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;


public class LocalChatMessage extends BukkitRunnable {

	protected final LocalChatManager manager;

	protected final Player player_from;
	protected final Location loc_from;
	protected final String msg;

	public enum ChatDelivery {
		DELIVER_LOCAL,
		DELIVER_RADIO
	};



	public LocalChatMessage(final LocalChatManager manager, final AsyncPlayerChatEvent event) {
		this.manager = manager;
		this.player_from = event.getPlayer();
		this.loc_from    = this.player_from.getLocation();
		this.msg         = event.getMessage();
	}



	@Override
	public void run() {
		// message to self
		this.sendMessage(this.player_from, null);
		final double local_distance = this.manager.getLocalDistance();
		final HashMap<Player, ChatDelivery> deliver = new HashMap<Player, ChatDelivery>();
		final Collection<? extends Player> online = Bukkit.getOnlinePlayers();
		// local
		for (final Player player : online) {
			if (this.player_from.equals(player)) continue;
			final Location loc = player.getLocation();
			final double distance = DistanceFast3D(this.loc_from, loc);
			if (distance >= 0.0
			&&  distance < local_distance)
				deliver.put(player, ChatDelivery.DELIVER_LOCAL);
		}
		// radio
		if (this.manager.hasRadio(this.player_from)) {
			for (final Player player : online) {
				if (this.player_from.equals(player)) continue;
				if (!deliver.containsKey(player)) {
					if (this.manager.hasRadio(player))
						deliver.put(player, ChatDelivery.DELIVER_RADIO);
				}
			}
		}
		// send messages
		final Iterator<Entry<Player, ChatDelivery>> it = deliver.entrySet().iterator();
		while (it.hasNext()) {
			final Entry<Player, ChatDelivery> entry = it.next();
			this.sendMessage(entry.getKey(), entry.getValue());
		}
	}



	protected void sendMessage(final Player player_to, final ChatDelivery deliver) {
		final String msg = this.manager.format(this.player_from, deliver, this.msg);
		player_to.sendMessage(msg);
	}



}
