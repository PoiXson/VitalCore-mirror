package com.poixson;

import com.poixson.tools.commands.CommandBuilder;


public interface PoiXsonAdapter {


	public void onLoad();
	public void onEnable();
	public void onDisable();


	public String getJarDir();
	public String getJarFile();


	public CommandBuilder getCommandBuilder(final String name);


	public void log_info(final String msg);


}
