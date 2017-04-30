package io.github.tehstoneman.betterstorage.utils;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.ModInfo;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BetterStorageEventHandler
{
	@SubscribeEvent
	public void onConfigChangeEvent( OnConfigChangedEvent event )
	{
		if( event.getModID().equals( ModInfo.modId ) && !event.isWorldRunning() )
			BetterStorage.config.syncFromGUI();
	}
}
