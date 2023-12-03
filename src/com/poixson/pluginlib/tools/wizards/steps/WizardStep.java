package com.poixson.pluginlib.tools.wizards.steps;

import java.io.Closeable;
import java.util.concurrent.atomic.AtomicReference;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.poixson.pluginlib.tools.plugin.xJavaPlugin;
import com.poixson.pluginlib.tools.wizards.Wizard;
import com.poixson.utils.Utils;


public abstract class WizardStep<T extends xJavaPlugin>
implements Runnable, Closeable {

	protected final String logPrefix;
	protected final String chatPrefix;

	protected final Wizard<T> wizard;

	protected final int stepIndex;

	// success/failed
	protected final AtomicReference<Boolean> state = new AtomicReference<Boolean>(null);



	public WizardStep(final Wizard<T> wizard, final String logPrefix, final String chatPrefix) {
		this.wizard     = wizard;
		this.stepIndex  = wizard.getStepsCount() + 1;
		this.logPrefix  = logPrefix;
		this.chatPrefix = chatPrefix;
	}



	@Override
	public abstract void run();

	@Override
	public void close() {
		this.state.compareAndSet(null, Boolean.FALSE);
		this.wizard.resetTimeout();
	}



	public boolean isCompleted() {
		return (this.state.get() != null);
	}
	public boolean isSuccess() {
		final Boolean result = this.state.get();
		if (result == null)
			return false;
		return result.booleanValue();
	}
	public boolean isCanceled() {
		final Boolean result = this.state.get();
		if (result == null)
			return false;
		return ! result.booleanValue();
	}



	public JavaPlugin getPlugin() {
		return this.wizard.getPlugin();
	}
	public Player getPlayer() {
		return this.wizard.getPlayer();
	}



	public void sendMessage(final String msg) {
		if (Utils.isEmpty(msg))
			this.wizard.sendMessage("");
		else
			this.wizard.sendMessage(this.chatPrefix + msg);
	}
	public void sendProgress(final String msg) {
		this.wizard.sendMessage(
			String.format(
				"[%d%%] %s",
				Long.valueOf(Math.round( ((double)this.stepIndex) / ((double)this.wizard.getStepsCount()) * 100.0)),
				msg
			)
		);
	}
	public void sendClear() {
		this.wizard.sendMessage("");
	}



}
