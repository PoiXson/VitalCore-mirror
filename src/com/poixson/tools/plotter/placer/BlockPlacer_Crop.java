package com.poixson.tools.plotter.placer;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.ChunkGenerator.ChunkData;
import org.bukkit.generator.LimitedRegion;


public class BlockPlacer_Crop extends BlockPlacer {



	public BlockPlacer_Crop(final World                 world    ) { this(world, null,  null,   null   ); }
	public BlockPlacer_Crop(final ChunkData             chunk    ) { this(null,  chunk, null,   null   ); }
	public BlockPlacer_Crop(final LimitedRegion         region   ) { this(null,  null,  region, null   ); }
	public BlockPlacer_Crop(final BlockPlacer_WorldEdit worldedit) { this(null,  null,  null, worldedit); }

	protected BlockPlacer_Crop(final World world, final ChunkData chunk,
			final LimitedRegion region, final BlockPlacer_WorldEdit worldedit) {
		super(world, chunk, region, worldedit);
	}



	public boolean isWithin(final int x, final int y, final int z) {
		return true;
	}



	// -------------------------------------------------------------------------------
	// set/get block



	@Override
	public BlockData getBlock(final int x, final int y, final int z) {
		return (this.isWithin(x, y, z) ? super.getBlock(x, y, z) : null);
	}
	@Override
	public void setBlock(final int x, final int y, final int z, final BlockData type) {
		if (this.isWithin(x, y, z))
			super.setBlock(x, y, z, type);
	}
	@Override
	public void setBlock(final int x, final int y, final int z, final Material type) {
		if (this.isWithin(x, y, z))
			super.setBlock(x, y, z, type);
	}



}
