package io.github.tehstoneman.betterstorage.common.tileentity;

import java.security.InvalidParameterException;

import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.api.EnumReinforced;
import io.github.tehstoneman.betterstorage.api.lock.EnumLockInteraction;
import io.github.tehstoneman.betterstorage.api.lock.IKey;
import io.github.tehstoneman.betterstorage.api.lock.ILock;
import io.github.tehstoneman.betterstorage.api.lock.ILockable;
import io.github.tehstoneman.betterstorage.attachment.Attachments;
import io.github.tehstoneman.betterstorage.attachment.IHasAttachments;
import io.github.tehstoneman.betterstorage.attachment.LockAttachment;
import io.github.tehstoneman.betterstorage.client.gui.BetterStorageGUIHandler.EnumGui;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

public abstract class TileEntityLockable extends TileEntityConnectable implements ILockable, IHasAttachments
{
	private boolean			powered;

	public EnumReinforced	material;
	public LockAttachment	lockAttachment;

	protected Attachments	attachments	= new Attachments( this );

	public TileEntityLockable()
	{
		if( !canHaveLock() )
			return;
		lockAttachment = attachments.add( LockAttachment.class );
		lockAttachment.setScale( 0.5F, 1.5F );
		setAttachmentPosition();
	}

	@Override
	public boolean hasCapability( Capability< ? > capability, EnumFacing facing )
	{
		if( capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && facing != null && getLock() != null )
			return false;
		return super.hasCapability( capability, facing );
	}

	public boolean canHaveLock()
	{
		return true;
	}

	protected ItemStack getLockInternal()
	{
		return canHaveLock() ? lockAttachment.getItem() : ItemStack.EMPTY;
	}

	protected void setLockInternal( ItemStack lock )
	{
		lockAttachment.setItem( lock );
	}

	public EnumReinforced getMaterial()
	{
		if( material == null )
			material = EnumReinforced.IRON;
		return material;
	}

	public void setMaterial( EnumReinforced reinforcedMaterial )
	{
		material = reinforcedMaterial;
		markDirty();
	}

	// Attachment points

	public abstract void setAttachmentPosition();

	@Override
	public Attachments getAttachments()
	{
		return ( (TileEntityLockable)getMainTileEntity() ).attachments;
	}

	/*
	 * @Override
	 * public void setOrientation( EnumFacing orientation )
	 * {
	 * super.setOrientation( orientation );
	 * if( canHaveLock() )
	 * lockAttachment.setDirection( orientation );
	 * }
	 */

	/*
	 * @Override
	 * public void setConnected( EnumFacing connected )
	 * {
	 * super.setConnected( connected );
	 * if( canHaveLock() )
	 * setAttachmentPosition();
	 * }
	 */

	@Override
	public void update()
	{
		super.update();
		attachments.update();
	}

	// TileEntityContainer stuff

	@Override
	public boolean onBlockActivated( BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY,
			float hitZ )
	{
		if( !getWorld().isRemote )
			if( canHaveLock() && !getLock().isEmpty() )
			{
				// if( state.getValue( BlockHorizontal.FACING ) == side )
				// {
				final ItemStack stack = player.getHeldItem( hand );
				if( !( stack.getItem() instanceof IKey ) && !canPlayerUseContainer( player ) )
					( (ILock)getLock().getItem() ).applyEffects( getLock(), this, player, EnumLockInteraction.OPEN );
				// }
				return false;
			}
		return super.onBlockActivated( pos, state, player, hand, side, hitX, hitY, hitZ );
	}

	@Override
	public boolean canPlayerUseContainer( EntityPlayer player )
	{
		return super.canPlayerUseContainer( player ) && ( getLock().isEmpty() || canUse( player ) );
	}

	/*
	 * @Override
	 * protected void onBlockPlacedBeforeCheckingConnections( EntityLivingBase player, ItemStack stack )
	 * {
	 * super.onBlockPlacedBeforeCheckingConnections( player, stack );
	 * //if( canHaveMaterial() )
	 * material = EnumReinforced.byMetadata( stack.getMetadata() );
	 * }
	 */

	/*
	 * @Override
	 * public ItemStack onPickBlock( ItemStack block, RayTraceResult target )
	 * {
	 * if( !canHaveMaterial() )
	 * return block;
	 * return getMaterial().setMaterial( block );
	 * }
	 */

	@Override
	public void dropContents()
	{
		super.dropContents();
		if( !canHaveLock() )
			return;
		final ItemStack stack = getLock();
		if( !stack.isEmpty() )
			getWorld().spawnEntity( new EntityItem( getWorld(), pos.getX(), pos.getY(), pos.getZ(), stack ) );
		setLock( ItemStack.EMPTY );
	}

	/*
	 * @Override
	 *
	 * @SideOnly( Side.CLIENT )
	 * public void onBlockRenderAsItem( ItemStack stack )
	 * {
	 * super.onBlockRenderAsItem( stack );
	 * // if( canHaveMaterial() )
	 * material = EnumReinforced.byMetadata( stack.getMetadata() );
	 * }
	 */

	// TileEntityConnactable stuff

	@Override
	protected boolean isAccessible()
	{
		return getLock() == null;
	}

	@Override
	public boolean canConnect( TileEntityConnectable connectable )
	{
		if( !( connectable instanceof TileEntityLockable ) )
			return false;
		final TileEntityLockable lockable = (TileEntityLockable)connectable;
		return super.canConnect( connectable ) && material == lockable.material && getLock() == ItemStack.EMPTY
				&& lockable.getLock() == ItemStack.EMPTY;
	}

	// ILockable implementation

	@Override
	public ItemStack getLock()
	{
		return ( (TileEntityLockable)getMainTileEntity() ).getLockInternal();
	}

