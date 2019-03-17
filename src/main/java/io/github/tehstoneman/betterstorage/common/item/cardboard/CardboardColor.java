package io.github.tehstoneman.betterstorage.common.item.cardboard;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;

public class CardboardColor implements IItemColor
{
	/*
	 * @Override
	 * public int colorMultiplier( ItemStack stack, int tintIndex )
	 * {
	 * if( stack.hasTagCompound() )
	 * {
	 * final NBTTagCompound tag = stack.getTagCompound();
	 * if( tag.hasKey( "color" ) )
	 * return tag.getInteger( "color" );
	 * }
	 * return 0x6e522b;
	 * }
	 */

	@Override
	public int getColor( ItemStack p_getColor_1_, int p_getColor_2_ )
	{
		// TODO Auto-generated method stub
		return 0;
	}
}
