package io.github.tehstoneman.betterstorage.container;

import io.github.tehstoneman.betterstorage.common.inventory.ContainerCraftingStation;
import io.github.tehstoneman.betterstorage.inventory.InventoryCraftingStation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotStationOutput extends Slot
{
	public final InventoryCraftingStation startingInventory;

	public SlotStationOutput( ContainerCraftingStation container, IInventory inventory, int index, int x, int y )
	{
		super( inventory, index, x, y );
		startingInventory = container.inventoryStation;
	}

	@Override
	public boolean mayPlace( ItemStack stack )
	{
		return false;
	}

	@Override
	public boolean mayPickup( EntityPlayer player )
	{
		return super.mayPickup( player ) && startingInventory.canTake( player );
	}
}
