package com.poixson.utils;

import static com.poixson.utils.Utils.IsEmpty;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.poixson.exceptions.RequiredArgumentException;
import com.poixson.tools.Keeper;


public final class CommandUtils {
	private CommandUtils() {}
	static { Keeper.add(new CommandUtils()); }



	public static PluginCommand GetCommand(final JavaPlugin plugin, final String namespace,
			final String[] labels, final String desc, final String usage, final String perm) {
		if (IsEmpty(namespace))
			return GetCommand(plugin, labels[0], labels, desc, usage, perm);
		final LinkedList<String> list = new LinkedList<String>();
		for (final String label : labels)
			list.addLast(label);
		final String first = list.removeFirst();
		PluginCommand plugin_command;
		// existing command
		plugin_command = plugin.getCommand(first);
		// register new command
		if (plugin_command == null)
			plugin_command = NewCommand(plugin, namespace, labels);
		if (plugin_command == null)
			return null;
		if (!IsEmpty(list)) plugin_command.setAliases(list);
		if (!IsEmpty(desc)) plugin_command.setDescription(desc);
		if (IsEmpty(plugin_command.getUsage())) {
			if (IsEmpty(usage)) plugin_command.setUsage("Invalid command");
			else                plugin_command.setUsage(usage);
		}
		if (!IsEmpty(perm)) plugin_command.setPermission(perm);
		return plugin_command;
	}



	public static PluginCommand NewCommand(final Plugin plugin,
			final String namespace, final String[] labels) {
		if (IsEmpty(labels   )) throw new RequiredArgumentException("labels");
		if (IsEmpty(labels[0])) throw new RequiredArgumentException("labels");
		if (IsEmpty(namespace)) return NewCommand(plugin, labels[0], labels);
		try {
			final Constructor<PluginCommand> construct =
				PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
			construct.setAccessible(true);
			final PluginCommand command = construct.newInstance(labels[0], plugin);
			// aliases
			final LinkedList<String> aliases = new LinkedList<String>();
			for (int i=1; i<labels.length; i++)
				aliases.addLast(labels[i]);
			command.setAliases(aliases);
			final CommandMap map = GetCommandMap();
			if (map != null) {
				map.register(namespace, command);
				return command;
			}
		} catch (NoSuchMethodException     e) { throw new RuntimeException(e);
		} catch (SecurityException         e) { throw new RuntimeException(e);
		} catch (InstantiationException    e) { throw new RuntimeException(e);
		} catch (IllegalAccessException    e) { throw new RuntimeException(e);
		} catch (IllegalArgumentException  e) { throw new RuntimeException(e);
		} catch (InvocationTargetException e) { throw new RuntimeException(e); }
		return null;
	}



	public static CommandMap GetCommandMap() {
		try {
			final Class<?> clss = Bukkit.getServer().getClass();
			final Method meth = clss.getDeclaredMethod("getCommandMap");
			return (CommandMap) meth.invoke(Bukkit.getServer());
		} catch (NoSuchMethodException     e) { throw new RuntimeException(e);
		} catch (SecurityException         e) { throw new RuntimeException(e);
		} catch (IllegalAccessException    e) { throw new RuntimeException(e);
		} catch (InvocationTargetException e) { throw new RuntimeException(e); }
	}



}
