package io.github.tehstoneman.betterstorage.common.enchantment;

import java.util.Map;

import io.github.tehstoneman.betterstorage.ModInfo;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantment.Rarity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder( ModInfo.MOD_ID )
public class EnchantmentBetterStorage
{
	//@formatter:off
	@ObjectHolder( "unlocking" )	public static EnchantmentKey	UNLOCKING;
	@ObjectHolder( "lockpicking" )	public static EnchantmentKey	LOCKPICKING;
	@ObjectHolder( "morphing" )		public static EnchantmentKey	MORPHING;

	@ObjectHolder( "persistance" )	public static EnchantmentLock	PERSISTANCE;
	@ObjectHolder( "security" )		public static EnchantmentLock	SECURITY;
	@ObjectHolder( "shock" )		public static EnchantmentLock	SHOCK;
	@ObjectHolder( "trigger" )		public static EnchantmentLock	TRIGGER;
	//@formatter:on

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

	@Mod.EventBusSubscriber( bus = Mod.EventBusSubscriber.Bus.MOD )
	private static class Register
	{
		@SubscribeEvent
		public static void onEnchantmentRegistry( final RegistryEvent.Register< Enchantment > event )
		{
			final IForgeRegistry< Enchantment > registry = event.getRegistry();

			registry.register( new EnchantmentKey( Rarity.COMMON, 5, 5, 10, 30, 0 ).setRegistryName( "unlocking" ) );
			registry.register( new EnchantmentKey( Rarity.COMMON, 5, 5, 8, 30, 0 ).setRegistryName( "lockpicking" ) );
			registry.register( new EnchantmentKey( Rarity.COMMON, 5, 10, 12, 30, 0 ).setRegistryName( "morphing" ) );

			registry.register( new EnchantmentLock( Rarity.COMMON, 5, 1, 8, 30, 0 ).setRegistryName( "persistance" ) );
			registry.register( new EnchantmentLock( Rarity.COMMON, 5, 1, 8, 30, 0 ).setRegistryName( "security" ) );
			registry.register( new EnchantmentLock( Rarity.COMMON, 5, 1, 8, 30, 0 ).setRegistryName( "shock" ) );
			registry.register( new EnchantmentLock( Rarity.COMMON, 1, 1, 8, 30, 0 ).setRegistryName( "trigger" ) );
		}
	}
}
