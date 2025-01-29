package com.poixson.vitalcore;

import static com.poixson.utils.Utils.PrepDefinesNode;

import com.poixson.logger.xLogRoot;
import com.poixson.tools.localization.LangShelfPaper;


public enum VitalCoreDefines {

	LANG_PERMISSION_DENIED,
	LANG_NO_PERMISSION_USE_CMD,
	LANG_NO_PERMISSION_USE_CMD_ON_OTHERS,
	LANG_CONSOLE_CANNOT_USE_CMD,



	// -------------------------------------------------------------------------------
	// commands

	CMD_NAMESPACE("pxn"),

	CMD_LABELS_HELP           ("help"                ), CMD_DESC_HELP,
	CMD_LABELS_UPDATES        ("updates"             ), CMD_DESC_UPDATES,
	CMD_LABELS_BACKUP         ("backup"              ), CMD_DESC_BACKUP,
	CMD_LABELS_GC             ("gc"                  ), CMD_DESC_GC,
	CMD_LABELS_UPTIME         ("uptime"              ), CMD_DESC_UPTIME,
	CMD_LABELS_GM             ("gm"                  ), CMD_DESC_GM,
	CMD_LABELS_GM_C           ("gmc"                 ), CMD_DESC_GM_C,
	CMD_LABELS_GM_S           ("gms"                 ), CMD_DESC_GM_S,
	CMD_LABELS_GM_A           ("gma"                 ), CMD_DESC_GM_A,
	CMD_LABELS_GM_SPEC        ("gmsp", "gmspec"      ), CMD_DESC_GM_SPEC,
	CMD_LABELS_FEED           ("feed"                ), CMD_DESC_FEED,
	CMD_LABELS_HEAL           ("heal"                ), CMD_DESC_HEAL,
	CMD_LABELS_REST           ("rest"                ), CMD_DESC_REST,
	CMD_LABELS_FLY            ("fly"                 ), CMD_DESC_FLY,
	CMD_LABELS_SPEED          ("speed"               ), CMD_DESC_SPEED,
	CMD_LABELS_SPEED_WALK     ("walkspeed"           ), CMD_DESC_SPEED_WALK,
	CMD_LABELS_SPEED_FLY      ("flyspeed"            ), CMD_DESC_SPEED_FLY,
	CMD_LABELS_WORKBENCH      ("workbench"           ), CMD_DESC_WORKBENCH,
	CMD_LABELS_ENDERCHEST     ("enderchest", "echest"), CMD_DESC_ENDERCHEST,
	CMD_LABELS_POWERTOOL      ("powertool"           ), CMD_DESC_POWERTOOL,
	CMD_LABELS_LIST           ("list"                ), CMD_DESC_LIST,
	CMD_LABELS_WORLD          ("world"               ), CMD_DESC_WORLD,
	CMD_LABELS_WORLDS         ("worlds"              ), CMD_DESC_WORLDS,
	CMD_LABELS_SPAWN          ("spawn"               ), CMD_DESC_SPAWN,
	CMD_LABELS_SPAWN_SET      ("setspawn"            ), CMD_DESC_SPAWN_SET,
	CMD_LABELS_HOME           ("home"                ), CMD_DESC_HOME,
	CMD_LABELS_HOMES          ("homes", "listhomes"  ), CMD_DESC_HOMES,
	CMD_LABELS_HOME_SET       ("sethome"             ), CMD_DESC_HOME_SET,
	CMD_LABELS_HOME_DEL       ("delhome", "rmhome"   ), CMD_DESC_HOME_DEL,
	CMD_LABELS_WARP           ("warp"                ), CMD_DESC_WARP,
	CMD_LABELS_WARPS          ("warps", "listwarps"  ), CMD_DESC_WARPS,
	CMD_LABELS_WARP_SET       ("setwarp"             ), CMD_DESC_WARP_SET,
	CMD_LABELS_BACK           ("back"                ), CMD_DESC_BACK,
	CMD_LABELS_JUMP           ("jump"                ), CMD_DESC_JUMP,
	CMD_LABELS_JUMP_INTO      ("jump-into"           ), CMD_DESC_JUMP_INTO,
	CMD_LABELS_UP             ("up"                  ), CMD_DESC_UP,
	CMD_LABELS_DOWN           ("down"                ), CMD_DESC_DOWN,
	CMD_LABELS_TOP            ("top"                 ), CMD_DESC_TOP,
	CMD_LABELS_BOTTOM         ("bottom"              ), CMD_DESC_BOTTOM,
	CMD_LABELS_TP             ("tp"                  ), CMD_DESC_TP,
	CMD_LABELS_TP_HERE        ("tphere"              ), CMD_DESC_TP_HERE,
	CMD_LABELS_TP_ASK         ("tpask"               ), CMD_DESC_TP_ASK,
	CMD_LABELS_TP_ASK_HERE    ("tpaskhere"           ), CMD_DESC_TP_ASK_HERE,
	CMD_LABELS_TP_OFFLINE     ("tpoffline"           ), CMD_DESC_TP_OFFLINE,
	CMD_LABELS_MOTD           ("motd"                ), CMD_DESC_MOTD,
	CMD_LABELS_BROADCAST      ("broadcast"           ), CMD_DESC_BROADCAST,
	CMD_LABELS_MSG            ("msg"                 ), CMD_DESC_MSG,
	CMD_LABELS_REPLY          ("reply", "r"          ), CMD_DESC_REPLY,
	CMD_LABELS_ME             ("me"                  ), CMD_DESC_ME,



