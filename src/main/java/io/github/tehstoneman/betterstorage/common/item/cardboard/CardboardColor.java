package io.github.tehstoneman.betterstorage.common.item.cardboard;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class CardboardColor implements IItemColor
{
	@Override
	public int getColor( ItemStack stack, int tintIndex )
	{
		if( stack.hasTag() )
		{
			final CompoundNBT tag = stack.getTag();
			if( tag.contains( "color" ) )
				return tag.getInt( "color" );
		}
		return 0x6e522b;
	}
}
