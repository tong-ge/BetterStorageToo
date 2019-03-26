package io.github.tehstoneman.betterstorage.event;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.common.block.BetterStorageBlocks;
import io.github.tehstoneman.betterstorage.common.item.BetterStorageItems;
import io.github.tehstoneman.betterstorage.common.item.ItemBlockCrate;
import io.github.tehstoneman.betterstorage.common.item.ItemBlockLocker;
import io.github.tehstoneman.betterstorage.common.item.ItemBlockReinforcedChest;
import io.github.tehstoneman.betterstorage.common.item.ItemBlockReinforcedLocker;
import io.github.tehstoneman.betterstorage.common.item.cardboard.ItemBlockCardboardBox;
import io.github.tehstoneman.betterstorage.common.tileentity.BetterStorageTileEntityTypes;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityLocker;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityReinforcedChest;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityReinforcedLocker;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber( bus = Mod.EventBusSubscriber.Bus.MOD )
public class RegistryEventHandler
{
	@SubscribeEvent
	public static void onBlocksRegistry( final RegistryEvent.Register< Block > event )
	{
		final IForgeRegistry< Block > registry = event.getRegistry();

		registry.register( BetterStorageBlocks.CRATE.setRegistryName( "crate" ) );
		registry.register( BetterStorageBlocks.REINFORCED_CHEST.setRegistryName( "reinforced_chest" ) );
		registry.register( BetterStorageBlocks.LOCKER.setRegistryName( "locker" ) );
		registry.register( BetterStorageBlocks.REINFORCED_LOCKER.setRegistryName( "reinforced_locker" ) );
		registry.register( BetterStorageBlocks.LOCKABLE_DOOR.setRegistryName( "lockable_door" ) );
		registry.register( BetterStorageBlocks.CARDBOARD_BOX.setRegistryName( "cardboard_box" ) );
		registry.register( BetterStorageBlocks.BLOCK_FLINT.setRegistryName( "block_flint" ) );

		// GameRegistry.registerTileEntity( TileEntityCrate.class, ModInfo.containerCrate );
	}

	@SubscribeEvent
	public static void onItemsRegistry( final RegistryEvent.Register< Item > event )
	{
		final IForgeRegistry< Item > registry = event.getRegistry();

		registry.register( new ItemBlockCrate( BetterStorageBlocks.CRATE ).setRegistryName( "crate" ) );
		registry.register( new ItemBlockReinforcedChest( BetterStorageBlocks.REINFORCED_CHEST ).setRegistryName( "reinforced_chest" ) );
		registry.register( new ItemBlockLocker( BetterStorageBlocks.LOCKER ).setRegistryName( "locker" ) );
		registry.register( new ItemBlockReinforcedLocker( BetterStorageBlocks.REINFORCED_LOCKER ).setRegistryName( "reinforced_locker" ) );

		registry.register( BetterStorageItems.KEY.setRegistryName( "key" ) );
		registry.register( BetterStorageItems.KEYRING.setRegistryName( "keyring" ) );
		registry.register( BetterStorageItems.MASTER_KEY.setRegistryName( "master_key" ) );
		registry.register( BetterStorageItems.LOCK.setRegistryName( "lock" ) );

		registry.register( BetterStorageItems.CARDBOARD_SHEET.setRegistryName( "cardboard_sheet" ) );

		registry.register( new ItemBlockCardboardBox( BetterStorageBlocks.CARDBOARD_BOX ).setRegistryName( "cardboard_box" ) );

		registry.register( BetterStorageItems.CARDBOARD_SWORD.setRegistryName( "cardboard_sword" ) );
		registry.register( BetterStorageItems.CARDBOARD_SHOVEL.setRegistryName( "cardboard_shovel" ) );
		registry.register( BetterStorageItems.CARDBOARD_PICKAXE.setRegistryName( "cardboard_pickaxe" ) );
		registry.register( BetterStorageItems.CARDBOARD_AXE.setRegistryName( "cardboard_axe" ) );
		registry.register( BetterStorageItems.CARDBOARD_HOE.setRegistryName( "cardboard_hoe" ) );

		registry.register( BetterStorageItems.CARDBOARD_HELMET.setRegistryName( "cardboard_helmet" ) );
		registry.register( BetterStorageItems.CARDBOARD_CHESTPLATE.setRegistryName( "cardboard_chestplate" ) );
		registry.register( BetterStorageItems.CARDBOARD_LEGGINGS.setRegistryName( "cardboard_leggings" ) );
		registry.register( BetterStorageItems.CARDBOARD_BOOTS.setRegistryName( "cardboard_boots" ) );

		registry.register( new ItemBlock( BetterStorageBlocks.BLOCK_FLINT, new Item.Properties().group( BetterStorage.ITEM_GROUP ) )
				.setRegistryName( "block_flint" ) );
	}

	@SubscribeEvent
	public static void onTileEntityRegistry( final RegistryEvent.Register< TileEntityType< ? > > event )
	{
		final IForgeRegistry< TileEntityType< ? > > registry = event.getRegistry();

		BetterStorageTileEntityTypes.REINFORCED_CHEST = TileEntityType.register( ModInfo.modId + ":reinforced_chest",
				TileEntityType.Builder.create( TileEntityReinforcedChest::new ) );
		BetterStorageTileEntityTypes.LOCKER = TileEntityType.register( ModInfo.modId + ":locker",
				TileEntityType.Builder.create( TileEntityLocker::new ) );
		BetterStorageTileEntityTypes.REINFORCED_LOCKER = TileEntityType.register( ModInfo.modId + ":reinforced_locker",
				TileEntityType.Builder.create( TileEntityReinforcedLocker::new ) );
	}
}
