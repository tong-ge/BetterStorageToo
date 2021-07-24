package io.github.tehstoneman.betterstorage.network;

import java.util.function.Supplier;

import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityCrate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

public class UpdateCrateMessage
{
	private final BlockPos	pos;
	private final int		numCrates;
	private final int		capacity;

	public UpdateCrateMessage( BlockPos pos, int numCrates, int capacity )
	{
		this.pos = pos;
		this.numCrates = numCrates;
		this.capacity = capacity;
	}

	public static UpdateCrateMessage decode( PacketBuffer buffer )
	{
		final BlockPos pos = buffer.readBlockPos();
		final int numCrates = buffer.readInt();
		final int capacity = buffer.readInt();
		return new UpdateCrateMessage( pos, numCrates, capacity );
	}

	public static void encode( UpdateCrateMessage message, PacketBuffer buffer )
	{
		buffer.writeBlockPos( message.pos );
		buffer.writeInt( message.numCrates );
		buffer.writeInt( message.capacity );
	}

	public static void handle( UpdateCrateMessage message, Supplier< NetworkEvent.Context > ctx )
	{
		ctx.get().enqueueWork( () ->
		{
			if( ctx.get().getDirection().getReceptionSide().isClient() )
			{
				final ClientWorld world = Minecraft.getInstance().level;
				final TileEntity tileEntity = world.getBlockEntity( message.pos );
				if( tileEntity instanceof TileEntityCrate )
				{
					final TileEntityCrate crate = (TileEntityCrate)tileEntity;
					crate.setNumCrates( message.numCrates );
					crate.setCapacity( message.capacity );
				}
			}
		} );
		ctx.get().setPacketHandled( true );
	}
}
