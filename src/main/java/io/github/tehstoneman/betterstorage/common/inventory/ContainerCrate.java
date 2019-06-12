package io.github.tehstoneman.betterstorage.common.inventory;

import java.util.List;

import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityCrate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;

//@InventoryContainer
public class ContainerCrate extends Container
{
	private final CrateStackHandler	inventoryCrate;
	private final IInventory		inventoryPlayer;
	private final TileEntityCrate	tileCrate;

	public int						indexStart, indexPlayer, indexHotbar;

	public int						volume	= 0;

	public ContainerCrate( TileEntityCrate tileCrate, EntityPlayer player )
	{
		this.tileCrate = tileCrate;
		inventoryCrate = (CrateStackHandler)tileCrate.getCapability( CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null ).orElse( null );
		inventoryPlayer = player.inventory;

		indexStart = 0;
		indexHotbar = Math.min( tileCrate.getCapacity(), 54 );
		indexPlayer = indexHotbar + 9;
		final List< Integer > slotList = inventoryCrate.getShuffledIndexes( indexHotbar );

		for( int i = 0; i < indexHotbar; i++ )
		{
			final int x = i % 9 * 18 + 8;
			final int y = i / 9 * 18 + 18;
			addSlot( new CrateSlotHandler( inventoryCrate, slotList.get( i ), x, y ) );
		}

		for( int i = 0; i < 27; i++ )
		{
			final int x = i % 9 * 18 + 8;
			final int y = 32 + ( indexHotbar + i ) / 9 * 18;
			addSlot( new Slot( inventoryPlayer, i + 9, x, y ) );
		}

		for( int i = 0; i < 9; i++ )
		{
			final int x = i % 9 * 18 + 8;
			final int y = 90 + indexHotbar / 9 * 18;
			addSlot( new Slot( inventoryPlayer, i, x, y ) );
		}
	}

	@Override
	public boolean canInteractWith( EntityPlayer playerIn )
	{
		return true;
	}

	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();

		volume = inventoryCrate.getOccupiedSlots() * 100 / inventoryCrate.getSlots();
		for( int j = 0; j < listeners.size(); ++j )
			listeners.get( j ).sendWindowProperty( this, 0, volume );
	}

	@Override
	// @SideOnly( Side.CLIENT )
	public void updateProgressBar( int id, int val )
	{
		if( id == 0 )
			volume = val;
	}

	@Override
	public ItemStack transferStackInSlot( EntityPlayer player, int index )
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
				inventoryCrate.addItems( itemStack );

			if( itemStack.isEmpty() )
				slot.putStack( ItemStack.EMPTY );
			else
				slot.onSlotChanged();
		}

		return returnStack;
	}

	@Override
	public void onContainerClosed( EntityPlayer playerIn )
	{
		super.onContainerClosed( playerIn );
		inventoryCrate.consolidateStacks();
		tileCrate.markDirty();
	}

	@Override
	protected boolean mergeItemStack( ItemStack stack, int startIndex, int endIndex, boolean reverseDirection )
	{
		boolean flag = super.mergeItemStack( stack, startIndex, endIndex, reverseDirection );
		if( !flag && endIndex <= indexHotbar )
			flag = inventoryCrate.mergeItemStack( stack, 0, inventoryCrate.getSlots() ) == 0;
		return flag;
	}

	/** Returns the recorded volume of this container */
	public int getVolume()
	{
		return volume;
	}
}
