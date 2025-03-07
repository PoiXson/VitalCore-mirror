// Generated for: VitalCore-Paper
// Sat Mar  1 01:08:52 AM EST 2025
package com.poixson.vitalcore;

import org.bukkit.plugin.java.JavaPlugin;


public class PoiXsonPluginModAdapter_Paper_VitalCore extends JavaPlugin implements PoiXsonPluginModAdapter {

	public final VitalCorePlugin xplugin;



	public PoiXsonPluginModAdapter_Paper_VitalCore() {
		super();
		this.xplugin = new VitalCorePlugin();
	}



	@Override public void onLoad() {    super.onLoad();    this.xplugin.onLoad();    }
	@Override public void onEnable() {  super.onEnable();  this.xplugin.onEnable();  }
	@Override public void onDisable() { super.onDisable(); this.xplugin.onDisable(); }



}
