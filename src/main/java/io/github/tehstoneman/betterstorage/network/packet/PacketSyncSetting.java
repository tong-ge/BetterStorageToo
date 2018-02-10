package io.github.tehstoneman.betterstorage.network.packet;

import java.io.IOException;

import io.github.tehstoneman.betterstorage.network.AbstractPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;

/** Synchronizes some settings with the client. */
public class PacketSyncSetting extends AbstractPacket< PacketSyncSetting >
{

	public NBTTagCompound data;

	public PacketSyncSetting()
	{}

	public PacketSyncSetting( NBTTagCompound data )
	{
		this.data = data;
	}

	/*
	 * public PacketSyncSetting( Config config )
	 * {
	 * data = new NBTTagCompound();
	 * config.write( data );
	 * }
	 */

	@Override
	public void encode( PacketBuffer buffer ) throws IOException
	{
		buffer.writeCompoundTag( data );
	}

	@Override
	public void decode( PacketBuffer buffer ) throws IOException
	{
		data = buffer.readCompoundTag();
	}

	@Override
	public void handle( EntityPlayer player )
	{
		// BetterStorage.config.read( data );
	}

}
