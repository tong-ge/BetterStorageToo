package io.github.tehstoneman.betterstorage.util;

import io.github.tehstoneman.betterstorage.ModInfo;
import net.minecraft.resources.ResourceLocation;

public class BetterStorageResource extends ResourceLocation
{
	public BetterStorageResource( String resourceName )
	{
		super( ModInfo.MOD_ID, resourceName );
	}
}
