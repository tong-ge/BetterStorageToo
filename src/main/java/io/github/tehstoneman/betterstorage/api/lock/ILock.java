package io.github.tehstoneman.betterstorage.api.lock;

import io.github.tehstoneman.betterstorage.api.tileentity.IKeyLoackable;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface ILock
{
	/**
	 * Returns a string describing the lock's type or how it functions. <br>
	 * Currently there is only "normal" locks, which work with damage.
	 */
	public String getLockType();

	/** Gets called after a player tries to open a lock, successfully or not. */
	public void onUnlock( ItemStack lock, ItemStack key, IKeyLoackable lockable, PlayerEntity player, boolean success );

	/** Applies any effects from the lock when an interaction fails. */
	public void applyEffects( ItemStack lock, IKeyLockable lockable, PlayerEntity player, EnumLockInteraction interaction );

	/** Returns if the lock can be enchanted with this lock enchantment. */
	public boolean canApplyEnchantment( ItemStack lock, Enchantment enchantment );
}
