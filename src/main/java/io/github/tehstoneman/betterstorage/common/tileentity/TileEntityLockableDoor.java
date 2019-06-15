package io.github.tehstoneman.betterstorage.common.tileentity;

import io.github.tehstoneman.betterstorage.api.lock.ILock;
import io.github.tehstoneman.betterstorage.api.lock.ILockable;
import io.github.tehstoneman.betterstorage.attachment.Attachments;
import io.github.tehstoneman.betterstorage.attachment.IHasAttachments;
import io.github.tehstoneman.betterstorage.attachment.LockAttachment;
import io.github.tehstoneman.betterstorage.utils.WorldUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;

public class TileEntityLockableDoor extends TileEntity implements ILockable, IHasAttachments
{

	private final Attachments	attachments	= new Attachments( this );
	public LockAttachment		lockAttachment;
	// public EnumFacing orientation = EnumFacing.NORTH;

	private boolean				powered		= false;
	private final boolean		swing		= false;

	public boolean				isOpen		= false;
	public boolean				isMirrored	= false;

	public TileEntityLockableDoor( TileEntityType< ? > tileEntityTypeIn )
	{
		super( tileEntityTypeIn );
		lockAttachment = attachments.add( LockAttachment.class );
		lockAttachment.setScale( 0.5F, 1.5F );
	}

	@Override
	// @SideOnly( Side.CLIENT )
	public AxisAlignedBB getRenderBoundingBox()
	{
		return WorldUtils.getAABB( this, 0, 0, 0, 0, 1, 0 );
	}

	/*
	 * private void updateLockPosition()
	 * {
	 * // Maybe we should use the orientation that the attachment has by itself.
	 * switch( orientation )
	 * {
	 * case WEST:
	 * if( isOpen )
	 * lockAttachment.setBox( 12.5, -1.5, 1.5, 5, 6, 3 );
	 * else
	 * lockAttachment.setBox( 1.5, -1.5, 12.5, 3, 6, 5 );
	 * break;
	 * case EAST:
	 * if( isOpen )
	 * lockAttachment.setBox( 3.5, -1.5, 14.5, 5, 6, 3 );
	 * else
	 * lockAttachment.setBox( 14.5, -1.5, 3.5, 3, 6, 5 );
	 * break;
	 * case SOUTH:
	 * if( isOpen )
	 * lockAttachment.setBox( 1.5, -1.5, 3.5, 3, 6, 5 );
	 * else
	 * lockAttachment.setBox( 12.5, -1.5, 14.5, 5, 6, 3 );
	 * break;
	 * default:
	 * if( isOpen )
	 * lockAttachment.setBox( 14.5, -1.5, 12.5, 3, 6, 5 );
	 * else
	 * lockAttachment.setBox( 3.5, -1.5, 1.5, 5, 6, 3 );
	 * break;
	 * }
	 * }
	 */

	@Override
	public Attachments getAttachments()
	{
		return attachments;
	}

	@Override
	public ItemStack getLock()
	{
		return lockAttachment.getItem();
	}

	@Override
	public boolean isLockValid( ItemStack lock )
	{
		return lock.isEmpty() || lock.getItem() instanceof ILock;
	}

	/*
	 * @Override
	 * public void setLock( ItemStack lock )
	 * {
	 * // Turn it back into a normal iron door
	 * if( lock.isEmpty() )
	 * lockAttachment.setItem( ItemStack.EMPTY );
	 * else
	 * setLockWithUpdate( lock );
	 * 
	 * }
	 */

	/*
	 * public void setLockWithUpdate( ItemStack lock )
	 * {
	 * lockAttachment.setItem( lock );
	 * updateLockPosition();
	 * final IBlockState blockState = world.getBlockState( pos );
	 * world.notifyBlockUpdate( pos, blockState, blockState, 3 );
	 * // world.markBlockForUpdate(pos);
	 * markDirty();
	 * }
	 */

	/*
	 * @Override
	 * public boolean canUse( EntityPlayer player )
	 * {
	 * return false;
	 * }
	 */

	/*
	 * @Override
	 * public void useUnlocked( EntityPlayer player )
	 * {
	 * final IBlockState state = world.getBlockState( pos );
	 * 
	 * // state = state.cycleProperty( BlockDoor.OPEN );
	 * world.setBlockState( pos, state, 10 );
	 * world.markBlockRangeForRenderUpdate( pos, pos );
	 * // world.playSound( (EntityPlayer)null, pos, state.getValue( BlockDoor.OPEN ).booleanValue() ? SoundEvents.BLOCK_IRON_DOOR_OPEN :
	 * // SoundEvents.BLOCK_IRON_DOOR_CLOSE, SoundCategory.BLOCKS, 1, 1 );
	 * // world.playEvent( player, state.getValue( BlockDoor.OPEN ).booleanValue() ? BlockLockableDoor.getOpenSound() : BlockLockableDoor.getCloseSound(), pos,
	 * // 0 );
	 * 
	 * isOpen = !isOpen;
	 * // worldObj.addBlockEvent(xCoord, yCoord, zCoord, getBlockType(), 0, isOpen ? 1 : 0);
	 * updateLockPosition();
	 * }
	 */

