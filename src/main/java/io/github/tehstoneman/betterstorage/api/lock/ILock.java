package io.github.tehstoneman.betterstorage.api.lock;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

/**
 * Interface to describe a lock
 * 
 * @author TehStoneMan
 *
 */
public interface ILock
{
	/**
	 * Returns a string describing the lock's type or how it functions. <br>
	 * Key and Lock types must match.
	 *
	 * @return The lock type.
	 */
	default String getLockType()
	{
		return "normal";
	}

	/**
	 * Gets called after a player tries to open a lock, successfully or not.
	 *
	 * @param lock
	 *            The {@link ItemStack} being unlocked.
	 * @param key
	 *            The {@link ItemStack} being used as a key.
	 * @param lockable
	 *            The {@link IKeyLockable} containing the lock.
	 * @param player
	 *            The {@link PlayerEntity} attempting to open the lock.
	 * @param success
	 *            True if the attempt is successful
	 */
	public void onUnlock( ItemStack lock, ItemStack key, IKeyLockable lockable, PlayerEntity player, boolean success );

	/**
	 * Applies any effects from the lock when an interaction fails.
	 *
	 * @param lock
	 *            The {@link ItemStack} being unlocked.
	 * @param lockable
	 *            The {@link IKeyLockable} containing the lock.
	 * @param player
	 *            The {@link PlayerEntity} attempting to open the lock.
	 * @param interaction
	 *            The type of interaction attempted - OPEN, PICK or ATTACK.
	 */
	public void applyEffects( ItemStack lock, IKeyLockable lockable, PlayerEntity player, LockInteraction interaction );

	/**
	 * Returns if the lock can be enchanted with this lock enchantment.
	 *
	 * @param lock
	 *            {@link ItemStack} to check.
	 * @param enchantment
	 *            {@link Enchantment} to test for.
	 * @return True is {@link Enchantment} can be applied to this lock.
	 */
	public boolean canApplyEnchantment( ItemStack lock, Enchantment enchantment );
}
