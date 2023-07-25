package com.poixson.commonmc.tools.scripting.loader;

import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;


public class ScriptLoader_File extends ScriptLoader {
	protected static final Logger LOG = Logger.getLogger("Minecraft");

	protected final String path_local;
	protected final String path_resource;
	protected final String filename;



	public ScriptLoader_File(final JavaPlugin plugin,
			final String path_local, final String path_resource,
			final String filename) {
		super(plugin);
		this.path_local    = path_local;
		this.path_resource = path_resource;
		this.filename      = filename;
	}



	@Override
	public boolean hasChanged() {
		final ScriptSourceDAO[] sources = this.sources.get();
		if (sources != null) {
			for (final ScriptSourceDAO src : sources) {
				if (src.hasFileChanged())
					return true;
			}
		}
		return false;
	}



	// -------------------------------------------------------------------------------



	@Override
	public ScriptSourceDAO[] getSources()
			throws FileNotFoundException {
		// existing sources
		{
			final ScriptSourceDAO[] sources = this.sources.get();
			if (sources != null)
				return sources;
		}
		// load sources
		{
			final LinkedList<ScriptSourceDAO> list = new LinkedList<ScriptSourceDAO>();
			final Map<String, String>        flags = new HashMap<String, String>();
			final Set<String>              exports = new HashSet<String>();
			try {
				this.loadSources("prepend.js", list, flags, exports);
			} catch (FileNotFoundException ignore) {}
			this.loadSources(this.filename, list, flags, exports);
			final ScriptSourceDAO[] sources = list.toArray(new ScriptSourceDAO[0]);
			if (this.sources.compareAndSet(null, sources)) {
				this.flags.set(Collections.unmodifiableMap(flags));
				this.exports.set(exports.toArray(new String[0]));
				return sources;
			}
		}
		return this.getSources();
	}



	// load sources recursively
	@Override
	protected void loadSources(final String filename,
			final LinkedList<ScriptSourceDAO> list,
			final Map<String, String> flags, final Set<String> exports)
			throws FileNotFoundException {
		// find local or resource file
		final ScriptSourceDAO found =
			ScriptSourceDAO.Find(
				this.plugin,
				this.path_local,
				this.path_resource,
				filename
			);
		if (found == null) throw new FileNotFoundException(filename);
		this.parseHeader(found.code, list, flags, exports);
		list.add(found);
	}



	// -------------------------------------------------------------------------------



	@Override
	public String getName() {
		return this.filename;
	}



}
