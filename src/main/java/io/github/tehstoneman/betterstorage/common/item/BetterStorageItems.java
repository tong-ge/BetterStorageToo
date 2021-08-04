package io.github.tehstoneman.betterstorage.common.item;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.common.block.BetterStorageBlocks;
import io.github.tehstoneman.betterstorage.common.fluid.BetterStorageFluids;
import io.github.tehstoneman.betterstorage.common.item.cardboard.ItemBlockCardboardBox;
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
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class BetterStorageItems
{
	public static final DeferredRegister< Item >				REGISTERY				= DeferredRegister.create( ForgeRegistries.ITEMS,
			ModInfo.MOD_ID );

	public static RegistryObject< ItemBlockCrate >				CRATE					= REGISTERY.register( "crate", () -> new ItemBlockCrate() );
	public static RegistryObject< ItemBlockReinforcedChest >	REINFORCED_CHEST		= REGISTERY.register( "reinforced_chest",
			() -> new ItemBlockReinforcedChest() );
	public static RegistryObject< ItemBlockLocker >				LOCKER					= REGISTERY.register( "locker", () -> new ItemBlockLocker() );
	public static RegistryObject< ItemBlockReinforcedLocker >	REINFORCED_LOCKER		= REGISTERY.register( "reinforced_locker",
			() -> new ItemBlockReinforcedLocker() );

	public static RegistryObject< ItemKey >						KEY						= REGISTERY.register( "key", () -> new ItemKey() );
	public static RegistryObject< ItemKeyring >					KEYRING					= REGISTERY.register( "keyring", () -> new ItemKeyring() );
	public static RegistryObject< ItemMasterKey >				MASTER_KEY				= REGISTERY.register( "master_key",
			() -> new ItemMasterKey() );
	public static RegistryObject< ItemLock >					LOCK					= REGISTERY.register( "lock", () -> new ItemLock() );

	public static RegistryObject< ItemCardboardSheet >			CARDBOARD_SHEET			= REGISTERY.register( "cardboard_sheet",
			() -> new ItemCardboardSheet() );
	public static RegistryObject< ItemBlockCardboardBox >		CARDBOARD_BOX			= REGISTERY.register( "cardboard_box",
			() -> new ItemBlockCardboardBox() );

	public static RegistryObject< ItemCardboardSword >			CARDBOARD_SWORD			= REGISTERY.register( "cardboard_sword",
			() -> new ItemCardboardSword() );
	public static RegistryObject< ItemCardboardShovel >			CARDBOARD_SHOVEL		= REGISTERY.register( "cardboard_shovel",
			() -> new ItemCardboardShovel() );
	public static RegistryObject< ItemCardboardPickaxe >		CARDBOARD_PICKAXE		= REGISTERY.register( "cardboard_pickaxe",
			() -> new ItemCardboardPickaxe() );
	public static RegistryObject< ItemCardboardAxe >			CARDBOARD_AXE			= REGISTERY.register( "cardboard_axe",
			() -> new ItemCardboardAxe() );
	public static RegistryObject< ItemCardboardHoe >			CARDBOARD_HOE			= REGISTERY.register( "cardboard_hoe",
			() -> new ItemCardboardHoe() );

	public static RegistryObject< ItemCardboardArmor >			CARDBOARD_HELMET		= REGISTERY.register( "cardboard_helmet",
			() -> new ItemCardboardArmor( EquipmentSlotType.HEAD ) );
	public static RegistryObject< ItemCardboardArmor >			CARDBOARD_CHESTPLATE	= REGISTERY.register( "cardboard_chestplate",
			() -> new ItemCardboardArmor( EquipmentSlotType.CHEST ) );
	public static RegistryObject< ItemCardboardArmor >			CARDBOARD_LEGGINGS		= REGISTERY.register( "cardboard_leggings",
			() -> new ItemCardboardArmor( EquipmentSlotType.LEGS ) );
	public static RegistryObject< ItemCardboardArmor >			CARDBOARD_BOOTS			= REGISTERY.register( "cardboard_boots",
			() -> new ItemCardboardArmor( EquipmentSlotType.FEET ) );

	public static RegistryObject< BlockItem >					BLOCK_FLINT				= REGISTERY.register( "block_flint",
			() -> new BlockItem( BetterStorageBlocks.BLOCK_FLINT.get(), new Item.Properties().tab( BetterStorage.ITEM_GROUP ) ) );
	public static RegistryObject< BlockItem >					GLASS_TANK				= REGISTERY.register( "glass_tank",
			() -> new BlockItem( BetterStorageBlocks.GLASS_TANK.get(), new Item.Properties().tab( BetterStorage.ITEM_GROUP ) ) );

	/*public static RegistryObject< BucketItem >					MILK_BUCKET				= REGISTERY.register( "milk_bucket",
			() -> new BucketItem( BetterStorageFluids.MILK, new Item.Properties().craftRemainder( Items.BUCKET ) ) );*/

	public static RegistryObject< HexKeyItem >					HEX_KEY					= REGISTERY.register( "hex_key", () -> new HexKeyItem() );
}
