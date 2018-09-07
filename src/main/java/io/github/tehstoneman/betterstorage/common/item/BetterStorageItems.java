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
}
