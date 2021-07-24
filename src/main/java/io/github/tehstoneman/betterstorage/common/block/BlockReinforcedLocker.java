package io.github.tehstoneman.betterstorage.common.block;

import javax.annotation.Nullable;

import io.github.tehstoneman.betterstorage.api.ConnectedType;
import io.github.tehstoneman.betterstorage.api.lock.ILock;
import io.github.tehstoneman.betterstorage.api.lock.LockInteraction;
import io.github.tehstoneman.betterstorage.common.enchantment.EnchantmentBetterStorage;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityContainer;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityReinforcedLocker;
import io.github.tehstoneman.betterstorage.common.world.storage.HexKeyConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class BlockReinforcedLocker extends BlockLocker
{
	public BlockReinforcedLocker()
	{
		this( Block.Properties.of( Material.WOOD ).strength( 5.0F, 6.0F ).sound( SoundType.WOOD ) );
	}

	public BlockReinforcedLocker( Properties properties )
	{
		super( properties );
	}

	@Override
	public TileEntity createTileEntity( BlockState state, IBlockReader world )
	{
		return new TileEntityReinforcedLocker();
	}

	@Override
	public ActionResultType use( BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit )
	{
		if( hit.getDirection() == state.getValue( FACING ) )
		{
			if( !worldIn.isClientSide )
			{
				final TileEntityReinforcedLocker tileChest = getLockerAt( worldIn, pos );
				if( tileChest != null && tileChest.isLocked() )
				{
					if( !tileChest.unlockWith( player.getItemInHand( hand ) ) )
					{
						final ItemStack lock = tileChest.getLock();
						( (ILock)lock.getItem() ).applyEffects( lock, tileChest, player, LockInteraction.OPEN );
						return ActionResultType.PASS;
					}
					if( player.isCrouching() )
					{
						worldIn.addFreshEntity( new ItemEntity( worldIn, pos.getX(), pos.getY(), pos.getZ(), tileChest.getLock().copy() ) );
						tileChest.setLock( ItemStack.EMPTY );
						return ActionResultType.SUCCESS;
					}
				}
				return super.use( state, worldIn, pos, player, hand, hit );
			}
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}

	@Nullable
	public static TileEntityReinforcedLocker getLockerAt( World world, BlockPos pos )
	{
		final TileEntity tileEntity = world.getBlockEntity( pos );
		if( tileEntity instanceof TileEntityReinforcedLocker )
			return (TileEntityReinforcedLocker)tileEntity;
		return null;
	}

	@Override
	public BlockState updateShape( BlockState thisState, Direction facing, BlockState facingState, IWorld world, BlockPos thisPos,
			BlockPos facingPos )
	{
		if( thisState.getValue( WATERLOGGED ) )
			world.getLiquidTicks().scheduleTick( thisPos, Fluids.WATER, Fluids.WATER.getTickDelay( world ) );

		if( facingState.getBlock() == this && facing.getAxis().isVertical() )
		{
			final ConnectedType facingType = facingState.getValue( TYPE );

			if( thisState.getValue( TYPE ) == ConnectedType.SINGLE && facingType != ConnectedType.SINGLE
					&& thisState.getValue( FACING ) == facingState.getValue( FACING ) && getDirectionToAttached( facingState ) == facing.getOpposite() )
			{
				final ConnectedType newType = facingType.opposite();
				final TileEntityReinforcedLocker thisLocker = getLockerAt( (World)world, thisPos );
				final TileEntityReinforcedLocker facingLocker = getLockerAt( (World)world, facingPos );
				if( newType == ConnectedType.SLAVE )
				{
					facingLocker.setConfig( thisLocker.getConfig() );
					thisLocker.setConfig( new HexKeyConfig() );
				}
				return thisState.setValue( TYPE, newType );
			}
		}
		else if( getDirectionToAttached( thisState ) == facing )
			return thisState.setValue( TYPE, ConnectedType.SINGLE );

		return super.updateShape( thisState, facing, facingState, world, thisPos, facingPos );
	}

	@Override
	public void onRemove( BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving )
	{
		if( state.getBlock() != newState.getBlock() )
		{
			final TileEntity tileentity = world.getBlockEntity( pos );
			if( tileentity instanceof TileEntityContainer )
			{
				if( state.getValue( TYPE ) == ConnectedType.MASTER )
				{
					final TileEntityReinforcedLocker thisLocker = getLockerAt( world, pos );
					final TileEntityReinforcedLocker facingLocker = getLockerAt( world, pos.relative( getDirectionToAttached( state ) ) );

					facingLocker.setConfig( thisLocker.getConfig() );
					thisLocker.setConfig( new HexKeyConfig() );
				}
				( (TileEntityContainer)tileentity ).dropInventoryItems();
				world.updateNeighbourForOutputSignal( pos, this );
			}

			super.onRemove( state, world, pos, newState, isMoving );
		}
	}

	@Override
	public boolean isSignalSource( BlockState state )
	{
		return true;
	}

	@Override
	public int getSignal( BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side )
	{
		final TileEntity tileEntity = blockAccess.getBlockEntity( pos );
		if( tileEntity instanceof TileEntityReinforcedLocker )
		{
			final TileEntityReinforcedLocker chest = (TileEntityReinforcedLocker)tileEntity;
			return chest.isPowered() ? MathHelper.clamp( chest.getPlayersUsing(), 0, 15 ) : 0;
		}
		return 0;
	}

	@Override
	public int getDirectSignal( BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side )
	{
		return side == Direction.UP ? blockState.getSignal( blockAccess, pos, side ) : 0;
	}

	@Override
	public float getExplosionResistance( BlockState state, IBlockReader world, BlockPos pos, Explosion explosion )
	{
		final TileEntityReinforcedLocker chest = getLockerAt( (World)world, pos );
		if( chest != null && chest.isLocked() )
		{
			final int resist = EnchantmentHelper.getItemEnchantmentLevel( EnchantmentBetterStorage.PERSISTANCE.get(), chest.getLock() ) + 1;
			return super.getExplosionResistance( state, world, pos, explosion ) * resist * 2;
		}
		return super.getExplosionResistance( state, world, pos, explosion );
	}
}
