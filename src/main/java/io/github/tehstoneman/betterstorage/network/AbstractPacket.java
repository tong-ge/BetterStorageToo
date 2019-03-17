package io.github.tehstoneman.betterstorage.network;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;

public abstract class AbstractPacket<T extends AbstractPacket< T >> // implements IMessage, IMessageHandler< T, IMessage >
{
	public abstract void encode( PacketBuffer buffer ) throws IOException;

	public abstract void decode( PacketBuffer buffer ) throws IOException;

	public abstract void handle( EntityPlayer player );

	// IMessage implementation

	/*
	 * @Override
	 * public final void fromBytes( ByteBuf buffer )
	 * {
	 * try
	 * {
	 * decode( new PacketBuffer( buffer ) );
	 * }
	 * catch( final IOException ex )
	 * {
	 * throw new RuntimeException( ex );
	 * }
	 * }
	 */

	/*
	 * @Override
	 * public final void toBytes( ByteBuf buffer )
	 * {
	 * try
	 * {
	 * encode( new PacketBuffer( buffer ) );
	 * }
	 * catch( final IOException ex )
	 * {
	 * throw new RuntimeException( ex );
	 * }
	 * }
	 */

	// IMessageHandler implementation

	/*
	 * @Override
	 * public final IMessage onMessage( T message, MessageContext context )
	 * {
	 * message.handle( context.side.isServer() ? context.getServerHandler().player : PlayerUtils.getLocalPlayer() );
	 * return null;
	 * }
	 */
}
