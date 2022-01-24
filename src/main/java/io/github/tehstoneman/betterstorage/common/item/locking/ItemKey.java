package io.github.tehstoneman.betterstorage.common.item.locking;

import java.util.UUID;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.api.lock.IKey;
import io.github.tehstoneman.betterstorage.api.lock.IKeyLockable;
import io.github.tehstoneman.betterstorage.api.lock.ILock;
import io.github.tehstoneman.betterstorage.api.lock.KeyLockItem;
import io.github.tehstoneman.betterstorage.api.lock.LockInteraction;
import io.github.tehstoneman.betterstorage.common.enchantment.EnchantmentBetterStorage;
import io.github.tehstoneman.betterstorage.common.enchantment.EnchantmentKey;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ItemKey extends KeyLockItem implements IKey
{
	public ItemKey()
	{
		super( new Properties().tab( BetterStorage.ITEM_GROUP ) );
	}

	@Override
	public void onCraftedBy( ItemStack stack, Level worldIn, Player playerIn )
	{
		if( !worldIn.isClientSide )
			ensureHasID( stack );
	}

	@Override
	public int getEnchantmentValue()
	{
		return 20;
	}

	@Override
	public boolean isEnchantable( ItemStack stack )
	{
		return true;
	}

	@Override
	public ItemStack getContainerItem( ItemStack stack )
	{
		return stack;
	}

	@Override
	public InteractionResult useOn( UseOnContext context )
	{
		final Level worldIn = context.getLevel();
		final InteractionHand hand = context.getHand();
		if( !worldIn.isClientSide && hand == InteractionHand.MAIN_HAND )
		{
			final Player playerIn = context.getPlayer();
			final ItemStack stack = playerIn.getItemInHand( hand );
			final BlockPos pos = context.getClickedPos();
			final BlockEntity tileEntity = worldIn.getBlockEntity( pos );
			/*
			 * if( tileEntity == null )
			 * {
			 * final BlockState state = worldIn.getBlockState( pos );
			 * if( state.getProperties().contains( DoorBlock.HALF ) && state.get( DoorBlock.HALF ) == DoubleBlockHalf.UPPER )
			 * {
			 * pos = pos.down();
			 * tileEntity = worldIn.getBlockEntity( pos );
			 * }
			 * }
			 */

			if( tileEntity instanceof IKeyLockable )
			{
				final IKeyLockable lockable = (IKeyLockable)tileEntity;
				if( unlock( stack, lockable.getLock(), false ) )
				{
					if( playerIn.isCrouching() )
					{
						worldIn.addFreshEntity( new ItemEntity( worldIn, pos.getX(), pos.getY(), pos.getZ(), lockable.getLock().copy() ) );
						lockable.setLock( ItemStack.EMPTY );
					}
					return InteractionResult.SUCCESS;
				}
				else
					( (ILock)lockable.getLock().getItem() ).applyEffects( lockable.getLock(), lockable, playerIn, LockInteraction.PICK );
			}
		}
		return super.useOn( context );
	}

	/*
	 * ====
	 * IKey
	 * ====
	 */

	@Override
	public boolean unlock( ItemStack key, ItemStack lock, boolean useAbility )
	{

		final ILock lockType = (ILock)lock.getItem();
		// If the lock type isn't normal, the key can't unlock it.
		if( lockType.getLockType() != "normal" )
			return false;

		final UUID lockId = getID( lock );
		final UUID keyId = getID( key );

		// If the lock and key IDs match, return true.
		if( lockId.equals( keyId ) )
			return true;

		final int lockSecurity = EnchantmentHelper.getItemEnchantmentLevel( EnchantmentBetterStorage.SECURITY.get(), lock );
		final int unlocking = EnchantmentHelper.getItemEnchantmentLevel( EnchantmentBetterStorage.UNLOCKING.get(), key );
		final int lockpicking = EnchantmentHelper.getItemEnchantmentLevel( EnchantmentBetterStorage.LOCKPICKING.get(), key );
		final int morphing = EnchantmentHelper.getItemEnchantmentLevel( EnchantmentBetterStorage.MORPHING.get(), key );

		final int effectiveUnlocking = Math.max( 0, unlocking - lockSecurity );
		final int effectiveLockpicking = Math.max( 0, lockpicking - lockSecurity );
		final int effectiveMorphing = Math.max( 0, morphing - lockSecurity );

		if( effectiveUnlocking > 0 )
		{
			final int roll = BetterStorage.RANDOM.nextInt( 5 );
			if( effectiveUnlocking > roll )
				return true;
		}
		if( effectiveLockpicking > 0 )
		{
			EnchantmentBetterStorage.decEnchantment( key, EnchantmentBetterStorage.LOCKPICKING.get(), 1 );
			return true;
		}
		if( effectiveMorphing > 0 )
		{
			setID( key, lockId );
			EnchantmentBetterStorage.decEnchantment( key, EnchantmentBetterStorage.MORPHING.get(), morphing );
			return true;
		}

		return false;
	}

	@Override
	public boolean canApplyEnchantment( ItemStack key, Enchantment enchantment )
	{
		return enchantment instanceof EnchantmentKey;
	}
}
