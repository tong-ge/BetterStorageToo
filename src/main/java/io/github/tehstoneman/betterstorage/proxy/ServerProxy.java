package io.github.tehstoneman.betterstorage.proxy;

import net.minecraft.world.level.Level;

public class ServerProxy implements IProxy
{
	@Override
	public Level getClientWorld()
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
