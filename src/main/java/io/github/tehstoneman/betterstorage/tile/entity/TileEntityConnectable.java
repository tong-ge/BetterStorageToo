package io.github.tehstoneman.betterstorage.tile.entity;

import java.util.logging.Logger;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityContainer;
import io.github.tehstoneman.betterstorage.config.GlobalConfig;
import io.github.tehstoneman.betterstorage.inventory.InventoryTileEntity;
import io.github.tehstoneman.betterstorage.utils.DirectionUtils;
import io.github.tehstoneman.betterstorage.utils.WorldUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public abstract class TileEntityConnectable extends TileEntityContainer implements IInventory
{
	private EnumFacing	orientation	= null;
	private EnumFacing	connected	= null;

	public EnumFacing getOrientation()
	{
		return orientation;
	}

	public void setOrientation( EnumFacing orientation )
	{
		this.orientation = orientation;
		if( worldObj != null )
			worldObj.notifyBlockUpdate( pos, worldObj.getBlockState( pos ), worldObj.getBlockState( pos ), 3 );
	}

	public EnumFacing getConnected()
	{
		return connected;
	}

	public void setConnected( EnumFacing connected )
	{
		this.connected = connected;
		if( worldObj != null )
			worldObj.notifyBlockUpdate( pos, worldObj.getBlockState( pos ), worldObj.getBlockState( pos ), 3 );
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
		if( BetterStorage.globalConfig.getBoolean( GlobalConfig.enableWarningMessages ) )
			BetterStorage.log.warn( "getConnectedTileEntity() returned null in getMainTileEntity(). " + "Location: {},{},{}", pos.getX(), pos.getY(),
					pos.getZ() );
		return this;
	}

	/** Returns the connected container. */
	public TileEntityConnectable getConnectedTileEntity()
	{
		if( !isConnected() )
			return null;
		final EnumFacing connected = getConnected();
		final int x = pos.getX() + connected.getFrontOffsetX();
		final int y = pos.getY() + connected.getFrontOffsetY();
		final int z = pos.getZ() + connected.getFrontOffsetZ();
		return WorldUtils.get( worldObj, x, y, z, TileEntityConnectable.class );
	}

	/** Returns if the container can connect to the other container. */
	public boolean canConnect( TileEntityConnectable connectable )
	{
		return connectable != null && // check for null
				getBlockType() == connectable.getBlockType() && // check for same block type
				getOrientation() == connectable.getOrientation() && // check for same orientation
				// Make sure the containers are not already connected.
				!isConnected() && !connectable.isConnected();
	}

	/** Connects the container to any other containers nearby, if possible. */
	public void checkForConnections()
	{
		if( worldObj.isRemote )
			return;
		TileEntityConnectable connectableFound = null;
		EnumFacing dirFound = null;
		for( final EnumFacing dir : getPossibleNeighbors() )
		{
			final int x = pos.getX() + dir.getFrontOffsetX();
			final int y = pos.getY() + dir.getFrontOffsetY();
			final int z = pos.getZ() + dir.getFrontOffsetZ();
			final TileEntityConnectable connectable = WorldUtils.get( worldObj, x, y, z, TileEntityConnectable.class );
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
			if( BetterStorage.globalConfig.getBoolean( GlobalConfig.enableWarningMessages ) )
				BetterStorage.log.warn( "getConnectedTileEntity() returned null in disconnect(). " + "Location: {},{},{}", pos.getX(), pos.getY(),
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
		return getConnectableName() + ( isConnected() ? "Large" : "" );
	}

	@Override
	protected boolean doesSyncPlayers()
	{
		return true;
	}

	@Override
	public InventoryTileEntity getPlayerInventory()
	{
		final TileEntityConnectable connected = getConnectedTileEntity();
		if( connected != null )
			return new InventoryTileEntity( this, isMain() ? this : connected, isMain() ? connected : this );
		else
			return super.getPlayerInventory();
	}

	@Override
	public final void onBlockPlaced( EntityLivingBase player, ItemStack stack )
	{
		super.onBlockPlaced( player, stack );
		onBlockPlacedBeforeCheckingConnections( player, stack );
		checkForConnections();
	}

	@Override
	public void onBlockDestroyed()
	{
		super.onBlockDestroyed();
		disconnect();
	}

	// This is a horrible name for a function.
	protected void onBlockPlacedBeforeCheckingConnections( EntityLivingBase player, ItemStack stack )
	{
		setOrientation( DirectionUtils.getOrientation( player ).getOpposite() );
	}

	/** Returns if the container is accessible by other machines etc. */
	protected boolean isAccessible()
	{
		return true;
	}

	// Update entity

	@Override
	public void updateEntity()
	{
		super.updateEntity();

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

		final float pitch = worldObj.rand.nextFloat() * 0.1F + 0.9F;

		// Play sound when opening
		// if ((lidAngle > 0.0F) && (prevLidAngle == 0.0F)) worldObj.playSoundEffect(x, y, z, "random.chestopen", 0.5F, pitch);
		// Play sound when closing
		// if ((lidAngle < 0.5F) && (prevLidAngle >= 0.5F)) worldObj.playSoundEffect(x, y, z, "random.chestclosed", 0.5F, pitch);
	}

	// IInventory stuff

	@Override
	public ITextComponent getDisplayName()
	{
		return new TextComponentString( getName() );
	}

	@Override
	public boolean hasCustomName()
	{
		return !shouldLocalizeTitle();
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public int getSizeInventory()
	{
		return isAccessible() ? getPlayerInventory().getSizeInventory() : 0;
	}

	@Override
	public ItemStack getStackInSlot( int slot )
	{
		return isAccessible() ? getPlayerInventory().getStackInSlot( slot ) : null;
	}

	@Override
	public void setInventorySlotContents( int slot, ItemStack stack )
	{
		if( isAccessible() )
			getPlayerInventory().setInventorySlotContents( slot, stack );
	}

	@Override
	public ItemStack decrStackSize( int slot, int amount )
	{
		return isAccessible() ? getPlayerInventory().decrStackSize( slot, amount ) : null;
	}

	@Override
	public boolean isItemValidForSlot( int slot, ItemStack stack )
	{
		return isAccessible() ? getPlayerInventory().isItemValidForSlot( slot, stack ) : false;
	}

	@Override
	public boolean isUseableByPlayer( EntityPlayer player )
	{
		return isAccessible() ? getPlayerInventory().isUseableByPlayer( player ) : false;
	}

	@Override
	public void openInventory( EntityPlayer player )
	{
		if( isAccessible() )
			getPlayerInventory().openInventory( player );
	}

	@Override
	public void closeInventory( EntityPlayer player )
	{
		if( isAccessible() )
			getPlayerInventory().closeInventory( player );
	}

	@Override
	public void markDirty()
	{
		if( isAccessible() )
			getPlayerInventory().markDirty();
	}

	// Tile entity synchronization

	@Override
	public NBTTagCompound getUpdateTag()
	{
		final NBTTagCompound compound = new NBTTagCompound();
		if( getOrientation() != null )
			compound.setByte( "orientation", (byte)getOrientation().getIndex() );
		if( getConnected() != null )
			compound.setByte( "connected", (byte)getConnected().getIndex() );
		return compound;
	}

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
	}

	@Override
	public void handleUpdateTag( NBTTagCompound compound )
	{
		if( compound.hasKey( "orientation" ) )
			setOrientation( EnumFacing.getFront( compound.getByte( "orientation" ) ) );
		if( compound.hasKey( "connected" ) )
			setConnected( EnumFacing.getFront( compound.getByte( "connected" ) ) );
		//Logger.getLogger( Constants.modId ).info(  compound.toString() );
	}

	// Reading from / writing to NBT

	@Override
	public void readFromNBT( NBTTagCompound compound )
	{
		super.readFromNBT( compound );
		if( compound.hasKey( "orientation" ) )
			setOrientation( EnumFacing.getFront( compound.getByte( "orientation" ) ) );
		if( compound.hasKey( "connected" ) )
			setConnected( EnumFacing.getFront( compound.getByte( "connected" ) ) );
		//Logger.getLogger( Constants.modId ).info(  compound.toString() );
	}

	@Override
	public NBTTagCompound writeToNBT( NBTTagCompound compound )
	{
		super.writeToNBT( compound );
		if( getOrientation() != null )
			compound.setByte( "orientation", (byte)getOrientation().getIndex() );
		if( getConnected() != null )
			compound.setByte( "connected", (byte)getConnected().getIndex() );
		//Logger.getLogger( Constants.modId ).info(  compound.toString() );
		return compound;
	}
}
