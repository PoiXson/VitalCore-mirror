package com.poixson.tools.chat;

import static com.poixson.utils.Utils.IsEmpty;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class ChatFormatter {

	protected final Map<String, String> formats = new HashMap<String, String>();



	public ChatFormatter() {
	}



	public static String Format(final ChatFormatter formatter,
			final Player player, final String msg, final ChatDelivery delivery) {
		return Format(
			formatter,
			player,
			msg,
			(delivery==null ? null : delivery.toString())
		);
	}
	public static String Format(final ChatFormatter formatter,
			final Player player, final String msg, final String flag) {
		return (
			formatter == null
			? msg
			: formatter.format(player, msg, flag)
		);
	}
	public static String Format(final ChatFormatter formatter,
			final Player player, final String msg) {
		return Format(formatter, player, msg, (String)null);
	}

	public String format(final Player player, final String msg, final String flag) {
		if (IsEmpty(flag)) {
			if (flag == "default")
				throw new RuntimeException("default chat format not found");
			return this.format(player, msg, "default");
		}
		String result = this.formats.get(flag);
		// no format
		if (result == null) {
			result = msg;
		// <MSG> tag
		} else {
			final int pos = result.indexOf("<MSG>");
			if (pos >= 0) result = result.substring(0, pos) + msg + result.substring(pos+5);
			else          result = result + msg;
		}
		// <PLAYER>
		result = result.replace("<PLAYER>", player.getDisplayName());
		// colors
		for (final ChatColor color : ChatColor.values())
			result = result.replace("<"+color.name()+">", color.toString());
		return result;
	}
	public String format(final Player player, final String msg) {
		return this.format(player, msg, null);
	}



	public Map<String, String> getFormats() {
		return this.formats;
	}
	public void addFormats(final Map<String, String> formats) {
		final Iterator<Entry<String, String>> it = formats.entrySet().iterator();
		while (it.hasNext()) {
			final Entry<String, String> entry = it.next();
			this.formats.put(entry.getKey(), entry.getValue());
		}
	}
	public void clearFormats() {
		this.formats.clear();
	}



}
