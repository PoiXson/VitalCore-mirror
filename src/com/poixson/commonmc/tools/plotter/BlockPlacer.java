package com.poixson.commonmc.tools.plotter;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.ChunkGenerator.ChunkData;
import org.bukkit.generator.LimitedRegion;

import com.poixson.commonmc.utils.BlockUtils;
import com.poixson.exceptions.InvalidValueException;
import com.poixson.utils.Utils;


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



	public Material getType(final int x, final int y, final int z) {
		if (this.world     != null) return this.world.getType(    x, y, z);
		if (this.chunk     != null) return this.chunk.getType(    x, y, z);
		if (this.worldedit != null) return this.worldedit.getType(x, y, z);
		if (this.region != null) {
			if (this.region.isInRegion(x, y, z))
				return this.region.getType(x, y, z);
			return null;
		}
		throw new InvalidValueException("world/chunk/region");
	}
	public boolean isType(final int x, final int y, final int z, final Material match) {
		return match.equals( this.getType(x, y, z) );
	}

	public void setType(final int x, final int y, final int z, final Material type, final Set<String> special) {
		this.setType(   x, y, z, type   );
		this.setSpecial(x, y, z, special);
	}
	public void setType(final int x, final int y, final int z, final Material type) {
		if (this.world     != null) this.world.setType(    x, y, z, type); else
		if (this.chunk     != null) this.chunk.setBlock(   x, y, z, type); else
		if (this.worldedit != null) this.worldedit.setType(x, y, z, type); else
		if (this.region != null) {
			if (this.region.isInRegion(x, y, z))
				this.region.setType(x, y, z, type);
		} else throw new InvalidValueException("world/chunk/region");
	}

	public void setSpecial(final int x, final int y, final int z, final Set<String> special) {
		if (Utils.isEmpty(special)) return;
		final BlockData data = this.getBlockData(x, y, z);
		if (BlockUtils.ApplyBlockSpecial(data, special))
			this.setBlockData(x, y, z, data);
	}

	public BlockData getBlockData(final int x, final int y, final int z) {
		if (this.world     != null) return this.world.getBlockData(    x, y, z);
		if (this.chunk     != null) return this.chunk.getBlockData(    x, y, z);
		if (this.worldedit != null) return this.worldedit.getBlockData(x, y, z);
		if (this.region != null) {
			if (this.region.isInRegion(x, y, z))
				return this.region.getBlockData(x, y, z);
			return null;
		} else throw new InvalidValueException("world/chunk/region");
	}
	public void setBlockData(final int x, final int y, final int z, final BlockData data) {
		if (this.world     != null) this.world.setBlockData(    x, y, z, data); else
		if (this.chunk     != null) this.chunk.setBlock(        x, y, z, data); else
		if (this.worldedit != null) this.worldedit.setBlockData(x, y, z, data); else
		if (this.region != null) {
			if (this.region.isInRegion(x, y, z))
				this.region.setBlockData(x, y, z, data);
		} else throw new InvalidValueException("world/chunk/region");
	}



}
