package com.poixson.commonmc.tools.scripting.loader;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.bukkit.plugin.java.JavaPlugin;


public abstract class ScriptLoader {

	protected final JavaPlugin plugin;

	protected final AtomicReference<ScriptSourceDAO[]> sources = new AtomicReference<ScriptSourceDAO[]>(null);
	protected final AtomicReference<Map<String, String>> flags = new AtomicReference<Map<String, String>>(null);
	protected final AtomicReference<String[]>          imports = new AtomicReference<String[]>(null);
	protected final AtomicReference<String[]>          exports = new AtomicReference<String[]>(null);



	public ScriptLoader(final JavaPlugin plugin) {
		this.plugin = plugin;
	}



	public void reload() {
		this.sources.set(null);
		try {
			this.getSources();
		} catch (FileNotFoundException ignore) {}
	}

	public abstract boolean hasChanged();

	public abstract ScriptSourceDAO[] getSources()
			throws FileNotFoundException;

	protected abstract void loadSources(final String filename,
			final LinkedList<ScriptSourceDAO> list,
			final Map<String, String> flags,
			final Set<String> imports, final Set<String> exports)
			throws FileNotFoundException;

	protected void parseHeader(final String code,
			final LinkedList<ScriptSourceDAO> list,
			final Map<String, String> flags,
			final Set<String> imports, final Set<String> exports)
			throws FileNotFoundException {
		String lines = code;
		String line;
		int pos;
		LOOP_LINES:
		while (true) {
			// blank line
			if (lines.startsWith("\n")) {
				lines = lines.substring(1);
				continue LOOP_LINES;
			}
			// block comment
			if (lines.startsWith("/*")) {
				pos = lines.indexOf("*/");
				if (pos == -1) return;
				lines = lines.substring(pos + 2);
				continue LOOP_LINES;
			}
			if (lines.startsWith("//#")) {
				pos = lines.indexOf('\n');
				if (pos == -1) {
					line  = lines.substring(3).trim();
					lines = "";
				} else {
					line  = lines.substring(3, pos).trim();
					lines = lines.substring(pos + 1);
				}
				if (line.length() == 0)
					break LOOP_LINES;
				pos = line.indexOf('=');
				// statement flag
				if (pos == -1) {
					flags.put(line, null);
				// key/value flag
				} else {
					final String key = line.substring(0, pos);
					final String val = line.substring(pos + 1);
					FLAG_SWITCH:
					switch (key) {
					//#include=file.js
					case "include": this.loadSources(val, list, flags, imports, exports); break FLAG_SWITCH;
					//#import=var
					case "import": imports.add(val); break FLAG_SWITCH;
					//#export=var
					case "export": exports.add(val); break FLAG_SWITCH;
					default:    flags.put(key, val); break FLAG_SWITCH;
					} // end FLAG_SWITCH
				}
				continue LOOP_LINES;
			} // end //#
			if (lines.startsWith("//")) {
				pos = lines.indexOf("\n");
				if (pos == -1) break LOOP_LINES;
				lines = lines.substring(pos + 1);
				continue LOOP_LINES;
			}
			break LOOP_LINES;
		} // end LOOP_LINES
	}



	// -------------------------------------------------------------------------------



	public abstract String getName();
	public abstract String getScriptFile();



	public boolean hasFlag(final String key) {
		final Map<String, String> flags = this.flags.get();
		return (flags == null ? false : flags.containsKey(key));
	}
	public String getFlag(final String key) {
		final Map<String, String> flags = this.flags.get();
		return (flags == null ? null : flags.get(key));
	}



	public Map<String, String> getFlags() {
		return this.flags.get();
	}
	public String[] getImports() {
		final String[] imports = this.imports.get();
		return (imports == null ? new String[0] : imports);
	}
	public String[] getExports() {
		final String[] exports = this.exports.get();
		return (exports == null ? new String[0] : exports);
	}



}
