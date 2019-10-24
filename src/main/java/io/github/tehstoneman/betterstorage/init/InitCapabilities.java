package io.github.tehstoneman.betterstorage.init;

import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.common.capabilities.CapabilityCrate;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber( modid = ModInfo.MOD_ID, bus = Bus.MOD )
public class InitCapabilities
{
	@SubscribeEvent
	public static void registerCapabilities( FMLCommonSetupEvent event )
	{
		CapabilityCrate.register();
	}
}
