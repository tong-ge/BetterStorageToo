package io.github.tehstoneman.betterstorage.common.inventory;

import io.github.tehstoneman.betterstorage.common.block.BetterStorageBlocks;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerLocker extends Container
{
	private final IItemHandler			inventoryContainer;
	private final TileEntityContainer	tileContainer;

	private final int					columns;
	private final int					rows;

	public int							indexStart, indexPlayer, indexHotbar;

	public ContainerLocker( int windowId, PlayerInventory playerInventory, World world, BlockPos pos )
	{
		super( BetterStorageContainerTypes.LOCKER.get(), windowId );
		tileContainer = (TileEntityContainer)world.getBlockEntity( pos );
		tileContainer.openInventory( playerInventory.player );
		inventoryContainer = tileContainer.getCapability( CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null ).orElse( null );

		if( inventoryContainer instanceof ExpandableStackHandler )
		{
			columns = ( (ExpandableStackHandler)inventoryContainer ).getColumns();
			rows = ( (ExpandableStackHandler)inventoryContainer ).getRows();
		}
		else
		{
			columns = 9;
			rows = 3;
		}

		indexStart = 0;
		indexHotbar = rows * columns;
		indexPlayer = indexHotbar + 9;

		for( int i = 0; i < indexHotbar; i++ )
		{
			final int x = i % columns * 18 + 8;
			final int y = i / columns * 18 + 18;
			addSlot( new SlotItemHandler( inventoryContainer, i, x, y ) );
		}

		final int offsetX = ( columns - 9 ) / 2 * 18;
		final int offsetY = 17 + rows * 18;

		for( int i = 0; i < 27; i++ )
		{
			final int x = i % 9 * 18 + 8;
			final int y = 15 + i / 9 * 18;
			addSlot( new Slot( playerInventory, i + 9, offsetX + x, offsetY + y ) );
		}

		for( int i = 0; i < 9; i++ )
		{
			final int x = i % 9 * 18 + 8;
			final int y = 73;
			addSlot( new Slot( playerInventory, i, offsetX + x, offsetY + y ) );
		}
	}

	public int getColumns()
	{
		return columns;
	}

	public int getRows()
	{
		return rows;
	}

	@Override
	public void removed( PlayerEntity playerIn )
	{
		tileContainer.closeInventory( playerIn );
		super.removed( playerIn );
	}

	@Override
	public ItemStack quickMoveStack( PlayerEntity playerIn, int index )
	{
		final Slot slot = slots.get( index );
		ItemStack returnStack = ItemStack.EMPTY;

		if( slot != null && slot.hasItem() )
		{
			final ItemStack itemStack = slot.getItem();
			returnStack = itemStack.copy();

			if( index < indexHotbar )
			{
				// Try to transfer from container to player
				if( !moveItemStackTo( itemStack, indexHotbar, slots.size(), true ) )
					return ItemStack.EMPTY;
			} // Otherwise try to transfer from player to container
			else if( !moveItemStackTo( itemStack, 0, indexHotbar, false ) )
				return ItemStack.EMPTY;

			if( itemStack.isEmpty() )
				slot.set( ItemStack.EMPTY );
			else
				slot.setChanged();
		}

		return returnStack;
	}

	@Override
	public boolean stillValid( PlayerEntity playerIn )
	{
		return stillValid( IWorldPosCallable.create( tileContainer.getLevel(), tileContainer.getBlockPos() ), playerIn,
				BetterStorageBlocks.LOCKER.get() );
	}
}
