package io.github.tehstoneman.betterstorage.client;

//@Mod.EventBusSubscriber( modid = ModInfo.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD )
public class ClientSetup
{
/*	@SubscribeEvent
	public static void init( final FMLClientSetupEvent event )
	{
		// Register GUIs
		ScreenManager.register( BetterStorageContainerTypes.CARDBOARD_BOX.get(), GuiCardboardBox::new );
		ScreenManager.register( BetterStorageContainerTypes.CRATE.get(), GuiCrate::new );
		ScreenManager.register( BetterStorageContainerTypes.KEYRING.get(), GuiKeyring::new );
		ScreenManager.register( BetterStorageContainerTypes.LOCKER.get(), GuiLocker::new );
		ScreenManager.register( BetterStorageContainerTypes.REINFORCED_CHEST.get(), GuiReinforcedChest::new );
		ScreenManager.register( BetterStorageContainerTypes.REINFORCED_LOCKER.get(), GuiReinforcedLocker::new );

		ScreenManager.register( BetterStorageContainerTypes.CONFIG.get(), ConfigContainerGui::new );

		// Register Tile Entity Renderers
		ClientRegistry.bindTileEntityRenderer( BetterStorageTileEntityTypes.GLASS_TANK.get(), TileEntityTankRenderer::new );
		ClientRegistry.bindTileEntityRenderer( BetterStorageTileEntityTypes.LOCKABLE_DOOR.get(), TileEntityLockableDoorRenderer::new );
		ClientRegistry.bindTileEntityRenderer( BetterStorageTileEntityTypes.LOCKER.get(), TileEntityLockerRenderer::new );
		ClientRegistry.bindTileEntityRenderer( BetterStorageTileEntityTypes.REINFORCED_CHEST.get(), TileEntityReinforcedChestRenderer::new );
		ClientRegistry.bindTileEntityRenderer( BetterStorageTileEntityTypes.REINFORCED_LOCKER.get(), TileEntityLockerRenderer::new );

		Minecraft.getInstance().getItemColors().register( new KeyColor(), BetterStorageItems.KEY.get(), BetterStorageItems.LOCK.get() );
		Minecraft.getInstance().getItemColors().register( new CardboardColor(), BetterStorageItems.CARDBOARD_AXE.get(),
				BetterStorageItems.CARDBOARD_BOOTS.get(), BetterStorageItems.CARDBOARD_CHESTPLATE.get(), BetterStorageItems.CARDBOARD_HELMET.get(),
				BetterStorageItems.CARDBOARD_HOE.get(), BetterStorageItems.CARDBOARD_LEGGINGS.get(), BetterStorageItems.CARDBOARD_PICKAXE.get(),
				BetterStorageItems.CARDBOARD_SHOVEL.get(), BetterStorageItems.CARDBOARD_SWORD.get(), BetterStorageBlocks.CARDBOARD_BOX.get() );
		Minecraft.getInstance().getBlockColors().register( new CardboardColor(), BetterStorageBlocks.CARDBOARD_BOX.get() );

		ItemBlockRenderTypes.setRenderLayer( BetterStorageBlocks.GLASS_TANK.get(), RenderType.cutout() );
	}

	@SubscribeEvent
	public static void onTextureStitch( TextureStitchEvent.Pre event )
	{
		if( !event.getMap().location().equals( InventoryMenu.BLOCK_ATLAS ) )
			return;

		event.addSprite( Resources.TEXTURE_CHEST_REINFORCED );
		event.addSprite( Resources.TEXTURE_CHEST_REINFORCED_DOUBLE );
		event.addSprite( Resources.TEXTURE_LOCKER_NORMAL );
		event.addSprite( Resources.TEXTURE_LOCKER_NORMAL_DOUBLE );
		event.addSprite( Resources.TEXTURE_LOCKER_REINFORCED );
		event.addSprite( Resources.TEXTURE_LOCKER_REINFORCED_DOUBLE );
		event.addSprite( Resources.TEXTURE_WHITE );
	}

	@SubscribeEvent
	public static void onModelRegistryEvent( ModelRegistryEvent event )
	{
		ModelLoaderRegistry.registerLoader( new ResourceLocation( ModInfo.MOD_ID, "xobj" ), XOBJLoader.INSTANCE );

		ModelLoader.addSpecialModel( Resources.MODEL_REINFORCED_CHEST );
		ModelLoader.addSpecialModel( Resources.MODEL_REINFORCED_CHEST_FRAME );
		ModelLoader.addSpecialModel( Resources.MODEL_REINFORCED_CHEST_LID );
		ModelLoader.addSpecialModel( Resources.MODEL_REINFORCED_CHEST_LID_FRAME );
		ModelLoader.addSpecialModel( Resources.MODEL_REINFORCED_CHEST_LARGE );
		ModelLoader.addSpecialModel( Resources.MODEL_REINFORCED_CHEST_LARGE_FRAME );
		ModelLoader.addSpecialModel( Resources.MODEL_REINFORCED_CHEST_LID_LARGE );
		ModelLoader.addSpecialModel( Resources.MODEL_REINFORCED_CHEST_LID_LARGE_FRAME );

		ModelLoader.addSpecialModel( Resources.MODEL_REINFORCED_LOCKER );
		ModelLoader.addSpecialModel( Resources.MODEL_REINFORCED_LOCKER_FRAME );
		ModelLoader.addSpecialModel( Resources.MODEL_REINFORCED_LOCKER_DOOR_L );
		ModelLoader.addSpecialModel( Resources.MODEL_REINFORCED_LOCKER_DOOR_R );
		ModelLoader.addSpecialModel( Resources.MODEL_REINFORCED_LOCKER_DOOR_FRAME_L );
		ModelLoader.addSpecialModel( Resources.MODEL_REINFORCED_LOCKER_DOOR_FRAME_R );
		ModelLoader.addSpecialModel( Resources.MODEL_REINFORCED_LOCKER_LARGE );
		ModelLoader.addSpecialModel( Resources.MODEL_REINFORCED_LOCKER_LARGE_FRAME );
		ModelLoader.addSpecialModel( Resources.MODEL_REINFORCED_LOCKER_DOOR_LARGE_L );
		ModelLoader.addSpecialModel( Resources.MODEL_REINFORCED_LOCKER_DOOR_LARGE_R );
		ModelLoader.addSpecialModel( Resources.MODEL_REINFORCED_LOCKER_DOOR_LARGE_FRAME_L );
		ModelLoader.addSpecialModel( Resources.MODEL_REINFORCED_LOCKER_DOOR_LARGE_FRAME_R );
	}*/
}
