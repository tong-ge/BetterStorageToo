package io.github.tehstoneman.betterstorage.common.inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityCrate;
import io.github.tehstoneman.betterstorage.common.world.CrateStackCollection;
import io.github.tehstoneman.betterstorage.config.GlobalConfig;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

public class CrateStackHandler extends ItemStackHandler
{
	private static final int	maxCratePileSize	= 125;

	private int					numCrates;
	private Region				region;
	private UUID				pileID;
	private List< Integer >		indexSlots;
	private boolean				doShuffle			= true;

	private TileEntityCrate		crateToUpdate;

	public CrateStackHandler( int size )
	{
		super( size );
		indexSlots = getShuffledIndexes( size );
	}

	/** Returns the number of crates attached. */
	public int getNumCrates()
	{
		return numCrates;
	}

	/** Returns the maximum number of slots for this inventory. */
	public int getCapacity()
	{
		return getCapacity( numCrates );
	}

	/** Returns the maximum number of slots for the given number of crates. */
	public static int getCapacity( int numCrates )
	{
		return numCrates * ( 18 + Math.min( numCrates / 6, 18 ) );
	}

	/** Returns the region / bounds this crate pile takes up. */
	public Region getRegion()
	{
		return region;
	}

	/** Returns the number of occupied slots */
	public int getOccupiedSlots()
	{
		int count = 0;
		for( final ItemStack stack : stacks )
			if( stack != null )
				count++;
		return count;
	}

	/**
	 * Returns a shuffled list of slot indexes<br>
	 * Tries to fill list with only occupied slots
	 */
	public List< Integer > getShuffledIndexes( int count )
	{
		final List< Integer > list = new ArrayList<>();
		final int occupied = getOccupiedSlots();
		for( int i = 0; i < Math.max( count, occupied ); i++ )
			list.add( i );
		Collections.shuffle( list );
		return list.subList( 0, count );
	}

	/** Returns a slot pointed to by a randomized index */
	public int getIndexedSlot( int slot )
	{
		return indexSlots.get( slot );
	}

	private static boolean isEnabled()
	{
		return GlobalConfig.enableCrateStorageInterfaceSetting.getValue();
	}

	/** Tries to fit contents into full stacks, and moves empty slots to the end of the inventory */
	public void consolidateStacks()
	{
		for( int i = 1; i < stacks.length; i++ )
			if( stacks[i] != null )
				if( mergeItemStack( stacks[i], 0, i ) == 0 )
					stacks[i] = null;
	}

	/** Merges partial stacks into each other */
	public int mergeItemStack( ItemStack stack, int startIndex, int endIndex )
	{
		int i = startIndex;

		if( stack.isStackable() )
			while( stack.stackSize > 0 && i < endIndex )
			{
				final ItemStack itemstack = stacks[i];

				if( itemstack == null )
				{
					stacks[i] = stack.copy();
					stack.stackSize = 0;
					break;
				}
				else
					if( ItemStack.areItemsEqual( stack, itemstack ) )
					{
						final int newSize = itemstack.stackSize + stack.stackSize;

						if( newSize <= stack.getMaxStackSize() )
						{
							stack.stackSize = 0;
							itemstack.stackSize = newSize;
						}
						else
							if( itemstack.stackSize < stack.getMaxStackSize() )
							{
								stack.stackSize -= stack.getMaxStackSize() - itemstack.stackSize;
								itemstack.stackSize = stack.getMaxStackSize();
							}
					}

				++i;
			}
		return stack.stackSize;
	}

	/** Returns if the crate can be added to the crate pile. */
	public boolean canAdd( TileEntityCrate crate )
	{
		return numCrates < maxCratePileSize && ( region.contains( crate.getPos() ) || canExpand( crate ) );
	}

