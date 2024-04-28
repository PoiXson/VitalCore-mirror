package com.poixson.pluginlib.commands;

import java.io.Closeable;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import com.poixson.pluginlib.pxnPluginLib;


public class Commands implements Closeable {

	protected final Command_Help            cmd_help;            // /help
	protected final Command_List            cmd_list;            // /list
	protected final Command_Home            cmd_home;            // /home
	protected final Command_TP              cmd_tp;              // /tp
	protected final Command_Jump            cmd_jump;            // /jump
	protected final Command_MOTD            cmd_motd;            // /motd
	protected final Command_Broadcast       cmd_broadcast;       // /broadcast
	protected final Command_MSG             cmd_msg;             // /msg
	protected final Command_Reply           cmd_reply;           // /reply
	protected final Command_Me              cmd_me;              // /me
	protected final Command_GM              cmd_gm;              // /gm
	protected final Command_Feed            cmd_feed;            // /feed
	protected final Command_Heal            cmd_heal;            // /heal
	protected final Command_Speed           cmd_speed;           // /speed
	protected final Command_Workbench       cmd_workbench;       // /workbench
	protected final Command_Enderchest      cmd_enderchest;      // /enderchest
	protected final Command_PowerTool       cmd_powertool;       // /powertool
	protected final Command_Backup          cmd_backup;          // /backup
	protected final Command_GC              cmd_gc;              // /gc



	public Commands(final pxnPluginLib plugin) {
		final FileConfiguration config = plugin.getConfig();
		final ConfigurationSection cfg = config.getConfigurationSection("Commands");
		if (cfg.getBoolean("help"      )) this.cmd_help       = new Command_Help      (plugin); else this.cmd_help       = null; // /help
		if (cfg.getBoolean("list"      )) this.cmd_list       = new Command_List      (plugin); else this.cmd_list       = null; // /list
		if (cfg.getBoolean("motd"      )) this.cmd_motd       = new Command_MOTD      (plugin); else this.cmd_motd       = null; // /motd
		if (cfg.getBoolean("broadcast" )) this.cmd_broadcast  = new Command_Broadcast (plugin); else this.cmd_broadcast  = null; // /broadcast
		if (cfg.getBoolean("me"        )) this.cmd_me         = new Command_Me        (plugin); else this.cmd_me         = null; // /me
		if (cfg.getBoolean("gm"        )) this.cmd_gm         = new Command_GM        (plugin); else this.cmd_gm         = null; // /gm
		if (cfg.getBoolean("feed"      )) this.cmd_feed       = new Command_Feed      (plugin); else this.cmd_feed       = null; // /feed
		if (cfg.getBoolean("heal"      )) this.cmd_heal       = new Command_Heal      (plugin); else this.cmd_heal       = null; // /heal
		if (cfg.getBoolean("speed"     )) this.cmd_speed      = new Command_Speed     (plugin); else this.cmd_speed      = null; // /speed
		if (cfg.getBoolean("workbench" )) this.cmd_workbench  = new Command_Workbench (plugin); else this.cmd_workbench  = null; // /workbench
		if (cfg.getBoolean("enderchest")) this.cmd_enderchest = new Command_Enderchest(plugin); else this.cmd_enderchest = null; // /enderchest
		if (cfg.getBoolean("powertool" )) this.cmd_powertool  = new Command_PowerTool (plugin); else this.cmd_powertool  = null; // /powertool
		if (cfg.getBoolean("backup"    )) this.cmd_backup     = new Command_Backup    (plugin); else this.cmd_backup     = null; // /backup
		if (cfg.getBoolean("gc"        )) this.cmd_gc         = new Command_GC        (plugin); else this.cmd_gc         = null; // /gc
		if (cfg.getBoolean("home")) {
			this.cmd_home      = new Command_Home     (plugin); // /home
		} else {
			this.cmd_home      = null;
		}
		// /tp
		if (cfg.getBoolean("tp")) {
			this.cmd_tp              = new Command_TP             (plugin); // /tp
		} else {
			this.cmd_tp              = null;
		}
		// /jump
		if (cfg.getBoolean("jump")) {
			this.cmd_jump      = new Command_Jump     (plugin); // /jump
		} else {
			this.cmd_jump      = null;
		}
		// /msg /reply
		if (cfg.getBoolean("msg")) {
			this.cmd_msg   = new Command_MSG  (plugin); // /msg
		} else {
			this.cmd_msg   = null; // /msg
		}
	}



	@Override
	public void close() {
		this.cmd_help           .close();
		this.cmd_list           .close();
		this.cmd_home           .close();
		this.cmd_tp             .close();
		this.cmd_jump           .close();
		this.cmd_motd           .close();
		this.cmd_broadcast      .close();
		this.cmd_msg            .close();
		this.cmd_me             .close();
		this.cmd_gm             .close();
		this.cmd_feed           .close();
		this.cmd_heal           .close();
		this.cmd_speed          .close();
		this.cmd_workbench      .close();
		this.cmd_enderchest     .close();
		this.cmd_powertool      .close();
		this.cmd_backup         .close();
		this.cmd_gc             .close();
	}



	public static void ConfigDefaults(final FileConfiguration config) {
		if (!config.contains("Commands"))
			config.set("Commands", config.createSection("Commands"));
//		config.addDefault("Commands.help",       Boolean.FALSE);
//		config.addDefault("Commands.list",       Boolean.FALSE);
//		config.addDefault("Commands.home",       Boolean.FALSE);
//		config.addDefault("Commands.tp",         Boolean.FALSE);
//		config.addDefault("Commands.jump",       Boolean.FALSE);
//		config.addDefault("Commands.motd",       Boolean.FALSE);
//		config.addDefault("Commands.broadcast",  Boolean.FALSE);
//		config.addDefault("Commands.msg",        Boolean.FALSE);
//		config.addDefault("Commands.me",         Boolean.FALSE);
//		config.addDefault("Commands.gm",         Boolean.FALSE);
//		config.addDefault("Commands.feed",       Boolean.FALSE);
//		config.addDefault("Commands.heal",       Boolean.FALSE);
//		config.addDefault("Commands.speed",      Boolean.FALSE);
//		config.addDefault("Commands.workbench",  Boolean.FALSE);
//		config.addDefault("Commands.enderchest", Boolean.FALSE);
//		config.addDefault("Commands.powertool",  Boolean.FALSE);
//		config.addDefault("Commands.backup",     Boolean.FALSE);
		config.addDefault("Commands.gc",         Boolean.FALSE);
	}



}
