package com.poixson.commonmc.tools.wizards;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import com.poixson.commonmc.tools.plugin.xJavaPlugin;
import com.poixson.commonmc.tools.wizards.steps.WizardStep;
import com.poixson.commonmc.utils.BukkitUtils;


public class Wizard<T extends xJavaPlugin> {

	protected final String logPrefix;
	protected final String chatPrefix;

	protected final T plugin;
	protected final Player player;

	protected final LinkedList<WizardStep> steps = new LinkedList<WizardStep>();

	// timeout
	protected final BukkitTask timeoutTask;
	protected final AtomicInteger timeoutCount = new AtomicInteger(0);
	protected final int timeoutSeconds;



	public Wizard(final T plugin, final Player player,
			final String logPrefix, final String chatPrefix) {
		this.logPrefix  = logPrefix;
		this.chatPrefix = chatPrefix;
		this.plugin = plugin;
		this.player = player;
		// timeout
		{
			this.timeoutSeconds = 30;
			final Runnable run = new Runnable() {
				@Override
				public void run() {
					Wizard.this.timeout();
				}
			};
			this.timeoutTask =
				Bukkit.getScheduler()
					.runTaskTimer(plugin, run, 20, 20);
		}
	}



	public void start() {
		this.next();
	}
	public void next() {
		final Runnable run = new Runnable() {
			@Override
			public void run() {
				Wizard.this.doNext();
			}
		};
		Bukkit.getScheduler()
			.runTask(this.plugin, run);
	}
	protected void doNext() {
		for (final WizardStep step : this.steps) {
			if (!step.isCompleted()) {
				try {
					step.run();
				} catch (Exception e) {
					this.sendMessage(this.chatPrefix + "ERROR: " + e.getMessage());
					throw(e);
				}
				return;
			}
		}
		this.finished();
	}
	public void finished() {
		try {
			this.timeoutTask.cancel();
		} catch (IllegalStateException ignore) {}
	}
	public void cancel() {
		try {
			this.timeoutTask.cancel();
		} catch (IllegalStateException ignore) {}
		for (final WizardStep step : this.steps) {
			step.close();
		}
	}



	public void timeout() {
		final int count = this.timeoutCount.incrementAndGet();
		if (count >= this.timeoutSeconds) {
			this.sendMessage(this.chatPrefix + "Wizard timeout.");
			this.sendMessage("");
			this.cancel();
		}
	}
	public void resetTimeout() {
		this.timeoutCount.set(0);
	}



	public void addStep(final WizardStep step) {
		this.steps.addLast(step);
		this.resetTimeout();
	}
	public WizardStep[] getSteps() {
		return this.steps.toArray(new WizardStep[0]);
	}
	public int getStepsCount() {
		return this.steps.size();
	}



	public T getPlugin() {
		return this.plugin;
	}



	public Player getPlayer() {
		return this.player;
	}
	public boolean isPlayer(final Player player) {
		return BukkitUtils.EqualsPlayer(player, this.player);
	}



	public void sendMessage(final String msg) {
		this.player.sendMessage(msg);
	}



}
