package io.github.tehstoneman.betterstorage.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class BetterStorageConfig
{
	private static final ForgeConfigSpec.Builder	BUILDER		= new ForgeConfigSpec.Builder();

	public static Common							COMMON		= new Common( BUILDER );
	private static ForgeConfigSpec					commonSpec	= BUILDER.build();

	public static class Common
	{
		public ForgeConfigSpec.IntValue		reinforcedColumns;
		public ForgeConfigSpec.BooleanValue	enableCrateInventoryInterface;

		public ForgeConfigSpec.IntValue		cardboardBoxRows;
		public ForgeConfigSpec.IntValue		cardboardBoxUses;
		public ForgeConfigSpec.BooleanValue	cardboardBoxShowContents;

		public ForgeConfigSpec.BooleanValue	lockBreakable;

		public ForgeConfigSpec.BooleanValue	enableHelpTooltips;
		public ForgeConfigSpec.BooleanValue	enableWarningMessages;

		public ForgeConfigSpec.BooleanValue	crateEnabled;
		public ForgeConfigSpec.BooleanValue	reinforcedChestEnabled;
		public ForgeConfigSpec.BooleanValue	lockerEnabled;
		public ForgeConfigSpec.BooleanValue	reinforcedLockerEnabled;
		public ForgeConfigSpec.BooleanValue	lockableDoorEnabled;
		public ForgeConfigSpec.BooleanValue	flintBlockEnabled;
		public ForgeConfigSpec.BooleanValue	cardboardBoxEnabled;

		public ForgeConfigSpec.BooleanValue	keyEnabled;
		public ForgeConfigSpec.BooleanValue	masterKeyEnabled;
		public ForgeConfigSpec.BooleanValue	keyringEnabled;
		public ForgeConfigSpec.BooleanValue	lockEnabled;
		public ForgeConfigSpec.BooleanValue	cardboardSheetEnabled;
		public ForgeConfigSpec.BooleanValue	slimeBucketEnabled;

		public ForgeConfigSpec.BooleanValue	cardboardHelmetEnabled;
		public ForgeConfigSpec.BooleanValue	cardboardChestplateEnabled;
		public ForgeConfigSpec.BooleanValue	cardboardLeggingsEnabled;
		public ForgeConfigSpec.BooleanValue	cardboardBootsEnabled;
		public ForgeConfigSpec.BooleanValue	cardboardSwordEnabled;
		public ForgeConfigSpec.BooleanValue	cardboardPickaxeEnabled;
		public ForgeConfigSpec.BooleanValue	cardboardShovelEnabled;
		public ForgeConfigSpec.BooleanValue	cardboardAxeEnabled;
		public ForgeConfigSpec.BooleanValue	cardboardHoeEnabled;

		public ForgeConfigSpec.BooleanValue	enchUnlockingEnabled;
		public ForgeConfigSpec.BooleanValue	enchLockpickingEnabled;
		public ForgeConfigSpec.BooleanValue	enchMorphingEnabled;
		public ForgeConfigSpec.BooleanValue	enchPersistanceEnabled;
		public ForgeConfigSpec.BooleanValue	enchSecurityEnabled;
		public ForgeConfigSpec.BooleanValue	enchShockEnabled;
		public ForgeConfigSpec.BooleanValue	enchTriggerEnabled;

		public Common( ForgeConfigSpec.Builder builder )
		{
			builder.comment( "General settings." ).push( "General" );
			//@formatter:off
			// Reinforced chest settings
			reinforcedColumns = builder
					.comment( "Number of columns in reinforced chests and lockers. Valid values are 9, 11 and 13." )
					.translation( "config.betterstorage.general.reinforcedColumns" )
					.defineInRange( "reinforcedColumns", 13, 9, 13 );

			// Crate settings
			enableCrateInventoryInterface = builder
					.comment( "If enabled, exposes a special block view of crates, so items can be moved in and out by automated systems." )
					.translation( "config.betterstorage.general.enableCrateInventoryInterface" )
					.define( "enableCrateInventoryInterface", true );

			// Cardboard box settings
			cardboardBoxRows = builder
					.comment( "Number of rows in cardboard boxes. Valid values are 1 to 3." )
					.translation( "config.betterstorage.general.cardboardBoxRows" )
					.defineInRange( "cardboardBoxRows", 1, 1, 3 );
			cardboardBoxUses = builder
					.comment( "Number of times cardboard boxes can be picked up with items before they break. Use 0 for infinite uses." )
					.translation( "config.betterstorage.general.cardboardBoxUses" )
					.defineInRange( "cardboardBoxUses", 4, 0, Integer.MAX_VALUE );
			cardboardBoxShowContents = builder
					.comment( "If disabled, doesn't show cardboard box contents in their tooltips." )
					.translation( "config.betterstorage.general.cardboardBoxShowContents" )
					.define( "cardboardBoxShowContents", true );

			// Miscellaneous settings
			lockBreakable = builder
					.comment( "If disabled, turns off the ability to break locks off of locked containers using tools." )
					.translation( "config.betterstorage.general.lockBreakable" )
					.define( "lockBreakable", true );
			enableHelpTooltips = builder
					.comment( "If enabled, shows tooltips on some items to help players who're new to the mod." )
					.translation( "config.betterstorage.general.enableHelpTooltips" )
					.define( "enableHelpTooltips", true );
			enableWarningMessages = builder
					.comment( "If enabled, allows additional warning messages to be logged to the console." )
					.translation( "config.betterstorage.general.enableWarningMessages" )
					.define( "enableWarningMessages", false );
			//@formatter:on
			builder.pop();

			builder.comment( "Enable or disable blocks." ).push( "Blocks" );
			//@formatter:off
			// Blocks
			crateEnabled = builder
					.translation( "tile.betterstorage.crate.name" )
					.worldRestart()
					.define( "crateEnabled", true );
			reinforcedChestEnabled = builder
					.translation( "tile.betterstorage.reinforced_chest.name" )
					.worldRestart()
					.define( "reinforcedChestEnabled", true );
			lockerEnabled = builder
					.translation( "tile.betterstorage.locker.name" )
					.worldRestart()
					.define( "lockerEnabled", true );
			reinforcedLockerEnabled = builder
					.translation( "tile.betterstorage.reinforced_locker.name" )
					.worldRestart()
					.define( "reinforcedLockerEnabled", true );
			lockableDoorEnabled = builder
					.translation( "tile.betterstorage.lockable_door.name" )
					.worldRestart()
					.define( "lockableDoorEnabled", true );
			flintBlockEnabled = builder
					.translation( "tile.betterstorage.flint_block.name" )
					.worldRestart()
					.define( "flintBlockEnabled", true );
			cardboardBoxEnabled = builder
					.translation( "tile.betterstorage.cardboard_box.name" )
					.worldRestart()
					.define( "cardboardBoxEnabled", true );
			//@formatter:on
			builder.pop();

			builder.comment( "Enable or disable items." ).push( "Items" );
			//@formatter:off
			// Items
			keyEnabled = builder
					.translation( "item.betterstorage.key.name" )
					.worldRestart()
					.define( "keyEnabled", true );
			masterKeyEnabled = builder
					.translation( "item.betterstorage.master_key.name" )
					.worldRestart()
					.define( "masterKeyEnabled", true );
			keyringEnabled = builder
					.translation( "item.betterstorage.keyring.name" )
					.worldRestart()
					.define( "keyringEnabled", true );
			lockEnabled = builder
					.translation( "item.betterstorage.lock.name" )
					.worldRestart()
					.define( "lockEnabled", true );
			cardboardSheetEnabled = builder
					.translation( "item.betterstorage.cardboard_sheet.name" )
					.worldRestart()
					.define( "cardboardSheetEnabled", true );
			slimeBucketEnabled = builder
					.translation( "item.betterstorage.bucket_slime.name" )
					.worldRestart()
					.define( "slimeBucketEnabled", true );
			//@formatter:on
			builder.pop();

			builder.comment( "Enable or disable cardboard items." ).push( "Cardboard" );
			//@formatter:off
			// Cardboard
			cardboardHelmetEnabled = builder
					.translation( "item.betterstorage.cardboard_helmet.name" )
					.worldRestart()
					.define( "cardboardHelmetEnabled", true );
			cardboardChestplateEnabled = builder
					.translation( "item.betterstorage.cardboard_chestplate.name" )
					.worldRestart()
					.define( "cardboardChestplateEnabled", true );
			cardboardLeggingsEnabled = builder
					.translation( "item.betterstorage.cardboard_leggings.name" )
					.worldRestart()
					.define( "cardboardLeggingsEnabled", true );
			cardboardBootsEnabled = builder
					.translation( "item.betterstorage.cardboard_boots.name" )
					.worldRestart()
					.define( "cardboardBootsEnabled", true );
			cardboardSwordEnabled = builder
					.translation( "item.betterstorage.cardboard_sword.name" )
					.worldRestart()
					.define( "cardboardSwordEnabled", true );
			cardboardPickaxeEnabled = builder
					.translation( "item.betterstorage.cardboard_pickaxe.name" )
					.worldRestart()
					.define( "cardboardPickaxeEnabled", true );
			cardboardShovelEnabled = builder
					.translation( "item.betterstorage.cardboard_shovel.name" )
					.worldRestart()
					.define( "cardboardShovelEnabled", true );
			cardboardAxeEnabled = builder
					.translation( "item.betterstorage.cardboard_axe.name" )
					.worldRestart()
					.define( "cardboardAxeEnabled", true );
			cardboardHoeEnabled = builder
					.translation( "item.betterstorage.cardboard_hoe.name" )
					.worldRestart()
					.define( "cardboardHoeEnabled", true );
			//@formatter:on
			builder.pop();

			builder.comment( "Enable or disable enchantments." ).push( "Enchantments" );
			//@formatter:off
			// Enchantments
			enchUnlockingEnabled = builder
					.translation( "enchantment.betterstorage.key.unlocking" )
					.worldRestart()
					.define( "enchUnlockingEnabled", true );
			enchLockpickingEnabled = builder
					.translation( "enchantment.betterstorage.key.lockpicking" )
					.worldRestart()
					.define( "enchLockpickingEnabled", true );
			enchMorphingEnabled = builder
					.translation( "enchantment.betterstorage.key.morphing" )
					.worldRestart()
					.define( "enchMorphingEnabled", true );
			enchPersistanceEnabled = builder
					.translation( "enchantment.betterstorage.key.persistance" )
					.worldRestart()
					.define( "enchPersistanceEnabled", true );
			enchSecurityEnabled = builder
					.translation( "enchantment.betterstorage.key.security" )
					.worldRestart()
					.define( "enchSecurityEnabled", true );
			enchShockEnabled = builder
					.translation( "enchantment.betterstorage.key.shock" )
					.worldRestart()
					.define( "enchShockEnabled", true );
			enchTriggerEnabled = builder
					.translation( "enchantment.betterstorage.key.trigger" )
					.worldRestart()
					.define( "enchTriggerEnabled", true );
			//@formatter:on
			builder.pop();
		}
	}

	/*
	 * static
	 * {
	 * final Pair< Common, ForgeConfigSpec > specPair = new ForgeConfigSpec.Builder().configure( Common::new );
	 * commonSpec = specPair.getRight();
	 * COMMON = specPair.getLeft();
	 * }
	 */

	public static void register( ModLoadingContext context )
	{
		context.registerConfig( ModConfig.Type.COMMON, commonSpec );
	}
}
