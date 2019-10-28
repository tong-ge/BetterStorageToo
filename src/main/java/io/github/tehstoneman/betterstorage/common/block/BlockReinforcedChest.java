package io.github.tehstoneman.betterstorage.common.block;

import java.util.List;

import javax.annotation.Nullable;

import io.github.tehstoneman.betterstorage.api.EnumConnectedType;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityContainer;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityReinforcedChest;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlockReinforcedChest extends BlockConnectableContainer implements IWaterLoggable
{
	public static final BooleanProperty		WATERLOGGED		= BlockStateProperties.WATERLOGGED;
	protected static final VoxelShape		SHAPE_NORTH		= Block.makeCuboidShape( 1.0D, 0.0D, 0.0D, 15.0D, 14.0D, 15.0D );
	protected static final VoxelShape		SHAPE_SOUTH		= Block.makeCuboidShape( 1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 16.0D );
	protected static final VoxelShape		SHAPE_WEST		= Block.makeCuboidShape( 0.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D );
	protected static final VoxelShape		SHAPE_EAST		= Block.makeCuboidShape( 1.0D, 0.0D, 1.0D, 16.0D, 14.0D, 15.0D );
	protected static final VoxelShape		SHAPE_SINGLE	= Block.makeCuboidShape( 1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D );
	public static final DirectionProperty	FACING			= BlockStateProperties.HORIZONTAL_FACING;

	public BlockReinforcedChest()
	{
		this( Block.Properties.create( Material.WOOD ).hardnessAndResistance( 5.0F, 6.0F ).sound( SoundType.WOOD ) );
	}

	public BlockReinforcedChest( Properties properties )
	{
		super( properties );

		//@formatter:off
		setDefaultState( stateContainer.getBaseState().with( FACING, Direction.NORTH )
													  .with( WATERLOGGED, Boolean.valueOf( false ) ) );
		//@formatter:on
	}

	@Override
	protected void fillStateContainer( StateContainer.Builder< Block, BlockState > builder )
	{
		super.fillStateContainer( builder );
		builder.add( FACING, WATERLOGGED );
	}

	/*
	 * ======================
	 * TileEntity / Rendering
	 * ======================
	 */

	@Override
	public BlockRenderType getRenderType( BlockState state )
	{
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public TileEntity createTileEntity( BlockState state, IBlockReader world )
	{
		return new TileEntityReinforcedChest();
	}

	@Override
	public boolean hasCustomBreakingProgress( BlockState state )
	{
		return true;
	}

	/*
	 * =========
	 * Placement
	 * =========
	 */

	@Override
	public VoxelShape getShape( BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context )
	{
		if( state.get( TYPE ) == EnumConnectedType.SINGLE )
			return SHAPE_SINGLE;
		switch( getDirectionToAttached( state ) )
		{
		case NORTH:
		default:
			return SHAPE_NORTH;
		case SOUTH:
			return SHAPE_SOUTH;
		case WEST:
			return SHAPE_WEST;
		case EAST:
			return SHAPE_EAST;
		}
	}

	@Override
	public BlockState getStateForPlacement( BlockItemUseContext context )
	{
		EnumConnectedType connectedType = EnumConnectedType.SINGLE;
		Direction direction = context.getPlacementHorizontalFacing().getOpposite();
		final IFluidState fluidState = context.getWorld().getFluidState( context.getPos() );
		final boolean sneaking = context.isPlacerSneaking();

		final Direction direction1 = context.getFace();
		if( direction1.getAxis().isHorizontal() && sneaking )
		{
			final Direction direction2 = getDirectionToAttach( context, direction1.getOpposite() );
			if( direction2 != null && direction2.getAxis() != direction1.getAxis() )
			{
				direction = direction2;
				connectedType = direction2.rotateYCCW() == direction1.getOpposite() ? EnumConnectedType.MASTER : EnumConnectedType.SLAVE;
			}
		}

		if( connectedType == EnumConnectedType.SINGLE && !sneaking )
			if( direction == getDirectionToAttach( context, direction.rotateY() ) )
				connectedType = EnumConnectedType.SLAVE;
			else if( direction == getDirectionToAttach( context, direction.rotateYCCW() ) )
				connectedType = EnumConnectedType.MASTER;

		return getDefaultState().with( FACING, direction ).with( TYPE, connectedType ).with( WATERLOGGED,
				Boolean.valueOf( fluidState.getFluid() == Fluids.WATER ) );
	}

	@Override
	public BlockState updatePostPlacement( BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos,
			BlockPos facingPos )
	{
		if( stateIn.get( WATERLOGGED ) )
			worldIn.getPendingFluidTicks().scheduleTick( currentPos, Fluids.WATER, Fluids.WATER.getTickRate( worldIn ) );

		if( facingState.getBlock() == this && facing.getAxis().isHorizontal() )
		{
			final EnumConnectedType lockerType = facingState.get( TYPE );

			if( stateIn.get( TYPE ) == EnumConnectedType.SINGLE && lockerType != EnumConnectedType.SINGLE
					&& stateIn.get( FACING ) == facingState.get( FACING ) && getDirectionToAttached( facingState ) == facing.getOpposite() )
				return stateIn.with( TYPE, lockerType.opposite() );
		}
		else if( getDirectionToAttached( stateIn ) == facing )
			return stateIn.with( TYPE, EnumConnectedType.SINGLE );

		return super.updatePostPlacement( stateIn, facing, facingState, worldIn, currentPos, facingPos );
	}

	/**
	 * Returns facing pointing to a chest to form a double chest with, null otherwise
	 *
	 * @param context
	 * @param facing
	 * @return
	 */
	@Nullable
	private Direction getDirectionToAttach( BlockItemUseContext context, Direction facing )
	{
		final BlockState blockState = context.getWorld().getBlockState( context.getPos().offset( facing ) );
		return blockState.getBlock() == this && blockState.get( TYPE ) == EnumConnectedType.SINGLE ? blockState.get( FACING ) : null;
	}

	/**
	 * Returns a facing pointing from the given state to its attached double chest
	 *
	 * @param state
	 * @return
	 */
	public static Direction getDirectionToAttached( BlockState state )
	{
		final Direction direction = state.get( FACING );
		return state.get( TYPE ) == EnumConnectedType.SLAVE ? direction.rotateY() : direction.rotateYCCW();
	}

	@Override
	public void onBlockPlacedBy( World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack )
	{
		if( stack.hasDisplayName() )
		{
			final TileEntity tileentity = worldIn.getTileEntity( pos );
			if( tileentity instanceof TileEntityReinforcedChest )
				( (TileEntityReinforcedChest)tileentity ).setCustomName( stack.getDisplayName() );
		}
	}

	@Override
	public void onReplaced( BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving )
	{
		if( state.getBlock() != newState.getBlock() )
		{
			final TileEntity tileentity = worldIn.getTileEntity( pos );
			if( tileentity instanceof TileEntityContainer )
			{
				( (TileEntityContainer)tileentity ).dropInventoryItems();
				worldIn.updateComparatorOutputLevel( pos, this );
			}

			super.onReplaced( state, worldIn, pos, newState, isMoving );
		}
	}

	/*
	 * ===========
	 * Adjustments
	 * ===========
	 */

	@Override
	public BlockState rotate( BlockState state, Rotation rot )
	{
		return state.with( FACING, rot.rotate( state.get( FACING ) ) );
	}

	@Override
	public BlockState mirror( BlockState state, Mirror mirrorIn )
	{
		return state.rotate( mirrorIn.toRotation( state.get( FACING ) ) );
	}

	/*
	 * ===========
	 * Interaction
	 * ===========
	 */

	@Override
	public boolean onBlockActivated( BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit )
	{
		if( worldIn.isRemote )
			return true;
		else
		{
			final INamedContainerProvider chest = getContainer( state, worldIn, pos );
			if( chest != null )
			{
				NetworkHooks.openGui( (ServerPlayerEntity)player, chest, pos );
				player.addStat( getOpenStat() );
			}

			return true;
		}
	}

	protected Stat< ResourceLocation > getOpenStat()
	{
		return Stats.CUSTOM.get( Stats.OPEN_CHEST );
	}

	@Override
	@Nullable
	public INamedContainerProvider getContainer( BlockState state, World worldIn, BlockPos pos )
	{
		return this.getContainer( state, worldIn, pos, false );
	}

	/**
	 * Gets the chest inventory at the given location, returning null if there is no chest at that location or optionally
	 * if the chest is blocked. Handles large chests.
	 *
	 * @param state
	 *            The current state
	 * @param worldIn
	 *            The world
	 * @param pos
	 *            The position to check
	 * @param allowBlockedChest
	 *            If false, then if the chest is blocked then <code>null</code> will be returned. If true,
	 *            then the chest can still be blocked (used by hoppers).
	 */
	@Nullable
	public TileEntityReinforcedChest getContainer( BlockState state, World worldIn, BlockPos pos, boolean allowBlockedChest )
	{
		final TileEntity tileentity = worldIn.getTileEntity( pos );
		if( !( tileentity instanceof TileEntityReinforcedChest ) )
			return null;
		else if( !allowBlockedChest && isBlocked( worldIn, pos ) )
			return null;
		else
		{
			final TileEntityReinforcedChest ilockablecontainer = (TileEntityReinforcedChest)tileentity;
			final EnumConnectedType chesttype = state.get( TYPE );
			if( chesttype == EnumConnectedType.SINGLE )
				return ilockablecontainer;
			else
			{
				final BlockPos blockpos = pos.offset( getDirectionToAttached( state ) );
				final BlockState iblockstate = worldIn.getBlockState( blockpos );
				if( iblockstate.getBlock() == this )
				{
					final EnumConnectedType chesttype1 = iblockstate.get( TYPE );
					if( chesttype1 != EnumConnectedType.SINGLE && chesttype != chesttype1 && iblockstate.get( FACING ) == state.get( FACING ) )
						if( !allowBlockedChest && isBlocked( worldIn, blockpos ) )
							return null;
				}

				return ilockablecontainer;
			}
		}
	}

	private static boolean isBlocked( IWorld world, BlockPos pos )
	{
		return isBelowSolidBlock( world, pos ) || isCatSittingOn( world, pos );
	}

	private static boolean isBelowSolidBlock( IBlockReader reader, BlockPos worldIn )
	{
		final BlockPos blockpos = worldIn.up();
		return reader.getBlockState( blockpos ).isNormalCube( reader, blockpos );
	}

	private static boolean isCatSittingOn( IWorld world, BlockPos pos )
	{
		final List< CatEntity > list = world.getEntitiesWithinAABB( CatEntity.class,
				new AxisAlignedBB( pos.getX(), pos.getY() + 1, pos.getZ(), pos.getX() + 1, pos.getY() + 2, pos.getZ() + 1 ) );
		if( !list.isEmpty() )
			for( final CatEntity catentity : list )
				if( catentity.isSitting() )
					return true;

		return false;
	}

	/*
	 * =====
	 * Fluid
	 * =====
	 */

	@Override
	public IFluidState getFluidState( BlockState blockState )
	{
		return blockState.get( WATERLOGGED ) ? Fluids.WATER.getStillFluidState( false ) : super.getFluidState( blockState );
	}
}
