package io.github.tehstoneman.betterstorage.common.tileentity;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.common.inventory.ConnectedStackHandler;
import io.github.tehstoneman.betterstorage.utils.WorldUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public abstract class TileEntityConnectable extends TileEntityContainer
{
	private EnumFacing	orientation	= null;
	private EnumFacing	connected	= null;

	@Override
	public <T> T getCapability( Capability< T > capability, EnumFacing facing )
	{
		if( isMain() )
			if( isConnected() && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY )
				return (T)new ConnectedStackHandler( inventory, getConnectedTileEntity().inventory );
			else
				return super.getCapability( capability, facing );
		return getMainTileEntity().getCapability( capability, facing );
	}

	public EnumFacing getOrientation()
	{
		return orientation != null ? orientation : EnumFacing.NORTH;
	}

	public void setOrientation( EnumFacing orientation )
	{
		this.orientation = orientation;
		if( getWorld() != null )
			getWorld().notifyBlockUpdate( pos, getWorld().getBlockState( pos ), getWorld().getBlockState( pos ), 3 );
	}

	public EnumFacing getConnected()
	{
		return connected;
	}

	public void setConnected( EnumFacing connected )
	{
		this.connected = connected;
		if( getWorld() != null )
			getWorld().notifyBlockUpdate( pos, getWorld().getBlockState( pos ), getWorld().getBlockState( pos ), 3 );
		markDirty();
	}

	/** Returns the possible directions the container can connect to. */
	public abstract EnumFacing[] getPossibleNeighbors();

	/** Returns if this container is connected to another one. */
	public boolean isConnected()
	{
		return getConnected() != null;
	}

	/** Returns if this container is the main container, or not connected to another container. */
	public boolean isMain()
	{
		final EnumFacing connected = getConnected();
		return !isConnected() || connected.getFrontOffsetX() + connected.getFrontOffsetY() + connected.getFrontOffsetZ() > 0;
	}

	/** Returns the main container. */
	public TileEntityConnectable getMainTileEntity()
	{
		if( isMain() )
			return this;
		final TileEntityConnectable connectable = getConnectedTileEntity();
		if( connectable != null )
			return connectable;
		if( BetterStorage.config.enableWarningMessages )
			BetterStorage.logger.warn( "getConnectedTileEntity() returned null in getMainTileEntity(). " + "Location: {},{},{}", pos.getX(), pos.getY(),
					pos.getZ() );
		return this;
	}

	/** Returns the connected container. */
	public TileEntityConnectable getConnectedTileEntity()
	{
		if( getWorld() == null || !isConnected() )
			return null;
		final TileEntity tileEntity = getWorld().getTileEntity( pos.offset( getConnected() ) );
		return tileEntity instanceof TileEntityConnectable ? (TileEntityConnectable)tileEntity : null;
	}

	/** Returns if the container can connect to the other container. */
	public boolean canConnect( TileEntityConnectable connectable )
	{
		return connectable != null && // check for null
				getBlockType() == connectable.getBlockType() && // check for same block type
				getOrientation() == connectable.getOrientation() && // check for same orientation
				getBlockMetadata() == connectable.getBlockMetadata() && // check for same material
				// Make sure the containers are not already connected.
				!isConnected() && !connectable.isConnected();
	}

	/** Connects the container to any other containers nearby, if possible. */
	public void checkForConnections()
	{
		if( getWorld().isRemote )
			return;
		TileEntityConnectable connectableFound = null;
		EnumFacing dirFound = null;
		for( final EnumFacing dir : getPossibleNeighbors() )
		{
			final int x = pos.getX() + dir.getFrontOffsetX();
			final int y = pos.getY() + dir.getFrontOffsetY();
			final int z = pos.getZ() + dir.getFrontOffsetZ();
			final TileEntityConnectable connectable = WorldUtils.get( getWorld(), x, y, z, TileEntityConnectable.class );
			if( !canConnect( connectable ) )
				continue;
			if( connectableFound != null )
				return;
			connectableFound = connectable;
			dirFound = dir;
		}
		if( connectableFound == null )
			return;
		setConnected( dirFound );
		connectableFound.setConnected( dirFound.getOpposite() );
		// Mark the block for an update, sends description packet to players.
		markForUpdate();
		connectableFound.markForUpdate();
	}

	/** Disconnects the container from its connected container, if it has one. */
	public void disconnect()
	{
		if( !isConnected() )
			return;
		final TileEntityConnectable connectable = getConnectedTileEntity();
		setConnected( null );
		if( connectable != null )
		{
			connectable.setConnected( null );
			connectable.markForUpdate();
		}
		else
			if( BetterStorage.config.enableWarningMessages )
				BetterStorage.logger.warn( "getConnectedTileEntity() returned null in disconnect(). " + "Location: {},{},{}", pos.getX(), pos.getY(),
						pos.getZ() );
	}

	// TileEntityContainer stuff

	/**
	 * Returns the unlocalized name of the container. <br>
	 * "Large" will be appended if the container is connected to another one.
	 */
	protected abstract String getConnectableName();

	@Override
	public final String getName()
	{
		return getConnectableName() + ( isConnected() ? "_large" : "" );
	}

	@Override
	protected boolean doesSyncPlayers()
	{
		return true;
	}

	@Override
	public int getRows()
	{
		if( isConnected() )
			return super.getRows() * 2;
		return super.getRows();
	}

	@Override
	public final void onBlockPlaced( EntityLivingBase player, ItemStack stack )
	{
		super.onBlockPlaced( player, stack );
		setOrientation( player.getHorizontalFacing().getOpposite() );
		checkForConnections();
	}

	@Override
	public void onBlockDestroyed()
	{
		super.onBlockDestroyed();
		disconnect();
	}

	/** Returns if the container is accessible by other machines etc. */
	protected boolean isAccessible()
	{
		return true;
	}

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

	@Override
	public void update()
	{
		super.update();

		double x = pos.getX() + 0.5;
		final double y = pos.getY() + 0.5;
		double z = pos.getZ() + 0.5;

		if( isConnected() )
		{
			if( !isMain() )
				return;
			final TileEntityConnectable connectable = getConnectedTileEntity();
			if( connectable != null )
			{
				x = ( x + connectable.pos.getX() + 0.5 ) / 2;
				z = ( z + connectable.pos.getZ() + 0.5 ) / 2;
				lidAngle = Math.max( lidAngle, connectable.lidAngle );
			}
		}

		final float pitch = getWorld().rand.nextFloat() * 0.1F + 0.9F;

		// Play sound when opening
		if( lidAngle > 0.0F && prevLidAngle == 0.0F )
			getWorld().playSound( (EntityPlayer)null, x, y, z, SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, pitch );
		// Play sound when closing
		if( lidAngle < 0.5F && prevLidAngle >= 0.5F )
			getWorld().playSound( (EntityPlayer)null, x, y, z, SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 0.5F, pitch );
	}

	// IInventory stuff

	@Override
	public ITextComponent getDisplayName()
	{
		return new TextComponentString( getName() );
	}

	@Override
	public void markDirty()
	{
		super.markDirty();
		if( isConnected() && getWorld() != null )
		{
			final TileEntity connected = getConnectedTileEntity();
			connected.updateContainingBlockInfo();
			getWorld().markChunkDirty( connected.getPos(), this );

			if( connected.getBlockType() != Blocks.AIR )
				getWorld().updateComparatorOutputLevel( connected.getPos(), connected.getBlockType() );
		}
	}

	// Tile entity synchronization

	@Override
	public NBTTagCompound getUpdateTag()
	{
		final NBTTagCompound compound = super.getUpdateTag();
		if( getOrientation() != null )
			compound.setByte( "orientation", (byte)getOrientation().getIndex() );
		if( getConnected() != null )
			compound.setByte( "connected", (byte)getConnected().getIndex() );
		return compound;
	}

	@Override
	public void handleUpdateTag( NBTTagCompound compound )
	{
		super.handleUpdateTag( compound );
		if( compound.hasKey( "orientation" ) )
			setOrientation( EnumFacing.getFront( compound.getByte( "orientation" ) ) );
		if( compound.hasKey( "connected" ) )
			connected = EnumFacing.getFront( compound.getByte( "connected" ) );
		else
			connected = null;
	}

	// Reading from / writing to NBT

	@Override
	public NBTTagCompound writeToNBT( NBTTagCompound compound )
	{
		super.writeToNBT( compound );
		if( getOrientation() != null )
			compound.setByte( "orientation", (byte)getOrientation().getIndex() );
		if( getConnected() != null )
			compound.setByte( "connected", (byte)getConnected().getIndex() );
		return compound;
	}

	@Override
	public void readFromNBT( NBTTagCompound compound )
	{
		super.readFromNBT( compound );
		if( compound.hasKey( "orientation" ) )
			setOrientation( EnumFacing.getFront( compound.getByte( "orientation" ) ) );
		if( compound.hasKey( "connected" ) )
			setConnected( EnumFacing.getFront( compound.getByte( "connected" ) ) );
	}
}
