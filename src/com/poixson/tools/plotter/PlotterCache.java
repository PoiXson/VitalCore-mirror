package com.poixson.tools.plotter;

import static com.poixson.utils.FileUtils.MergePaths;
import static com.poixson.utils.Utils.IsEmpty;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import com.poixson.tools.abstractions.Tuple;
import com.poixson.utils.StringUtils;


public class PlotterCache {

	public static final int DEFAULT_TIMEOUT_TICKS = 20;

	protected final ThreadLocal<Map<String, Tuple<BlockPlotterHolder, AtomicInteger>>> cache =
			new ThreadLocal<Map<String, Tuple<BlockPlotterHolder, AtomicInteger>>>();

	protected final String path_loc;
	protected final String path_res;

	protected final Class<?> clss;



	public PlotterCache(final String path_loc, final String path_res, final Class<?> clss) {
		this.path_loc = path_loc;
		this.path_res = path_res;
		this.clss     = clss;
	}



	public BlockPlotterHolder get(final String name) {
		if (IsEmpty(name))
			return null;
		if (name.endsWith(".json"))
			return this.get(name.substring(0, name.length()-5));
		final Map<String, Tuple<BlockPlotterHolder, AtomicInteger>> map = this.getLocalHashMap();
		// cached structure
		{
			final Tuple<BlockPlotterHolder, AtomicInteger> tup = map.get(name);
			if (tup != null) {
				tup.val.set(0);
				return tup.key;
			}
		}
		// load structure
		try {
			final String file = StringUtils.ForceEnds(".json", name);
			final String file_loc = MergePaths(this.path_loc, file);
			final String file_res = MergePaths(this.path_res, file);
			final BlockPlotterHolder holder = BlockPlotter.Load(this.clss, file_loc, file_res);
			if (holder == null)
				return null;
			final Tuple<BlockPlotterHolder, AtomicInteger> tup =
				new Tuple<BlockPlotterHolder, AtomicInteger>(
					holder, new AtomicInteger(0)
				);
			map.put(name, tup);
			return holder;
		} catch (IOException e) {
			throw new RuntimeException("Failed to load structure: "+name, e);
		}
	}

	public Map<String, Tuple<BlockPlotterHolder, AtomicInteger>> getLocalHashMap() {
		// existing
		{
			final Map<String, Tuple<BlockPlotterHolder, AtomicInteger>> map = this.cache.get();
			if (map != null)
				return map;
		}
		// new local
		{
			final Map<String, Tuple<BlockPlotterHolder, AtomicInteger>> map =
				new HashMap<String, Tuple<BlockPlotterHolder, AtomicInteger>>();
			this.cache.set(map);
			return map;
		}
	}



	public void tick() {
		final Map<String, Tuple<BlockPlotterHolder, AtomicInteger>> map = this.cache.get();
		if (map != null) {
			final LinkedList<String> removing = new LinkedList<String>();
			for (final Entry<String, Tuple<BlockPlotterHolder, AtomicInteger>> entry : map.entrySet()) {
				final Tuple<BlockPlotterHolder, AtomicInteger> tup = entry.getValue();
				final int ticks = tup.val.incrementAndGet();
				if (ticks > DEFAULT_TIMEOUT_TICKS)
					removing.add(entry.getKey());
			}
			if (removing.size() > 0) {
				for (final String key : removing)
					map.remove(key);
			}
		}
	}



}
