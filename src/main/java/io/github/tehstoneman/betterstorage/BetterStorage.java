package io.github.tehstoneman.betterstorage;

import org.apache.logging.log4j.Logger;

import io.github.tehstoneman.betterstorage.addon.Addon;
import io.github.tehstoneman.betterstorage.config.Config;
import io.github.tehstoneman.betterstorage.config.GlobalConfig;
import io.github.tehstoneman.betterstorage.content.BetterStorageEntities;
import io.github.tehstoneman.betterstorage.content.BetterStorageItems;
import io.github.tehstoneman.betterstorage.content.BetterStorageTileEntities;
import io.github.tehstoneman.betterstorage.content.BetterStorageTiles;
import io.github.tehstoneman.betterstorage.item.EnchantmentBetterStorage;
import io.github.tehstoneman.betterstorage.item.tile.ItemTileBetterStorage;
import io.github.tehstoneman.betterstorage.misc.Constants;
import io.github.tehstoneman.betterstorage.misc.CreativeTabBetterStorage;
import io.github.tehstoneman.betterstorage.misc.DungeonLoot;
import io.github.tehstoneman.betterstorage.misc.Recipes;
import io.github.tehstoneman.betterstorage.proxy.CommonProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;

//@formatter:off
@Mod( modid        = Constants.modId,
      name         = Constants.modName,
      dependencies = "required-after:Forge; after:Thaumcraft; after:JEI;",
      guiFactory   = "io.github.tehstoneman.betterstorage.client.gui.BetterStorageGuiFactory" )
//@formatter:on
public class BetterStorage
{
	@Instance( Constants.modId )
	public static BetterStorage			instance;

	@SidedProxy( serverSide = Constants.commonProxy, clientSide = Constants.clientProxy )
	public static CommonProxy			proxy;

	public static SimpleNetworkWrapper	simpleNetworkWrapper;

	public static Logger				log;

	public static CreativeTabs			creativeTab;

	public static Config				globalConfig;

	@EventHandler
	public void preInit( FMLPreInitializationEvent event )
	{
		simpleNetworkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel( Constants.modId );
		log = event.getModLog();
		creativeTab = new CreativeTabBetterStorage();

		Addon.initialize();

		globalConfig = new GlobalConfig( event.getSuggestedConfigurationFile() );
		Addon.setupConfigsAll();
		globalConfig.load();
		globalConfig.save();

		BetterStorageTiles.initialize();
		BetterStorageItems.initialize();
		
		EnchantmentBetterStorage.initialize();

		BetterStorageTileEntities.register();
		BetterStorageEntities.register();
		DungeonLoot.add();
		
		proxy.preInit();
	}

	@EventHandler
	public void load( FMLInitializationEvent event )
	{
		Recipes.add();

		proxy.initialize();
	}

	@EventHandler
	public void postInit( FMLPostInitializationEvent event )
	{
		Addon.postInitializeAll();
		
		proxy.postInit();
	}
}
