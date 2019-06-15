package io.github.tehstoneman.betterstorage.common.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;

public class SlotStationCrafting extends Slot
{
	public SlotStationCrafting( IInventory inventoryIn, int slotIndex, int xPosition, int yPosition )
	{
		super( inventoryIn, slotIndex, xPosition, yPosition );
	}
}
