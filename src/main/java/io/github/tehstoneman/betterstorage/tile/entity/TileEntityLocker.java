package io.github.tehstoneman.betterstorage.tile.entity;

import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.misc.Resources;
import io.github.tehstoneman.betterstorage.utils.DirectionUtils;
import io.github.tehstoneman.betterstorage.utils.WorldUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityLocker extends TileEntityLockable
{
	private static final EnumFacing[]	neighbors	= { EnumFacing.DOWN, EnumFacing.UP };

	public boolean						mirror		= false;

	@Override
	@SideOnly( Side.CLIENT )
	public AxisAlignedBB getRenderBoundingBox()
	{
		return WorldUtils.getAABB( this, 0, 0, 0, 0, 1, 0 );
	}

	@SideOnly( Side.CLIENT )
	public ResourceLocation getResource()
	{
		return isConnected() ? Resources.textureLockerLarge : Resources.textureLocker;
	}

	@Override
	public boolean canHaveLock()
	{
		return false;
	}

	@Override
	public boolean canHaveMaterial()
	{
		return false;
	}

	@Override
	public void setAttachmentPosition()
	{}

	@Override
	public EnumFacing[] getPossibleNeighbors()
	{
		return neighbors;
	}

	@Override
	protected String getConnectableName()
	{
		return ModInfo.containerLocker;
	}

	@Override
	public boolean canConnect( TileEntityConnectable connectable )
	{
		if( !( connectable instanceof TileEntityLocker ) )
			return false;
		final TileEntityLocker locker = (TileEntityLocker)connectable;
		return super.canConnect( connectable ) && mirror == locker.mirror;
	}

	@Override
	public void onBlockPlacedBeforeCheckingConnections( EntityLivingBase player, ItemStack stack )
	{
		super.onBlockPlacedBeforeCheckingConnections( player, stack );
		final double angle = DirectionUtils.getRotation( getOrientation().getOpposite() );
		final double yaw = ( player.rotationYaw % 360 + 360 ) % 360;
		mirror = DirectionUtils.angleDifference( angle, yaw ) > 0;
		setAttachmentPosition();
	}

	@Override
	public boolean onBlockActivated( EntityPlayer player, int side, float hitX, float hitY, float hitZ )
	{
		if( getOrientation().ordinal() != side )
			return true;
		return super.onBlockActivated( player, side, hitX, hitY, hitZ );
	}

	// TileEntity synchronization

	@Override
	public NBTTagCompound getUpdateTag()
	{
		final NBTTagCompound compound = super.getUpdateTag();
		compound.setBoolean( "mirror", mirror );
		return compound;
	}

	@Override
	public void onDataPacket( NetworkManager net, SPacketUpdateTileEntity packet )
	{
		super.onDataPacket( net, packet );
		final NBTTagCompound compound = packet.getNbtCompound();
		mirror = compound.getBoolean( "mirror" );
		setAttachmentPosition();
	}

	// Reading from / writing to NBT

	@Override
	public void readFromNBT( NBTTagCompound compound )
	{
		super.readFromNBT( compound );
		mirror = compound.getBoolean( "mirror" );
		setAttachmentPosition();
	}

	@Override
	public NBTTagCompound writeToNBT( NBTTagCompound compound )
	{
		super.writeToNBT( compound );
		compound.setBoolean( "mirror", mirror );
		return compound;
	}

	@Override
	public ItemStack removeStackFromSlot( int index )
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getField( int id )
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setField( int id, int value )
	{
		// TODO Auto-generated method stub

	}

	@Override
	public int getFieldCount()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void clear()
	{
		// TODO Auto-generated method stub

	}
}
