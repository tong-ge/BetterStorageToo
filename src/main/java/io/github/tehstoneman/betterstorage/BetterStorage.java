package io.github.tehstoneman.betterstorage;

import org.apache.logging.log4j.Logger;

import io.github.tehstoneman.betterstorage.addon.Addon;
import io.github.tehstoneman.betterstorage.common.item.crafting.Recipes;
import io.github.tehstoneman.betterstorage.common.tileentity.BetterStorageTileEntities;
import io.github.tehstoneman.betterstorage.config.Config;
import io.github.tehstoneman.betterstorage.config.GlobalConfig;
import io.github.tehstoneman.betterstorage.content.BetterStorageEntities;
import io.github.tehstoneman.betterstorage.item.EnchantmentBetterStorage;
import io.github.tehstoneman.betterstorage.misc.CreativeTabBetterStorage;
import io.github.tehstoneman.betterstorage.misc.DungeonLoot;
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

//@formatter:off
@Mod( modid						= ModInfo.modId,
      name						= ModInfo.modName,
      version					= ModInfo.modVersion,
      dependencies				= ModInfo.dependencies,
      acceptedMinecraftVersions	= ModInfo.acceptedMC,
      guiFactory				= ModInfo.guiFactory,
      updateJSON				= ModInfo.updateJson )
//@formatter:on
public class BetterStorage
{
	@Instance( ModInfo.modId )
	public static BetterStorage			instance;

	@SidedProxy( serverSide = ModInfo.commonProxy, clientSide = ModInfo.clientProxy )
	public static CommonProxy			proxy;

	public static SimpleNetworkWrapper	simpleNetworkWrapper;

	public static Logger				log;

	public static CreativeTabs			creativeTab;

	public static Config				globalConfig;

	@EventHandler
	public void preInit( FMLPreInitializationEvent event )
	{
		// Register network messages
		final int messageID = 0;
		simpleNetworkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel( ModInfo.modId );
		// simpleNetworkWrapper.registerMessage( SyncCrateMessage.Handler.class, SyncCrateMessage.class, messageID++, Side.CLIENT );

		log = event.getModLog();
		creativeTab = new CreativeTabBetterStorage();

		Addon.initialize();

		globalConfig = new GlobalConfig( event.getSuggestedConfigurationFile() );
		Addon.setupConfigsAll();
		globalConfig.load();
		globalConfig.save();

		proxy.preInit();

		EnchantmentBetterStorage.initialize();

		BetterStorageTileEntities.register();
		BetterStorageEntities.register();
		DungeonLoot.add();

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