	@Override
	public boolean isLockValid( ItemStack lock )
	{
		return lock.isEmpty() || canHaveLock() && lock.getItem() instanceof ILock;
	}

	@Override
	public void setLock( ItemStack lock )
	{
		if( !isLockValid( lock ) )
			throw new InvalidParameterException( "Can't set lock to " + lock + "." );

		final TileEntityLockable main = (TileEntityLockable)getMainTileEntity();
		main.setLockInternal( lock );
		main.markForUpdate();
		markDirty();
	}

	@Override
	public boolean canUse( EntityPlayer player )
	{
		return getMainTileEntity().getPlayersUsing() > 0;
	}

	@Override
	public void useUnlocked( EntityPlayer player )
	{
		player.openGui( ModInfo.modId, EnumGui.GENERAL.getGuiID(), getWorld(), pos.getX(), pos.getY(), pos.getZ() );
	}

	@Override
	public void applyTrigger()
	{
		setPowered( true );
	}

	// Trigger enchantment related

	/** Returns if the chest is emitting redstone. */
	public boolean isPowered()
	{
		return ( (TileEntityLockable)getMainTileEntity() ).powered;
	}

	/**
	 * Sets if the chest is emitting redstone.
	 * Updates all nearby blocks to make sure they notice it.
	 */
	public void setPowered( boolean powered )
	{

		final TileEntityLockable chest = (TileEntityLockable)getMainTileEntity();
		if( chest != this )
		{
			chest.setPowered( powered );
			return;
		}

		if( this.powered == powered )
			return;
		this.powered = powered;

		final Block block = getBlockType();
		// Schedule a block update to turn the redstone signal back off.
		if( powered )
			getWorld().scheduleBlockUpdate( pos, block, 10, 1 );

		// Notify nearby blocks
		getWorld().notifyNeighborsOfStateChange( pos, block, true );
		// this.getWorld().notifyNeighborsOfStateChange( pos.add( 1, 0, 0 ), block );
		// this.getWorld().notifyNeighborsOfStateChange( pos.add( -1, 0, 0 ), block );
		// this.getWorld().notifyNeighborsOfStateChange( pos.add( 0, 1, 0 ), block );
		// this.getWorld().notifyNeighborsOfStateChange( pos.add( 0, -1, 0 ), block );
		// this.getWorld().notifyNeighborsOfStateChange( pos.add( 0, 0, 1 ), block );
		// this.getWorld().notifyNeighborsOfStateChange( pos.add( 0, 0, -1 ), block );

		// Notify nearby blocks of adjacent chest
		/*
		 * if( isConnected() && getConnected() == EnumFacing.EAST )
		 * {
		 * this.getWorld().notifyNeighborsOfStateChange( pos.add( 2, 0, 0 ), block );
		 * this.getWorld().notifyNeighborsOfStateChange( pos.add( 1, 1, 0 ), block );
		 * this.getWorld().notifyNeighborsOfStateChange( pos.add( 1, -1, 0 ), block );
		 * this.getWorld().notifyNeighborsOfStateChange( pos.add( 1, 0, 1 ), block );
		 * this.getWorld().notifyNeighborsOfStateChange( pos.add( 1, 0, -1 ), block );
		 * }
		 * if( isConnected() && getConnected() == EnumFacing.SOUTH )
		 * {
		 * this.getWorld().notifyNeighborsOfStateChange( pos.add( 0, 0, 2 ), block );
		 * this.getWorld().notifyNeighborsOfStateChange( pos.add( 1, 0, 1 ), block );
		 * this.getWorld().notifyNeighborsOfStateChange( pos.add( -1, 0, 1 ), block );
		 * this.getWorld().notifyNeighborsOfStateChange( pos.add( 0, 1, 1 ), block );
		 * this.getWorld().notifyNeighborsOfStateChange( pos.add( 0, -1, 1 ), block );
		 * }
		 */
	}

	// TileEntity synchronization

	@Override
	public NBTTagCompound getUpdateTag()
	{
		final NBTTagCompound compound = super.getUpdateTag();
		if( material != null )
			compound.setString( "material", material.getName() );
		if( canHaveLock() )
		{
			final ItemStack lock = getLockInternal();
			if( !lock.isEmpty() )
				compound.setTag( "lock", lock.writeToNBT( new NBTTagCompound() ) );
		}
		return compound;
	}

	@Override
	public void handleUpdateTag( NBTTagCompound compound )
	{
		super.handleUpdateTag( compound );
		if( compound.hasKey( "material" ) )
			material = EnumReinforced.byName( compound.getString( "material" ) );
		if( canHaveLock() )
		{
			ItemStack itemStack = ItemStack.EMPTY;
			if( compound.hasKey( "lock" ) )
				itemStack = new ItemStack( compound.getCompoundTag( "lock" ) );
			setLockInternal( itemStack );
		}
	}

	// Reading from / writing to NBT

	@Override
	public NBTTagCompound writeToNBT( NBTTagCompound compound )
	{
		super.writeToNBT( compound );
		if( material != null )
			compound.setString( "material", material.getName() );
		if( canHaveLock() )
		{
			final ItemStack lock = getLockInternal();
			if( !lock.isEmpty() )
				compound.setTag( "lock", lock.writeToNBT( new NBTTagCompound() ) );
		}
		return compound;
	}

	@Override
	public void readFromNBT( NBTTagCompound compound )
	{
		super.readFromNBT( compound );
		if( compound.hasKey( "material" ) )
			material = EnumReinforced.byName( compound.getString( "material" ) );
		if( canHaveLock() )
		{
			ItemStack itemStack = ItemStack.EMPTY;
			if( compound.hasKey( "lock" ) )
				itemStack = new ItemStack( compound.getCompoundTag( "lock" ) );
			setLockInternal( itemStack );
		}
	}
}
