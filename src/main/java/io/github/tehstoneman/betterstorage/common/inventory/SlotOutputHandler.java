package io.github.tehstoneman.betterstorage.common.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotOutputHandler extends SlotItemHandler
{
	public boolean isGhosted = false;
	
	public SlotOutputHandler( IItemHandler itemHandler, int index, int xPosition, int yPosition )
	{
		super( itemHandler, index, xPosition, yPosition );
	}

    @Override
    public boolean isItemValid(ItemStack stack)
    {
    	return false;
    }

    @Override
    public boolean canTakeStack(EntityPlayer playerIn)
    {
        return isGhosted ? false : super.canTakeStack( playerIn );
    }

    @SideOnly(Side.CLIENT)
    public boolean canBeHovered()
    {
        return isGhosted ? false : super.canBeHovered();
    }
}
