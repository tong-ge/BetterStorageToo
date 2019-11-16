package io.github.tehstoneman.betterstorage.api;

import java.util.List;

import net.minecraft.item.DyeColor;
import net.minecraft.item.DyeItem;
import net.minecraft.item.IDyeableArmorItem;
import net.minecraft.item.Item;
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

	static ItemStack dyeItem( ItemStack itemStack, List< DyeColor > dyeList )
	{
		ItemStack restuleStacktack = ItemStack.EMPTY;

		final int[] aint = new int[3];
		int i = 0;
		int count = 0;
		IDyeableItem dyeableItem = null;
		final Item item = itemStack.getItem();

		if( item instanceof IDyeableItem )
		{
			dyeableItem = (IDyeableItem)item;
			restuleStacktack = itemStack.copy();
			restuleStacktack.setCount( 1 );

			if( dyeableItem.hasColor( itemStack ) )
			{
				final int color = dyeableItem.getColor( restuleStacktack );
				final float r = ( color >> 16 & 255 ) / 255.0F;
				final float g = ( color >> 8 & 255 ) / 255.0F;
				final float b = ( color & 255 ) / 255.0F;
				i = (int)( i + Math.max( r, Math.max( g, b ) ) * 255.0F );
				aint[0] = (int)( aint[0] + r * 255.0F );
				aint[1] = (int)( aint[1] + g * 255.0F );
				aint[2] = (int)( aint[2] + b * 255.0F );
				++count;
			}

			for( final DyeColor dyeitem : dyeList )
			{
				final float[] afloat = dyeitem.getColorComponentValues();
				final int r = (int)( afloat[0] * 255.0F );
				final int g = (int)( afloat[1] * 255.0F );
				final int b = (int)( afloat[2] * 255.0F );
				i += Math.max( r, Math.max( g, b ) );
				aint[0] += r;
				aint[1] += g;
				aint[2] += b;
				++count;
			}
		}

		if( dyeableItem == null )
			return ItemStack.EMPTY;
		else
		{
			int r = aint[0] / count;
			int g = aint[1] / count;
			int b = aint[2] / count;
			final float f3 = (float)i / (float)count;
			final float f4 = Math.max( r, Math.max( g, b ) );
			r = (int)( r * f3 / f4 );
			g = (int)( g * f3 / f4 );
			b = (int)( b * f3 / f4 );
			int j2 = ( r << 8 ) + g;
			j2 = ( j2 << 8 ) + b;
			dyeableItem.setColor( restuleStacktack, j2 );
			return restuleStacktack;
		}
	}
}
