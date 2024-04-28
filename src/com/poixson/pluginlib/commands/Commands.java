package com.poixson.pluginlib.commands;

import java.io.Closeable;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import com.poixson.pluginlib.pxnPluginLib;


public class Commands implements Closeable {

//	protected final Command_Help            cmd_help;            // /help
//	protected final Command_List            cmd_list;            // /list
//	protected final Command_Home            cmd_home;            // /home
//	protected final Command_Home_List       cmd_home_list;       // /list-homes
//	protected final Command_Home_Set        cmd_home_set;        // /set-home
//	protected final Command_Home_Del        cmd_home_del;        // /del-home
//	protected final Command_TP              cmd_tp;              // /tp
//	protected final Command_TP_Here         cmd_tp_here;         // /tp-here
//	protected final Command_TP_Ask          cmd_tp_ask;          // /tp-ask
//	protected final Command_TP_Ask_Here     cmd_tp_ask_here;     // /tp-ask-here
//	protected final Command_TP_All_Here     cmd_tp_all_here;     // /tp-all-here
//	protected final Command_TP_Ask_All_Here cmd_tp_ask_all_here; // /tp-ask-all-here
//	protected final Command_TP_Offline      cmd_tp_offline;      // /tp-offline
//	protected final Command_Jump            cmd_jump;            // /jump
//	protected final Command_Jump_Into       cmd_jump_into;       // /jump-into
//	protected final Command_Jump_Down       cmd_jump_down;       // /jump-down
//	protected final Command_MOTD            cmd_motd;            // /motd
//	protected final Command_Broadcast       cmd_broadcast;       // /broadcast
//	protected final Command_MSG             cmd_msg;             // /msg
//	protected final Command_Reply           cmd_reply;           // /reply
//	protected final Command_Me              cmd_me;              // /me
//	protected final Command_GM              cmd_gm;              // /gm
//	protected final Command_Feed            cmd_feed;            // /feed
//	protected final Command_Heal            cmd_heal;            // /heal
//	protected final Command_Speed           cmd_speed;           // /speed
//	protected final Command_Workbench       cmd_workbench;       // /workbench
//	protected final Command_Enderchest      cmd_enderchest;      // /enderchest
//	protected final Command_PowerTool       cmd_powertool;       // /powertool
//	protected final Command_Backup          cmd_backup;          // /backup
	protected final Command_GC              cmd_gc;              // /gc



	public Commands(final pxnPluginLib plugin) {
		final FileConfiguration config = plugin.getConfig();
		final ConfigurationSection cfg = config.getConfigurationSection("Commands");
//		if (cfg.getBoolean("help"      )) this.cmd_help       = new Command_Help      (plugin); else this.cmd_help       = null; // /help
//		if (cfg.getBoolean("list"      )) this.cmd_list       = new Command_List      (plugin); else this.cmd_list       = null; // /list
//		if (cfg.getBoolean("motd"      )) this.cmd_motd       = new Command_MOTD      (plugin); else this.cmd_motd       = null; // /motd
//		if (cfg.getBoolean("broadcast" )) this.cmd_broadcast  = new Command_Broadcast (plugin); else this.cmd_broadcast  = null; // /broadcast
//		if (cfg.getBoolean("me"        )) this.cmd_me         = new Command_Me        (plugin); else this.cmd_me         = null; // /me
//		if (cfg.getBoolean("gm"        )) this.cmd_gm         = new Command_GM        (plugin); else this.cmd_gm         = null; // /gm
//		if (cfg.getBoolean("feed"      )) this.cmd_feed       = new Command_Feed      (plugin); else this.cmd_feed       = null; // /feed
//		if (cfg.getBoolean("heal"      )) this.cmd_heal       = new Command_Heal      (plugin); else this.cmd_heal       = null; // /heal
//		if (cfg.getBoolean("speed"     )) this.cmd_speed      = new Command_Speed     (plugin); else this.cmd_speed      = null; // /speed
//		if (cfg.getBoolean("workbench" )) this.cmd_workbench  = new Command_Workbench (plugin); else this.cmd_workbench  = null; // /workbench
//		if (cfg.getBoolean("enderchest")) this.cmd_enderchest = new Command_Enderchest(plugin); else this.cmd_enderchest = null; // /enderchest
//		if (cfg.getBoolean("powertool" )) this.cmd_powertool  = new Command_PowerTool (plugin); else this.cmd_powertool  = null; // /powertool
//		if (cfg.getBoolean("backup"    )) this.cmd_backup     = new Command_Backup    (plugin); else this.cmd_backup     = null; // /backup
		if (cfg.getBoolean("gc"        )) this.cmd_gc         = new Command_GC        (plugin); else this.cmd_gc         = null; // /gc
//		if (cfg.getBoolean("home")) {
//			this.cmd_home      = new Command_Home     (plugin); // /home
//			this.cmd_home_list = new Command_Home_List(plugin); // /list-homes
//			this.cmd_home_set  = new Command_Home_Set (plugin); // /set-home
//			this.cmd_home_del  = new Command_Home_Del (plugin); // /del-home
//		} else {
//			this.cmd_home      = null;
//			this.cmd_home_list = null;
//			this.cmd_home_set  = null;
//			this.cmd_home_del  = null;
//		}
//		// /tp
//		if (cfg.getBoolean("tp")) {
//			this.cmd_tp              = new Command_TP             (plugin); // /tp
//			this.cmd_tp_here         = new Command_TP_Here        (plugin); // /tp-here
//			this.cmd_tp_ask          = new Command_TP_Ask         (plugin); // /tp-ask
//			this.cmd_tp_ask_here     = new Command_TP_Ask_Here    (plugin); // /tp-ask-here
//			this.cmd_tp_all_here     = new Command_TP_All_Here    (plugin); // /tp-all-here
//			this.cmd_tp_ask_all_here = new Command_TP_Ask_All_Here(plugin); // /tp-ask-all-here
//			this.cmd_tp_offline      = new Command_TP_Offline     (plugin); // /tp-offline
//		} else {
//			this.cmd_tp              = null;
//			this.cmd_tp_here         = null;
//			this.cmd_tp_ask          = null;
//			this.cmd_tp_ask_here     = null;
//			this.cmd_tp_all_here     = null;
//			this.cmd_tp_ask_all_here = null;
//			this.cmd_tp_offline      = null;
//		}
//		// /jump
//		if (cfg.getBoolean("jump")) {
//			this.cmd_jump      = new Command_Jump     (plugin); // /jump
//			this.cmd_jump_into = new Command_Jump_Into(plugin); // /jump-into
//			this.cmd_jump_down = new Command_Jump_Down(plugin); // /jump-down
//		} else {
//			this.cmd_jump      = null;
//			this.cmd_jump_into = null;
//			this.cmd_jump_down = null;
//		}
//		// /msg /reply
//		if (cfg.getBoolean("msg")) {
//			this.cmd_msg   = new Command_MSG  (plugin); // /msg
//			this.cmd_reply = new Command_Reply(plugin); // reply
//		} else {
//			this.cmd_msg   = null; // /msg
//			this.cmd_reply = null; // /reply
//		}
	}



