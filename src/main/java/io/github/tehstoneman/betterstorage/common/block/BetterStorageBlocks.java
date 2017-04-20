package io.github.tehstoneman.betterstorage.common.block;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.addon.Addon;
import io.github.tehstoneman.betterstorage.common.block.BlockLockable.EnumReinforced;
import io.github.tehstoneman.betterstorage.common.item.ItemBlockCardboardBox;
import io.github.tehstoneman.betterstorage.common.item.ItemBlockCardboardBoxColored;
import io.github.tehstoneman.betterstorage.common.item.ItemBlockCrate;
import io.github.tehstoneman.betterstorage.common.item.ItemBlockLockable;
import io.github.tehstoneman.betterstorage.config.GlobalConfig;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
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
		if( BetterStorage.globalConfig.getBoolean( GlobalConfig.crateEnabled ) )
		{
			CRATE = (BlockCrate)new BlockCrate().setUnlocalizedName( ModInfo.modId + ".crate" );
			GameRegistry.register( CRATE.setRegistryName( "crate" ) );
			GameRegistry.register( new ItemBlockCrate( CRATE ).setRegistryName( CRATE.getRegistryName() ) );
		}
		if( BetterStorage.globalConfig.getBoolean( GlobalConfig.reinforcedChestEnabled ) )
		{
			REINFORCED_CHEST = (BlockReinforcedChest)new BlockReinforcedChest().setUnlocalizedName( ModInfo.modId + ".reinforced_chest" );
			GameRegistry.register( REINFORCED_CHEST.setRegistryName( "reinforced_chest" ) );
			GameRegistry.register( new ItemBlockLockable( REINFORCED_CHEST ).setRegistryName( REINFORCED_CHEST.getRegistryName() ) );
		}
		if( BetterStorage.globalConfig.getBoolean( GlobalConfig.lockerEnabled ) )
		{
			LOCKER = (BlockLocker)new BlockLocker().setUnlocalizedName( ModInfo.modId + ".locker" );
			GameRegistry.register( LOCKER.setRegistryName( "locker" ) );
			GameRegistry.register( new ItemBlock( LOCKER ).setRegistryName( LOCKER.getRegistryName() ) );
		}
		if( BetterStorage.globalConfig.getBoolean( GlobalConfig.reinforcedLockerEnabled ) )
		{
			REINFORCED_LOCKER = (BlockReinforcedLocker)new BlockReinforcedLocker().setUnlocalizedName( ModInfo.modId + ".reinforced_locker" );
			GameRegistry.register( REINFORCED_LOCKER.setRegistryName( "reinforced_locker" ) );
			GameRegistry.register( new ItemBlockLockable( REINFORCED_LOCKER ).setRegistryName( REINFORCED_LOCKER.getRegistryName() ) );
		}
		if( BetterStorage.globalConfig.getBoolean( GlobalConfig.cardboardBoxEnabled ) )
		{
			CARDBOARD_BOX = (BlockCardboardBox)new BlockCardboardBox().setUnlocalizedName( ModInfo.modId + ".cardboard_box" );
			GameRegistry.register( CARDBOARD_BOX.setRegistryName( "cardboard_box" ) );
			GameRegistry.register( new ItemBlockCardboardBox( CARDBOARD_BOX ).setRegistryName( CARDBOARD_BOX.getRegistryName() ) );

			CARDBOARD_BOX_COLORED = (BlockCardboardBoxColored)new BlockCardboardBoxColored()
					.setUnlocalizedName( ModInfo.modId + ".cardboard_box_colored" );
			GameRegistry.register( CARDBOARD_BOX_COLORED.setRegistryName( "cardboard_box_colored" ) );
			GameRegistry
					.register( new ItemBlockCardboardBoxColored( CARDBOARD_BOX_COLORED ).setRegistryName( CARDBOARD_BOX_COLORED.getRegistryName() ) );
		}
		/*
		 * if( BetterStorage.globalConfig.getBoolean( GlobalConfig.presentEnabled ) )
		 * {
		 * PRESENT = (BlockPresent)new BlockPresent().setUnlocalizedName( ModInfo.modId + ".present" );
		 * GameRegistry.register( PRESENT.setRegistryName( "present" ) );
		 * GameRegistry.register( new ItemBlockPresent( PRESENT ).setRegistryName( PRESENT.getRegistryName() ) );
		 * }
		 */

		/*
		 * if( BetterStorage.globalConfig.getBoolean( GlobalConfig.craftingStationEnabled ) )
		 * {
		 * CRAFTING_STATION = (BlockCraftingStation)new BlockCraftingStation().setUnlocalizedName( ModInfo.modId + ".crafting_station" );
		 * GameRegistry.register( CRAFTING_STATION.setRegistryName( "crafting_station" ) );
		 * GameRegistry.register( new ItemBlockPresent( CRAFTING_STATION ).setRegistryName( CRAFTING_STATION.getRegistryName() ) );
		 * }
		 */

		if( BetterStorage.globalConfig.getBoolean( GlobalConfig.flintBlockEnabled ) )
		{
			BLOCK_FLINT = (BlockFlintBlock)new BlockFlintBlock().setUnlocalizedName( ModInfo.modId + ".block_flint" );
			GameRegistry.register( BLOCK_FLINT.setRegistryName( "block_flint" ) );
			GameRegistry.register( new ItemBlock( BLOCK_FLINT ).setRegistryName( BLOCK_FLINT.getRegistryName() ) );
		}
		/*
		 * if( BetterStorage.globalConfig.getBoolean( GlobalConfig.lockableDoorEnabled ) )
		 * {
		 * LOCKABLE_DOOR = (BlockLockableDoor)new BlockLockableDoor().setUnlocalizedName( ModInfo.modId + ".lockable_door" );
		 * GameRegistry.register( LOCKABLE_DOOR.setRegistryName( "lockable_door" ) );
		 * }
		 */

		Addon.initializeTilesAll();
	}

	@SideOnly( Side.CLIENT )
	public static void registerItemModels()
	{
		if( BetterStorage.globalConfig.getBoolean( GlobalConfig.crateEnabled ) )
			ModelLoader.setCustomModelResourceLocation( Item.getItemFromBlock( CRATE ), 0,
					new ModelResourceLocation( CRATE.getRegistryName(), "inventory" ) );

		if( BetterStorage.globalConfig.getBoolean( GlobalConfig.reinforcedChestEnabled ) )
			for( final EnumReinforced material : EnumReinforced.values() )
				if( material != EnumReinforced.SPECIAL )
					ModelLoader.setCustomModelResourceLocation( Item.getItemFromBlock( REINFORCED_CHEST ), material.getMetadata(),
							new ModelResourceLocation( REINFORCED_CHEST.getRegistryName() + "_" + material.getName(), "inventory" ) );

		if( BetterStorage.globalConfig.getBoolean( GlobalConfig.lockerEnabled ) )
			ModelLoader.setCustomModelResourceLocation( Item.getItemFromBlock( LOCKER ), 0,
					new ModelResourceLocation( LOCKER.getRegistryName(), "inventory" ) );

		if( BetterStorage.globalConfig.getBoolean( GlobalConfig.reinforcedLockerEnabled ) )
			for( final EnumReinforced material : EnumReinforced.values() )
				if( material != EnumReinforced.SPECIAL )
					ModelLoader.setCustomModelResourceLocation( Item.getItemFromBlock( REINFORCED_LOCKER ), material.getMetadata(),
							new ModelResourceLocation( REINFORCED_LOCKER.getRegistryName() + "_" + material.getName(), "inventory" ) );

		if( BetterStorage.globalConfig.getBoolean( GlobalConfig.cardboardBoxEnabled ) )
		{
			ModelLoader.setCustomModelResourceLocation( Item.getItemFromBlock( CARDBOARD_BOX ), 0,
					new ModelResourceLocation( CARDBOARD_BOX.getRegistryName(), "inventory" ) );

			for( final EnumDyeColor color : EnumDyeColor.values() )
				ModelLoader.setCustomModelResourceLocation( Item.getItemFromBlock( CARDBOARD_BOX_COLORED ), color.getMetadata(),
						new ModelResourceLocation( CARDBOARD_BOX.getRegistryName() + "_" + color.getName(), "inventory" ) );
		}

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

		if( BetterStorage.globalConfig.getBoolean( GlobalConfig.flintBlockEnabled ) )
			ModelLoader.setCustomModelResourceLocation( Item.getItemFromBlock( BLOCK_FLINT ), 0,
					new ModelResourceLocation( BLOCK_FLINT.getRegistryName(), "inventory" ) );
	}
}