	// -------------------------------------------------------------------------------
	// permissions

	PERM_CMD_HELP       ("pxn.cmd.help"      ),
	PERM_CMD_UPDATES    ("pxn.cmd.updates"   ),
	PERM_UPDATES        ("pxn.updates"       ),
	PERM_CMD_BACKUP     ("pxn.cmd.backup"    ),
	PERM_CMD_GC         ("pxn.cmd.gc"        ),
	PERM_CMD_UPTIME     ("pxn.cmd.uptime"    ),
	PERM_CMD_GM         ("pxn.cmd.gm"        ), PERM_CMD_GM_OTHERS        ("pxn.cmd.gm.others"        ),
	PERM_CMD_GM_C       ("pxn.cmd.gm.c"      ), PERM_CMD_GM_C_OTHERS      ("pxn.cmd.gm.c.others"      ),
	PERM_CMD_GM_S       ("pxn.cmd.gm.s"      ), PERM_CMD_GM_S_OTHERS      ("pxn.cmd.gm.s.others"      ),
	PERM_CMD_GM_A       ("pxn.cmd.gm.a"      ), PERM_CMD_GM_A_OTHERS      ("pxn.cmd.gm.a.others"      ),
	PERM_CMD_GM_SPEC    ("pxn.cmd.gm.spec"   ), PERM_CMD_GM_SPEC_OTHERS   ("pxn.cmd.gm.spec.others"   ),
	PERM_CMD_FEED       ("pxn.cmd.feed"      ), PERM_CMD_FEED_OTHERS      ("pxn.cmd.feed.others"      ),
	PERM_CMD_HEAL       ("pxn.cmd.heal"      ), PERM_CMD_HEAL_OTHERS      ("pxn.cmd.heal.others"      ),
	PERM_CMD_REST       ("pxn.cmd.rest"      ), PERM_CMD_REST_OTHERS      ("pxn.cmd.rest.others"      ),
	PERM_CMD_FLY        ("pxn.cmd.fly"       ), PERM_CMD_FLY_OTHERS       ("pxn.cmd.fly.others"       ),
	PERM_CMD_SPEED      ("pxn.cmd.speed"     ), PERM_CMD_SPEED_OTHERS     ("pxn.cmd.speed.others"     ),
	PERM_CMD_SPEED_WALK ("pxn.cmd.speed.walk"), PERM_CMD_SPEED_WALK_OTHERS("pxn.cmd.speed.walk.others"),
	PERM_CMD_SPEED_FLY  ("pxn.cmd.speed.fly" ), PERM_CMD_SPEED_FLY_OTHERS ("pxn.cmd.speed.fly.others" ),
	PERM_CMD_WORKBENCH  ("pxn.cmd.workbench" ),
	PERM_CMD_ENDERCHEST ("pxn.cmd.enderchest"),
	PERM_CMD_POWERTOOL  ("pxn.cmd.powertool" ),
	PERM_CMD_LIST       ("pxn.cmd.list"      ),
	PERM_CMD_WORLD      ("pxn.cmd.world"     ), PERM_CMD_WORLD_OTHERS("pxn.cmd.world.others"),
	PERM_CMD_WORLDS     ("pxn.cmd.worlds"    ),
	PERM_CMD_SPAWN      ("pxn.cmd.spawn"     ), PERM_CMD_SPAWN_OTHERS("pxn.cmd.spawn.others"),
	PERM_CMD_SPAWN_SET  ("pxn.cmd.setspawn"  ),
	PERM_CMD_HOME       ("pxn.cmd.home"      ),
	PERM_CMD_WARP       ("pxn.cmd.warp"      ),
	PERM_CMD_WARP_SET   ("pxn.cmd.setwarp"   ),
	PERM_CMD_BACK       ("pxn.cmd.back"      ),
	PERM_CMD_JUMP       ("pxn.cmd.jump"      ),
	PERM_CMD_TP         ("pxn.cmd.tp"        ), PERM_CMD_TP_ASK     ("pxn.cmd.tp.ask"    ),
	PERM_CMD_TP_HERE    ("pxn.cmd.tp.here"   ), PERM_CMD_TP_ASK_HERE("pxn.cmd.tp.askhere"),
	PERM_CMD_TP_OFFLINE ("pxn.cmd.tp.offline"),
	PERM_CMD_MOTD       ("pxn.cmd.motd"      ),
	PERM_CMD_BROADCAST  ("pxn.cmd.broadcast" ),
	PERM_CMD_MSG        ("pxn.cmd.msg"       ),
	PERM_CMD_ME         ("pxn.cmd.me"        ),



	// -------------------------------------------------------------------------------
;
	public static final LangShelfPaper Lang = new LangShelfPaper(xLogRoot.GetLangShelf());

	public final String   NODE;
	public final String[] NODES;



	VitalCoreDefines() {
		this(null);
	}
	VitalCoreDefines(final String node, final String...nodes) {
		this.NODE  = PrepDefinesNode(node, this.name());
		this.NODES = nodes;
	}



	@Override
	public String toString() {
		return this.NODE;
	}
	public String[] toStrings() {
		return this.NODES;
	}



}
