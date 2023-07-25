package com.poixson.commonmc.tools.scripting;

import java.io.OutputStream;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.poixson.utils.StringUtils;


public class LocalOut extends OutputStream {

	protected final Location loc;

	protected final StringBuilder buffer = new StringBuilder();



	public LocalOut(final Location loc) {
		super();
		this.loc = loc;
	}



	@Override
	public void write(final int chr) {
		if (chr == '\r') return;
		if (chr == '\n') {
			if (this.buffer.length() == 0)
				this.buffer.append(' ');
			this.flush();
		} else {
			this.buffer.append((char)chr);
		}
	}



	@Override
	public void flush() {
		String str = this.buffer.toString();
		this.buffer.setLength(0);
		str = StringUtils.ceTrim(str, '\n');
		if (str.isEmpty())
			str = " ";
		final World world = this.loc.getWorld();
		Location loc_player;
		double distance;
		for (final Player player : Bukkit.getOnlinePlayers()) {
			loc_player = player.getLocation();
			if (loc_player.getWorld().equals(world)) {
				distance = loc_player.distance(this.loc);
				if (distance < 10.0)
					player.sendMessage(str);
			}
		}
	}
	@Override
	public void close() {
		this.flush();
	}



}
