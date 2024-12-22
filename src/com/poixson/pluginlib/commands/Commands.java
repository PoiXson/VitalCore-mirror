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
//	protected final Command_Warp            cmd_warp;            // /warp
//	protected final Command_SetWarp         cmd_setwarp;         // /setwarp
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
	protected final Command_GM              cmd_gm;              // /gm
	protected final Command_GMC             cmd_gm_c;            // /gmc
	protected final Command_GMS             cmd_gm_s;            // /gms
	protected final Command_GMA             cmd_gm_a;            // /gma
	protected final Command_GMSpec          cmd_gm_sp;           // /gmsp
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
//		this.cmd_help            = (cfg.getBoolean("help"      ) ? new Command_Help           (plugin) : null); // /help
//		this.cmd_list            = (cfg.getBoolean("list"      ) ? new Command_List           (plugin) : null); // /list
//		this.cmd_home            = (cfg.getBoolean("home"      ) ? new Command_Home           (plugin) : null); // /home
//		this.cmd_home_list       = (cfg.getBoolean("home"      ) ? new Command_Home_List      (plugin) : null); // /list-homes
//		this.cmd_home_set        = (cfg.getBoolean("home"      ) ? new Command_Home_Set       (plugin) : null); // /set-home
//		this.cmd_home_del        = (cfg.getBoolean("home"      ) ? new Command_Home_Del       (plugin) : null); // /del-home
		this.cmd_world           = (cfg.getBoolean("world"     ) ? new Command_World          (plugin) : null); // /world
		this.cmd_spawn           = (cfg.getBoolean("spawn"     ) ? new Command_Spawn          (plugin) : null); // /spawn
		this.cmd_setspawn        = (cfg.getBoolean("spawn"     ) ? new Command_SetSpawn       (plugin) : null); // /setspawn
//		this.cmd_warp            = (cfg.getBoolean("warp"      ) ? new Command_Warp           (plugin) : null); // /warp
//		this.cmd_setwarp         = (cfg.getBoolean("warp"      ) ? new Command_SetWarp        (plugin) : null); // /setwarp
//		this.cmd_tp              = (cfg.getBoolean("tp"        ) ? new Command_TP             (plugin) : null); // /tp
//		this.cmd_tp_here         = (cfg.getBoolean("tp"        ) ? new Command_TP_Here        (plugin) : null); // /tp-here
//		this.cmd_tp_ask          = (cfg.getBoolean("tp"        ) ? new Command_TP_Ask         (plugin) : null); // /tp-ask
//		this.cmd_tp_ask_here     = (cfg.getBoolean("tp"        ) ? new Command_TP_Ask_Here    (plugin) : null); // /tp-ask-here
//		this.cmd_tp_all_here     = (cfg.getBoolean("tp"        ) ? new Command_TP_All_Here    (plugin) : null); // /tp-all-here
//		this.cmd_tp_ask_all_here = (cfg.getBoolean("tp"        ) ? new Command_TP_Ask_All_Here(plugin) : null); // /tp-ask-all-here
//		this.cmd_tp_offline      = (cfg.getBoolean("tp"        ) ? new Command_TP_Offline     (plugin) : null); // /tp-offline
//		this.cmd_back            = (cfg.getBoolean("back"      ) ? new Command_Back           (plugin) : null); // /back
//		this.cmd_top             = (cfg.getBoolean("top"       ) ? new Command_Top            (plugin) : null); // /top
//		this.cmd_bottom          = (cfg.getBoolean("bottom"    ) ? new Command_Bottom         (plugin) : null); // /bottom
//		this.cmd_jump            = (cfg.getBoolean("jump"      ) ? new Command_Jump           (plugin) : null); // /jump
//		this.cmd_jump_into       = (cfg.getBoolean("jump"      ) ? new Command_Jump_Into      (plugin) : null); // /jump-into
//		this.cmd_jump_down       = (cfg.getBoolean("jump"      ) ? new Command_Jump_Down      (plugin) : null); // /jump-down
		this.cmd_fly             = (cfg.getBoolean("fly"       ) ? new Command_Fly            (plugin) : null); // /fly
//		this.cmd_motd            = (cfg.getBoolean("motd"      ) ? new Command_MOTD           (plugin) : null); // /motd
//		this.cmd_broadcast       = (cfg.getBoolean("broadcast" ) ? new Command_Broadcast      (plugin) : null); // /broadcast
//		this.cmd_msg             = (cfg.getBoolean("msg"       ) ? new Command_MSG            (plugin) : null); // /msg
//		this.cmd_reply           = (cfg.getBoolean("msg"       ) ? new Command_Reply          (plugin) : null); // /reply
//		this.cmd_me              = (cfg.getBoolean("me"        ) ? new Command_Me             (plugin) : null); // /me
		this.cmd_gm              = (cfg.getBoolean("gm"        ) ? new Command_GM             (plugin) : null); // /gm
		this.cmd_gm_c            = (cfg.getBoolean("gm"        ) ? new Command_GMC            (plugin) : null); // /gmc
		this.cmd_gm_s            = (cfg.getBoolean("gm"        ) ? new Command_GMS            (plugin) : null); // /gms
		this.cmd_gm_a            = (cfg.getBoolean("gm"        ) ? new Command_GMA            (plugin) : null); // /gma
		this.cmd_gm_sp           = (cfg.getBoolean("gm"        ) ? new Command_GMSpec         (plugin) : null); // /gmsp
		this.cmd_feed            = (cfg.getBoolean("feed"      ) ? new Command_Feed           (plugin) : null); // /feed
		this.cmd_heal            = (cfg.getBoolean("heal"      ) ? new Command_Heal           (plugin) : null); // /heal
		this.cmd_rest            = (cfg.getBoolean("rest"      ) ? new Command_Rest           (plugin) : null); // /rest
