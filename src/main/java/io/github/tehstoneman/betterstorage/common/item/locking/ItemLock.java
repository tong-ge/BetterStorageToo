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
		super( new Properties().tab( BetterStorage.ITEM_GROUP ) );
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
	public void onCraftedBy( ItemStack stack, World world, PlayerEntity player )
	{
		if( !world.isClientSide )
			ensureHasID( stack );
	}

	@Override
	public ActionResultType useOn( ItemUseContext context )
	{
		final PlayerEntity playerIn = context.getPlayer();
		final Hand hand = context.getHand();
		final World worldIn = context.getLevel();
		BlockPos pos = context.getClickedPos();
		final ItemStack stack = context.getItemInHand();

		if( hand == Hand.MAIN_HAND )
		{
			BlockState blockState = worldIn.getBlockState( pos );
			Block block = blockState.getBlock();

			if( block == Blocks.IRON_DOOR )
			{
				if( blockState.getValue( DoorBlock.HALF ) == DoubleBlockHalf.UPPER )
				{
					pos = pos.below();
					blockState = worldIn.getBlockState( pos );
					block = blockState.getBlock();
				}

				//@formatter:off
				worldIn.setBlockAndUpdate( pos, BetterStorageBlocks.LOCKABLE_DOOR.get().defaultBlockState()
						.setValue( DoorBlock.FACING, blockState.getValue( DoorBlock.FACING ) )
						.setValue( DoorBlock.OPEN, blockState.getValue( DoorBlock.OPEN ) )
						.setValue( DoorBlock.HINGE, blockState.getValue( DoorBlock.HINGE ) )
						.setValue( DoorBlock.HALF, DoubleBlockHalf.LOWER ) );
				worldIn.setBlockAndUpdate( pos.above(), BetterStorageBlocks.LOCKABLE_DOOR.get().defaultBlockState()
						.setValue( DoorBlock.FACING, blockState.getValue( DoorBlock.FACING ) )
						.setValue( DoorBlock.OPEN, blockState.getValue( DoorBlock.OPEN ) )
						.setValue( DoorBlock.HINGE, blockState.getValue( DoorBlock.HINGE ) )
						.setValue( DoorBlock.HALF, DoubleBlockHalf.UPPER ) );
				//@formatter:on

				final TileEntity tileEntity = worldIn.getBlockEntity( pos );
				if( tileEntity instanceof TileEntityLockableDoor )
				{
					final TileEntityLockableDoor lockable = (TileEntityLockableDoor)tileEntity;
					if( lockable.isLockValid( stack ) )
					{
						lockable.setLock( stack.copy() );
						if( !playerIn.isCreative() )
							playerIn.setItemInHand( hand, ItemStack.EMPTY );
						return ActionResultType.SUCCESS;
					}
				}
			}

			final TileEntity tileEntity = worldIn.getBlockEntity( pos );
			if( tileEntity instanceof IKeyLockable )
			{
				final IKeyLockable lockable = (IKeyLockable)tileEntity;
				if( lockable.isLockValid( stack ) && lockable.getLock().isEmpty() )
				{
					lockable.setLock( stack.copy() );
					if( !playerIn.isCreative() )
						playerIn.setItemInHand( hand, ItemStack.EMPTY );
					return ActionResultType.SUCCESS;
				}
			}
		}
		return super.useOn( context );
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
		final int shock = EnchantmentHelper.getItemEnchantmentLevel( EnchantmentBetterStorage.SHOCK.get(), lock );

		if( shock > 0 )
		{
			int damage = shock;
			if( interaction == LockInteraction.PICK )
				damage *= 3;
			player.hurt( DamageSource.MAGIC, damage );
			final World world = player.getCommandSenderWorld();
			world.playSound( (PlayerEntity)null, player.getX(), player.getY(), player.getZ(), SoundEvents.BLAZE_SHOOT,
					SoundCategory.BLOCKS, 0.5f, world.random.nextFloat() * 0.1F + 0.9F );
			if( shock >= 3 && interaction != LockInteraction.OPEN )
				player.setSecondsOnFire( 3 );
		}
	}

	@Override
	public boolean canApplyEnchantment( ItemStack lock, Enchantment enchantment )
	{
		return enchantment instanceof EnchantmentLock;
	}
}
