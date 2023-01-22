package com.poixson.commonmc.tools;

import org.bukkit.scheduler.BukkitRunnable;


public class DelayedBlockPlotter extends BukkitRunnable {

	protected final BlockPlotter plotter;

	protected final String axis;
	protected final int dims;

	protected final StringBuilder     matrix1d;
	protected final StringBuilder[]   matrix2d;
	protected final StringBuilder[][] matrix3d;



	public DelayedBlockPlotter(final BlockPlotter plotter, final String axis,
			final StringBuilder matrix) {
		this.plotter = plotter;
		this.axis = axis;
		this.dims = 1;
		this.matrix1d = matrix;
		this.matrix2d = null;
		this.matrix3d = null;
	}
	public DelayedBlockPlotter(final BlockPlotter plotter, final String axis,
			final StringBuilder[] matrix) {
		this.plotter = plotter;
		this.axis = axis;
		this.dims = 2;
		this.matrix1d = null;
		this.matrix2d = matrix;
		this.matrix3d = null;
	}
	public DelayedBlockPlotter(final BlockPlotter plotter, final String axis,
			final StringBuilder[][] matrix) {
		this.plotter = plotter;
		this.axis = axis;
		this.dims = 3;
		this.matrix1d = null;
		this.matrix2d = null;
		this.matrix3d = matrix;
	}



	@Override
	public void run() {
		switch (this.dims) {
		case 1: this.plotter.place1D(this.axis.charAt(0), this.matrix1d); break;
		case 2: this.plotter.place2D(this.axis,           this.matrix2d); break;
		case 3: this.plotter.place3D(this.axis,           this.matrix3d); break;
		default: throw new RuntimeException("Unknown plotter dimensions: "+Integer.toString(this.dims));
		}
	}



}
