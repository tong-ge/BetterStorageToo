package io.github.tehstoneman.betterstorage.common.item.locking;

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
			return ItemBetterStorage.getKeyColor2( stack );
		case 1:
			return ItemBetterStorage.getKeyColor1( stack );
		case 2:
			return  Color.WHITE.getRGB();
		}
		return Color.WHITE.getRGB();
	}
}
