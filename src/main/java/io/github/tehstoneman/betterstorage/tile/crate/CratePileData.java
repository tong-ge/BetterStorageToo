package io.github.tehstoneman.betterstorage.tile.crate;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.api.crate.ICrateWatcher;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityCrate;
import io.github.tehstoneman.betterstorage.common.world.CrateStackCollection;
import io.github.tehstoneman.betterstorage.config.GlobalConfig;
import io.github.tehstoneman.betterstorage.inventory.InventoryCrateBlockView;
import io.github.tehstoneman.betterstorage.misc.ItemIdentifier;
import io.github.tehstoneman.betterstorage.misc.Region;
import io.github.tehstoneman.betterstorage.utils.StackUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.common.util.Constants.NBT;

/**
 * Holds data for a single crate pile, a multi-block
 * structure made from individual crate blocks
 */
public class CratePileData extends WorldSavedData
{

	private static final int				maxCratePileSize	= 125;

	public final CrateStackCollection		collection;
	public final UUID						pileID;

	private final CrateItems				contents			= new CrateItems();

	private int								numCrates			= 0;
	private boolean							destroyed			= false;
	private boolean							dirty				= false;
	private CratePileMap					map;
	private AxisAlignedBB					bounding;

	private final Set< ICrateWatcher >		watchers			= new HashSet<>();

	/** An inventory interface built for machines accessing the crate pile. */
	public final InventoryCrateBlockView	blockView			= new InventoryCrateBlockView( this );

	public CratePileData( CrateStackCollection collection, UUID id, int numCrates )
	{
		super("name");
		this.collection = collection;
		this.pileID = id;
		this.numCrates = numCrates;
	}

	/** Returns the items in this crate pile. */
	public CrateItems getContents()
	{
		return contents;
	}

	/** Returns the number of crates attached. */
	public int getNumCrates()
	{
		return numCrates;
	}

	/** Returns the maximum number of slots. */
	public int getCapacity()
	{
		return numCrates * ( 18 + Math.min( numCrates / 6, 18 ) );
	}

	/** Returns the number of unique items. */
	public int getUniqueItems()
	{
		return contents.getUniqueItems();
	}

	/** Returns the number of slots occupied. */
	public int getOccupiedSlots()
	{
		return contents.getTotalStacks();
	}

	/** Returns the number of slots free. Negative if there's any overflow. */
	public int getFreeSlots()
	{
		return getCapacity() - getOccupiedSlots();
	}

	/** Returns the region / bounds this crate pile takes up. */
	public Region getRegion()
	{
		return map.region;
	}

	public int getCenterX()
	{
		return ( map.region.minX + map.region.maxX ) / 2;
	}

	public int getCenterY()
	{
		return ( map.region.minY + map.region.maxY ) / 2;
	}

	public int getCenterZ()
	{
		return ( map.region.minZ + map.region.maxZ ) / 2;
	}

	// Saving related

	/** Returns if the crate pile is marked as dirty. */
	public boolean isDirty()
	{
		return dirty;
	}

	/** Marks the crate pile as dirty. */
	public void markDirty()
	{
		if( isDirty() || destroyed )
			return;
		dirty = true;

		if( BetterStorage.globalConfig.getBoolean( GlobalConfig.crateDebugMessages ) )
			BetterStorage.log
					.info( String.format( "[CRATE DEBUG] Marked crate pile at [%s,%s,%s] as dirty.", getCenterX(), getCenterY(), getCenterZ() ) );
	}

	/** Saves the crate pile to disk if it's been marked as dirty. */
	public void save()
	{
		if( !isDirty() )
			return;
		//collection.save( this );
		dirty = false;

		if( BetterStorage.globalConfig.getBoolean( GlobalConfig.crateDebugMessages ) )
			BetterStorage.log.info( String.format( "[CRATE DEBUG] Saved crate pile at [%s,%s,%s].", getCenterX(), getCenterY(), getCenterZ() ) );
	}

	/** Removes this (empty) crate pile from the collection. */
	public void remove()
	{
		destroyed = true;
		collection.removeCratePile( this );

		if( BetterStorage.globalConfig.getBoolean( GlobalConfig.crateDebugMessages ) )
			BetterStorage.log
					.info( String.format( "[CRATE DEBUG] Removed empty crate pile at [%s,%s,%s].", getCenterX(), getCenterY(), getCenterZ() ) );
	}

