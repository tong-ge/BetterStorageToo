package io.github.tehstoneman.betterstorage.common.tileentity;

import java.util.UUID;

import javax.annotation.Nullable;

import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.common.inventory.ContainerCrate;
import io.github.tehstoneman.betterstorage.common.inventory.CrateStackHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class TileEntityCrate extends TileEntityConnectable
{
	public CrateStackHandler					crateInventory;
	private final LazyOptional< IItemHandler >	crateHandler	= LazyOptional.of( () -> crateInventory );

	public static final int						slotsPerCrate		= 18;
	public static final int						maxCrates			= 125;
	public static final int						maxPerSide			= 5;

	private UUID								pileID;
	protected ITextComponent					customName;

	private int									numCrates;
	private int									capacity;

	public TileEntityCrate( TileEntityType< ? > tileEntityTypeIn )
	{
		super( tileEntityTypeIn );
	}

	public TileEntityCrate()
	{
		this( BetterStorageTileEntityTypes.CRATE );
	}

	@Override
	public <T> LazyOptional< T > getCapability( Capability< T > capability, Direction facing )
	{
		if( capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY )
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty( capability, crateHandler );

		/*
		 * if( facing == null || BetterStorageConfig.GENERAL.enableCrateInventoryInterface.get() )
		 * return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty( capability, inventoryHandler );
		 */
		return super.getCapability( capability, facing );
	}

	/** Helper function to add crate to handler */
	/*
	 * public void onBlockPlaced( EntityLivingBase placer, ItemStack stack )
	 * {
	 * if( !getWorld().isRemote && pileID == null )
	 * {
	 * getCrateStackHandler().addCrate( this );
	 * markDirty();
	 * }
	 * }
	 */

	/** Get the crate stack handler for this tile entity. */
	/*
	 * public CrateStackHandler getCrateStackHandler()
	 * {
	 * final CrateStackCollection collection = CrateStackCollection.getCollection( getWorld() );
	 * if( getWorld().isRemote )
	 * return new CrateStackHandler( getCapacity() );
	 * final CrateStackHandler handler = collection.getCratePile( pileID );
	 * handler.sendUpdatesTo( this );
	 * return handler;
	 * }
	 */

	/** Get the pile ID for this tile entity */
	/*
	 * public UUID getPileID()
	 * {
	 * if( pileID == null )
	 * {
	 * final CrateStackCollection collection = CrateStackCollection.getCollection( getWorld() );
	 * final CrateStackHandler handler = collection.createCratePile();
	 * pileID = handler.getPileID();
	 * }
	 * return pileID;
	 * }
	 */

	/** Sets the pile ID for this tile entity */
	public void setPileID( UUID pileID )
	{
		this.pileID = pileID;
		markDirty();
	}

	/**
	 * Destroys all crates above, and makes sure when piles split,
	 * each pile gets their own CratePileData object.
	 */
	/*
	 * private void checkPileConnections( UUID pileID )
	 * {
	 * final int x = pos.getX(), y = pos.getY(), z = pos.getZ();
	 *
	 * // Destroy all crates above.
	 * final TileEntity tileEntity = getWorld().getTileEntity( pos.up() );
	 * if( tileEntity instanceof TileEntityCrate )
	 * {
	 * final TileEntityCrate crateAbove = (TileEntityCrate)tileEntity;
	 * if( crateAbove != null && crateAbove.getPileID().equals( pileID ) )
	 * {
	 * final CrateStackHandler handler = getCrateStackHandler();
	 * final NonNullList< ItemStack > overflow = handler.removeCrate( crateAbove );
	 * if( !overflow.isEmpty() )
	 * for( final ItemStack stack : overflow )
	 * if( !stack.isEmpty() )
	 * getWorld().spawnEntity( new EntityItem( getWorld(), pos.getX(), pos.getY() + 1, pos.getZ(), stack ) );
	 * getWorld().setBlockToAir( pos.up() );
	 * getWorld().spawnEntity( new EntityItem( getWorld(), x, y + 1, z, new ItemStack( BetterStorageBlocks.CRATE ) ) );
	 * }
	 * }
	 *
	 * // If there's still some crates left and this is a
	 * // base crate, see which crates are still connected.
	 * final CrateStackHandler handler = getCrateStackHandler();
	 * if( getNumCrates() <= 0 || y != handler.getRegion().posMin.getY() )
	 * return;
	 *
	 * // If there's more than one crate set, they need to split.
	 * final List< HashSet< TileEntityCrate > > crateSets = getCrateSets( x, y, z, pileID );
	 * if( crateSets.size() <= 1 )
	 * {
	 * handler.trimRegion( getWorld() );
	 * notifyRegionUpdate( handler.getRegion(), getPileID() );
	 * return;
	 * }
	 *
	 * // The first crate set will keep the original pile data.
	 * // All other sets will get new pile data objects.
	 * final CrateStackCollection collection = CrateStackCollection.getCollection( getWorld() );
	 * for( int i = 1; i < crateSets.size(); i++ )
	 * {
	 * final HashSet< TileEntityCrate > set = crateSets.get( i );
	 * final CrateStackHandler newHandler = collection.createCratePile();
	 * int numCrates = set.size();
	 * // Add the base crates from the set.
	 * for( TileEntityCrate newCrate : set )
	 * {
	 * newHandler.addCrate( newCrate );
	 * final NonNullList< ItemStack > overflow = handler.removeCrate( newCrate );
	 * if( !overflow.isEmpty() )
	 * for( final ItemStack stack : overflow )
	 * if( !stack.isEmpty() )
	 * newHandler.addItems( stack );
	 *
	 * // Add all crates above the base crate.
	 * while( true )
	 * {
	 * newCrate = (TileEntityCrate)getWorld().getTileEntity( newCrate.getPos().up() );
	 * if( newCrate == null )
	 * break;
	 * newHandler.addCrate( newCrate );
	 * numCrates++;
	 * }
	 * }
	 * notifyRegionUpdate( newHandler.getRegion(), newHandler.getPileID() );
	 * }
	 * // Trim the original map to the size it actually is.
	 * // This is needed because the crates may not be removed in
	 * // order, from outside to inside.
	 * handler.trimRegion( getWorld() );
	 * notifyRegionUpdate( handler.getRegion(), getPileID() );
	 * }
	 */

	/** Compile lists of connected crates */
	/*
	 * private List< HashSet< TileEntityCrate > > getCrateSets( int x, int y, int z, UUID pileID )
	 * {
	 * final List< HashSet< TileEntityCrate > > crateSets = new ArrayList<>();
	 * int checkedCrates = 0;
	 *
	 * neighborLoop: // Suck it :P
	 * for( final EnumFacing dir : EnumFacing.HORIZONTALS )
	 * {
	 * final int nx = x + dir.getFrontOffsetX();
	 * final int nz = z + dir.getFrontOffsetZ();
	 *
	 * // Continue if this neighbor block is not part of the crate pile.
	 * final TileEntity tileEntity = getWorld().getTileEntity( pos.add( dir.getDirectionVec() ) );
	 * if( tileEntity instanceof TileEntityCrate )
	 * {
	 * final TileEntityCrate neighborCrate = (TileEntityCrate)tileEntity;
	 * if( neighborCrate == null || !neighborCrate.getPileID().equals( pileID ) )
	 * continue;
	 *
	 * // See if the neighbor crate is already in a crate set,
	 * // in that case continue with the next neighbor block.
	 * for( final HashSet< TileEntityCrate > set : crateSets )
	 * if( set.contains( neighborCrate ) )
	 * continue neighborLoop;
	 *
	 * // Create a new set of crates and fill it with all connecting crates.
	 * final HashSet< TileEntityCrate > set = new HashSet<>();
	 * set.add( neighborCrate );
	 * for( final EnumFacing ndir : EnumFacing.HORIZONTALS )
	 * checkConnections( nx + ndir.getFrontOffsetX(), y, nz + ndir.getFrontOffsetZ(), pileID, set );
	 * crateSets.add( set );
	 *
	 * // If we checked all crates, stop the loop.
	 * checkedCrates += set.size();
	 * }
	 * if( checkedCrates >= getNumCrates() )
	 * break;
	 * }
	 *
	 * return crateSets;
	 * }
	 */

	/** Checks crate connections */
	/*
	 * private void checkConnections( int x, int y, int z, UUID pileID, HashSet< TileEntityCrate > set )
	 * {
	 * final TileEntity tileEntity = getWorld().getTileEntity( new BlockPos( x, y, z ) );
	 * if( tileEntity instanceof TileEntityCrate )
	 * {
	 * final TileEntityCrate crate = (TileEntityCrate)tileEntity;
	 * if( !pileID.equals( crate.getPileID() ) || set.contains( crate ) )
	 * return;
	 * set.add( crate );
	 * }
	 * for( final EnumFacing ndir : EnumFacing.HORIZONTALS )
	 * checkConnections( x + ndir.getFrontOffsetX(), y, z + ndir.getFrontOffsetZ(), pileID, set );
	 * }
	 */

	/** Tries to connect a crate to the given side */
	/*
	 * public boolean attemptConnect( EnumFacing side )
	 * {
	 * if( getWorld().isRemote || side == EnumFacing.UP )
	 * return false;
	 * final BlockPos neighbourPos = pos.add( side.getDirectionVec() );
	 *
	 * final TileEntity tileEntity = getWorld().getTileEntity( neighbourPos );
	 * if( tileEntity instanceof TileEntityCrate )
	 * {
	 * final TileEntityCrate crateClicked = (TileEntityCrate)tileEntity;
	 *
	 * final CrateStackHandler handler = crateClicked.getCrateStackHandler();
	 * if( handler.canAdd( this ) )
	 * {
	 * handler.addCrate( this );
	 * pileID = handler.getPileID();
	 * notifyRegionUpdate( handler.getRegion(), pileID );
	 * markDirty();
	 * crateClicked.markDirty();
	 * return true;
	 * }
	 * }
	 * return false;
	 * }
	 */

	/** Notify all matching crates within a region of an update */
	/*
	 * public void notifyRegionUpdate( Region region, UUID pileID )
	 * {
	 * for( final BlockPos blockPos : BlockPos.getAllInBox( region.posMin, region.posMax ) )
	 * {
	 * final TileEntity te = getWorld().getTileEntity( blockPos );
	 * if( te instanceof TileEntityCrate && ( (TileEntityCrate)te ).pileID.equals( pileID ) )
	 * {
	 * final IBlockState state = getWorld().getBlockState( blockPos );
	 * getWorld().notifyBlockUpdate( blockPos, state, state, 3 );
	 * }
	 * }
	 * }
	 */

	/*
	 * @Override
	 * public void invalidate()
	 * {
	 * super.invalidate();
	 * if( getWorld().isRemote )
	 * return;
	 * checkPileConnections( getPileID() );
	 * }
	 */

	/** Get the number of crates connected to this tile entity */
	public int getNumCrates()
	{
		// if( getWorld().isRemote )
		return numCrates;
		// return getCrateStackHandler().getNumCrates();
	}

	/** Sets the number of crates connected to this tile entity */
	/*
	 * public void setNumCrates( int numCrates )
	 * {
	 * getCrateStackHandler().setNumCrates( numCrates );
	 * markDirty();
	 * }
	 */

	/** Get the total capacity this tile entity */

	public int getCapacity()
	{
		// if( getWorld().isRemote )
		return capacity;
		// return getCrateStackHandler().getCapacity();
	}

	// Comparator related

	/** Get the comparator level of this tile entity */
	/*
	 * public int getComparatorSignalStrength()
	 * {
	 * if( getWorld().isRemote )
	 * return 0;
	 * final CrateStackHandler handler = getCrateStackHandler();
	 * return handler.getOccupiedSlots() > 0 ? 1 + handler.getOccupiedSlots() * 14 / handler.getCapacity() : 0;
	 * }
	 */

	/*
	 * @Override
	 * public void markDirty()
	 * {
	 * final CrateStackCollection collection = CrateStackCollection.getCollection( getWorld() );
	 * collection.markDirty();
	 * super.markDirty();
	 * }
	 */

	@Override
	public boolean hasCustomName()
	{
		return customName != null;
	}

	@Override
	public void setCustomName( @Nullable ITextComponent name )
	{
		customName = name;
	}

	@Override
	@Nullable
	public ITextComponent getCustomName()
	{
		return customName;
	}

	@Override
	public Container createMenu( int windowID, PlayerInventory playerInventory, PlayerEntity player )
	{
		return new ContainerCrate( windowID, playerInventory, world, pos );
	}

	@Override
	public ITextComponent getName()
	{
		return new TranslationTextComponent( ModInfo.CONTAINER_CRATE_NAME );
	}

	@Override
	public BlockPos getConnected()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getConnectableName()
	{
		// TODO Auto-generated method stub
		return null;
	}

	// TileEntity synchronization

	/*
	 * @Override
	 * public NBTTagCompound getUpdateTag()
	 * {
	 * final NBTTagCompound compound = super.getUpdateTag();
	 *
	 * if( pileID != null )
	 * compound.setUniqueId( "PileID", pileID );
	 * compound.setInt( "NumCrates", getNumCrates() );
	 * compound.setInt( "Capacity", getCapacity() );
	 *
	 * return compound;
	 * }
	 */

	/*
	 * @Override
	 * public SPacketUpdateTileEntity getUpdatePacket()
	 * {
	 * final NBTTagCompound compound = getUpdateTag();
	 * return new SPacketUpdateTileEntity( getPos(), 0, compound );
	 * }
	 */

	/*
	 * @Override
	 * public void onDataPacket( NetworkManager net, SPacketUpdateTileEntity packet )
	 * {
	 * final NBTTagCompound compound = packet.getNbtCompound();
	 * if( compound.hasUniqueId( "PileID" ) )
	 * pileID = compound.getUniqueId( "PileID" );
	 * numCrates = compound.getInt( "NumCrates" );
	 * capacity = compound.getInt( "Capacity" );
	 *
	 * getWorld().markBlockRangeForRenderUpdate( pos.add( -1, -1, -1 ), pos.add( 1, 1, 1 ) );
	 * }
	 */

	/*
	 * @Override
	 * public void handleUpdateTag( NBTTagCompound compound )
	 * {
	 * super.handleUpdateTag( compound );
	 *
	 * if( compound.hasUniqueId( "PileID" ) )
	 * pileID = compound.getUniqueId( "PileID" );
	 * numCrates = compound.getInt( "NumCrates" );
	 * capacity = compound.getInt( "Capacity" );
	 * }
	 */

	// Reading from / writing to NBT

	/*
	 * @Override
	 * public NBTTagCompound write( NBTTagCompound compound )
	 * {
	 * super.write( compound );
	 *
	 * if( pileID != null )
	 * compound.setUniqueId( "PileID", pileID );
	 * compound.setInt( "NumCrates", getNumCrates() );
	 * compound.setInt( "Capacity", getCapacity() );
	 *
	 * return compound;
	 * }
	 */

	/*
	 * @Override
	 * public void read( NBTTagCompound compound )
	 * {
	 * super.read( compound );
	 *
	 * if( compound.hasUniqueId( "PileID" ) )
	 * pileID = compound.getUniqueId( "PileID" );
	 * numCrates = compound.getInt( "NumCrates" );
	 * capacity = compound.getInt( "Capacity" );
	 * }
	 */
}