//		this.cmd_speed           = (cfg.getBoolean("speed"     ) ? new Command_Speed          (plugin) : null); // /speed
//		this.cmd_speed_walk      = (cfg.getBoolean("speed"     ) ? new Command_Speed_Walk     (plugin) : null); // /walkspeed
//		this.cmd_speed_fly       = (cfg.getBoolean("speed"     ) ? new Command_Speed_Fly      (plugin) : null); // /flyspeed
		this.cmd_workbench       = (cfg.getBoolean("workbench" ) ? new Command_Workbench      (plugin) : null); // /workbench
		this.cmd_enderchest      = (cfg.getBoolean("enderchest") ? new Command_Enderchest     (plugin) : null); // /enderchest
//		this.cmd_powertool       = (cfg.getBoolean("powertool" ) ? new Command_PowerTool      (plugin) : null); // /powertool
//		this.cmd_backup          = (cfg.getBoolean("backup"    ) ? new Command_Backup         (plugin) : null); // /backup
		this.cmd_gc              = (cfg.getBoolean("gc"        ) ? new Command_GC             (plugin) : null); // /gc
		this.cmd_uptime          = (cfg.getBoolean("uptime"    ) ? new Command_Uptime         (plugin) : null); // /uptime
	}



	@Override
	public void close() {
//		if (this.cmd_help            != null) this.cmd_help           .close();
//		if (this.cmd_list            != null) this.cmd_list           .close();
//		if (this.cmd_home            != null) this.cmd_home           .close();
//		if (this.cmd_home_list       != null) this.cmd_home_list      .close();
//		if (this.cmd_home_set        != null) this.cmd_home_set       .close();
//		if (this.cmd_home_del        != null) this.cmd_home_del       .close();
		if (this.cmd_world           != null) this.cmd_world          .close();
		if (this.cmd_spawn           != null) this.cmd_spawn          .close();
		if (this.cmd_setspawn        != null) this.cmd_setspawn       .close();
//		if (this.cmd_warp            != null) this.cmd_warp           .close();
//		if (this.cmd_setwarp         != null) this.cmd_setwarp        .close();
//		if (this.cmd_tp              != null) this.cmd_tp             .close();
//		if (this.cmd_tp_here         != null) this.cmd_tp_here        .close();
//		if (this.cmd_tp_ask          != null) this.cmd_tp_ask         .close();
//		if (this.cmd_tp_ask_here     != null) this.cmd_tp_ask_here    .close();
//		if (this.cmd_tp_all_here     != null) this.cmd_tp_all_here    .close();
//		if (this.cmd_tp_ask_all_here != null) this.cmd_tp_ask_all_here.close();
//		if (this.cmd_tp_offline      != null) this.cmd_tp_offline     .close();
//		if (this.cmd_back            != null) this.cmd_back           .close();
//		if (this.cmd_top             != null) this.cmd_top            .close();
//		if (this.cmd_bottom          != null) this.cmd_bottom         .close();
//		if (this.cmd_jump            != null) this.cmd_jump           .close();
//		if (this.cmd_jump_into       != null) this.cmd_jump_into      .close();
//		if (this.cmd_jump_down       != null) this.cmd_jump_down      .close();
		if (this.cmd_fly             != null) this.cmd_fly            .close();
//		if (this.cmd_motd            != null) this.cmd_motd           .close();
//		if (this.cmd_broadcast       != null) this.cmd_broadcast      .close();
//		if (this.cmd_msg             != null) this.cmd_msg            .close();
//		if (this.cmd_reply           != null) this.cmd_reply          .close();
//		if (this.cmd_me              != null) this.cmd_me             .close();
		if (this.cmd_gm              != null) this.cmd_gm             .close();
		if (this.cmd_gm_c            != null) this.cmd_gm_c           .close();
		if (this.cmd_gm_s            != null) this.cmd_gm_s           .close();
		if (this.cmd_gm_a            != null) this.cmd_gm_a           .close();
		if (this.cmd_gm_sp           != null) this.cmd_gm_sp          .close();
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
//		config.addDefault("Commands.home",       Boolean.FALSE);
		config.addDefault("Commands.world",      Boolean.FALSE);
		config.addDefault("Commands.spawn",      Boolean.FALSE);
//		config.addDefault("Commands.warp",       Boolean.FALSE);
//		config.addDefault("Commands.tp",         Boolean.FALSE);
//		config.addDefault("Commands.back",       Boolean.FALSE);
//		config.addDefault("Commands.top",        Boolean.FALSE);
//		config.addDefault("Commands.bottom",     Boolean.FALSE);
//		config.addDefault("Commands.jump",       Boolean.FALSE);
		config.addDefault("Commands.fly",        Boolean.FALSE);
//		config.addDefault("Commands.motd",       Boolean.FALSE);
//		config.addDefault("Commands.broadcast",  Boolean.FALSE);
//		config.addDefault("Commands.msg",        Boolean.FALSE);
//		config.addDefault("Commands.me",         Boolean.FALSE);
		config.addDefault("Commands.gm",         Boolean.FALSE);
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
