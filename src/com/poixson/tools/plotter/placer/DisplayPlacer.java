package com.poixson.tools.plotter.placer;

import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.EntityType;


public class DisplayPlacer extends BlockPlacer {

	protected final LinkedList<BlockDisplay> entities = new LinkedList<BlockDisplay>();



	public DisplayPlacer(final World world) {
		super(world, null, null, null);
	}



	public BlockDisplay[] getEntities() {
		return this.entities.toArray(new BlockDisplay[0]);
	}



	// -------------------------------------------------------------------------------
	// set/get block



	@Override
	public BlockData getBlock(final int x, final int y, final int z) {
		throw new UnsupportedOperationException();
	}
	@Override
	public void setBlock(final int x, final int y, final int z, final Material type) {
		this.setBlock(x, y, z, Bukkit.createBlockData(type));
	}
	@Override
	public void setBlock(final int x, final int y, final int z, final BlockData type) {
		final Location loc = this.world.getBlockAt(x, y, z).getLocation();
		final BlockDisplay entity = (BlockDisplay) this.world.spawnEntity(loc, EntityType.BLOCK_DISPLAY);
		entity.setBlock(type);
		this.entities.add(entity);
	}



}
