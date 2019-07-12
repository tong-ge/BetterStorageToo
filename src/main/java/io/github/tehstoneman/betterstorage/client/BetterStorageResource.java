package io.github.tehstoneman.betterstorage.client;

import io.github.tehstoneman.betterstorage.ModInfo;
import net.minecraft.util.ResourceLocation;

public class BetterStorageResource extends ResourceLocation
{
	@Deprecated
	public BetterStorageResource( String location )
	{
		super( ModInfo.MOD_ID, location );
	}
}
