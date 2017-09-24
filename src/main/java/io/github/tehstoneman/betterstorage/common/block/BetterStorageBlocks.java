package io.github.tehstoneman.betterstorage.common.block;

import io.github.tehstoneman.betterstorage.BetterStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class BetterStorageBlocks
{
	public static BlockCrate			CRATE				= new BlockCrate();
	public static BlockReinforcedChest	REINFORCED_CHEST	= new BlockReinforcedChest();
	public static BlockLocker			LOCKER				= new BlockLocker();
	public static BlockReinforcedLocker	REINFORCED_LOCKER	= new BlockReinforcedLocker();
	public static BlockFlintBlock		BLOCK_FLINT			= new BlockFlintBlock();
	public static BlockLockableDoor		LOCKABLE_DOOR		= new BlockLockableDoor();
	public static BlockCardboardBox		CARDBOARD_BOX		= new BlockCardboardBox();
	// public static BlockPresent PRESENT;

	// public static BlockCraftingStation CRAFTING_STATION;

	public static void registerBlocks()
	{
		/*
		 * if( BetterStorage.globalConfig.getBoolean( GlobalConfig.craftingStationEnabled ) )
		 * {
		 * CRAFTING_STATION = (BlockCraftingStation)new BlockCraftingStation().setUnlocalizedName( ModInfo.modId + ".crafting_station" );
		 * GameRegistry.register( CRAFTING_STATION.setRegistryName( "crafting_station" ) );
		 * GameRegistry.register( new ItemBlockPresent( CRAFTING_STATION ).setRegistryName( CRAFTING_STATION.getRegistryName() ) );
		 * }
		 */

		// Addon.initializeTilesAll();
	}

	@SideOnly( Side.CLIENT )
	public static void registerItemModels()
	{
		if( BetterStorage.config.crateEnabled )
			CRATE.registerItemModels();
		if( BetterStorage.config.reinforcedChestEnabled )
			REINFORCED_CHEST.registerItemModels();
		if( BetterStorage.config.lockerEnabled )
		{
			LOCKER.registerItemModels();
			if( BetterStorage.config.reinforcedLockerEnabled )
				REINFORCED_LOCKER.registerItemModels();
		}
		if( BetterStorage.config.flintBlockEnabled )
			BLOCK_FLINT.registerItemModels();
		if( BetterStorage.config.cardboardBoxEnabled )
			CARDBOARD_BOX.registerItemModels();

		/*
		 * if( BetterStorage.globalConfig.getBoolean( GlobalConfig.presentEnabled ) )
		 * ModelLoader.setCustomModelResourceLocation( Item.getItemFromBlock( PRESENT ), 0,
		 * new ModelResourceLocation( PRESENT.getRegistryName(), "inventory" ) );
		 */

		/*
		 * if( BetterStorage.globalConfig.getBoolean( GlobalConfig.craftingStationEnabled ) )
		 * ModelLoader.setCustomModelResourceLocation( Item.getItemFromBlock( CRAFTING_STATION ), 0,
		 * new ModelResourceLocation( CRAFTING_STATION.getRegistryName(), "inventory" ) );
		 */

	}
}
