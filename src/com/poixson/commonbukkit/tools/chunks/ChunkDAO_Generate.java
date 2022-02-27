package com.poixson.commonbukkit.tools.chunks;

import org.bukkit.block.data.BlockData;
import org.bukkit.generator.WorldInfo;
import org.bukkit.generator.ChunkGenerator.ChunkData;


public class ChunkDAO_Generate extends ChunkDAO {

	public final WorldInfo world;
	public final ChunkData chunk;



	public ChunkDAO_Generate(final WorldInfo world,
			final int chunkX, final int chunkZ,
			final ChunkData chunk) {
		super(null, chunkX, chunkZ);
		if (world == null) throw new NullPointerException("world");
		this.world = world;
		this.chunk = chunk;
	}


	public void setBlock(final BlockData material, final int x, final int y, final int z) {
		this.chunk.setBlock(x, y, z, material);
	}



}
