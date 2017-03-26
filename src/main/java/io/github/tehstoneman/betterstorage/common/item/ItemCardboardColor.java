package io.github.tehstoneman.betterstorage.common.item;

import java.awt.Color;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;

public class ItemCardboardColor implements IItemColor
{
	@Override
	public int getColorFromItemstack( ItemStack stack, int tintIndex )
	{
		//return EnumDyeColor.BROWN.getMapColor().colorValue;
		return Color.decode( "0x6E522B" ).getRGB();
	}
}
