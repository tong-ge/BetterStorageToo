package io.github.tehstoneman.betterstorage;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.github.tehstoneman.betterstorage.common.block.BetterStorageBlocks;
import io.github.tehstoneman.betterstorage.common.enchantment.EnchantmentBetterStorage;
import io.github.tehstoneman.betterstorage.common.fluid.BetterStorageFluids;
import io.github.tehstoneman.betterstorage.common.inventory.BetterStorageContainerTypes;
import io.github.tehstoneman.betterstorage.common.item.BetterStorageItemGroup;
import io.github.tehstoneman.betterstorage.common.item.BetterStorageItems;
import io.github.tehstoneman.betterstorage.common.tileentity.BetterStorageTileEntityTypes;
import io.github.tehstoneman.betterstorage.config.BetterStorageConfig;
import io.github.tehstoneman.betterstorage.network.ModNetwork;
import io.github.tehstoneman.betterstorage.proxy.ClientProxy;
import io.github.tehstoneman.betterstorage.proxy.IProxy;
import io.github.tehstoneman.betterstorage.proxy.ServerProxy;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.simple.SimpleChannel;

@Mod( ModInfo.MOD_ID )
public class BetterStorage
{
	public static final Logger			LOGGER		= LogManager.getLogger( ModInfo.MOD_ID );
	public static final CreativeModeTab	ITEM_GROUP	= new BetterStorageItemGroup();
	public static final SimpleChannel	NETWORK		= ModNetwork.getNetworkChannel();
	public static final IProxy			PROXY		= DistExecutor.<IProxy> safeRunForDist( () -> ClientProxy::new, () -> ServerProxy::new );

	public static Random				RANDOM;

	public BetterStorage()
	{
		// Initialize random numbers
		RANDOM = new Random();

		BetterStorageConfig.register( ModLoadingContext.get() );

		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		ForgeMod.enableMilkFluid();

		BetterStorageBlocks.REGISTERY.register( modEventBus );
		BetterStorageFluids.REGISTERY.register( modEventBus );
		BetterStorageItems.REGISTERY.register( modEventBus );
		EnchantmentBetterStorage.REGISTERY.register( modEventBus );
		BetterStorageTileEntityTypes.REGISTERY.register( modEventBus );
		BetterStorageContainerTypes.REGISTERY.register( modEventBus );

		// Register the setup method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener( this::setup );
	}

	public void setup( FMLCommonSetupEvent event )
	{
		if( BetterStorageConfig.COMMON.cardboardBoxDispenserPlaceable.get() )
			DispenserBlock.registerBehavior( BetterStorageBlocks.CARDBOARD_BOX.get(), new OptionalDispenseItemBehavior()
			{
				/**
				 * Dispense the specified stack, play the dispense sound and spawn particles.
				 */
				@Override
				protected ItemStack execute( BlockSource source, ItemStack stack )
				{
					setSuccess( false );
					final Item item = stack.getItem();
					if( item instanceof BlockItem )
					{
						final Direction direction = source.getBlockState().getValue( DispenserBlock.FACING );
						final BlockPos blockpos = source.getPos().relative( direction );
						final Direction direction1 = source.getLevel().isEmptyBlock( blockpos.below() ) ? direction : Direction.UP;
						setSuccess( ( (BlockItem)item )
								.place( new DirectionalPlaceContext( source.getLevel(), blockpos, direction, stack, direction1 ) ).consumesAction() );
					}

					return stack;
				}
			} );
		/*
		 * if( BetterStorageConfig.COMMON.useFluidMilk.get() )
		 * DispenserBlock.registerBehavior( Items.MILK_BUCKET, new OptionalDispenseBehavior()
		 * {
		 * private final DefaultDispenseItemBehavior dispenseBehavior = new DefaultDispenseItemBehavior();
		 *
		 * @Override
		 * protected ItemStack execute( IBlockSource source, ItemStack stack )
		 * {
		 * final Item bucketitem = stack.getItem();
		 * final BlockPos blockpos = source.getPos().relative( source.getBlockState().getValue( DispenserBlock.FACING ) );
		 * final Level world = source.getLevel();
		 * if( FluidMilk.emptyBucket( bucketitem, (Player)null, world, blockpos, (BlockHitResult)null ) )
		 * return new ItemStack( Items.BUCKET );
		 * else
		 * return dispenseBehavior.dispense( source, stack );
		 * }
		 * } );
		 */
	}
}
