package com.poixson.pluginlib.tools;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import com.poixson.utils.FastNoiseLiteD;
import com.poixson.utils.NumberUtils;


public class PathTracer {

	protected final FastNoiseLiteD noise;

	protected final ConcurrentHashMap<Integer, Double> cache;
	protected final ThreadLocal<SoftReference<HashMap<Integer, Double>>> cacheLocal =
			new ThreadLocal<SoftReference<HashMap<Integer, Double>>>();

	protected final int start_x, start_z;



	public PathTracer(final FastNoiseLiteD noise) {
		this(noise, 0, 0, new ConcurrentHashMap<Integer, Double>());
	}
	public PathTracer(final FastNoiseLiteD noise,
			final int start_x, final int start_z) {
		this(noise, start_x, start_z, new ConcurrentHashMap<Integer, Double>());
	}
	public PathTracer(final FastNoiseLiteD noise,
			final int start_x, final int start_z,
			final ConcurrentHashMap<Integer, Double> cache) {
		this.noise = noise;
		this.cache = cache;
		this.start_x = start_x;
		this.start_z = start_z;
	}



	public boolean isPath(final int x, final int z, final int width) {
		if (z < this.start_z) return false;
		final int xx = this.getPathX(z);
		if (xx == Integer.MIN_VALUE) return false;
		return (x >= xx-width && x <= xx+width);
	}



	// find x
	public int getPathX(final int z) {
		if (z < this.start_z) return Integer.MIN_VALUE;
		Double value;
		// cached
		final HashMap<Integer, Double> local = this.getLocalCache();
		value = local.get(Integer.valueOf(z));
		if (value != null) return (int) Math.round(value.intValue());
		value = this.cache.get(Integer.valueOf(z));
		if (value != null) return (int) Math.round(value.intValue());
		// starting point
		if (z == this.start_z) {
			local.put(     Integer.valueOf(this.start_z), Double.valueOf((double)this.start_x));
			this.cache.put(Integer.valueOf(this.start_z), Double.valueOf((double)this.start_x));
			return this.start_x;
		}
		// find last cached value
		int from = this.start_z;
		double x = (double)this.start_x;
		for (int i=z-1; i>=this.start_z; i--) {
			value = local.get(Integer.valueOf(i));
			if (value != null) {
				x = value.doubleValue();
				from = i;
				break;
			}
			value = this.cache.get(Integer.valueOf(i));
			if (value != null) {
				x = value.doubleValue();
				from = i;
				break;
			}
		}
		int step;
		double valueE, valueW;
		for (int i=from+1; i<=z; i++) {
			if (i > this.start_z+10) {
				valueE = this.noise.getNoise(x+1.0, i);
				valueW = this.noise.getNoise(x-1.0, i);
				x += (valueW - valueE) * 5.0;
			}
			step = NumberUtils.MinMax( (int)Math.floor(Math.pow(i, 0.5)), 3, 1000 );
			local.put(Integer.valueOf(i), Double.valueOf(x));
			if (i % step == 0)
				this.cache.put(Integer.valueOf(i), Double.valueOf(x));
		}
		return (int) Math.round(x);
	}



	public HashMap<Integer, Double> getLocalCache() {
		// existing
		{
			final SoftReference<HashMap<Integer, Double>> soft = this.cacheLocal.get();
			if (soft != null) {
				final HashMap<Integer, Double> map = soft.get();
				if (map != null) {
					if (map.size() < 1000)
						return map;
				}
			}
		}
		// new instance
		{
			final HashMap<Integer, Double> map = new HashMap<Integer, Double>();
			this.cacheLocal.set(new SoftReference<HashMap<Integer,Double>>(map));
			return map;
		}
	}



}
