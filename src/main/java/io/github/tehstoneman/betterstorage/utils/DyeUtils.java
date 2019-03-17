package io.github.tehstoneman.betterstorage.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;

public final class DyeUtils
{
	private static final Map< String, EnumDyeColor > dyes = new HashMap<>();
	/*
	 * static
	 * {
	 * addColorFromTable( "dyeBlack" );
	 * addColorFromTable( "dyeRed" );
	 * addColorFromTable( "dyeGreen" );
	 * addColorFromTable( "dyeBrown" );
	 * addColorFromTable( "dyeBlue" );
	 * addColorFromTable( "dyePurple" );
	 * addColorFromTable( "dyeCyan" );
	 * addColorFromTable( "dyeLightGray" );
	 * addColorFromTable( "dyeGray" );
	 * addColorFromTable( "dyePink" );
	 * addColorFromTable( "dyeLime" );
	 * addColorFromTable( "dyeYellow" );
	 * addColorFromTable( "dyeLightBlue" );
	 * addColorFromTable( "dyeMagenta" );
	 * addColorFromTable( "dyeOrange" );
	 * addColorFromTable( "dyeWhite" );
	 * };
	 */

	private DyeUtils()
	{}

	/**
	 * Gets the dye color of the item stack. <br>
	 * If it's not a dye, it will return null.
	 */
	public static EnumDyeColor getDyeColor( ItemStack stack )
	{
		if( stack == null )
			return null;
		// final int[] oreIds = OreDictionary.getOreIDs( stack );
		/*
		 * for( final int ore : oreIds )
		 * {
		 * final String name = OreDictionary.getOreName( ore );
		 * if( dyes.containsKey( name ) )
		 * return dyes.get( name );
		 * }
		 */
		return null;
	}

	/** Returns if the item stack is a dye. */
	public static boolean isDye( ItemStack stack )
	{
		// final int[] oreIDs = OreDictionary.getOreIDs( stack );
		/*
		 * if( oreIDs.length > 0 )
		 * {
		 * final int dye = OreDictionary.getOreID( "dye" );
		 * for( final int oreID : oreIDs )
		 * if( oreID == dye )
		 * return true;
		 * }
		 */
		return false;
	}

	/** Returns the combined color of all the dyes and the base color. */
	public static int getColorFromDyes( int color, Collection< ItemStack > dyes )
	{
		int number = dyes.size();
		if( number < 1 )
			return -1;
		int r = 0, g = 0, b = 0;
		if( color >= 0 )
		{
			r = color >> 16;
			g = color >> 8 & 0xFF;
			b = color & 0xFF;
			number++;
		}
		/*
		 * for( final ItemStack dye : dyes )
		 * {
		 * color = getDyeColor( dye ).getColorValue();
		 * if( color < 0 )
		 * continue;
		 * r += color >> 16;
		 * g += color >> 8 & 0xFF;
		 * b += color & 0xFF;
		 * }
		 */
		r /= number;
		g /= number;
		b /= number;
		return r << 16 | g << 8 | b;
	}

	/** Returns the combined color of all the dyes. */
	public static int getColorFromDyes( Collection< ItemStack > dyes )
	{
		return getColorFromDyes( -1, dyes );
	}

	/*
	 * private static void addColorFromTable( String name )
	 * {
	 * final EnumDyeColor dye = EnumDyeColor.byDyeDamage( dyes.size() );
	 * final int color = dye.getColorValue();
	 * dyes.put( name, dye );
	 * }
	 */

}
