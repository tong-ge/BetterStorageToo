package io.github.tehstoneman.betterstorage.addon.jei;

import io.github.tehstoneman.betterstorage.ModInfo;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.util.ResourceLocation;

@JeiPlugin
public class BetterStoragePlugin implements IModPlugin
{
	@Override
	public ResourceLocation getPluginUid()
	{
		return new ResourceLocation( ModInfo.MOD_ID, "jei_plugin" );
	}

	/*
	 * @Override
	 * public void registerVanillaCategoryExtensions( IVanillaCategoryExtensionRegistration registration )
	 * {
	 * final IExtendableRecipeCategory< ICraftingRecipe, ICraftingCategoryExtension > craftingCategory = registration.getCraftingCategory();
	 * craftingCategory.addCategoryExtension( ICraftingRecipe.class, CopyKeyRecipeExtension::new );
	 * }
	 */

	@Override
	public void registerRecipes( IRecipeRegistration registration )
	{}
}
