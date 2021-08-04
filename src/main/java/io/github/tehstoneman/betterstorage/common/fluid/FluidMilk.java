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
import net.minecraftforge.common.ForgeMod;
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

	public static Material					MATERIAL_MILK	= new Material.Builder( DyeColor.WHITE.getMaterialColor() ).noCollider().liquid()
			.nonSolid().replaceable().build();

	public static FluidAttributes.Builder	BUILDER			= FluidAttributes.builder( stillTexture, flowingTexture ).color( 0xFFFFFFFF )
			.density( 1025 ).temperature( 310 ).viscosity( 2000 );

	/*public static Properties				PROPERTIES		= new Properties( ForgeMod.MILK, ForgeMod.FLOWING_MILK, BUILDER )
			.bucket( () ->
																	{
																		return Items.MILK_BUCKET;
																	} )
			.block( BetterStorageBlocks.MILK );*/

	protected FluidMilk( Properties properties )
	{
		super( properties );
	}

	/*public static boolean interactFluidHandler( @Nonnull PlayerEntity player, @Nonnull Hand hand, @Nonnull IFluidHandler handler )
	{
		final ItemStack heldItem = new ItemStack( BetterStorageItems.MILK_BUCKET.get() );
		return player.getCapability( CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ).map( playerInventory ->
		{

			FluidActionResult fluidActionResult = FluidUtil.tryFillContainerAndStow( heldItem, handler, playerInventory, Integer.MAX_VALUE, player,
					true );
			if( !fluidActionResult.isSuccess() )
				fluidActionResult = FluidUtil.tryEmptyContainerAndStow( heldItem, handler, playerInventory, Integer.MAX_VALUE, player, true );

			if( fluidActionResult.isSuccess() )
				// player.setItemInHand( hand, fluidActionResult.getResult() );
				return true;
			return false;
		} ).orElse( false );
	}*/

	/*@SuppressWarnings( "deprecation" )
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
			final boolean flag = blockstate.canBeReplaced( containedBlock );
			if( blockstate.isAir() || flag || blockstate.getBlock() instanceof ILiquidContainer
					&& ( (ILiquidContainer)blockstate.getBlock() ).canPlaceLiquid( worldIn, posIn, blockstate, containedBlock ) )
			{
				
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
				 
				if( blockstate.getBlock() instanceof ILiquidContainer && containedBlock == BetterStorageFluids.MILK.get() )
				{
					if( ( (ILiquidContainer)blockstate.getBlock() ).placeLiquid( worldIn, posIn, blockstate,
							( (FlowingFluid)containedBlock ).getSource( false ) ) )
						worldIn.playSound( player, posIn, SoundEvents.BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F );
				}
				else
				{
					if( !worldIn.isClientSide() && flag && !material.isLiquid() )
						worldIn.destroyBlock( posIn, true );

					worldIn.playSound( player, posIn, SoundEvents.BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F );
					worldIn.setBlock( posIn, containedBlock.defaultFluidState().createLegacyBlock(), 11 );
				}

				return true;
			}
			else
				return rayTraceResult == null ? false
						: tryPlaceContainedLiquid( bucketitem, player, worldIn, rayTraceResult.getBlockPos().relative( rayTraceResult.getDirection() ),
								(BlockRayTraceResult)null );
		}
	}*/
}
