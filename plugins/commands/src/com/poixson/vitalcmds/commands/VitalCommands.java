package com.poixson.vitalcore.commands;

import static com.poixson.vitalcore.VitalCoreDefines.CMD_DESC_BACK;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_DESC_BACKUP;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_DESC_BOTTOM;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_DESC_BROADCAST;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_DESC_DOWN;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_DESC_ENDERCHEST;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_DESC_FEED;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_DESC_FLY;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_DESC_GC;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_DESC_GM;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_DESC_GM_A;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_DESC_GM_C;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_DESC_GM_S;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_DESC_GM_SPEC;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_DESC_HEAL;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_DESC_HELP;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_DESC_HOME;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_DESC_HOMES;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_DESC_HOME_DEL;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_DESC_HOME_SET;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_DESC_JUMP;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_DESC_JUMP_INTO;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_DESC_LIST;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_DESC_ME;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_DESC_MOTD;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_DESC_MSG;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_DESC_POWERTOOL;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_DESC_REPLY;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_DESC_REST;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_DESC_SPAWN;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_DESC_SPAWN_SET;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_DESC_SPEED;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_DESC_SPEED_FLY;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_DESC_SPEED_WALK;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_DESC_TOP;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_DESC_TP;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_DESC_TP_ASK;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_DESC_TP_ASK_HERE;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_DESC_TP_HERE;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_DESC_TP_OFFLINE;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_DESC_UP;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_DESC_UPDATES;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_DESC_UPTIME;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_DESC_WARP;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_DESC_WARPS;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_DESC_WARP_SET;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_DESC_WORKBENCH;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_DESC_WORLD;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_DESC_WORLDS;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_BACK;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_BACKUP;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_BOTTOM;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_BROADCAST;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_DOWN;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_ENDERCHEST;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_FEED;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_FLY;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_GC;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_GM;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_GM_A;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_GM_C;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_GM_S;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_GM_SPEC;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_HEAL;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_HELP;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_HOME;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_HOMES;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_HOME_DEL;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_HOME_SET;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_JUMP;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_JUMP_INTO;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_LIST;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_ME;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_MOTD;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_MSG;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_POWERTOOL;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_REPLY;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_REST;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_SPAWN;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_SPAWN_SET;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_SPEED;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_SPEED_FLY;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_SPEED_WALK;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_TOP;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_TP;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_TP_ASK;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_TP_ASK_HERE;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_TP_HERE;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_TP_OFFLINE;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_UP;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_UPDATES;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_UPTIME;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_WARP;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_WARPS;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_WARP_SET;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_WORKBENCH;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_WORLD;
import static com.poixson.vitalcore.VitalCoreDefines.CMD_LABELS_WORLDS;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import com.poixson.tools.commands.PluginCommandsHolder;
import com.poixson.vitalcore.VitalCorePlugin;

import io.papermc.paper.command.brigadier.Commands;


