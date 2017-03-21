package io.github.tehstoneman.betterstorage.common.tileentity;

import com.google.common.collect.ImmutableMap;

import io.github.tehstoneman.betterstorage.BetterStorage;
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
import net.minecraftforge.client.model.animation.Animation;
import net.minecraftforge.common.animation.Event;
import net.minecraftforge.common.animation.ITimeValue;
import net.minecraftforge.common.animation.TimeValues.VariableValue;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.model.animation.CapabilityAnimation;
import net.minecraftforge.common.model.animation.IAnimationStateMachine;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityLocker extends TileEntityLockable
{
	private static final EnumFacing[]	neighbors	= { EnumFacing.DOWN, EnumFacing.UP };

	public boolean						mirror		= false;
	
	public void handleEvents(float time, Iterable<Event> pastEvents) {}
	
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
}
