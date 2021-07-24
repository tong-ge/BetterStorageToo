package io.github.tehstoneman.betterstorage.inventory;

import io.github.tehstoneman.betterstorage.utils.StackUtils;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * Basic IInventory with name and default
 * remove and getItemOnClosing,
 */
public abstract class InventoryBetterStorage implements IInventory
{
	private final String name;

	public InventoryBetterStorage( String name )
	{
		this.name = name;
	}

	public InventoryBetterStorage()
	{
		this( "" );
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public ItemStack remove( int slot, int amount )
	{
		ItemStack stack = getItem( slot );
		if( stack == null )
			return null;
		amount = Math.min( amount, stack.stackSize );
		if( amount < stack.stackSize )
		{
			stack.stackSize -= amount;
			stack = StackUtils.copyStack( stack, amount );
			setChanged();
		}
		else
			setInventorySlotContents( slot, null );
		return stack;
	}

	/*
	 * @Override
	 * public ItemStack getItemOnClosing(int slot) {
	 * ItemStack stack = getItem(slot);
	 * if (stack == null) return null;
	 * setInventorySlotContents(slot, null);
	 * return stack;
	 * }
	 */

	@Override
	public boolean hasCustomName()
	{
		return false;
	}

	@Override
	public boolean mayPlaceForSlot( int slot, ItemStack stack )
	{
		return true;
	}
}
