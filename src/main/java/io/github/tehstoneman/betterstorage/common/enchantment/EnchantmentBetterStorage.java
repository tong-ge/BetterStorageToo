package io.github.tehstoneman.betterstorage.common.enchantment;

import io.github.tehstoneman.betterstorage.ModInfo;
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

	public static void initialize()
	{
		// Add key enchantments
		/*
		 * if( BetterStorage.config.keyEnabled )
		 * {
		 * if( BetterStorage.config.enchUnlockingEnabled )
		 * GameRegistry.register( unlocking, new BetterStorageResource( "unlocking" ) );
		 * if( BetterStorage.config.enchLockpickingEnabled )
		 * GameRegistry.register( lockpicking, new BetterStorageResource( "lockpicking" ) );
		 * if( BetterStorage.config.enchMorphingEnabled )
		 * GameRegistry.register( morphing, new BetterStorageResource( "morphing" ) );
		 *
		 * if( BetterStorage.config.enchLockpickingEnabled && BetterStorage.config.enchMorphingEnabled )
		 * {
		 * lockpicking.setIncompatible( morphing );
		 * morphing.setIncompatible( lockpicking );
		 * }
		 * }
		 */

		// Add lock enchantments
		/*
		 * if( BetterStorage.config.lockEnabled )
		 * {
		 * if( BetterStorage.config.enchPersistanceEnabled )
		 * GameRegistry.register( persistance, new BetterStorageResource( "persistance" ) );
		 * if( BetterStorage.config.enchSecurityEnabled )
		 * GameRegistry.register( security, new BetterStorageResource( "security" ) );
		 * if( BetterStorage.config.enchShockEnabled )
		 * GameRegistry.register( shock, new BetterStorageResource( "shock" ) );
		 * if( BetterStorage.config.enchTriggerEnabled )
		 * GameRegistry.register( trigger, new BetterStorageResource( "trigger" ) );
		 * }
		 */
	}
}
