package io.github.tehstoneman.betterstorage.common.item.cardboard;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class CardboardColor implements IItemColor
{
	@Override
	public int getColorFromItemstack( ItemStack stack, int tintIndex )
	{
		if( stack.hasTagCompound() )
		{
			final NBTTagCompound tag = stack.getTagCompound();
			if( tag.hasKey( "color" ) )
				return tag.getInteger( "color" );
		}
		return 0x6e522b;
	}
}
