package io.github.tehstoneman.betterstorage.common.enchantment;

import java.util.Map;

import io.github.tehstoneman.betterstorage.ModInfo;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantment.Rarity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EnchantmentBetterStorage
{
	public static final DeferredRegister< Enchantment >	REGISTERY	= DeferredRegister.create( ForgeRegistries.ENCHANTMENTS, ModInfo.MOD_ID );

	public static RegistryObject< EnchantmentKey >		UNLOCKING	= REGISTERY.register( "unlocking",
			() -> new EnchantmentKey( Rarity.COMMON, 5, 5, 10, 30, 0 ) );
	public static RegistryObject< EnchantmentKey >		LOCKPICKING	= REGISTERY.register( "lockpicking",
			() -> new EnchantmentKey( Rarity.COMMON, 5, 5, 8, 30, 0 ) );
	public static RegistryObject< EnchantmentKey >		MORPHING	= REGISTERY.register( "morphing",
			() -> new EnchantmentKey( Rarity.COMMON, 5, 10, 12, 30, 0 ) );

	public static RegistryObject< EnchantmentLock >		PERSISTANCE	= REGISTERY.register( "persistance",
			() -> new EnchantmentLock( Rarity.COMMON, 5, 1, 8, 30, 0 ) );
	public static RegistryObject< EnchantmentLock >		SECURITY	= REGISTERY.register( "security",
			() -> new EnchantmentLock( Rarity.COMMON, 5, 1, 8, 30, 0 ) );
	public static RegistryObject< EnchantmentLock >		SHOCK		= REGISTERY.register( "shock",
			() -> new EnchantmentLock( Rarity.COMMON, 5, 1, 8, 30, 0 ) );
	public static RegistryObject< EnchantmentLock >		TRIGGER		= REGISTERY.register( "trigger",
			() -> new EnchantmentLock( Rarity.COMMON, 1, 1, 8, 30, 0 ) );

	/**
	 * Helper method to get the level of that enchantment on the item.
	 *
	 * @param stack
	 *            {@link ItemStack} to get the {@link Enchantment} from.
	 * @param enchantment
	 *            {@link Enchantment} to check level for.
	 * @return The level of the requested (@link Enchantment}, or 0 if none.
	 */
	public static int getLevel( ItemStack stack, Enchantment enchantment )
	{
		if( stack.isEnchanted() )
		{
			final Map< Enchantment, Integer > enchantments = EnchantmentHelper.getEnchantments( stack );
			return enchantments.getOrDefault( enchantment, 0 );
		}
		return 0;
	}

	/**
	 * Helper method to decrease the level of an enchantment on the item.
	 *
	 * @param stack
	 *            {@link ItemStack} with the {@link Enchantment}.
	 * @param ench
	 *            {@link Enchantment} to decrease.
	 * @param level
	 *            Amount to decrease by.
	 */
	public static void decEnchantment( ItemStack stack, Enchantment ench, int level )
	{
		if( stack.isEmpty() )
			return;

		final Map< Enchantment, Integer > list = EnchantmentHelper.getEnchantments( stack );

		final int newLevel = list.getOrDefault( ench, 0 ) - level;

		if( newLevel <= 0 )
			list.remove( ench );
		else
			list.put( ench, newLevel );

		EnchantmentHelper.setEnchantments( list, stack );
	}
}
