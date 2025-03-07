// Generated for: VitalServices-Paper
// Fri Mar  7 10:54:13 AM EST 2025
package com.poixson.vitalservices;

import org.bukkit.plugin.java.JavaPlugin;

import com.poixson.PoiXsonAdapter;
import com.poixson.services.VitalServicesPlugin;
import com.poixson.tools.commands.CommandBuilder;
import com.poixson.tools.commands.CommandBuilder_Paper;


public class PoiXsonAdapter_Paper_VitalServices
extends JavaPlugin implements PoiXsonAdapter {

	public final VitalServicesPlugin xplugin;



	public PoiXsonAdapter_Paper_VitalServices() {
		super();
		this.xplugin = new VitalServicesPlugin(this);
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