	// CrateMap related functions

	/** Returns if the crate can be added to the crate pile. */
	public boolean canAdd( TileEntityCrate crate )
	{
		Logger.getLogger( ModInfo.modId ).info( "bounding : " + bounding );
		// return map != null && numCrates < maxCratePileSize && ( map.region.contains( crate ) || canExpand( crate ) ) && ( map.get( crate.getPos().getX(),
		// crate.getPos().getY() - 1, crate.getPos().getZ() ) || crate.getPos().getY() == map.region.minY );
		return numCrates < maxCratePileSize
				&& ( bounding.isVecInside( new Vec3d( crate.getPos() ).addVector( 0.5, 0.5, 0.5 ) ) || canExpand( crate ) );
	}

	/** Returns if the crate can expand the crate pile. */
	private boolean canExpand( TileEntityCrate crate )
	{
		final int volume = getVolume();
		// Can't expand if there's not enough crates in the bounding box.
		if( numCrates < Math.min( (int)( volume * 0.8 ), volume - 5 ) )
			return false;
		
		int width = getWidth();
		int height = getHeight();
		int depth = getDepth();

		if( crate.getPos().getX() < bounding.minX || crate.getPos().getX() > bounding.maxX )
		{
			final int maxDiff = height == 1 ? 1 : 3;
			if( width >= maxDiff + Math.min( height, depth ) )
				return false;
		}
		else
			if( crate.getPos().getZ() < bounding.minZ || crate.getPos().getZ() > bounding.maxZ )
			{
				final int maxDiff = width == 1 ? 1 : 3;
				if( height >= maxDiff + Math.min( width, depth ) )
					return false;
			}
			else
				if( crate.getPos().getY() < bounding.minY || crate.getPos().getY() > bounding.maxY )
				{
					final int maxDiff = width == 1 || height == 1 ? 1 : 4;
					if( depth >= maxDiff + Math.min( width, height ) )
						return false;
				}

		return true;
	}

	public int getWidth()
	{
		return (int)( bounding.maxX - bounding.minX );
	}

	public int getDepth()
	{
		return (int)( bounding.maxY - bounding.minY );
	}

	public int getHeight()
	{
		return (int)( bounding.maxZ - bounding.minZ );
	}

	public int getVolume()
	{
		return getWidth() * getDepth() * getHeight();
	}

	public void trimMap()
	{
		if( map != null )
			map.trim();
	}

	// Adding and removing crates

	/**
	 * Adds a crate to the crate pile, increasing the number
	 * of crates and adding it to the crate pile map.
	 */
	public void addCrate( TileEntityCrate crate )
	{
		if( numCrates == 0 )
			map = new CratePileMap( crate );
		map.add( crate );
		numCrates++;
		markDirty();
	}

	/**
	 * Removes a crate from the crate pile, decreasing the number
	 * of crates and removing it from the crate pile map.
	 */
	public void removeCrate( TileEntityCrate crate )
	{
		if( --numCrates > 0 )
			if( map != null )
			{
				map.remove( crate );
				markDirty();
			}
			else
				remove();
	}

	/** Returns if there's a crate from the crate pile at that position. */

	public boolean hasCrate( int x, int y, int z )
	{
		return map.get( x, y, z );
	}

	// Adding items

	/**
	 * Tries to add a stack to the contents. <br>
	 * Returns what could not be added, null if there was no overflow.
	 */
	public ItemStack addItems( ItemStack stack )
	{
		if( stack == null )
			return null;
		ItemStack overflow = null;

		final int space = getSpaceForItem( stack );
		if( space > 0 )
		{

			if( space < stack.stackSize )
				overflow = stack.splitStack( stack.stackSize - space );

			final ItemIdentifier item = new ItemIdentifier( stack );
			getContents().set( item, getContents().get( item ) + stack.stackSize );

			for( final ICrateWatcher watcher : watchers )
				watcher.onCrateItemsModified( stack );

		}
		else
			overflow = stack;

		markDirty();
		return overflow;
	}

	// Removing items

