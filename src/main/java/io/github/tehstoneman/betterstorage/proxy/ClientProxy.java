package io.github.tehstoneman.betterstorage.proxy;

import net.minecraft.world.level.Level;

public class ClientProxy implements IProxy
{
	@Override
	public Level getClientWorld()
	{
		// return Minecraft.getInstance().level;
		return null;
	}
}
