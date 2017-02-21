package io.github.tehstoneman.betterstorage.common.tileentity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.common.block.BetterStorageBlocks;
import io.github.tehstoneman.betterstorage.common.inventory.CrateStackHandler;
import io.github.tehstoneman.betterstorage.common.world.CrateStackCollection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

//public class TileEntityCrate extends TileEntityContainer implements IInventory, ICrateStorage, ICrateWatcher
public class TileEntityCrate extends TileEntity
{
	public static final int	slotsPerCrate	= 18;
	public static final int	maxCrates		= 125;
	public static final int	maxPerSide		= 5;

	private UUID			pileID;
	private String			customTitle;

	private int				numCrates;
	private int				capacity;

	@Override
	public boolean hasCapability( Capability< ? > capability, EnumFacing facing )
	{
		if( capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY )
			return true;
		return super.hasCapability( capability, facing );
	}

	@Override
	public <T> T getCapability( Capability< T > capability, EnumFacing facing )
	{
		if( capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY )
			return (T)getCrateStackHandler();
		return super.getCapability( capability, facing );
	}

	public void onBlockPlaced( EntityLivingBase placer, ItemStack stack )
	{
		if( !worldObj.isRemote && pileID == null )
		{
			getCrateStackHandler().addCrate( this );
			markDirty();
		}
	}

	/** Get the crate stack handler for this tile entity. */
	public CrateStackHandler getCrateStackHandler()
	{
		final CrateStackCollection collection = CrateStackCollection.getCollection( worldObj );
		if( worldObj.isRemote )
			return new CrateStackHandler( getCapacity() );
		return collection.getCratePile( getPileID() );
	}

	public UUID getPileID()
	{
		if( pileID == null )
		{
			final CrateStackCollection collection = CrateStackCollection.getCollection( worldObj );
			final CrateStackHandler handler = collection.createCratePile();
			pileID = handler.getPileID();
		}
		return pileID;
	}

	public void setPileID( UUID pileID )
	{
		this.pileID = pileID;
	}

	/**
	 * Destroys all crates above, and makes sure when piles split,
	 * each pile gets their own CratePileData object.
	 */
	private void checkPileConnections( UUID pileID )
	{
		final int x = pos.getX(), y = pos.getY(), z = pos.getZ();
		Logger.getLogger( ModInfo.modId ).info( "Checking connections" );

		// Destroy all crates above.
		final TileEntity tileEntity = worldObj.getTileEntity( pos.up() );
		if( tileEntity instanceof TileEntityCrate )
		{
			final TileEntityCrate crateAbove = (TileEntityCrate)tileEntity;
			if( crateAbove != null && crateAbove.getPileID().equals( pileID ) )
			{
				final CrateStackHandler handler = getCrateStackHandler();
				final ItemStack[] overflow = handler.removeCrate( crateAbove );
				if( overflow != null )
					for( final ItemStack stack : overflow )
						if( stack != null && stack.stackSize > 0 )
							worldObj.spawnEntityInWorld( new EntityItem( worldObj, pos.getX(), pos.getY() + 1, pos.getZ(), stack ) );
				worldObj.setBlockToAir( pos.up() );
				worldObj.spawnEntityInWorld( new EntityItem( worldObj, x, y + 1, z, new ItemStack( BetterStorageBlocks.CRATE ) ) );
			}
		}

		// If there's still some crates left and this is a
		// base crate, see which crates are still connected.
		final CrateStackHandler handler = getCrateStackHandler();
		if( getNumCrates() <= 0 || y != handler.getRegion().posMin.getY() )
			return;

		// If there's more than one crate set, they need to split.
		final List< HashSet< TileEntityCrate > > crateSets = getCrateSets( x, y, z, pileID );
		if( crateSets.size() <= 1 )
			return;

		// The first crate set will keep the original pile data.
		// All other sets will get new pile data objects.
		final CrateStackCollection collection = CrateStackCollection.getCollection( worldObj );
		for( int i = 1; i < crateSets.size(); i++ )
		{
			final HashSet< TileEntityCrate > set = crateSets.get( i );
			final CrateStackHandler newHandler = collection.createCratePile();
			int numCrates = set.size();
			// Add the base crates from the set.
			for( TileEntityCrate newCrate : set )
			{
				newHandler.addCrate( newCrate );
				final ItemStack[] overflow = handler.removeCrate( newCrate );
				if( overflow != null )
					for( final ItemStack stack : overflow )
						if( stack != null )
							newHandler.addItems( stack );

				Logger.getLogger( ModInfo.modId ).info( "crateSets=" + crateSets.size() + " : numCrates=" + numCrates );
				// Add all crates above the base crate.
				while( true )
				{
					newCrate = (TileEntityCrate)worldObj.getTileEntity( newCrate.getPos().up() );
					if( newCrate == null )
						break;
					newHandler.addCrate( newCrate );
					numCrates++;
				}
			}
			// Move some of the items over to the new crate pile.
			/*
			 * final int count = numCrates * newHandler.getOccupiedSlots() / ( newHandler.getNumCrates() + numCrates );
			 * for( final ItemStack stack : newHandler.getRandomStacks( count ) )
			 * {
			 * handler.removeItems( stack );
			 * newHandler.addItems( stack );
			 * }
			 */
		}
		// Trim the original map to the size it actually is.
		// This is needed because the crates may not be removed in
		// order, from outside to inside.
		handler.trimRegion( worldObj );
	}

