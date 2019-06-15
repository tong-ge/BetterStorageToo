package io.github.tehstoneman.betterstorage.common.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;

//@InventoryContainer
public class ContainerKeyring extends Container
{
	// private final IInventory inventoryPlayer;
	// private final KeyringStackHandler inventoryKeyRing;
	// private final int protectedIndex;

	public int indexStart, indexPlayer, indexHotbar;

	protected ContainerKeyring( ContainerType< ? > type, int id )
	{
		super( type, id );
		// TODO Auto-generated constructor stub
	}

	/*
	 * public ContainerKeyring( EntityPlayer player, ItemStack keyring, int protectedIndex )
	 * {
	 * inventoryPlayer = player.inventory;
	 * // inventoryKeyRing = (KeyringStackHandler)keyring.getCapability( CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null );
	 * this.protectedIndex = protectedIndex;
	 * 
	 * indexStart = 0;
	 * // indexHotbar = inventoryKeyRing.getSlots();
	 * indexPlayer = indexHotbar + 9;
	 * 
	 * for( int i = 0; i < indexHotbar; i++ )
	 * {
	 * final int x = i % 9 * 18 + 8;
	 * final int y = i / 9 * 18 + 18;
	 * // addSlotToContainer( new KeyringSlotHandler( inventoryKeyRing, i, x, y ) );
	 * }
	 * 
	 * for( int i = 0; i < 27; i++ )
	 * {
	 * final int x = i % 9 * 18 + 8;
	 * final int y = 50 + i / 9 * 18;
	 * // addSlotToContainer( new Slot( inventoryPlayer, i + 9, x, y ) );
	 * }
	 * 
	 * for( int i = 0; i < 9; i++ )
	 * {
	 * final int x = i % 9 * 18 + 8;
	 * final int y = 108;
	 * // addSlotToContainer( new Slot( inventoryPlayer, i, x, y ) );
	 * }
	 * }
	 */

	/*
	 * @Override
	 * public ItemStack slotClick( int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player )
	 * {
	 * if( slotId >= 0 && getSlot( slotId ) != null && getSlot( slotId ).getStack() == player.getHeldItemMainhand() )
	 * return ItemStack.EMPTY;
	 * return super.slotClick( slotId, dragType, clickTypeIn, player );
	 * }
	 */

	/*
	 * @Override
	 * public boolean canInteractWith( EntityPlayer playerIn )
	 * {
	 * return true;
	 * }
	 */

	/*
	 * @Override
	 * public ItemStack transferStackInSlot( EntityPlayer playerIn, int index )
	 * {
	 * final Slot slot = inventorySlots.get( index );
	 * ItemStack returnStack = ItemStack.EMPTY;
	 * 
	 * if( slot != null && slot.getHasStack() )
	 * {
	 * final ItemStack itemStack = slot.getStack();
	 * returnStack = itemStack.copy();
	 * 
	 * if( index < indexHotbar )
	 * {
	 * // Try to transfer from container to player
	 * if( !mergeItemStack( itemStack, indexHotbar, inventorySlots.size(), true ) )
	 * return ItemStack.EMPTY;
	 * }
	 * else
	 * if( !mergeItemStack( itemStack, 0, indexHotbar, false ) )
	 * return ItemStack.EMPTY;
	 * 
	 * if( itemStack.isEmpty() )
	 * slot.putStack( ItemStack.EMPTY );
	 * else
	 * slot.onSlotChanged();
	 * }
	 * 
	 * return returnStack;
	 * }
	 */

	/*
	 * @Override
	 * public void onContainerClosed( EntityPlayer playerIn )
	 * {
	 * super.onContainerClosed( playerIn );
	 * // inventoryKeyRing.updateCount();
	 * }
	 */

	@Override
	public boolean canInteractWith( PlayerEntity playerIn )
	{
		// TODO Auto-generated method stub
		return false;
	}
}
