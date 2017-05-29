package io.github.tehstoneman.betterstorage.proxy;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.client.renderer.BetterStorageRenderingHandler;
import io.github.tehstoneman.betterstorage.client.renderer.TileEntityReinforcedChestRenderer;
import io.github.tehstoneman.betterstorage.common.block.BetterStorageBlocks;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityReinforcedChest;
import net.minecraft.client.resources.I18n;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.animation.ITimeValue;
import net.minecraftforge.common.model.animation.IAnimationStateMachine;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly( Side.CLIENT )
public class ClientProxy extends CommonProxy
{
	public static final Map< Class< ? extends TileEntity >, BetterStorageRenderingHandler > renderingHandlers = new HashMap<>();

	@Override
	public void preInit()
	{
		super.preInit();

		OBJLoader.INSTANCE.addDomain( ModInfo.modId );

		BetterStorageBlocks.registerItemModels();
		// BetterStorageItems.registerItemModels();
	}

	@Override
	public void initialize()
	{
		super.initialize();

		// new KeyBindingHandler();

		registerRenderers();
	}

	@Override
	public void postInit()
	{
		super.postInit();

		// Minecraft.getMinecraft().getItemColors().registerItemColorHandler( new KeyColor(), BetterStorageItems.KEY, BetterStorageItems.LOCK );
		/*
		 * Minecraft.getMinecraft().getItemColors().registerItemColorHandler( new CardboardColor(), BetterStorageItems.CARDBOARD_AXE,
		 * BetterStorageItems.CARDBOARD_BOOTS, BetterStorageItems.CARDBOARD_CHESTPLATE, BetterStorageItems.CARDBOARD_HELMET,
		 * BetterStorageItems.CARDBOARD_HOE, BetterStorageItems.CARDBOARD_LEGGINGS, BetterStorageItems.CARDBOARD_PICKAXE,
		 * BetterStorageItems.CARDBOARD_SHOVEL, BetterStorageItems.CARDBOARD_SWORD );
		 */
		// Minecraft.getMinecraft().getItemColors().registerItemColorHandler( new ItemCardboardColor(), ItemBlock.getItemFromBlock(
		// BetterStorageBlocks.CARDBOARD_BOX ) );
	}

	private void registerRenderers()
	{
		// RenderingRegistry.registerEntityRenderingHandler(EntityFrienderman.class, new RenderFrienderman());
		// RenderingRegistry.registerEntityRenderingHandler(EntityCluckington.class, new RenderChicken(new ModelCluckington(), 0.4F));

		ClientRegistry.bindTileEntitySpecialRenderer( TileEntityReinforcedChest.class, new TileEntityReinforcedChestRenderer() );
		// ClientRegistry.bindTileEntitySpecialRenderer( TileEntityLocker.class, new TileEntityLockerRenderer() );
		// ClientRegistry.bindTileEntitySpecialRenderer( TileEntityReinforcedLocker.class, new TileEntityLockerRenderer() );
		// ClientRegistry.bindTileEntitySpecialRenderer( TileEntityLockableDoor.class, new TileEntityLockableDoorRenderer() );
		// ClientRegistry.bindTileEntitySpecialRenderer( TileEntityPresent.class, new TileEntityPresentRenderer() );
		// RenderingRegistry.registerBlockHandler(new TileLockableDoorRenderingHandler());
		// Addon.registerRenderersAll();
	}

	/*
	 * public static int registerTileEntityRenderer( Class< ? extends TileEntity > tileEntityClass, TileEntitySpecialRenderer renderer,
	 * boolean render3dInInventory, float rotation, float scale, float yOffset )
	 * {
	 * ClientRegistry.bindTileEntitySpecialRenderer( tileEntityClass, renderer );
	 * final BetterStorageRenderingHandler renderingHandler = new BetterStorageRenderingHandler( tileEntityClass, renderer, render3dInInventory,
	 * rotation, scale, yOffset );
	 * renderingHandlers.put( tileEntityClass, renderingHandler );
	 * return renderingHandler.getRenderId();
	 * }
	 */

	/*
	 * public static int registerTileEntityRenderer( Class< ? extends TileEntity > tileEntityClass, TileEntitySpecialRenderer renderer )
	 * {
	 * return registerTileEntityRenderer( tileEntityClass, renderer, true, 90, 1, 0 );
	 * }
	 */

	@Override
	public String localize( String unlocalized, Object... args )
	{
		return I18n.format( unlocalized, args );
	}

	@Override
	public IAnimationStateMachine load( ResourceLocation location, ImmutableMap< String, ITimeValue > parameters )
	{
		return ModelLoaderRegistry.loadASM( location, parameters );
	}
}