	private List< HashSet< TileEntityCrate > > getCrateSets( int x, int y, int z, UUID pileID )
	{
		final List< HashSet< TileEntityCrate > > crateSets = new ArrayList<>();
		int checkedCrates = 0;

		neighborLoop: // Suck it :P
		for( final EnumFacing dir : EnumFacing.HORIZONTALS )
		{
			final int nx = x + dir.getFrontOffsetX();
			final int nz = z + dir.getFrontOffsetZ();

			// Continue if this neighbor block is not part of the crate pile.
			final TileEntity tileEntity = worldObj.getTileEntity( pos.add( dir.getDirectionVec() ) );
			if( tileEntity instanceof TileEntityCrate )
			{
				final TileEntityCrate neighborCrate = (TileEntityCrate)tileEntity;
				if( neighborCrate == null || !neighborCrate.getPileID().equals( pileID ) )
					continue;

				// See if the neighbor crate is already in a crate set,
				// in that case continue with the next neighbor block.
				for( final HashSet< TileEntityCrate > set : crateSets )
					if( set.contains( neighborCrate ) )
						continue neighborLoop;

				// Create a new set of crates and fill it with all connecting crates.
				final HashSet< TileEntityCrate > set = new HashSet<>();
				set.add( neighborCrate );
				for( final EnumFacing ndir : EnumFacing.HORIZONTALS )
					checkConnections( nx + ndir.getFrontOffsetX(), y, nz + ndir.getFrontOffsetZ(), pileID, set );
				crateSets.add( set );

				// If we checked all crates, stop the loop.
				checkedCrates += set.size();
			}
			if( checkedCrates >= getNumCrates() )
				break;
		}

		return crateSets;
	}

	private void checkConnections( int x, int y, int z, UUID pileID, HashSet< TileEntityCrate > set )
	{
		final TileEntityCrate crate = (TileEntityCrate)worldObj.getTileEntity( new BlockPos( x, y, z ) );
		if( crate == null || !pileID.equals( crate.getPileID() ) || set.contains( crate ) )
			return;
		set.add( crate );
		for( final EnumFacing ndir : EnumFacing.HORIZONTALS )
			checkConnections( x + ndir.getFrontOffsetX(), y, z + ndir.getFrontOffsetZ(), pileID, set );
	}

	public boolean attemptConnect( EnumFacing side )
	{
		if( worldObj.isRemote || side == EnumFacing.UP )
			return false;
		final BlockPos neighbourPos = pos.add( side.getDirectionVec() );

		final TileEntity tileEntity = worldObj.getTileEntity( neighbourPos );
		if( tileEntity instanceof TileEntityCrate )
		{
			final TileEntityCrate crateClicked = (TileEntityCrate)tileEntity;

			final CrateStackHandler handler = crateClicked.getCrateStackHandler();
			if( handler.canAdd( this ) )
			{
				handler.addCrate( this );
				pileID = handler.getPileID();
				final IBlockState state = worldObj.getBlockState( pos );
				worldObj.notifyBlockUpdate( pos, state, state, 3 );
				markDirty();
				return true;
			}
		}
		return false;
	}

	public void setCustomTitle( String displayName )
	{
		customTitle = displayName;
	}

	@Override
	public void invalidate()
	{
		super.invalidate();
		if( worldObj.isRemote )
			return;
		checkPileConnections( getPileID() );
		/*
		 * final CrateStackHandler handler = getCrateStackHandler();
		 * handler.removeCrate( this );
		 * if(handler.getNumCrates()==0)
		 * {
		 * CrateStackCollection collection = CrateStackCollection.getCollection( worldObj );
		 * collection.removeCratePile( pileID );
		 * }
		 */
		// dropOverflowContents( data );
		// checkPileConnections( data );
	}

	public int getNumCrates()
	{
		if( worldObj.isRemote )
			return numCrates;
		return getCrateStackHandler().getNumCrates();
	}

	public void setNumCrates( int numCrates )
	{
		getCrateStackHandler().setNumCrates( numCrates );
	}

	public int getCapacity()
	{
		if( worldObj.isRemote )
			return capacity;
		return getCrateStackHandler().getCapacity();
	}

	// Comparator related

	// private final boolean watcherRegistered = false;

	/*
	 * @Override
	 * protected int getComparatorSignalStengthInternal()
	 * {
	 * if( worldObj.isRemote )
	 * return 0;
	 * final CratePileData data = getPileData();
	 * return data.getOccupiedSlots() > 0 ? 1 + data.getOccupiedSlots() * 14 / data.getCapacity() : 0;
	 * }
	 */

	/*
	 * @Override
	 * protected void markComparatorAccessed()
	 * {
	 * super.markComparatorAccessed();
	 *
	 * if( !watcherRegistered && !worldObj.isRemote )
	 * {
	 * //getPileData().addWatcher( this );
	 * watcherRegistered = true;
	 * }
	 * }
	 */

