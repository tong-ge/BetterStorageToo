package io.github.tehstoneman.betterstorage.common.item;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.addon.Addon;
import io.github.tehstoneman.betterstorage.common.item.cardboard.ItemCardboardArmor;
import io.github.tehstoneman.betterstorage.common.item.cardboard.ItemCardboardAxe;
import io.github.tehstoneman.betterstorage.common.item.cardboard.ItemCardboardHoe;
import io.github.tehstoneman.betterstorage.common.item.cardboard.ItemCardboardPickaxe;
import io.github.tehstoneman.betterstorage.common.item.cardboard.ItemCardboardSheet;
import io.github.tehstoneman.betterstorage.common.item.cardboard.ItemCardboardShovel;
import io.github.tehstoneman.betterstorage.common.item.cardboard.ItemCardboardSword;
import io.github.tehstoneman.betterstorage.common.item.locking.ItemKey;
import io.github.tehstoneman.betterstorage.common.item.locking.ItemKeyring;
import io.github.tehstoneman.betterstorage.common.item.locking.ItemLock;
import io.github.tehstoneman.betterstorage.common.item.locking.ItemMasterKey;
import io.github.tehstoneman.betterstorage.config.GlobalConfig;
import io.github.tehstoneman.betterstorage.item.ItemBucketSlime;
import io.github.tehstoneman.betterstorage.item.ItemPresentBook;
import io.github.tehstoneman.betterstorage.utils.MiscUtils;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public final class BetterStorageItems
{
	public static ItemKey				KEY;
	public static ItemMasterKey			MASTER_KEY;
	public static ItemKeyring			KEYRING;
	public static ItemLock				LOCK;
	public static ItemCardboardSheet	CARDBOARD_SHEET;

	public static ItemCardboardArmor	CARDBOARD_HELMET;
	public static ItemCardboardArmor	CARDBOARD_CHESTPLATE;
	public static ItemCardboardArmor	CARDBOARD_LEGGINGS;
	public static ItemCardboardArmor	CARDBOARD_BOOTS;

	public static ItemCardboardSword	CARDBOARD_SWORD;
	public static ItemCardboardPickaxe	CARDBOARD_PICKAXE;
	public static ItemCardboardShovel	CARDBOARD_SHOVEL;
	public static ItemCardboardAxe		CARDBOARD_AXE;
	public static ItemCardboardHoe		CARDBOARD_HOE;

	// public static ItemDrinkingHelmet drinkingHelmet;
	public static ItemBucketSlime		slimeBucket;
	public static ItemPresentBook		presentBook;

	public static boolean				anyCardboardItemsEnabled;

	public static void registerItems()
	{
		if( BetterStorage.globalConfig.getBoolean( GlobalConfig.keyEnabled ) )
		{
			KEY = (ItemKey)new ItemKey().setUnlocalizedName( ModInfo.modId + ".key" );
			GameRegistry.register( KEY.setRegistryName( "key" ) );
		}
		if( BetterStorage.globalConfig.getBoolean( GlobalConfig.masterKeyEnabled ) )
		{
			MASTER_KEY = (ItemMasterKey)new ItemMasterKey().setUnlocalizedName( ModInfo.modId + ".master_key" );
			GameRegistry.register( MASTER_KEY.setRegistryName( "master_key" ) );
		}
		if( BetterStorage.globalConfig.getBoolean( GlobalConfig.keyringEnabled ) )
		{
			KEYRING = (ItemKeyring)new ItemKeyring().setUnlocalizedName( ModInfo.modId + ".keyring" );
			GameRegistry.register( KEYRING.setRegistryName( "keyring" ) );
		}
		if( BetterStorage.globalConfig.getBoolean( GlobalConfig.lockEnabled ) )
		{
			LOCK = (ItemLock)new ItemLock().setUnlocalizedName( ModInfo.modId + ".lock" );
			GameRegistry.register( LOCK.setRegistryName( "lock" ) );
		}

		if( BetterStorage.globalConfig.getBoolean( GlobalConfig.cardboardSheetEnabled ) )
		{
			CARDBOARD_SHEET = (ItemCardboardSheet)new ItemCardboardSheet().setUnlocalizedName( ModInfo.modId + ".cardboard_sheet" );
			GameRegistry.register( CARDBOARD_SHEET.setRegistryName( "cardboard_sheet" ) );
		}

		if( BetterStorage.globalConfig.getBoolean( GlobalConfig.cardboardHelmetEnabled ) )
		{
			CARDBOARD_HELMET = (ItemCardboardArmor)new ItemCardboardArmor( EntityEquipmentSlot.HEAD )
					.setUnlocalizedName( ModInfo.modId + ".cardboard_helmet" );
			GameRegistry.register( CARDBOARD_HELMET.setRegistryName( "cardboard_helmet" ) );
		}
		if( BetterStorage.globalConfig.getBoolean( GlobalConfig.cardboardChestplateEnabled ) )
		{
			CARDBOARD_CHESTPLATE = (ItemCardboardArmor)new ItemCardboardArmor( EntityEquipmentSlot.CHEST )
					.setUnlocalizedName( ModInfo.modId + ".cardboard_chestplate" );
			GameRegistry.register( CARDBOARD_CHESTPLATE.setRegistryName( "cardboard_chestplate" ) );
		}
		if( BetterStorage.globalConfig.getBoolean( GlobalConfig.cardboardLeggingsEnabled ) )
		{
			CARDBOARD_LEGGINGS = (ItemCardboardArmor)new ItemCardboardArmor( EntityEquipmentSlot.LEGS )
					.setUnlocalizedName( ModInfo.modId + ".cardboard_leggings" );
			GameRegistry.register( CARDBOARD_LEGGINGS.setRegistryName( "cardboard_leggings" ) );
		}
		if( BetterStorage.globalConfig.getBoolean( GlobalConfig.cardboardBootsEnabled ) )
		{
			CARDBOARD_BOOTS = (ItemCardboardArmor)new ItemCardboardArmor( EntityEquipmentSlot.FEET )
					.setUnlocalizedName( ModInfo.modId + ".cardboard_boots" );
			GameRegistry.register( CARDBOARD_BOOTS.setRegistryName( "cardboard_boots" ) );
		}

		if( BetterStorage.globalConfig.getBoolean( GlobalConfig.cardboardSwordEnabled ) )
		{
			CARDBOARD_SWORD = (ItemCardboardSword)new ItemCardboardSword().setUnlocalizedName( ModInfo.modId + ".cardboard_sword" );
			GameRegistry.register( CARDBOARD_SWORD.setRegistryName( "cardboard_sword" ) );
		}
		if( BetterStorage.globalConfig.getBoolean( GlobalConfig.cardboardShovelEnabled ) )
		{
			CARDBOARD_SHOVEL = (ItemCardboardShovel)new ItemCardboardShovel().setUnlocalizedName( ModInfo.modId + ".cardboard_shovel" );
			GameRegistry.register( CARDBOARD_SHOVEL.setRegistryName( "cardboard_shovel" ) );
		}
		if( BetterStorage.globalConfig.getBoolean( GlobalConfig.cardboardPickaxeEnabled ) )
		{
			CARDBOARD_PICKAXE = (ItemCardboardPickaxe)new ItemCardboardPickaxe().setUnlocalizedName( ModInfo.modId + ".cardboard_pickaxe" );
			GameRegistry.register( CARDBOARD_PICKAXE.setRegistryName( "cardboard_pickaxe" ) );
		}
		if( BetterStorage.globalConfig.getBoolean( GlobalConfig.cardboardAxeEnabled ) )
		{
			CARDBOARD_AXE = (ItemCardboardAxe)new ItemCardboardAxe().setUnlocalizedName( ModInfo.modId + ".cardboard_axe" );
			GameRegistry.register( CARDBOARD_AXE.setRegistryName( "cardboard_axe" ) );
		}
		if( BetterStorage.globalConfig.getBoolean( GlobalConfig.cardboardHoeEnabled ) )
		{
			CARDBOARD_HOE = (ItemCardboardHoe)new ItemCardboardHoe().setUnlocalizedName( ModInfo.modId + ".cardboard_hoe" );
			GameRegistry.register( CARDBOARD_HOE.setRegistryName( "cardboard_hoe" ) );
		}

		// drinkingHelmet = MiscUtils.conditionalNew( ItemDrinkingHelmet.class, GlobalConfig.drinkingHelmetEnabled );
		slimeBucket = MiscUtils.conditionalNew( ItemBucketSlime.class, GlobalConfig.slimeBucketEnabled );
		presentBook = new ItemPresentBook();

		anyCardboardItemsEnabled = BetterStorageItems.CARDBOARD_HELMET != null || BetterStorageItems.CARDBOARD_CHESTPLATE != null
				|| BetterStorageItems.CARDBOARD_LEGGINGS != null || BetterStorageItems.CARDBOARD_BOOTS != null
				|| BetterStorageItems.CARDBOARD_SWORD != null || BetterStorageItems.CARDBOARD_PICKAXE != null
				|| BetterStorageItems.CARDBOARD_AXE != null || BetterStorageItems.CARDBOARD_SHOVEL != null
				|| BetterStorageItems.CARDBOARD_HOE != null;

		if( CARDBOARD_SHEET != null )
			OreDictionary.registerOre( "sheetCardboard", CARDBOARD_SHEET );

		Addon.initializeItemsAll();
	}

	private static ItemCardboardArmor conditionalNewArmor( String configName, EntityEquipmentSlot armorType )
	{
		if( !BetterStorage.globalConfig.getBoolean( configName ) )
			return null;
		return new ItemCardboardArmor( armorType );
	}

	@SideOnly( Side.CLIENT )
	public static void registerItemModels()
	{
		if( BetterStorage.globalConfig.getBoolean( GlobalConfig.keyEnabled ) )
			ModelLoader.setCustomModelResourceLocation( KEY, 0, new ModelResourceLocation( KEY.getRegistryName(), "inventory" ) );
		if( BetterStorage.globalConfig.getBoolean( GlobalConfig.masterKeyEnabled ) )
			ModelLoader.setCustomModelResourceLocation( MASTER_KEY, 0, new ModelResourceLocation( MASTER_KEY.getRegistryName(), "inventory" ) );
		if( BetterStorage.globalConfig.getBoolean( GlobalConfig.keyringEnabled ) )
			for( int i = 0; i < 4; i++ )
				ModelLoader.setCustomModelResourceLocation( KEYRING, i,
						new ModelResourceLocation( KEYRING.getRegistryName() + "_" + i, "inventory" ) );
		if( BetterStorage.globalConfig.getBoolean( GlobalConfig.lockEnabled ) )
			ModelLoader.setCustomModelResourceLocation( LOCK, 0, new ModelResourceLocation( LOCK.getRegistryName(), "inventory" ) );

		if( BetterStorage.globalConfig.getBoolean( GlobalConfig.cardboardSheetEnabled ) )
			ModelLoader.setCustomModelResourceLocation( CARDBOARD_SHEET, 0,
					new ModelResourceLocation( CARDBOARD_SHEET.getRegistryName(), "inventory" ) );

		if( BetterStorage.globalConfig.getBoolean( GlobalConfig.cardboardHelmetEnabled ) )
			ModelLoader.setCustomModelResourceLocation( CARDBOARD_HELMET, 0,
					new ModelResourceLocation( CARDBOARD_HELMET.getRegistryName(), "inventory" ) );
		if( BetterStorage.globalConfig.getBoolean( GlobalConfig.cardboardChestplateEnabled ) )
			ModelLoader.setCustomModelResourceLocation( CARDBOARD_CHESTPLATE, 0,
					new ModelResourceLocation( CARDBOARD_CHESTPLATE.getRegistryName(), "inventory" ) );
		if( BetterStorage.globalConfig.getBoolean( GlobalConfig.cardboardLeggingsEnabled ) )
			ModelLoader.setCustomModelResourceLocation( CARDBOARD_LEGGINGS, 0,
					new ModelResourceLocation( CARDBOARD_LEGGINGS.getRegistryName(), "inventory" ) );
		if( BetterStorage.globalConfig.getBoolean( GlobalConfig.cardboardBootsEnabled ) )
			ModelLoader.setCustomModelResourceLocation( CARDBOARD_BOOTS, 0,
					new ModelResourceLocation( CARDBOARD_BOOTS.getRegistryName(), "inventory" ) );

		if( BetterStorage.globalConfig.getBoolean( GlobalConfig.cardboardSwordEnabled ) )
			ModelLoader.setCustomModelResourceLocation( CARDBOARD_SWORD, 0,
					new ModelResourceLocation( CARDBOARD_SWORD.getRegistryName(), "inventory" ) );
		if( BetterStorage.globalConfig.getBoolean( GlobalConfig.cardboardShovelEnabled ) )
			ModelLoader.setCustomModelResourceLocation( CARDBOARD_SHOVEL, 0,
					new ModelResourceLocation( CARDBOARD_SHOVEL.getRegistryName(), "inventory" ) );
		if( BetterStorage.globalConfig.getBoolean( GlobalConfig.cardboardPickaxeEnabled ) )
			ModelLoader.setCustomModelResourceLocation( CARDBOARD_PICKAXE, 0,
					new ModelResourceLocation( CARDBOARD_PICKAXE.getRegistryName(), "inventory" ) );
		if( BetterStorage.globalConfig.getBoolean( GlobalConfig.cardboardAxeEnabled ) )
			ModelLoader.setCustomModelResourceLocation( CARDBOARD_AXE, 0,
					new ModelResourceLocation( CARDBOARD_AXE.getRegistryName(), "inventory" ) );
		if( BetterStorage.globalConfig.getBoolean( GlobalConfig.cardboardHoeEnabled ) )
			ModelLoader.setCustomModelResourceLocation( CARDBOARD_HOE, 0,
					new ModelResourceLocation( CARDBOARD_HOE.getRegistryName(), "inventory" ) );

		slimeBucket.registerItemModels();
	}
}
