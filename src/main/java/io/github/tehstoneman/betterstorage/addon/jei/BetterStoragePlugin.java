package io.github.tehstoneman.betterstorage.addon.jei;

import io.github.tehstoneman.betterstorage.ModInfo;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;
import net.minecraft.util.ResourceLocation;

@JeiPlugin
public class BetterStoragePlugin implements IModPlugin
{
	@Override
	public ResourceLocation getPluginUid()
	{
		return new ResourceLocation( ModInfo.MOD_ID, "jei_plugin" );
	}

	@Override
	public void registerVanillaCategoryExtensions( IVanillaCategoryExtensionRegistration registration )
	{
		// BetterStorage.LOGGER.info( "Better Storage JEI Plugin ==== {} ====", registration.getCraftingCategory().getUid() );
		// registration.getCraftingCategory().getRecipeClass();
	}
}
