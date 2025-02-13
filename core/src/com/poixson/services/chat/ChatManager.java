package com.poixson.tools.chat;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import com.poixson.tools.xListener;
import com.poixson.vitalcore.VitalCorePlugin;

import io.papermc.paper.event.player.AsyncChatEvent;


public class ChatManager implements xListener {

	protected final VitalCorePlugin plugin;

	protected final PlayerLocalGroups local;



	public ChatManager(final VitalCorePlugin plugin) {
		this.plugin = plugin;
		this.local = null;
	}
	public ChatManager(final VitalCorePlugin plugin,
			final double range, final double grace) {
		this.plugin = plugin;
		this.local = new PlayerLocalGroups(plugin, range, grace);
	}



	public void register() {
		if (this.local != null)
			this.local.start();
		xListener.super.register(this.plugin);
	}
	@Override
	public void unregister() {
		xListener.super.unregister();
		if (this.local != null)
			this.local.stop();
	}



	// -------------------------------------------------------------------------------
	// listeners



	@EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
	public void onPlayerChat(final AsyncChatEvent event) {
		event.setCancelled(true);
		final ChatMessage msg = new ChatMessage(this, event);
		msg.runTask(this.plugin);
	}



	// -------------------------------------------------------------------------------



	public boolean hasRadio(final Player player) {
//TODO
		return false;
	}
	public boolean hasRadioInHand(final Player player) {
//TODO
		return false;
	}



}
