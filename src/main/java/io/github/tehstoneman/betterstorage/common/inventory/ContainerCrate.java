package io.github.tehstoneman.betterstorage.common.inventory;

import java.util.List;

import io.github.tehstoneman.betterstorage.common.block.BetterStorageBlocks;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityCrate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

//@InventoryContainer
public class ContainerCrate extends AbstractContainerMenu
{
	private final IItemHandler		inventoryCrate;
	private final TileEntityCrate	tileCrate;

	private final int				rows;
	public int						indexStart, indexPlayer, indexHotbar;
	private final DataSlot			volume	= DataSlot.standalone();

	public ContainerCrate( int windowID, Inventory playerInventory, Level world, BlockPos pos )
	{
		super( BetterStorageContainerTypes.CRATE.get(), windowID );
		tileCrate = (TileEntityCrate)world.getBlockEntity( pos );
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

		addDataSlot( volume );
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
	public void broadcastChanges()
	{
		volume.set( ( (CrateStackHandler)inventoryCrate ).getOccupiedSlots() * 100 / inventoryCrate.getSlots() );
		super.broadcastChanges();
	}

	@Override
	public ItemStack quickMoveStack( Player player, int index )
	{
		final Slot slot = slots.get( index );
		ItemStack returnStack = ItemStack.EMPTY;

		if( slot != null && slot.hasItem() )
		{
			final ItemStack itemStack = slot.getItem();
			returnStack = itemStack.copy();

			if( index < indexHotbar )
			{
				// Try to transfer from crate to player
				if( !moveItemStackTo( itemStack, indexHotbar, slots.size(), true ) )
					return ItemStack.EMPTY;
			}
			else if( !moveItemStackTo( itemStack, 0, indexHotbar, false ) )
				return ItemStack.EMPTY;
			if( itemStack.getCount() > 0 )
				( (CrateStackHandler)inventoryCrate ).addItems( itemStack );

			if( itemStack.isEmpty() )
				slot.set( ItemStack.EMPTY );
			else
				slot.setChanged();
		}

		return returnStack;
	}

	@Override
	public void removed( Player playerIn )
	{
		super.removed( playerIn );
		( (CrateStackHandler)inventoryCrate ).consolidateStacks();
		tileCrate.setChanged();
	}

	@Override
	protected boolean moveItemStackTo( ItemStack stack, int startIndex, int endIndex, boolean reverseDirection )
	{
		boolean flag = super.moveItemStackTo( stack, startIndex, endIndex, reverseDirection );
		if( !flag && endIndex <= indexHotbar )
			flag = ( (CrateStackHandler)inventoryCrate ).moveItemStackTo( stack, 0, inventoryCrate.getSlots() ) == 0;
		return flag;
	}

	@Override
	public boolean stillValid( Player playerIn )
	{
		return stillValid( ContainerLevelAccess.create( tileCrate.getLevel(), tileCrate.getBlockPos() ), playerIn, BetterStorageBlocks.CRATE.get() );
	}
}
