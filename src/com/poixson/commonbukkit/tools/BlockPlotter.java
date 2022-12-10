package com.poixson.commonbukkit.tools;

import static com.poixson.commonbukkit.utils.LocationUtils.RotateXZ;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.ChunkGenerator.ChunkData;
import org.bukkit.generator.LimitedRegion;

import com.poixson.tools.dao.Ixywd;


public class BlockPlotter {

	public final World world;
	public final ChunkData chunk;
	public final LimitedRegion region;

	public final BlockFace direction;
	public final int absX, absY, absZ;
	public final int w, d;



	public BlockPlotter(final World world, final BlockFace direction,
			final int absX, final int absY, final int absZ, final int w, final int d) {
		this(world, null, null, direction, absX, absY, absZ, w, d);
	}
	public BlockPlotter(final ChunkData chunk, final BlockFace direction,
			final int absX, final int absY, final int absZ, final int w, final int d) {
		this(null, chunk, null, direction, absX, absY, absZ, w, d);
	}
	public BlockPlotter(final LimitedRegion region, final BlockFace direction,
			final int absX, final int absY, final int absZ, final int w, final int d) {
		this(null, null, region, direction, absX, absY, absZ, w, d);
	}
	protected BlockPlotter(final World world, final ChunkData chunk,
			final LimitedRegion region, final BlockFace direction,
			final int absX, final int absY, final int absZ, final int w, final int d) {
		if (world == null && chunk == null && region == null)
			throw new NullPointerException();
		this.world  = world;
		this.chunk  = chunk;
		this.region = region;
		this.direction = direction;
		this.absX = absX;
		this.absY = absY;
		this.absZ = absZ;
		this.w = w;
		this.d = d;
	}



	public void setBlock(final int x, final int y, final int z, final Material type) {
		final Ixywd rel = RotateXZ(new Ixywd(x, z, this.w, this.d), this.direction);
		this.setAbsBlock(rel.x+this.absX, y+this.absY, rel.y+this.absZ, type);
	}

	public BlockData getBlockData(final int x, final int y, final int z) {
		final Ixywd rel = RotateXZ(new Ixywd(x, z, this.w, this.d), this.direction);
		return this.getAbsBlockData(rel.x+this.absX, y+this.absY, rel.y+this.absZ);
	}
	public void setBlockData(final int x, final int y, final int z, final BlockData block) {
		final Ixywd rel = RotateXZ(new Ixywd(x, z, this.w, this.d), this.direction);
		this.setAbsBlockData(rel.x+this.absX, y+this.absY, rel.y+this.absZ, block);
	}



	protected void setAbsBlock(final int x, final int y, final int z, final Material type) {
		if (this.world != null) {
			this.world.setType(x, y, z, type);
		} else
		if (this.chunk != null) {
			this.chunk.setBlock(x, y, z, type);
		} else
		if (this.region != null) {
			this.region.setType(x, y, z, type);
		}
	}

	protected BlockData getAbsBlockData(final int x, final int y, final int z) {
		if (this.world != null) {
			return this.world.getBlockData(x, y, z);
		} else
		if (this.chunk != null) {
			return this.chunk.getBlockData(x, y, z);
		} else
		if (this.region != null) {
			return this.region.getBlockData(x, y, z);
		}
		return null;
	}
	protected void setAbsBlockData(final int x, final int y, final int z, final BlockData block) {
		if (this.world != null) {
			this.world.setBlockData(x, y, z, block);
		} else
		if (this.chunk != null) {
			this.chunk.setBlock(x, y, z, block);
		} else
		if (this.region != null) {
			this.region.setBlockData(x, y, z, block);
		}
	}



}
