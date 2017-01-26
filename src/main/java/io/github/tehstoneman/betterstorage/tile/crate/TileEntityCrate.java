package io.github.tehstoneman.betterstorage.tile.crate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import io.github.tehstoneman.betterstorage.api.crate.ICrateStorage;
import io.github.tehstoneman.betterstorage.api.crate.ICrateWatcher;
import io.github.tehstoneman.betterstorage.config.GlobalConfig;
import io.github.tehstoneman.betterstorage.container.ContainerBetterStorage;
import io.github.tehstoneman.betterstorage.container.ContainerCrate;
import io.github.tehstoneman.betterstorage.content.BetterStorageTiles;
import io.github.tehstoneman.betterstorage.inventory.InventoryCratePlayerView;
import io.github.tehstoneman.betterstorage.misc.Constants;
import io.github.tehstoneman.betterstorage.misc.ItemIdentifier;
import io.github.tehstoneman.betterstorage.tile.entity.TileEntityContainer;
import io.github.tehstoneman.betterstorage.utils.WorldUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class TileEntityCrate extends TileEntityContainer implements IInventory, ICrateStorage, ICrateWatcher
{

	private static final EnumFacing[]	sideDirections	= { EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST };

	public static final int				slotsPerCrate	= 18;

	private CratePileData				data;
	/** Crate pile id of this crate, used only for saving/loading. */
	private int							id				= -1;
	private int							numCrates		= 1;

	public int getID()
	{
		return id;
	}

	/** Get the pile data for this tile entity. */
	public CratePileData getPileData()
	{
		if( worldObj.isRemote )
			throw new IllegalStateException( "Can't be called client-side." );
		if( data == null )
		{
			final CratePileCollection collection = CratePileCollection.getCollection( worldObj );
			if( id == -1 )
				setPileData( collection.createCratePile(), true );
			else
				setPileData( collection.getCratePile( id ), false );
		}
		numCrates = data.getNumCrates();
		return data;
	}

	/**
	 * Sets the pile data and adds the crate to it if desired. <br>
	 * Removes the crate from the old pile data if it had one.
	 */
	private void setPileData( CratePileData data, boolean addCrate )
	{
		if( this.data != null )
			this.data.removeCrate( this );
		this.data = data;
		if( data != null )
		{
			id = data.id;
			markForUpdate();
			if( addCrate )
				data.addCrate( this );
			numCrates = this.data.getNumCrates();
		}
		else
		{
			id = -1;
			numCrates = 1;
		}
		worldObj.notifyBlockUpdate( pos, worldObj.getBlockState( pos ), worldObj.getBlockState( pos ), 3 );
	}

	/**
	 * Destroys all crates above, and makes sure when piles split,
	 * each pile gets their own CratePileData object.
	 */
	private void checkPileConnections( CratePileData data )
	{
		final int x = pos.getX(), y = pos.getY(), z = pos.getZ();

		// Destroy all crates above.
		final TileEntityCrate crateAbove = WorldUtils.get( worldObj, x, y + 1, z, TileEntityCrate.class );
		if( crateAbove != null && crateAbove.data == data )
		{
			worldObj.setBlockToAir( new BlockPos( x, y + 1, z ) );
			crateAbove.dropItem( new ItemStack( BetterStorageTiles.crate ) );
		}

		// If there's still some crates left and this is a
		// base crate, see which crates are still connected.
		if( data.getNumCrates() <= 0 || y != data.getRegion().minY )
			return;

		// If there's more than one crate set, they need to split.
		final List< HashSet< TileEntityCrate > > crateSets = getCrateSets( x, y, z, data );
		if( crateSets.size() <= 1 )
			return;

		// The first crate set will keep the original pile data.
		// All other sets will get new pile data objects.
		for( int i = 1; i < crateSets.size(); i++ )
		{
			final HashSet< TileEntityCrate > set = crateSets.get( i );
			final CratePileData newPileData = data.collection.createCratePile();
			int numCrates = set.size();
			// Add the base crates from the set.
			for( TileEntityCrate newPileCrate : set )
			{
				newPileCrate.setPileData( newPileData, true );
				// Add all crates above the base crate.
				while( true )
				{
					newPileCrate = WorldUtils.get( worldObj, newPileCrate.pos.getX(), newPileCrate.pos.getY() + 1, newPileCrate.pos.getZ(),
							TileEntityCrate.class );
					if( newPileCrate == null )
						break;
					newPileCrate.setPileData( newPileData, true );
					numCrates++;
				}
			}
			// Move some of the items over to the new crate pile.
			final int count = numCrates * data.getOccupiedSlots() / ( data.getNumCrates() + numCrates );
			for( final ItemStack stack : data.getContents().getRandomStacks( count ) )
			{
				data.removeItems( stack );
				newPileData.addItems( stack );
			}
		}

		// Trim the original map to the size it actually is.
		// This is needed because the crates may not be removed in
		// order, from outside to inside.
		data.trimMap();
	}

	private List< HashSet< TileEntityCrate > > getCrateSets( int x, int y, int z, CratePileData data )
	{
		final List< HashSet< TileEntityCrate > > crateSets = new ArrayList<>();
		int checkedCrates = 0;

		neighborLoop: // Suck it :P
		for( final EnumFacing dir : sideDirections )
		{
			final int nx = x + dir.getFrontOffsetX();
			final int nz = z + dir.getFrontOffsetZ();

			// Continue if this neighbor block is not part of the crate pile.
			final TileEntityCrate neighborCrate = WorldUtils.get( worldObj, nx, y, nz, TileEntityCrate.class );
			if( neighborCrate == null || neighborCrate.data != data )
				continue;

			// See if the neighbor crate is already in a crate set,
			// in that case continue with the next neighbor block.
			for( final HashSet< TileEntityCrate > set : crateSets )
				if( set.contains( neighborCrate ) )
					continue neighborLoop;

			// Create a new set of crates and fill it with all connecting crates.
			final HashSet< TileEntityCrate > set = new HashSet<>();
			set.add( neighborCrate );
			for( final EnumFacing ndir : sideDirections )
				checkConnections( nx + ndir.getFrontOffsetX(), y, nz + ndir.getFrontOffsetZ(), data, set );
			crateSets.add( set );

			// If we checked all crates, stop the loop.
			checkedCrates += set.size();
			if( checkedCrates >= data.getNumCrates() )
				break;
		}

		return crateSets;
	}

	private void checkConnections( int x, int y, int z, CratePileData data, HashSet< TileEntityCrate > set )
	{
		final TileEntityCrate crate = WorldUtils.get( worldObj, x, y, z, TileEntityCrate.class );
		if( crate == null || data != crate.data || set.contains( crate ) )
			return;
		set.add( crate );
		for( final EnumFacing ndir : sideDirections )
			checkConnections( x + ndir.getFrontOffsetX(), y, z + ndir.getFrontOffsetZ(), data, set );
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();
		if( worldObj.isRemote || data != null )
			return;
		if( !isInvalid() )
			getPileData();
	}

	public void attemptConnect( EnumFacing side )
	{
		if( worldObj.isRemote || side == EnumFacing.UP )
			return;
		final BlockPos neighbourPos = pos.add( side.getDirectionVec() );

		final TileEntity tileEntity = worldObj.getTileEntity( neighbourPos );
		if( tileEntity instanceof TileEntityCrate )
		{
			final TileEntityCrate crateClicked = (TileEntityCrate)tileEntity;

			final CratePileData pileData = crateClicked.getPileData();
			if( pileData.canAdd( this ) )
				setPileData( pileData, true );
		}
	}

	@Override
	public void invalidate()
	{
		super.invalidate();
		if( worldObj.isRemote )
			return;
		final CratePileData data = getPileData();
		if( watcherRegistered )
			data.removeWatcher( this );
		setPileData( null, false );
		dropOverflowContents( data );
		checkPileConnections( data );
	}

	/** Drops a single item from the (destroyed) crate. */
	private void dropItem( ItemStack stack )
	{
		WorldUtils.dropStackFromBlock( worldObj, pos.getX(), pos.getY(), pos.getZ(), stack );
	}

	/** Drops multiple item from the (destroyed) crate. */
	private void dropItems( List< ItemStack > stacks )
	{
		for( final ItemStack stack : stacks )
			dropItem( stack );
	}

	/**
	 * Drops contents that don't fit into the
	 * crate pile after a crate got destroyed.
	 */
	private void dropOverflowContents( CratePileData data )
	{
		final int amount = -data.getFreeSlots();
		if( amount <= 0 )
			return;
		final List< ItemStack > items = data.getContents().getRandomStacks( amount );
		for( final ItemStack stack : items )
			data.removeItems( stack );
		dropItems( items );
	}

	// TileEntityContainer stuff

	@Override
	protected int getSizeContents()
	{
		return 0;
	}

	@Override
	public String getName()
	{
		return Constants.containerCrate;
	}

	@Override
	public ContainerBetterStorage createContainer( EntityPlayer player )
	{
		return new ContainerCrate( player, new InventoryCratePlayerView( this ) );
	}

	// Comparator related

	private boolean watcherRegistered = false;

	@Override
	protected int getComparatorSignalStengthInternal()
	{
		if( worldObj.isRemote )
			return 0;
		final CratePileData data = getPileData();
		return data.getOccupiedSlots() > 0 ? 1 + data.getOccupiedSlots() * 14 / data.getCapacity() : 0;
	}

	@Override
	protected void markComparatorAccessed()
	{
		super.markComparatorAccessed();

		if( !watcherRegistered && !worldObj.isRemote )
		{
			getPileData().addWatcher( this );
			watcherRegistered = true;
		}
	}

	@Override
	protected void comparatorUpdateAndReset()
	{
		super.comparatorUpdateAndReset();
		if( watcherRegistered && !hasComparatorAccessed() )
		{
			getPileData().removeWatcher( this );
			watcherRegistered = false;
		}
	}

	@Override
	public void onCrateItemsModified( ItemStack stack )
	{
		markContentsChanged();
	}

	// IInventory implementation

	@Override
	public ITextComponent getDisplayName()
	{
		return new TextComponentString( getName() );
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public int getSizeInventory()
	{
		if( worldObj.isRemote )
			return numCrates * slotsPerCrate;
		if( !GlobalConfig.enableCrateInventoryInterfaceSetting.getValue() )
			return 0;
		return getPileData().blockView.getSizeInventory();
	}

	@Override
	public boolean isItemValidForSlot( int slot, ItemStack stack )
	{
		if( !GlobalConfig.enableCrateInventoryInterfaceSetting.getValue() )
			return false;
		return getPileData().blockView.isItemValidForSlot( slot, stack );
	}

	@Override
	public ItemStack getStackInSlot( int slot )
	{
		if( !GlobalConfig.enableCrateInventoryInterfaceSetting.getValue() )
			return null;
		return getPileData().blockView.getStackInSlot( slot );
	}

	@Override
	public void setInventorySlotContents( int slot, ItemStack stack )
	{
		if( GlobalConfig.enableCrateInventoryInterfaceSetting.getValue() )
		{
			getPileData().blockView.setInventorySlotContents( slot, stack );
			markDirty();
		}
	}

	@Override
	public ItemStack decrStackSize( int slot, int amount )
	{
		if( !GlobalConfig.enableCrateInventoryInterfaceSetting.getValue() )
			return null;
		markDirty();
		return getPileData().blockView.decrStackSize( slot, amount );
	}

	@Override
	public void markDirty()
	{
		if( GlobalConfig.enableCrateInventoryInterfaceSetting.getValue() )
			getPileData().blockView.markDirty();
	}

	@Override
	public boolean shouldRefresh( World world, BlockPos pos, IBlockState oldState, IBlockState newState )
	{
		return oldState.getBlock() != newState.getBlock();
	}

	@Override
	public boolean isUseableByPlayer( EntityPlayer player )
	{
		return false;
	}

	@Override
	public boolean hasCustomName()
	{
		return false;
	}

	@Override
	public void openInventory( EntityPlayer player )
	{
		getPileData().blockView.openInventory( player );
	}

	@Override
	public void closeInventory( EntityPlayer player )
	{
		getPileData().blockView.closeInventory( player );
	}

	// ICrateStorage implementation

	private static boolean isEnabled()
	{
		return GlobalConfig.enableCrateStorageInterfaceSetting.getValue();
	}

	@Override
	public Object getCrateIdentifier()
	{
		return getPileData();
	}

	@Override
	public int getCapacity()
	{
		return isEnabled() ? getPileData().getCapacity() : 0;
	}

	@Override
	public int getOccupiedSlots()
	{
		return isEnabled() ? getPileData().getOccupiedSlots() : 0;
	}

	@Override
	public int getUniqueItems()
	{
		return isEnabled() ? getPileData().getUniqueItems() : 0;
	}

	@Override
	public Iterable< ItemStack > getContents()
	{
		return isEnabled() ? getPileData().getContents().getItems() : Collections.EMPTY_LIST;
	}

	@Override
	public Iterable< ItemStack > getRandomStacks()
	{
		return isEnabled() ? getPileData().getContents().getRandomStacks() : Collections.EMPTY_LIST;
	}

	@Override
	public int getItemCount( ItemStack identifier )
	{
		return isEnabled() ? getPileData().getContents().get( new ItemIdentifier( identifier ) ) : 0;
	}

	@Override
	public int getSpaceForItem( ItemStack identifier )
	{
		return isEnabled() ? getPileData().getSpaceForItem( identifier ) : 0;
	}

	@Override
	public ItemStack insertItems( ItemStack stack )
	{
		return isEnabled() ? getPileData().addItems( stack ) : stack;
	}

	@Override
	public ItemStack extractItems( ItemStack identifier, int amount )
	{
		return isEnabled() ? getPileData().removeItems( new ItemIdentifier( identifier ), amount ) : null;
	}

	@Override
	public void registerCrateWatcher( ICrateWatcher watcher )
	{
		if( isEnabled() )
			getPileData().addWatcher( watcher );
	}

	@Override
	public void unregisterCrateWatcher( ICrateWatcher watcher )
	{
		if( isEnabled() )
			getPileData().removeWatcher( watcher );
	}

	// TileEntity synchronization

	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		final NBTTagCompound compound = getUpdateTag();
		return new SPacketUpdateTileEntity( pos, 0, compound );
	}

	@Override
	public void onDataPacket( NetworkManager net, SPacketUpdateTileEntity packet )
	{
		final NBTTagCompound compound = packet.getNbtCompound();
		handleUpdateTag( compound );
		// worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}

	@Override
	public NBTTagCompound getUpdateTag()
	{
		final NBTTagCompound compound = new NBTTagCompound();
		compound.setInteger( "crateId", id );
		compound.setInteger( "numCrates", numCrates );
		return compound;
	}

	@Override
	public void handleUpdateTag( NBTTagCompound compound )
	{
		id = compound.getInteger( "crateId" );
		numCrates = compound.getInteger( "numCrates" );
		worldObj.markBlockRangeForRenderUpdate( pos.add( -1, -1, -1 ), pos.add( 1, 1, 1 ) );
	}

	// Reading from / writing to NBT

	@Override
	public void readFromNBT( NBTTagCompound compound )
	{
		super.readFromNBT( compound );
		id = compound.getInteger( "crateId" );
	}

	@Override
	public NBTTagCompound writeToNBT( NBTTagCompound compound )
	{
		super.writeToNBT( compound );
		compound.setInteger( "crateId", id );
		// TODO: This might not be the best place to save the crate data.
		getPileData().save();
		return compound;
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
}
