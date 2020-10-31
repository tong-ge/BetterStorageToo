package io.github.tehstoneman.betterstorage.init;

import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.common.capabilities.CapabilityCrate;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber( modid = ModInfo.MOD_ID, bus = Bus.FORGE )
public class AttachModCapabilities
{
	@SubscribeEvent
	public static void attachWorldCapabilities( AttachCapabilitiesEvent< World > event )
	{
		event.addCapability( CapabilityCrate.CAPABILITY_RESOURCE, new CapabilityCrate.Provider() );
	}
}