public class VitalCoreCommands extends PluginCommandsHolder<VitalCorePlugin> implements
CMD_Help,
CMD_Updates,
CMD_Backup,
CMD_GC,
CMD_Uptime,
CMD_GM,
CMD_GM_C,
CMD_GM_S,
CMD_GM_A,
CMD_GM_Spec,
CMD_Feed,
CMD_Heal,
CMD_Rest,
CMD_Fly,
CMD_Speed,
CMD_Speed_Walk,
CMD_Speed_Fly,
CMD_Workbench,
CMD_Enderchest,
CMD_PowerTool,
CMD_List,
CMD_World,
CMD_Worlds,
CMD_Spawn,
CMD_Spawn_Set,
CMD_Home,
CMD_Homes,
CMD_Home_Set,
CMD_Home_Del,
CMD_Warp,
CMD_Warps,
CMD_Warp_Set,
CMD_Back,
CMD_Jump,
CMD_Jump_Into,
CMD_Up,
CMD_Down,
CMD_Top,
CMD_Bottom,
CMD_TP,
CMD_TP_Here,
CMD_TP_Ask,
CMD_TP_Ask_Here,
CMD_TP_Offline,
CMD_MOTD,
CMD_Broadcast,
CMD_MSG,
CMD_Reply,
CMD_Me {



	public VitalCoreCommands(final VitalCorePlugin plugin) {
		super(plugin);
	}



	@Override
	protected void register_commands(final Commands registrar) {
		final FileConfiguration config = this.plugin.getConfig();
		final ConfigurationSection cfg = config.getConfigurationSection("Commands");
		if (cfg.getBoolean("help"      )) this.register_cmd(registrar, CMD_Help      .super.register_Help      (this.plugin), CMD_DESC_HELP      .NODE, CMD_LABELS_HELP      .NODES); // /help
		if (cfg.getBoolean("updates"   )) this.register_cmd(registrar, CMD_Updates   .super.register_Updates   (this.plugin), CMD_DESC_UPDATES   .NODE, CMD_LABELS_UPDATES   .NODES); // /updates
		if (cfg.getBoolean("backup"    )) this.register_cmd(registrar, CMD_Backup    .super.register_Backup    (this.plugin), CMD_DESC_BACKUP    .NODE, CMD_LABELS_BACKUP    .NODES); // /backup
		if (cfg.getBoolean("gc"        )) this.register_cmd(registrar, CMD_GC        .super.register_GC        (this.plugin), CMD_DESC_GC        .NODE, CMD_LABELS_GC        .NODES); // /gc
		if (cfg.getBoolean("uptime"    )) this.register_cmd(registrar, CMD_Uptime    .super.register_Uptime    (this.plugin), CMD_DESC_UPTIME    .NODE, CMD_LABELS_UPTIME    .NODES); // /uptime
		if (cfg.getBoolean("feed"      )) this.register_cmd(registrar, CMD_Feed      .super.register_Feed      (this.plugin), CMD_DESC_FEED      .NODE, CMD_LABELS_FEED      .NODES); // /feed
		if (cfg.getBoolean("heal"      )) this.register_cmd(registrar, CMD_Heal      .super.register_Heal      (this.plugin), CMD_DESC_HEAL      .NODE, CMD_LABELS_HEAL      .NODES); // /heal
		if (cfg.getBoolean("rest"      )) this.register_cmd(registrar, CMD_Rest      .super.register_Rest      (this.plugin), CMD_DESC_REST      .NODE, CMD_LABELS_REST      .NODES); // /rest
		if (cfg.getBoolean("fly"       )) this.register_cmd(registrar, CMD_Fly       .super.register_Fly       (this.plugin), CMD_DESC_FLY       .NODE, CMD_LABELS_FLY       .NODES); // /fly
		if (cfg.getBoolean("workbench" )) this.register_cmd(registrar, CMD_Workbench .super.register_Workbench (this.plugin), CMD_DESC_WORKBENCH .NODE, CMD_LABELS_WORKBENCH .NODES); // /workbench
		if (cfg.getBoolean("enderchest")) this.register_cmd(registrar, CMD_Enderchest.super.register_Enderchest(this.plugin), CMD_DESC_ENDERCHEST.NODE, CMD_LABELS_ENDERCHEST.NODES); // /enderchest
		if (cfg.getBoolean("powertool" )) this.register_cmd(registrar, CMD_PowerTool .super.register_PowerTool (this.plugin), CMD_DESC_POWERTOOL .NODE, CMD_LABELS_POWERTOOL .NODES); // /powertool
		if (cfg.getBoolean("list"      )) this.register_cmd(registrar, CMD_List      .super.register_List      (this.plugin), CMD_DESC_LIST      .NODE, CMD_LABELS_LIST      .NODES); // /list
		if (cfg.getBoolean("world"     )) this.register_cmd(registrar, CMD_World     .super.register_World     (this.plugin), CMD_DESC_WORLD     .NODE, CMD_LABELS_WORLD     .NODES); // /world
		if (cfg.getBoolean("worlds"    )) this.register_cmd(registrar, CMD_Worlds    .super.register_Worlds    (this.plugin), CMD_DESC_WORLDS    .NODE, CMD_LABELS_WORLDS    .NODES); // /worlds
		if (cfg.getBoolean("spawn"     )) this.register_cmd(registrar, CMD_Spawn     .super.register_Spawn     (this.plugin), CMD_DESC_SPAWN     .NODE, CMD_LABELS_SPAWN     .NODES); // /spawn
		if (cfg.getBoolean("setspawn"  )) this.register_cmd(registrar, CMD_Spawn_Set .super.register_Spawn_Set (this.plugin), CMD_DESC_SPAWN_SET .NODE, CMD_LABELS_SPAWN_SET .NODES); // /setspawn
		if (cfg.getBoolean("setwarp"   )) this.register_cmd(registrar, CMD_Warp_Set  .super.register_Warp_Set  (this.plugin), CMD_DESC_WARP_SET  .NODE, CMD_LABELS_WARP_SET  .NODES); // /setwarp
		if (cfg.getBoolean("back"      )) this.register_cmd(registrar, CMD_Back      .super.register_Back      (this.plugin), CMD_DESC_BACK      .NODE, CMD_LABELS_BACK      .NODES); // /back
		if (cfg.getBoolean("tpoffline" )) this.register_cmd(registrar, CMD_TP_Offline.super.register_TP_Offline(this.plugin), CMD_DESC_TP_OFFLINE.NODE, CMD_LABELS_TP_OFFLINE.NODES); // /tp-offline
		if (cfg.getBoolean("motd"      )) this.register_cmd(registrar, CMD_MOTD      .super.register_MOTD      (this.plugin), CMD_DESC_MOTD      .NODE, CMD_LABELS_MOTD      .NODES); // /motd
		if (cfg.getBoolean("broadcast" )) this.register_cmd(registrar, CMD_Broadcast .super.register_Broadcast (this.plugin), CMD_DESC_BROADCAST .NODE, CMD_LABELS_BROADCAST .NODES); // /broadcast
		if (cfg.getBoolean("me"        )) this.register_cmd(registrar, CMD_Me        .super.register_Me        (this.plugin), CMD_DESC_ME        .NODE, CMD_LABELS_ME        .NODES); // /me
		if (cfg.getBoolean("gm")) {
			this.register_cmd(registrar, CMD_GM         .super.register_GM         (this.plugin), CMD_DESC_GM         .NODE, CMD_LABELS_GM         .NODES); // /gm
			this.register_cmd(registrar, CMD_GM_C       .super.register_GM_C       (this.plugin), CMD_DESC_GM_C       .NODE, CMD_LABELS_GM_C       .NODES); // /gmc
			this.register_cmd(registrar, CMD_GM_S       .super.register_GM_S       (this.plugin), CMD_DESC_GM_S       .NODE, CMD_LABELS_GM_S       .NODES); // /gms
			this.register_cmd(registrar, CMD_GM_A       .super.register_GM_A       (this.plugin), CMD_DESC_GM_A       .NODE, CMD_LABELS_GM_A       .NODES); // /gma
			this.register_cmd(registrar, CMD_GM_Spec    .super.register_GM_Spec    (this.plugin), CMD_DESC_GM_SPEC    .NODE, CMD_LABELS_GM_SPEC    .NODES); // /gmspec
		}
		if (cfg.getBoolean("speed")) {
			this.register_cmd(registrar, CMD_Speed      .super.register_Speed      (this.plugin), CMD_DESC_SPEED      .NODE, CMD_LABELS_SPEED      .NODES); // /speed
			this.register_cmd(registrar, CMD_Speed_Walk .super.register_Speed_Walk (this.plugin), CMD_DESC_SPEED_WALK .NODE, CMD_LABELS_SPEED_WALK .NODES); // /walkspeed
			this.register_cmd(registrar, CMD_Speed_Fly  .super.register_Speed_Fly  (this.plugin), CMD_DESC_SPEED_FLY  .NODE, CMD_LABELS_SPEED_FLY  .NODES); // /flyspeed
		}
		if (cfg.getBoolean("home")) {
			this.register_cmd(registrar, CMD_Home       .super.register_Home       (this.plugin), CMD_DESC_HOME       .NODE, CMD_LABELS_HOME       .NODES); // /home
			this.register_cmd(registrar, CMD_Homes      .super.register_Homes      (this.plugin), CMD_DESC_HOMES      .NODE, CMD_LABELS_HOMES      .NODES); // /homes
			this.register_cmd(registrar, CMD_Home_Set   .super.register_Home_Set   (this.plugin), CMD_DESC_HOME_SET   .NODE, CMD_LABELS_HOME_SET   .NODES); // /sethome
			this.register_cmd(registrar, CMD_Home_Del   .super.register_Home_Del   (this.plugin), CMD_DESC_HOME_DEL   .NODE, CMD_LABELS_HOME_DEL   .NODES); // /delhome
		}
		if (cfg.getBoolean("warp")) {
			this.register_cmd(registrar, CMD_Warp       .super.register_Warp       (this.plugin), CMD_DESC_WARP       .NODE, CMD_LABELS_WARP       .NODES); // /warp
			this.register_cmd(registrar, CMD_Warps      .super.register_Warps      (this.plugin), CMD_DESC_WARPS      .NODE, CMD_LABELS_WARPS      .NODES); // /warps
		}
		if (cfg.getBoolean("jump")) {
			this.register_cmd(registrar, CMD_Jump       .super.register_Jump       (this.plugin), CMD_DESC_JUMP       .NODE, CMD_LABELS_JUMP       .NODES); // /jump
			this.register_cmd(registrar, CMD_Jump_Into  .super.register_Jump_Into  (this.plugin), CMD_DESC_JUMP_INTO  .NODE, CMD_LABELS_JUMP_INTO  .NODES); // /jump-into
			this.register_cmd(registrar, CMD_Up         .super.register_Up         (this.plugin), CMD_DESC_UP         .NODE, CMD_LABELS_UP         .NODES); // /up
			this.register_cmd(registrar, CMD_Down       .super.register_Down       (this.plugin), CMD_DESC_DOWN       .NODE, CMD_LABELS_DOWN       .NODES); // /down
			this.register_cmd(registrar, CMD_Top        .super.register_Top        (this.plugin), CMD_DESC_TOP        .NODE, CMD_LABELS_TOP        .NODES); // /top
			this.register_cmd(registrar, CMD_Bottom     .super.register_Bottom     (this.plugin), CMD_DESC_BOTTOM     .NODE, CMD_LABELS_BOTTOM     .NODES); // /bottom
		}
		if (cfg.getBoolean("tp")) {
			this.register_cmd(registrar, CMD_TP         .super.register_TP         (this.plugin), CMD_DESC_TP         .NODE, CMD_LABELS_TP         .NODES); // /tp
			this.register_cmd(registrar, CMD_TP_Here    .super.register_TP_Here    (this.plugin), CMD_DESC_TP_HERE    .NODE, CMD_LABELS_TP_HERE    .NODES); // /tphere
		}
		if (cfg.getBoolean("tpask")) {
			this.register_cmd(registrar, CMD_TP_Ask     .super.register_TP_Ask     (this.plugin), CMD_DESC_TP_ASK     .NODE, CMD_LABELS_TP_ASK     .NODES); // /tpask
			this.register_cmd(registrar, CMD_TP_Ask_Here.super.register_TP_Ask_Here(this.plugin), CMD_DESC_TP_ASK_HERE.NODE, CMD_LABELS_TP_ASK_HERE.NODES); // /tpaskhere
		}
		if (cfg.getBoolean("msg")) {
			this.register_cmd(registrar, CMD_MSG        .super.register_MSG        (this.plugin), CMD_DESC_MSG        .NODE, CMD_LABELS_MSG        .NODES); // /msg
			this.register_cmd(registrar, CMD_Reply      .super.register_Reply      (this.plugin), CMD_DESC_REPLY      .NODE, CMD_LABELS_REPLY      .NODES); // /reply
		}
	}



	public static void ConfigDefaults(final FileConfiguration config) {
		config.addDefault("Commands.help",       Boolean.FALSE);
		config.addDefault("Commands.updates",    Boolean.FALSE);
		config.addDefault("Commands.backup",     Boolean.FALSE);
		config.addDefault("Commands.gc",         Boolean.FALSE);
		config.addDefault("Commands.uptime",     Boolean.FALSE);
		config.addDefault("Commands.gm",         Boolean.FALSE);
		config.addDefault("Commands.feed",       Boolean.FALSE);
		config.addDefault("Commands.heal",       Boolean.FALSE);
		config.addDefault("Commands.rest",       Boolean.FALSE);
		config.addDefault("Commands.fly",        Boolean.FALSE);
		config.addDefault("Commands.speed",      Boolean.FALSE);
		config.addDefault("Commands.workbench",  Boolean.FALSE);
		config.addDefault("Commands.enderchest", Boolean.FALSE);
		config.addDefault("Commands.powertool",  Boolean.FALSE);
		config.addDefault("Commands.list",       Boolean.FALSE);
		config.addDefault("Commands.world",      Boolean.FALSE);
		config.addDefault("Commands.worlds",     Boolean.FALSE);
		config.addDefault("Commands.spawn",      Boolean.FALSE);
		config.addDefault("Commands.home",       Boolean.FALSE);
		config.addDefault("Commands.warp",       Boolean.FALSE);
		config.addDefault("Commands.back",       Boolean.FALSE);
		config.addDefault("Commands.jump",       Boolean.FALSE);
		config.addDefault("Commands.tp",         Boolean.FALSE);
		config.addDefault("Commands.motd",       Boolean.FALSE);
		config.addDefault("Commands.broadcast",  Boolean.FALSE);
		config.addDefault("Commands.msg",        Boolean.FALSE);
		config.addDefault("Commands.me",         Boolean.FALSE);
	}



}
