package io.github.tehstoneman.betterstorage.common.tileentity;

import io.github.tehstoneman.betterstorage.api.ConnectedType;
import io.github.tehstoneman.betterstorage.common.block.BlockConnectableContainer;
import io.github.tehstoneman.betterstorage.common.inventory.ConnectedStackHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public abstract class TileEntityConnectable extends TileEntityContainer
{
	public ConnectedStackHandler				connectedInventory;
	private final LazyOptional< IItemHandler >	connectedHandler	= LazyOptional.of( () -> connectedInventory );

	public TileEntityConnectable( BlockEntityType< ? > tileEntityTypeIn, BlockPos blockPos, BlockState blockState )
	{
		super( tileEntityTypeIn, blockPos, blockState );
	}

	@Override
	public <T> LazyOptional< T > getCapability( Capability< T > capability, Direction facing )
	{
		if( isConnected() )
			if( isMain() )
			{
				if( capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY )
				{
					connectedInventory = new ConnectedStackHandler( inventory, getConnectedTileEntity().inventory );
					return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty( capability, connectedHandler );
				}
			}
			else
				return getMainTileEntity().getCapability( capability, facing );
		return super.getCapability( capability, facing );
	}

	/**
	 * Returns position of the connected BlockEntity
	 *
	 * @return Position
	 */
	public abstract BlockPos getConnected();

	/**
	 * Returns if this container is connected to another one.
	 *
	 * @return True if connected
	 */
	public boolean isConnected()
	{
		final BlockState state = getBlockState();
		if( state.hasProperty( BlockConnectableContainer.TYPE ) )
			return state.getValue( BlockConnectableContainer.TYPE ) != ConnectedType.SINGLE;
		return false;
	}

	/**
	 * Returns if this container is the main container, or not connected to another container.
	 *
	 * @return True if main
	 */
	public boolean isMain()
	{
		final BlockState state = getBlockState();
		if( state.hasProperty( BlockConnectableContainer.TYPE ) )
			return state.getValue( BlockConnectableContainer.TYPE ) == ConnectedType.SINGLE
					|| state.getValue( BlockConnectableContainer.TYPE ) == ConnectedType.MASTER;
		return true;
	}

	/**
	 * Returns the main container.
	 *
	 * @return Main container
	 */
	public TileEntityConnectable getMainTileEntity()
	{
		if( isMain() )
			return this;
		final TileEntityConnectable connectable = getConnectedTileEntity();
		if( connectable != null )
			return connectable;
		return this;
	}

	/**
	 * Returns the connected container.
	 *
	 * @return Connected container
	 */
	public TileEntityConnectable getConnectedTileEntity()
	{
		if( getLevel() == null || !isConnected() )
			return null;
		final BlockEntity tileEntity = getLevel().getBlockEntity( getConnected() );
		return tileEntity instanceof TileEntityConnectable ? (TileEntityConnectable)tileEntity : null;
	}

	@Override
	public void setChanged()
	{
		if( !isMain() )
			getConnectedTileEntity().setChanged();
		super.setChanged();
	}

	/*
	 * ===================
	 * TileEntityContainer
	 * ===================
	 */

	/**
	 * Returns the unlocalized name of the container. <br>
	 * "Large" will be appended if the container is connected to another one.
	 *
	 * @return The name of the container
	 */
	protected abstract String getConnectableName();

	@Override
	public Component getName()
	{
		return new TranslatableComponent( getConnectableName().concat( isConnected() ? "_large" : "" ) );
	}
}