	@Override
	public void close() {
//		if (this.cmd_help            != null) this.cmd_help           .close();
//		if (this.cmd_list            != null) this.cmd_list           .close();
//		if (this.cmd_home            != null) this.cmd_home           .close();
//		if (this.cmd_home_list       != null) this.cmd_home_list      .close();
//		if (this.cmd_home_set        != null) this.cmd_home_set       .close();
//		if (this.cmd_home_del        != null) this.cmd_home_del       .close();
//		if (this.cmd_tp              != null) this.cmd_tp             .close();
//		if (this.cmd_tp_here         != null) this.cmd_tp_here        .close();
//		if (this.cmd_tp_ask          != null) this.cmd_tp_ask         .close();
//		if (this.cmd_tp_ask_here     != null) this.cmd_tp_ask_here    .close();
//		if (this.cmd_tp_all_here     != null) this.cmd_tp_all_here    .close();
//		if (this.cmd_tp_ask_all_here != null) this.cmd_tp_ask_all_here.close();
//		if (this.cmd_tp_offline      != null) this.cmd_tp_offline     .close();
//		if (this.cmd_jump            != null) this.cmd_jump           .close();
//		if (this.cmd_jump_into       != null) this.cmd_jump_into      .close();
//		if (this.cmd_jump_down       != null) this.cmd_jump_down      .close();
//		if (this.cmd_motd            != null) this.cmd_motd           .close();
//		if (this.cmd_broadcast       != null) this.cmd_broadcast      .close();
//		if (this.cmd_msg             != null) this.cmd_msg            .close();
//		if (this.cmd_reply           != null) this.cmd_reply          .close();
//		if (this.cmd_me              != null) this.cmd_me             .close();
//		if (this.cmd_gm              != null) this.cmd_gm             .close();
//		if (this.cmd_feed            != null) this.cmd_feed           .close();
//		if (this.cmd_heal            != null) this.cmd_heal           .close();
//		if (this.cmd_speed           != null) this.cmd_speed          .close();
//		if (this.cmd_workbench       != null) this.cmd_workbench      .close();
//		if (this.cmd_enderchest      != null) this.cmd_enderchest     .close();
//		if (this.cmd_powertool       != null) this.cmd_powertool      .close();
//		if (this.cmd_backup          != null) this.cmd_backup         .close();
		if (this.cmd_gc              != null) this.cmd_gc             .close();
	}



	public static void ConfigDefaults(final FileConfiguration config) {
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
