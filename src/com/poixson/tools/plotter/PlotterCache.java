package com.poixson.tools.plotter;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import com.poixson.tools.abstractions.Triple;


public class PlotterCache {

	public static final int DEFAULT_TIMEOUT_TICKS = 20;

	protected final ThreadLocal<Map<String, PlotterCacheDAO>> cache =
			new ThreadLocal<Map<String, PlotterCacheDAO>>();

	protected final String path_local;
	protected final String path_res;

	protected final Class<?> clss;



	public PlotterCache(final String path_local, final String path_res, final Class<?> clss) {
		this.path_local = path_local;
		this.path_res   = path_res;
		this.clss       = clss;
	}



	public Triple<BlockPlotter, StringBuilder[][], String> get(final String structure) {
		final Map<String, PlotterCacheDAO> map = this.getLocalHashMap();
		// cached structure
		{
			final PlotterCacheDAO dao = map.get(structure);
			if (dao != null) {
				dao.ticks.set(0);
				return dao.getTriple();
			}
		}
		// load structure
		try {
			final String file = structure + ".json";
			final String file_local = this.path_local + file;
			final String file_res   = this.path_res   + file;
			final Triple<BlockPlotter, StringBuilder[][], String> tup =
				BlockPlotter.Load(this.clss, file_local, file_res);
			if (tup == null) {
				throw new RuntimeException(String.format(
					"Failed to load structure: %s  loc: %s  res: %s",
					structure, file_local, file_res
				));
			}
			final PlotterCacheDAO dao = new PlotterCacheDAO(tup.key, tup.val, tup.ent);
			map.put(structure, dao);
			return tup;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public Map<String, PlotterCacheDAO> getLocalHashMap() {
		// existing
		{
			final Map<String, PlotterCacheDAO> map = this.cache.get();
			if (map != null)
				return map;
		}
		// new local
		{
			final Map<String, PlotterCacheDAO> map = new HashMap<String, PlotterCacheDAO>();
			this.cache.set(map);
			return map;
		}
	}



	public void tick() {
		final Map<String, PlotterCacheDAO> map = this.cache.get();
		if (map != null) {
			final LinkedList<String> removing = new LinkedList<String>();
			for (final Entry<String, PlotterCacheDAO> entry : map.entrySet()) {
				final int ticks = entry.getValue().ticks.incrementAndGet();
				if (ticks > DEFAULT_TIMEOUT_TICKS)
					removing.add(entry.getKey());
			}
			if (removing.size() > 0) {
				for (final String key : removing)
					map.remove(key);
			}
		}
	}



	public class PlotterCacheDAO {

		public final BlockPlotter plot;
		public final StringBuilder[][] matrix;
		public final String script;

		public final AtomicInteger ticks = new AtomicInteger(0);

		public PlotterCacheDAO(final BlockPlotter plot,
				final StringBuilder[][] matrix, final String script) {
			this.plot   = plot;
			this.matrix = matrix;
			this.script = script;
		}

		public Triple<BlockPlotter, StringBuilder[][], String> getTriple() {
			return new Triple<BlockPlotter, StringBuilder[][], String>(
				this.plot,
				this.matrix,
				this.script
			);
		}

	}



}