	@Override
	public void applyTrigger()
	{
		setPowered( true );
	}

	/*
	 * public boolean onBlockActivated( World world, BlockPos pos, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ )
	 * {
	 * if( !world.isRemote )
	 * {
	 * Logger.getLogger( ModInfo.modId ).info( "Block Acticated" );
	 * if( canUse( player ) )
	 * useUnlocked( player );
	 * else
	 * if( !getLock().isEmpty() )
	 * ( (ILock)getLock().getItem() ).applyEffects( getLock(), this, player, EnumLockInteraction.OPEN );
	 * }
	 * return false;
	 * }
	 */

	/*
	 * @Override
	 * public boolean receiveClientEvent( int eventID, int par )
	 * {
	 * // worldObj.playAuxSFX(1003, xCoord, yCoord, zCoord, 0);
	 * swing = true;
	 * isOpen = par == 1;
	 * updateLockPosition();
	 * world.markBlockRangeForRenderUpdate( pos, pos );
	 * return true;
	 * }
	 */

	/*
	 * @Override
	 * public void updateEntity()
	 * {
	 * attachments.update();
	 * }
	 */

	public boolean isPowered()
	{
		return powered;
	}

	public void setPowered( boolean powered )
	{

		if( this.powered == powered )
			return;
		this.powered = powered;

		// if (powered) worldObj.scheduleBlockUpdate(xCoord, yCoord, zCoord, getBlockType(), 10);
		// WorldUtils.notifyBlocksAround(worldObj, xCoord, yCoord, zCoord);
		// WorldUtils.notifyBlocksAround(worldObj, xCoord, yCoord + 1, zCoord);
	}

	@Override
	public void setLock( ItemStack lock )
	{
		// TODO Auto-generated method stub

	}

	/*
	 * @Override
	 * public boolean shouldRefresh( World world, BlockPos pos, IBlockState oldState, IBlockState newSate )
	 * {
	 * return oldState.getBlock() != newSate.getBlock();
	 * }
	 */

	// TileEntity synchronization

	/*
	 * @Override
	 * public NBTTagCompound getUpdateTag()
	 * {
	 * final NBTTagCompound compound = new NBTTagCompound();
	 * 
	 * compound.setBoolean( "isOpen", isOpen );
	 * compound.setBoolean( "isMirrored", isMirrored );
	 * compound.setByte( "orientation", (byte)orientation.ordinal() );
	 * if( !lockAttachment.getItem().isEmpty() )
	 * compound.setTag( "lock", lockAttachment.getItem().writeToNBT( new NBTTagCompound() ) );
	 * 
	 * // writeToNBT( compound );
	 * return compound;
	 * }
	 */

	/*
	 * @Override
	 * public void handleUpdateTag( NBTTagCompound compound )
	 * {
	 * 
	 * Logger.getLogger( ModInfo.modId ).info( "Compound : " + compound );
	 * if( !compound.hasKey( "lock" ) )
	 * lockAttachment.setItem( null );
	 * else
	 * {
	 * ItemStack lock = new ItemStack( compound.getCompoundTag( "lock" ) );
	 * Logger.getLogger( ModInfo.modId ).info( "Lock : " + lock );
	 * lockAttachment.setItem( new ItemStack( compound.getCompoundTag( "lock" ) ) );
	 * }
	 * orientation = EnumFacing.getFront( compound.getByte( "orientation" ) );
	 * isOpen = compound.getBoolean( "isOpen" );
	 * isMirrored = compound.getBoolean( "isMirrored" );
	 * updateLockPosition();
	 * 
	 * // readFromNBT( compound );
	 * }
	 */

	// Reading from / writing to NBT

	/*
	 * @Override
	 * public NBTTagCompound writeToNBT( NBTTagCompound compound )
	 * {
	 * super.writeToNBT( compound );
	 * compound.setBoolean( "isOpen", isOpen );
	 * compound.setBoolean( "isMirrored", isMirrored );
	 * compound.setByte( "orientation", (byte)orientation.ordinal() );
	 * if( !lockAttachment.getItem().isEmpty() )
	 * compound.setTag( "lock", lockAttachment.getItem().writeToNBT( new NBTTagCompound() ) );
	 * return compound;
	 * }
	 */

	/*
	 * @Override
	 * public void updateEntity()
	 * {
	 * attachments.update();
	 * }
	 */

	/*
	 * @Override
	 * public void readFromNBT( NBTTagCompound compound )
	 * {
	 * super.readFromNBT( compound );
	 * isOpen = compound.getBoolean( "isOpen" );
	 * isMirrored = compound.getBoolean( "isMirrored" );
	 * orientation = EnumFacing.getFront( compound.getByte( "orientation" ) );
	 * if( compound.hasKey( "lock" ) )
	 * lockAttachment.setItem( new ItemStack( compound.getCompoundTag( "lock" ) ) );
	 * updateLockPosition();
	 * }
	 */
}
