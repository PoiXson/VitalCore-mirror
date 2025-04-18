// Generated for: VitalServices-Paper
// Fri Mar 21 12:45:56 AM EDT 2025
package com.poixson.vitalservices;

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


public class PoiXsonLoader_Paper implements PluginLoader, PoiXsonLoader {

	protected Logger log = LoggerFactory.getLogger("VitalServices-Paper");



	public PoiXsonLoader_Paper() {
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
			this.log = LoggerFactory.getLogger(plugin_name);
			final String path_data   = context.getDataDirectory().toString();
			final String path_plugin = this.MergPths(path_server, path_data);
			if (!(new File(path_plugin)).isDirectory())
				this.CreateDir(path_plugin);
			final String path_libs = this.MergPths(path_plugin, "libs");
			if (!(new File(path_libs)).isDirectory())
				this.CreateDir(path_libs);
			// extract libraries
			this.ExtractLibs(path_server, path_plugin, path_libs);
			// load libraries
			final File[] files = (new File(path_libs)).listFiles( (_path, name) -> name.endsWith(".jar") );
			if (files != null
			&&  files.length > 0) {
				for (final File file : files)
					builder.addLibrary(new JarLibrary(file.toPath()));
			}
		} catch (LibraryLoaderException e) {
			this.log.error("Failed to extract/load libraries", e);
			throw new RuntimeException(e);
		} catch (IOException e) {
			this.log.error("Failed to extract/load libraries", e);
			throw new RuntimeException(e);
		}
//TODO: load maven libraries
//		final MavenLibraryResolver resolver = new MavenLibraryResolver();
//		resolver.addRepository(new RemoteRepository("https://repo.com"));
//		resolver.addDependency("com.class.path:version");
//		builder.addLibrary(resolver);
	}



	@Override
	public void log_info(final String msg) {
		this.log.info(msg);
	}



}
