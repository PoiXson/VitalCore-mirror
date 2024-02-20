package com.poixson.tools.screen;

import java.awt.Color;


public abstract class PixelSource {



	public PixelSource() {
	}



	public abstract void update();
	public abstract boolean isUpdated();

	public abstract Color[][] getPixels();



}
