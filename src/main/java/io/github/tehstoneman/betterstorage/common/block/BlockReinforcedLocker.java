package io.github.tehstoneman.betterstorage.common.block;

import javax.annotation.Nullable;

import io.github.tehstoneman.betterstorage.api.ConnectedType;
import io.github.tehstoneman.betterstorage.api.lock.ILock;
import io.github.tehstoneman.betterstorage.api.lock.LockInteraction;
import io.github.tehstoneman.betterstorage.common.enchantment.EnchantmentBetterStorage;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityContainer;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityReinforcedLocker;
import io.github.tehstoneman.betterstorage.common.world.storage.HexKeyConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;

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
	public BlockEntity newBlockEntity( BlockPos blockPos, BlockState blockState )
	{
		return new TileEntityReinforcedLocker( blockPos, blockState );
	}

	@Override
	public InteractionResult use( BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit )
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
						return InteractionResult.PASS;
					}
					if( player.isCrouching() )
					{
						worldIn.addFreshEntity( new ItemEntity( worldIn, pos.getX(), pos.getY(), pos.getZ(), tileChest.getLock().copy() ) );
						tileChest.setLock( ItemStack.EMPTY );
						return InteractionResult.SUCCESS;
					}
				}
				return super.use( state, worldIn, pos, player, hand, hit );
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	@Nullable
	public static TileEntityReinforcedLocker getLockerAt( Level world, BlockPos pos )
	{
		final BlockEntity tileEntity = world.getBlockEntity( pos );
		if( tileEntity instanceof TileEntityReinforcedLocker )
			return (TileEntityReinforcedLocker)tileEntity;
		return null;
	}

	@Override
	public BlockState updateShape( BlockState thisState, Direction facing, BlockState facingState, LevelAccessor world, BlockPos thisPos,
			BlockPos facingPos )
	{
		if( thisState.getValue( WATERLOGGED ) )
			world.scheduleTick( thisPos, Fluids.WATER, Fluids.WATER.getTickDelay( world ) );

		if( facingState.getBlock() == this && facing.getAxis().isVertical() )
		{
			final ConnectedType facingType = facingState.getValue( TYPE );

			if( thisState.getValue( TYPE ) == ConnectedType.SINGLE && facingType != ConnectedType.SINGLE
					&& thisState.getValue( FACING ) == facingState.getValue( FACING )
					&& getDirectionToAttached( facingState ) == facing.getOpposite() )
			{
				final ConnectedType newType = facingType.opposite();
				final TileEntityReinforcedLocker thisLocker = getLockerAt( (Level)world, thisPos );
				final TileEntityReinforcedLocker facingLocker = getLockerAt( (Level)world, facingPos );
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
	public void onRemove( BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving )
	{
		if( state.getBlock() != newState.getBlock() )
		{
			final BlockEntity tileentity = world.getBlockEntity( pos );
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
	public int getSignal( BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side )
	{
		final BlockEntity tileEntity = blockAccess.getBlockEntity( pos );
		if( tileEntity instanceof TileEntityReinforcedLocker )
		{
			final TileEntityReinforcedLocker chest = (TileEntityReinforcedLocker)tileEntity;
			return chest.isPowered() ? Mth.clamp( chest.getPlayersUsing(), 0, 15 ) : 0;
		}
		return 0;
	}

	@Override
	public int getDirectSignal( BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side )
	{
		return side == Direction.UP ? blockState.getSignal( blockAccess, pos, side ) : 0;
	}

	@Override
	public float getExplosionResistance( BlockState state, BlockGetter world, BlockPos pos, Explosion explosion )
	{
		final TileEntityReinforcedLocker chest = getLockerAt( (Level)world, pos );
		if( chest != null && chest.isLocked() )
		{
			final int resist = EnchantmentHelper.getItemEnchantmentLevel( EnchantmentBetterStorage.PERSISTANCE.get(), chest.getLock() ) + 1;
			return super.getExplosionResistance( state, world, pos, explosion ) * resist * 2;
		}
		return super.getExplosionResistance( state, world, pos, explosion );
	}
}
