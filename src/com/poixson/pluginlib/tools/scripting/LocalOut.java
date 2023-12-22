package com.poixson.pluginlib.tools.scripting;

import static com.poixson.utils.Utils.IsEmpty;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.poixson.tools.abstractions.OutputStreamLineRemapper;


public class LocalOut extends OutputStreamLineRemapper {

	protected final Location loc;
	protected final int radius;



	public LocalOut(final Location loc, final int radius) {
		super();
		this.loc    = loc;
		this.radius = radius;
	}



	@Override
	public void print(final String line) {
		for (final Player player : Bukkit.getOnlinePlayers()) {
			if (this.isPlayerNear(player))
				player.sendMessage(line);
		}
	}
	public void println(final String line) {
		if (IsEmpty(line)) this.print("\n");
		else               this.print(line+"\n");
	}
	public void println() {
		this.println("\n");
	}



	public boolean isPlayerNear(final Player player) {
		return isNear(player.getLocation());
	}
	public boolean isNear(final Location loc) {
		return (loc.distance(this.loc) <= this.radius);
	}



}
