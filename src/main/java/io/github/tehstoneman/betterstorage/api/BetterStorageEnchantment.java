package io.github.tehstoneman.betterstorage.api;

import java.util.HashMap;
import java.util.Map;

import io.github.tehstoneman.betterstorage.BetterStorage;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public final class BetterStorageEnchantment
{
	public static Map< String, EnchantmentType > enchantmentTypes = new HashMap<>();
	// public static Map< String, Enchantment > enchantments = new HashMap<>();

	private BetterStorageEnchantment()
	{}

	/**
	 * Returns a BetterStorage enchantment type with that name. <br>
	 * <b>Types:</b> key, lock
	 */
	public static EnchantmentType getType( String name )
	{
		return enchantmentTypes.get( name );
	}

	/**
	 * Returns a BetterStorage enchantment with that name. <br>
	 * <b>Key:</b> unlocking, lockpicking, morphing. <br>
	 * <b>Lock:</b> persistance, security, shock, trigger.
	 */
	/*
	 * public static Enchantment get( String name )
	 * {
	 * return enchantments.get( name );
	 * }
	 */

	/** Helper method to get the level of that enchantment on the item. */
	public static int getLevel( ItemStack stack, Enchantment enchantment )
	{
		if( stack.isEnchanted() )
		{
			final Map< Enchantment, Integer > enchantments = EnchantmentHelper.getEnchantments( stack );
			return enchantments.getOrDefault( enchantment, 0 );
		}
		return 0;
	}

	/** Helper method to decrease the level of an enchantment on the item. */
	public static void decEnchantment( ItemStack stack, Enchantment ench, int level )
	{
		if(stack.isEmpty())return;
		
		Map< Enchantment, Integer > list = EnchantmentHelper.getEnchantments( stack );
		
		int newLevel = list.getOrDefault( ench, 0 ) - level;

		if(newLevel<=0)
		{
			list.remove( ench );
		}
		else
			list.put( ench, newLevel );

		EnchantmentHelper.setEnchantments( list, stack );
	}
}