	/** Returns if the crate can expand the crate pile. */
	private boolean canExpand( TileEntityCrate crate )
	{
		// Can't expand if there's not enough crates in the bounding box.
		final int volume = region.volume();
		if( numCrates < volume - 1 )
			return false;

		final int width = region.width();
		final int height = region.height();
		final int depth = region.depth();

		if( crate.getPos().getX() < region.posMin.getX() || crate.getPos().getX() > region.posMax.getX() )
		{
			if( width >= maxCratePileSize )
				return false;
			final int maxDiff = height == 1 ? 1 : 3;
			if( width >= maxDiff + Math.min( height, depth ) )
				return false;
		}
		else
			if( crate.getPos().getZ() < region.posMin.getZ() || crate.getPos().getZ() > region.posMax.getZ() )
			{
				if( depth >= maxCratePileSize )
					return false;
				final int maxDiff = width == 1 ? 1 : 3;
				if( depth >= maxDiff + Math.min( height, depth ) )
					return false;
			}
			else
				if( crate.getPos().getY() < region.posMin.getY() || crate.getPos().getY() > region.posMax.getY() )
				{
					if( height >= maxCratePileSize )
						return false;
					final int maxDiff = width == 1 || depth == 1 ? 1 : 4;
					if( height >= maxDiff + Math.min( width, depth ) )
						return false;
				}

		return true;
	}

	/**
	 * Adds a crate to the stack handler, increasing the number
	 * of crates and adding it to the crate collection.
	 */
	public void addCrate( TileEntityCrate crate )
	{
		if( numCrates == 0 || region == null )
			region = new Region( crate );
		else
			region.expandToContain( crate );
		numCrates++;
		stacks = Arrays.copyOf( stacks, this.getCapacity() );
		crate.setPileID( getPileID() );
		// setSize( getCapacity() );
	}

	/**
	 * Removes a crate from the crate pile, decreasing the number
	 * of crates and removing it from the crate pile map.
	 *
	 * @return Overflow contents
	 */
	public ItemStack[] removeCrate( TileEntityCrate crate )
	{
		if( numCrates > 1 )
		{
			final ItemStack[] overflow = Arrays.copyOfRange( stacks, CrateStackHandler.getCapacity( numCrates - 1 ), stacks.length );
			setNumCrates( numCrates - 1 );
			CrateStackCollection.getCollection( crate.getWorld() ).markDirty();
			return overflow;
		}
		else
		{
			CrateStackCollection.getCollection( crate.getWorld() ).removeCratePile( pileID );
			return stacks;
		}
	}

	/** Sets the region of this handler */
	public void setRegion( Region region )
	{
		this.region = region;
	}

	/** Sets the number of crates used by this hander */
	public void setNumCrates( int numCrates )
	{
		this.numCrates = numCrates;
		stacks = Arrays.copyOf( stacks, this.getCapacity() );
	}

	/** Returns the ID of this handler */
	public UUID getPileID()
	{
		return pileID;
	}

	/** Sets the ID used by this handler */
	public void setPileID( UUID pileID )
	{
		this.pileID = pileID;
	}

	/** Returns a randomized copy of the inventory */
	public ItemStack[] getRandomStacks( int count )
	{
		final List< ItemStack > items = Arrays.asList( stacks );
		Collections.shuffle( items );
		return (ItemStack[])Arrays.copyOf( items.toArray(), count );
	}

	/**
	 * Removes and returns a specific amount of items. <br>
	 * Returns less than the requested amount when there's
	 * not enough, or null if there's none at all.
	 */
	public void removeItems( ItemStack itemStack )
	{
		int i = 0;
		while( i < stacks.length && !ItemStack.areItemStacksEqual( stacks[i], itemStack ) )
			i++;

		if( i < stacks.length && stacks[i] != null )
			stacks[i] = null;
	}

	/**
	 * Tries to add a stack to the contents. <br>
	 * Returns what could not be added, null if there was no overflow.
	 */
	public void addItems( ItemStack stack )
	{
		int i = 0;
		while( i < stacks.length && stacks[i] != null )
			i++;

		if( i < stacks.length && stacks[i] == null )
			stacks[i] = stack.copy();
	}

