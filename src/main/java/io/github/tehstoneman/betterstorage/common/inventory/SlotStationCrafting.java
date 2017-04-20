package io.github.tehstoneman.betterstorage.common.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;

public class SlotStationCrafting extends Slot
{
	public SlotStationCrafting(IInventory inventoryIn, int slotIndex, int xPosition,
			int yPosition )
	{
		super( inventoryIn, slotIndex, xPosition, yPosition );
	}
}
