package com.poixson.tools.wizards.steps;

import static com.poixson.utils.BukkitUtils.ComponentToString;
import static com.poixson.utils.BukkitUtils.EqualsPlayer;

import java.util.concurrent.atomic.AtomicReference;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import com.poixson.tools.xJavaPlugin;
import com.poixson.tools.wizards.Wizard;

import io.papermc.paper.event.player.AsyncChatEvent;


public abstract class WizardStep_Ask<T extends xJavaPlugin>
extends WizardStep<T> implements Listener {

	protected final String question;

	protected final AtomicReference<String> answer = new AtomicReference<String>(null);



	public WizardStep_Ask(final Wizard<T> wizard,
			final String logPrefix, final String chatPrefix,
			final String question) {
		super(
			wizard,
			logPrefix,
			chatPrefix
		);
		this.question   = question;
	}



	@Override
	public void run() {
		this.sendClear();
		this.sendProgress(this.question);
		Bukkit.getPluginManager()
			.registerEvents(this, this.wizard.getPlugin());
		this.wizard.resetTimeout();
	}



	@EventHandler
	public void onPlayerChat(final AsyncChatEvent event) {
		if (this.isCompleted()) {
			this.close();
			return;
		}
		if (!EqualsPlayer(this.getPlayer(), event.getPlayer()))
			return;
		event.setCancelled(true);
		this.answer.set( ComponentToString(event.message()) );
		if (!this.validateAnswer()) {
			this.answer.set(null);
			this.sendMessage("Invalid answer. Please try again");
			return;
		}
		this.sendReply();
		if (this.state.compareAndSet(null, Boolean.TRUE))
			this.wizard.next();
	}



	public abstract boolean validateAnswer();
	public abstract void sendReply();



	@Override
	public void close() {
		super.close();
		HandlerList.unregisterAll(this);
	}



	public String getAnswer() {
		return this.answer.get();
	}



}
