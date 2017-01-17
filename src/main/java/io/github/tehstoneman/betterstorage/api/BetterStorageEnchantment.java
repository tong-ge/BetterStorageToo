package io.github.tehstoneman.betterstorage.api;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.ItemStack;

public final class BetterStorageEnchantment
{
	public static Map< String, EnumEnchantmentType >	enchantmentTypes	= new HashMap< >();
	public static Map< String, Enchantment >			enchantments		= new HashMap< >();

	private BetterStorageEnchantment()
	{}

	/**
	 * Returns a BetterStorage enchantment type with that name. <br>
	 * <b>Types:</b> key, lock
	 */
	public static EnumEnchantmentType getType( String name )
	{
		return enchantmentTypes.get( name );
	}

	/**
	 * Returns a BetterStorage enchantment with that name. <br>
	 * <b>Key:</b> unlocking, lockpicking, morphing. <br>
	 * <b>Lock:</b> persistance, security, shock, trigger.
	 */
	public static Enchantment get( String name )
	{
		return enchantments.get( name );
	}

	/** Helper method to get the level of that enchantment on the item. */
	public static int getLevel( ItemStack stack, String name )
	{
		final Enchantment enchantment = get( name );
		if( enchantment == null )
			return 0;
		return EnchantmentHelper.getEnchantmentLevel( enchantment, stack );
	}
}
