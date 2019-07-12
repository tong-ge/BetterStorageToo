package io.github.tehstoneman.betterstorage.event;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.common.block.BetterStorageBlocks;
import io.github.tehstoneman.betterstorage.common.block.BlockBetterStorage;
import io.github.tehstoneman.betterstorage.common.block.BlockLocker;
import io.github.tehstoneman.betterstorage.common.inventory.BetterStorageContainerTypes;
import io.github.tehstoneman.betterstorage.common.inventory.ContainerBetterStorage;
import io.github.tehstoneman.betterstorage.common.item.LockerItem;
import io.github.tehstoneman.betterstorage.common.item.cardboard.ItemCardboardSheet;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityLocker;
import net.minecraft.block.Block;
import net.minecraft.block.Block.Properties;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.extensions.IForgeContainerType;
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

		// registry.register( BetterStorageBlocks.CRATE.setRegistryName( "crate" ) );
		// registry.register( BetterStorageBlocks.REINFORCED_CHEST.setRegistryName( "reinforced_chest" ) );
		registry.register( new BlockLocker().setRegistryName( ModInfo.MOD_ID, "locker" ) );
		// registry.register( BetterStorageBlocks.REINFORCED_LOCKER.setRegistryName( "reinforced_locker" ) );
		// registry.register( BetterStorageBlocks.LOCKABLE_DOOR.setRegistryName( "lockable_door" ) );
		// registry.register( BetterStorageBlocks.CARDBOARD_BOX.setRegistryName( "cardboard_box" ) );
		registry.register( new BlockBetterStorage( Properties.create( Material.ROCK, MaterialColor.BLACK ).hardnessAndResistance( 5.0F, 6.0F ) )
				.setRegistryName( ModInfo.MOD_ID, "block_flint" ) );

		// GameRegistry.registerTileEntity( TileEntityCrate.class, ModInfo.containerCrate );
	}

	@SubscribeEvent
	public static void onItemsRegistry( final RegistryEvent.Register< Item > event )
	{
		final IForgeRegistry< Item > registry = event.getRegistry();

		// registry.register( new ItemBlockCrate( BetterStorageBlocks.CRATE ).setRegistryName( "crate" ) );
		// registry.register( new ItemBlockReinforcedChest( BetterStorageBlocks.REINFORCED_CHEST ).setRegistryName( "reinforced_chest" ) );
		registry.register( new LockerItem( BetterStorageBlocks.LOCKER ).setRegistryName( BetterStorageBlocks.LOCKER.getRegistryName() ) );
		// registry.register( new ItemBlockReinforcedLocker( BetterStorageBlocks.REINFORCED_LOCKER ).setRegistryName( "reinforced_locker" ) );

		// registry.register( BetterStorageItems.KEY.setRegistryName( "key" ) );
		// registry.register( BetterStorageItems.KEYRING.setRegistryName( "keyring" ) );
		// registry.register( BetterStorageItems.MASTER_KEY.setRegistryName( "master_key" ) );
		// registry.register( BetterStorageItems.LOCK.setRegistryName( "lock" ) );

		registry.register( new ItemCardboardSheet().setRegistryName( ModInfo.MOD_ID, "cardboard_sheet" ) );

		// registry.register( new ItemBlockCardboardBox( BetterStorageBlocks.CARDBOARD_BOX ).setRegistryName( "cardboard_box" ) );

		// registry.register( BetterStorageItems.CARDBOARD_SWORD.setRegistryName( "cardboard_sword" ) );
		// registry.register( BetterStorageItems.CARDBOARD_SHOVEL.setRegistryName( "cardboard_shovel" ) );
		// registry.register( BetterStorageItems.CARDBOARD_PICKAXE.setRegistryName( "cardboard_pickaxe" ) );
		// registry.register( BetterStorageItems.CARDBOARD_AXE.setRegistryName( "cardboard_axe" ) );
		// registry.register( BetterStorageItems.CARDBOARD_HOE.setRegistryName( "cardboard_hoe" ) );

		// registry.register( BetterStorageItems.CARDBOARD_HELMET.setRegistryName( "cardboard_helmet" ) );
		// registry.register( BetterStorageItems.CARDBOARD_CHESTPLATE.setRegistryName( "cardboard_chestplate" ) );
		// registry.register( BetterStorageItems.CARDBOARD_LEGGINGS.setRegistryName( "cardboard_leggings" ) );
		// registry.register( BetterStorageItems.CARDBOARD_BOOTS.setRegistryName( "cardboard_boots" ) );

		registry.register( new BlockItem( BetterStorageBlocks.BLOCK_FLINT, new Item.Properties().group( BetterStorage.ITEM_GROUP ) )
				.setRegistryName( BetterStorageBlocks.BLOCK_FLINT.getRegistryName() ) );
	}

	@SubscribeEvent
	public static void onTileEntityRegistry( final RegistryEvent.Register< TileEntityType< ? > > event )
	{
		final IForgeRegistry< TileEntityType< ? > > registry = event.getRegistry();

		// BetterStorageTileEntityTypes.CRATE = TileEntityType.register( ModInfo.modId + ":crate", TileEntityType.Builder.create( TileEntityCrate::new ) );
		// BetterStorageTileEntityTypes.REINFORCED_CHEST = TileEntityType.register( ModInfo.modId + ":reinforced_chest", TileEntityType.Builder.create(
		// TileEntityReinforcedChest::new ) );
		registry.register( TileEntityType.Builder.create( TileEntityLocker::new, BetterStorageBlocks.LOCKER ).build( null )
				.setRegistryName( BetterStorageBlocks.LOCKER.getRegistryName() ) );
		// BetterStorageTileEntityTypes.REINFORCED_LOCKER = TileEntityType.register( ModInfo.modId + ":reinforced_locker", TileEntityType.Builder.create(
		// TileEntityReinforcedLocker::new ) );
	}

	@SubscribeEvent
	public static void onContainerRegistry( final RegistryEvent.Register< ContainerType< ? > > event )
	{
		final IForgeRegistry< ContainerType< ? > > registry = event.getRegistry();

		//registry.register( new ContainerType<>( ContainerBetterStorage::new ).setRegistryName( BetterStorageBlocks.LOCKER.getRegistryName() ) );
		registry.register( IForgeContainerType.create( ( windowID, inv, data ) ->
		{
			BlockPos pos = data.readBlockPos();
			return new ContainerBetterStorage( windowID, inv, BetterStorage.proxy.getClientWorld(), pos );
		} ).setRegistryName( BetterStorageBlocks.LOCKER.getRegistryName() ) );
	}
}
