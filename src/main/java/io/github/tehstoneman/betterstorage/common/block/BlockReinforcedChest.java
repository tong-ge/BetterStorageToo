package io.github.tehstoneman.betterstorage.common.block;

import java.util.List;

import javax.annotation.Nullable;

import io.github.tehstoneman.betterstorage.api.ConnectedType;
import io.github.tehstoneman.betterstorage.api.lock.ILock;
import io.github.tehstoneman.betterstorage.api.lock.LockInteraction;
import io.github.tehstoneman.betterstorage.common.enchantment.EnchantmentBetterStorage;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityContainer;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityReinforcedChest;
import io.github.tehstoneman.betterstorage.common.world.storage.HexKeyConfig;
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
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
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
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkHooks;

public class BlockReinforcedChest extends BlockConnectableContainer implements SimpleWaterloggedBlock
{
	public static final BooleanProperty		WATERLOGGED		= BlockStateProperties.WATERLOGGED;
	protected static final VoxelShape		SHAPE_NORTH		= Block.box( 1.0D, 0.0D, 0.0D, 15.0D, 14.0D, 15.0D );
	protected static final VoxelShape		SHAPE_SOUTH		= Block.box( 1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 16.0D );
	protected static final VoxelShape		SHAPE_WEST		= Block.box( 0.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D );
	protected static final VoxelShape		SHAPE_EAST		= Block.box( 1.0D, 0.0D, 1.0D, 16.0D, 14.0D, 15.0D );
	protected static final VoxelShape		SHAPE_SINGLE	= Block.box( 1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D );
	public static final DirectionProperty	FACING			= BlockStateProperties.FACING;

	public BlockReinforcedChest()
	{
		this( Block.Properties.of( Material.WOOD ).strength( 5.0F, 6.0F ).sound( SoundType.WOOD ) );
	}

	public BlockReinforcedChest( Properties properties )
	{
		super( properties );

		//@formatter:off
		registerDefaultState( defaultBlockState().setValue( FACING, Direction.NORTH )
												 .setValue( WATERLOGGED, Boolean.valueOf( false ) ) );
		//@formatter:on
	}

	@Override
	protected void createBlockStateDefinition( StateDefinition.Builder< Block, BlockState > builder )
	{
		super.createBlockStateDefinition( builder );
		builder.add( FACING, WATERLOGGED );
	}

	/*
	 * ======================
	 * BlockEntity / Rendering
	 * ======================
	 */

