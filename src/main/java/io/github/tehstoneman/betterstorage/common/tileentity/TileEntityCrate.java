package io.github.tehstoneman.betterstorage.common.tileentity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;


import com.google.common.collect.Iterables;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.common.inventory.ContainerCrate;
import io.github.tehstoneman.betterstorage.common.inventory.CrateStackHandler;
import io.github.tehstoneman.betterstorage.common.inventory.Region;
import io.github.tehstoneman.betterstorage.common.world.CrateStackCollection;
import io.github.tehstoneman.betterstorage.config.BetterStorageConfig;
import io.github.tehstoneman.betterstorage.network.UpdateCrateMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.PacketDistributor;

public class TileEntityCrate extends TileEntityContainer
{
	// private static final int maxCratePileSize = 125;

	public static final int	slotsPerCrate	= 18;
	public static final int	MAX_CRATES		= 125;
	public static final int	MAX_PER_SIDE	= 5;

	// private BlockPos mainPos;
	private UUID			pileID;
	protected Component		customName;

	private int				numCrates		= 1;
	private int				capacity		= slotsPerCrate;

	public TileEntityCrate( BlockEntityType< ? > tileEntityTypeIn, BlockPos blockPos, BlockState blockState )
	{
		super( tileEntityTypeIn, blockPos, blockState );
	}

	public TileEntityCrate( BlockPos blockPos, BlockState blockState )
	{
		this( BetterStorageTileEntityTypes.CRATE.get(), blockPos, blockState );
	}

	@Override
	public <T> LazyOptional< T > getCapability( Capability< T > capability, Direction facing )
	{
		if( capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY )
		{
			if( facing != null && !BetterStorageConfig.COMMON.crateAllowAutomation.get() )
				return LazyOptional.empty();
			final LazyOptional< IItemHandler > crateHandler = LazyOptional.of( () -> getCrateStackHandler() );
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty( capability, crateHandler );
		}
		else
			return super.getCapability( capability, facing );
	}

	public boolean hasID()
	{
		return pileID != null;
	}

	public CrateStackHandler getCrateStackHandler()
	{
		if( level.isClientSide )
			return new CrateStackHandler( getCapacity() );

		final CrateStackCollection collection = CrateStackCollection.getCollection( level );
		final CrateStackHandler handler = collection.computeIfAbsentCratePile( getPileID() );
		handler.sendUpdatesTo( this );

		return handler;
	}

	public UUID getPileID()
	{
		if( pileID == null )
		{
			final CrateStackCollection collection = CrateStackCollection.getCollection( level );
			final CrateStackHandler handler = collection.createCratePile();
			setPileID( handler.getPileID() );
		}
		return pileID;
	}

	public boolean isSamePile( TileEntityCrate tileEntity )
	{
		return tileEntity.hasID() && getPileID().equals( tileEntity.getPileID() );
	}

	public void setPileID(UUID pileID )
	{
		this.pileID = pileID;
		setChanged();
	}

	public void updateConnections()
	{
		/*
		 * if( !getLevel().isClientSide )
		 * {
		 * final CrateStackHandler handler = getCrateStackHandler();
		 * pileID = handler.getPileID();
		 * numCrates = handler.getNumCrates();
		 * checkPileConnections( pileID );
		 * setChanged();
		 * notifyRegionUpdate( handler.getRegion(), pileID );
		 * }
		 */
	}

