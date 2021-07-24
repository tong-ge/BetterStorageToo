package io.github.tehstoneman.betterstorage.inventory;

import io.github.tehstoneman.betterstorage.utils.StackUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class InventoryItem extends InventoryStacks
{

	private final String			title;

	protected final EntityPlayer	player;
	protected final int				slot;

	protected final ItemStack		stack;

	public InventoryItem( EntityPlayer player, int size, String title, boolean localized )
	{
		super( StackUtils.getStackContents( player.getMainHandItem(), size ) );
		this.title = title;
		this.player = player;
		slot = player.inventory.currentItem;
		stack = player.getMainHandItem().copy();
	}

	@Override
	public String getName()
	{
		return title;
	}

	@Override
	public boolean isUseableByPlayer( EntityPlayer player )
	{
		return true;
	}

	/*
	 * @Override
	 * public void closeInventory()
	 * {
	 * updateStack();
	 * }
	 */

	protected void updateStack()
	{
		StackUtils.setStackContents( stack, allContents[0] );
		player.inventory.setInventorySlotContents( slot, stack );
	}

}
