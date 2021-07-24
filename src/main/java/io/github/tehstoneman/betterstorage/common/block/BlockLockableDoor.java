package io.github.tehstoneman.betterstorage.common.block;

import javax.annotation.Nullable;

import io.github.tehstoneman.betterstorage.api.lock.ILock;
import io.github.tehstoneman.betterstorage.api.lock.LockInteraction;
import io.github.tehstoneman.betterstorage.common.enchantment.EnchantmentBetterStorage;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityLockableDoor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DoorBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.DoorHingeSide;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockLockableDoor extends Block
{
	protected static final VoxelShape	SOUTH_AABB	= Block.box( 0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 3.0D );
	protected static final VoxelShape	NORTH_AABB	= Block.box( 0.0D, 0.0D, 13.0D, 16.0D, 16.0D, 16.0D );
	protected static final VoxelShape	WEST_AABB	= Block.box( 13.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D );
	protected static final VoxelShape	EAST_AABB	= Block.box( 0.0D, 0.0D, 0.0D, 3.0D, 16.0D, 16.0D );

	public BlockLockableDoor()
	{
		super( Properties.copy( Blocks.IRON_DOOR ) );

		//@formatter:off
		registerDefaultState( defaultBlockState().getBlockState().setValue( DoorBlock.FACING, Direction.NORTH )
													  			 .setValue( DoorBlock.OPEN, Boolean.valueOf( false ) )
													  			 .setValue( DoorBlock.HINGE, DoorHingeSide.LEFT )
													  			 .setValue( DoorBlock.HALF, DoubleBlockHalf.LOWER ) );
		//@formatter:on
	}

	@Override
	public VoxelShape getShape( BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context )
	{
		final Direction direction = state.getValue( DoorBlock.FACING );
		final boolean open = !state.getValue( DoorBlock.OPEN );
		final boolean hinge = state.getValue( DoorBlock.HINGE ) == DoorHingeSide.RIGHT;
		switch( direction )
		{
		case EAST:
		default:
			return open ? EAST_AABB : hinge ? NORTH_AABB : SOUTH_AABB;
		case SOUTH:
			return open ? SOUTH_AABB : hinge ? EAST_AABB : WEST_AABB;
		case WEST:
			return open ? WEST_AABB : hinge ? SOUTH_AABB : NORTH_AABB;
		case NORTH:
			return open ? NORTH_AABB : hinge ? WEST_AABB : EAST_AABB;
		}
	}

	@Override
	public ActionResultType use( BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn,
			BlockRayTraceResult hit )
	{
		if( worldIn.isClientSide() )
			return ActionResultType.SUCCESS;
		else
		{
			if( state.getValue( DoorBlock.HALF ) == DoubleBlockHalf.UPPER )
				pos = pos.below();
			final TileEntity tileentity = worldIn.getBlockEntity( pos );
			if( tileentity instanceof TileEntityLockableDoor )
			{
				final TileEntityLockableDoor tileDoor = (TileEntityLockableDoor)tileentity;
				if( !tileDoor.unlockWith( player.getItemInHand(  handIn ) ) )
				{
					final ItemStack lock = tileDoor.getLock();
					( (ILock)lock.getItem() ).applyEffects( lock, tileDoor, player, LockInteraction.OPEN );
					return ActionResultType.PASS;
				}
				if( player.isCrouching() )
				{
					worldIn.addFreshEntity( new ItemEntity( worldIn, pos.getX(), pos.getY(), pos.getZ(), tileDoor.getLock().copy() ) );
					tileDoor.setLock( ItemStack.EMPTY );
					return ActionResultType.SUCCESS;
				}
				state = state.cycle( DoorBlock.OPEN );
				// state = state.cycle( DoorBlock.OPEN );
				worldIn.setBlock( pos, worldIn.getBlockState( pos ).cycle( DoorBlock.OPEN ), 11 );
				worldIn.setBlock( pos.above(), worldIn.getBlockState( pos.above() ).cycle( DoorBlock.OPEN ), 11 );
				worldIn.levelEvent( player, state.getValue( DoorBlock.OPEN ) ? getOpenSound() : getCloseSound(), pos, 0 );
			}
		}
		return ActionResultType.PASS;
	}

	@Override
	public ItemStack getPickBlock( BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player )
	{
		return new ItemStack( Items.IRON_DOOR );
	}

	@Override
	public void playerDestroy( World worldIn, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack stack )
	{
		super.playerDestroy( worldIn, player, pos, Blocks.AIR.defaultBlockState(), te, stack );
	}

	@Override
	public void playerWillDestroy( World worldIn, BlockPos pos, BlockState state, PlayerEntity player )
	{
		final DoubleBlockHalf doubleblockhalf = state.getValue( DoorBlock.HALF );
		final BlockPos blockpos = doubleblockhalf == DoubleBlockHalf.LOWER ? pos.above() : pos.below();
		final BlockState blockstate = worldIn.getBlockState( blockpos );
		if( blockstate.getBlock() == this && blockstate.getValue( DoorBlock.HALF ) != doubleblockhalf )
		{
			worldIn.setBlock( blockpos, Blocks.AIR.defaultBlockState(), 35 );
			worldIn.levelEvent( player, 2001, blockpos, Block.getId( blockstate ) );
			final ItemStack itemstack = player.getMainHandItem();
			if( !worldIn.isClientSide && !player.isCreative() )
			{
				Block.dropResources( state, worldIn, pos, (TileEntity)null, player, itemstack );
				Block.dropResources( blockstate, worldIn, blockpos, (TileEntity)null, player, itemstack );
			}
		}
	}

	@Override
	public boolean isPathfindable( BlockState state, IBlockReader worldIn, BlockPos pos, PathType type )
	{
		switch( type )
		{
		case LAND:
			return state.getValue( DoorBlock.OPEN );
		case WATER:
			return false;
		case AIR:
			return state.getValue( DoorBlock.OPEN );
		default:
			return false;
		}
	}

	public static int getCloseSound()
	{
		return 1011;
	}

	public static int getOpenSound()
	{
		return 1005;
	}

	@Override
	protected void createBlockStateDefinition( StateContainer.Builder< Block, BlockState > builder )
	{
		builder.add( DoorBlock.HALF, DoorBlock.FACING, DoorBlock.OPEN, DoorBlock.HINGE );
	}

	@Override
	public float getExplosionResistance( BlockState state, IBlockReader world, BlockPos pos, Explosion explosion )
	{
		final TileEntity tileEntity = world.getBlockEntity( pos );
		if( tileEntity instanceof TileEntityLockableDoor )
		{
			final TileEntityLockableDoor door = (TileEntityLockableDoor)tileEntity;
			if( door.isLocked() )
			{
				final int resist = EnchantmentHelper.getItemEnchantmentLevel( EnchantmentBetterStorage.PERSISTANCE.get(), door.getLock() ) + 1;
				return super.getExplosionResistance( state, world, pos, explosion ) * resist * 2;
			}
		}
		return super.getExplosionResistance( state, world, pos, explosion );
	}

	/*
	 * @Override
	 * public BlockRenderLayer getRenderLayer()
	 * {
	 * return BlockRenderLayer.CUTOUT;
	 * }
	 */

	@Override
	public boolean isSignalSource( BlockState state )
	{
		return true;
	}

	@Override
	public int getSignal( BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side )
	{
		final TileEntity tileEntity = blockAccess.getBlockEntity( pos );
		if( tileEntity instanceof TileEntityLockableDoor )
		{
			final TileEntityLockableDoor chest = (TileEntityLockableDoor)tileEntity;
			return chest.isPowered() && blockState.getValue( DoorBlock.OPEN ) ? 15 : 0;
		}
		return 0;
	}

	@Override
	public int getDirectSignal( BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side )
	{
		return side == Direction.UP ? blockState.getSignal( blockAccess, pos, side ) : 0;
	}

	@Override
	public TileEntity createTileEntity( BlockState state, IBlockReader world )
	{
		return new TileEntityLockableDoor();
	}

	@Override
	public boolean hasTileEntity( BlockState state )
	{
		return true;
	}
}