	/*
	 * @Override
	 * protected void comparatorUpdateAndReset()
	 * {
	 * super.comparatorUpdateAndReset();
	 * if( watcherRegistered && !hasComparatorAccessed() )
	 * {
	 * //getPileData().removeWatcher( this );
	 * watcherRegistered = false;
	 * }
	 * }
	 */

	/*
	 * @Override
	 * public void onCrateItemsModified( ItemStack stack )
	 * {
	 * markContentsChanged();
	 * }
	 */

	// IInventory implementation

	/*
	 * @Override
	 * public ITextComponent getDisplayName()
	 * {
	 * return new TextComponentString( getName() );
	 * }
	 */

	/*
	 * @Override
	 * public int getInventoryStackLimit()
	 * {
	 * return 64;
	 * }
	 */

	/*
	 * @Override
	 * public int getSizeInventory()
	 * {
	 * if( worldObj.isRemote || GlobalConfig.enableCrateInventoryInterfaceSetting.getValue() )
	 * return ( numCrates + (int)Math.pow( numCrates, 1.25 ) ) * ( slotsPerCrate / 2 );
	 * return 0;
	 * // return getPileData().blockView.getSizeInventory();
	 * }
	 */

	/*
	 * @Override
	 * public boolean isItemValidForSlot( int slot, ItemStack stack )
	 * {
	 * if( !GlobalConfig.enableCrateInventoryInterfaceSetting.getValue() )
	 * return false;
	 * return getPileData().blockView.isItemValidForSlot( slot, stack );
	 * }
	 */

	/*
	 * @Override
	 * public void setInventorySlotContents( int slot, ItemStack stack )
	 * {
	 * if( GlobalConfig.enableCrateInventoryInterfaceSetting.getValue() )
	 * {
	 * getPileData().blockView.setInventorySlotContents( slot, stack );
	 * markDirty();
	 * }
	 * }
	 */

	/*
	 * @Override
	 * public ItemStack decrStackSize( int slot, int amount )
	 * {
	 * if( !GlobalConfig.enableCrateInventoryInterfaceSetting.getValue() )
	 * return null;
	 * markDirty();
	 * return getPileData().blockView.decrStackSize( slot, amount );
	 * }
	 */

	@Override
	public void markDirty()
	{
		// if( GlobalConfig.enableCrateInventoryInterfaceSetting.getValue() )
		// getPileData().blockView.markDirty();
		final CrateStackCollection collection = CrateStackCollection.getCollection( worldObj );
		collection.markDirty();
		super.markDirty();
	}

	/*
	 * @Override
	 * public boolean isUseableByPlayer( EntityPlayer player )
	 * {
	 * return false;
	 * }
	 */

	/*
	 * @Override
	 * public boolean hasCustomName()
	 * {
	 * return false;
	 * }
	 */

	/*
	 * @Override
	 * public void openInventory( EntityPlayer player )
	 * {
	 * getPileData().blockView.openInventory( player );
	 * }
	 */

	/*
	 * @Override
	 * public void closeInventory( EntityPlayer player )
	 * {
	 * getPileData().blockView.closeInventory( player );
	 * }
	 */

	// TileEntity synchronization

	@Override
	public NBTTagCompound getUpdateTag()
	{
		final NBTTagCompound compound = super.getUpdateTag();

		if( pileID != null )
			compound.setUniqueId( "PileID", pileID );
		compound.setInteger( "NumCrates", getNumCrates() );
		compound.setInteger( "Capacity", getCapacity() );

		return compound;
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		final NBTTagCompound compound = getUpdateTag();
		return new SPacketUpdateTileEntity( getPos(), 0, compound );
	}

	@Override
	public void onDataPacket( NetworkManager net, SPacketUpdateTileEntity packet )
	{
		final NBTTagCompound compound = packet.getNbtCompound();
		if( compound.hasUniqueId( "PileID" ) )
			pileID = compound.getUniqueId( "PileID" );
		numCrates = compound.getInteger( "NumCrates" );
		capacity = compound.getInteger( "Capacity" );

		worldObj.markBlockRangeForRenderUpdate( pos.add( -1, -1, -1 ), pos.add( 1, 1, 1 ) );
	}

	@Override
	public void handleUpdateTag( NBTTagCompound compound )
	{
		super.handleUpdateTag( compound );

		if( compound.hasUniqueId( "PileID" ) )
			pileID = compound.getUniqueId( "PileID" );
		numCrates = compound.getInteger( "NumCrates" );
		capacity = compound.getInteger( "Capacity" );
	}

	// Reading from / writing to NBT

	@Override
	public NBTTagCompound writeToNBT( NBTTagCompound compound )
	{
		super.writeToNBT( compound );

		if( pileID != null )
			compound.setUniqueId( "PileID", pileID );

		return compound;
	}

	@Override
	public void readFromNBT( NBTTagCompound compound )
	{
		super.readFromNBT( compound );

		if( compound.hasUniqueId( "PileID" ) )
			pileID = compound.getUniqueId( "PileID" );
	}
}
