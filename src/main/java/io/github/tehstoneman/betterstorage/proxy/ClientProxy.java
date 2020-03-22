package io.github.tehstoneman.betterstorage.proxy;

import io.github.tehstoneman.betterstorage.client.gui.GuiCardboardBox;
import io.github.tehstoneman.betterstorage.client.gui.GuiCrate;
import io.github.tehstoneman.betterstorage.client.gui.GuiKeyring;
import io.github.tehstoneman.betterstorage.client.gui.GuiLocker;
import io.github.tehstoneman.betterstorage.client.gui.GuiReinforcedChest;
import io.github.tehstoneman.betterstorage.client.gui.GuiReinforcedLocker;
import io.github.tehstoneman.betterstorage.client.renderer.TileEntityLockableDoorRenderer;
import io.github.tehstoneman.betterstorage.client.renderer.TileEntityLockerRenderer;
import io.github.tehstoneman.betterstorage.client.renderer.TileEntityReinforcedChestRenderer;
import io.github.tehstoneman.betterstorage.client.renderer.tileentity.TileEntityTankRenderer;
import io.github.tehstoneman.betterstorage.common.block.BetterStorageBlocks;
import io.github.tehstoneman.betterstorage.common.inventory.BetterStorageContainerTypes;
import io.github.tehstoneman.betterstorage.common.item.BetterStorageItems;
import io.github.tehstoneman.betterstorage.common.item.cardboard.CardboardColor;
import io.github.tehstoneman.betterstorage.common.item.locking.KeyColor;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityLockableDoor;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityLocker;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityReinforcedChest;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityReinforcedLocker;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityTank;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.world.World;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class ClientProxy implements IProxy
{
	@Override
	public void setup( FMLCommonSetupEvent event )
	{
		// OBJLoader.INSTANCE.addDomain( ModInfo.modId );
		DeferredWorkQueue.runLater( () ->
		{
			ClientRegistry.bindTileEntitySpecialRenderer( TileEntityReinforcedChest.class, new TileEntityReinforcedChestRenderer() );
			ClientRegistry.bindTileEntitySpecialRenderer( TileEntityLocker.class, new TileEntityLockerRenderer() );
			ClientRegistry.bindTileEntitySpecialRenderer( TileEntityReinforcedLocker.class, new TileEntityLockerRenderer() );
			ClientRegistry.bindTileEntitySpecialRenderer( TileEntityReinforcedChest.class, new TileEntityReinforcedChestRenderer() );
			ClientRegistry.bindTileEntitySpecialRenderer( TileEntityLockableDoor.class, new TileEntityLockableDoorRenderer() );
			ClientRegistry.bindTileEntitySpecialRenderer( TileEntityTank.class, new TileEntityTankRenderer() );
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
			Minecraft.getInstance().getItemColors().register( new KeyColor(), BetterStorageItems.KEY, BetterStorageItems.LOCK );
			Minecraft.getInstance().getItemColors().register( new CardboardColor(), BetterStorageItems.CARDBOARD_AXE,
					BetterStorageItems.CARDBOARD_BOOTS, BetterStorageItems.CARDBOARD_CHESTPLATE, BetterStorageItems.CARDBOARD_HELMET,
					BetterStorageItems.CARDBOARD_HOE, BetterStorageItems.CARDBOARD_LEGGINGS, BetterStorageItems.CARDBOARD_PICKAXE,
					BetterStorageItems.CARDBOARD_SHOVEL, BetterStorageItems.CARDBOARD_SWORD, BetterStorageBlocks.CARDBOARD_BOX );
			Minecraft.getInstance().getBlockColors().register( new CardboardColor(), BetterStorageBlocks.CARDBOARD_BOX );
		} );
	}

	@Override
	public World getClientWorld()
	{
		return Minecraft.getInstance().world;
	}
}
