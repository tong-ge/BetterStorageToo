package io.github.tehstoneman.betterstorage.common.inventory;

import java.util.List;

import io.github.tehstoneman.betterstorage.common.block.BetterStorageBlocks;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityCrate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

//@InventoryContainer
public class ContainerCrate extends Container
{
	private final IItemHandler			inventoryCrate;
	private final TileEntityCrate		tileCrate;

	private final int					rows;
	public int							indexStart, indexPlayer, indexHotbar;
	private final IntReferenceHolder	volume	= IntReferenceHolder.single();

	public ContainerCrate( int windowID, PlayerInventory playerInventory, World world, BlockPos pos )
	{
		super( BetterStorageContainerTypes.CRATE, windowID );
		tileCrate = (TileEntityCrate)world.getTileEntity( pos );
		inventoryCrate = tileCrate.getCapability( CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null ).orElse( new CrateStackHandler( 18 ) );

		rows = Math.min( tileCrate.getCapacity() / 9, 6 );

		indexStart = 0;
		indexHotbar = rows * 9;
		indexPlayer = indexHotbar + 9;

		final List< Integer > slotList = ( (CrateStackHandler)inventoryCrate ).getShuffledIndexes( indexHotbar );

		for( int i = 0; i < indexHotbar; i++ )
		{
			final int x = i % 9 * 18 + 8;
			final int y = i / 9 * 18 + 18;
			addSlot( new CrateSlotHandler( inventoryCrate, slotList.get( i ), x, y ) );
		}

		final int offsetY = 17 + rows * 18;

		for( int i = 0; i < 27; i++ )
		{
			final int x = i % 9 * 18 + 8;
			final int y = 14 + i / 9 * 18;
			addSlot( new Slot( playerInventory, i + 9, x, y + offsetY ) );
		}

		for( int i = 0; i < 9; i++ )
		{
			final int x = i % 9 * 18 + 8;
			final int y = 72;
			addSlot( new Slot( playerInventory, i, x, y + offsetY ) );
		}

		trackInt( volume );
	}

	public int getRows()
	{
		return rows;
	}

	/**
	 * Returns the recorded volume of this container
	 *
	 * @return Volume
	 */
	public int getVolume()
	{
		return volume.get();
	}

	@Override
	public void detectAndSendChanges()
	{
		volume.set( ( (CrateStackHandler)inventoryCrate ).getOccupiedSlots() * 100 / inventoryCrate.getSlots() );
		super.detectAndSendChanges();
	}

	@Override
	public ItemStack transferStackInSlot( PlayerEntity player, int index )
	{
		final Slot slot = inventorySlots.get( index );
		ItemStack returnStack = ItemStack.EMPTY;

		if( slot != null && slot.getHasStack() )
		{
			final ItemStack itemStack = slot.getStack();
			returnStack = itemStack.copy();

			if( index < indexHotbar )
			{
				// Try to transfer from crate to player
				if( !mergeItemStack( itemStack, indexHotbar, inventorySlots.size(), true ) )
					return ItemStack.EMPTY;
			}
			else if( !mergeItemStack( itemStack, 0, indexHotbar, false ) )
				return ItemStack.EMPTY;
			if( itemStack.getCount() > 0 )
				( (CrateStackHandler)inventoryCrate ).addItems( itemStack );

			if( itemStack.isEmpty() )
				slot.putStack( ItemStack.EMPTY );
			else
				slot.onSlotChanged();
		}

		return returnStack;
	}

	@Override
	public void onContainerClosed( PlayerEntity playerIn )
	{
		super.onContainerClosed( playerIn );
		( (CrateStackHandler)inventoryCrate ).consolidateStacks();
		tileCrate.markDirty();
	}

	@Override
	protected boolean mergeItemStack( ItemStack stack, int startIndex, int endIndex, boolean reverseDirection )
	{
		boolean flag = super.mergeItemStack( stack, startIndex, endIndex, reverseDirection );
		if( !flag && endIndex <= indexHotbar )
			flag = ( (CrateStackHandler)inventoryCrate ).mergeItemStack( stack, 0, inventoryCrate.getSlots() ) == 0;
		return flag;
	}

	@Override
	public boolean canInteractWith( PlayerEntity playerIn )
	{
		return isWithinUsableDistance( IWorldPosCallable.of( tileCrate.getWorld(), tileCrate.getPos() ), playerIn, BetterStorageBlocks.CRATE.get() );
	}
}
