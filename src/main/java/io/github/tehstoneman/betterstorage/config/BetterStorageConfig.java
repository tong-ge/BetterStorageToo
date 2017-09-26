package io.github.tehstoneman.betterstorage.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import scala.actors.threadpool.Arrays;

public class BetterStorageConfig
{
	private final Configuration	config;

	public static final String	CATEGORY_BLOCKS	= "blocks";
	public static final String	CATEGORY_ITEMS	= "items";

	// Block settings
	public boolean				crateEnabled;
	public boolean				reinforcedChestEnabled;
	public boolean				lockerEnabled;
	public boolean				reinforcedLockerEnabled;
	public boolean				lockableDoorEnabled;
	public boolean				flintBlockEnabled;
	public boolean				cardboardBoxEnabled;

	// Item settings
	public boolean				keyEnabled;
	public boolean				masterKeyEnabled;
	public boolean				keyringEnabled;
	public boolean				lockEnabled;
	public boolean				cardboardSheetEnabled;

	// Cardboard items
	public boolean				cardboardHelmetEnabled;
	public boolean				cardboardChestplateEnabled;
	public boolean				cardboardLeggingsEnabled;
	public boolean				cardboardBootsEnabled;
	public boolean				cardboardSwordEnabled;
	public boolean				cardboardPickaxeEnabled;
	public boolean				cardboardShovelEnabled;
	public boolean				cardboardAxeEnabled;
	public boolean				cardboardHoeEnabled;

	// General settings
	public int					reinforcedColumns;
	public boolean				enableCrateInventoryInterface;

	public int					cardboardBoxRows;
	public int					cardboardBoxUses;
	public boolean				cardboardBoxShowContents;

	public boolean				enableHelpTooltips;
	public boolean				enableWarningMessages;

	public BetterStorageConfig( File file )
	{
		config = new Configuration( file );
	}

	public Configuration getConfig()
	{
		return config;
	}

	/**
	 * Load the configuration values from the configuration file
	 */
	public void syncFromFile()
	{
		syncConfig( true, true );
	}

	/**
	 * Save the GUI-altered values to disk
	 */
	public void syncFromGUI()
	{
		syncConfig( false, true );
	}

	/**
	 * Save the MBEConfiguration variables (fields) to disk
	 */
	public void syncFromFields()
	{
		syncConfig( false, false );
	}

