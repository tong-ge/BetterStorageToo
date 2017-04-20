package io.github.tehstoneman.betterstorage.common.inventory;

import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotCraftingHandler extends SlotItemHandler
{
	public SlotCraftingHandler( IItemHandler itemHandler, int index, int xPosition, int yPosition )
	{
		super( itemHandler, index, xPosition, yPosition );
	}
}
