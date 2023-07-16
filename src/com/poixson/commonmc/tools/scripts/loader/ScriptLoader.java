package com.poixson.commonmc.tools.scripts.loader;

import java.io.FileNotFoundException;


public interface ScriptLoader {


	public boolean hasChanged();
	public void reload();

	public String getName();

	public int getFPS(final int def);

	public ScriptSourceDAO[] getSources() throws FileNotFoundException;


}
