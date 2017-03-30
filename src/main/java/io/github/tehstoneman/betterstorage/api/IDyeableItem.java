package io.github.tehstoneman.betterstorage.api;

import net.minecraft.item.ItemStack;

public interface IDyeableItem
{
	/** Returns if the item can be dyed. */
	boolean canDye( ItemStack stack );

	public int getColor( ItemStack stack );
}
