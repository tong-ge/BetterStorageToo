package io.github.tehstoneman.betterstorage.common.block;

import javax.annotation.Nullable;

import io.github.tehstoneman.betterstorage.api.lock.EnumLockInteraction;
import io.github.tehstoneman.betterstorage.api.lock.ILock;
import io.github.tehstoneman.betterstorage.common.enchantment.EnchantmentBetterStorage;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityLockableDoor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DoorBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.DoorHingeSide;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public class BlockLockableDoor extends Block
{
	protected static final VoxelShape	SOUTH_AABB	= Block.makeCuboidShape( 0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 3.0D );
	protected static final VoxelShape	NORTH_AABB	= Block.makeCuboidShape( 0.0D, 0.0D, 13.0D, 16.0D, 16.0D, 16.0D );
	protected static final VoxelShape	WEST_AABB	= Block.makeCuboidShape( 13.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D );
	protected static final VoxelShape	EAST_AABB	= Block.makeCuboidShape( 0.0D, 0.0D, 0.0D, 3.0D, 16.0D, 16.0D );

	public BlockLockableDoor()
	{
		super( Properties.from( Blocks.IRON_DOOR ) );

		//@formatter:off
		setDefaultState( stateContainer.getBaseState().with( DoorBlock.FACING, Direction.NORTH )
													  .with( DoorBlock.OPEN, Boolean.valueOf( false ) )
													  .with( DoorBlock.HINGE, DoorHingeSide.LEFT )
													  .with( DoorBlock.HALF, DoubleBlockHalf.LOWER ) );
		//@formatter:on
	}

	@Override
	public VoxelShape getShape( BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context )
	{
		final Direction direction = state.get( DoorBlock.FACING );
		final boolean open = !state.get( DoorBlock.OPEN );
		final boolean hinge = state.get( DoorBlock.HINGE ) == DoorHingeSide.RIGHT;
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
	public boolean onBlockActivated( BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit )
	{
		if( worldIn.isRemote )
			return true;
		else
		{
			if( state.get( DoorBlock.HALF ) == DoubleBlockHalf.UPPER )
				pos = pos.down();
			final TileEntity tileentity = worldIn.getTileEntity( pos );
			if( tileentity instanceof TileEntityLockableDoor )
			{
				final TileEntityLockableDoor tileDoor = (TileEntityLockableDoor)tileentity;
				if( !tileDoor.unlockWith( player.getHeldItem( handIn ) ) )
				{
					final ItemStack lock = tileDoor.getLock();
					( (ILock)lock.getItem() ).applyEffects( lock, tileDoor, player, EnumLockInteraction.OPEN );
					return false;
				}
				if( player.isSneaking() )
				{
					worldIn.addEntity( new ItemEntity( worldIn, pos.getX(), pos.getY(), pos.getZ(), tileDoor.getLock().copy() ) );
					tileDoor.setLock( ItemStack.EMPTY );
					return true;
				}
				state = state.cycle( DoorBlock.OPEN );
				worldIn.setBlockState( pos, worldIn.getBlockState( pos ).cycle( DoorBlock.OPEN ), 11 );
				worldIn.setBlockState( pos.up(), worldIn.getBlockState( pos.up() ).cycle( DoorBlock.OPEN ), 11 );
				worldIn.playEvent( player, state.get( DoorBlock.OPEN ) ? getOpenSound() : getCloseSound(), pos, 0 );
			}
		}
		return false;
	}

	@Override
	public ItemStack getPickBlock( BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player )
	{
		return new ItemStack( Items.IRON_DOOR );
	}

	@Override
	public void harvestBlock( World worldIn, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack stack )
	{
		super.harvestBlock( worldIn, player, pos, Blocks.AIR.getDefaultState(), te, stack );
	}

	@Override
	public void onBlockHarvested( World worldIn, BlockPos pos, BlockState state, PlayerEntity player )
	{
		final DoubleBlockHalf doubleblockhalf = state.get( DoorBlock.HALF );
		final BlockPos blockpos = doubleblockhalf == DoubleBlockHalf.LOWER ? pos.up() : pos.down();
		final BlockState blockstate = worldIn.getBlockState( blockpos );
		if( blockstate.getBlock() == this && blockstate.get( DoorBlock.HALF ) != doubleblockhalf )
		{
			worldIn.setBlockState( blockpos, Blocks.AIR.getDefaultState(), 35 );
			worldIn.playEvent( player, 2001, blockpos, Block.getStateId( blockstate ) );
			final ItemStack itemstack = player.getHeldItemMainhand();
			if( !worldIn.isRemote && !player.isCreative() )
			{
				Block.spawnDrops( state, worldIn, pos, (TileEntity)null, player, itemstack );
				Block.spawnDrops( blockstate, worldIn, blockpos, (TileEntity)null, player, itemstack );
			}
		}
	}

	@Override
	public boolean allowsMovement( BlockState state, IBlockReader worldIn, BlockPos pos, PathType type )
	{
		switch( type )
		{
		case LAND:
			return state.get( DoorBlock.OPEN );
		case WATER:
			return false;
		case AIR:
			return state.get( DoorBlock.OPEN );
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
	protected void fillStateContainer( StateContainer.Builder< Block, BlockState > builder )
	{
		builder.add( DoorBlock.HALF, DoorBlock.FACING, DoorBlock.OPEN, DoorBlock.HINGE );
	}

	@Override
	public float getExplosionResistance( BlockState state, IWorldReader world, BlockPos pos, @Nullable Entity exploder, Explosion explosion )
	{
		final TileEntity tileEntity = world.getTileEntity( pos );
		if( tileEntity instanceof TileEntityLockableDoor )
		{
			final TileEntityLockableDoor door = (TileEntityLockableDoor)tileEntity;
			if( door.isLocked() )
			{
				final int resist = EnchantmentHelper.getEnchantmentLevel( EnchantmentBetterStorage.PERSISTANCE, door.getLock() ) + 1;
				return super.getExplosionResistance( state, world, pos, exploder, explosion ) * resist * 2;
			}
		}
		return super.getExplosionResistance( state, world, pos, exploder, explosion );
	}

	@Override
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public boolean canProvidePower( BlockState state )
	{
		return true;
	}

	@Override
	public int getWeakPower( BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side )
	{
		final TileEntity tileEntity = blockAccess.getTileEntity( pos );
		if( tileEntity instanceof TileEntityLockableDoor )
		{
			final TileEntityLockableDoor chest = (TileEntityLockableDoor)tileEntity;
			return chest.isPowered() && blockState.get( DoorBlock.OPEN ) ? 15 : 0;
		}
		return 0;
	}

	@Override
	public int getStrongPower( BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side )
	{
		return side == Direction.UP ? blockState.getWeakPower( blockAccess, pos, side ) : 0;
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