	private void checkPileConnections( UUID pileID )
	{
		final CrateStackHandler handler = getCrateStackHandler();
		final CrateStackCollection collection = CrateStackCollection.getCollection( level );

		// Remove empty pile
		if( handler.getNumCrates() <= 0 )
		{
			collection.removeCratePile( pileID );
			final BlockState state = getBlockState();
			getLevel().sendBlockUpdated( worldPosition, state, state, 3 );
			return;
		}

		// If there's more than one crate set, they need to split.
		final List< HashSet< TileEntityCrate > > crateSets = getCrateSets( worldPosition, pileID );
		if( crateSets.size() > 1 )
			// The first crate set will keep the original pile data.
			// All other sets will get new pile data objects.
			for( final HashSet< TileEntityCrate > set : Iterables.skip( crateSets, 1 ) )
			{
				// final HashSet< TileEntityCrate > set = crateSets.get( i );
				final CrateStackHandler newHandler = collection.createCratePile();
				// int numCrates = set.size();

				// Add the base crates from the set.
				for( final TileEntityCrate crate : set )
				{
					final NonNullList< ItemStack > overflow = handler.removeCrate( crate );
					newHandler.addCrate( crate );
					if( !overflow.isEmpty() )
						for( final ItemStack stack : overflow )
							if( !stack.isEmpty() )
								newHandler.addItems( stack );
				}
				notifyRegionUpdate( newHandler.getRegion(), newHandler.getPileID() );
			}
		// Trim the original map to the size it actually is.
		// This is needed because the crates may not be removed in
		// order, from outside to inside.
		handler.trimRegion( getLevel() );
		notifyRegionUpdate( handler.getRegion(), getPileID() );
	}

	private List< HashSet< TileEntityCrate > > getCrateSets( BlockPos pos, UUID pileID )
	{
		final List< HashSet< TileEntityCrate > > crateSets = new ArrayList<>();
		for( final Direction dir : Direction.values() )
		{
			// Continue if this neighbor block is not part of the crate pile.
			final TileEntityCrate tileCrate = getCrateAt( level, pos.relative( dir ) );
			if( tileCrate != null )
				if( !isInSet( tileCrate, crateSets ) )
				{
					// Create a new set of crates and fill it with all connecting crates.
					final HashSet< TileEntityCrate > set = new HashSet<>();
					set.add( tileCrate );
					for( final Direction ndir : Direction.values() )
						checkConnections( tileCrate.getBlockPos().relative( ndir ), pileID, set );
					crateSets.add( set );
				}
		}

		return crateSets;
	}

	private boolean isInSet( TileEntityCrate tileCrate, List< HashSet< TileEntityCrate > > crateSets )
	{
		for( final HashSet< TileEntityCrate > set : crateSets )
			if( set.contains( tileCrate ) )
				return true;
		return false;
	}

	private void checkConnections( BlockPos pos, UUID pileID, HashSet< TileEntityCrate > set )
	{
		final TileEntityCrate tileCrate = getCrateAt( level, pos );
		if( tileCrate == null || tileCrate == this || !pileID.equals( tileCrate.getPileID() ) || set.contains( tileCrate ) )
			return;
		set.add( tileCrate );
		for( final Direction ndir : Direction.values() )
			checkConnections( pos.relative( ndir ), pileID, set );
	}

	public void notifyRegionUpdate( Region region, UUID pileID )
	{
		if( region == null || region.isEmpty() )
			return;
		for( final BlockPos blockPos : region.betweenClosed() )
		{
			final BlockEntity te = getLevel().getBlockEntity( blockPos );
			if( te instanceof TileEntityCrate && ( (TileEntityCrate)te ).pileID.equals( pileID ) )
			{
				te.setChanged();
				final BlockState state = getLevel().getBlockState( blockPos );
				getLevel().sendBlockUpdated( blockPos, state, state, 3 );
			}
		}
	}

	public void setCustomTitle( String displayName )
	{}

	public int getNumCrates()
	{
		/*
		 * if( getLevel().isClientSide )
		 * return numCrates;
		 */
		return getCrateStackHandler().getNumCrates();
	}

	public void setNumCrates( int numCrates )
	{
		this.numCrates = numCrates;
	}

	public int getCapacity()
	{
		/*
		 * if( getLevel().isClientSide )
		 * return capacity;
		 */
		return getCrateStackHandler().getCapacity();
	}

	public void setCapacity( int capacity )
	{
		this.capacity = capacity;
	}

	// Comparator related

	@Override
	public void setChanged()
	{
		super.setChanged();
		if( !level.isClientSide )
			CrateStackCollection.getCollection( getLevel() ).setDirty();
	}

	// BlockEntity synchronization

