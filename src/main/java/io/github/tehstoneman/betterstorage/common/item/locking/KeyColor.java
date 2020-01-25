package io.github.tehstoneman.betterstorage.common.item.locking;

import java.awt.Color;

import io.github.tehstoneman.betterstorage.api.lock.KeyLockItem;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;

public class KeyColor implements IItemColor
{
	@Override
	public int getColor( ItemStack stack, int tintIndex )
	{
		switch( tintIndex )
		{
		case 0:
			return KeyLockItem.getKeyColor2( stack );
		case 1:
			return KeyLockItem.getKeyColor1( stack );
		case 2:
			return Color.WHITE.getRGB();
		}
		return Color.WHITE.getRGB();
	}
}
