package io.github.tehstoneman.betterstorage.common.inventory;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class OutsetHandler extends ItemStackHandler
{
	ItemStack[] ghostStacks;
	public OutsetHandler()
	{
		super();
	}

	public OutsetHandler( int size )
	{
		super( size );
		ghostStacks = new ItemStack[size];
	}

	public OutsetHandler( ItemStack[] stacks )
	{
		super( stacks );
		ghostStacks = new ItemStack[stacks.length];
	}

    public void setSize(int size)
    {
    	super.setSize( size );
    	ghostStacks = new ItemStack[size];
    }

	public void setStackGhosted(int slot, ItemStack stack)
    {
        validateSlotIndex(slot);
        if (ItemStack.matches(this.ghostStacks[slot], stack))
            return;
        this.ghostStacks[slot] = stack;
    }
}
