package com.poixson.vitalcore.commands.types;

import java.util.concurrent.CompletableFuture;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.poixson.vitalcore.VitalCorePlugin;

import io.papermc.paper.command.brigadier.argument.CustomArgumentType;


public class ArgumentType_HomeName implements CustomArgumentType<String, String> {

	protected final VitalCorePlugin plugin;



	public static ArgumentType_HomeName Create(final VitalCorePlugin plugin) {
		return new ArgumentType_HomeName(plugin);
	}

	public ArgumentType_HomeName(final VitalCorePlugin plugin) {
		this.plugin = plugin;
	}



	@Override
	public String parse(final StringReader reader) throws CommandSyntaxException {
		final String home = reader.readString();
//TODO: remove this?
//		if (!this.plugin.isValidHome(home))
//			throw CommandSyntaxException.BUILT_IN_EXCEPTIONS
//				.dispatcherParseException().create("Invalid home: "+home);
		return home;
	}



	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(
			final CommandContext<S> context, final SuggestionsBuilder builder) {
		final S source = context.getSource();
		final CommandSender sender = (CommandSender) source;
		if (sender instanceof Player) {
			final Player self = (Player) sender;
			for (final String home : this.plugin.getHomes(self))
				builder.suggest(home);
		}
		return builder.buildFuture();
	}



	@Override
	public ArgumentType<String> getNativeType() {
		return StringArgumentType.string();
	}



}
