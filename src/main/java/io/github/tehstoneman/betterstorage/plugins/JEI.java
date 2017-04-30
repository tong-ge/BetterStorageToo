package io.github.tehstoneman.betterstorage.plugins;

import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IModIngredientRegistration;

@JEIPlugin
public class JEI implements IModPlugin
{
	@Override
	public void register( IModRegistry registry )
	{}

	@Override
	public void onRuntimeAvailable( IJeiRuntime jeiRuntime )
	{}

	@Override
	public void registerItemSubtypes( ISubtypeRegistry subtypeRegistry )
	{}

	@Override
	public void registerIngredients( IModIngredientRegistration registry )
	{}
}
