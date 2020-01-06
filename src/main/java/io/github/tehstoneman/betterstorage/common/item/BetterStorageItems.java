package io.github.tehstoneman.betterstorage.common.item;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.common.block.BetterStorageBlocks;
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
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
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

	@ObjectHolder( "cardboard_helmet" )		public static ItemCardboardArmor CARDBOARD_HELMET;
	@ObjectHolder( "cardboard_chestplate" )	public static ItemCardboardArmor CARDBOARD_CHESTPLATE;
	@ObjectHolder( "cardboard_leggings" )	public static ItemCardboardArmor CARDBOARD_LEGGINGS;
	@ObjectHolder( "cardboard_boots" )		public static ItemCardboardArmor CARDBOARD_BOOTS;

	// public static ItemBucketSlime SLIME_BUCKET = new ItemBucketSlime();

	// public static ItemDrinkingHelmet drinkingHelmet;
	// public static ItemPresentBook PRESENT_BOOK;
	//@formatter:on

	@Mod.EventBusSubscriber( bus = Mod.EventBusSubscriber.Bus.MOD )
	private static class Register
	{
		@SubscribeEvent
		public static void onItemsRegistry( final RegistryEvent.Register< Item > event )
		{
			final IForgeRegistry< Item > registry = event.getRegistry();

			registry.register( new ItemBlockCrate( BetterStorageBlocks.CRATE ).setRegistryName( BetterStorageBlocks.CRATE.getRegistryName() ) );
			registry.register( new ItemBlockReinforcedChest( BetterStorageBlocks.REINFORCED_CHEST )
					.setRegistryName( BetterStorageBlocks.REINFORCED_CHEST.getRegistryName() ) );
			registry.register( new ItemBlockLocker( BetterStorageBlocks.LOCKER ).setRegistryName( BetterStorageBlocks.LOCKER.getRegistryName() ) );
			registry.register( new ItemBlockReinforcedLocker( BetterStorageBlocks.REINFORCED_LOCKER )
					.setRegistryName( BetterStorageBlocks.REINFORCED_LOCKER.getRegistryName() ) );

			registry.register( new ItemKey().setRegistryName( "key" ) );
			registry.register( new ItemKeyring().setRegistryName( "keyring" ) );
			registry.register( new ItemMasterKey().setRegistryName( "master_key" ) );
			registry.register( new ItemLock().setRegistryName( "lock" ) );

			registry.register( new ItemCardboardSheet().setRegistryName( ModInfo.MOD_ID, "cardboard_sheet" ) );

			registry.register( new ItemBlockCardboardBox( BetterStorageBlocks.CARDBOARD_BOX ).setRegistryName( ModInfo.MOD_ID, "cardboard_box" ) );

			registry.register( new ItemCardboardSword().setRegistryName( ModInfo.MOD_ID, "cardboard_sword" ) );
			registry.register( new ItemCardboardShovel().setRegistryName( ModInfo.MOD_ID, "cardboard_shovel" ) );
			registry.register( new ItemCardboardPickaxe().setRegistryName( ModInfo.MOD_ID, "cardboard_pickaxe" ) );
			registry.register( new ItemCardboardAxe().setRegistryName( ModInfo.MOD_ID, "cardboard_axe" ) );
			registry.register( new ItemCardboardHoe().setRegistryName( ModInfo.MOD_ID, "cardboard_hoe" ) );

			registry.register( new ItemCardboardArmor( EquipmentSlotType.HEAD ).setRegistryName( "cardboard_helmet" ) );
			registry.register( new ItemCardboardArmor( EquipmentSlotType.CHEST ).setRegistryName( "cardboard_chestplate" ) );
			registry.register( new ItemCardboardArmor( EquipmentSlotType.LEGS ).setRegistryName( "cardboard_leggings" ) );
			registry.register( new ItemCardboardArmor( EquipmentSlotType.FEET ).setRegistryName( "cardboard_boots" ) );

			registry.register( new BlockItem( BetterStorageBlocks.BLOCK_FLINT, new Item.Properties().group( BetterStorage.ITEM_GROUP ) )
					.setRegistryName( BetterStorageBlocks.BLOCK_FLINT.getRegistryName() ) );
			registry.register( new BlockItem( BetterStorageBlocks.GLASS_TANK, new Item.Properties().group( BetterStorage.ITEM_GROUP ) )
					.setRegistryName( BetterStorageBlocks.GLASS_TANK.getRegistryName() ) );
		}
	}
}
