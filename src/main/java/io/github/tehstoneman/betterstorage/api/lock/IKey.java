package io.github.tehstoneman.betterstorage.api.lock;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

/**
 * Interface that describes a key
 *
 * @author TehStoneMan
 *
 */
public interface IKey
{
	/**
	 * Returns if the key is a normal key, instead of a special item which has some key-like features.
	 *
	 * @return True if this is a normal key.
	 */
	default boolean isNormalKey()
	{
		return true;
	}

	/**
	 * Returns a string describing the key's type or how it functions. <br>
	 * Key and Lock types must match.
	 *
	 * @return The lock type.
	 */
	default String getKeyType()
	{
		return "normal";
	}

	/**
	 * Gets called when a key is used to open a lock and returns if it's successful. <br>
	 * If useAbility is true, the key will use up an ability, like lockpicking or morphing.
	 *
	 * @param key
	 *            The {@link ItemStack} to use as a key.
	 * @param lock
	 *            The {@link ItemStack} that represents the lock to be opened.
	 * @param useAbility
	 *            True to use any enchantment or ability present on the key.
	 * @return Success or failure.
	 */
	public boolean unlock( ItemStack key, ItemStack lock, boolean useAbility );

	/**
	 * Returns if the key can be enchanted with this key enchantment.
	 *
	 * @param key
	 *            {@link ItemStack} to check.
	 * @param enchantment
	 *            {@link Enchantment} to test for.
	 * @return True is {@link Enchantment} can be applied to this key.
	 */
	public boolean canApplyEnchantment( ItemStack key, Enchantment enchantment );
}
