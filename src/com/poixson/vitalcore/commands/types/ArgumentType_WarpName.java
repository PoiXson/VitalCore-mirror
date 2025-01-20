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


public class ArgumentType_WarpName implements CustomArgumentType<String, String> {

	protected final VitalCorePlugin plugin;



	public static ArgumentType_WarpName Create(final VitalCorePlugin plugin) {
		return new ArgumentType_WarpName(plugin);
	}

	public ArgumentType_WarpName(final VitalCorePlugin plugin) {
		this.plugin = plugin;
	}



	@Override
	public String parse(final StringReader reader) throws CommandSyntaxException {
		final String warp = reader.readString();
//TODO: remove this?
//		if (!this.plugin.isValidWarp(warp))
//			throw CommandSyntaxException.BUILT_IN_EXCEPTIONS
//				.dispatcherParseException().create("Invalid warp: "+warp);
		return warp;
	}



	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(
			final CommandContext<S> context, final SuggestionsBuilder builder) {
		final S source = context.getSource();
		final CommandSender sender = (CommandSender) source;
		if (sender instanceof Player) {
			final Player self = (Player) sender;
			for (final String warp : this.plugin.getWarps(self))
				builder.suggest(warp);
		}
		return builder.buildFuture();
	}



	@Override
	public ArgumentType<String> getNativeType() {
		return StringArgumentType.string();
	}



}
