package io.github.tehstoneman.betterstorage.proxy;

import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.api.IProxy;
import io.github.tehstoneman.betterstorage.client.renderer.TileEntityLockerRenderer;
import io.github.tehstoneman.betterstorage.client.renderer.TileEntityReinforcedChestRenderer;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityLocker;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityReinforcedChest;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

//@SideOnly( Side.CLIENT )
public class ClientProxy implements IProxy
{
	// public static final Map< Class< ? extends TileEntity >, BetterStorageRenderingHandler > renderingHandlers = new HashMap<>();

	@Override
	public void setup( FMLCommonSetupEvent event )
	{
		OBJLoader.INSTANCE.addDomain( ModInfo.modId );
		ClientRegistry.bindTileEntitySpecialRenderer( TileEntityReinforcedChest.class, new TileEntityReinforcedChestRenderer() );
		ClientRegistry.bindTileEntitySpecialRenderer( TileEntityLocker.class, new TileEntityLockerRenderer() );
	}

	/*
	 * @Override
	 * public void initialize()
	 * {
	 * super.initialize();
	 *
	 * // new KeyBindingHandler();
	 *
	 * // BetterStorageBlocks.registerItemModels();
	 * // BetterStorageItems.registerItemModels();
	 *
	 * registerRenderers();
	 * BetterStorageColorHandler.registerColorHandlers();
	 * }
	 */

	/*@Override
	public void postInit()
	{
		super.postInit();

		//@formatter:off
		Minecraft.getInstance().getItemColors().registerItemColorHandler( new KeyColor(),
				BetterStorageItems.KEY,
				BetterStorageItems.LOCK );
		Minecraft.getInstance().getItemColors().registerItemColorHandler( new CardboardColor(),
				Item.getItemFromBlock( BetterStorageBlocks.CARDBOARD_BOX ),
				BetterStorageItems.CARDBOARD_AXE,
				BetterStorageItems.CARDBOARD_BOOTS,
				BetterStorageItems.CARDBOARD_CHESTPLATE,
				BetterStorageItems.CARDBOARD_HELMET,
				BetterStorageItems.CARDBOARD_HOE,
				BetterStorageItems.CARDBOARD_LEGGINGS,
				BetterStorageItems.CARDBOARD_PICKAXE,
				BetterStorageItems.CARDBOARD_SHOVEL,
				BetterStorageItems.CARDBOARD_SWORD );
		//@formatter:on
	}*/

	/*
	 * private void registerRenderers()
	 * {
	 * // RenderingRegistry.registerEntityRenderingHandler(EntityFrienderman.class, new RenderFrienderman());
	 * // RenderingRegistry.registerEntityRenderingHandler(EntityCluckington.class, new RenderChicken(new ModelCluckington(), 0.4F));
	 *
	 * // ClientRegistry.bindTileEntitySpecialRenderer( TileEntityReinforcedChest.class, new TileEntityReinforcedChestRenderer() );
	 * // ClientRegistry.bindTileEntitySpecialRenderer( TileEntityLocker.class, new TileEntityLockerRenderer() );
	 * // ClientRegistry.bindTileEntitySpecialRenderer( TileEntityReinforcedLocker.class, new TileEntityLockerRenderer() );
	 * // ClientRegistry.bindTileEntitySpecialRenderer( TileEntityLockableDoor.class, new TileEntityLockableDoorRenderer() );
	 * // ClientRegistry.bindTileEntitySpecialRenderer( TileEntityPresent.class, new TileEntityPresentRenderer() );
	 * // Addon.registerRenderersAll();
	 * }
	 */

	/*
	 * @Override
	 * public String localize( String unlocalized, Object... args )
	 * {
	 * return I18n.format( unlocalized, args );
	 * }
	 */
}
