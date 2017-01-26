package io.github.tehstoneman.betterstorage.inventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import io.github.tehstoneman.betterstorage.api.crate.ICrateWatcher;
import io.github.tehstoneman.betterstorage.misc.Constants;
import io.github.tehstoneman.betterstorage.misc.ItemIdentifier;
import io.github.tehstoneman.betterstorage.tile.crate.CratePileData;
import io.github.tehstoneman.betterstorage.tile.crate.TileEntityCrate;
import io.github.tehstoneman.betterstorage.utils.StackUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

/** An inventory interface built for players accessing crate piles. */
public class InventoryCratePlayerView extends InventoryBetterStorage implements ICrateWatcher
{
	private static final int inventoryMaxSize = 9 * 6;

	private static class MapData
	{
		public int itemCount = 0;
	}

	public final CratePileData						data;
	public final TileEntityCrate					crate;

	private final ItemStack[]						tempContents;
	private final Map< ItemIdentifier, MapData >	countData			= new HashMap< >();

	private boolean									ignoreModifiedItems	= false;

	public InventoryCratePlayerView( TileEntityCrate crate )
	{
		super( Constants.containerCrate );
		data = crate.getPileData();
		this.crate = crate;

		final int size = Math.min( data.getCapacity(), inventoryMaxSize );
		tempContents = new ItemStack[size];

		// Fill temp contents with some random items from the crate pile.
		final List< Integer > slotAccess = new ArrayList< >( size );
		for( int i = 0; i < size; i++ )
			slotAccess.add( i );
		if( data.getOccupiedSlots() < size )
			Collections.shuffle( slotAccess );

		final Iterator< ItemStack > iter = data.getContents().getRandomStacks().iterator();
		for( int slot = 0; slot < size && iter.hasNext(); slot++ )
		{
			final ItemStack stack = iter.next();
			getMapData( stack ).itemCount += stack.stackSize;
			tempContents[slotAccess.get( slot )] = stack;
		}
	}

	// Map data related functions

	private MapData getMapData( ItemIdentifier item )
	{
		MapData data = countData.get( item );
		if( data != null )
			return data;
		data = new MapData();
		countData.put( item, data );
		return data;
	}

	private MapData getMapData( ItemStack item )
	{
		return getMapData( new ItemIdentifier( item ) );
	}

	// IInventory implementation

	@Override
	public int getSizeInventory()
	{
		return tempContents.length;
	}

	@Override
	public ItemStack getStackInSlot( int slot )
	{
		if( slot < 0 || slot >= getSizeInventory() )
			return null;
		return tempContents[slot];
	}

	@Override
	public void setInventorySlotContents( int slot, ItemStack stack )
	{
		if( slot < 0 || slot >= getSizeInventory() )
			return;
		final ItemStack oldStack = getStackInSlot( slot );
		ignoreModifiedItems = true;
		if( oldStack != null )
		{
			final ItemIdentifier item = new ItemIdentifier( oldStack );
			getMapData( item ).itemCount -= oldStack.stackSize;
			data.removeItems( item, oldStack.stackSize );
		}
		if( stack != null )
		{
			final int amount = Math.min( stack.stackSize, Math.min( data.getSpaceForItem( stack ), stack.getMaxStackSize() ) );
			if( amount <= 0 )
				return;
			stack = StackUtils.copyStack( stack.copy(), amount );
			getMapData( stack ).itemCount += amount;
			data.addItems( stack );
		}
		ignoreModifiedItems = false;
		tempContents[slot] = stack;
		if( stack == null )
			onSlotEmptied( slot );
	}

