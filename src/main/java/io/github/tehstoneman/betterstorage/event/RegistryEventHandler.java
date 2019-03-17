package io.github.tehstoneman.betterstorage.event;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.common.block.BetterStorageBlocks;
import io.github.tehstoneman.betterstorage.common.item.ItemBlockCrate;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
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
		// GameRegistry.registerTileEntity( TileEntityCrate.class, ModInfo.containerCrate );
	}

	@SubscribeEvent
	public static void onItemsRegistry( final RegistryEvent.Register< Item > event )
	{
		final IForgeRegistry< Item > registry = event.getRegistry();

		registry.register(
				new ItemBlockCrate( BetterStorageBlocks.CRATE, new Item.Properties().group( BetterStorage.ITEM_GROUP ) ).setRegistryName( "crate" ) );
	}
}
