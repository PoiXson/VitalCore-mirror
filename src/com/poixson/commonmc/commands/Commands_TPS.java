package com.poixson.commonmc.commands;

import static com.poixson.commonmc.tools.tps.TicksPerSecond.DisplayTPS;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.poixson.commonmc.pxnCommonPlugin;
import com.poixson.commonmc.tools.commands.pxnCommand;
import com.poixson.commonmc.tools.commands.pxnCommandsHandler;
import com.poixson.commonmc.tools.tps.TicksAnnouncer;


public class Commands_TPS extends pxnCommandsHandler {



	public Commands_TPS(final pxnCommonPlugin plugin) {
		super(plugin, "tps", "lag");
		this.addCommand(new Command_TPS());
	}



	public class Command_TPS extends pxnCommand {

		public Command_TPS() {
			super(true, "toggle");
		}

		@Override
		public boolean run(final CommandSender sender, final String label, final String[] args) {
			final Player player = (sender instanceof Player ? (Player)sender : null);
			final int num_args = args.length;
			if (num_args == 0) {
				DisplayTPS(player);
				return true;
			} else {
				if ("toggle".equals(args[0])) {
					final TicksAnnouncer announcer = pxnCommonPlugin.GetTicksAnnouncer();
					announcer.toggle(player);
					return true;
				}
			}
			return false;
		}

		@Override
		public boolean isDefault() {
			return true;
		}

	}



}