	@Override
	public RenderShape getRenderShape( BlockState state )
	{
		// return RenderShape.MODEL;
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public BlockEntity newBlockEntity( BlockPos blockPos, BlockState blockState )
	{
		return new TileEntityReinforcedChest( blockPos, blockState );
	}

	/*
	 * =========
	 * Placement
	 * =========
	 */

	/*
	 * @Override
	 * public VoxelShape getShape( BlockState state, BlockGetter worldIn, BlockPos pos, LevelAccessor context )
	 * {
	 * if( state.getValue( TYPE ) == ConnectedType.SINGLE )
	 * return SHAPE_SINGLE;
	 * switch( getDirectionToAttached( state ) )
	 * {
	 * case NORTH:
	 * default:
	 * return SHAPE_NORTH;
	 * case SOUTH:
	 * return SHAPE_SOUTH;
	 * case WEST:
	 * return SHAPE_WEST;
	 * case EAST:
	 * return SHAPE_EAST;
	 * }
	 * }
	 */

	@Override
	public BlockState getStateForPlacement( BlockPlaceContext context )
	{
		ConnectedType connectedType = ConnectedType.SINGLE;
		Direction direction = context.getHorizontalDirection().getOpposite();
		final FluidState fluidState = context.getLevel().getFluidState( context.getClickedPos() );
		final boolean sneaking = context.getPlayer().isShiftKeyDown();

		final Direction direction1 = context.getClickedFace();
		if( direction1.getAxis().isHorizontal() && sneaking )
		{
			final Direction direction2 = getDirectionToAttach( context, direction1.getOpposite() );
			if( direction2 != null && direction2.getAxis() != direction1.getAxis() )
			{
				direction = direction2;
				connectedType = direction2.getCounterClockWise() == direction1.getOpposite() ? ConnectedType.MASTER : ConnectedType.SLAVE;
			}
		}

		if( connectedType == ConnectedType.SINGLE && !sneaking )
			if( direction == getDirectionToAttach( context, direction.getClockWise() ) )
				connectedType = ConnectedType.SLAVE;
			else if( direction == getDirectionToAttach( context, direction.getCounterClockWise() ) )
				connectedType = ConnectedType.MASTER;

		return defaultBlockState().setValue( FACING, direction ).setValue( TYPE, connectedType ).setValue( WATERLOGGED,
				Boolean.valueOf( fluidState.getType() == Fluids.WATER ) );
	}

	@SuppressWarnings( "deprecation" )
	@Override
	public BlockState updateShape( BlockState thisState, Direction facing, BlockState facingState, LevelAccessor world, BlockPos thisPos,
			BlockPos facingPos )
	{
		if( thisState.getValue( WATERLOGGED ) )
			world.scheduleTick( thisPos, Fluids.WATER, Fluids.WATER.getTickDelay( world ) );

		if( facingState.getBlock() == this && facing.getAxis().isHorizontal() )
		{
			final ConnectedType facingType = facingState.getValue( TYPE );

			if( thisState.getValue( TYPE ) == ConnectedType.SINGLE && facingType != ConnectedType.SINGLE
					&& thisState.getValue( FACING ) == facingState.getValue( FACING )
					&& getDirectionToAttached( facingState ) == facing.getOpposite() )
			{
				final ConnectedType newType = facingType.opposite();
				final TileEntityReinforcedChest thisChest = getChestAt( (Level)world, thisPos );
				final TileEntityReinforcedChest facingChest = getChestAt( (Level)world, facingPos );
				if( newType == ConnectedType.SLAVE )
				{
					facingChest.setConfig( thisChest.getConfig() );
					thisChest.setConfig( new HexKeyConfig() );
				}
				return thisState.setValue( TYPE, newType );
			}
		}
		else if( getDirectionToAttached( thisState ) == facing )
			return thisState.setValue( TYPE, ConnectedType.SINGLE );

		return super.updateShape( thisState, facing, facingState, world, thisPos, facingPos );
	}

	/**
	 * Returns facing pointing to a chest to form a double chest with, null otherwise
	 *
	 * @param context
	 *            Context
	 * @param facing
	 *            The offset direction
	 * @return Facing direction
	 */
	@Nullable
	private Direction getDirectionToAttach( BlockPlaceContext context, Direction facing )
	{
		final BlockState blockState = context.getLevel().getBlockState( context.getClickedPos().relative( facing ) );
		return blockState.getBlock() == this && blockState.getValue( TYPE ) == ConnectedType.SINGLE ? blockState.getValue( FACING ) : null;
	}

	/**
	 * Returns a facing pointing from the given state to its attached double chest
	 *
	 * @param state
	 *            The BlockState of the block to attach to
	 * @return The direction of the attached block
	 */
	public static Direction getDirectionToAttached( BlockState state )
	{
		final Direction direction = state.getValue( FACING );
		return state.getValue( TYPE ) == ConnectedType.SLAVE ? direction.getClockWise() : direction.getCounterClockWise();
	}

	@Override
	public void setPlacedBy( Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack )
	{
		if( stack.hasCustomHoverName() )
		{
			final BlockEntity tileentity = worldIn.getBlockEntity( pos );
			if( tileentity instanceof TileEntityReinforcedChest )
				( (TileEntityReinforcedChest)tileentity ).setCustomName( stack.getDisplayName() );
		}
	}

	@SuppressWarnings( "deprecation" )
	@Override
	public void onRemove( BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving )
	{
		if( state.getBlock() != newState.getBlock() )
		{
			final BlockEntity tileentity = world.getBlockEntity( pos );
			if( tileentity instanceof TileEntityContainer )
			{
				if( state.getValue( TYPE ) == ConnectedType.MASTER )
				{
					final TileEntityReinforcedChest thisChest = getChestAt( world, pos );
					final TileEntityReinforcedChest facingChest = getChestAt( world, pos.relative( getDirectionToAttached( state ) ) );

					facingChest.setConfig( thisChest.getConfig() );
					thisChest.setConfig( new HexKeyConfig() );
				}
				( (TileEntityContainer)tileentity ).dropInventoryItems();
				world.updateNeighbourForOutputSignal( pos, this );
			}

			super.onRemove( state, world, pos, newState, isMoving );
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
				( (TileEntityReinforcedChest)worldIn.getBlockEntity( pos ) ).getCapability( CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ) );
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

	@Override
	public boolean isSignalSource( BlockState state )
	{
		return true;
	}

	@Override
	public int getSignal( BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side )
	{
		final BlockEntity tileEntity = blockAccess.getBlockEntity( pos );
		if( tileEntity instanceof TileEntityReinforcedChest )
		{
			final TileEntityReinforcedChest chest = (TileEntityReinforcedChest)tileEntity;
			return chest.isPowered() ? Mth.clamp( chest.getPlayersUsing(), 0, 15 ) : 0;
		}
		return 0;
	}

	@Override
	public int getDirectSignal( BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side )
	{
		return side == Direction.UP ? blockState.getSignal( blockAccess, pos, side ) : 0;
	}

	/*
	 * ===========
	 * Interaction
	 * ===========
	 */

	@Override
	public InteractionResult use( BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit )
	{
		if( worldIn.isClientSide )
			return InteractionResult.SUCCESS;
		else
		{
			final TileEntityReinforcedChest tileChest = getChestAt( worldIn, pos );
			if( tileChest != null && tileChest.isLocked() )
			{
				if( !tileChest.unlockWith( player.getItemInHand( hand ) ) )
				{
					final ItemStack lock = tileChest.getLock();
					( (ILock)lock.getItem() ).applyEffects( lock, tileChest, player, LockInteraction.OPEN );
					return InteractionResult.PASS;
				}
				if( player.isCrouching() )
				{
					worldIn.addFreshEntity( new ItemEntity( worldIn, pos.getX(), pos.getY(), pos.getZ(), tileChest.getLock().copy() ) );
					tileChest.setLock( ItemStack.EMPTY );
					return InteractionResult.SUCCESS;
				}
			}
			final MenuProvider chest = getMenuProvider( state, worldIn, pos );
			if( chest != null )
			{
				NetworkHooks.openGui( (ServerPlayer)player, chest, pos );
				player.awardStat( getOpenStat() );
			}

			return InteractionResult.SUCCESS;
		}
	}

	@Nullable
	public static TileEntityReinforcedChest getChestAt( Level world, BlockPos pos )
	{
		final BlockEntity tileEntity = world.getBlockEntity( pos );
		if( tileEntity instanceof TileEntityReinforcedChest )
			return (TileEntityReinforcedChest)tileEntity;
		return null;
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
	 * @return The chest at the position, or null if none.
	 */
	@Nullable
	public TileEntityReinforcedChest getContainer( BlockState state, Level worldIn, BlockPos pos, boolean allowBlockedChest )
	{
		final BlockEntity tileentity = worldIn.getBlockEntity( pos );
		if( !( tileentity instanceof TileEntityReinforcedChest ) )
			return null;
		else if( !allowBlockedChest && isBlocked( worldIn, pos ) )
			return null;
		else
		{
			final TileEntityReinforcedChest ilockablecontainer = (TileEntityReinforcedChest)tileentity;
			final ConnectedType chesttype = state.getValue( TYPE );
			if( chesttype == ConnectedType.SINGLE )
				return ilockablecontainer;
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

				return ilockablecontainer;
			}
		}
	}

	private static boolean isBlocked( LevelAccessor world, BlockPos pos )
	{
		return isBelowSolidBlock( world, pos ) || isCatSittingOn( world, pos );
	}

	private static boolean isBelowSolidBlock( BlockGetter reader, BlockPos worldIn )
	{
		final BlockPos blockpos = worldIn.above();
		return reader.getBlockState( blockpos ).isRedstoneConductor( reader, blockpos );
	}

	private static boolean isCatSittingOn( LevelAccessor world, BlockPos pos )
	{
		final List< Cat > list = world.getEntitiesOfClass( Cat.class,
				new AABB( pos.getX(), pos.getY() + 1, pos.getZ(), pos.getX() + 1, pos.getY() + 2, pos.getZ() + 1 ) );
		if( !list.isEmpty() )
			for( final Cat catentity : list )
				if( catentity.isInSittingPose() )
					return true;

		return false;
	}

	/*
	 * =====
	 * Fluid
	 * =====
	 */

	@SuppressWarnings( "deprecation" )
	@Override
	public FluidState getFluidState( BlockState blockState )
	{
		return blockState.getValue( WATERLOGGED ) ? Fluids.WATER.getSource( false ) : super.getFluidState( blockState );

	}

	@Override
	public float getExplosionResistance( BlockState state, BlockGetter world, BlockPos pos, Explosion explosion )
	{
		final TileEntityReinforcedChest chest = getChestAt( (Level)world, pos );
		if( chest != null && chest.isLocked() )
		{
			final int resist = EnchantmentHelper.getItemEnchantmentLevel( EnchantmentBetterStorage.PERSISTANCE.get(), chest.getLock() ) + 1;
			return super.getExplosionResistance( state, world, pos, explosion ) * resist * 2;
		}
		return super.getExplosionResistance( state, world, pos, explosion );
	}
}
