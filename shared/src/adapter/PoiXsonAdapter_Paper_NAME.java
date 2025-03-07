// Generated for: {{{TITLE}}}
// {{{TIMESTAMP}}}
package com.poixson.{{{NAME-LOWER}}};

import org.bukkit.plugin.java.JavaPlugin;

import com.poixson.PoiXsonAdapter;
import com.poixson.services.VitalServicesPlugin;
import com.poixson.tools.commands.CommandBuilder;
import com.poixson.tools.commands.CommandBuilder_Paper;


public class PoiXsonAdapter_Paper_{{{NAME}}}
extends JavaPlugin implements PoiXsonAdapter {

	public final {{{NAME}}}Plugin xplugin;



	public PoiXsonAdapter_Paper_{{{NAME}}}() {
		super();
		this.xplugin = new {{{NAME}}}Plugin(this);
	}



	@Override public void onLoad()    { super.onLoad();    this.xplugin.onLoad();    }
	@Override public void onEnable()  { super.onEnable();  this.xplugin.onEnable();  }
	@Override public void onDisable() { super.onDisable(); this.xplugin.onDisable(); }



	@Override
	public CommandBuilder getCommandBuilder(final String name) {
		return new CommandBuilder_Paper(name);
	}



	@Override
	public void log_info(final String msg) {
		this.getLogger().info(msg);
	}



}
