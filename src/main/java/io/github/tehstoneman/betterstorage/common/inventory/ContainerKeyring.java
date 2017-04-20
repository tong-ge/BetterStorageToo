package io.github.tehstoneman.betterstorage.common.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

//@InventoryContainer
public class ContainerKeyring extends Container
{
	private final IInventory		inventoryPlayer;
	private final InventoryKeyring	inventoryKeyRing;
	private final int				protectedIndex;

	public int						indexStart, indexPlayer, indexHotbar;

	public ContainerKeyring( EntityPlayer player, ItemStack keyring, int protectedIndex )
	{
		inventoryPlayer = player.inventory;
		inventoryKeyRing = new InventoryKeyring( keyring );
		this.protectedIndex = protectedIndex;

		indexStart = 0;
		indexHotbar = inventoryKeyRing.getSizeInventory();
		indexPlayer = indexHotbar + 9;

		for( int i = 0; i < indexHotbar; i++ )
		{
			final int x = i % 9 * 18 + 8;
			final int y = i / 9 * 18 + 18;
			addSlotToContainer( new SlotKeyRing( inventoryKeyRing, i, x, y ) );
		}

		for( int i = 0; i < 27; i++ )
		{
			final int x = i % 9 * 18 + 8;
			final int y = 50 + i / 9 * 18;
			addSlotToContainer( new Slot( inventoryPlayer, i + 9, x, y ) );
		}

		for( int i = 0; i < 9; i++ )
		{
			final int x = i % 9 * 18 + 8;
			final int y = 108;
			addSlotToContainer( new Slot( inventoryPlayer, i, x, y ) );
		}
	}

	@Override
	public ItemStack slotClick( int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player )
	{
		if( slotId >= 0 && getSlot( slotId ) != null && getSlot( slotId ).getStack() == player.getHeldItemMainhand() )
			return null;
		return super.slotClick( slotId, dragType, clickTypeIn, player );
	}

	@Override
	public boolean canInteractWith( EntityPlayer playerIn )
	{
		return true;
	}

	@Override
	public ItemStack transferStackInSlot( EntityPlayer playerIn, int index )
	{
		final Slot slot = inventorySlots.get( index );
		ItemStack returnStack = null;

		if( slot != null && slot.getHasStack() )
		{
			final ItemStack itemStack = slot.getStack();
			returnStack = itemStack.copy();

			if( index < indexHotbar )
			{
				// Try to transfer from container to player
				if( !mergeItemStack( itemStack, indexHotbar, inventorySlots.size(), true ) )
					return null;
			}
			else
				if( !mergeItemStack( itemStack, 0, indexHotbar, false ) )
					return null;

			if( itemStack == null || itemStack.stackSize == 0 )
				slot.putStack( null );
			else
				slot.onSlotChanged();
		}

		return returnStack;
	}
}
