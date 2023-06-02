package com.poixson.commonmc.tools.scripts.loader;

import static com.poixson.commonmc.pxnCommonPlugin.LOG_PREFIX;
import static com.poixson.utils.FileUtils.GetLastModified;
import static com.poixson.utils.FileUtils.ReadInputStream;
import static com.poixson.utils.Utils.GetMS;
import static com.poixson.utils.Utils.SafeClose;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

import com.poixson.utils.Utils;


public class ScriptSourceDAO {
	protected static final Logger LOG = Logger.getLogger("Minecraft");

	public final String path_local;
	public final String path_resource;

	public final String filename;
	public final boolean isReal;

	public final String code;
	public final long timestamp;



	public static ScriptSourceDAO Find(final JavaPlugin plugin,
			final String path_local, final String path_resource,
			final String filename)
			throws FileNotFoundException {
		InputStream in = null;
		boolean isReal = false;
		// local file
		if (path_local != null) {
			final File file = new File(path_local, filename);
			if (file.isFile()) {
				in = new FileInputStream(file);
				isReal = true;
			}
		}
		// resource file
		if (path_resource != null) {
			final StringBuilder resFile = new StringBuilder();
			if (Utils.notEmpty(path_resource))
				resFile.append(path_resource).append('/');
			resFile.append(filename);
			if (in == null) in = plugin.getResource(resFile.toString());
			if (in == null) throw new FileNotFoundException(filename);
			final String code = ReadInputStream(in);
			SafeClose(in);
			final ScriptSourceDAO dao =
				new ScriptSourceDAO(
					isReal,
					path_local,
					path_resource,
					filename,
					code
				);
			LOG.info(String.format("%sLoaded %s script: %s", LOG_PREFIX, isReal?"local":"resource", filename));
			return dao;
		}
		throw new FileNotFoundException(filename);
	}



	public ScriptSourceDAO(final boolean isReal,
			final String path_local, final String path_resource,
			final String filename, final String code) {
		this.isReal        = isReal;
		this.path_local    = path_local;
		this.path_resource = path_resource;
		this.filename      = filename;
		this.code          = code;
		this.timestamp = GetMS();
	}



	public boolean hasFileChanged() {
		if (!this.isReal)
			return false;
		{
			final File file = new File(this.path_local, this.filename);
			try {
				final long last = GetLastModified(file) * 1000L;
				return (last > this.timestamp);
			} catch (IOException ignore) {}
			return false;
		}
	}



}
