package io.github.tehstoneman.betterstorage.proxy;

import io.github.tehstoneman.betterstorage.client.gui.GuiCardboardBox;
import io.github.tehstoneman.betterstorage.client.gui.GuiCrate;
import io.github.tehstoneman.betterstorage.client.gui.GuiKeyring;
import io.github.tehstoneman.betterstorage.client.gui.GuiLocker;
import io.github.tehstoneman.betterstorage.client.gui.GuiReinforcedChest;
import io.github.tehstoneman.betterstorage.client.gui.GuiReinforcedLocker;
import io.github.tehstoneman.betterstorage.common.block.BetterStorageBlocks;
import io.github.tehstoneman.betterstorage.common.inventory.BetterStorageContainerTypes;
import io.github.tehstoneman.betterstorage.common.item.BetterStorageItems;
import io.github.tehstoneman.betterstorage.common.item.cardboard.CardboardColor;
import io.github.tehstoneman.betterstorage.common.item.locking.KeyColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class ClientProxy implements IProxy
{
	@Override
	public void setup( FMLCommonSetupEvent event )
	{
		// OBJLoader.INSTANCE.addDomain( ModInfo.modId );
		DeferredWorkQueue.runLater( () ->
		{
			// ClientRegistry.bindTileEntityRenderer( TileEntityReinforcedChest.class, new TileEntityReinforcedChestRenderer() );
			// ClientRegistry.bindTileEntityRenderer( TileEntityLocker.class, new TileEntityLockerRenderer() );
			// ClientRegistry.bindTileEntityRenderer( TileEntityReinforcedLocker.class, new TileEntityLockerRenderer() );
			// ClientRegistry.bindTileEntityRenderer( TileEntityReinforcedChest.class, new TileEntityReinforcedChestRenderer() );
			// ClientRegistry.bindTileEntityRenderer( TileEntityLockableDoor.class, new TileEntityLockableDoorRenderer() );
			// ClientRegistry.bindTileEntityRenderer( TileEntityTank.class, new TileEntityTankRenderer() );
		} );

		DeferredWorkQueue.runLater( () ->
		{
			ScreenManager.registerFactory( BetterStorageContainerTypes.LOCKER, GuiLocker::new );
			ScreenManager.registerFactory( BetterStorageContainerTypes.CRATE, GuiCrate::new );
			ScreenManager.registerFactory( BetterStorageContainerTypes.REINFORCED_CHEST, GuiReinforcedChest::new );
			ScreenManager.registerFactory( BetterStorageContainerTypes.REINFORCED_LOCKER, GuiReinforcedLocker::new );
			ScreenManager.registerFactory( BetterStorageContainerTypes.KEYRING, GuiKeyring::new );
			ScreenManager.registerFactory( BetterStorageContainerTypes.CARDBOARD_BOX, GuiCardboardBox::new );
		} );

		DeferredWorkQueue.runLater( () ->
		{
			Minecraft.getInstance().getItemColors().register( new KeyColor(), BetterStorageItems.KEY.get(), BetterStorageItems.LOCK.get() );
			Minecraft.getInstance().getItemColors().register( new CardboardColor(), BetterStorageItems.CARDBOARD_AXE.get(),
					BetterStorageItems.CARDBOARD_BOOTS.get(), BetterStorageItems.CARDBOARD_CHESTPLATE.get(), BetterStorageItems.CARDBOARD_HELMET.get(),
					BetterStorageItems.CARDBOARD_HOE.get(), BetterStorageItems.CARDBOARD_LEGGINGS.get(), BetterStorageItems.CARDBOARD_PICKAXE.get(),
					BetterStorageItems.CARDBOARD_SHOVEL.get(), BetterStorageItems.CARDBOARD_SWORD.get(), BetterStorageBlocks.CARDBOARD_BOX.get() );
			Minecraft.getInstance().getBlockColors().register( new CardboardColor(), BetterStorageBlocks.CARDBOARD_BOX.get() );
		} );
	}
}
