package io.github.tehstoneman.betterstorage.common.inventory;

import javax.annotation.Nonnull;

import io.github.tehstoneman.betterstorage.api.lock.IKey;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class KeyringSlotHandler extends SlotItemHandler
{
	public KeyringSlotHandler( IItemHandler itemHandler, int index, int xPosition, int yPosition )
	{
		super( itemHandler, index, xPosition, yPosition );
	}

	@Override
	public boolean isItemValid( @Nonnull ItemStack stack )
	{
		return stack.isEmpty() || stack.getItem() instanceof IKey && ( (IKey)stack.getItem() ).isNormalKey() ? super.isItemValid( stack ) : false;
	}
}