	/*
	 * @Override
	 * public ClientboundBlockEntityDataPacket getUpdatePacket()
	 * {
	 * final CompoundTag nbt = new CompoundTag();
	 *
	 * if( pileID != null )
	 * nbt.putUUID( "PileID", pileID );
	 * nbt.putInt( "NumCrates", getNumCrates() );
	 * nbt.putInt( "Capacity", getCapacity() );
	 * return new ClientboundBlockEntityDataPacket( getBlockPos(), 1, nbt );
	 * }
	 */

	@Override
	public void onDataPacket( Connection network, ClientboundBlockEntityDataPacket packet )
	{
		final CompoundTag nbt = packet.getTag();

		if( nbt.hasUUID( "PileID" ) )
			pileID = nbt.getUUID( "PileID" );
		numCrates = nbt.getInt( "NumCrates" );
		capacity = nbt.getInt( "Capacity" );
	}

	@Override
	public CompoundTag getUpdateTag()
	{
		final CompoundTag nbt = super.getUpdateTag();

		if( pileID != null )
			nbt.putUUID( "PileID", pileID );
		nbt.putInt( "NumCrates", getNumCrates() );
		nbt.putInt( "Capacity", getCapacity() );

		return nbt;
	}

	@Override
	public void handleUpdateTag( CompoundTag nbt )
	{
		super.handleUpdateTag( nbt );

		if( nbt.hasUUID( "PileID" ) )
			pileID = nbt.getUUID( "PileID" );
		numCrates = nbt.getInt( "NumCrates" );
		capacity = nbt.getInt( "Capacity" );
	}

	// Reading from / writing to NBT

	@Override
	public CompoundTag save( CompoundTag nbt )
	{
		super.save( nbt );

		if( pileID != null )
			nbt.putUUID( "PileID", pileID );

		return nbt;
	}

	@Override
	public void load( CompoundTag nbt )
	{
		if( nbt.hasUUID( "PileID" ) )
			pileID = nbt.getUUID( "PileID" );

		super.load( nbt );
	}

	@Override
	public AbstractContainerMenu createMenu( int windowID, Inventory playerInventory, Player player )
	{
		/*
		 * BetterStorage.NETWORK.send( PacketDistributor.PLAYER.setValue( () -> (ServerPlayer)player ),
		 * new UpdateCrateMessage( worldPosition, getNumCrates(), getCapacity() ) );
		 */
		return new ContainerCrate( windowID, playerInventory, level, worldPosition );
	}

	@Override
	public Component getName()
	{
		return new TranslatableComponent( ModInfo.CONTAINER_CRATE_NAME );
	}

	@Nullable
	public static TileEntityCrate getCrateAt( BlockGetter world, BlockPos pos )
	{
		final BlockEntity tileEntity = world.getBlockEntity( pos );
		if( tileEntity instanceof TileEntityCrate )
			return (TileEntityCrate)tileEntity;
		return null;
	}

	public boolean tryAddCrate( @Nullable TileEntityCrate crate )
	{
		// Rule 1 - New crate must exist
		if( crate == null )
			return false;

		// Rule 2 - PileID's must be different
		if( crate.getPileID().equals( pileID ) )
			return true;

		// Rule 3 - New region must remain within size limits
		final CrateStackHandler handler = getCrateStackHandler();
		final Region region = handler.getRegion().clone();
		if( crate.getNumCrates() > 1 )
			region.expandToContain( crate.getCrateStackHandler().getRegion() );
		else
			region.expandToContain( crate );
		if( region.width() > MAX_PER_SIDE || region.height() > MAX_PER_SIDE || region.depth() > MAX_PER_SIDE )
			return false;

		// All rules passed - Add crate to pile
		for( final BlockPos blockPos : BlockPos.betweenClosed( region.posMin, region.posMax ) )
		{
			final TileEntityCrate newCrate = getCrateAt( level, blockPos );
			if( newCrate != null && !newCrate.getPileID().equals( pileID ) )
				handler.addCrate( newCrate );
		}
		notifyRegionUpdate( region, pileID );
		checkPileConnections( pileID );
		return crate.getPileID().equals( pileID );
	}

	public NonNullList< ItemStack > removeCrate()
	{
		final NonNullList< ItemStack > overflow = getCrateStackHandler().removeCrate( this );
		checkPileConnections( pileID );
		return overflow;
	}
}