	/**
	 * Trims the bounding box to the actual size of the crate pile. <br>
	 * This is needed when crates are not removed in order, for
	 * example when they're split.
	 */
	public void trimRegion( World world )
	{
		Region region = null;
		for( final BlockPos pos : BlockPos.getAllInBox( this.region.posMin, this.region.posMax ) )
		{
			final TileEntity tileEntity = world.getTileEntity( pos );
			if( tileEntity instanceof TileEntityCrate )
			{
				final TileEntityCrate crate = (TileEntityCrate)tileEntity;
				if( crate.getPileID().equals( pileID ) )
					if( region == null )
						region = new Region( pos, pos );
					else
						region.expandToContain( pos );
			}
		}
		if( region != null )
			this.region = region;
	}

	@Override
	public void setSize( int size )
	{
		stacks = Arrays.copyOf( stacks, size );
		indexSlots = getShuffledIndexes( size );
	}

	@Override
	public void setStackInSlot( int slot, ItemStack stack )
	{
		super.setStackInSlot( getIndexedSlot( slot ), stack );
	}

	/** Bypass the randomization of the overrided version of this function */
	public void setStackInSlotFixed( int slot, ItemStack stack )
	{
		doShuffle = false;
		super.setStackInSlot( slot, stack );
		doShuffle = true;
	}

	@Override
	public ItemStack getStackInSlot( int slot )
	{
		return super.getStackInSlot( getIndexedSlot( slot ) );
	}

	/** Bypass the randomization of the overrided version of this function */
	public ItemStack getStackInSlotFixed( int slot )
	{
		return super.getStackInSlot( slot );
	}

	@Override
	public ItemStack extractItem( int slot, int amount, boolean simulate )
	{
		return super.extractItem( getIndexedSlot( slot ), amount, simulate );
	}

	/** Bypass the randomization of the overrided version of this function */
	public ItemStack extractItemFixed( int slot, int amount, boolean simulate )
	{
		doShuffle = false;
		final ItemStack result = super.extractItem( slot, amount, simulate );
		doShuffle = true;
		return result;
	}

	@Override
	public ItemStack insertItem( int slot, ItemStack stack, boolean simulate )
	{
		return super.insertItem( getIndexedSlot( slot ), stack, simulate );
	}

	/** Bypass the randomization of the overrided version of this function */
	public ItemStack insertItemFixed( int slot, ItemStack stack, boolean simulate )
	{
		doShuffle = false;
		final ItemStack result = super.insertItem( slot, stack, simulate );
		doShuffle = true;
		return result;
	}

	/** Records a tileentity to notify if contents has changed */
	public void sendUpdatesTo( TileEntityCrate tileEntityCrate )
	{
		crateToUpdate = tileEntityCrate;
	}

	@Override
	protected void onContentsChanged( int slot )
	{
		if( doShuffle )
		{
			consolidateStacks();
			indexSlots = getShuffledIndexes( stacks.length );
		}
		if( crateToUpdate != null )
			crateToUpdate.markDirty();
	}

	@Override
	protected void onLoad()
	{
		indexSlots = getShuffledIndexes( stacks.length );
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		final NBTTagCompound compound = super.serializeNBT();

		compound.setInteger( "NumCrates", numCrates );
		if( pileID != null )
			compound.setUniqueId( "PileID", pileID );

		if( region != null )
			compound.setTag( "Region", region.toCompound() );
		return compound;
	}

	@Override
	public void deserializeNBT( NBTTagCompound compound )
	{
		super.deserializeNBT( compound );

		numCrates = compound.getInteger( "NumCrates" );
		if( compound.hasUniqueId( "PileID" ) )
			pileID = compound.getUniqueId( "PileID" );

		if( compound.hasKey( "Region" ) )
			region = Region.fromCompound( compound.getCompoundTag( "Region" ) );
		onLoad();
	}
}
