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
	protected final Command_World           cmd_world;           // /world
	protected final Command_Spawn           cmd_spawn;           // /spawn
	protected final Command_SetSpawn        cmd_setspawn;        // /setspawn
//	protected final Command_TP              cmd_tp;              // /tp
//	protected final Command_TP_Here         cmd_tp_here;         // /tp-here
//	protected final Command_TP_Ask          cmd_tp_ask;          // /tp-ask
//	protected final Command_TP_Ask_Here     cmd_tp_ask_here;     // /tp-ask-here
//	protected final Command_TP_All_Here     cmd_tp_all_here;     // /tp-all-here
//	protected final Command_TP_Ask_All_Here cmd_tp_ask_all_here; // /tp-ask-all-here
//	protected final Command_TP_Offline      cmd_tp_offline;      // /tp-offline
//	protected final Command_Back            cmd_back;            // /back
//	protected final Command_Top             cmd_top;             // /top
//	protected final Command_Bottom          cmd_bottom;          // /bottom
//	protected final Command_Jump            cmd_jump;            // /jump
//	protected final Command_Jump_Into       cmd_jump_into;       // /jump-into
//	protected final Command_Jump_Down       cmd_jump_down;       // /jump-down
	protected final Command_Fly             cmd_fly;             // /fly
//	protected final Command_MOTD            cmd_motd;            // /motd
//	protected final Command_Broadcast       cmd_broadcast;       // /broadcast
//	protected final Command_MSG             cmd_msg;             // /msg
//	protected final Command_Reply           cmd_reply;           // /reply
//	protected final Command_Me              cmd_me;              // /me
//	protected final Command_GM              cmd_gm;              // /gm
//	protected final Command_GMC             cmd_gm_c;            // /gmc
//	protected final Command_GMS             cmd_gm_s;            // /gms
//	protected final Command_GMA             cmd_gm_a;            // /gma
//	protected final Command_GMSpec          cmd_gm_spec;         // /gm-spec
	protected final Command_Feed            cmd_feed;            // /feed
	protected final Command_Heal            cmd_heal;            // /heal
	protected final Command_Rest            cmd_rest;            // /rest
//	protected final Command_Speed           cmd_speed;           // /speed
//	protected final Command_Speed_Walk      cmd_speed_walk;      // /walkspeed
//	protected final Command_Speed_Fly       cmd_speed_fly        // /flyspeed
	protected final Command_Workbench       cmd_workbench;       // /workbench
	protected final Command_Enderchest      cmd_enderchest;      // /enderchest
