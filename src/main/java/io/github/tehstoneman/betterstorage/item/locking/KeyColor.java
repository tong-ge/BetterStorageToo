package io.github.tehstoneman.betterstorage.item.locking;

import java.awt.Color;

import io.github.tehstoneman.betterstorage.item.ItemBetterStorage;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class KeyColor implements IItemColor
{
	@Override
	public int getColorFromItemstack( ItemStack stack, int tintIndex )
	{
		switch( tintIndex )
		{
		case 0:
			return Color.WHITE.getRGB();
		case 1:
			return ItemBetterStorage.getFullColor( stack );
		case 2:
			return ItemBetterStorage.getColor( stack );
		}
		return Color.WHITE.getRGB();
	}
}
