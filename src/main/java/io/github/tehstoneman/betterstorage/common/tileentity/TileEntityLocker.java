package io.github.tehstoneman.betterstorage.common.tileentity;

import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.api.EnumConnectedType;
import io.github.tehstoneman.betterstorage.common.block.BetterStorageBlocks;
import io.github.tehstoneman.betterstorage.common.block.BlockLockable;
import io.github.tehstoneman.betterstorage.common.block.BlockLocker;
import io.github.tehstoneman.betterstorage.common.block.BlockReinforcedChest;
import io.github.tehstoneman.betterstorage.common.inventory.ContainerBetterStorage;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TileEntityLocker extends TileEntityLockable
{
	protected int ticksSinceSync;

	public TileEntityLocker( TileEntityType< ? > tileEntityTypeIn )
	{
		super( tileEntityTypeIn );
	}

	public TileEntityLocker()
	{
		this( BetterStorageTileEntityTypes.LOCKER );
	}

	@Override
	@OnlyIn( Dist.CLIENT )
	public AxisAlignedBB getRenderBoundingBox()
	{
		return new AxisAlignedBB( pos.add( -1, 0, -1 ), pos.add( 2, 2, 2 ) );
	}

	// private static final EnumFacing[] neighbors = { EnumFacing.DOWN, EnumFacing.UP };

	// public boolean mirror = false;

	/*
	 * @Override
	 * public boolean canHaveLock()
	 * {
	 * return false;
	 * }
	 */

	/*
	 * @Override
	 * public void setAttachmentPosition()
	 * {}
	 */

	/*
	 * @Override
	 * public EnumFacing[] getPossibleNeighbors()
	 * {
	 * return neighbors;
	 * }
	 */

	/*
	 * @Override
	 * protected String getConnectableName()
	 * {
	 * return ModInfo.containerLocker;
	 * }
	 */

	/*
	 * @Override
	 * public boolean canConnect( TileEntityConnectable connectable )
	 * {
	 * if( !( connectable instanceof TileEntityLocker ) )
	 * return false;
	 * final TileEntityLocker locker = (TileEntityLocker)connectable;
	 * return super.canConnect( connectable ) && mirror == locker.mirror;
	 * }
	 */

	/*
	 * @Override
	 * public boolean onBlockActivated( BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY,
	 * float hitZ )
	 * {
	 * // if( getOrientation() != side ) return false;
	 * return super.onBlockActivated( pos, state, player, hand, side, hitX, hitY, hitZ );
	 * }
	 */

	/*
	 * @Override
	 * public void onBlockDestroyed()
	 * {
	 * super.onBlockDestroyed();
	 *
	 * // Don't drop an empty cardboard box in creative.
	 *
	 * if( !brokenInCreative )
	 * {
	 * final ItemStack stack = this instanceof TileEntityReinforcedLocker ? new ItemStack( BetterStorageBlocks.REINFORCED_LOCKER, 1, material.getMetadata()
	 * ) : new ItemStack( BetterStorageBlocks.LOCKER );
	 * final EntityItem entityItem = new EntityItem( world, pos.getX(), pos.getY(), pos.getZ(), stack );
	 * world.spawnEntity( entityItem );
	 * }
	 *
	 * }
	 */

	// TileEntity synchronization

	/*
	 * @Override
	 * public NBTTagCompound getUpdateTag()
	 * {
	 * final NBTTagCompound compound = super.getUpdateTag();
	 * compound.setBoolean( "mirror", mirror );
	 * return compound;
	 * }
	 */

	/*
	 * @Override
	 * public void onDataPacket( NetworkManager net, SPacketUpdateTileEntity packet )
	 * {
	 * super.onDataPacket( net, packet );
	 * final NBTTagCompound compound = packet.getNbtCompound();
	 * mirror = compound.getBoolean( "mirror" );
	 * setAttachmentPosition();
	 * }
	 */

	// Reading from / writing to NBT

	/*
	 * @Override
	 * public void readFromNBT( NBTTagCompound compound )
	 * {
	 * super.readFromNBT( compound );
	 * mirror = compound.getBoolean( "mirror" );
	 * setAttachmentPosition();
	 * }
	 */

	/*
	 * @Override
	 * public NBTTagCompound writeToNBT( NBTTagCompound compound )
	 * {
	 * super.writeToNBT( compound );
	 * compound.setBoolean( "mirror", mirror );
	 * return compound;
	 * }
	 */

	@Override
	public void tick()
	{
		final int i = pos.getX();
		final int j = pos.getY();
		final int k = pos.getZ();
		++ticksSinceSync;
		if( !world.isRemote && numPlayersUsing != 0 && ( ticksSinceSync + i + j + k ) % 200 == 0 )
		{
			numPlayersUsing = 0;
			final float f = 5.0F;

			for( final EntityPlayer entityplayer : world.getEntitiesWithinAABB( EntityPlayer.class,
					new AxisAlignedBB( i - 5.0F, j - 5.0F, k - 5.0F, i + 1 + 5.0F, j + 1 + 5.0F, k + 1 + 5.0F ) ) )
				if( entityplayer.openContainer instanceof ContainerBetterStorage )
					++numPlayersUsing;
		}

		prevLidAngle = lidAngle;
		final float f1 = 0.1F;
		if( numPlayersUsing > 0 && lidAngle == 0.0F )
			playSound( SoundEvents.BLOCK_CHEST_OPEN );

		if( numPlayersUsing == 0 && lidAngle > 0.0F || numPlayersUsing > 0 && lidAngle < 1.0F )
		{
			final float f2 = lidAngle;
			if( numPlayersUsing > 0 )
				lidAngle += 0.1F;
			else
				lidAngle -= 0.1F;

			if( lidAngle > 1.0F )
				lidAngle = 1.0F;

			final float f3 = 0.5F;
			if( lidAngle < 0.5F && f2 >= 0.5F )
				playSound( SoundEvents.BLOCK_CHEST_CLOSE );

			if( lidAngle < 0.0F )
				lidAngle = 0.0F;
		}
	}

	@Override
	public void setAttachmentPosition()
	{
		// TODO Auto-generated method stub
	}

	public EnumConnectedType getLockerType()
	{
		final IBlockState blockState = hasWorld() ? getBlockState() : BetterStorageBlocks.LOCKER.getDefaultState();
		return blockState.has( BlockLockable.TYPE ) ? blockState.get( BlockLockable.TYPE ) : EnumConnectedType.SINGLE;
	}

	@Override
	public BlockPos getConnected()
	{
		if( isConnected() )
			return getPos().offset( BlockLocker.getDirectionToAttached( getBlockState() ) );
		return pos;
	}

	@Override
	public boolean isConnected()
	{
		return getLockerType() != EnumConnectedType.SINGLE;
	}

	@Override
	public boolean isMain()
	{
		return getLockerType() != EnumConnectedType.SLAVE;
	}

	@Override
	protected String getConnectableName()
	{
		return ModInfo.containerLocker;
	}

	@Override
	protected void playSound( SoundEvent soundIn )
	{
		final EnumConnectedType chesttype = getBlockState().get( BlockLockable.TYPE );
		double x = pos.getX() + 0.5D;
		final double y = pos.getY() + 0.5D;
		double z = pos.getZ() + 0.5D;
		if( chesttype != EnumConnectedType.SINGLE )
		{
			final EnumFacing enumfacing = BlockLocker.getDirectionToAttached( getBlockState() );
			x += enumfacing.getXOffset() * 0.5D;
			z += enumfacing.getZOffset() * 0.5D;
		}

		world.playSound( (EntityPlayer)null, x, y, z, soundIn, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F );
	}
}