	/**
	 * Removes and returns a specific amount of items. <br>
	 * Returns less than the requested amount when there's
	 * not enough, or null if there's none at all.
	 */
	public ItemStack removeItems( ItemIdentifier item, int amount )
	{
		final int currentAmount = getContents().get( item );
		amount = Math.min( amount, currentAmount );
		if( amount <= 0 )
			return null;

		getContents().set( item, currentAmount - amount );

		final ItemStack removedStack = item.createStack( -amount );
		for( final ICrateWatcher watcher : watchers )
			watcher.onCrateItemsModified( removedStack );

		markDirty();
		return item.createStack( amount );
	}

	/**
	 * Removes and returns a specific amount of items. <br>
	 * Returns less than the requested amount when there's
	 * not enough, or null if there's none at all.
	 */
	public ItemStack removeItems( ItemStack stack )
	{
		return removeItems( new ItemIdentifier( stack ), stack.stackSize );
	}

	// Checking space

	/** Returns how much space there is left for a specific item. */
	public int getSpaceForItem( ItemIdentifier item )
	{
		if( item == null )
			return 0;
		final int amount = getContents().get( item );
		final ItemStack testStack = item.createStack( amount );

		final int maxStackSize = testStack.getMaxStackSize();
		int space = getFreeSlots() * maxStackSize;
		if( amount > 0 )
			space += StackUtils.calcNumStacks( testStack ) * maxStackSize - testStack.stackSize;
		return space;
	}

	/** Returns how much space there is left for a specific item. */
	public int getSpaceForItem( ItemStack item )
	{
		if( item == null )
			return 0;
		return getSpaceForItem( new ItemIdentifier( item ) );
	}

	// Crate watcher related functions

	/**
	 * Adds a crate watcher to the watchers list, so it
	 * gets informed about any added or removed items.
	 */
	public void addWatcher( ICrateWatcher watcher )
	{
		watchers.add( watcher );
	}

	/** Removes a crate watcher to the watchers list. */
	public void removeWatcher( ICrateWatcher watcher )
	{
		watchers.remove( watcher );
	}

	// NBT related functions

	public NBTTagCompound toCompound()
	{
		final NBTTagCompound compound = new NBTTagCompound();
		compound.setShort( "numCrates", (short)getNumCrates() );
		final NBTTagList stacks = new NBTTagList();

		for( final ItemStack stack : getContents().getItems() )
		{
			final NBTTagCompound stackCompound = new NBTTagCompound();
			stackCompound.setShort( "id", (short)Item.getIdFromItem( stack.getItem() ) );
			stackCompound.setInteger( "Count", stack.stackSize );
			stackCompound.setShort( "Damage", (short)StackUtils.getRealItemDamage( stack ) );
			if( stack.hasTagCompound() )
				stackCompound.setTag( "tag", stack.getTagCompound() );
			stacks.appendTag( stackCompound );
		}
		compound.setTag( "stacks", stacks );
		if( map != null )
			compound.setTag( "map", map.toCompound() );

		return compound;
	}

	public static CratePileData fromCompound( CrateStackCollection collection, UUID crateId, NBTTagCompound compound )
	{
		final int numCrates = compound.getShort( "numCrates" );
		final CratePileData pileData = new CratePileData( collection, crateId, numCrates );
		final NBTTagList stacks = compound.getTagList( "stacks", NBT.TAG_COMPOUND );
		for( int j = 0; j < stacks.tagCount(); j++ )
		{
			final NBTTagCompound stackCompound = stacks.getCompoundTagAt( j );
			final Item item = Item.getItemById( stackCompound.getShort( "id" ) );
			final int count = stackCompound.getInteger( "Count" );
			final int damage = stackCompound.getShort( "Damage" );
			final ItemStack stack = new ItemStack( item, count, damage );
			if( stackCompound.hasKey( "tag" ) )
				stack.setTagCompound( stackCompound.getCompoundTag( "tag" ) );
			if( stack.getItem() != null )
				pileData.getContents().set( new ItemIdentifier( stack ), stack.stackSize );
		}
		if( compound.hasKey( "map" ) )
			pileData.map = CratePileMap.fromCompound( compound.getCompoundTag( "map" ) );
		return pileData;
	}

	@Override
	public void readFromNBT( NBTTagCompound nbt )
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public NBTTagCompound writeToNBT( NBTTagCompound compound )
	{
		// TODO Auto-generated method stub
		return null;
	}
}
