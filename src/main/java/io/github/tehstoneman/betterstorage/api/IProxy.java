package io.github.tehstoneman.betterstorage.api;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.world.World;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public interface IProxy
{
	public void setup( FMLCommonSetupEvent event );

	public World getClientWorld();
}
