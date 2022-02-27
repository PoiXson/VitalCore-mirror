package com.poixson.commonbukkit.tools.chunks;

import org.bukkit.block.data.BlockData;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;


public class ChunkDAO_Populate extends ChunkDAO {

	public final WorldInfo world;
	public final LimitedRegion region;



	public ChunkDAO_Populate(final WorldInfo world,
			final int chunkX, final int chunkZ,
			final LimitedRegion region) {
		super(null, chunkX, chunkZ);
		if (region == null) throw new NullPointerException("region");
		this.world  = world;
		this.region = region;
	}



	public void setBlock(final BlockData material, final int x, final int y, final int z) {
		this.region.setBlockData(x, y, z, material);
	}



}
