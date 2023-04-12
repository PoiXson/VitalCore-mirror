package com.poixson.commonmc.commands;

import org.bukkit.command.CommandSender;

import com.poixson.commonmc.pxnCommonPlugin;
import com.poixson.commonmc.tools.commands.pxnCommand;
import com.poixson.commonmc.tools.commands.pxnCommandsHandler;


public class Commands_Memory extends pxnCommandsHandler<pxnCommonPlugin> {



	public Commands_Memory(final pxnCommonPlugin plugin) {
		super(plugin,
			"mem",
			"memory"
		);
		this.addCommand(new Command_Memory(plugin));
	}



	public class Command_Memory extends pxnCommand<pxnCommonPlugin> {

		public Command_Memory(final pxnCommonPlugin plugin) {
			super(plugin, true);
		}

		@Override
		public boolean run(final CommandSender sender, final String label, final String[] args) {
//TODO
sender.sendMessage("MEMORY COMMAND UNFINISHED");
System.out.println("MEMORY COMMAND UNFINISHED");
for (final String arg : args)
System.out.println("ARG: "+arg);
			return true;
		}

	}



}
