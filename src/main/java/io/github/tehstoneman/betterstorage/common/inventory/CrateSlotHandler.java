package io.github.tehstoneman.betterstorage.common.inventory;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class CrateSlotHandler extends SlotItemHandler
{
	private final int index;

	public CrateSlotHandler( IItemHandler itemHandler, int index, int xPosition, int yPosition )
	{
		super( itemHandler, index, xPosition, yPosition );
		this.index = index;
	}

	@Override
	public boolean isItemValid( ItemStack stack )
	{
		if( stack.isEmpty() )
			return false;

		final IItemHandler handler = getItemHandler();
		if( !( handler instanceof CrateStackHandler ) )
			return super.isItemValid( stack );

		final CrateStackHandler crateHandler = (CrateStackHandler)handler;
		ItemStack remainder;
		final ItemStack currentStack = crateHandler.getStackInSlotFixed( index );

		crateHandler.setStackInSlotFixed( index, ItemStack.EMPTY );

		remainder = crateHandler.insertItemFixed( index, stack, true );

		crateHandler.setStackInSlotFixed( index, currentStack );

		return remainder.isEmpty() || remainder.getCount() < stack.getCount();
	}

	@Override
	public ItemStack getStack()
	{
		final IItemHandler handler = getItemHandler();
		if( !( handler instanceof CrateStackHandler ) )
			return super.getStack();

		final CrateStackHandler crateHandler = (CrateStackHandler)handler;
		return crateHandler.getStackInSlotFixed( index );
	}

	@Override
	public void putStack( ItemStack stack )
	{
		final IItemHandler handler = getItemHandler();
		if( !( handler instanceof CrateStackHandler ) )
			super.putStack( stack );

		final CrateStackHandler crateHandler = (CrateStackHandler)handler;
		crateHandler.setStackInSlotFixed( index, stack );
		onSlotChanged();
	}

	@Override
	public int getItemStackLimit( ItemStack stack )
	{
		final IItemHandler handler = getItemHandler();
		if( !( handler instanceof CrateStackHandler ) )
			return super.getItemStackLimit( stack );

		final CrateStackHandler crateHandler = (CrateStackHandler)handler;

		final ItemStack maxAdd = stack.copy();
		final int maxInput = stack.getMaxStackSize();
		maxAdd.setCount( maxInput );

		final ItemStack currentStack = crateHandler.getStackInSlotFixed( index );

		crateHandler.setStackInSlotFixed( index, ItemStack.EMPTY );

		final ItemStack remainder = crateHandler.insertItemFixed( index, maxAdd, true );

		crateHandler.setStackInSlotFixed( index, currentStack );

		return maxInput - ( !remainder.isEmpty() ? remainder.getCount() : 0 );
	}

	/*
	 * @Override
	 * public boolean canTakeStack( EntityPlayer playerIn )
	 * {
	 * final IItemHandler handler = getItemHandler();
	 * if( !( handler instanceof CrateStackHandler ) )
	 * return super.canTakeStack( playerIn );
	 * 
	 * final CrateStackHandler crateHandler = (CrateStackHandler)handler;
	 * return !crateHandler.extractItemFixed( index, 1, true ).isEmpty();
	 * }
	 */

	@Override
	public ItemStack decrStackSize( int amount )
	{
		final IItemHandler handler = getItemHandler();
		if( !( handler instanceof CrateStackHandler ) )
			return super.decrStackSize( amount );

		final CrateStackHandler crateHandler = (CrateStackHandler)handler;
		return crateHandler.extractItemFixed( index, amount, false );
	}
}