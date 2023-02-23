package com.poixson.commonmc.tools;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import org.bukkit.scheduler.BukkitRunnable;

import com.poixson.commonmc.pxnCommonPlugin;
import com.poixson.utils.Utils;


public class TicksPerSecond extends BukkitRunnable {

	protected static final AtomicReference<TicksPerSecond> instance = new AtomicReference<TicksPerSecond>(null);

	protected final pxnCommonPlugin plugin;

	protected final LinkedList<Double> history = new LinkedList<Double>();
	protected final AtomicReference<double[]> averages = new AtomicReference<double[]>();

	protected final AtomicLong ticks = new AtomicLong(0L);
	protected long last = 0L;



	public static TicksPerSecond Get() {
		// existing instance
		{
			final TicksPerSecond tps = instance.get();
			if (tps != null)
				return tps;
		}
		// new instance
		{
			final pxnCommonPlugin plugin = pxnCommonPlugin.GetPlugin();
			if (plugin == null) throw new RuntimeException("Failed to get pxnCommonPlugin");
			final TicksPerSecond tps = new TicksPerSecond(plugin);
			if (instance.compareAndSet(null, tps))
				return tps;
		}
		return Get();
	}
	public static double GetTPS() {
		final double[] tps = Get().getTPS();
		return tps[0];
	}



	public TicksPerSecond(final pxnCommonPlugin plugin) {
		this.plugin = plugin;
	}



	public boolean start() {
		try {
			this.runTaskTimer(this.plugin, 100L, 1L);
			synchronized (this.history) {
				this.history.clear();
				this.ticks.set(0L);
				this.last = 0L;
			}
			return true;
		} catch (IllegalStateException ignore) {}
		return false;
	}
	public boolean stop() {
		try {
			this.cancel();
			return true;
		} catch (IllegalStateException ignore) {}
		return false;
	}



	@Override
	public void run() {
		final long time = Utils.GetMS();
		this.ticks.incrementAndGet();
		// calculate once per second
		final long next = (Math.floorDiv(this.last, 1000L) * 1000L) + 999L;
		if (time > next) {
			final long last = this.last;
			this.last = time;
			if (last > 0) {
				final long since = time - last;
				final double tps = (double)since / 50.0;
				if (this.history.size() == 0) {
					for (int i=0; i<299; i++)
						this.history.push(Double.valueOf(20.0));
				}
				this.history.push(Double.valueOf(tps));
				while (this.history.size() > 300)
					this.history.removeLast();
				this.averages.set(null);
			}
		}
	}



	public double[] getTPS() {
		// cached
		{
			final double[] result = this.averages.get();
			if (result != null)
				return result;
		}
		// calculate averages
		{
			final List<Double> list = Arrays.asList( this.history.toArray(new Double[0]) );
			double total_10s = 0.0; int count_10s = 0;
			double total_1m  = 0.0; int count_1m  = 0;
			double total_5m  = 0.0; int count_5m  = 0;
			if (list.size() > 0) {
				double value;
				for (int i=list.size()-1; i>=0; i--) {
					value = list.get(i).doubleValue();
					total_5m += value; count_5m++;
					if (i < 10) { total_10s += value; count_10s++; }
					if (i < 60) { total_1m  += value; count_1m++;  }
				}
			}
			final double[] result = new double[] {
				total_10s / (double)count_10s,
				total_1m  / (double)count_1m,
				total_5m  / (double)count_5m
			};
			this.averages.set(result);
			return result;
		}
	}
	public long getTicks() {
		return this.ticks.get();
	}



}
