package io.github.tehstoneman.betterstorage.common.block;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class BetterStorageBlocks
{
	public static BlockCrate				CRATE;
	public static BlockReinforcedChest		REINFORCED_CHEST;
	public static BlockLocker				LOCKER;
	public static BlockReinforcedLocker		REINFORCED_LOCKER;
	public static BlockCardboardBox			CARDBOARD_BOX;
	public static BlockCardboardBoxColored	CARDBOARD_BOX_COLORED;
	// public static BlockPresent PRESENT;

	// public static BlockCraftingStation CRAFTING_STATION;
	public static BlockFlintBlock			BLOCK_FLINT;
	// public static BlockLockableDoor LOCKABLE_DOOR;

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

		/*
		 * if( BetterStorage.globalConfig.getBoolean( GlobalConfig.flintBlockEnabled ) )
		 * {
		 * BLOCK_FLINT = (BlockFlintBlock)new BlockFlintBlock().setUnlocalizedName( ModInfo.modId + ".block_flint" );
		 * GameRegistry.register( BLOCK_FLINT.setRegistryName( "block_flint" ) );
		 * GameRegistry.register( new ItemBlock( BLOCK_FLINT ).setRegistryName( BLOCK_FLINT.getRegistryName() ) );
		 * }
		 */
		/*
		 * if( BetterStorage.globalConfig.getBoolean( GlobalConfig.lockableDoorEnabled ) )
		 * {
		 * LOCKABLE_DOOR = (BlockLockableDoor)new BlockLockableDoor().setUnlocalizedName( ModInfo.modId + ".lockable_door" );
		 * GameRegistry.register( LOCKABLE_DOOR.setRegistryName( "lockable_door" ) );
		 * }
		 */

		// Addon.initializeTilesAll();
	}

	@SideOnly( Side.CLIENT )
	public static void registerItemModels()
	{
		if( CRATE != null )
			CRATE.registerItemModels();
		if( REINFORCED_CHEST != null )
			REINFORCED_CHEST.registerItemModels();

		/*
		 * if( BetterStorage.globalConfig.getBoolean( GlobalConfig.reinforcedChestEnabled ) )
		 * for( final EnumReinforced material : EnumReinforced.values() )
		 * if( material != EnumReinforced.SPECIAL )
		 * ModelLoader.setCustomModelResourceLocation( Item.getItemFromBlock( REINFORCED_CHEST ), material.getMetadata(),
		 * new ModelResourceLocation( REINFORCED_CHEST.getRegistryName() + "_" + material.getName(), "inventory" ) );
		 */

		/*
		 * if( BetterStorage.globalConfig.getBoolean( GlobalConfig.lockerEnabled ) )
		 * ModelLoader.setCustomModelResourceLocation( Item.getItemFromBlock( LOCKER ), 0,
		 * new ModelResourceLocation( LOCKER.getRegistryName(), "inventory" ) );
		 */

		/*
		 * if( BetterStorage.globalConfig.getBoolean( GlobalConfig.reinforcedLockerEnabled ) )
		 * for( final EnumReinforced material : EnumReinforced.values() )
		 * if( material != EnumReinforced.SPECIAL )
		 * ModelLoader.setCustomModelResourceLocation( Item.getItemFromBlock( REINFORCED_LOCKER ), material.getMetadata(),
		 * new ModelResourceLocation( REINFORCED_LOCKER.getRegistryName() + "_" + material.getName(), "inventory" ) );
		 */

		/*
		 * if( BetterStorage.globalConfig.getBoolean( GlobalConfig.cardboardBoxEnabled ) )
		 * {
		 * ModelLoader.setCustomModelResourceLocation( Item.getItemFromBlock( CARDBOARD_BOX ), 0,
		 * new ModelResourceLocation( CARDBOARD_BOX.getRegistryName(), "inventory" ) );
		 *
		 * for( final EnumDyeColor color : EnumDyeColor.values() )
		 * ModelLoader.setCustomModelResourceLocation( Item.getItemFromBlock( CARDBOARD_BOX_COLORED ), color.getMetadata(),
		 * new ModelResourceLocation( CARDBOARD_BOX.getRegistryName() + "_" + color.getName(), "inventory" ) );
		 * }
		 */

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

		/*
		 * if( BetterStorage.globalConfig.getBoolean( GlobalConfig.flintBlockEnabled ) )
		 * ModelLoader.setCustomModelResourceLocation( Item.getItemFromBlock( BLOCK_FLINT ), 0,
		 * new ModelResourceLocation( BLOCK_FLINT.getRegistryName(), "inventory" ) );
		 */
	}
}
