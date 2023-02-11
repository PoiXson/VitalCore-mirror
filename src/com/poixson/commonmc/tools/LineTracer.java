package com.poixson.commonmc.tools;

import java.util.HashSet;
import java.util.LinkedList;

import org.bukkit.Location;

import com.poixson.tools.dao.Ixy;


public abstract class LineTracer implements Runnable {

	public final LinkedList<Ixy> points = new LinkedList<Ixy>();
	public final HashSet<Ixy> pointSet = new HashSet<Ixy>();
	public final HashSet<Ixy> queued  = new HashSet<Ixy>();
	public final HashSet<Ixy> checked = new HashSet<Ixy>();

	public final boolean allow_forking;

	public boolean ok = false;



	public LineTracer(final Ixy loc) {
		this(loc, false);
	}
	public LineTracer(final Ixy loc, final boolean allow_forking) {
		this(loc.x, loc.y, allow_forking);
	}
	public LineTracer(final int x, final int y, final boolean allow_forking) {
		this.allow_forking = allow_forking;
		final Ixy loc = new Ixy(x, y);
		this.checked.add(loc);
		if (this.isValidPoint(x, y)) {
			this.points.add(loc);
			this.pointSet.add(loc);
			this.queued.add(loc);
			this.ok = true;
		}
	}



	public abstract boolean isValidPoint(final int x, final int y);
	public abstract void check(final Ixy from);



	@Override
	public void run() {
		if (!this.ok || this.queued.size() == 0) return;
		Ixy loc;
		while (this.ok) {
			if (this.queued.size() == 0) break;
			loc = null;
			for (final Ixy dao : this.queued) {
				loc = dao;
				break;
			}
			if (loc == null) break;
			this.queued.remove(loc);
			this.check(loc);
		}
	}



	public boolean add(final int x, final int y) {
		return this.add(new Ixy(x, y));
	}
	public boolean add(final Location loc) {
		return this.add(new Ixy(loc.getBlockX(), loc.getBlockZ()));
	}
	public boolean add(final Ixy loc) {
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
			final Ixy near = this.getNear(loc);
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
		return this.contains(new Ixy(x, y));
	}
	public boolean contains(final Location loc) {
		return this.contains(new Ixy(loc.getBlockX(), loc.getBlockZ()));
	}
	public boolean contains(final Ixy loc) {
		return this.pointSet.contains(loc);
	}



	public Ixy getNear(final Location loc) {
		return this.getNear(loc.getBlockX(), loc.getBlockZ());
	}
	public Ixy getNear(final Ixy loc) {
		return this.getNear(loc.x, loc.y);
	}
	public Ixy getNear(final int x, final int y) {
		return this.getNear(x, y, 1);
	}

	public Ixy getNear(final Location loc, final int distance) {
		return this.getNear(loc.getBlockX(), loc.getBlockZ(), distance);
	}
	public Ixy getNear(final Ixy loc, final int distance) {
		return this.getNear(loc.x, loc.y, distance);
	}
	public Ixy getNear(final int x, final int y, final int distance) {
		Ixy near = null;
		int nearest = distance;
		for (final Ixy point : this.points) {
			final int dist = this.getDistance(point.x, point.y, x, y);
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
	public int getDistance(final Ixy locA, final Ixy locB) {
		return
			this.getDistance(
				locA.x, locA.y,
				locB.x, locB.y
			);
	}
	public int getDistance(
			final int x1, final int y1,
			final int x2, final int y2) {
		return Math.abs(x1 - x2) + Math.abs(y1 - y2);
	}



}
