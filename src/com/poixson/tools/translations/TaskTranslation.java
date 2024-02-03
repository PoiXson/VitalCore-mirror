package com.poixson.tools.translations;

import org.bukkit.entity.BlockDisplay;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.joml.Vector3f;

import com.poixson.tools.abstractions.xStartStop;
import com.poixson.tools.dao.Dabc;


public class TaskTranslation extends BukkitRunnable implements xStartStop {
	public static final long MAGIC_TICKS = 3L;

	protected final JavaPlugin plugin;

	public final BlockDisplay[] entities;
	public final Dabc movement;

	protected final int delay;
	protected final int duration;



	public TaskTranslation(final JavaPlugin plugin,
			final BlockDisplay[] entities, final Dabc movement,
			final int delay, final int duration) {
		this.plugin   = plugin;
		this.entities = entities;
		this.movement = movement;
		this.delay    = (delay<0 ? 0 : delay);
		this.duration = duration;
	}
	public TaskTranslation(final JavaPlugin plugin,
			final BlockDisplay entity, final Dabc movement,
			final int delay, final int duration) {
		this(
			plugin,
			new BlockDisplay[] { entity },
			movement, delay, duration
		);
	}



	@Override
	public void start() {
		this.runTaskLater(this.plugin, MAGIC_TICKS+this.delay);
	}
	@Override
	public void stop() {
		try {
			this.cancel();
		} catch (Exception ignore) {}
	}



	@Override
	public void run() {
		for (final BlockDisplay entity : this.entities) {
			entity.setPersistent(false);
			entity.setInterpolationDelay(-1);
			entity.setInterpolationDuration(this.duration);
			final Transformation trans = entity.getTransformation();
			final Vector3f vec = trans.getTranslation();
			vec.x += (float) this.movement.a;
			vec.y += (float) this.movement.b;
			vec.z += (float) this.movement.c;
			entity.setTransformation(trans);
		}
	}



}
