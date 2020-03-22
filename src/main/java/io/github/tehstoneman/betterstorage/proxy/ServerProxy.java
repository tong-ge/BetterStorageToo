package io.github.tehstoneman.betterstorage.proxy;

import net.minecraft.world.World;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class ServerProxy implements IProxy
{
	@Override
	public void setup( FMLCommonSetupEvent event )
	{}

	@Override
	public World getClientWorld()
	{
		try
		{
			throw new Exception( "Cannot call ClientWorld on server." );
		}
		catch( final Exception e )
		{
			e.printStackTrace();
		}
		return null;
	}
}
