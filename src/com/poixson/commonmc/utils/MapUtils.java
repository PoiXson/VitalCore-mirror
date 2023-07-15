package com.poixson.commonmc.utils;

import java.awt.Color;

import org.bukkit.map.MapPalette;


public final class MapUtils {
	private MapUtils() {}



	@SuppressWarnings("deprecation")
	public static Color NearestMapColor(final Color color) {
		return MapPalette.getColor(MapPalette.matchColor(color));
	}



}
