package io.github.tehstoneman.betterstorage.common.tileentity;

import java.security.InvalidParameterException;

import io.github.tehstoneman.betterstorage.api.lock.EnumLockInteraction;
import io.github.tehstoneman.betterstorage.api.lock.ILock;
import io.github.tehstoneman.betterstorage.api.lock.ILockable;
import io.github.tehstoneman.betterstorage.attachment.Attachments;
import io.github.tehstoneman.betterstorage.attachment.IHasAttachments;
import io.github.tehstoneman.betterstorage.attachment.LockAttachment;
import io.github.tehstoneman.betterstorage.common.block.BlockLockable.EnumReinforced;
import io.github.tehstoneman.betterstorage.utils.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class TileEntityLockable extends TileEntityConnectable implements ILockable, IHasAttachments
{
	private boolean			powered;

	public EnumReinforced	material;
	public LockAttachment	lockAttachment;

	protected Attachments	attachments	= new Attachments( this );

	protected ItemStack getLockInternal()
	{
		return canHaveLock() ? lockAttachment.getItem() : null;
	}

	protected void setLockInternal( ItemStack lock )
	{
		lockAttachment.setItem( lock );
	}

	public TileEntityLockable()
	{
		if( !canHaveLock() )
			return;
		lockAttachment = attachments.add( LockAttachment.class );
		lockAttachment.setScale( 0.5F, 1.5F );
		setAttachmentPosition();
	}

	public EnumReinforced getMaterial()
	{
		// if( !canHaveMaterial() ) return null;
		if( material == null )
			material = EnumReinforced.byMetadata( getBlockMetadata() );
		return material;
	}

	/*
	 * public boolean canHaveMaterial()
	 * {
	 * return getBlockMetadata() == EnumReinforced.SPECIAL.getMetadata();
	 * }
	 */

	public boolean canHaveLock()
	{
		return true;
	}

	public abstract void setAttachmentPosition();

	// Attachment points

	@Override
	public Attachments getAttachments()
	{
		return ( (TileEntityLockable)getMainTileEntity() ).attachments;
	}

	@Override
	public void setOrientation( EnumFacing orientation )
	{
		super.setOrientation( orientation );
		if( canHaveLock() )
			lockAttachment.setDirection( orientation );
	}

	@Override
	public void setConnected( EnumFacing connected )
	{
		super.setConnected( connected );
		if( canHaveLock() )
			setAttachmentPosition();
	}

	@Override
	public void update()
	{
		super.update();
		attachments.update();
	}

	// TileEntityContainer stuff

	@Override
	public boolean onBlockActivated( EntityPlayer player, int side, float hitX, float hitY, float hitZ )
	{
		if( !worldObj.isRemote && canHaveLock() && !canPlayerUseContainer( player ) )
			( (ILock)getLock().getItem() ).applyEffects( getLock(), this, player, EnumLockInteraction.OPEN );
		return super.onBlockActivated( player, side, hitX, hitY, hitZ );
	}

	@Override
	public boolean canPlayerUseContainer( EntityPlayer player )
	{
		return super.canPlayerUseContainer( player ) && ( getLock() == null || canUse( player ) );
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
		WorldUtils.dropStackFromBlock( this, getLock() );
		setLock( null );
	}

	@Override
	@SideOnly( Side.CLIENT )
	public void onBlockRenderAsItem( ItemStack stack )
	{
		super.onBlockRenderAsItem( stack );
		// if( canHaveMaterial() )
		material = EnumReinforced.byMetadata( stack.getMetadata() );
	}

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
		return super.canConnect( connectable ) && material == lockable.material && getLock() == null && lockable.getLock() == null;
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
		return lock == null || lock.getItem() instanceof ILock && canHaveLock();
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
		//openGui( player );
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
			worldObj.scheduleBlockUpdate( pos, block, 10, 1 );

		// Notify nearby blocks
		worldObj.notifyNeighborsOfStateChange( pos, block );
		worldObj.notifyNeighborsOfStateChange( pos.add( 1, 0, 0 ), block );
		worldObj.notifyNeighborsOfStateChange( pos.add( -1, 0, 0 ), block );
		worldObj.notifyNeighborsOfStateChange( pos.add( 0, 1, 0 ), block );
		worldObj.notifyNeighborsOfStateChange( pos.add( 0, -1, 0 ), block );
		worldObj.notifyNeighborsOfStateChange( pos.add( 0, 0, 1 ), block );
		worldObj.notifyNeighborsOfStateChange( pos.add( 0, 0, -1 ), block );

		// Notify nearby blocks of adjacent chest
		if( isConnected() && getConnected() == EnumFacing.EAST )
		{
			worldObj.notifyNeighborsOfStateChange( pos.add( 2, 0, 0 ), block );
			worldObj.notifyNeighborsOfStateChange( pos.add( 1, 1, 0 ), block );
			worldObj.notifyNeighborsOfStateChange( pos.add( 1, -1, 0 ), block );
			worldObj.notifyNeighborsOfStateChange( pos.add( 1, 0, 1 ), block );
			worldObj.notifyNeighborsOfStateChange( pos.add( 1, 0, -1 ), block );
		}
		if( isConnected() && getConnected() == EnumFacing.SOUTH )
		{
			worldObj.notifyNeighborsOfStateChange( pos.add( 0, 0, 2 ), block );
			worldObj.notifyNeighborsOfStateChange( pos.add( 1, 0, 1 ), block );
			worldObj.notifyNeighborsOfStateChange( pos.add( -1, 0, 1 ), block );
			worldObj.notifyNeighborsOfStateChange( pos.add( 0, 1, 1 ), block );
			worldObj.notifyNeighborsOfStateChange( pos.add( 0, -1, 1 ), block );
		}

	}

	// TileEntity synchronization

	@Override
	public NBTTagCompound getUpdateTag()
	{
		final NBTTagCompound compound = super.getUpdateTag();
		if( canHaveLock() )
		{
			final ItemStack lock = getLockInternal();
			if( lock != null )
				compound.setTag( "lock", lock.writeToNBT( new NBTTagCompound() ) );
		}
		return compound;
	}

	@Override
	public void handleUpdateTag( NBTTagCompound compound )
	{
		super.handleUpdateTag( compound );
		if( canHaveLock() )
			if( !compound.hasKey( "lock" ) )
				setLockInternal( null );
			else
				setLockInternal( ItemStack.loadItemStackFromNBT( compound.getCompoundTag( "lock" ) ) );
	}

	// Reading from / writing to NBT

	@Override
	public NBTTagCompound writeToNBT( NBTTagCompound compound )
	{
		super.writeToNBT( compound );
		if( canHaveLock() )
		{
			final ItemStack lock = getLockInternal();
			if( lock != null )
				compound.setTag( "lock", lock.writeToNBT( new NBTTagCompound() ) );
		}
		return compound;
	}

	@Override
	public void readFromNBT( NBTTagCompound compound )
	{
		super.readFromNBT( compound );
		if( canHaveLock() && compound.hasKey( "lock" ) )
			setLockInternal( ItemStack.loadItemStackFromNBT( compound.getCompoundTag( "lock" ) ) );
	}
}
