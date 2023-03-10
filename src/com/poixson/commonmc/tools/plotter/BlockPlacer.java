package com.poixson.commonmc.tools.plotter;

import static com.poixson.commonmc.utils.LocationUtils.Rotate;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.ChunkGenerator.ChunkData;
import org.bukkit.generator.LimitedRegion;

import com.poixson.commonmc.utils.BlockUtils;
import com.poixson.exceptions.InvalidValueException;
import com.poixson.tools.dao.Iabcd;
import com.poixson.utils.Utils;


public class BlockPlacer {

	protected final World         world;
	protected final ChunkData     chunk;
	protected final LimitedRegion region;

	public int x = 0; public int w = 0;
	public int y = 0; public int h = 0;
	public int z = 0; public int d = 0;

	public BlockFace rotation = BlockFace.NORTH;

	public boolean wrap = false;



	public BlockPlacer(final World world) {          this(world, null, null,  null); }
	public BlockPlacer(final ChunkData chunk) {      this(null, chunk, null,  null); }
	public BlockPlacer(final LimitedRegion region) { this(null, null, region, null); }
	protected BlockPlacer(final World world, final ChunkData chunk, final LimitedRegion region) {
		this.world  = world;
		this.chunk  = chunk;
		this.region = region;
	}



	// -------------------------------------------------------------------------------
	// set/get block



	public Material getType(final int x, final int y, final int z) {
		final Iabcd loc = Rotate(new Iabcd(x, z, this.w, this.d), this.rotation);
		final int xx = this.x + loc.a;
		final int zz = this.z + loc.b;
		final int yy = this.y + y;
		if (this.world != null) return this.world.getType(xx, yy, zz);
		if (this.chunk != null) return this.chunk.getType(xx, yy, zz);
		if (this.region != null) {
			if (this.region.isInRegion(xx, yy, zz))
				return this.region.getType(xx, yy, zz);
			return null;
		}
		throw new InvalidValueException("world/chunk/region");
	}
	public boolean isType(final int x, final int y, final int z, final Material match) {
		if (match == null) return false;
		final Material existing = this.getType(x, y, z);
		return match.equals(existing);
	}

	public void setType(final int x, final int y, final int z, final Material type, final Set<String> special) {
		this.setType(x, y, z, type);
		this.setSpecial(x, y, z, special);
	}
	public void setType(final int x, final int y, final int z, final Material type) {
		if (type == null) return;
		final Iabcd loc = Rotate(new Iabcd(x, z, this.w, this.d), this.rotation);
		final int xx = this.x + loc.a;
		final int zz = this.z + loc.b;
		final int yy = this.y + y;
		if (this.world != null) this.world.setType( xx, yy, zz, type); else
		if (this.chunk != null) this.chunk.setBlock(xx, yy, zz, type); else
		if (this.region != null) {
			if (this.region.isInRegion(xx, yy, zz))
				this.region.setType(xx, yy, zz, type);
		} else throw new InvalidValueException("world/chunk/region");
	}



	public void setSpecial(final int x, final int y, final int z, final Set<String> special) {
		if (Utils.isEmpty(special)) return;
		final BlockData data = this.getBlockData(x, y, z);
		if (BlockUtils.ApplyBlockSpecial(data, special))
			this.setBlockData(x, y, z, data);
	}



	public BlockData getBlockData(final int x, final int y, final int z) {
		final Iabcd loc = Rotate(new Iabcd(x, z, this.w, this.d), this.rotation);
		final int xx = this.x + loc.a;
		final int zz = this.z + loc.b;
		final int yy = this.y + y;
		if (this.world != null) return this.world.getBlockData(xx, yy, zz); else
		if (this.chunk != null) return this.chunk.getBlockData(xx, yy, zz); else
		if (this.region != null) {
			if (this.region.isInRegion(xx, yy, zz))
				return this.region.getBlockData(xx, yy, zz);
			return null;
		} else throw new InvalidValueException("world/chunk/region");
	}
	public void setBlockData(final int x, final int y, final int z, final BlockData data) {
		final Iabcd loc = Rotate(new Iabcd(x, z, this.w, this.d), this.rotation);
		final int xx = this.x + loc.a;
		final int zz = this.z + loc.b;
		final int yy = this.y + y;
		if (this.world != null) this.world.setBlockData(xx, yy, zz, data); else
		if (this.chunk != null) this.chunk.setBlock(    xx, yy, zz, data); else
		if (this.region != null) {
			if (this.region.isInRegion(xx, yy, zz))
				this.region.setBlockData(xx, yy, zz, data);
		} else throw new InvalidValueException("world/chunk/region");
	}



	// -------------------------------------------------------------------------------
	// location/size



	public int x() { return this.x; }
	public int y() { return this.y; }
	public int z() { return this.z; }
	public int w() { return this.w; }
	public int h() { return this.h; }
	public int d() { return this.d; }

	public BlockPlacer x(final int x) { this.x = x; return this; }
	public BlockPlacer y(final int y) { this.y = y; return this; }
	public BlockPlacer z(final int z) { this.z = z; return this; }
	public BlockPlacer w(final int w) { this.w = w; return this; }
	public BlockPlacer h(final int h) { this.h = h; return this; }
	public BlockPlacer d(final int d) { this.d = d; return this; }

	public BlockPlacer wrap(final boolean wrap) { this.wrap = wrap; return this; }



}
