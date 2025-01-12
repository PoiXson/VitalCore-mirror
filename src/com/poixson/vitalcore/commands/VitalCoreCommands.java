package com.poixson.vitalcore.commands;

import java.io.Closeable;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import com.poixson.vitalcore.VitalCorePlugin;


public class PluginCommands implements Closeable {

//	protected final CMD_Help            cmd_help;            // /help
//	protected final CMD_List            cmd_list;            // /list
//	protected final CMD_Home            cmd_home;            // /home
//	protected final CMD_Home_List       cmd_home_list;       // /list-homes
//	protected final CMD_Home_Set        cmd_home_set;        // /set-home
//	protected final CMD_Home_Del        cmd_home_del;        // /del-home
	protected final CMD_World           cmd_world;           // /world
	protected final CMD_Spawn           cmd_spawn;           // /spawn
	protected final CMD_Spawn_Set       cmd_setspawn;        // /setspawn
//	protected final CMD_Warp            cmd_warp;            // /warp
//	protected final CMD_Warp_Set        cmd_setwarp;         // /setwarp
//	protected final CMD_TP              cmd_tp;              // /tp
//	protected final CMD_TP_Here         cmd_tp_here;         // /tp-here
//	protected final CMD_TP_Ask          cmd_tp_ask;          // /tp-ask
//	protected final CMD_TP_Ask_Here     cmd_tp_ask_here;     // /tp-ask-here
//	protected final CMD_TP_All_Here     cmd_tp_all_here;     // /tp-all-here
//	protected final CMD_TP_Ask_All_Here cmd_tp_ask_all_here; // /tp-ask-all-here
//	protected final CMD_TP_Offline      cmd_tp_offline;      // /tp-offline
//	protected final CMD_Back            cmd_back;            // /back
//	protected final CMD_Top             cmd_top;             // /top
//	protected final CMD_Bottom          cmd_bottom;          // /bottom
//	protected final CMD_Jump            cmd_jump;            // /jump
//	protected final CMD_Jump_Into       cmd_jump_into;       // /jump-into
//	protected final CMD_Jump_Down       cmd_jump_down;       // /jump-down
	protected final CMD_Fly             cmd_fly;             // /fly
//	protected final CMD_MOTD            cmd_motd;            // /motd
//	protected final CMD_Broadcast       cmd_broadcast;       // /broadcast
//	protected final CMD_MSG             cmd_msg;             // /msg
//	protected final CMD_Reply           cmd_reply;           // /reply
//	protected final CMD_Me              cmd_me;              // /me
	protected final CMD_GM              cmd_gm;              // /gm
	protected final CMD_GM_C            cmd_gm_c;            // /gmc
	protected final CMD_GM_S            cmd_gm_s;            // /gms
	protected final CMD_GM_A            cmd_gm_a;            // /gma
	protected final CMD_GM_Spec         cmd_gm_sp;           // /gmsp
	protected final CMD_Feed            cmd_feed;            // /feed
	protected final CMD_Heal            cmd_heal;            // /heal
	protected final CMD_Rest            cmd_rest;            // /rest
//	protected final CMD_Speed           cmd_speed;           // /speed
//	protected final CMD_Speed_Walk      cmd_speed_walk;      // /walkspeed
//	protected final CMD_Speed_Fly       cmd_speed_fly        // /flyspeed
	protected final CMD_Workbench       cmd_workbench;       // /workbench
	protected final CMD_Enderchest      cmd_enderchest;      // /enderchest
//	protected final CMD_PowerTool       cmd_powertool;       // /powertool
//	protected final CMD_Backup          cmd_backup;          // /backup
	protected final CMD_GC              cmd_gc;              // /gc
	protected final CMD_Uptime          cmd_uptime;          // /uptime



