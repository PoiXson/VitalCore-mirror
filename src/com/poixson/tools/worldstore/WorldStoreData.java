package com.poixson.tools.worldstore;

import java.io.IOException;


public interface WorldStoreData {


	public boolean isStale(final long time);

	public boolean load() throws IOException;
	public boolean save() throws IOException;


}
