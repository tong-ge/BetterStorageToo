package io.github.tehstoneman.betterstorage.common.item.cardboard;

import java.awt.Color;

import io.github.tehstoneman.betterstorage.api.ICardboardItem;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;

public class CardboardColor implements IItemColor
{
	@Override
	public int getColorFromItemstack( ItemStack stack, int tintIndex )
	{
		if( stack.getItem() instanceof ICardboardItem )
			return ( (ICardboardItem)stack.getItem() ).getColor( stack );
		else
			if( stack.getItem() instanceof ItemCardboardArmor )
				return ( (ItemCardboardArmor)stack.getItem() ).getColor( stack );
		return Color.WHITE.getRGB();
	}
}
