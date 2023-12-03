package com.poixson.commonmc.tools.updatechecker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

import org.bukkit.Bukkit;

import com.google.gson.Gson;
import com.poixson.tools.xTime;
import com.poixson.utils.StringUtils;


public class SpigotWebAPI {

	public static final String SPIGOT_API_URL = "https://api.spigotmc.org/simple/0.2/index.php?action=getResource&id=%s";
	public static final String SPIGOT_RES_URL = "https://www.spigotmc.org/resources/%s/";

	public int id;
	public String title;
	public String tag;

	public String current_version;
	public String native_minecraft_version;
	public Set<String> supported_minecraft_versions;

	public String icon_link;
	public String external_download_url;

	public PluginStats stats;
	public class PluginStats {
		public int downloads;
		public int updates;
		public int rating;
	}



	public static SpigotWebAPI Get(final int id) {
		try {
			return Get( String.format(SPIGOT_API_URL, Integer.valueOf(id)) );
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		} catch (IOException ignore) {
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	public static SpigotWebAPI Get(final String url)
			throws MalformedURLException, IOException, URISyntaxException {
		final HttpURLConnection connection = (HttpURLConnection) (new URI(url)).toURL().openConnection();
		connection.setConnectTimeout( (int) (new xTime("30s")).ms() );
		connection.setReadTimeout(    (int) (new xTime("30s")).ms() );
		final InputStreamReader input = new InputStreamReader(connection.getInputStream());
		final BufferedReader reader = new BufferedReader(input);
		final StringBuilder data = new StringBuilder();
		int c;
		while ((c = reader.read()) != -1) {
			data.append( (char)c );
		}
		return GetFromData(data.toString());
	}

	public static SpigotWebAPI GetFromData(final String data) {
		final Gson gson = new Gson();
		final SpigotWebAPI api = gson.fromJson(data, SpigotWebAPI.class);
		return api;
	}



	public double diffServerVersion() {
		final String server_version = Bukkit.getBukkitVersion();
		double diff = StringUtils.CompareVersions(server_version, this.native_minecraft_version);
		double d;
		for (final String vers : this.supported_minecraft_versions) {
			d = StringUtils.CompareVersions(server_version, vers);
			if (diff > Math.abs(d))
				diff = Math.abs(d);
		}
		return diff;
	}



}
