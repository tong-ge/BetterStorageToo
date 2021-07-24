package io.github.tehstoneman.betterstorage.common.inventory;

import net.minecraft.entity.player.PlayerEntity;
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
	public boolean mayPlace( ItemStack stack )
	{
		if( stack.isEmpty() )
			return false;

		final IItemHandler handler = getItemHandler();
		if( !( handler instanceof CrateStackHandler ) )
			return super.mayPlace( stack );

		final CrateStackHandler crateHandler = (CrateStackHandler)handler;
		ItemStack remainder;
		final ItemStack currentStack = crateHandler.getItemFixed( index );

		crateHandler.setStackInSlotFixed( index, ItemStack.EMPTY );

		remainder = crateHandler.insertItemFixed( index, stack, true );

		crateHandler.setStackInSlotFixed( index, currentStack );

		return remainder.isEmpty() || remainder.getCount() < stack.getCount();
	}

	@Override
	public ItemStack getItem()
	{
		final IItemHandler handler = getItemHandler();
		if( !( handler instanceof CrateStackHandler ) )
			return super.getItem();

		final CrateStackHandler crateHandler = (CrateStackHandler)handler;
		return crateHandler.getItemFixed( index );
	}

	@Override
	public void set( ItemStack stack )
	{
		final IItemHandler handler = getItemHandler();
		if( !( handler instanceof CrateStackHandler ) )
			super.set( stack );

		final CrateStackHandler crateHandler = (CrateStackHandler)handler;
		crateHandler.setStackInSlotFixed( index, stack );
		setChanged();
	}

	@Override
	public int getMaxStackSize( ItemStack stack )
	{
		final IItemHandler handler = getItemHandler();
		if( !( handler instanceof CrateStackHandler ) )
			return super.getMaxStackSize( stack );

		final CrateStackHandler crateHandler = (CrateStackHandler)handler;

		final ItemStack maxAdd = stack.copy();
		final int maxInput = stack.getMaxStackSize();
		maxAdd.setCount( maxInput );

		final ItemStack currentStack = crateHandler.getItemFixed( index );

		crateHandler.setStackInSlotFixed( index, ItemStack.EMPTY );

		final ItemStack remainder = crateHandler.insertItemFixed( index, maxAdd, true );

		crateHandler.setStackInSlotFixed( index, currentStack );

		return maxInput - ( !remainder.isEmpty() ? remainder.getCount() : 0 );
	}

	@Override
	public boolean mayPickup( PlayerEntity playerIn )
	{
		final IItemHandler handler = getItemHandler();
		if( !( handler instanceof CrateStackHandler ) )
			return super.mayPickup( playerIn );

		final CrateStackHandler crateHandler = (CrateStackHandler)handler;
		return !crateHandler.extractItemFixed( index, 1, true ).isEmpty();
	}

	@Override
	public ItemStack remove( int amount )
	{
		final IItemHandler handler = getItemHandler();
		if( !( handler instanceof CrateStackHandler ) )
			return super.remove( amount );

		final CrateStackHandler crateHandler = (CrateStackHandler)handler;
		return crateHandler.extractItemFixed( index, amount, false );
	}
}