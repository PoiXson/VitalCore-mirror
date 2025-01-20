package com.poixson.tools.commands;

import java.util.Arrays;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.poixson.tools.xJavaPlugin;
import com.poixson.tools.abstractions.xStartStop;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;


public abstract class PluginCommandsHolder<P extends xJavaPlugin<P>> implements xStartStop {

	public final P plugin;



	public PluginCommandsHolder(final P plugin) {
		this.plugin = plugin;
	}



	@Override
	public void start() {
		this.register_holder();
	}
	@Override
	public void stop() {
	}



	protected void register_holder() {
		this.plugin.getLifecycleManager()
			.registerEventHandler(
				LifecycleEvents.COMMANDS,
				event -> this.register_commands(event.registrar())
			);
	}

	protected abstract void register_commands(final Commands registrar);



	protected void register_cmd(final Commands registrar,
			final ArgumentBuilder<CommandSourceStack, ?> builder,
			final String desc, final String[] aliases) {
		registrar.register(
			(LiteralCommandNode<CommandSourceStack>) builder.build(),
			desc,
			Arrays.asList(aliases)
		);
	}



}
