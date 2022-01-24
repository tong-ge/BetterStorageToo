package io.github.tehstoneman.betterstorage.network.packet;

import java.io.IOException;

import io.github.tehstoneman.betterstorage.item.ItemDrinkingHelmet;
import io.github.tehstoneman.betterstorage.network.AbstractPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.FriendlyByteBuf;

/** Sent when the player presses the button to use eir drinking helmet. */
public class PacketDrinkingHelmetUse extends AbstractPacket< PacketDrinkingHelmetUse >
{

	public PacketDrinkingHelmetUse()
	{}

	@Override
	public void encode( FriendlyByteBuf buffer ) throws IOException
	{
		// No additional data.
	}

	@Override
	public void decode( FriendlyByteBuf buffer ) throws IOException
	{
		// No additional data.
	}

	@Override
	public void handle( EntityPlayer player )
	{
		ItemDrinkingHelmet.use( player );
	}

}
