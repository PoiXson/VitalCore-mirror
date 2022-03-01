package com.poixson.commonbukkit.tools.chunks;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.World;
import org.bukkit.block.data.BlockData;

import com.poixson.commonbukkit.utils.BukkitUtils;
import com.poixson.utils.StringUtils;


public class ChunkDAO {

	public final World world;
	public final int chunkX, chunkZ;
	public final int absX, absZ;

	public final ConcurrentHashMap<String, Object> blockAliases = new ConcurrentHashMap<String, Object>();



	public ChunkDAO(final World world, final int chunkX, final int chunkZ) {
		this.world  = world;
		this.chunkX = chunkX;
		this.chunkZ = chunkZ;
		this.absX   = chunkX * 16;
		this.absZ   = chunkZ * 16;
	}



	public BlockData getBlockAlias(final String matStr) {
		final Object alias = this.blockAliases.get(matStr);
		if (alias == null)
			return null;
		if (alias instanceof String) {
			final BlockData mat = this.getBlockType(alias);
			if (mat != null) {
				this.blockAliases.replace(matStr, mat);
				return mat;
			}
		} else
		if (alias instanceof BlockData) {
			return (BlockData) alias;
		}
		this.blockAliases.remove(matStr);
		System.out.println(String.format(
			"Invalid block alias type %s :: %s",
			alias.getClass().getName(),
			matStr
		));
		return null;
	}
	public void setBlockAlias(final String key, final Object material) {
		this.blockAliases.put(key, material);
	}
	public void setBlockAliases(final Object aliases) {
		@SuppressWarnings("unchecked")
		final Map<String, Object> map = (Map<String, Object>) aliases;
		final Iterator<Entry<String, Object>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			final Entry<String, Object> entry = it.next();
			this.setBlockAlias(entry.getKey(), entry.getValue());
		}
	}



	public BlockData getBlockType(final Object material) {
		if (material == null)
			return null;
		if (material instanceof String) {
			final String matStr = StringUtils.ToString(material);
			if (matStr.length() == 0)
				return null;
			// aliases
			{
				final BlockData alias = this.getBlockAlias(matStr);
				if (alias != null)
					return alias;
			}
			// parse block type
			final BlockData mat = BukkitUtils.ParseBlockType(matStr);
			if (mat != null)
				return mat;
			System.out.println("Unknown block/material type: " + matStr);
			return null;
		} else
		if (material instanceof BlockData) {
			return (BlockData) material;
		}
		System.out.println(String.format(
			"Unknown block/material type: %s  %s",
			StringUtils.ToString(material),
			material.getClass().getName()
		));
		return null;
	}



	public void setBlockJS(final Object x, final Object y, final Object z, final Object material) {
		final BlockData mat = this.getBlockType(material);
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
