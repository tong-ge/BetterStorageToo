package io.github.tehstoneman.betterstorage.common.block;

import javax.annotation.Nullable;

import io.github.tehstoneman.betterstorage.api.ConnectedType;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityContainer;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityLocker;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkHooks;

public class BlockLocker extends BlockConnectableContainer implements SimpleWaterloggedBlock
{
	public static final EnumProperty< DoorHingeSide >	HINGE		= BlockStateProperties.DOOR_HINGE;
	public static final BooleanProperty					WATERLOGGED	= BlockStateProperties.WATERLOGGED;
	protected static final VoxelShape					SHAPE_NORTH	= Block.box( 0.0D, 0.0D, 1.0D, 16.0D, 16.0D, 16.0D );
	protected static final VoxelShape					SHAPE_SOUTH	= Block.box( 0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 15.0D );
	protected static final VoxelShape					SHAPE_WEST	= Block.box( 1.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D );
	protected static final VoxelShape					SHAPE_EAST	= Block.box( 0.0D, 0.0D, 0.0D, 15.0D, 16.0D, 16.0D );
	public static final DirectionProperty				FACING		= BlockStateProperties.FACING;

	public BlockLocker()
	{
		this( Block.Properties.of( Material.WOOD ).strength( 2.5F ).sound( SoundType.WOOD ) );
	}

	public BlockLocker( Properties properties )
	{
		super( properties );

		//@formatter:off
		registerDefaultState( defaultBlockState().setValue( FACING, Direction.NORTH )
												 .setValue( HINGE, DoorHingeSide.LEFT )
												 .setValue( WATERLOGGED, Boolean.valueOf( false ) ) );
		//@formatter:on
	}

	@Override
	protected void createBlockStateDefinition( StateDefinition.Builder< Block, BlockState > builder )
	{
		super.createBlockStateDefinition( builder );
		builder.add( FACING, HINGE, WATERLOGGED );
	}

	/*
	 * ======================
	 * BlockEntity / Rendering
	 * ======================
	 */

	@Override
	public RenderShape getRenderShape( BlockState state )
	{
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public BlockEntity newBlockEntity( BlockPos blockPos, BlockState blockState )
	{
		return new TileEntityLocker( blockPos, blockState );
	}

	/*
	 * @Override
	 * public boolean hasCustomBreakingProgress( BlockState state )
	 * {
	 * return true;
	 * }
	 */

	@Override
	public VoxelShape getOcclusionShape( BlockState state, BlockGetter worldIn, BlockPos pos )
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
	public BlockState getStateForPlacement( BlockPlaceContext context )
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
	public BlockState updateShape( BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos,
			BlockPos facingPos )
	{
		if( stateIn.getValue( WATERLOGGED ) )
			worldIn.scheduleTick( currentPos, Fluids.WATER, Fluids.WATER.getTickDelay( worldIn ) );

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

	private DoorHingeSide getHingeSide( BlockPlaceContext context )
	{
		final BlockPos blockPos = context.getClickedPos();
		final Direction direction = context.getHorizontalDirection();
		final int offX = direction.getStepX();
		final int offY = direction.getStepY();
		final Vec3 v = context.getClickLocation();
		final double hitX = v.x - blockPos.getX();
		final double hitY = v.z - blockPos.getZ();
		return ( offX >= 0 || !( hitY < 0.5D ) ) && ( offX <= 0 || !( hitY > 0.5D ) ) && ( offY >= 0 || !( hitX > 0.5D ) )
				&& ( offY <= 0 || !( hitX < 0.5D ) ) ? DoorHingeSide.LEFT : DoorHingeSide.RIGHT;
	}

	@Nullable
	private Direction getDirectionToAttach( BlockPlaceContext context, Direction facing )
	{
		final BlockState faceState = context.getLevel().getBlockState( context.getClickedPos().relative( facing ) );
		return faceState.getBlock() == this && faceState.getValue( TYPE ) == ConnectedType.SINGLE ? faceState.getValue( FACING ) : null;
	}

	public static Direction getDirectionToAttached( BlockState state )
	{
		return state.getValue( TYPE ) == ConnectedType.SLAVE ? Direction.DOWN : Direction.UP;
	}

	@Override
	public void setPlacedBy( Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack )
	{
		if( stack.hasCustomHoverName() )
		{
			final BlockEntity tileentity = worldIn.getBlockEntity( pos );
			if( tileentity instanceof TileEntityContainer )
				( (TileEntityContainer)tileentity ).setCustomName( stack.getDisplayName() );
		}
	}

	@SuppressWarnings( "deprecation" )
	@Override
	public void onRemove( BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving )
	{
		if( state.getBlock() != newState.getBlock() )
		{
			final BlockEntity tileentity = worldIn.getBlockEntity( pos );
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
	public int getAnalogOutputSignal( BlockState blockState, Level worldIn, BlockPos pos )
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
			return Mth.floor( f * 14.0F ) + ( i > 0 ? 1 : 0 );
		}
	}

	/*
	 * ===========
	 * Interaction
	 * ===========
	 */

	@Override
	public InteractionResult use( BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit )
	{
		if( hit.getDirection() == state.getValue( FACING ) )
			if( worldIn.isClientSide )
				return InteractionResult.SUCCESS;
			else
			{
				final MenuProvider locker = getMenuProvider( state, worldIn, pos );
				if( locker != null )
				{
					NetworkHooks.openGui( (ServerPlayer)player, locker, pos );
					player.awardStat( getOpenStat() );
				}
				return InteractionResult.SUCCESS;
			}
		return InteractionResult.PASS;
	}

	protected Stat< ResourceLocation > getOpenStat()
	{
		return Stats.CUSTOM.get( Stats.OPEN_CHEST );
	}

	@Override
	@Nullable
	public MenuProvider getMenuProvider( BlockState state, Level worldIn, BlockPos pos )
	{
		return getContainer( state, worldIn, pos, false );
	}

	public MenuProvider getContainer( BlockState state, Level worldIn, BlockPos pos, boolean allowBlockedChest )
	{
		final BlockEntity tileentity = worldIn.getBlockEntity( pos );
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

	private static boolean isBlocked( LevelAccessor world, BlockPos pos )
	{
		return isBehindSolidBlock( world, pos );
	}

	private static boolean isBehindSolidBlock( BlockGetter reader, BlockPos worldIn )
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
