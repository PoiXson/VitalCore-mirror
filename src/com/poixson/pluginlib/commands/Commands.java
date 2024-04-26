package com.poixson.pluginlib.commands;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import com.poixson.pluginlib.pxnPluginLib;
import com.poixson.tools.commands.xCMD_Handler;


public class Commands extends xCMD_Handler {

//	protected final Command_Help       cmd_help;
//	protected final Command_List       cmd_list;
//	protected final Command_Home       cmd_home;
//	protected final Command_TP         cmd_tp;
//	protected final Command_Jump       cmd_jump;
//	protected final Command_MOTD       cmd_motd;
//	protected final Command_Broadcast  cmd_broadcast;
//	protected final Command_MSG        cmd_msg;
//	protected final Command_Me         cmd_me;
//	protected final Command_GM         cmd_gm;
//	protected final Command_Feed       cmd_feed;
//	protected final Command_Heal       cmd_heal;
//	protected final Command_Speed      cmd_speed;
//	protected final Command_Workbench  cmd_workbench;
//	protected final Command_Enderchest cmd_enderchest;
//	protected final Command_PowerTool  cmd_powertool;
//	protected final Command_Backup     cmd_backup;
	protected final Command_GC         cmd_gc;



	public Commands(final pxnPluginLib plugin) {
		super(plugin);
		final FileConfiguration config = plugin.getConfig();
		final ConfigurationSection cfg = config.getConfigurationSection("Commands");
//		if (cfg.getBoolean("help"      )) this.addCommand(this.cmd_help       = new Command_Help());       else this.cmd_help       = null;
//		if (cfg.getBoolean("list"      )) this.addCommand(this.cmd_list       = new Command_List());       else this.cmd_list       = null;
//		if (cfg.getBoolean("home"      )) this.addCommand(this.cmd_home       = new Command_Home());       else this.cmd_home       = null;
//		if (cfg.getBoolean("tp"        )) this.addCommand(this.cmd_tp         = new Command_TP());         else this.cmd_tp         = null;
//		if (cfg.getBoolean("jump"      )) this.addCommand(this.cmd_jump       = new Command_Jump());       else this.cmd_jump       = null;
//		if (cfg.getBoolean("motd"      )) this.addCommand(this.cmd_motd       = new Command_MOTD());       else this.cmd_motd       = null;
//		if (cfg.getBoolean("broadcast" )) this.addCommand(this.cmd_broadcast  = new Command_Broadcast());  else this.cmd_broadcast  = null;
//		if (cfg.getBoolean("msg"       )) this.addCommand(this.cmd_msg        = new Command_MSG());        else this.cmd_msg        = null;
//		if (cfg.getBoolean("me"        )) this.addCommand(this.cmd_me         = new Command_Me());         else this.cmd_me         = null;
//		if (cfg.getBoolean("gm"        )) this.addCommand(this.cmd_gm         = new Command_GM());         else this.cmd_gm         = null;
//		if (cfg.getBoolean("feed"      )) this.addCommand(this.cmd_feed       = new Command_Feed());       else this.cmd_feed       = null;
//		if (cfg.getBoolean("heal"      )) this.addCommand(this.cmd_heal       = new Command_Heal());       else this.cmd_heal       = null;
//		if (cfg.getBoolean("speed"     )) this.addCommand(this.cmd_speed      = new Command_Speed());      else this.cmd_speed      = null;
//		if (cfg.getBoolean("workbench" )) this.addCommand(this.cmd_workbench  = new Command_Workbench());  else this.cmd_workbench  = null;
//		if (cfg.getBoolean("enderchest")) this.addCommand(this.cmd_enderchest = new Command_Enderchest()); else this.cmd_enderchest = null;
//		if (cfg.getBoolean("powertool" )) this.addCommand(this.cmd_powertool  = new Command_PowerTool());  else this.cmd_powertool  = null;
//		if (cfg.getBoolean("backup"    )) this.addCommand(this.cmd_backup     = new Command_Backup());     else this.cmd_backup     = null;
		if (cfg.getBoolean("gc"        )) this.addCommand(this.cmd_gc         = new Command_GC());         else this.cmd_gc         = null;
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