//	protected final Command_PowerTool       cmd_powertool;       // /powertool
//	protected final Command_Backup          cmd_backup;          // /backup
	protected final Command_GC              cmd_gc;              // /gc
	protected final Command_Uptime          cmd_uptime;          // /uptime



	public Commands(final pxnPluginLib plugin) {
		final FileConfiguration config = plugin.getConfig();
		final ConfigurationSection cfg = config.getConfigurationSection("Commands");
//		if (cfg.getBoolean("help"      )) this.cmd_help       = new Command_Help      (plugin); else this.cmd_help       = null; // /help
//		if (cfg.getBoolean("list"      )) this.cmd_list       = new Command_List      (plugin); else this.cmd_list       = null; // /list
		if (cfg.getBoolean("world"     )) this.cmd_world      = new Command_World     (plugin); else this.cmd_world      = null; // /world
		if (cfg.getBoolean("spawn"     )) this.cmd_spawn      = new Command_Spawn     (plugin); else this.cmd_spawn      = null; // /spawn
		if (cfg.getBoolean("setspawn"  )) this.cmd_setspawn   = new Command_SetSpawn  (plugin); else this.cmd_setspawn   = null; // /setspawn
//		if (cfg.getBoolean("back"      )) this.cmd_back       = new Command_Back      (plugin); else this.cmd_back       = null; // /back
//		if (cfg.getBoolean("top"       )) this.cmd_top        = new Command_Top       (plugin); else this.cmd_top        = null; // /top
//		if (cfg.getBoolean("bottom"    )) this.cmd_bottom     = new Command_Bottom    (plugin); else this.cmd_bottom     = null; // /bottom
		if (cfg.getBoolean("fly"       )) this.cmd_fly        = new Command_Fly       (plugin); else this.cmd_fly        = null; // /fly
//		if (cfg.getBoolean("motd"      )) this.cmd_motd       = new Command_MOTD      (plugin); else this.cmd_motd       = null; // /motd
//		if (cfg.getBoolean("broadcast" )) this.cmd_broadcast  = new Command_Broadcast (plugin); else this.cmd_broadcast  = null; // /broadcast
//		if (cfg.getBoolean("me"        )) this.cmd_me         = new Command_Me        (plugin); else this.cmd_me         = null; // /me
		if (cfg.getBoolean("feed"      )) this.cmd_feed       = new Command_Feed      (plugin); else this.cmd_feed       = null; // /feed
		if (cfg.getBoolean("heal"      )) this.cmd_heal       = new Command_Heal      (plugin); else this.cmd_heal       = null; // /heal
		if (cfg.getBoolean("rest"      )) this.cmd_rest       = new Command_Rest      (plugin); else this.cmd_rest       = null; // /rest
		if (cfg.getBoolean("workbench" )) this.cmd_workbench  = new Command_Workbench (plugin); else this.cmd_workbench  = null; // /workbench
		if (cfg.getBoolean("enderchest")) this.cmd_enderchest = new Command_Enderchest(plugin); else this.cmd_enderchest = null; // /enderchest
//		if (cfg.getBoolean("powertool" )) this.cmd_powertool  = new Command_PowerTool (plugin); else this.cmd_powertool  = null; // /powertool
//		if (cfg.getBoolean("backup"    )) this.cmd_backup     = new Command_Backup    (plugin); else this.cmd_backup     = null; // /backup
		if (cfg.getBoolean("gc"        )) this.cmd_gc         = new Command_GC        (plugin); else this.cmd_gc         = null; // /gc
		if (cfg.getBoolean("uptime"    )) this.cmd_uptime     = new Command_Uptime    (plugin); else this.cmd_uptime    = null; // /uptime
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
//		// /gm
//		if (cfg.getBoolean("gm")) {
//			this.cmd_gm      = new Command_GM    (plugin); // /gm
//			this.cmd_gm_c    = new Command_GMC   (plugin); // /gmc
//			this.cmd_gm_s    = new Command_GMS   (plugin); // /gms
//			this.cmd_gm_a    = new Command_GMA   (plugin); // /gma
//			this.cmd_gm_spec = new Command_GMSpec(plugin); // /gm-spec
//		} else {
//			this.cmd_gm      = null; // /gm
//			this.cmd_gm_c    = null; // /gmc
//			this.cmd_gm_s    = null; // /gms
//			this.cmd_gm_a    = null; // /gma
//			this.cmd_gm_spec = null; // /gm-spec
//		}
//		if (cfg.getBoolean("speed")) {
//			this.cmd_speed      = new Command_Speed     (plugin); // /speed
//			this.cmd_speed_walk = new Command_Speed_Walk(plugin); // /walkspeed
//			this.cmd_speed_fly  = new Command_Speed_Fly (plugin); // /flyspeed
//		} else {
//			this.cmd_speed      = null; // /speed
//			this.cmd_speed_walk = null; // /walkspeed
//			this.cmd_speed_fly  = null; // /flyspeed
//		}
	}



	@Override
	public void close() {
//		if (this.cmd_help            != null) this.cmd_help           .close();
//		if (this.cmd_list            != null) this.cmd_list           .close();
//		if (this.cmd_back            != null) this.cmd_back           .close();
//		if (this.cmd_top             != null) this.cmd_top            .close();
//		if (this.cmd_bottom          != null) this.cmd_bottom         .close();
//		if (this.cmd_home            != null) this.cmd_home           .close();
//		if (this.cmd_home_list       != null) this.cmd_home_list      .close();
//		if (this.cmd_home_set        != null) this.cmd_home_set       .close();
//		if (this.cmd_home_del        != null) this.cmd_home_del       .close();
		if (this.cmd_world           != null) this.cmd_world          .close();
		if (this.cmd_spawn           != null) this.cmd_spawn          .close();
		if (this.cmd_setspawn        != null) this.cmd_setspawn       .close();
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
		if (this.cmd_fly             != null) this.cmd_fly            .close();
//		if (this.cmd_motd            != null) this.cmd_motd           .close();
//		if (this.cmd_broadcast       != null) this.cmd_broadcast      .close();
//		if (this.cmd_msg             != null) this.cmd_msg            .close();
//		if (this.cmd_reply           != null) this.cmd_reply          .close();
//		if (this.cmd_me              != null) this.cmd_me             .close();
//		if (this.cmd_gm              != null) this.cmd_gm             .close();
//		if (this.cmd_gm_c            != null) this.cmd_gm_c           .close();
//		if (this.cmd_gm_s            != null) this.cmd_gm_s           .close();
//		if (this.cmd_gm_a            != null) this.cmd_gm_a           .close();
//		if (this.cmd_gm_spec         != null) this.cmd_gm_spec        .close();
		if (this.cmd_feed            != null) this.cmd_feed           .close();
		if (this.cmd_heal            != null) this.cmd_heal           .close();
		if (this.cmd_rest            != null) this.cmd_rest           .close();
//		if (this.cmd_speed           != null) this.cmd_speed          .close();
//		if (this.cmd_speed_walk      != null) this.cmd_speed_walk     .close();
//		if (this.cmd_speed_fly       != null) this.cmd_speed_fly      .close();
		if (this.cmd_workbench       != null) this.cmd_workbench      .close();
		if (this.cmd_enderchest      != null) this.cmd_enderchest     .close();
//		if (this.cmd_powertool       != null) this.cmd_powertool      .close();
//		if (this.cmd_backup          != null) this.cmd_backup         .close();
		if (this.cmd_gc              != null) this.cmd_gc             .close();
		if (this.cmd_uptime          != null) this.cmd_uptime         .close();
	}



	public static void ConfigDefaults(final FileConfiguration config) {
//		config.addDefault("Commands.help",       Boolean.FALSE);
//		config.addDefault("Commands.list",       Boolean.FALSE);
//		config.addDefault("Commands.back",       Boolean.FALSE);
//		config.addDefault("Commands.top",        Boolean.FALSE);
//		config.addDefault("Commands.bottom",     Boolean.FALSE);
//		config.addDefault("Commands.home",       Boolean.FALSE);
		config.addDefault("Commands.world",      Boolean.FALSE);
		config.addDefault("Commands.spawn",      Boolean.FALSE);
		config.addDefault("Commands.setspawn",   Boolean.FALSE);
//		config.addDefault("Commands.tp",         Boolean.FALSE);
//		config.addDefault("Commands.jump",       Boolean.FALSE);
		config.addDefault("Commands.fly",        Boolean.FALSE);
//		config.addDefault("Commands.motd",       Boolean.FALSE);
//		config.addDefault("Commands.broadcast",  Boolean.FALSE);
//		config.addDefault("Commands.msg",        Boolean.FALSE);
//		config.addDefault("Commands.me",         Boolean.FALSE);
//		config.addDefault("Commands.gm",         Boolean.FALSE);
		config.addDefault("Commands.feed",       Boolean.FALSE);
		config.addDefault("Commands.heal",       Boolean.FALSE);
		config.addDefault("Commands.rest",       Boolean.FALSE);
//		config.addDefault("Commands.speed",      Boolean.FALSE);
		config.addDefault("Commands.workbench",  Boolean.FALSE);
		config.addDefault("Commands.enderchest", Boolean.FALSE);
//		config.addDefault("Commands.powertool",  Boolean.FALSE);
//		config.addDefault("Commands.backup",     Boolean.FALSE);
		config.addDefault("Commands.gc",         Boolean.FALSE);
		config.addDefault("Commands.uptime",     Boolean.FALSE);
	}



}
