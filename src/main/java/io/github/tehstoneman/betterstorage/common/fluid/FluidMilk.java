package io.github.tehstoneman.betterstorage.common.fluid;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import io.github.tehstoneman.betterstorage.common.block.BetterStorageBlocks;
import io.github.tehstoneman.betterstorage.common.item.BetterStorageItems;
import io.github.tehstoneman.betterstorage.util.BetterStorageResource;
import net.minecraft.block.BlockState;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

public abstract class FluidMilk extends ForgeFlowingFluid
{
	public static BetterStorageResource		stillTexture	= new BetterStorageResource( "block/milk_still" );
	public static BetterStorageResource		flowingTexture	= new BetterStorageResource( "block/milk_flowing" );

	public static Material					MATERIAL_MILK	= new Material.Builder( DyeColor.WHITE.getMapColor() ).doesNotBlockMovement().liquid()
			.notSolid().replaceable().build();

	public static FluidAttributes.Builder	BUILDER			= FluidAttributes.builder( stillTexture, flowingTexture ).color( 0xFFFFFFFF )
			.density( 1025 ).temperature( 310 ).viscosity( 2000 );

	public static Properties				PROPERTIES		= new Properties( BetterStorageFluids.MILK, BetterStorageFluids.FLOWING_MILK, BUILDER )
			.bucket( () ->
																	{
																		return Items.MILK_BUCKET;
																	} )
			.block( BetterStorageBlocks.MILK );

	protected FluidMilk( Properties properties )
	{
		super( properties );
	}

	public static boolean interactFluidHandler( @Nonnull PlayerEntity player, @Nonnull Hand hand, @Nonnull IFluidHandler handler )
	{
		final ItemStack heldItem = new ItemStack( BetterStorageItems.MILK_BUCKET.get() );
		return player.getCapability( CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ).map( playerInventory ->
		{

			FluidActionResult fluidActionResult = FluidUtil.tryFillContainerAndStow( heldItem, handler, playerInventory, Integer.MAX_VALUE, player,
					true );
			if( !fluidActionResult.isSuccess() )
				fluidActionResult = FluidUtil.tryEmptyContainerAndStow( heldItem, handler, playerInventory, Integer.MAX_VALUE, player, true );

			if( fluidActionResult.isSuccess() )
				// player.setHeldItem( hand, fluidActionResult.getResult() );
				return true;
			return false;
		} ).orElse( false );
	}

	@SuppressWarnings( "deprecation" )
	public static boolean tryPlaceContainedLiquid( Item bucketitem, @Nullable PlayerEntity player, World worldIn, BlockPos posIn,
			@Nullable BlockRayTraceResult rayTraceResult )
	{
		if( bucketitem != Items.MILK_BUCKET )
			return false;
		else
		{
			final Fluid containedBlock = BetterStorageFluids.MILK.get();
			final BlockState blockstate = worldIn.getBlockState( posIn );
			final Material material = blockstate.getMaterial();
			final boolean flag = blockstate.isReplaceable( containedBlock );
			if( blockstate.isAir() || flag || blockstate.getBlock() instanceof ILiquidContainer
					&& ( (ILiquidContainer)blockstate.getBlock() ).canContainFluid( worldIn, posIn, blockstate, containedBlock ) )
			{
				/*
				 * if( worldIn.dimension.doesWaterVaporize() && containedBlock == BetterStorageFluids.MILK.get() )
				 * {
				 * final int i = posIn.getX();
				 * final int j = posIn.getY();
				 * final int k = posIn.getZ();
				 * worldIn.playSound( player, posIn, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F,
				 * 2.6F + ( worldIn.rand.nextFloat() - worldIn.rand.nextFloat() ) * 0.8F );
				 * 
				 * for( int l = 0; l < 8; ++l )
				 * worldIn.addParticle( ParticleTypes.LARGE_SMOKE, i + Math.random(), j + Math.random(), k + Math.random(), 0.0D, 0.0D, 0.0D );
				 * }
				 * else
				 */
				if( blockstate.getBlock() instanceof ILiquidContainer && containedBlock == BetterStorageFluids.MILK.get() )
				{
					if( ( (ILiquidContainer)blockstate.getBlock() ).receiveFluid( worldIn, posIn, blockstate,
							( (FlowingFluid)containedBlock ).getStillFluidState( false ) ) )
						worldIn.playSound( player, posIn, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F );
				}
				else
				{
					if( !worldIn.isRemote && flag && !material.isLiquid() )
						worldIn.destroyBlock( posIn, true );

					worldIn.playSound( player, posIn, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F );
					worldIn.setBlockState( posIn, containedBlock.getDefaultState().getBlockState(), 11 );
				}

				return true;
			}
			else
				return rayTraceResult == null ? false
						: tryPlaceContainedLiquid( bucketitem, player, worldIn, rayTraceResult.getPos().offset( rayTraceResult.getFace() ),
								(BlockRayTraceResult)null );
		}
	}
}
