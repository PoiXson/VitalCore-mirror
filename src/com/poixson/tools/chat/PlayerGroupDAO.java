package com.poixson.tools.chat;

import static com.poixson.utils.LocationUtils.Distance3D;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicReference;

import org.bukkit.World;

import com.poixson.tools.LocationSafe;


public class PlayerGroupDAO {

	public final String channel;

	public final AtomicReference<UUID>         center_player = new AtomicReference<UUID>(null);
	public final AtomicReference<LocationSafe> center_loc    = new AtomicReference<LocationSafe>(null);

	public final CopyOnWriteArraySet<UUID> players = new CopyOnWriteArraySet<UUID>();



	public PlayerGroupDAO(final String channel) {
		this.channel = channel;
	}



	public void updateCenter(final Map<UUID, LocationSafe> online) {
		World world = null;
		{
			final LocationSafe center = this.center_loc.get();
			if (center != null)
				world = center.getWorld();
		}
		double x = 0.0;
		double y = 0.0;
		double z = 0.0;
		int count = 0;
		for (final UUID uuid : this.players) {
			final LocationSafe loc = online.get(uuid);
			if (loc != null) {
				count++;
				x += loc.getX();
				y += loc.getY();
				z += loc.getZ();
				if (world == null)
					world = loc.getWorld();
			}
		}
		final LocationSafe center = new LocationSafe(world, x/count, y/count, z/count);
		this.center_loc.set(center);
		// find center player
		{
			double distance = Double.MAX_VALUE;
			UUID nearest = null;
			for (final UUID uuid : this.players) {
				final LocationSafe loc = online.get(uuid);
				if (loc != null) {
					final double dist = Distance3D(center, loc);
					if (distance > dist) {
						distance = dist;
						nearest = uuid;
					}
				}
			}
			this.center_player.set(nearest);
		}
	}



}
