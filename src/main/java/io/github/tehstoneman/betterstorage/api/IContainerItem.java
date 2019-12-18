package io.github.tehstoneman.betterstorage.api;

import javax.annotation.Nullable;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * When implemented for an {@link Item), signalizes that it can store or is storing other items. This is useful for other container items to keep themselves
 * from accepting them, resulting in infinite storage.
 *
 * @author TehStoneMan
 *
 */
public interface IContainerItem
{
	/**
	 * Returns the contents of this container item. <br>
	 * May return null if not supported.
	 *
	 * @param itemStack
	 *            The {@link ItemStack} to check.
	 * @return a list of {@link ItemStack}s that represents the contents.
	 */
	@Nullable
	public ItemStack[] getContainerItemContents( ItemStack itemStack );

	/**
	 * Returns if this {@link Item} can be stored in another container item.
	 *
	 * @param itemStack
	 *            The {@link ItemStack} to check.
	 * @return True if this {@link Item} can be stored in the {@link ItemStack}.
	 */
	public boolean canBeStoredInContainerItem( ItemStack itemStack );
}
