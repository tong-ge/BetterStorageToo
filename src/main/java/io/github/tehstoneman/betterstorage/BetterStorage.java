package io.github.tehstoneman.betterstorage;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.github.tehstoneman.betterstorage.common.block.BetterStorageBlocks;
import io.github.tehstoneman.betterstorage.config.BetterStorageConfig;
import io.github.tehstoneman.betterstorage.event.RegistryEventHandler;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod( ModInfo.modId )
public class BetterStorage
{
	// Directly reference a log4j logger.
	public static final Logger			LOGGER		= LogManager.getLogger();
	public static final ItemGroup		ITEM_GROUP	= new ItemGroup( "betterStorageToo" )
													{
														@Override
														@OnlyIn( Dist.CLIENT )
														public ItemStack createIcon()
														{
															return new ItemStack( BetterStorageBlocks.CRATE );
														}
													};

	public static BetterStorageConfig	config;

	public static Random				random;

	public BetterStorage()
	{
		MinecraftForge.EVENT_BUS.register( new RegistryEventHandler() );
	}

	/*
	 * public void preInit( FMLPreInitializationEvent event )
	 * {
	 * // Register network messages
	 * final int messageID = 0;
	 * simpleNetworkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel( ModInfo.modId );
	 * 
	 * logger = event.getModLog();
	 * creativeTab = new CreativeTabBetterStorage();
	 * 
	 * // Initialize random numbers
	 * random = new Random();
	 * 
	 * config = new BetterStorageConfig( event.getSuggestedConfigurationFile() );
	 * config.syncFromFile();
	 * 
	 * MinecraftForge.EVENT_BUS.register( new BetterStorageEventHandler() );
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
