package com.poixson.pluginlib.tools.plotter;

import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.ChunkGenerator.ChunkData;
import org.bukkit.generator.LimitedRegion;

import com.poixson.exceptions.InvalidValueException;


public class BlockPlacer {

	public final World                 world;
	public final ChunkData             chunk;
	public final LimitedRegion         region;
	public final BlockPlacer_WorldEdit worldedit;

//TODO
//	public boolean wrap = false;



	public BlockPlacer(final World                 world    ) { this(world, null,  null,   null   ); }
	public BlockPlacer(final ChunkData             chunk    ) { this(null,  chunk, null,   null   ); }
	public BlockPlacer(final LimitedRegion         region   ) { this(null,  null,  region, null   ); }
	public BlockPlacer(final BlockPlacer_WorldEdit worldedit) { this(null,  null,  null, worldedit); }
	public BlockPlacer(final World world, final ChunkData chunk,
			final LimitedRegion region, final BlockPlacer_WorldEdit worldedit) {
		this.world     = world;
		this.chunk     = chunk;
		this.region    = region;
		this.worldedit = worldedit;
	}



	// -------------------------------------------------------------------------------
	// set/get block



	public BlockData getBlock(final int x, final int y, final int z) {
		if (this.world     != null) return this.world    .getBlockData(x, y, z);
		if (this.chunk     != null) return this.chunk    .getBlockData(x, y, z);
		if (this.worldedit != null) return this.worldedit.getBlock    (x, y, z);
		if (this.region != null) {
			if (this.region.isInRegion(x, y, z))
				return this.region.getBlockData(x, y, z);
			return null;
		}
		throw new InvalidValueException("world/chunk/region");
	}
	public void setBlock(final int x, final int y, final int z, final BlockData type) {
		if (this.world     != null) this.world    .setBlockData(x, y, z, type); else
		if (this.chunk     != null) this.chunk    .setBlock    (x, y, z, type); else
		if (this.worldedit != null) this.worldedit.setBlock    (x, y, z, type); else
		if (this.region != null) {
			if (this.region.isInRegion(x, y, z))
				this.region.setBlockData(x, y, z, type);
		} else throw new InvalidValueException("world/chunk/region");
	}



}
