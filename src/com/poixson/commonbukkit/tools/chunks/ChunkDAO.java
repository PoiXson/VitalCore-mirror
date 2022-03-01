package com.poixson.commonbukkit.tools.chunks;

import java.util.List;
import java.util.Map;

import org.bukkit.World;
import org.bukkit.block.data.BlockData;

import com.poixson.commonbukkit.utils.BukkitUtils;
import com.poixson.utils.StringUtils;


public class ChunkDAO {

	public final World world;
	public final int chunkX, chunkZ;
	public final int absX, absZ;



	public ChunkDAO(final World world, final int chunkX, final int chunkZ) {
		this.world  = world;
		this.chunkX = chunkX;
		this.chunkZ = chunkZ;
		this.absX   = chunkX * 16;
		this.absZ   = chunkZ * 16;
	}



	public void setBlockJS(final Object material, final Object x, final Object y, final Object z) {
		if (material == null)
			return;
		BlockData mat = null;
		if (material instanceof String) {
			final String matStr = StringUtils.ToString(material);
			if (matStr.length() == 0)
				return;
			mat = BukkitUtils.ParseBlockType(matStr);
			if (mat == null) {
				System.out.println("Unknown block/material type: " + matStr);
				return;
			}
		} else
		if (material instanceof BlockData) {
			mat = (BlockData) material;
		}
		if (mat == null) {
			System.out.println("Unknown block/material type: " +
					StringUtils.ToString(material) + "  " + material.getClass().getName());
			return;
		}
		final int xx = jsToInt(x);
		final int yy = jsToInt(y);
		final int zz = jsToInt(z);
		this.setBlock(mat, xx, yy, zz);
	}
	public void setBlocksJS(final Object blocks) {
		final List<?> list = (List<?>) blocks;
		for (final Object entry : list) {
			if (entry == null) continue;
			@SuppressWarnings("unchecked")
			final Map<String, Object> map = (Map<String, Object>) entry;
			final Object type = map.get("type");
			final Object x    = map.get("x");
			final Object y    = map.get("y");
			final Object z    = map.get("z");
			this.setBlockJS(type, x, y, z);
		}
	}



	public static int jsToInt(final Object value) {
		if (value instanceof Integer) return ((Integer) value).intValue();
		if (value instanceof Long)    return ((Long)    value).intValue();
		if (value instanceof Double)  return ((Double)  value).intValue();
		if (value instanceof Float)   return ((Float)   value).intValue();
		throw new ClassCastException();
	}



	// override in gen/pop child classes
	public void setBlock(final BlockData material, final int x, final int y, final int z) {
		if (this.world == null) throw new NullPointerException("world");
		this.world.setBlockData(this.absX+x, y, this.absZ+z, material);
	}



	public int getSeed() {
		return (int) (this.world.getSeed() % 2147483647L);
	}



}
