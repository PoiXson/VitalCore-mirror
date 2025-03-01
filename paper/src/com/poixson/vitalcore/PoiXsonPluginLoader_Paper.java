// Generated for: VitalCore-Paper
// Sat Mar  1 01:30:46 AM EST 2025
package com.poixson.vitalcore;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import io.papermc.paper.plugin.configuration.PluginMeta;
import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.JarLibrary;


// loader for paper                            paper                          base
public class PoiXsonPluginLoader_Paper extends PoiXsonPluginLoader implements PluginLoader {

	protected Logger log         = null;
	protected Logger log_default = null;



	public PoiXsonPluginLoader_Paper() {
		super();
	}



	@Override
	public void classloader(final PluginClasspathBuilder builder) {
		final PluginProviderContext context = builder.getContext();
		try {
			// paths
			final String path_server = Paths.get("").toAbsolutePath().toString();
			final PluginMeta meta = context.getConfiguration();
			final String plugin_name = meta.getName();
			this.setLog(plugin_name);
			final String path_data   = context.getDataDirectory().toString();
			final String path_plugin = MergPths(path_server, path_data);
			if (!(new File(path_plugin)).isDirectory())
				this.create_dir(path_plugin);
			final String path_libs = MergPths(path_plugin, "libs");
			if (!(new File(path_plugin)).isDirectory())
				this.create_dir(path_libs);
			// extract libraries
			this.extract_libs(path_server, path_plugin, path_libs);
			// load libraries
			final File[] files = (new File(path_libs)).listFiles( (_path, name) -> name.endsWith(".jar") );
			if (files != null
			&&  files.length > 0) {
				for (final File file : files)
					builder.addLibrary(new JarLibrary(file.toPath()));
			}
		} catch (LibraryLoaderException e) {
			this.log().error("Failed to extract/load libraries", e);
			throw new RuntimeException(e);
		} catch (IOException e) {
			this.log().error("Failed to extract/load libraries", e);
			throw new RuntimeException(e);
		}
//TODO: load maven libraries
//		final MavenLibraryResolver resolver = new MavenLibraryResolver();
//		resolver.addRepository(new RemoteRepository("https://repo.com"));
//		resolver.addDependency("com.class.path:version");
//		builder.addLibrary(resolver);
	}



	public Logger log() {
		// cached logger
		if (this.log != null)
			return this.log;
		// default logger
		if (this.log_default != null)
			return this.log_default;
		// new default logger
		this.log_default = LoggerFactory.getLogger("pxn");
		return this.log_default;
	}

	public void setLog(final String name) {
		this.setLog(LoggerFactory.getLogger(name));
	}
	public void setLog(final Logger log) {
		this.log         = log;
		this.log_default = null;
	}

	@Override
	public void log_info(final String msg) {
		this.log().info(msg);
	}



}
