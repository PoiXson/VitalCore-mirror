package com.poixson.tools.wizards.steps;

import static com.poixson.utils.BukkitUtils.EqualsPlayer;

import java.util.concurrent.atomic.AtomicReference;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import com.poixson.tools.xJavaPlugin;
import com.poixson.tools.wizards.Wizard;


public abstract class WizardStep_BlockClick<P extends xJavaPlugin<P>>
extends WizardStep<P> implements xListener {

	protected final AtomicReference<Location> loc = new AtomicReference<Location>(null);



	public WizardStep_BlockClick(final Wizard<P> wizard,
			final String logPrefix, final String chatPrefix) {
		super(
			wizard,
			logPrefix,
			chatPrefix
		);
	}



	protected abstract void sendMessage_ClickBlock();



	@Override
	public void run() {
		this.sendClear();
		this.sendMessage_ClickBlock();
		Bukkit.getPluginManager()
			.registerEvents(this, this.getPlugin());
		this.wizard.resetTimeout();
	}



	@EventHandler
	public void onPlayerInteract(final PlayerInteractEvent event) {
		if (this.isCompleted()) {
			this.close();
			return;
		}
		final Player player = this.getPlayer();
		if (event.getHand() != EquipmentSlot.HAND) return;
		if (event.getAction() != Action.LEFT_CLICK_BLOCK
		&&  event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		if (!EqualsPlayer(player, event.getPlayer())) return;
		// hand is empty
		{
			final ItemStack stack =
				player.getInventory()
					.getItemInMainHand();
			final Material material = stack.getType();
			if (!material.isAir())
				return;
		}
		final Block block = event.getClickedBlock();
		if (block == null) return;
		final Material mat = block.getType();
		if (mat == null || !mat.isSolid())
			return;
		final Location loc = block.getLocation();
		event.setCancelled(true);
		this.loc.set(loc);
		if (this.state.compareAndSet(null, Boolean.TRUE))
			this.wizard.next();
		this.close();
	}



	@Override
	public void close() {
		super.close();
		HandlerList.unregisterAll(this);
	}



	public Location getLocation() {
		return this.loc.get();
	}



}