	@Override
	public ItemStack decrStackSize( int slot, int amount )
	{
		final ItemStack stack = getStackInSlot( slot );
		if( stack == null )
			return null;
		amount = Math.min( amount, stack.stackSize );

		final ItemIdentifier item = new ItemIdentifier( stack );
		getMapData( item ).itemCount -= amount;

		stack.stackSize -= amount;
		if( stack.stackSize <= 0 )
			tempContents[slot] = null;

		ignoreModifiedItems = true;
		final ItemStack result = data.removeItems( item, amount );
		ignoreModifiedItems = false;

		if( tempContents[slot] == null )
			onSlotEmptied( slot );

		return result;
	}

	@Override
	public boolean isUseableByPlayer( EntityPlayer player )
	{
		final int x = crate.getPos().getX();
		final int y = crate.getPos().getY();
		final int z = crate.getPos().getZ();
		return player.worldObj.getTileEntity( crate.getPos() ) == crate && player.getDistanceSq( x + 0.5, y + 0.5, z + 0.5 ) < 64.0
				&& getSizeInventory() <= data.getCapacity();
	}

	@Override
	public void markDirty()
	{}

	@Override
	public void openInventory( EntityPlayer player )
	{
		data.addWatcher( this );
	}

	@Override
	public void closeInventory( EntityPlayer player )
	{
		data.removeWatcher( this );
	}

	// ICrateWatcher implementation

	@Override
	public void onCrateItemsModified( ItemStack changed )
	{
		if( ignoreModifiedItems )
			return;

		final ItemIdentifier item = new ItemIdentifier( changed );
		int amount = changed.stackSize;

		final MapData itemData = getMapData( item );
		final Queue< Integer > emptySlots = new LinkedList< >();

		for( int slot = 0; slot < tempContents.length; slot++ )
		{
			final ItemStack stack = tempContents[slot];
			if( stack == null )
			{
				emptySlots.add( slot );
				continue;
			}
			if( !item.matches( stack ) )
				continue;
			amount -= modifyItemsInSlot( slot, stack, itemData, amount );
			if( amount == 0 )
				return;
		}

		while( amount > 0 && emptySlots.size() > 0 )
			amount -= setItemsInSlot( emptySlots.poll(), item, itemData, amount );
	}

	// Misc functions

	/**
	 * Returns if at least 1 item from the stack could be
	 * stored in the player view, but doesn't change anything.
	 */
	public boolean canFitSome( ItemStack item )
	{
		for( final ItemStack stack : tempContents )
			if( stack == null || StackUtils.matches( stack, item ) && stack.stackSize < stack.getMaxStackSize() )
				return true;
		return false;
	}

	private void onSlotEmptied( int slot )
	{
		int emptySlots = 0;
		for( final ItemStack tempContent : tempContents )
			if( tempContent == null )
				emptySlots++;

		if( emptySlots <= data.getFreeSlots() )
			return;

		for( final ItemStack stack : data.getContents().getItems() )
		{
			final ItemIdentifier item = new ItemIdentifier( stack );
			final MapData itemData = getMapData( item );

			final int count = stack.stackSize - itemData.itemCount;
			if( count <= 0 )
				continue;
			setItemsInSlot( slot, item, itemData, count );
		}
	}

	private int modifyItemsInSlot( int slot, ItemStack stack, MapData itemData, int amount )
	{
		final int count = Math.max( -stack.stackSize, Math.min( amount, stack.getMaxStackSize() - stack.stackSize ) );
		if( ( stack.stackSize += count ) <= 0 )
			tempContents[slot] = null;
		itemData.itemCount += count;
		return count;
	}

	private int setItemsInSlot( int slot, ItemIdentifier item, MapData itemData, int maxAmount )
	{
		final int size = Math.min( maxAmount, item.createStack( 1 ).getMaxStackSize() );
		tempContents[slot] = item.createStack( size );
		itemData.itemCount += size;
		return size;
	}

	@Override
	public ItemStack removeStackFromSlot( int index )
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getField( int id )
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setField( int id, int value )
	{
		// TODO Auto-generated method stub

	}

	@Override
	public int getFieldCount()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void clear()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public ITextComponent getDisplayName()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
