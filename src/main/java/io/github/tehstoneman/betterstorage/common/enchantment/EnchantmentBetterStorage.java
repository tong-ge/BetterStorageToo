package io.github.tehstoneman.betterstorage.common.enchantment;

import io.github.tehstoneman.betterstorage.ModInfo;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantment.Rarity;
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
