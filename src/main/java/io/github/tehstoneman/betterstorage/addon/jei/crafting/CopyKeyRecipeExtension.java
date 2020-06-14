package io.github.tehstoneman.betterstorage.addon.jei.crafting;

import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICustomCraftingCategoryExtension;
import net.minecraft.item.crafting.ICraftingRecipe;

public class CopyKeyRecipeExtension<T extends ICraftingRecipe> implements ICustomCraftingCategoryExtension
{
	protected final T recipe;

	public CopyKeyRecipeExtension( T recipe )
	{
		this.recipe = recipe;
	}

	@Override
	public void setIngredients( IIngredients ingredients )
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setRecipe( IRecipeLayout recipeLayout, IIngredients ingredients )
	{
		// TODO Auto-generated method stub

	}
}
