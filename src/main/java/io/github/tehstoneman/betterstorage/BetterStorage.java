package io.github.tehstoneman.betterstorage;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.github.tehstoneman.betterstorage.api.IProxy;
import io.github.tehstoneman.betterstorage.common.block.BetterStorageBlocks;
import io.github.tehstoneman.betterstorage.config.BetterStorageConfig;
import io.github.tehstoneman.betterstorage.event.RegistryEventHandler;
import io.github.tehstoneman.betterstorage.network.ModNetwork;
import io.github.tehstoneman.betterstorage.proxy.ClientProxy;
import io.github.tehstoneman.betterstorage.proxy.ServerProxy;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.simple.SimpleChannel;

@Mod( ModInfo.MOD_ID )
public class BetterStorage
{
	public static final Logger			LOGGER		= LogManager.getLogger( ModInfo.MOD_ID );
	public static final ItemGroup		ITEM_GROUP	= new ItemGroup( "betterStorageToo" )
													{
														@Override
														@OnlyIn( Dist.CLIENT )
														public ItemStack createIcon()
														{
															return new ItemStack( BetterStorageBlocks.CRATE );
														}
													};
	public static final SimpleChannel	NETWORK		= ModNetwork.getNetworkChannel();
	public static final IProxy			PROXY		= DistExecutor.<IProxy> runForDist( () -> ClientProxy::new, () -> ServerProxy::new );

	public static Random				RANDOM;

	public BetterStorage()
	{
		// Initialize random numbers
		RANDOM = new Random();

		BetterStorageConfig.register( ModLoadingContext.get() );

		// Register the setup method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener( this::setup );
		// FMLJavaModLoadingContext.get().getModEventBus().addListener( ClientEvents::clientStartup );

		// MinecraftForge.EVENT_BUS.register( new ClientEventHandler() );
		MinecraftForge.EVENT_BUS.register( new RegistryEventHandler() );
		// MinecraftForge.EVENT_BUS.register( new BetterStorageEventHandler() );
	}

	public void setup( FMLCommonSetupEvent event )
	{
		PROXY.setup( event );
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
