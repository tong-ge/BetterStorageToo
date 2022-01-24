package io.github.tehstoneman.betterstorage.api;

import javax.annotation.Nonnull;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;

/**
 * Interface that describes the configuration of adjustable storage containers
 *
 * @author TehStoneMan
 */
public interface IHexKeyConfig extends IItemHandler, INBTSerializable< CompoundTag >
{
	/**
	 * Check if this config object is empty
	 *
	 * @return true if empty
	 */
	public boolean isEmpty();

	/**
	 * Overrides the stack in the given slot
	 *
	 * @param slot
	 *            Slot to modify
	 * @param stack
	 *            ItemStack to set slot to (may be empty).
	 **/
	public void setStackInSlot( int slot, @Nonnull ItemStack stack );
}
