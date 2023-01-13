package com.poixson.commonmc.tools.wizards.steps;

import java.util.concurrent.atomic.AtomicReference;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.poixson.commonmc.tools.wizards.Wizard;
import com.poixson.commonmc.utils.BukkitUtils;


public abstract class WizardStep_Ask extends WizardStep implements Listener {

	protected final String question;

	protected final AtomicReference<String> answer = new AtomicReference<String>(null);



	public WizardStep_Ask(final Wizard wizard,
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
	public void onPlayerChat(final AsyncPlayerChatEvent event) {
		if (this.isCompleted()) {
			this.close();
			return;
		}
		if (!BukkitUtils.MatchPlayer(this.getPlayer(), event.getPlayer()))
			return;
		event.setCancelled(true);
		this.answer.set(event.getMessage());
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
