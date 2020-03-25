package io.github.tehstoneman.betterstorage;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.github.tehstoneman.betterstorage.common.block.BetterStorageBlocks;
import io.github.tehstoneman.betterstorage.common.enchantment.EnchantmentBetterStorage;
import io.github.tehstoneman.betterstorage.common.fluid.BetterStorageFluids;
import io.github.tehstoneman.betterstorage.common.fluid.FluidMilk;
import io.github.tehstoneman.betterstorage.common.inventory.BetterStorageContainerTypes;
import io.github.tehstoneman.betterstorage.common.item.BetterStorageItemGroup;
import io.github.tehstoneman.betterstorage.common.item.BetterStorageItems;
import io.github.tehstoneman.betterstorage.common.tileentity.BetterStorageTileEntityTypes;
import io.github.tehstoneman.betterstorage.config.BetterStorageConfig;
import io.github.tehstoneman.betterstorage.network.ModNetwork;
import io.github.tehstoneman.betterstorage.proxy.ClientProxy;
import io.github.tehstoneman.betterstorage.proxy.IProxy;
import io.github.tehstoneman.betterstorage.proxy.ServerProxy;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.OptionalDispenseBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DirectionalPlaceContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DeferredWorkQueue;
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
	public static final ItemGroup		ITEM_GROUP	= new BetterStorageItemGroup();
	public static final SimpleChannel	NETWORK		= ModNetwork.getNetworkChannel();
	public static final IProxy			PROXY		= DistExecutor.<IProxy> runForDist( () -> ClientProxy::new, () -> ServerProxy::new );

	public static Random				RANDOM;

	public BetterStorage()
	{
		// Initialize random numbers
		RANDOM = new Random();

		BetterStorageConfig.register( ModLoadingContext.get() );

		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

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
		DeferredWorkQueue.runLater( () ->
		{
			if( BetterStorageConfig.COMMON.cardboardBoxDispenserPlaceable.get() )
				DispenserBlock.registerDispenseBehavior( BetterStorageBlocks.CARDBOARD_BOX.get(), new OptionalDispenseBehavior()
				{
					/**
					 * Dispense the specified stack, play the dispense sound and spawn particles.
					 */
					@Override
					protected ItemStack dispenseStack( IBlockSource source, ItemStack stack )
					{
						successful = false;
						final Item item = stack.getItem();
						if( item instanceof BlockItem )
						{
							final Direction direction = source.getBlockState().get( DispenserBlock.FACING );
							final BlockPos blockpos = source.getBlockPos().offset( direction );
							final Direction direction1 = source.getWorld().isAirBlock( blockpos.down() ) ? direction : Direction.UP;
							successful = ( (BlockItem)item ).tryPlace( new DirectionalPlaceContext( source.getWorld(), blockpos, direction, stack,
									direction1 ) ) == ActionResultType.SUCCESS;
						}

						return stack;
					}
				} );
			if( BetterStorageConfig.COMMON.useFluidMilk.get() )
				DispenserBlock.registerDispenseBehavior( Items.MILK_BUCKET, new OptionalDispenseBehavior()
				{
					private final DefaultDispenseItemBehavior dispenseBehavior = new DefaultDispenseItemBehavior();

					@Override
					protected ItemStack dispenseStack( IBlockSource source, ItemStack stack )
					{
						final Item bucketitem = stack.getItem();
						final BlockPos blockpos = source.getBlockPos().offset( source.getBlockState().get( DispenserBlock.FACING ) );
						final World world = source.getWorld();
						if( FluidMilk.tryPlaceContainedLiquid( bucketitem, (PlayerEntity)null, world, blockpos, (BlockRayTraceResult)null ) )
							return new ItemStack( Items.BUCKET );
						else
							return dispenseBehavior.dispense( source, stack );
					}
				} );
		} );
	}
}
