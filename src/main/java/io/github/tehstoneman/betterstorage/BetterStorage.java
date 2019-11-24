package io.github.tehstoneman.betterstorage;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.github.tehstoneman.betterstorage.common.block.BetterStorageBlocks;
import io.github.tehstoneman.betterstorage.common.enchantment.EnchantmentKey;
import io.github.tehstoneman.betterstorage.common.enchantment.EnchantmentLock;
import io.github.tehstoneman.betterstorage.config.BetterStorageConfig;
import io.github.tehstoneman.betterstorage.event.BetterStorageEventHandler;
import io.github.tehstoneman.betterstorage.event.RegistryEventHandler;
import io.github.tehstoneman.betterstorage.network.ModNetwork;
import io.github.tehstoneman.betterstorage.proxy.ClientProxy;
import io.github.tehstoneman.betterstorage.proxy.IProxy;
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
	public static final ItemGroup		ITEM_GROUP	= new ItemGroup( "better_storage_too" )
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
	}

	public void setup( FMLCommonSetupEvent event )
	{
		PROXY.setup( event );
		ITEM_GROUP.setRelevantEnchantmentTypes( EnchantmentKey.KEY, EnchantmentLock.LOCK );
	}

	/*
	 * public void preInit( FMLPreInitializationEvent event )
	 * {
	 * config = new BetterStorageConfig( event.getSuggestedConfigurationFile() );
	 * config.syncFromFile();
	 *
	 * // Addon.initialize();
	 * // Addon.setupConfigsAll();
	 *
	 * // Initialize API
	 * BetterStorageAPI.materials = new MaterialRegistry();
	 *
	 * // BetterStorageEntities.register();
	 * // DungeonLoot.add();
	 * }
	 */
}
