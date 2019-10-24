package io.github.tehstoneman.betterstorage.common.item;

import io.github.tehstoneman.betterstorage.ModInfo;
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
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder( ModInfo.MOD_ID )
public final class BetterStorageItems
{
	//@formatter:off
	@ObjectHolder( "key" )					public static ItemKey KEY;
	@ObjectHolder( "master_key" )			public static ItemMasterKey MASTER_KEY;
	@ObjectHolder( "keyring" )				public static ItemKeyring KEYRING;
	@ObjectHolder( "lock" )					public static ItemLock LOCK;
	@ObjectHolder( "cardboard_sheet" )		public static ItemCardboardSheet CARDBOARD_SHEET;

	@ObjectHolder( "cardboard_sword" )		public static ItemCardboardSword CARDBOARD_SWORD;
	@ObjectHolder( "cardboard_shovel" )		public static ItemCardboardShovel CARDBOARD_SHOVEL;
	@ObjectHolder( "cardboard_pickaxe" )	public static ItemCardboardPickaxe CARDBOARD_PICKAXE;
	@ObjectHolder( "cardboard_axe" )		public static ItemCardboardAxe CARDBOARD_AXE;
	@ObjectHolder( "cardboard_hoe" )		public static ItemCardboardHoe CARDBOARD_HOE;

	// public static ItemCardboardArmor CARDBOARD_HELMET = new ItemCardboardArmor( EntityEquipmentSlot.HEAD );
	// public static ItemCardboardArmor CARDBOARD_CHESTPLATE = new ItemCardboardArmor( EntityEquipmentSlot.CHEST );
	// public static ItemCardboardArmor CARDBOARD_LEGGINGS = new ItemCardboardArmor( EntityEquipmentSlot.LEGS );
	// public static ItemCardboardArmor CARDBOARD_BOOTS = new ItemCardboardArmor( EntityEquipmentSlot.FEET );

	// public static ItemBucketSlime SLIME_BUCKET = new ItemBucketSlime();

	// public static ItemDrinkingHelmet drinkingHelmet;
	// public static ItemPresentBook PRESENT_BOOK;
	//@formatter:on
}
