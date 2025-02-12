package com.poixson.tools.wizards;

import static com.poixson.utils.BukkitUtils.EqualsPlayer;
import static com.poixson.utils.BukkitUtils.SafeCancel;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.poixson.tools.xJavaPlugin;
import com.poixson.tools.wizards.steps.WizardStep;


public class Wizard<P extends xJavaPlugin<P>> {

	protected final String logPrefix;
	protected final String chatPrefix;

	protected final P plugin;
	protected final Player player;

	protected final LinkedList<WizardStep<P>> steps = new LinkedList<WizardStep<P>>();

	// timeout
	protected final BukkitRunnable task_timeout;
	protected final AtomicInteger timeoutCount = new AtomicInteger(0);
	protected final int timeoutSeconds;



	public Wizard(final P plugin, final Player player,
			final String logPrefix, final String chatPrefix) {
		this.logPrefix  = logPrefix;
		this.chatPrefix = chatPrefix;
		this.plugin = plugin;
		this.player = player;
		// timeout
		{
			this.timeoutSeconds = 30;
			this.task_timeout = new BukkitRunnable() {
				@Override
				public void run() {
					Wizard.this.timeout();
				}
			};
			this.task_timeout.runTaskTimer(plugin, 20L, 20L);
		}
	}



	public void start() {
		this.next();
	}
	public void next() {
		new BukkitRunnable() {
			@Override
			public void run() {
				Wizard.this.doNext();
			}
		}.runTask(this.plugin);
	}
	protected void doNext() {
		for (final WizardStep<P> step : this.steps) {
			if (!step.isCompleted()) {
				try {
					step.run();
				} catch (Exception e) {
					this.sendMessage(String.format("%sERROR: %s", this.chatPrefix, e.getMessage()));
					throw(e);
				}
				return;
			}
		}
		this.finished();
	}
	public void finished() {
		SafeCancel(this.task_timeout);
	}
	public void cancel() {
		SafeCancel(this.task_timeout);
		for (final WizardStep<P> step : this.steps)
			step.close();
	}



	public void timeout() {
		final int count = this.timeoutCount.incrementAndGet();
		if (count >= this.timeoutSeconds) {
			this.sendMessage(this.chatPrefix+"Wizard timeout.");
			this.sendMessage("");
			this.cancel();
		}
	}
	public void resetTimeout() {
		this.timeoutCount.set(0);
	}



	public void addStep(final WizardStep<P> step) {
		this.steps.addLast(step);
		this.resetTimeout();
	}
	@SuppressWarnings("unchecked")
	public WizardStep<P>[] getSteps() {
		return this.steps.toArray(new WizardStep[0]);
	}
	public int getStepsCount() {
		return this.steps.size();
	}



	public P getPlugin() {
		return this.plugin;
	}



	public Player getPlayer() {
		return this.player;
	}
	public boolean isPlayer(final Player player) {
		return EqualsPlayer(player, this.player);
	}



	public void sendMessage(final String msg) {
		this.player.sendMessage(msg);
	}



}
