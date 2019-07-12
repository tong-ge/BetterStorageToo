package io.github.tehstoneman.betterstorage.common.tileentity;

import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.config.BetterStorageConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TileEntityReinforcedChest extends TileEntityLockable
{
	protected int ticksSinceSync;

	public TileEntityReinforcedChest()
	{
		super( BetterStorageTileEntityTypes.REINFORCED_CHEST );
	}

	@Override
	@OnlyIn( Dist.CLIENT )
	public AxisAlignedBB getRenderBoundingBox()
	{
		return new AxisAlignedBB( pos.add( -1, 0, -1 ), pos.add( 2, 2, 2 ) );
	}

	@Override
	public int getColumns()
	{
		return BetterStorageConfig.COMMON.reinforcedColumns.get();
	}

	/*
	 * =====================
	 * TileEntityConnectable
	 * =====================
	 */

	/*
	 * public EnumConnectedType getChestType()
	 * {
	 * final IBlockState blockState = hasWorld() ? getBlockState() : BetterStorageBlocks.REINFORCED_CHEST.getDefaultState();
	 * return blockState.has( BlockLockable.TYPE ) ? blockState.get( BlockLockable.TYPE ) : EnumConnectedType.SINGLE;
	 * }
	 */

	/*
	 * @Override
	 * public BlockPos getConnected()
	 * {
	 * if( isConnected() )
	 * return getPos().offset( BlockReinforcedChest.getDirectionToAttached( getBlockState() ) );
	 * return pos;
	 * }
	 */

	/*
	 * @Override
	 * public boolean isConnected()
	 * {
	 * return getChestType() != EnumConnectedType.SINGLE;
	 * }
	 */

	/*
	 * @Override
	 * public boolean isMain()
	 * {
	 * return getChestType() != EnumConnectedType.SLAVE;
	 * }
	 */

	@Override
	protected String getConnectableName()
	{
		return ModInfo.containerReinforcedChest;
	}

	/*
	 * ==================
	 * TileEntityLockable
	 * ==================
	 */

	@Override
	public void setAttachmentPosition()
	{
		// final double x = !isConnected() ? 8 : getOrientation() == EnumFacing.WEST || getOrientation() == EnumFacing.SOUTH ? 0 : 16;
		// lockAttachment.setBox( x, 6.5, 0.5, 7, 7, 1 );
	}

	@Override
	public ITextComponent getName()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BlockPos getConnected()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isConnected()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isMain()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Container createMenu( int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_ )
	{
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * =========
	 * ITickable
	 * =========
	 */

	/*
	 * @Override
	 * public void tick()
	 * {
	 * final int i = pos.getX();
	 * final int j = pos.getY();
	 * final int k = pos.getZ();
	 * ++ticksSinceSync;
	 * if( !world.isRemote && numPlayersUsing != 0 && ( ticksSinceSync + i + j + k ) % 200 == 0 )
	 * {
	 * numPlayersUsing = 0;
	 * final float f = 5.0F;
	 * 
	 * for( final EntityPlayer entityplayer : world.getEntitiesWithinAABB( EntityPlayer.class,
	 * new AxisAlignedBB( i - 5.0F, j - 5.0F, k - 5.0F, i + 1 + 5.0F, j + 1 + 5.0F, k + 1 + 5.0F ) ) )
	 * if( entityplayer.openContainer instanceof ContainerBetterStorage )
	 * ++numPlayersUsing;
	 * }
	 * 
	 * prevLidAngle = lidAngle;
	 * final float f1 = 0.1F;
	 * if( numPlayersUsing > 0 && lidAngle == 0.0F )
	 * playSound( SoundEvents.BLOCK_CHEST_OPEN );
	 * 
	 * if( numPlayersUsing == 0 && lidAngle > 0.0F || numPlayersUsing > 0 && lidAngle < 1.0F )
	 * {
	 * final float f2 = lidAngle;
	 * if( numPlayersUsing > 0 )
	 * lidAngle += 0.1F;
	 * else
	 * lidAngle -= 0.1F;
	 * 
	 * if( lidAngle > 1.0F )
	 * lidAngle = 1.0F;
	 * 
	 * final float f3 = 0.5F;
	 * if( lidAngle < 0.5F && f2 >= 0.5F )
	 * playSound( SoundEvents.BLOCK_CHEST_CLOSE );
	 * 
	 * if( lidAngle < 0.0F )
	 * lidAngle = 0.0F;
	 * }
	 * }
	 */

	/*
	 * =========
	 * IChestLid
	 * =========
	 */

	/*
	 * @Override
	 * protected void playSound( SoundEvent soundIn )
	 * {
	 * final EnumConnectedType chesttype = getBlockState().get( BlockLockable.TYPE );
	 * double x = pos.getX() + 0.5D;
	 * final double y = pos.getY() + 0.5D;
	 * double z = pos.getZ() + 0.5D;
	 * if( chesttype != EnumConnectedType.SINGLE )
	 * {
	 * final EnumFacing enumfacing = BlockReinforcedChest.getDirectionToAttached( getBlockState() );
	 * x += enumfacing.getXOffset() * 0.5D;
	 * z += enumfacing.getZOffset() * 0.5D;
	 * }
	 * 
	 * world.playSound( (EntityPlayer)null, x, y, z, soundIn, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F );
	 * }
	 */
}
