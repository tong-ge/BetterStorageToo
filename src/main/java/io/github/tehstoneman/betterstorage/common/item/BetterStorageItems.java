package io.github.tehstoneman.betterstorage.common.item;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.addon.Addon;
import io.github.tehstoneman.betterstorage.common.item.locking.ItemKey;
import io.github.tehstoneman.betterstorage.common.item.locking.ItemKeyring;
import io.github.tehstoneman.betterstorage.common.item.locking.ItemLock;
import io.github.tehstoneman.betterstorage.common.item.locking.ItemMasterKey;
import io.github.tehstoneman.betterstorage.config.GlobalConfig;
import io.github.tehstoneman.betterstorage.item.ItemBucketSlime;
import io.github.tehstoneman.betterstorage.item.ItemPresentBook;
import io.github.tehstoneman.betterstorage.item.cardboard.ItemCardboardArmor;
import io.github.tehstoneman.betterstorage.item.cardboard.ItemCardboardAxe;
import io.github.tehstoneman.betterstorage.item.cardboard.ItemCardboardHoe;
import io.github.tehstoneman.betterstorage.item.cardboard.ItemCardboardPickaxe;
import io.github.tehstoneman.betterstorage.item.cardboard.ItemCardboardSheet;
import io.github.tehstoneman.betterstorage.item.cardboard.ItemCardboardShovel;
import io.github.tehstoneman.betterstorage.item.cardboard.ItemCardboardSword;
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
	public static ItemCardboardSheet	cardboardSheet;
	// public static ItemDrinkingHelmet drinkingHelmet;
	public static ItemBucketSlime		slimeBucket;
	public static ItemPresentBook		presentBook;

	public static ItemCardboardArmor	cardboardHelmet;
	public static ItemCardboardArmor	cardboardChestplate;
	public static ItemCardboardArmor	cardboardLeggings;
	public static ItemCardboardArmor	cardboardBoots;

	public static ItemCardboardSword	cardboardSword;
	public static ItemCardboardPickaxe	cardboardPickaxe;
	public static ItemCardboardShovel	cardboardShovel;
	public static ItemCardboardAxe		cardboardAxe;
	public static ItemCardboardHoe		cardboardHoe;

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
			MASTER_KEY = (ItemMasterKey)new ItemMasterKey().setUnlocalizedName( ModInfo.modId + ".masterKey" );
			GameRegistry.register( MASTER_KEY.setRegistryName( "masterKey" ) );
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
		cardboardSheet = MiscUtils.conditionalNew( ItemCardboardSheet.class, GlobalConfig.cardboardSheetEnabled );
		// drinkingHelmet = MiscUtils.conditionalNew( ItemDrinkingHelmet.class, GlobalConfig.drinkingHelmetEnabled );
		slimeBucket = MiscUtils.conditionalNew( ItemBucketSlime.class, GlobalConfig.slimeBucketEnabled );
		presentBook = new ItemPresentBook();

		cardboardHelmet = conditionalNewArmor( GlobalConfig.cardboardHelmetEnabled, EntityEquipmentSlot.HEAD );
		cardboardChestplate = conditionalNewArmor( GlobalConfig.cardboardChestplateEnabled, EntityEquipmentSlot.CHEST );
		cardboardLeggings = conditionalNewArmor( GlobalConfig.cardboardLeggingsEnabled, EntityEquipmentSlot.LEGS );
		cardboardBoots = conditionalNewArmor( GlobalConfig.cardboardBootsEnabled, EntityEquipmentSlot.FEET );

		cardboardSword = MiscUtils.conditionalNew( ItemCardboardSword.class, GlobalConfig.cardboardSwordEnabled );
		cardboardPickaxe = MiscUtils.conditionalNew( ItemCardboardPickaxe.class, GlobalConfig.cardboardPickaxeEnabled );
		cardboardShovel = MiscUtils.conditionalNew( ItemCardboardShovel.class, GlobalConfig.cardboardShovelEnabled );
		cardboardAxe = MiscUtils.conditionalNew( ItemCardboardAxe.class, GlobalConfig.cardboardAxeEnabled );
		cardboardHoe = MiscUtils.conditionalNew( ItemCardboardHoe.class, GlobalConfig.cardboardHoeEnabled );

		anyCardboardItemsEnabled = BetterStorageItems.cardboardHelmet != null || BetterStorageItems.cardboardChestplate != null
				|| BetterStorageItems.cardboardLeggings != null || BetterStorageItems.cardboardBoots != null
				|| BetterStorageItems.cardboardSword != null || BetterStorageItems.cardboardPickaxe != null || BetterStorageItems.cardboardAxe != null
				|| BetterStorageItems.cardboardShovel != null || BetterStorageItems.cardboardHoe != null;

		if( cardboardSheet != null )
			OreDictionary.registerOre( "sheetCardboard", cardboardSheet );

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

		cardboardSheet.registerItemModels();
		slimeBucket.registerItemModels();
	}
}
