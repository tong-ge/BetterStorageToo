package io.github.tehstoneman.betterstorage.api.lock;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

/**
 * Interface to describe a lockable object. <br>
 * Should normally be used with a {@link TileEntity} <br>
 * Defines and object that can accept an {@link ItemStack} to use as a lock.
 *
 * @author TehStoneMan
 *
 */
public interface IKeyLockable
{
	/**
	 * Returns the lock of this object.
	 *
	 * @return The current lock or empty.
	 */
	public ItemStack getLock();

	/**
	 * Check if this object is locked.
	 *
	 * @return True if a lock is present.
	 */
	default boolean isLocked()
	{
		return !getLock().isEmpty();
	}

	/**
	 * Returns if this container can be locked with this lock.
	 *
	 * @param lock
	 *            The {@link ItemStack} to check.
	 * @return True if the given {@link ItemStack} can be used to lock this object.
	 */
	default boolean isLockValid( ItemStack lock )
	{
		return !lock.isEmpty() && lock.getItem() instanceof ILock;
	}

	/**
	 * Sets the lock of this object. <br>
	 * Has no effect if canSetLock() returns false.
	 *
	 * @param lock
	 *            The {@link ItemStack} to use as a lock.
	 */
	public void setLock( ItemStack lock );

	/**
	 * Returns if this object can be used by the player without using a key, for example,
	 * while a container is being held open by another player.
	 *
	 * @param player
	 *            The {@link PlayerEntity} to check for.
	 * @return True is this {@link PlayerEntity} can use this object.
	 */
	public boolean canUse( PlayerEntity player );

	/**
	 * Apply logic for when the lock has the Trigger enchantment.
	 *
	 */
	public void applyTrigger();

	/**
	 * Try to unlock this object with the given {@link ItemStack}.
	 *
	 * @param itemStack
	 *            The {@link ItemStack} to try and unlock this object with.
	 * @return True if unlocking is successful.
	 */
	public boolean unlockWith( ItemStack itemStack );
}
