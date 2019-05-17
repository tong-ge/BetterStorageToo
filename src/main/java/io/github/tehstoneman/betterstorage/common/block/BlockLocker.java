package io.github.tehstoneman.betterstorage.common.block;

import java.util.List;

import javax.annotation.Nullable;

import io.github.tehstoneman.betterstorage.api.EnumLockerType;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityLocker;
import net.minecraft.block.Block;
import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.init.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoorHingeSide;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class BlockLocker extends BlockLockable implements IBucketPickupHandler, ILiquidContainer
{
	public static final EnumProperty< EnumLockerType >	TYPE		= EnumProperty.create( "type", EnumLockerType.class );
	public static final EnumProperty< DoorHingeSide >	HINGE		= BlockStateProperties.DOOR_HINGE;
	public static final BooleanProperty					WATERLOGGED	= BlockStateProperties.WATERLOGGED;
	protected static final VoxelShape					SHAPE_NORTH	= Block.makeCuboidShape( 0.0D, 0.0D, 1.0D, 16.0D, 16.0D, 16.0D );
	protected static final VoxelShape					SHAPE_SOUTH	= Block.makeCuboidShape( 0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 15.0D );
	protected static final VoxelShape					SHAPE_WEST	= Block.makeCuboidShape( 1.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D );
	protected static final VoxelShape					SHAPE_EAST	= Block.makeCuboidShape( 0.0D, 0.0D, 0.0D, 15.0D, 16.0D, 16.0D );

	public BlockLocker()
	{
		this( Block.Properties.create( Material.WOOD ).hardnessAndResistance( 5.0F, 6.0F ).sound( SoundType.WOOD ) );
	}

	public BlockLocker( Properties properties )
	{
		super( properties );

		//@formatter:off
		setDefaultState( stateContainer.getBaseState().with( FACING, EnumFacing.NORTH )
													  .with( HINGE, DoorHingeSide.LEFT )
		  											  .with( WATERLOGGED, Boolean.valueOf( false ) ) );
		//@formatter:on
	}

	@Override
	public boolean isFullCube( IBlockState state )
	{
		return false;
	}

	@Override
	public IBlockState updatePostPlacement( IBlockState stateIn, EnumFacing facing, IBlockState facingState, IWorld worldIn, BlockPos currentPos,
			BlockPos facingPos )
	{
		if( stateIn.get( WATERLOGGED ) )
			worldIn.getPendingFluidTicks().scheduleTick( currentPos, Fluids.WATER, Fluids.WATER.getTickRate( worldIn ) );

		if( facingState.getBlock() == this && facing.getAxis().isVertical() )
		{
			final EnumLockerType lockerType = facingState.get( TYPE );

			if( stateIn.get( TYPE ) == EnumLockerType.SINGLE && lockerType != EnumLockerType.SINGLE
					&& stateIn.get( FACING ) == facingState.get( FACING ) && getDirectionToAttached( facingState ) == facing.getOpposite() )
				return stateIn.with( TYPE, lockerType.opposite() );
		}
		else
			if( getDirectionToAttached( stateIn ) == facing )
				return stateIn.with( TYPE, EnumLockerType.SINGLE );

		return super.updatePostPlacement( stateIn, facing, facingState, worldIn, currentPos, facingPos );
	}

	@Override
	public VoxelShape getShape( IBlockState state, IBlockReader worldIn, BlockPos pos )
	{
		switch( state.get( FACING ) )
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

	/**
	 * Returns a facing pointing from the given state to its attached double locker
	 */
	public static EnumFacing getDirectionToAttached( IBlockState state )
	{
		return state.get( TYPE ) == EnumLockerType.TOP ? EnumFacing.DOWN : EnumFacing.UP;
	}

	@Override
	public IBlockState getStateForPlacement( BlockItemUseContext context )
	{
		EnumLockerType chesttype = EnumLockerType.SINGLE;
		final EnumFacing enumfacing = context.getPlacementHorizontalFacing().getOpposite();
		final IFluidState ifluidstate = context.getWorld().getFluidState( context.getPos() );
		final boolean flag = context.isPlacerSneaking();
		final EnumFacing enumfacing1 = context.getFace();
		DoorHingeSide hingeSide = getHingeSide( context );
		/*
		 * if( enumfacing1.getAxis().isHorizontal() && flag )
		 * {
		 * final EnumFacing enumfacing2 = getDirectionToAttach( context, enumfacing1.getOpposite() );
		 * if( enumfacing2 != null && enumfacing2.getAxis() != enumfacing1.getAxis() )
		 * {
		 * enumfacing = enumfacing2;
		 * chesttype = enumfacing2.rotateYCCW() == enumfacing1.getOpposite() ? EnumLockerType.RIGHT : EnumLockerType.LEFT;
		 * }
		 * }
		 */

		if( chesttype == EnumLockerType.SINGLE && !flag )
			if( enumfacing == getDirectionToAttach( context, EnumFacing.DOWN ) )
			{
				chesttype = EnumLockerType.TOP;
				hingeSide = context.getWorld().getBlockState( context.getPos().offset( EnumFacing.DOWN ) ).get( HINGE );
			}
			else
				if( enumfacing == getDirectionToAttach( context, enumfacing.rotateYCCW() ) )
				{
					chesttype = EnumLockerType.BOTTOM;
					hingeSide = context.getWorld().getBlockState( context.getPos().offset( EnumFacing.DOWN ) ).get( HINGE );
				}

		return getDefaultState().with( FACING, enumfacing ).with( HINGE, hingeSide ).with( TYPE, chesttype ).with( WATERLOGGED,
				Boolean.valueOf( ifluidstate.getFluid() == Fluids.WATER ) );
	}

	private DoorHingeSide getHingeSide( BlockItemUseContext context )
	{
		final IBlockReader blockReader = context.getWorld();
		final BlockPos blockPos = context.getPos();
		final EnumFacing facing = context.getPlacementHorizontalFacing();
		final BlockPos blockAbove = blockPos.up();
		final EnumFacing facingLeft = facing.rotateYCCW();
		final IBlockState blockLeft = blockReader.getBlockState( blockPos.offset( facingLeft ) );
		final IBlockState blockAboveLeft = blockReader.getBlockState( blockAbove.offset( facingLeft ) );
		final EnumFacing facingRight = facing.rotateY();
		final IBlockState blockRight = blockReader.getBlockState( blockPos.offset( facingRight ) );
		final IBlockState blockAboveRight = blockReader.getBlockState( blockAbove.offset( facingRight ) );
		final int i = ( blockLeft.isBlockNormalCube() ? -1 : 0 ) + ( blockAboveLeft.isBlockNormalCube() ? -1 : 0 )
				+ ( blockRight.isBlockNormalCube() ? 1 : 0 ) + ( blockAboveRight.isBlockNormalCube() ? 1 : 0 );
		final boolean flagLeft = blockLeft.getBlock() == this && blockLeft.get( TYPE ) != EnumLockerType.TOP;
		final boolean flagRight = blockRight.getBlock() == this && blockLeft.get( TYPE ) != EnumLockerType.TOP;
		if( ( !flagLeft || flagRight ) && i <= 0 )
		{
			if( ( !flagRight || flagLeft ) && i >= 0 )
			{
				final int j = facing.getXOffset();
				final int k = facing.getZOffset();
				final float f = context.getHitX();
				final float f1 = context.getHitZ();
				return ( j >= 0 || !( f1 < 0.5F ) ) && ( j <= 0 || !( f1 > 0.5F ) ) && ( k >= 0 || !( f > 0.5F ) ) && ( k <= 0 || !( f < 0.5F ) )
						? DoorHingeSide.LEFT
						: DoorHingeSide.RIGHT;
			}
			else
				return DoorHingeSide.LEFT;
		}
		else
			return DoorHingeSide.RIGHT;
	}

	@Override
	public Fluid pickupFluid( IWorld worldIn, BlockPos pos, IBlockState state )
	{
		if( state.get( WATERLOGGED ) )
		{
			worldIn.setBlockState( pos, state.with( WATERLOGGED, Boolean.valueOf( false ) ), 3 );
			return Fluids.WATER;
		}
		else
			return Fluids.EMPTY;
	}

	@Override
	public IFluidState getFluidState( IBlockState state )
	{
		return state.get( WATERLOGGED ) ? Fluids.WATER.getStillFluidState( false ) : super.getFluidState( state );
	}

	@Override
	public boolean canContainFluid( IBlockReader worldIn, BlockPos pos, IBlockState state, Fluid fluidIn )
	{
		return !state.get( WATERLOGGED ) && fluidIn == Fluids.WATER;
	}

	@Override
	public boolean receiveFluid( IWorld worldIn, BlockPos pos, IBlockState state, IFluidState fluidStateIn )
	{
		if( !state.get( WATERLOGGED ) && fluidStateIn.getFluid() == Fluids.WATER )
		{
			if( !worldIn.isRemote() )
			{
				worldIn.setBlockState( pos, state.with( WATERLOGGED, Boolean.valueOf( true ) ), 3 );
				worldIn.getPendingFluidTicks().scheduleTick( pos, Fluids.WATER, Fluids.WATER.getTickRate( worldIn ) );
			}

			return true;
		}
		else
			return false;
	}

	/**
	 * Returns facing pointing to a chest to form a double chest with, null otherwise
	 */

	@Nullable
	private EnumFacing getDirectionToAttach( BlockItemUseContext context, EnumFacing facing )
	{
		final IBlockState blockState = context.getWorld().getBlockState( context.getPos() );
		final IBlockState faceState = context.getWorld().getBlockState( context.getPos().offset( facing ) );
		return faceState.getBlock() == this && faceState.get( TYPE ) == EnumLockerType.SINGLE ? faceState.get( FACING ) : null;
	}

	@Override
	protected Stat< ResourceLocation > getOpenStat()
	{
		return StatList.CUSTOM.get( StatList.OPEN_CHEST );
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
	public ILockableContainer getContainer( IBlockState state, World worldIn, BlockPos pos, boolean allowBlockedChest )
	{
		final TileEntity tileentity = worldIn.getTileEntity( pos );
		if( !( tileentity instanceof TileEntityChest ) )
			return null;
		else
			if( !allowBlockedChest && isBlocked( worldIn, pos ) )
				return null;
			else
			{
				final ILockableContainer ilockablecontainer = (TileEntityChest)tileentity;
				/*
				 * final ChestType chesttype = state.get( TYPE );
				 * if( chesttype == ChestType.SINGLE )
				 * return ilockablecontainer;
				 * else
				 * {
				 * final BlockPos blockpos = pos.offset( getDirectionToAttached( state ) );
				 * final IBlockState iblockstate = worldIn.getBlockState( blockpos );
				 * if( iblockstate.getBlock() == this )
				 * {
				 * final ChestType chesttype1 = iblockstate.get( TYPE );
				 * if( chesttype1 != ChestType.SINGLE && chesttype != chesttype1 && iblockstate.get( FACING ) == state.get( FACING ) )
				 * {
				 * if( !allowBlockedChest && isBlocked( worldIn, blockpos ) )
				 * return null;
				 *
				 * final TileEntity tileentity1 = worldIn.getTileEntity( blockpos );
				 * if( tileentity1 instanceof TileEntityChest )
				 * {
				 * final ILockableContainer ilockablecontainer1 = chesttype == ChestType.RIGHT ? ilockablecontainer
				 * : (ILockableContainer)tileentity1;
				 * final ILockableContainer ilockablecontainer2 = chesttype == ChestType.RIGHT ? (ILockableContainer)tileentity1
				 * : ilockablecontainer;
				 * ilockablecontainer = new InventoryLargeChest( new TextComponentTranslation( "container.chestDouble" ),
				 * ilockablecontainer1, ilockablecontainer2 );
				 * }
				 * }
				 * }
				 */

				return ilockablecontainer;
			}
	}

	@Override
	public TileEntity createTileEntity( IBlockState state, IBlockReader world )
	{
		return new TileEntityLocker();
	}

	private boolean isBlocked( World worldIn, BlockPos pos )
	{
		return isBelowSolidBlock( worldIn, pos ) || isOcelotSittingOnChest( worldIn, pos );
	}

	private boolean isBelowSolidBlock( IBlockReader worldIn, BlockPos pos )
	{
		return worldIn.getBlockState( pos.up() ).doesSideBlockChestOpening( worldIn, pos.up(), EnumFacing.DOWN );
	}

	private boolean isOcelotSittingOnChest( World worldIn, BlockPos pos )
	{
		final List< EntityOcelot > list = worldIn.getEntitiesWithinAABB( EntityOcelot.class,
				new AxisAlignedBB( pos.getX(), pos.getY() + 1, pos.getZ(), pos.getX() + 1, pos.getY() + 2, pos.getZ() + 1 ) );
		if( !list.isEmpty() )
			for( final EntityOcelot entityocelot : list )
				if( entityocelot.isSitting() )
					return true;

		return false;
	}

	@Override
	protected void fillStateContainer( StateContainer.Builder< Block, IBlockState > builder )
	{
		super.fillStateContainer( builder );
		builder.add( HINGE, WATERLOGGED );
	}

	@Override
	public BlockFaceShape getBlockFaceShape( IBlockReader worldIn, IBlockState state, BlockPos pos, EnumFacing face )
	{
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	public boolean allowsMovement( IBlockState state, IBlockReader worldIn, BlockPos pos, PathType type )
	{
		return false;
	}
}
