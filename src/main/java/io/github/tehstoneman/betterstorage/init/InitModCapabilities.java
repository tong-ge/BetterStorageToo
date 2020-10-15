package io.github.tehstoneman.betterstorage.init;

import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.common.capabilities.CapabilityConfig;
import io.github.tehstoneman.betterstorage.common.capabilities.CapabilityCrate;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@EventBusSubscriber( modid = ModInfo.MOD_ID, bus = Bus.FORGE )
public class InitModCapabilities
{
	@SubscribeEvent
	public static void registerCapabilities( FMLCommonSetupEvent event )
	{
		CapabilityConfig.register();
		CapabilityCrate.register();
	}

	@SubscribeEvent
	public static void attachWorldCapabilities( AttachCapabilitiesEvent< World > event )
	{
		event.addCapability( CapabilityCrate.CAPABILITY_RESOURCE, new CapabilityCrate.Provider() );
	}
}
