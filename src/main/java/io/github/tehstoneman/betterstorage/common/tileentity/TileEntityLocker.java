package io.github.tehstoneman.betterstorage.common.tileentity;

import net.minecraft.tileentity.IChestLid;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TileEntityLocker extends TileEntity implements IChestLid, ITickable // TileEntityLockable
{
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
		// TODO Auto-generated method stub

	}

	@Override
	public float getLidAngle( float partialTicks )
	{
		// TODO Auto-generated method stub
		return 0;
	}
}
