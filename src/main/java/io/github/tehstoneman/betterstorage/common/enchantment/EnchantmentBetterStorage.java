package io.github.tehstoneman.betterstorage.common.enchantment;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.client.BetterStorageResource;
import net.minecraft.enchantment.Enchantment.Rarity;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class EnchantmentBetterStorage
{
	public final static EnchantmentKey	unlocking	= new EnchantmentKey( "unlocking", Rarity.COMMON, 5, 5, 10, 30, 0 );
	public final static EnchantmentKey	lockpicking	= new EnchantmentKey( "lockpicking", Rarity.COMMON, 5, 5, 8, 30, 0 );
	public final static EnchantmentKey	morphing	= new EnchantmentKey( "morphing", Rarity.COMMON, 5, 10, 12, 30, 0 );

	public final static EnchantmentLock	persistance	= new EnchantmentLock( "persistance", Rarity.COMMON, 5, 1, 8, 30, 0 );
	public final static EnchantmentLock	security	= new EnchantmentLock( "security", Rarity.COMMON, 5, 1, 8, 30, 0 );
	public final static EnchantmentLock	shock		= new EnchantmentLock( "shock", Rarity.COMMON, 5, 1, 8, 30, 0 );
	public final static EnchantmentLock	trigger		= new EnchantmentLock( "trigger", Rarity.COMMON, 1, 1, 8, 30, 0 );

	public static void initialize()
	{
		// Add key enchantments
		if( BetterStorage.config.keyEnabled )
		{
			if( BetterStorage.config.enchUnlockingEnabled )
				GameRegistry.register( unlocking, new BetterStorageResource( "unlocking" ) );
			if( BetterStorage.config.enchLockpickingEnabled )
				GameRegistry.register( lockpicking, new BetterStorageResource( "lockpicking" ) );
			if( BetterStorage.config.enchMorphingEnabled )
				GameRegistry.register( morphing, new BetterStorageResource( "morphing" ) );

			if( BetterStorage.config.enchLockpickingEnabled && BetterStorage.config.enchMorphingEnabled )
			{
				lockpicking.setIncompatible( morphing );
				morphing.setIncompatible( lockpicking );
			}
		}

		// Add lock enchantments
		if( BetterStorage.config.lockEnabled )
		{
			if( BetterStorage.config.enchPersistanceEnabled )
				GameRegistry.register( persistance, new BetterStorageResource( "persistance" ) );
			if( BetterStorage.config.enchSecurityEnabled )
				GameRegistry.register( security, new BetterStorageResource( "security" ) );
			if( BetterStorage.config.enchShockEnabled )
				GameRegistry.register( shock, new BetterStorageResource( "shock" ) );
			if( BetterStorage.config.enchTriggerEnabled )
				GameRegistry.register( trigger, new BetterStorageResource( "trigger" ) );
		}
	}
}
