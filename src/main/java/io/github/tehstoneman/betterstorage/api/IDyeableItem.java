package io.github.tehstoneman.betterstorage.api;

import net.minecraft.item.ItemStack;

public interface IDyeableItem
{
	/** Returns if the item can be dyed. */
	boolean canDye( ItemStack stack );

	/** Set the color of the object. */
	public int getColor( ItemStack stack );

	/** Returns color of the item. */
	boolean hasColor( ItemStack itemstack1 );

	/** Set the color of the item. */
	void setColor( ItemStack itemstack, int colorRGB );
}
