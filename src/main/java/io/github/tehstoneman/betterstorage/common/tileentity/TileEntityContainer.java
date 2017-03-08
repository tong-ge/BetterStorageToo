package io.github.tehstoneman.betterstorage.common.tileentity;

import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.client.gui.BetterStorageGUIHandler.EnumGui;
import io.github.tehstoneman.betterstorage.utils.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public abstract class TileEntityContainer extends TileEntity implements ITickable
{
	public ItemStackHandler	inventory;

	// private final InventoryTileEntity playerInventory;

	/** The custom title of this container, set by an anvil. */
	private String			customTitle			= null;

	private int				playersUsing		= 0;

	protected boolean		brokenInCreative	= false;

	public int				ticksExisted		= 0;
	public float			lidAngle			= 0;
	public float			prevLidAngle		= 0;

	public TileEntityContainer()
	{
		final int size = getSizeContents();
		if( size > 0 )
			inventory = new ItemStackHandler( size )
			{
				@Override
				protected void onContentsChanged(int slot)
				{
					TileEntityContainer.this.markDirty();
				}
			};
			else
				inventory = null;
	}

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
			return (T)inventory;
		return super.getCapability( capability, facing );
	}

	/** The amount of columns in the container. */
	public int getColumns()
	{
		return 9;
	}

	/** The amount of rows in the container. */
	public int getRows()
	{
		return 3;
	}

	/** The size of the container's contents, or 0 if it's not supposed to have any items. */
	protected int getSizeContents()
	{
		return getColumns() * getRows();
	}

	/** The unlocalized name of the container, for example "container.chest". */
	public abstract String getName();

	/** The number of players which are currently accessing this container. */
	public final int getPlayersUsing()
	{
		return playersUsing;
	}

	/*
	 * public InventoryTileEntity makePlayerInventory()
	 * {
	 * return inventory != null ? new InventoryTileEntity( this ) : null;
	 * }
	 */

	/*
	 * public InventoryTileEntity getPlayerInventory()
	 * {
	 * return playerInventory;
	 * }
	 */

	/**
	 * Returns if a player can use this container. This is called once before
	 * the GUI is opened and then again every tick. Returning false when the
	 * GUI is open, like when the player is too far away, will close the GUI.
	 */
	public boolean canPlayerUseContainer( EntityPlayer player )
	{
		return WorldUtils.isTileEntityUsableByPlayer( this, player );
	}

	/** Creates and returns a Container for this container. */
	/*
	 * public ContainerBetterStorage createContainer( EntityPlayer player )
	 * {
	 * return new ContainerBetterStorage( player, getPlayerInventory() );
	 * }
	 */

	// Container title related

	/** Returns the title of the container. */
	public String getContainerTitle()
	{
		return hasCustomTitle() ? getCustomTitle() : getName();
	}

	/** Returns the custom title of this container. */
	public String getCustomTitle()
	{
		return customTitle;
	}

	/** Returns if the container has a custom title. */
	public boolean hasCustomTitle()
	{
		return getCustomTitle() != null;
	}

	/** Returns if the title of this container should be localized. */
	public boolean shouldLocalizeTitle()
	{
		return !hasCustomTitle();
	}

	/** Returns if the title of this container can be set. */
	public boolean canSetCustomTitle()
	{
		return true;
	}

	/** Sets the custom title of this container. Has no effect if it can't be set. */
	public void setCustomTitle( String title )
	{
		if( canSetCustomTitle() )
			customTitle = title;
	}

	// Block functions

	/**
	 * Called when a block is placed by a player. Sets data of the tile
	 * entity, like custom container title, enchantments or similar.
	 */
	public void onBlockPlaced( EntityLivingBase player, ItemStack stack )
	{
		if( stack.hasDisplayName() )
			setCustomTitle( stack.getDisplayName() );
	}

	/**
	 * Called then the block is activated (right clicked).
	 * Usually opens the GUI of the container.
	 */
	public boolean onBlockActivated( EntityPlayer player, int side, float hitX, float hitY, float hitZ )
	{
		if( worldObj.isRemote )
			return true;
		if( !canPlayerUseContainer( player ) )
			return true;
		player.openGui( ModInfo.modId, EnumGui.GENERAL.getGuiID(), worldObj, pos.getX(), pos.getY(), pos.getZ() );
		return true;
	}

	/** Opens the GUI of this container for the player. */
	/*
	 * public void openGui( EntityPlayer player )
	 * {
	 * final ContainerBetterStorage container = createContainer( player );
	 * PlayerUtils.openGui( player, getName(), container.getColumns(), container.getRows(), getCustomTitle(), container );
	 * }
	 */

	/**
	 * Called when the block is picked (default: middle mouse).
	 * Returns the item to be picked, or null if nothing.
	 */
	public ItemStack onPickBlock( ItemStack block, RayTraceResult target )
	{
		return block;
	}

	/**
	 * Called when a block is attempted to be broken by a player.
	 * Returns if the block should actually be broken.
	 */
	public boolean onBlockBreak( EntityPlayer player )
	{
		brokenInCreative = player.capabilities.isCreativeMode;
		return true;
	}

	/** Called when a neighbor block changes. */
	public void onNeighborUpdate( Block neighborBlock )
	{
		if( acceptsRedstoneSignal() && neighborBlock.canProvidePower( null ) )
			checkForRedstoneChange();
	}

	/** Called after the block is destroyed, drops contents etc. */
	public void onBlockDestroyed()
	{
		dropContents();
		brokenInCreative = false;
	}

	/** Called when the container is destroyed to drop its contents. */
	public void dropContents()
	{
		if( inventory != null )
			for( int i = 0; i < inventory.getSlots(); i++ )
			{
				final ItemStack stack = inventory.getStackInSlot( i );
				if( stack != null && stack.stackSize > 0 )
					worldObj.spawnEntityInWorld( new EntityItem( worldObj, pos.getX(), pos.getY(), pos.getZ(), stack ) );
			}
	}

	/**
	 * Called before the tile entity is being rendered as an item.
	 * Sets things like the material taken from the stack. <br>
	 * Only gets called if an ItemRendererContainer is registered.
	 */
	@SideOnly( Side.CLIENT )
	public void onBlockRenderAsItem( ItemStack stack )
	{}

	// Redstone related

	private int redstonePower = 0;

	/** Returns if the block accepts redstone power. */
	protected boolean acceptsRedstoneSignal()
	{
		return false;
	}

	/** Returns if the block requires a strong instead of a weak signal. */
	protected boolean requiresStrongSignal()
	{
		return false;
	}

	public int getRedstonePower()
	{
		return redstonePower;
	}

	public boolean isRedstonePowered()
	{
		return getRedstonePower() > 0;
	}

	protected void checkForRedstoneChange()
	{
		final int previousPower = redstonePower;
		redstonePower = requiresStrongSignal() ? getStrongRedstoneSignal() : getWeakRedstoneSignal();
		if( redstonePower == previousPower )
			return;
		onRedstonePowerChanged( previousPower, redstonePower );
		if( previousPower <= 0 )
			onRedstoneActivated();
		if( redstonePower <= 0 )
			onRedstoneDeactivated();
	}

	/** Called when redstone power going to this block changes. */
	protected void onRedstonePowerChanged( int previousPower, int currentPower )
	{}

	/** Called when redstone power going to this block activates. */
	protected void onRedstoneActivated()
	{}

	/** Called when redstone power going to this block deactivates. */
	protected void onRedstoneDeactivated()
	{}

	/** Returns the strong redstone signal power going into this block. */
	protected int getStrongRedstoneSignal()
	{
		return worldObj.getStrongPower( pos );
	}

	/** Returns the weak redstone signal power going into this block. */
	protected int getWeakRedstoneSignal()
	{
		return worldObj.isBlockIndirectlyGettingPowered( pos );
	}

	// Comparator related

	private boolean	compAccessedOnLoad	= false;
	private boolean	compAccessed		= false;
	private boolean	compContentsChanged	= false;

	protected boolean hasComparatorAccessed()
	{
		return compAccessed;
	}

	protected boolean hasContentsChanged()
	{
		return compContentsChanged;
	}

	/**
	 * Called when a comparator or similar block wants to know this
	 * TileEntity's comparator signal strength. Marks this location
	 * to be updated the next time the container contents change.
	 */
	public int getComparatorSignalStrength()
	{
		if( worldObj.isRemote )
			return 0;

		int i = 0;
		float f = 0.0F;

		for( int j = 0; j < inventory.getSlots(); ++j )
		{
			final ItemStack itemstack = inventory.getStackInSlot( j );

			if( itemstack != null )
			{
				f += (float)itemstack.stackSize / itemstack.getMaxStackSize();
				++i;
			}
		}

		f = f / inventory.getSlots();
		return MathHelper.floor_float( f * 14.0F ) + ( i > 0 ? 1 : 0 );
	}

	/**
	 * Called when a comparator or similar block wants to know this
	 * container's comparator signal strength. Marks the block to
	 * update blocks around it next updateEntity().
	 */
	protected void markComparatorAccessed()
	{
		compAccessed = true;
	}

	/**
	 * Called when the inventory is modified. When a comparator
	 * accessed this container, marks to update blocks around it
	 * next updateEntity().
	 */
	protected void markContentsChanged()
	{
		compContentsChanged = true;
	}

	/**
	 * Returns the container's comparator signal strength. Calculated
	 * automatically if it implements IInventory. Overridden if necessary.
	 */
	protected int getComparatorSignalStengthInternal()
	{
		return this instanceof IInventory ? Container.calcRedstoneFromInventory( (IInventory)this ) : 0;
	}

	/** Resets accessed and contents changed flags and updates nearby blocks. */
	protected void comparatorUpdateAndReset()
	{
		compAccessed = false;
		compContentsChanged = false;
		WorldUtils.notifyBlocksAround( worldObj, pos.getX(), pos.getY(), pos.getZ() );
	}

	/**
	 * Calls the TileEntity.markDirty function without affecting the
	 * attached inventory (if any). Marks the tile entity to be saved.
	 */
	public void markDirtySuper()
	{
		if( worldObj == null || worldObj.isRemote )
			return;
		worldObj.markChunkDirty( new BlockPos( pos ), this );
		if( hasComparatorAccessed() )
			markContentsChanged();
	}

	/*
	 * @Override
	 * public void markDirty()
	 * {
	 * markDirtySuper();
	 * super.markDirty();
	 * }
	 */

	@Override
	public void validate()
	{
		if( compAccessedOnLoad )
		{
			markComparatorAccessed();
			compAccessedOnLoad = false;
		}
	}

	// Players using synchronization

	/** Returns if the container does synchronize playersUsing at all. */
	protected boolean doesSyncPlayers()
	{
		return false;
	}

	/** Returns if the container should synchronize playersUsing over the network, called each tick. */
	protected boolean syncPlayersUsing()
	{
		return !worldObj.isRemote && doesSyncPlayers() && ( ticksExisted + pos.getX() + pos.getY() + pos.getZ() & 0xFF ) == 0;
		// && worldObj.doChunksNearChunkExist( pos, 16 );
	}

	/** Synchronizes playersUsing over the network. */
	private void doSyncPlayersUsing( int playersUsing )
	{
		if( !doesSyncPlayers() )
			return;
		worldObj.addBlockEvent( pos, getBlockType(), 0, playersUsing );
	}

	@Override
	public boolean receiveClientEvent( int event, int value )
	{
		if( event == 0 )
			playersUsing = value;
		return true;
	}

	/** Called when a player opens this container. */
	public void onContainerOpened()
	{
		doSyncPlayersUsing( ++playersUsing );
	}

	/** Called when a player closes this container. */
	public void onContainerClosed()
	{
		doSyncPlayersUsing( --playersUsing );
	}

	// Update entity

	protected float getLidSpeed()
	{
		return 0.1F;
	}

	@Override
	public void update()
	{
		if( ticksExisted++ == 0 )
			// Only run once after tile entity has loaded.
			checkForRedstoneChange();

		// If a comparator or such has accessed the container and
		// the contents have been changed, send a block update.
		if( hasComparatorAccessed() && hasContentsChanged() )
			comparatorUpdateAndReset();

		prevLidAngle = lidAngle;
		if( playersUsing > 0 )
		{
			if( lidAngle < 1.0F )
				lidAngle = Math.min( 1.0F, lidAngle + getLidSpeed() );
		}
		else
			if( lidAngle > 0.0F )
				lidAngle = Math.max( 0.0F, lidAngle - getLidSpeed() );
	}

	// TileEntity synchronization

	@Override
	public NBTTagCompound getUpdateTag()
	{
		final NBTTagCompound compound = super.getUpdateTag();

		if( customTitle != null )
			compound.setString( "CustomName", customTitle );

		return compound;
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		final NBTTagCompound compound = getUpdateTag();
		return new SPacketUpdateTileEntity( getPos(), getBlockMetadata(), compound );
	}

	@Override
	public void onDataPacket( NetworkManager net, SPacketUpdateTileEntity packet )
	{
		handleUpdateTag( packet.getNbtCompound() );

		worldObj.markBlockRangeForRenderUpdate( pos.add( -1, -1, -1 ), pos.add( 1, 1, 1 ) );
	}

	@Override
	public void handleUpdateTag( NBTTagCompound compound )
	{
		super.handleUpdateTag( compound );

		if( compound.hasKey( "CustomName" ) )
			customTitle = compound.getString( "CustomName" );
	}

	// Reading from / writing to NBT

	@Override
	public void readFromNBT( NBTTagCompound compound )
	{
		super.readFromNBT( compound );
		if( compound.hasKey( "CustomName" ) )
			customTitle = compound.getString( "CustomName" );
		if( compound.hasKey( "Inventory" ) )
			inventory.deserializeNBT( compound.getCompoundTag( "Inventory" ) );
		if( compound.getBoolean( "ComparatorAccessed" ) )
			compAccessedOnLoad = true;
		if( acceptsRedstoneSignal() )
			redstonePower = compound.getByte( "RedstonePower" );
	}

	@Override
	public NBTTagCompound writeToNBT( NBTTagCompound compound )
	{
		super.writeToNBT( compound );
		if( customTitle != null )
			compound.setString( "CustomName", customTitle );
		if( inventory != null )
			compound.setTag( "Inventory", inventory.serializeNBT() );
		if( hasComparatorAccessed() )
			compound.setBoolean( "ComparatorAccessed", true );
		if( acceptsRedstoneSignal() )
			compound.setByte( "RedstonePower", (byte)redstonePower );
		return compound;
	}

	// Utility functions

	/**
	 * Marks the block for an update, which will cause
	 * a description packet to be send to players.
	 */
	public void markForUpdate()
	{
		worldObj.notifyBlockUpdate( pos, worldObj.getBlockState( pos ), worldObj.getBlockState( pos ), 3 );
		markDirty();
	}

}
