package com.poixson.tools.localchat;

import java.util.concurrent.atomic.AtomicReference;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.poixson.pluginlib.pxnPluginLib;
import com.poixson.tools.xListener;
import com.poixson.tools.localchat.LocalChatMessage.ChatDelivery;
import com.poixson.utils.BukkitUtils;


public class LocalChatManager implements xListener {

	protected final pxnPluginLib plugin;

	protected final String chat_format;
	protected final double local_distance;



	public LocalChatManager(final pxnPluginLib plugin,
			final String chat_format, final double local_distance) {
		this.plugin         = plugin;
		this.chat_format    = BukkitUtils.FormatColors(chat_format);
		this.local_distance = local_distance;
	}



	public void register() {
		xListener.super.register(this.plugin);
	}



	@EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
	public void onPlayerChat(final AsyncPlayerChatEvent event) {
		event.setCancelled(true);
		this.runChatMessage(new LocalChatMessage(this, event));
	}



	public void runChatMessage(final LocalChatMessage msg) {
		new BukkitRunnable() {
			final AtomicReference<LocalChatMessage> msg = new AtomicReference<LocalChatMessage>(null);
			public BukkitRunnable init(final LocalChatMessage msg) {
				this.msg.set(msg);
				return this;
			}
			@Override
			public void run() {
				final LocalChatMessage msg = this.msg.get();
				if (msg != null)
					msg.run();
			}
		}.init(msg)
		.runTask(this.plugin);
	}



	public double getLocalDistance() {
		return this.local_distance;
	}



	public boolean hasRadio(final Player player) {
//TODO
return true;
	}



	public String format(final Player player_from, final ChatDelivery deliver, final String msg) {
		String result = this.chat_format
			.replace("<PLAYER>", player_from.getName());
		// local chat
		if (ChatDelivery.DELIVER_LOCAL.equals(deliver)) {
			result = result
				.replace("<LOCAL>",  "")
				.replace("</LOCAL>", "");
		} else {
			final int tag_start = result.indexOf("<LOCAL>" );
			final int tag_end   = result.indexOf("</LOCAL>");
			if (tag_start >= 0 && tag_end > 0) {
				result =
					result.substring(0, tag_start) +
					result.substring(tag_end + 8);
			}
		}
		// radio chat
		if (ChatDelivery.DELIVER_RADIO.equals(deliver)) {
			result = result
				.replace("<RADIO>",  "")
				.replace("</RADIO>", "");
		} else {
			final int tag_start = result.indexOf("<RADIO>" );
			final int tag_end   = result.indexOf("</RADIO>");
			if (tag_start >= 0 && tag_end > 0) {
				result =
					result.substring(0, tag_start) +
					result.substring(tag_end + 8);
			}
		}
		return result + msg;
	}



}
