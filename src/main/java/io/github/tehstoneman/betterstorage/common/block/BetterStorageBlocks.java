package io.github.tehstoneman.betterstorage.common.block;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.addon.Addon;
import io.github.tehstoneman.betterstorage.common.block.BlockLockable.EnumReinforced;
import io.github.tehstoneman.betterstorage.common.item.ItemBlockCrate;
import io.github.tehstoneman.betterstorage.common.item.ItemBlockLockable;
import io.github.tehstoneman.betterstorage.config.GlobalConfig;
import io.github.tehstoneman.betterstorage.tile.TileCardboardBox;
import io.github.tehstoneman.betterstorage.tile.TileCraftingStation;
import io.github.tehstoneman.betterstorage.tile.TileFlintBlock;
import io.github.tehstoneman.betterstorage.tile.TileLockableDoor;
import io.github.tehstoneman.betterstorage.tile.TilePresent;
import io.github.tehstoneman.betterstorage.tile.stand.TileArmorStand;
import io.github.tehstoneman.betterstorage.utils.MiscUtils;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class BetterStorageBlocks
{
	public static BlockCrate			CRATE;
	public static BlockReinforcedChest	REINFORCED_CHEST;
	public static BlockLocker			LOCKER;
	public static BlockReinforcedLocker	REINFORCED_LOCKER;
	public static TileArmorStand		armorStand;

	public static TileCardboardBox		cardboardBox;
	public static TileCraftingStation	craftingStation;
	public static TileFlintBlock		flintBlock;
	public static TileLockableDoor		lockableDoor;
	public static TilePresent			present;

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

		armorStand = MiscUtils.conditionalNew( TileArmorStand.class, GlobalConfig.armorStandEnabled );
		cardboardBox = MiscUtils.conditionalNew( TileCardboardBox.class, GlobalConfig.cardboardBoxEnabled );
		craftingStation = MiscUtils.conditionalNew( TileCraftingStation.class, GlobalConfig.craftingStationEnabled );
		flintBlock = MiscUtils.conditionalNew( TileFlintBlock.class, GlobalConfig.flintBlockEnabled );
		lockableDoor = MiscUtils.conditionalNew( TileLockableDoor.class, GlobalConfig.lockableDoorEnabled );
		present = MiscUtils.conditionalNew( TilePresent.class, GlobalConfig.presentEnabled );

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

		armorStand.registerItemModels();
		cardboardBox.registerItemModels();
		craftingStation.registerItemModels();
		flintBlock.registerItemModels();
		lockableDoor.registerItemModels();
		present.registerItemModels();
	}
}
