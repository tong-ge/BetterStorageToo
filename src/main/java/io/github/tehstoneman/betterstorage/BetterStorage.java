package io.github.tehstoneman.betterstorage;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.github.tehstoneman.betterstorage.api.IProxy;
import io.github.tehstoneman.betterstorage.client.gui.GuiBetterStorage;
import io.github.tehstoneman.betterstorage.client.gui.GuiCrate;
import io.github.tehstoneman.betterstorage.common.block.BetterStorageBlocks;
import io.github.tehstoneman.betterstorage.common.inventory.ContainerCrate;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityCrate;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityLocker;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityReinforcedChest;
import io.github.tehstoneman.betterstorage.config.BetterStorageConfig;
import io.github.tehstoneman.betterstorage.event.BetterStorageEventHandler;
import io.github.tehstoneman.betterstorage.event.RegistryEventHandler;
import io.github.tehstoneman.betterstorage.proxy.ClientProxy;
import io.github.tehstoneman.betterstorage.proxy.ServerProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod( ModInfo.modId )
public class BetterStorage
{
	// Directly reference a log4j logger.
	public static final Logger			LOGGER		= LogManager.getLogger( ModInfo.modId );
	public static final ItemGroup		ITEM_GROUP	= new ItemGroup( "betterStorageToo" )
													{
														@Override
														@OnlyIn( Dist.CLIENT )
														public ItemStack createIcon()
														{
															return new ItemStack( BetterStorageBlocks.CRATE );
														}
													};
	public static IProxy				proxy		= DistExecutor.<IProxy> runForDist( () -> ClientProxy::new, () -> ServerProxy::new );
	public static BetterStorageConfig	config;

	public static Random				random;

	public BetterStorage()
	{
		// Initialize random numbers
		random = new Random();

		ModLoadingContext.get().registerExtensionPoint( ExtensionPoint.GUIFACTORY, () ->
		{
			return ( openContainer ) ->
			{
				final ResourceLocation location = openContainer.getId();
				if( location.toString().equals( "betterstorage:reinforced_chest" ) )
				{
					final EntityPlayerSP player = Minecraft.getInstance().player;
					final BlockPos pos = openContainer.getAdditionalData().readBlockPos();
					final TileEntity tileEntity = player.world.getTileEntity( pos );
					if( tileEntity instanceof TileEntityReinforcedChest )
					{
						final TileEntityReinforcedChest reinforcedChest = (TileEntityReinforcedChest)tileEntity;
						return new GuiBetterStorage( reinforcedChest.getContainer( player ) );
					}
				}
				if( location.toString().equals( "betterstorage:locker" ) )
				{
					final EntityPlayerSP player = Minecraft.getInstance().player;
					final BlockPos pos = openContainer.getAdditionalData().readBlockPos();
					final TileEntity tileEntity = player.world.getTileEntity( pos );
					if( tileEntity instanceof TileEntityLocker )
					{
						final TileEntityLocker locker = (TileEntityLocker)tileEntity;
						return new GuiBetterStorage( locker.getContainer( player ) );
					}
				}
				if( location.toString().equals( "betterstorage:crate" ) )
				{
					final EntityPlayerSP player = Minecraft.getInstance().player;
					final BlockPos pos = openContainer.getAdditionalData().readBlockPos();
					final TileEntity tileEntity = player.world.getTileEntity( pos );
					if( tileEntity instanceof TileEntityCrate )
					{
						final TileEntityCrate crate = (TileEntityCrate)tileEntity;
						return new GuiCrate( crate, new ContainerCrate( crate, player ) );
					}
				}
				return null;
			};
		} );

		BetterStorageConfig.register( ModLoadingContext.get() );

		// Register the setup method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener( this::setup );

		MinecraftForge.EVENT_BUS.register( new RegistryEventHandler() );
		MinecraftForge.EVENT_BUS.register( new BetterStorageEventHandler() );
	}

	public void setup( FMLCommonSetupEvent event )
	{
		proxy.setup( event );
	}

	/*
	 * public void preInit( FMLPreInitializationEvent event )
	 * {
	 * // Register network messages
	 * final int messageID = 0;
	 * simpleNetworkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel( ModInfo.modId );
	 *
	 * config = new BetterStorageConfig( event.getSuggestedConfigurationFile() );
	 * config.syncFromFile();
	 *
	 * // Addon.initialize();
	 * // Addon.setupConfigsAll();
	 *
	 * // Initialize API
	 * BetterStorageAPI.materials = new MaterialRegistry();
	 *
	 * proxy.preInit();
	 *
	 * EnchantmentBetterStorage.initialize();
	 *
	 * // BetterStorageEntities.register();
	 * // DungeonLoot.add();
	 *
	 * }
	 */

	/*
	 * @EventHandler
	 * public void init( FMLInitializationEvent event )
	 * {
	 * // Recipes.add();
	 *
	 * proxy.initialize();
	 * }
	 */

	/*
	 * @EventHandler
	 * public void postInit( FMLPostInitializationEvent event )
	 * {
	 * // Addon.postInitializeAll();
	 *
	 * proxy.postInit();
	 * }
	 */
}
