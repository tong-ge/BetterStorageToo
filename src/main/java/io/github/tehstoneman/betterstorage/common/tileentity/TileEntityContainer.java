package io.github.tehstoneman.betterstorage.common.tileentity;

import javax.annotation.Nullable;

import io.github.tehstoneman.betterstorage.common.block.BlockContainerBetterStorage;
import io.github.tehstoneman.betterstorage.common.inventory.ExpandableStackHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.INameable;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public abstract class TileEntityContainer extends TileEntity implements INamedContainerProvider, INameable
{
	public ExpandableStackHandler				inventory;
	private final LazyOptional< IItemHandler >	inventoryHandler	= LazyOptional.of( () -> inventory );

	/** The custom title of this container, set by an anvil. */
	protected ITextComponent					customName;

	protected int								numPlayersUsing;

	public int									ticksExisted		= 0;
	public float								lidAngle			= 0;
	public float								prevLidAngle		= 0;

	public TileEntityContainer( TileEntityType< ? > tileEntityTypeIn )
	{
		super( tileEntityTypeIn );
		final int size = getSizeContents();
		if( size > 0 )
			inventory = new ExpandableStackHandler( getColumns(), getRows() )
			{
				@Override
				protected void onContentsChanged( int slot )
				{
					TileEntityContainer.this.setChanged();
				}
			};
		else
			inventory = null;
	}

	/*
	 * ============
	 * Capabilities
	 * ============
	 */

	@Nullable
	@Override
	public <T> LazyOptional< T > getCapability( Capability< T > capability, @Nullable Direction facing )
	{
		if( capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY )
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty( capability, inventoryHandler );
		return super.getCapability( capability, facing );
	}

	/*
	 * private IItemHandler createHandler()
	 * {
	 * return new ExpandableStackHandler( getColumns(), getRows() );
	 * }
	 */

	/**
	 * The amount of columns in the container.
	 *
	 * @return Number of columns
	 */
	public int getColumns()
	{
		return 9;
	}

	/**
	 * The amount of rows in the container.
	 *
	 * @return Number of rows.
	 */
	public int getRows()
	{
		return 3;
	}

	/**
	 * The size of the container's contents, or 0 if it's not supposed to have any items.
	 *
	 * @return Number of slots.
	 */
	protected int getSizeContents()
	{
		return getColumns() * getRows();
	}

	/*
	 * ===========
	 * Interaction
	 * ===========
	 */

	/**
	 * The number of players which are currently accessing this container.
	 *
	 * @return Number of players using.
	 */
	public final int getPlayersUsing()
	{
		return numPlayersUsing;
	}

	/*
	 * ===================
	 * INameable functions
	 * ===================
	 */

	/**
	 * Sets the custom title of this container. Has no effect if it can't be set.
	 *
	 * @param name
	 *            The name of this container.
	 */
	public void setCustomName( @Nullable ITextComponent name )
	{
		customName = name;
	}

	/**
	 * Returns the custom title of this container.
	 *
	 */
	@Override
	@Nullable
	public ITextComponent getCustomName()
	{
		return customName;
	}

	@Override
	public ITextComponent getDisplayName()
	{
		if( hasCustomName() )
			return getCustomName();
		return getName();
	}

	/*
	 * =================
	 * Utility functions
	 * =================
	 */

	protected static int EVENT_PLAYER_USING = 1;

	/**
	 * Marks the block for an update, which will cause
	 * a description packet to be send to players.
	 */
	public void markForUpdate()
	{
		getLevel().sendBlockUpdated( worldPosition, getLevel().getBlockState( worldPosition ), getLevel().getBlockState( worldPosition ), 3 );
		setChanged();
	}

	public void dropInventoryItems()
	{
		if( inventory != null )
			for( int i = 0; i < inventory.getSlots(); i++ )
			{
				final ItemStack stack = inventory.getStackInSlot( i );
				if( !stack.isEmpty() )
					InventoryHelper.dropItemStack( getLevel(), worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), stack );
			}
	}

	public void openInventory( PlayerEntity player )
	{
		if( !player.isSpectator() )
		{
			if( numPlayersUsing < 0 )
				numPlayersUsing = 0;

			++numPlayersUsing;
			onOpenOrClose();
		}
	}

	public void closeInventory( PlayerEntity player )
	{
		if( !player.isSpectator() )
		{
			--numPlayersUsing;
			onOpenOrClose();
		}
	}

	protected void onOpenOrClose()
	{
		final Block block = getBlockState().getBlock();
		if( block instanceof BlockContainerBetterStorage )
		{
			level.blockEvent( worldPosition, block, EVENT_PLAYER_USING, numPlayersUsing );
			level.updateNeighborsAt( worldPosition, block );
		}
	}

	@Override
	public boolean triggerEvent( int id, int type )
	{
		if( id == EVENT_PLAYER_USING )
		{
			numPlayersUsing = type;
			return true;
		}
		else
			return super.triggerEvent( id, type );
	}

	/*
	 * ==========================
	 * TileEntity synchronization
	 * ==========================
	 */

	@Override
	public CompoundNBT getUpdateTag()
	{
		final CompoundNBT nbt = super.getUpdateTag();

		if( inventory.getSlots() > 0 )
			nbt.put( "Inventory", inventory.serializeNBT() );

		if( hasCustomName() )
			nbt.putString( "CustomName", ITextComponent.Serializer.toJson( getCustomName() ) );

		return nbt;
	}

	@Override
	public void handleUpdateTag( BlockState state, CompoundNBT nbt )
	{
		super.handleUpdateTag( state, nbt );

		if( nbt.contains( "Inventory" ) )
			inventory.deserializeNBT( (CompoundNBT)nbt.get( "Inventory" ) );

		if( nbt.contains( "CustomName" ) )
			setCustomName( ITextComponent.Serializer.fromJson( nbt.getString( "CustomName" ) ) );
	}

	@Override
	public CompoundNBT save( CompoundNBT nbt )
	{
		if( inventory.getSlots() > 0 )
			nbt.put( "Inventory", inventory.serializeNBT() );

		if( hasCustomName() )
			nbt.putString( "CustomName", ITextComponent.Serializer.toJson( getCustomName() ) );

		return super.save( nbt );
	}

	@Override
	public void load( BlockState state, CompoundNBT nbt )
	{
		if( nbt.contains( "Inventory" ) )
			inventory.deserializeNBT( (CompoundNBT)nbt.get( "Inventory" ) );

		if( nbt.contains( "CustomName" ) )
			setCustomName( ITextComponent.Serializer.fromJson( nbt.getString( "CustomName" ) ) );

		// For backwards compatibility
		if( nbt.contains( "inventory" ) )
			inventory.deserializeNBT( (CompoundNBT)nbt.get( "inventory" ) );

		super.load( state, nbt );
	}
}
