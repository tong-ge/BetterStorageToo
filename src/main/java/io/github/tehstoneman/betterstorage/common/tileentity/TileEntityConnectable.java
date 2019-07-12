package io.github.tehstoneman.betterstorage.common.tileentity;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.api.EnumConnectedType;
import io.github.tehstoneman.betterstorage.common.block.BlockConnectableContainer;
import io.github.tehstoneman.betterstorage.common.inventory.ConnectedStackHandler;
import io.github.tehstoneman.betterstorage.config.BetterStorageConfig;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public abstract class TileEntityConnectable extends TileEntityContainer
{
	public ConnectedStackHandler				connectedInventory;
	private final LazyOptional< IItemHandler >	connectedHandler	= LazyOptional.of( () -> connectedInventory );

	public TileEntityConnectable( TileEntityType< ? > tileEntityTypeIn )
	{
		super( tileEntityTypeIn );
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

	/** Returns position of the connected TileEntity */
	public abstract BlockPos getConnected();

	/** Returns if this container is connected to another one. */
	public boolean isConnected()
	{
		final BlockState state = getBlockState();
		if( state.has( BlockConnectableContainer.TYPE ) )
			return state.get( BlockConnectableContainer.TYPE ) != EnumConnectedType.SINGLE;
		return false;
	};

	/** Returns if this container is the main container, or not connected to another container. */
	public boolean isMain()
	{

		final BlockState state = getBlockState();
		if( state.has( BlockConnectableContainer.TYPE ) )
			return state.get( BlockConnectableContainer.TYPE ) == EnumConnectedType.SINGLE
					|| state.get( BlockConnectableContainer.TYPE ) == EnumConnectedType.MASTER;
		return false;
	}

	/** Returns the main container. */
	public TileEntityConnectable getMainTileEntity()
	{
		if( isMain() )
			return this;
		final TileEntityConnectable connectable = getConnectedTileEntity();
		if( connectable != null )
			return connectable;
		if( BetterStorageConfig.COMMON.enableWarningMessages.get() )
			BetterStorage.LOGGER.warn( "getConnectedTileEntity() returned null in getMainTileEntity(). " + "Location: {},{},{}", pos.getX(),
					pos.getY(), pos.getZ() );
		return this;
	}

	/** Returns the connected container. */
	public TileEntityConnectable getConnectedTileEntity()
	{
		if( getWorld() == null || !isConnected() )
			return null;
		final TileEntity tileEntity = getWorld().getTileEntity( getConnected() );
		return tileEntity instanceof TileEntityConnectable ? (TileEntityConnectable)tileEntity : null;
	}

	/*
	 * ===================
	 * TileEntityContainer
	 * ===================
	 */

	/**
	 * Returns the unlocalized name of the container. <br>
	 * "Large" will be appended if the container is connected to another one.
	 */
	protected abstract String getConnectableName();

	@Override
	public ITextComponent getName()
	{
		return new TranslationTextComponent( getConnectableName().concat( isConnected() ? "_large" : "" ) );
	}

	@Override
	protected boolean doesSyncPlayers()
	{
		return true;
	}

	/*
	 * @Override
	 * public int getRows()
	 * {
	 * if( isConnected() )
	 * return super.getRows() * 2;
	 * return super.getRows();
	 * }
	 */

	@Override
	public int getComparatorSignalStrength()
	{
		if( getWorld().isRemote )
			return 0;

		if( !isConnected() )
			return super.getComparatorSignalStrength();

		int i = 0;
		float f = 0.0F;

		for( int j = 0; j < inventory.getSlots(); ++j )
		{
			final ItemStack itemstack = inventory.getStackInSlot( j );

			if( itemstack != null )
			{
				f += (float)itemstack.getCount() / itemstack.getMaxStackSize();
				++i;
			}
		}

		final ItemStackHandler otherInventory = getConnectedTileEntity().inventory;

		for( int j = 0; j < otherInventory.getSlots(); ++j )
		{
			final ItemStack itemstack = otherInventory.getStackInSlot( j );

			if( itemstack != null )
			{
				f += (float)itemstack.getCount() / itemstack.getMaxStackSize();
				++i;
			}
		}

		f = f / ( inventory.getSlots() + otherInventory.getSlots() );
		return MathHelper.floor( f * 14.0F ) + ( i > 0 ? 1 : 0 );
	}

	// Update entity

	/*
	 * @Override
	 * public void update()
	 * {
	 * super.update();
	 *
	 * double x = pos.getX() + 0.5;
	 * final double y = pos.getY() + 0.5;
	 * double z = pos.getZ() + 0.5;
	 *
	 * if( isConnected() )
	 * {
	 * if( !isMain() )
	 * return;
	 * final TileEntityConnectable connectable = getConnectedTileEntity();
	 * if( connectable != null )
	 * {
	 * x = ( x + connectable.pos.getX() + 0.5 ) / 2;
	 * z = ( z + connectable.pos.getZ() + 0.5 ) / 2;
	 * lidAngle = Math.max( lidAngle, connectable.lidAngle );
	 * }
	 * }
	 *
	 * final float pitch = getWorld().rand.nextFloat() * 0.1F + 0.9F;
	 *
	 * // Play sound when opening
	 * if( lidAngle > 0.0F && prevLidAngle == 0.0F )
	 * getWorld().playSound( (EntityPlayer)null, x, y, z, SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, pitch );
	 * // Play sound when closing
	 * if( lidAngle < 0.5F && prevLidAngle >= 0.5F )
	 * getWorld().playSound( (EntityPlayer)null, x, y, z, SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 0.5F, pitch );
	 * }
	 */

	/*
	 * @Override
	 * public ContainerBetterStorage getContainer( EntityPlayer player )
	 * {
	 * return new ContainerBetterStorage( getMainTileEntity(), player );
	 * }
	 */
}
