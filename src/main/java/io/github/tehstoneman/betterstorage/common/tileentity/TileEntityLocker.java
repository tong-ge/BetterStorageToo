package io.github.tehstoneman.betterstorage.common.tileentity;

import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.client.renderer.Resources;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityLocker extends TileEntityLockable
{
	private static final EnumFacing[]	neighbors	= { EnumFacing.DOWN, EnumFacing.UP };

	public boolean						mirror		= false;

	// private final TimeValues.VariableValue open;
	// private final IAnimationStateMachine asm;

	public TileEntityLocker()
	{
		/*
		 * if(FMLCommonHandler.instance().getSide()==Side.CLIENT)
		 * {
		 * open = new TimeValues.VariableValue( 0 );
		 * asm = ModelLoaderRegistry.loadASM( new ResourceLocation(ModInfo.modId, "asms/block/locker.json" ), ImmutableMap.of("open", open) );
		 * }
		 * else
		 * {
		 * open = null;
		 * asm = null;
		 * }
		 */
	}

	@Override
	public boolean hasCapability( Capability< ? > capability, EnumFacing facing )
	{
		/*
		 * if( capability == CapabilityAnimation.ANIMATION_CAPABILITY )
		 * return true;
		 */
		return super.hasCapability( capability, facing );
	}

	@Override
	public <T> T getCapability( Capability< T > capability, EnumFacing facing )
	{
		/*
		 * if( capability == CapabilityAnimation.ANIMATION_CAPABILITY )
		 * return CapabilityAnimation.ANIMATION_CAPABILITY.cast( asm );
		 */
		return super.getCapability( capability, facing );
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
