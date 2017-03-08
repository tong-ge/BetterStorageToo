package io.github.tehstoneman.betterstorage.common.inventory;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class ConnectedStackHandler extends ItemStackHandler
{
	private final ItemStackHandler	inventoryUpper;
	private final ItemStackHandler	inventoryLower;

	public ConnectedStackHandler( ItemStackHandler inventoryUpper, ItemStackHandler inventoryLower )
	{
		super();
		this.inventoryUpper = inventoryUpper;
		this.inventoryLower = inventoryLower;
	}

	@Override
	public void setStackInSlot( int slot, ItemStack stack )
	{
		final int divide = inventoryUpper.getSlots();
		if( slot < divide )
			inventoryUpper.setStackInSlot( slot, stack );
		else
			inventoryLower.setStackInSlot( slot - divide, stack );
	}

	@Override
	public int getSlots()
	{
		return inventoryUpper.getSlots() + inventoryLower.getSlots();
	}

	@Override
	public ItemStack getStackInSlot( int slot )
	{
		final int divide = inventoryUpper.getSlots();
		if( slot < divide )
			return inventoryUpper.getStackInSlot( slot );
		return inventoryLower.getStackInSlot( slot - divide );
	}

	@Override
	public ItemStack insertItem( int slot, ItemStack stack, boolean simulate )
	{
		final int divide = inventoryUpper.getSlots();
		if( slot < divide )
			return inventoryUpper.insertItem( slot, stack, simulate );
		return inventoryLower.insertItem( slot - divide, stack, simulate );
	}

	@Override
	public ItemStack extractItem( int slot, int amount, boolean simulate )
	{
		final int divide = inventoryUpper.getSlots();
		if( slot < divide )
			return inventoryUpper.extractItem( slot, amount, simulate );
		return inventoryLower.extractItem( slot - divide, amount, simulate );
	}
}
