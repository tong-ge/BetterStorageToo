package io.github.tehstoneman.betterstorage.common.item;

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
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class BetterStorageItems
{
	public static ItemKey				KEY						= new ItemKey();
	public static ItemMasterKey			MASTER_KEY				= new ItemMasterKey();
	public static ItemKeyring			KEYRING					= new ItemKeyring();
	public static ItemLock				LOCK					= new ItemLock();
	public static ItemCardboardSheet	CARDBOARD_SHEET			= new ItemCardboardSheet();

	public static ItemCardboardSword	CARDBOARD_SWORD			= new ItemCardboardSword();
	public static ItemCardboardShovel	CARDBOARD_SHOVEL		= new ItemCardboardShovel();
	public static ItemCardboardPickaxe	CARDBOARD_PICKAXE		= new ItemCardboardPickaxe();
	public static ItemCardboardAxe		CARDBOARD_AXE			= new ItemCardboardAxe();
	public static ItemCardboardHoe		CARDBOARD_HOE			= new ItemCardboardHoe();

	public static ItemCardboardArmor	CARDBOARD_HELMET		= new ItemCardboardArmor( EntityEquipmentSlot.HEAD );
	public static ItemCardboardArmor	CARDBOARD_CHESTPLATE	= new ItemCardboardArmor( EntityEquipmentSlot.CHEST );
	public static ItemCardboardArmor	CARDBOARD_LEGGINGS		= new ItemCardboardArmor( EntityEquipmentSlot.LEGS );
	public static ItemCardboardArmor	CARDBOARD_BOOTS			= new ItemCardboardArmor( EntityEquipmentSlot.FEET );

	public static ItemBucketSlime		SLIME_BUCKET			= new ItemBucketSlime();

	// public static ItemDrinkingHelmet drinkingHelmet;
	// public static ItemPresentBook PRESENT_BOOK;

	public static boolean				anyCardboardItemsEnabled;

	@SideOnly( Side.CLIENT )
	public static void registerItemModels()
	{
		/*
		 * if( BetterStorage.config.cardboardSheetEnabled )
		 * CARDBOARD_SHEET.registerItemModels();
		 * 
		 * if( BetterStorage.config.cardboardSwordEnabled )
		 * CARDBOARD_SWORD.registerItemModels();
		 * if( BetterStorage.config.cardboardShovelEnabled )
		 * CARDBOARD_SHOVEL.registerItemModels();
		 * if( BetterStorage.config.cardboardPickaxeEnabled )
		 * CARDBOARD_PICKAXE.registerItemModels();
		 * if( BetterStorage.config.cardboardAxeEnabled )
		 * CARDBOARD_AXE.registerItemModels();
		 * if( BetterStorage.config.cardboardHoeEnabled )
		 * CARDBOARD_HOE.registerItemModels();
		 * 
		 * if( BetterStorage.config.cardboardHelmetEnabled )
		 * CARDBOARD_HELMET.registerItemModels();
		 * if( BetterStorage.config.cardboardChestplateEnabled )
		 * CARDBOARD_CHESTPLATE.registerItemModels();
		 * if( BetterStorage.config.cardboardLeggingsEnabled )
		 * CARDBOARD_LEGGINGS.registerItemModels();
		 * if( BetterStorage.config.cardboardBootsEnabled )
		 * CARDBOARD_BOOTS.registerItemModels();
		 * 
		 * if( BetterStorage.config.slimeBucketEnabled )
		 * SLIME_BUCKET.registerItemModels();
		 */
	}
}
