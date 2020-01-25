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
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemKey extends KeyLockItem implements IKey
{
	public ItemKey()
	{
		super( new Properties().group( BetterStorage.ITEM_GROUP ) );
	}

	@Override
	public void onCreated( ItemStack stack, World worldIn, PlayerEntity playerIn )
	{
		if( !worldIn.isRemote )
			ensureHasID( stack );
	}

	@Override
	public int getItemEnchantability()
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
	public ActionResultType onItemUse( ItemUseContext context )
	{
		final World worldIn = context.getWorld();
		final Hand hand = context.getHand();
		if( !worldIn.isRemote && hand == Hand.MAIN_HAND )
		{
			final PlayerEntity playerIn = context.getPlayer();
			final ItemStack stack = playerIn.getHeldItem( hand );
			BlockPos pos = context.getPos();
			TileEntity tileEntity = worldIn.getTileEntity( pos );
			if( tileEntity == null )
			{
				final BlockState state = worldIn.getBlockState( pos );
				if( state.getProperties().contains( DoorBlock.HALF ) && state.get( DoorBlock.HALF ) == DoubleBlockHalf.UPPER )
				{
					pos = pos.down();
					tileEntity = worldIn.getTileEntity( pos );
				}
			}

			if( tileEntity instanceof IKeyLockable )
			{
				final IKeyLockable lockable = (IKeyLockable)tileEntity;
				if( unlock( stack, lockable.getLock(), false ) )
				{
					if( playerIn.isSneaking() )
					{
						worldIn.addEntity( new ItemEntity( worldIn, pos.getX(), pos.getY(), pos.getZ(), lockable.getLock().copy() ) );
						lockable.setLock( ItemStack.EMPTY );
					}
					return ActionResultType.SUCCESS;
				}
				else
					( (ILock)lockable.getLock().getItem() ).applyEffects( lockable.getLock(), lockable, playerIn, LockInteraction.PICK );
			}
		}
		return super.onItemUse( context );
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

		final int lockSecurity = EnchantmentHelper.getEnchantmentLevel( EnchantmentBetterStorage.SECURITY, lock );
		final int unlocking = EnchantmentHelper.getEnchantmentLevel( EnchantmentBetterStorage.UNLOCKING, key );
		final int lockpicking = EnchantmentHelper.getEnchantmentLevel( EnchantmentBetterStorage.LOCKPICKING, key );
		final int morphing = EnchantmentHelper.getEnchantmentLevel( EnchantmentBetterStorage.MORPHING, key );

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
			EnchantmentBetterStorage.decEnchantment( key, EnchantmentBetterStorage.LOCKPICKING, 1 );
			return true;
		}
		if( effectiveMorphing > 0 )
		{
			setID( key, lockId );
			EnchantmentBetterStorage.decEnchantment( key, EnchantmentBetterStorage.MORPHING, morphing );
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
