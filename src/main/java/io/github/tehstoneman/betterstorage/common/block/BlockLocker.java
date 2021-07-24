package io.github.tehstoneman.betterstorage.common.block;

import javax.annotation.Nullable;

import io.github.tehstoneman.betterstorage.api.ConnectedType;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityContainer;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityLocker;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoorHingeSide;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class BlockLocker extends BlockConnectableContainer implements IWaterLoggable
{
	public static final EnumProperty< DoorHingeSide >	HINGE		= BlockStateProperties.DOOR_HINGE;
	public static final BooleanProperty					WATERLOGGED	= BlockStateProperties.WATERLOGGED;
	protected static final VoxelShape					SHAPE_NORTH	= Block.box( 0.0D, 0.0D, 1.0D, 16.0D, 16.0D, 16.0D );
	protected static final VoxelShape					SHAPE_SOUTH	= Block.box( 0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 15.0D );
	protected static final VoxelShape					SHAPE_WEST	= Block.box( 1.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D );
	protected static final VoxelShape					SHAPE_EAST	= Block.box( 0.0D, 0.0D, 0.0D, 15.0D, 16.0D, 16.0D );
	public static final DirectionProperty				FACING		= BlockStateProperties.HORIZONTAL_FACING;

	public BlockLocker()
	{
		this( Block.Properties.of( Material.WOOD ).strength( 2.5F ).sound( SoundType.WOOD ) );
	}

	public BlockLocker( Properties properties )
	{
		super( properties );

		//@formatter:off
		registerDefaultState( defaultBlockState().getBlockState().setValue( FACING, Direction.NORTH )
													  			 .setValue( HINGE, DoorHingeSide.LEFT )
													  			 .setValue( WATERLOGGED, Boolean.valueOf( false ) ) );
		//@formatter:on
	}

	@Override
	protected void createBlockStateDefinition( StateContainer.Builder< Block, BlockState > builder )
	{
		super.createBlockStateDefinition( builder );
		builder.add( FACING, HINGE, WATERLOGGED );
	}

	/*
	 * ======================
	 * TileEntity / Rendering
	 * ======================
	 */

	@Override
	public BlockRenderType getRenderShape( BlockState state )
	{
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public TileEntity createTileEntity( BlockState state, IBlockReader world )
	{
		return new TileEntityLocker();
	}

	/*
	 * @Override
	 * public boolean hasCustomBreakingProgress( BlockState state )
	 * {
	 * return true;
	 * }
	 */

	@Override
	public VoxelShape getShape( BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context )
	{
		switch( state.getValue( FACING ) )
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

	/*
	 * =========
	 * Placement
	 * =========
	 */

	@Override
	public BlockState getStateForPlacement( BlockItemUseContext context )
	{
		ConnectedType connectedType = ConnectedType.SINGLE;
		final Direction direction = context.getHorizontalDirection().getOpposite();
		final FluidState fluidState = context.getLevel().getFluidState( context.getClickedPos() );
		final boolean sneaking = context.getPlayer().isShiftKeyDown();
		DoorHingeSide hingeSide = getHingeSide( context );
		if( connectedType == ConnectedType.SINGLE && !sneaking )
			if( direction == getDirectionToAttach( context, Direction.DOWN ) )
			{
				connectedType = ConnectedType.SLAVE;
				hingeSide = context.getLevel().getBlockState( context.getClickedPos().relative( Direction.DOWN ) ).getValue( HINGE );
			}
			else if( direction == getDirectionToAttach( context, Direction.UP ) )
			{
				connectedType = ConnectedType.MASTER;
				hingeSide = context.getLevel().getBlockState( context.getClickedPos().relative( Direction.UP ) ).getValue( HINGE );
			}

		return defaultBlockState().setValue( FACING, direction ).setValue( HINGE, hingeSide ).setValue( TYPE, connectedType ).setValue( WATERLOGGED,
				Boolean.valueOf( fluidState.getType() == Fluids.WATER ) );
	}

	@SuppressWarnings( "deprecation" )
	@Override
	public BlockState updateShape( BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos,
			BlockPos facingPos )
	{
		if( stateIn.getValue( WATERLOGGED ) )
			worldIn.getLiquidTicks().scheduleTick( currentPos, Fluids.WATER, Fluids.WATER.getTickDelay( worldIn ) );

		if( facingState.getBlock() == this && facing.getAxis().isVertical() )
		{
			final ConnectedType lockerType = facingState.getValue( TYPE );

			if( stateIn.getValue( TYPE ) == ConnectedType.SINGLE && lockerType != ConnectedType.SINGLE
					&& stateIn.getValue( FACING ) == facingState.getValue( FACING ) && getDirectionToAttached( facingState ) == facing.getOpposite() )
				return stateIn.setValue( TYPE, lockerType.opposite() );
		}
		else if( getDirectionToAttached( stateIn ) == facing )
			return stateIn.setValue( TYPE, ConnectedType.SINGLE );

		return super.updateShape( stateIn, facing, facingState, worldIn, currentPos, facingPos );
	}

	private DoorHingeSide getHingeSide( BlockItemUseContext context )
	{
		final BlockPos blockPos = context.getClickedPos();
		final Direction direction = context.getHorizontalDirection();
		final int offX = direction.getStepX();
		final int offY = direction.getStepY();
		final Vector3d v = context.getClickLocation();
		final double hitX = v.x - blockPos.getX();
		final double hitY = v.z - blockPos.getZ();
		return ( offX >= 0 || !( hitY < 0.5D ) ) && ( offX <= 0 || !( hitY > 0.5D ) ) && ( offY >= 0 || !( hitX > 0.5D ) )
				&& ( offY <= 0 || !( hitX < 0.5D ) ) ? DoorHingeSide.LEFT : DoorHingeSide.RIGHT;
	}

	@Nullable
	private Direction getDirectionToAttach( BlockItemUseContext context, Direction facing )
	{
		final BlockState faceState = context.getLevel().getBlockState( context.getClickedPos().relative( facing ) );
		return faceState.getBlock() == this && faceState.getValue( TYPE ) == ConnectedType.SINGLE ? faceState.getValue( FACING ) : null;
	}

	public static Direction getDirectionToAttached( BlockState state )
	{
		return state.getValue( TYPE ) == ConnectedType.SLAVE ? Direction.DOWN : Direction.UP;
	}

	@Override
	public void setPlacedBy( World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack )
	{
		if( stack.hasCustomHoverName() )
		{
			final TileEntity tileentity = worldIn.getBlockEntity( pos );
			if( tileentity instanceof TileEntityContainer )
				( (TileEntityContainer)tileentity ).setCustomName( stack.getDisplayName() );
		}
	}

	@SuppressWarnings( "deprecation" )
	@Override
	public void onRemove( BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving )
	{
		if( state.getBlock() != newState.getBlock() )
		{
			final TileEntity tileentity = worldIn.getBlockEntity( pos );
			if( tileentity instanceof TileEntityContainer )
			{
				( (TileEntityContainer)tileentity ).dropInventoryItems();
				worldIn.updateNeighbourForOutputSignal( pos, this );
			}

			super.onRemove( state, worldIn, pos, newState, isMoving );
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
		return state.setValue( FACING, rot.rotate( state.getValue( FACING ) ) );
	}

	@SuppressWarnings( "deprecation" )
	@Override
	public BlockState mirror( BlockState state, Mirror mirrorIn )
	{
		return state.rotate( mirrorIn.getRotation( state.getValue( FACING ) ) );
	}

	@Override
	public boolean hasAnalogOutputSignal( BlockState state )
	{
		return true;
	}

	@Override
	public int getAnalogOutputSignal( BlockState blockState, World worldIn, BlockPos pos )
	{
		return calcRedstoneFromInventory(
				( (TileEntityLocker)worldIn.getBlockEntity( pos ) ).getCapability( CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ) );
	}

	public static int calcRedstoneFromInventory( @Nullable LazyOptional< IItemHandler > lazyOptional )
	{
		if( !lazyOptional.isPresent() )
			return 0;
		else
		{
			final IItemHandler inventory = lazyOptional.orElseGet( null );
			int i = 0;
			float f = 0.0F;

			for( int j = 0; j < inventory.getSlots(); ++j )
			{
				final ItemStack itemstack = inventory.getStackInSlot( j );
				if( !itemstack.isEmpty() )
				{
					f += (float)itemstack.getCount() / itemstack.getMaxStackSize();
					++i;
				}
			}

			f = f / inventory.getSlots();
			return MathHelper.floor( f * 14.0F ) + ( i > 0 ? 1 : 0 );
		}
	}

	/*
	 * ===========
	 * Interaction
	 * ===========
	 */

	@Override
	public ActionResultType use( BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit )
	{
		if( hit.getDirection() == state.getValue( FACING ) )
			if( worldIn.isClientSide )
				return ActionResultType.SUCCESS;
			else
			{
				final INamedContainerProvider locker = getMenuProvider( state, worldIn, pos );
				if( locker != null )
				{
					NetworkHooks.openGui( (ServerPlayerEntity)player, locker, pos );
					player.awardStat( getOpenStat() );
				}
				return ActionResultType.SUCCESS;
			}
		return ActionResultType.PASS;
	}

	protected Stat< ResourceLocation > getOpenStat()
	{
		return Stats.CUSTOM.get( Stats.OPEN_CHEST );
	}

	@Override
	@Nullable
	public INamedContainerProvider getMenuProvider( BlockState state, World worldIn, BlockPos pos )
	{
		return this.getContainer( state, worldIn, pos, false );
	}

	public INamedContainerProvider getContainer( BlockState state, World worldIn, BlockPos pos, boolean allowBlockedChest )
	{
		final TileEntity tileentity = worldIn.getBlockEntity( pos );
		if( !( tileentity instanceof TileEntityLocker ) )
			return null;
		else if( !allowBlockedChest && isBlocked( worldIn, pos ) )
			return null;
		else
		{
			final TileEntityLocker locker = (TileEntityLocker)tileentity;
			final ConnectedType chesttype = state.getValue( TYPE );
			if( chesttype == ConnectedType.SINGLE )
				return locker;
			else
			{
				final BlockPos blockpos = pos.relative( getDirectionToAttached( state ) );
				final BlockState iblockstate = worldIn.getBlockState( blockpos );
				if( iblockstate.getBlock() == this )
				{
					final ConnectedType chesttype1 = iblockstate.getValue( TYPE );
					if( chesttype1 != ConnectedType.SINGLE && chesttype != chesttype1 && iblockstate.getValue( FACING ) == state.getValue( FACING ) )
						if( !allowBlockedChest && isBlocked( worldIn, blockpos ) )
							return null;
				}

				return locker;
			}
		}
	}

	private static boolean isBlocked( IWorld world, BlockPos pos )
	{
		return isBehindSolidBlock( world, pos );
	}

	private static boolean isBehindSolidBlock( IBlockReader reader, BlockPos worldIn )
	{
		final Direction facing = reader.getBlockState( worldIn ).getValue( FACING );
		final BlockPos blockpos = worldIn.relative( facing );
		return reader.getBlockState( blockpos ).isRedstoneConductor( reader, blockpos );
	}

	/*
	 * =====
	 * Fluid
	 * =====
	 */

	@SuppressWarnings( "deprecation" )
	@Override
	public FluidState getFluidState( BlockState state )
	{
		return state.getValue( WATERLOGGED ) ? Fluids.WATER.getSource( false ) : super.getFluidState( state );
	}
}
