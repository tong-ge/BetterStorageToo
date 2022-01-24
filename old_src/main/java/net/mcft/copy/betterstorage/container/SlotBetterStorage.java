package io.github.tehstoneman.betterstorage.container;

import io.github.tehstoneman.betterstorage.common.inventory.ContainerBetterStorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotBetterStorage extends Slot {

	private final ContainerBetterStorage container;
	private boolean isProtected = false;
	
	public SlotBetterStorage(ContainerBetterStorage container,
	                         Inventory inventory, int index, int x, int y) {
		super(inventory, index, x, y);
		this.container = container;
	}
	
	/** Prevents the slot from being interacted with. */
	public SlotBetterStorage setProtected() {
		isProtected = true;
		return this;
	}
	/** Returns if the slot is protected. */
	public boolean isProtected() { return isProtected; }
	
	@Override
	public boolean mayPlace(ItemStack stack) {
		return (!isProtected && inventory.mayPlaceForSlot(slotNumber, stack));
	}
	
	@Override
	public boolean mayPickup(EntityPlayer player) {
		// Not sure if mayPlaceForSlot can / should be used to check
		// if items can be taken out, but I'll just use it anyway.
		return (!isProtected && inventory.mayPlaceForSlot(slotNumber, null));
	}
	
	@Override
	public void setChanged() {
		container.setChanged(slotNumber);
		super.setChanged();
	}
	
}
