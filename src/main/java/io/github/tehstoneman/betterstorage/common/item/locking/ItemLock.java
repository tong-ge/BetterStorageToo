package io.github.tehstoneman.betterstorage.common.item.locking;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.api.lock.IKeyLockable;
import io.github.tehstoneman.betterstorage.api.lock.ILock;
import io.github.tehstoneman.betterstorage.api.lock.KeyLockItem;
import io.github.tehstoneman.betterstorage.api.lock.LockInteraction;
import io.github.tehstoneman.betterstorage.common.block.BetterStorageBlocks;
import io.github.tehstoneman.betterstorage.common.enchantment.EnchantmentBetterStorage;
import io.github.tehstoneman.betterstorage.common.enchantment.EnchantmentLock;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityLockableDoor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DoorBlock;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemLock extends KeyLockItem implements ILock
{
	public ItemLock()
	{
		super( new Properties().group( BetterStorage.ITEM_GROUP ) );
	}

	/*
	 * @Override
	 * public boolean getIsRepairable( ItemStack stack, ItemStack material )
	 * {
	 * return material.getItem() == Items.GOLD_INGOT;
	 * }
	 *
	 * @Override
	 * public boolean isDamageable()
	 * {
	 * return true;
	 * }
	 */

	@Override
	public void onCreated( ItemStack stack, World world, PlayerEntity player )
	{
		if( !world.isRemote )
			ensureHasID( stack );
	}

	@Override
	public ActionResultType onItemUse( ItemUseContext context )
	{
		final PlayerEntity playerIn = context.getPlayer();
		final Hand hand = context.getHand();
		final World worldIn = context.getWorld();
		BlockPos pos = context.getPos();
		final ItemStack stack = context.getItem();

		if( hand == Hand.MAIN_HAND )
		{
			BlockState blockState = worldIn.getBlockState( pos );
			Block block = blockState.getBlock();

			if( block == Blocks.IRON_DOOR )
			{
				if( blockState.get( DoorBlock.HALF ) == DoubleBlockHalf.UPPER )
				{
					pos = pos.down();
					blockState = worldIn.getBlockState( pos );
					block = blockState.getBlock();
				}

				//@formatter:off
				worldIn.setBlockState( pos, BetterStorageBlocks.LOCKABLE_DOOR.getDefaultState()
						.with( DoorBlock.FACING, blockState.get( DoorBlock.FACING ) )
						.with( DoorBlock.OPEN, blockState.get( DoorBlock.OPEN ) )
						.with( DoorBlock.HINGE, blockState.get( DoorBlock.HINGE ) )
						.with( DoorBlock.HALF, DoubleBlockHalf.LOWER ) );
				worldIn.setBlockState( pos.up(), BetterStorageBlocks.LOCKABLE_DOOR.getDefaultState()
						.with( DoorBlock.FACING, blockState.get( DoorBlock.FACING ) )
						.with( DoorBlock.OPEN, blockState.get( DoorBlock.OPEN ) )
						.with( DoorBlock.HINGE, blockState.get( DoorBlock.HINGE ) )
						.with( DoorBlock.HALF, DoubleBlockHalf.UPPER ) );
				//@formatter:on

				final TileEntity tileEntity = worldIn.getTileEntity( pos );
				if( tileEntity instanceof TileEntityLockableDoor )
				{
					final TileEntityLockableDoor lockable = (TileEntityLockableDoor)tileEntity;
					if( lockable.isLockValid( stack ) )
					{
						lockable.setLock( stack.copy() );
						if( !playerIn.isCreative() )
							playerIn.setHeldItem( hand, ItemStack.EMPTY );
						return ActionResultType.SUCCESS;
					}
				}
			}

			final TileEntity tileEntity = worldIn.getTileEntity( pos );
			if( tileEntity instanceof IKeyLockable )
			{
				final IKeyLockable lockable = (IKeyLockable)tileEntity;
				if( lockable.isLockValid( stack ) && lockable.getLock().isEmpty() )
				{
					lockable.setLock( stack.copy() );
					if( !playerIn.isCreative() )
						playerIn.setHeldItem( hand, ItemStack.EMPTY );
					return ActionResultType.SUCCESS;
				}
			}
		}
		return super.onItemUse( context );
	}

	/*
	 * =====
	 * ILock
	 * =====
	 */

	@Override
	public void onUnlock( ItemStack lock, ItemStack key, IKeyLockable lockable, PlayerEntity player, boolean success )
	{
		if( success )
			return;
		// Power is 2 when a key was used to open the lock, 1 otherwise.
		applyEffects( lock, lockable, player, key.isEmpty() ? LockInteraction.OPEN : LockInteraction.PICK );
	}

	@Override
	public void applyEffects( ItemStack lock, IKeyLockable lockable, PlayerEntity player, LockInteraction interaction )
	{
		final int shock = EnchantmentHelper.getEnchantmentLevel( EnchantmentBetterStorage.SHOCK, lock );

		if( shock > 0 )
		{
			int damage = shock;
			if( interaction == LockInteraction.PICK )
				damage *= 3;
			player.attackEntityFrom( DamageSource.MAGIC, damage );
			final World world = player.getEntityWorld();
			world.playSound( (PlayerEntity)null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_BLAZE_SHOOT,
					SoundCategory.BLOCKS, 0.5f, world.rand.nextFloat() * 0.1F + 0.9F );
			if( shock >= 3 && interaction != LockInteraction.OPEN )
				player.setFire( 3 );
		}
	}

	@Override
	public boolean canApplyEnchantment( ItemStack lock, Enchantment enchantment )
	{
		return enchantment instanceof EnchantmentLock;
	}
}