	/**
	 * Synchronize the three copies of the data
	 * 1) loadConfigFromFile && readFieldsFromConfig -> initialize everything from the disk file.
	 * 2) !loadConfigFromFile && readFieldsFromConfig --> copy everything from the config file (altered by GUI).
	 * 3) !loadConfigFromFile && !readFieldsFromConfig --> copy everything from the native fields.
	 *
	 * @param loadConfigFromFile
	 *            if true, load the config field from the configuration file on disk.
	 * @param readFieldsFromConfig
	 *            if true, reload the member variables from the config field.
	 */
	public void syncConfig( boolean loadConfigFromFile, boolean readFieldsFromConfig )
	{
		// Load config file
		if( loadConfigFromFile )
			config.load();

		// Define config properties
		//@formatter:off
		// Blocks
		final Property propCrateEnabled = config.get( CATEGORY_BLOCKS, "crateEnabled", true )
				.setLanguageKey( "tile.betterstorage.crate.name" ).setRequiresMcRestart( true );
		final Property propReinforcedChestEnabled = config.get( CATEGORY_BLOCKS, "reinforcedChestEnabled", true )
				.setLanguageKey( "tile.betterstorage.reinforced_chest.name" ).setRequiresMcRestart( true );
		final Property propLockerEnabled = config.get( CATEGORY_BLOCKS, "lockerEnabled", true )
				.setLanguageKey( "tile.betterstorage.locker.name" ).setRequiresMcRestart( true );
		final Property propReinforcedLockerEnabled = config.get( CATEGORY_BLOCKS, "reinforcedLockerEnabled", true )
				.setLanguageKey( "tile.betterstorage.reinforced_locker.name" ).setRequiresMcRestart( true );
		final Property propLockableDoorEnabled = config.get(  CATEGORY_BLOCKS, "lockableDoorEnabled", true )
				.setLanguageKey( "tile.betterstorage.lockable_door.name" ).setRequiresMcRestart( true );
		final Property propFlintBlockEnabled = config.get( CATEGORY_BLOCKS, "flintBlockEnabled", true )
				.setLanguageKey( "tile.betterstorage.flint_block.name" ).setRequiresMcRestart( true );
		final Property propCardboardBoxEnabled = config.get( CATEGORY_BLOCKS, "cardboardBoxEnabled", true )
				.setLanguageKey( "tile.betterstorage.cardboard_box.name" ).setRequiresMcRestart( true );

		// Items
		final Property propKeyEnabled = config.get( CATEGORY_ITEMS, "keyEnabled", true )
				.setLanguageKey( "item.betterstorage.key.name" ).setRequiresMcRestart( true );
		final Property propMasterKeyEnabled = config.get( CATEGORY_ITEMS, "masterKeyEnabled", true )
				.setLanguageKey( "item.betterstorage.master_key.name" ).setRequiresMcRestart( true );
		final Property propKeyringEnabled = config.get( CATEGORY_ITEMS, "keyringEnabled", true )
				.setLanguageKey( "item.betterstorage.keyring.name" ).setRequiresMcRestart( true );
		final Property propLockEnabled = config.get( CATEGORY_ITEMS, "lockEnabled", true )
				.setLanguageKey( "item.betterstorage.lock.name" ).setRequiresMcRestart( true );
		final Property propCardboardSheetEnabled = config.get( CATEGORY_ITEMS, "cardboardSheetEnabled", true )
				.setLanguageKey( "item.betterstorage.cardboard_sheet.name" ).setRequiresMcRestart( true );

		// Cardboard items
		final Property propCardboardHelmetEnabled = config.get( CATEGORY_ITEMS, "cardboardHelmetEnabled", true )
				.setLanguageKey( "item.betterstorage.cardboard_helmet.name" ).setRequiresMcRestart( true );
		final Property propCardboardChestplateEnabled = config.get( CATEGORY_ITEMS, "cardboardChestplateEnabled", true )
				.setLanguageKey( "item.betterstorage.cardboard_chestplate.name" ).setRequiresMcRestart( true );
		final Property propCardboardLeggingsEnabled = config.get( CATEGORY_ITEMS, "cardboardLeggingsEnabled", true )
				.setLanguageKey( "item.betterstorage.cardboard_leggings.name" ).setRequiresMcRestart( true );
		final Property propCardboardBootsEnabled = config.get( CATEGORY_ITEMS, "cardboardBootsEnabled", true )
				.setLanguageKey( "item.betterstorage.cardboard_boots.name" ).setRequiresMcRestart( true );
		final Property propCardboardSwordEnabled = config.get( CATEGORY_ITEMS, "cardboardSwordEnabled", true )
				.setLanguageKey( "item.betterstorage.cardboard_sword.name" ).setRequiresMcRestart( true );
		final Property propCardboardPickaxeEnabled = config.get( CATEGORY_ITEMS, "cardboardPickaxeEnabled", true )
				.setLanguageKey( "item.betterstorage.cardboard_pickaxe.name" ).setRequiresMcRestart( true );
		final Property propCardboardShovelEnabled = config.get( CATEGORY_ITEMS, "cardboardShovelEnabled", true )
				.setLanguageKey( "item.betterstorage.cardboard_shovel.name" ).setRequiresMcRestart( true );
		final Property propCardboardAxeEnabled = config.get( CATEGORY_ITEMS, "cardboardAxeEnabled", true )
				.setLanguageKey( "item.betterstorage.cardboard_axe.name" ).setRequiresMcRestart( true );
		final Property propCardboardHoeEnabled = config.get( CATEGORY_ITEMS, "cardboardHoeEnabled", true )
				.setLanguageKey( "item.betterstorage.cardboard_hoe.name" ).setRequiresMcRestart( true );

		// Reinforced chest settings
		final Property propReinforcedColumns = config.get( Configuration.CATEGORY_GENERAL, "reinforcedColumns", "13",
				"Number of columns in reinforced chests and lockers. Valid values are 9, 11 and 13.", new String[]{ "9", "11", "13" } )
				.setLanguageKey( "config.betterstorage.general.reinforcedColumns" );

		// Crate settings
		final Property propCrateInventoryInterface = config.get( Configuration.CATEGORY_GENERAL, "enableCrateInventoryInterface", true,
				"If enabled, exposes a special block view of crates, so items can be moved in and out by automated systems." )
				.setLanguageKey( "config.betterstorage.general.enableCrateInventoryInterface" );

		// Cardboard box settings
		final Property propCardboardBoxRows = config.get( Configuration.CATEGORY_GENERAL, "cardboardBoxRows", 1,
				"Number of rows in cardboard boxes. Valid values are 1 to 3.", 1, 3 )
				.setLanguageKey( "config.betterstorage.general.cardboardBoxRows" );
		final Property propCardboardBoxUses = config.get( Configuration.CATEGORY_GENERAL, "cardboardBoxUses", 4,
				"Number of times cardboard boxes can be picked up with items before they break. Use 0 for infinite uses.", 0, Integer.MAX_VALUE )
				.setLanguageKey( "config.betterstorage.general.cardboardBoxUses" );
		final Property propCardboardBoxShowContents = config.get( Configuration.CATEGORY_GENERAL, "cardboardBoxShowContents", true,
				"If disabled, doesn't show cardboard box contents in their tooltips." )
				.setLanguageKey( "config.betterstorage.general.cardboardBoxShowContents" );

		// Miscellaneous settings
		final Property propEnableHelpTooltips = config.get( Configuration.CATEGORY_GENERAL, "enableHelpTooltips", true,
				"If enabled, shows tooltips on some items to help players who're new to the mod." )
				.setLanguageKey( "config.betterstorage.general.enableHelpTooltips" );
		final Property propEnableWarningMessages = config.get( Configuration.CATEGORY_GENERAL, "enableWarningMessages", false,
				"If disabled, prevents certain warning messages from being logged to the console." )
				.setLanguageKey( "config.betterstorage.general.enableWarningMessages" );

		// Define property order
		config.setCategoryPropertyOrder( Configuration.CATEGORY_GENERAL,
				Arrays.asList( new String[] { "reinforcedColumns",
											  "enableCrateInventoryInterface",
											  "cardboardBoxRows",
											  "cardboardBoxUses",
											  "cardboardBoxShowContents",
											  "enableHelpTooltips",
											  "enableWarningMessages" } ) );
		config.setCategoryPropertyOrder( CATEGORY_BLOCKS,
				Arrays.asList( new String[] { "crateEnabled",
											  "reinforcedChestEnabled",
											  "lockerEnabled",
											  "reinforcedLockerEnabled",
											  "lockableDoorEnabled",
											  "flintBlockEnabled",
											  "cardboardBoxEnabled" } ) );
		config.setCategoryPropertyOrder( CATEGORY_ITEMS,
				Arrays.asList( new String[] { "keyEnabled",
											  "masterKeyEnabled",
											  "keyringEnabled",
											  "lockEnabled",
											  "cardboardSheetEnabled",
											  "cardboardHelmetEnabled",
											  "cardboardChestplateEnabled",
											  "cardboardLeggingsEnabled",
											  "cardboardBootsEnabled",
											  "cardboardSwordEnabled",
											  "cardboardPickaxeEnabled",
											  "cardboardShovelEnabled",
											  "cardboardAxeEnabled",
											  "cardboardHoeEnabled" } ) );
		//@formatter:on

		// Read properties
		if( readFieldsFromConfig )
		{
			crateEnabled = propCrateEnabled.getBoolean();
			reinforcedChestEnabled = propReinforcedChestEnabled.getBoolean();
			lockerEnabled = propLockerEnabled.getBoolean();
			reinforcedLockerEnabled = propReinforcedLockerEnabled.getBoolean();
			lockableDoorEnabled = propLockableDoorEnabled.getBoolean();
			flintBlockEnabled = propFlintBlockEnabled.getBoolean();
			cardboardBoxEnabled = propCardboardBoxEnabled.getBoolean();

			cardboardHelmetEnabled = propCardboardHelmetEnabled.getBoolean();
			cardboardChestplateEnabled = propCardboardChestplateEnabled.getBoolean();
			cardboardLeggingsEnabled = propCardboardLeggingsEnabled.getBoolean();
			cardboardBootsEnabled = propCardboardBootsEnabled.getBoolean();
			cardboardSwordEnabled = propCardboardSwordEnabled.getBoolean();
			cardboardPickaxeEnabled = propCardboardPickaxeEnabled.getBoolean();
			cardboardShovelEnabled = propCardboardShovelEnabled.getBoolean();
			cardboardAxeEnabled = propCardboardAxeEnabled.getBoolean();
			cardboardHoeEnabled = propCardboardHoeEnabled.getBoolean();

			keyEnabled = propKeyEnabled.getBoolean();
			masterKeyEnabled = propMasterKeyEnabled.getBoolean();
			keyringEnabled = propKeyringEnabled.getBoolean();
			lockEnabled = propLockEnabled.getBoolean();
			cardboardSheetEnabled = propCardboardSheetEnabled.getBoolean();

			reinforcedColumns = propReinforcedColumns.getInt();
			enableCrateInventoryInterface = propCrateInventoryInterface.getBoolean();
			cardboardBoxRows = propCardboardBoxRows.getInt();
			cardboardBoxUses = propCardboardBoxUses.getInt();
			cardboardBoxShowContents = propCardboardBoxShowContents.getBoolean();
			enableHelpTooltips = propEnableHelpTooltips.getBoolean();
			enableWarningMessages = propEnableWarningMessages.getBoolean();
		}

		// Save properties to file
		propCrateEnabled.set( crateEnabled );
		propReinforcedChestEnabled.set( reinforcedChestEnabled );
		propLockerEnabled.set( lockerEnabled );
		propReinforcedLockerEnabled.set( reinforcedLockerEnabled );
		propLockableDoorEnabled.set( lockableDoorEnabled );
		propFlintBlockEnabled.set( flintBlockEnabled );
		propCardboardBoxEnabled.set( cardboardBoxEnabled );

		propKeyEnabled.set( keyEnabled );
		propMasterKeyEnabled.set( masterKeyEnabled );
		propKeyringEnabled.set( keyringEnabled );
		propLockEnabled.set( lockEnabled );
		propCardboardSheetEnabled.set( cardboardSheetEnabled );

		propReinforcedColumns.set( reinforcedColumns );
		propReinforcedChestEnabled.set( enableCrateInventoryInterface );
		propCardboardBoxRows.set( cardboardBoxRows );
		propCardboardBoxUses.set( cardboardBoxUses );
		propCardboardBoxShowContents.set( cardboardBoxShowContents );
		propEnableHelpTooltips.set( enableHelpTooltips );
		propEnableWarningMessages.set( enableWarningMessages );

		if( config.hasChanged() )
			config.save();
	}
}