	public PluginCommands(final VitalCorePlugin plugin) {
		final FileConfiguration config = plugin.getConfig();
		final ConfigurationSection cfg = config.getConfigurationSection("Commands");
//		this.cmd_help            = (cfg.getBoolean("help"      ) ? new CMD_Help           (plugin) : null); // /help
//		this.cmd_list            = (cfg.getBoolean("list"      ) ? new CMD_List           (plugin) : null); // /list
//		this.cmd_home            = (cfg.getBoolean("home"      ) ? new CMD_Home           (plugin) : null); // /home
//		this.cmd_home_list       = (cfg.getBoolean("home"      ) ? new CMD_Home_List      (plugin) : null); // /list-homes
//		this.cmd_home_set        = (cfg.getBoolean("home"      ) ? new CMD_Home_Set       (plugin) : null); // /set-home
//		this.cmd_home_del        = (cfg.getBoolean("home"      ) ? new CMD_Home_Del       (plugin) : null); // /del-home
		this.cmd_world           = (cfg.getBoolean("world"     ) ? new CMD_World          (plugin) : null); // /world
		this.cmd_spawn           = (cfg.getBoolean("spawn"     ) ? new CMD_Spawn          (plugin) : null); // /spawn
		this.cmd_setspawn        = (cfg.getBoolean("spawn"     ) ? new CMD_Spawn_Set      (plugin) : null); // /setspawn
//		this.cmd_warp            = (cfg.getBoolean("warp"      ) ? new CMD_Warp           (plugin) : null); // /warp
//		this.cmd_setwarp         = (cfg.getBoolean("warp"      ) ? new CMD_Warp_Set       (plugin) : null); // /setwarp
//		this.cmd_tp              = (cfg.getBoolean("tp"        ) ? new CMD_TP             (plugin) : null); // /tp
//		this.cmd_tp_here         = (cfg.getBoolean("tp"        ) ? new CMD_TP_Here        (plugin) : null); // /tp-here
//		this.cmd_tp_ask          = (cfg.getBoolean("tp"        ) ? new CMD_TP_Ask         (plugin) : null); // /tp-ask
//		this.cmd_tp_ask_here     = (cfg.getBoolean("tp"        ) ? new CMD_TP_Ask_Here    (plugin) : null); // /tp-ask-here
//		this.cmd_tp_all_here     = (cfg.getBoolean("tp"        ) ? new CMD_TP_All_Here    (plugin) : null); // /tp-all-here
//		this.cmd_tp_ask_all_here = (cfg.getBoolean("tp"        ) ? new CMD_TP_Ask_All_Here(plugin) : null); // /tp-ask-all-here
//		this.cmd_tp_offline      = (cfg.getBoolean("tp"        ) ? new CMD_TP_Offline     (plugin) : null); // /tp-offline
//		this.cmd_back            = (cfg.getBoolean("back"      ) ? new CMD_Back           (plugin) : null); // /back
//		this.cmd_top             = (cfg.getBoolean("top"       ) ? new CMD_Top            (plugin) : null); // /top
//		this.cmd_bottom          = (cfg.getBoolean("bottom"    ) ? new CMD_Bottom         (plugin) : null); // /bottom
//		this.cmd_jump            = (cfg.getBoolean("jump"      ) ? new CMD_Jump           (plugin) : null); // /jump
//		this.cmd_jump_into       = (cfg.getBoolean("jump"      ) ? new CMD_Jump_Into      (plugin) : null); // /jump-into
//		this.cmd_jump_down       = (cfg.getBoolean("jump"      ) ? new CMD_Jump_Down      (plugin) : null); // /jump-down
		this.cmd_fly             = (cfg.getBoolean("fly"       ) ? new CMD_Fly            (plugin) : null); // /fly
//		this.cmd_motd            = (cfg.getBoolean("motd"      ) ? new CMD_MOTD           (plugin) : null); // /motd
//		this.cmd_broadcast       = (cfg.getBoolean("broadcast" ) ? new CMD_Broadcast      (plugin) : null); // /broadcast
//		this.cmd_msg             = (cfg.getBoolean("msg"       ) ? new CMD_MSG            (plugin) : null); // /msg
//		this.cmd_reply           = (cfg.getBoolean("msg"       ) ? new CMD_Reply          (plugin) : null); // /reply
//		this.cmd_me              = (cfg.getBoolean("me"        ) ? new CMD_Me             (plugin) : null); // /me
		this.cmd_gm              = (cfg.getBoolean("gm"        ) ? new CMD_GM             (plugin) : null); // /gm
		this.cmd_gm_c            = (cfg.getBoolean("gm"        ) ? new CMD_GM_C           (plugin) : null); // /gmc
		this.cmd_gm_s            = (cfg.getBoolean("gm"        ) ? new CMD_GM_S           (plugin) : null); // /gms
		this.cmd_gm_a            = (cfg.getBoolean("gm"        ) ? new CMD_GM_A           (plugin) : null); // /gma
		this.cmd_gm_sp           = (cfg.getBoolean("gm"        ) ? new CMD_GM_Spec        (plugin) : null); // /gmsp
		this.cmd_feed            = (cfg.getBoolean("feed"      ) ? new CMD_Feed           (plugin) : null); // /feed
		this.cmd_heal            = (cfg.getBoolean("heal"      ) ? new CMD_Heal           (plugin) : null); // /heal
		this.cmd_rest            = (cfg.getBoolean("rest"      ) ? new CMD_Rest           (plugin) : null); // /rest
//		this.cmd_speed           = (cfg.getBoolean("speed"     ) ? new CMD_Speed          (plugin) : null); // /speed
//		this.cmd_speed_walk      = (cfg.getBoolean("speed"     ) ? new CMD_Speed_Walk     (plugin) : null); // /walkspeed
//		this.cmd_speed_fly       = (cfg.getBoolean("speed"     ) ? new CMD_Speed_Fly      (plugin) : null); // /flyspeed
		this.cmd_workbench       = (cfg.getBoolean("workbench" ) ? new CMD_Workbench      (plugin) : null); // /workbench
		this.cmd_enderchest      = (cfg.getBoolean("enderchest") ? new CMD_Enderchest     (plugin) : null); // /enderchest
//		this.cmd_powertool       = (cfg.getBoolean("powertool" ) ? new CMD_PowerTool      (plugin) : null); // /powertool
//		this.cmd_backup          = (cfg.getBoolean("backup"    ) ? new CMD_Backup         (plugin) : null); // /backup
		this.cmd_gc              = (cfg.getBoolean("gc"        ) ? new CMD_GC             (plugin) : null); // /gc
		this.cmd_uptime          = (cfg.getBoolean("uptime"    ) ? new CMD_Uptime         (plugin) : null); // /uptime
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
