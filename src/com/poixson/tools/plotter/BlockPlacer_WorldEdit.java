package com.poixson.tools.plotter;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.scripting.CraftScriptContext;


public class BlockPlacer_WorldEdit extends BlockPlacer {

	protected final BlockVector3       origin;
	protected final CraftScriptContext context;
	protected final EditSession        session;




	public BlockPlacer_WorldEdit(final BlockVector3 origin,
			final CraftScriptContext context, final EditSession session) {
		super(null, null, null, null);
		this.origin  = origin;
		this.context = context;
		this.session = session;
	}



	// block data
	@Override
	public BlockData getBlock(final int x, final int y, final int z) {
		return BukkitAdapter.adapt(
			this.session.getBlock(this.origin.add(x, y, z))
		);
	}
	public void setBlockData(final int x, final int y, final int z, final BlockData block) {
		this.setBlock(x, y, z, block);
	}
	@Override
	public void setBlock(final int x, final int y, final int z, final BlockData block) {
		try {
			this.session.setBlock(
				this.origin.add(x, y, z),
				BukkitAdapter.adapt(block)
			);
		} catch (MaxChangedBlocksException e) {
			e.printStackTrace();
		}
	}

	// material
	public Material getType(final int x, final int y, final int z) {
		final BlockData block = this.getBlock(x, y, z);
		return (block == null ? null : block.getMaterial());
	}
	public void setType(final int x, final int y, final int z, final Material type) {
		final BlockData block = Bukkit.createBlockData(type);
		this.setBlock(x, y, z, block);
	}



}
