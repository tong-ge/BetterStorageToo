package io.github.tehstoneman.betterstorage.common.block;

import java.util.Map;

import com.google.common.collect.Maps;

import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityTank;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

public class BlockTank extends BlockContainerBetterStorage
{
	protected static final VoxelShape						SHAPE_TANK				= Block.box( 1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D );
	public static final BooleanProperty						UP						= BlockStateProperties.UP;
	public static final BooleanProperty						DOWN					= BlockStateProperties.DOWN;
	public static final Map< Direction, BooleanProperty >	FACING_TO_PROPERTY_MAP	= Util.make( Maps.newEnumMap( Direction.class ), ( facing ) ->
																					{
																						facing.put( Direction.UP, UP );
																						facing.put( Direction.DOWN, DOWN );
																					} );

	protected BlockTank()
	{
		super( Block.Properties.copy( Blocks.GLASS ).strength( 3.0f ) );

		//@formatter:off
		registerDefaultState( defaultBlockState().getBlockState().setValue( UP, Boolean.valueOf( false ) )
																 .setValue( DOWN, Boolean.valueOf( false ) ) );
		//@formatter:on
	}

	@Override
	protected void createBlockStateDefinition( StateContainer.Builder< Block, BlockState > builder )
	{
		super.createBlockStateDefinition( builder );
		builder.add( UP, DOWN );
	}

	@SuppressWarnings( "deprecation" )
	@Override
	public BlockState updateShape( BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos,
			BlockPos facingPos )
	{
		return facing.getAxis().getPlane() == Direction.Plane.VERTICAL
				? stateIn.setValue( FACING_TO_PROPERTY_MAP.get( facing ), facingState.getBlock() == this )
				: super.updateShape( stateIn, facing, facingState, worldIn, currentPos, facingPos );
	}

	/*
	 * ======================
	 * TileEntity / Rendering
	 * ======================
	 */

	@Override
	public VoxelShape getShape( BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context )
	{
		return SHAPE_TANK;
	}

	@Override
	public TileEntity createTileEntity( BlockState state, IBlockReader world )
	{
		return new TileEntityTank();
	}

	/*
	 * ===========
	 * Interaction
	 * ===========
	 */

	@SuppressWarnings( "deprecation" )
	@Override
	public ActionResultType use( BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit )
	{
		final ItemStack itemStack = player.getItemInHand( hand );
		final LazyOptional< IFluidHandlerItem > capability = itemStack.getCapability( CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY );
		if( capability.isPresent() )
		{
			final boolean success = FluidUtil.interactWithFluidHandler( player, hand, world, pos, hit.getDirection() );
			return success ? ActionResultType.SUCCESS : ActionResultType.PASS;
		}
		return super.use( state, world, pos, player, hand, hit );
	}
}
