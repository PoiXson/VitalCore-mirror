package com.poixson.tools.plotter.placer;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.ChunkGenerator.ChunkData;
import org.bukkit.generator.LimitedRegion;


public class BlockPlacer_Wrap extends BlockPlacer {

	public final int w, h, d;



	public BlockPlacer_Wrap(final World                 world    ) { this(world,     16, Integer.MAX_VALUE, 16); }
	public BlockPlacer_Wrap(final ChunkData             chunk    ) { this(chunk,     16, Integer.MAX_VALUE, 16); }
	public BlockPlacer_Wrap(final LimitedRegion         region   ) { this(region,    16, Integer.MAX_VALUE, 16); }
	public BlockPlacer_Wrap(final BlockPlacer_WorldEdit worldedit) { this(worldedit, 16, Integer.MAX_VALUE, 16); }

	public BlockPlacer_Wrap(final World                 world,     final int w, final int d) { this(world,     w, Integer.MAX_VALUE, d); }
	public BlockPlacer_Wrap(final ChunkData             chunk,     final int w, final int d) { this(chunk,     w, Integer.MAX_VALUE, d); }
	public BlockPlacer_Wrap(final LimitedRegion         region,    final int w, final int d) { this(region,    w, Integer.MAX_VALUE, d); }
	public BlockPlacer_Wrap(final BlockPlacer_WorldEdit worldedit, final int w, final int d) { this(worldedit, w, Integer.MAX_VALUE, d); }

	public BlockPlacer_Wrap(final World                 world,     final int w, final int h, final int d) { this(world, null,  null,   null,    w, h, d); }
	public BlockPlacer_Wrap(final ChunkData             chunk,     final int w, final int h, final int d) { this(null,  chunk, null,   null,    w, h, d); }
	public BlockPlacer_Wrap(final LimitedRegion         region,    final int w, final int h, final int d) { this(null,  null,  region, null,    w, h, d); }
	public BlockPlacer_Wrap(final BlockPlacer_WorldEdit worldedit, final int w, final int h, final int d) { this(null,  null,  null, worldedit, w, h, d); }

	protected BlockPlacer_Wrap(final World world, final ChunkData chunk,
			final LimitedRegion region, final BlockPlacer_WorldEdit worldedit,
			final int w, final int h, final int d) {
		super(world, chunk, region, worldedit);
		this.w = w;
		this.h = h;
		this.d = d;
	}



	// -------------------------------------------------------------------------------
	// set/get block



	@Override
	public BlockData getBlock(final int x, final int y, final int z) {
		return super.getBlock(x%this.w, y%this.h, z%this.d);
	}
	@Override
	public void setBlock(final int x, final int y, final int z, final BlockData type) {
		super.setBlock(x%this.w, y%this.h, z%this.d, type);
	}
	@Override
	public void setBlock(final int x, final int y, final int z, final Material type) {
		super.setBlock(x%this.w, y%this.h, z%this.d, type);
	}



}
