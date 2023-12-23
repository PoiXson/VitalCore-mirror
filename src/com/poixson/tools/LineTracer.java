package com.poixson.tools;

import java.util.HashSet;
import java.util.LinkedList;

import org.bukkit.Location;

import com.poixson.tools.dao.Iab;


public abstract class LineTracer implements Runnable {

	public final LinkedList<Iab> points = new LinkedList<Iab>();
	public final HashSet<Iab>  pointSet = new HashSet<Iab>();
	public final HashSet<Iab>  queued   = new HashSet<Iab>();
	public final HashSet<Iab>  checked  = new HashSet<Iab>();

	public final boolean allow_forking;

	public boolean ok = false;



	public LineTracer(final Iab loc) {
		this(loc, false);
	}
	public LineTracer(final Iab loc, final boolean allow_forking) {
		this(loc.a, loc.b, allow_forking);
	}
	public LineTracer(final int x, final int y, final boolean allow_forking) {
		this.allow_forking = allow_forking;
		final Iab loc = new Iab(x, y);
		this.checked.add(loc);
		if (this.isValidPoint(x, y)) {
			this.points.add(loc);
			this.pointSet.add(loc);
			this.queued.add(loc);
			this.ok = true;
		}
	}



	public abstract boolean isValidPoint(final int x, final int y);
	public abstract void check(final Iab from);



	@Override
	public void run() {
		if (!this.ok || this.queued.size() == 0) return;
		Iab loc;
		while (this.ok) {
			if (this.queued.size() == 0) break;
			loc = null;
			for (final Iab dao : this.queued) {
				loc = dao;
				break;
			}
			if (loc == null) break;
			this.queued.remove(loc);
			this.check(loc);
		}
	}



	public boolean add(final int x, final int y) {
		return this.add(new Iab(x, y));
	}
	public boolean add(final Location loc) {
		return this.add(new Iab(loc.getBlockX(), loc.getBlockZ()));
	}
	public boolean add(final Iab loc) {
		// already contains point
		if (this.contains(loc)) return false;
		// connect to end
		if (1 == this.getDistance(this.points.getLast(), loc)) {
			this.points.addLast(loc);
			this.pointSet.add(loc);
			return true;
		}
		// connect to front
		if (1 == this.getDistance(this.points.getFirst(), loc)) {
			this.points.addFirst(loc);
			this.pointSet.add(loc);
			return true;
		}
		// find connecting point
		if (this.allow_forking) {
			final Iab near = this.getNear(loc);
			if (near == null) return false;
			final int index = this.points.indexOf(near);
			if (index == -1) return false;
			this.points.add(index+1, loc);
			this.pointSet.add(loc);
			return true;
		}
		return false;
	}



	public boolean contains(final int x, final int y) {
		return this.contains(new Iab(x, y));
	}
	public boolean contains(final Location loc) {
		return this.contains(new Iab(loc.getBlockX(), loc.getBlockZ()));
	}
	public boolean contains(final Iab loc) {
		return this.pointSet.contains(loc);
	}



	public Iab getNear(final Location loc) {
		return this.getNear(loc.getBlockX(), loc.getBlockZ());
	}
	public Iab getNear(final Iab loc) {
		return this.getNear(loc.a, loc.b);
	}
	public Iab getNear(final int x, final int y) {
		return this.getNear(x, y, 1);
	}

	public Iab getNear(final Location loc, final int distance) {
		return this.getNear(loc.getBlockX(), loc.getBlockZ(), distance);
	}
	public Iab getNear(final Iab loc, final int distance) {
		return this.getNear(loc.a, loc.b, distance);
	}
	public Iab getNear(final int x, final int y, final int distance) {
		Iab near = null;
		int nearest = distance;
		for (final Iab point : this.points) {
			final int dist = this.getDistance(point.a, point.b, x, y);
			if (nearest >= dist) {
				nearest = dist;
				near = point;
				if (nearest == 0)
					break;
			}
		}
		return near;
	}



	public int getDistance(final Location locA, final Location locB) {
		return
			this.getDistance(
				locA.getBlockX(), locA.getBlockZ(),
				locB.getBlockX(), locB.getBlockZ()
			);
	}
	public int getDistance(final Iab locA, final Iab locB) {
		return
			this.getDistance(
				locA.a, locA.b,
				locB.a, locB.b
			);
	}
	public int getDistance(
			final int x1, final int y1,
			final int x2, final int y2) {
		return Math.abs(x1 - x2) + Math.abs(y1 - y2);
	}



}
