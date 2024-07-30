package com.poixson.tools.chat;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.poixson.pluginlib.pxnPluginLib;
import com.poixson.tools.xListener;


public class ChatManager implements xListener {

	protected final pxnPluginLib plugin;

	protected final PlayerLocalGroups local;



	public ChatManager(final pxnPluginLib plugin) {
		this.plugin = plugin;
		this.local = null;
	}
	public ChatManager(final pxnPluginLib plugin,
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
	public void onPlayerChat(final AsyncPlayerChatEvent event) {
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
