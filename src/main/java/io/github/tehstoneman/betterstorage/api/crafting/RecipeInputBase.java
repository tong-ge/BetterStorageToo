package io.github.tehstoneman.betterstorage.api.crafting;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class RecipeInputBase implements IRecipeInput
{
	@Override
	public void craft( ItemStack input, ContainerInfo containerInfo )
	{
		if( input == null )
			return;

		final Item item = input.getItem();
		final ItemStack containerItem = item.getContainerItem( input.copy() );
		final boolean doesLeaveCrafting = item.getContainerItem() == item;
		containerInfo.set( containerItem, doesLeaveCrafting );

		input.setCount( input.getCount() - getAmount() );
	}
}
