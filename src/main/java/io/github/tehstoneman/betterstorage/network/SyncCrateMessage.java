package io.github.tehstoneman.betterstorage.network;

import java.util.UUID;

import io.github.tehstoneman.betterstorage.common.inventory.CrateStackHandler;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityCrate;
import io.github.tehstoneman.betterstorage.common.world.CrateStackCollection;
import io.github.tehstoneman.betterstorage.misc.Region;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class SyncCrateMessage implements IMessage
{
	private UUID	pileID;
	private Region		region;
	private int			numCrates;

	public SyncCrateMessage()
	{}

	public SyncCrateMessage( UUID pileID, Region region, int numCrates )
	{
		this.pileID = pileID;
		this.region = region;
		this.numCrates = numCrates;
	}

	@Override
	public void fromBytes( ByteBuf buf )
	{
		long mostSigBits = buf.readLong();
		long leastSigBits = buf.readLong();
		pileID = new UUID(mostSigBits,leastSigBits);
		final BlockPos posMin = BlockPos.fromLong( buf.readLong() );
		final BlockPos posMax = BlockPos.fromLong( buf.readLong() );
		region = new Region( posMin, posMax );
		numCrates = buf.readInt();
	}

	@Override
	public void toBytes( ByteBuf buf )
	{
		buf.writeLong( pileID.getMostSignificantBits() );
		buf.writeLong( pileID.getLeastSignificantBits() );
		buf.writeLong( region.posMin.toLong() );
		buf.writeLong( region.posMax.toLong() );
		buf.writeInt( numCrates );
	}

	public static class Handler implements IMessageHandler< SyncCrateMessage, IMessage >
	{
		@Override
		public IMessage onMessage( SyncCrateMessage message, MessageContext ctx )
		{
			if( ctx.side == Side.CLIENT )
			{
				final Minecraft minecraft = Minecraft.getMinecraft();
				final WorldClient worldClient = minecraft.theWorld;
				minecraft.addScheduledTask( new Runnable()
				{
					@Override
					public void run()
					{
						processMessage( worldClient, message );
					}
				} );
			}
			return null;
		}

		protected void processMessage( WorldClient worldClient, SyncCrateMessage message )
		{
			CrateStackCollection collection = CrateStackCollection.getCollection( worldClient );
			CrateStackHandler handler = collection.getOrCreateCratePile( message.pileID );
			handler.setRegion( message.region );
			handler.setNumCrates( message.numCrates );
		}
	}
}
