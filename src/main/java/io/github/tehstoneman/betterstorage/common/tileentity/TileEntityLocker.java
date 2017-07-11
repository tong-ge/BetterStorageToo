package io.github.tehstoneman.betterstorage.common.tileentity;

import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.client.renderer.Resources;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityLocker extends TileEntityLockable
{
	private static final EnumFacing[]	neighbors	= { EnumFacing.DOWN, EnumFacing.UP };

	public boolean						mirror		= false;

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
	public boolean onBlockActivated( BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY,
			float hitZ )
	{
		if( getOrientation() != side )
			return false;
		return super.onBlockActivated( pos, state, player, hand, side, hitX, hitY, hitZ );
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
}
