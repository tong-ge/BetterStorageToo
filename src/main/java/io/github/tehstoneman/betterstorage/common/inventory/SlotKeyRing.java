package io.github.tehstoneman.betterstorage.common.inventory;

import io.github.tehstoneman.betterstorage.api.lock.IKey;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class SlotKeyRing extends Slot
{
	public SlotKeyRing( IInventory inventoryIn, int index, int xPosition, int yPosition )
	{
		super( inventoryIn, index, xPosition, yPosition );
	}

	@Override
	public boolean mayPlace( ItemStack stack )
	{
		return stack == null || stack.getItem() instanceof IKey && ( (IKey)stack.getItem() ).isNormalKey();
	}
}
