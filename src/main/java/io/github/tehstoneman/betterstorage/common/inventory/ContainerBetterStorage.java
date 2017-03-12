package io.github.tehstoneman.betterstorage.common.inventory;

import io.github.tehstoneman.betterstorage.client.gui.GuiBetterStorage;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

//@ChestContainer(isLargeChest = true)
public class ContainerBetterStorage extends Container
{
	private final ItemStackHandler		inventoryContainer;
	private final IInventory			inventoryPlayer;
	private final TileEntityContainer	tileContainer;

	private final int					columns;
	private final int					rows;

	public int							indexStart, indexPlayer, indexHotbar;

	public final int					separation;

	@SideOnly( Side.CLIENT )
	public GuiBetterStorage				updateGui;

	public ContainerBetterStorage( EntityPlayer player, TileEntityContainer tileContainer )
	{
		inventoryContainer = (ItemStackHandler)tileContainer.getCapability( CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null );
		inventoryPlayer = player.inventory;
		this.tileContainer = tileContainer;
		tileContainer.onContainerOpened();

		columns = tileContainer.getColumns();
		rows = tileContainer.getRows();

		indexStart = 0;
		indexHotbar = inventoryContainer.getSlots();
		indexPlayer = indexHotbar + 9;

		for( int i = 0; i < indexHotbar; i++ )
		{
			final int x = i % columns * 18 + 8;
			final int y = i / columns * 18 + 18;
			addSlotToContainer( new SlotItemHandler( inventoryContainer, i, x, y ) );
		}

		final int offsetX = ( columns - 9 ) / 2 * 18;
		final int offsetY = rows * 18 + 18;

		for( int i = 0; i < 27; i++ )
		{
			final int x = i % 9 * 18 + 8;
			final int y = 14 + i / 9 * 18;
			addSlotToContainer( new Slot( inventoryPlayer, i + 9, offsetX + x, offsetY + y ) );
		}

		for( int i = 0; i < 9; i++ )
		{
			final int x = i % 9 * 18 + 8;
			final int y = 72;
			addSlotToContainer( new Slot( inventoryPlayer, i, offsetX + x, offsetY + y ) );
		}

		separation = 14;
	}

	// @ChestContainer.RowSizeCallback
	public int getColumns()
	{
		return columns;
	}

	public int getRows()
	{
		return rows;
	}

	@Override
	public boolean canInteractWith( EntityPlayer playerIn )
	{
		return true;
	}

	@Override
	public void onContainerClosed( EntityPlayer playerIn )
	{
		tileContainer.onContainerClosed();
		super.onContainerClosed( playerIn );
	}

	public int getHeight()
	{
		return ( getRows() + 4 ) * 18 + separation + 29;
	}

	/** Returns if the slot is in the inventory. */
	/*
	 * protected boolean inInventory( int slot )
	 * {
	 * return slot < inventory.getSizeInventory();
	 * }
	 */

	/** Returns the start slot where items should be transfered to from this slot. */
	/*
	 * protected int transferStart( int slot )
	 * {
	 * return inInventory( slot ) ? inventory.getSizeInventory() : 0;
	 * }
	 */

	/** Returns the end slot where items should be transfered to from this slot. */
	/*
	 * protected int transferEnd( int slot )
	 * {
	 * return inInventory( slot ) ? inventorySlots.size() : inventory.getSizeInventory();
	 * }
	 */

	/** Returns the direction the stack should be transfered in. true = normal, false = backwards. */
	/*
	 * protected boolean transferDirection( int slot )
	 * {
	 * return inInventory( slot );
	 * }
	 */

	/** Called when a slot is changed. */
	/*
	 * public void onSlotChanged( int slot )
	 * {}
	 */

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

	/*
	 * @Override
	 * // public ItemStack slotClick( int slotId, int button, int special, EntityPlayer player )
	 * public ItemStack slotClick( int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player )
	 * {
	 * Slot slot = null;
	 * if( slotId >= 0 && slotId < inventorySlots.size() )
	 * slot = inventorySlots.get( slotId );
	 *
	 * if( slot != null )
	 * if( clickTypeIn == ClickType.QUICK_CRAFT )
	 * {
	 * if( dragType == 0 || dragType == 1 )
	 * {
	 * // Override default behavior to use putStack
	 * // instead of manipulating stackSize directly.
	 * final ItemStack slotStack = slot.getStack();
	 * final ItemStack holding = player.inventory.getItemStack();
	 * if( slotStack != null && holding != null && ( holding == null ? slot.canTakeStack( player ) : slot.isItemValid( holding ) )
	 * && StackUtils.matches( slotStack, holding ) )
	 * {
	 * int amount = dragType == 0 ? holding.stackSize : 1;
	 * amount = Math.min( amount, slot.getSlotStackLimit() - slotStack.stackSize );
	 * amount = Math.min( amount, slotStack.getMaxStackSize() - slotStack.stackSize );
	 * if( amount > 0 )
	 * {
	 * if( ( holding.stackSize -= amount ) <= 0 )
	 * player.inventory.setItemStack( null );
	 * slot.putStack( StackUtils.copyStack( slotStack, slotStack.stackSize + amount ) );
	 * }
	 * return slotStack;
	 * }
	 * }
	 * }
	 * else
	 * if( clickTypeIn == ClickType.PICKUP )
	 * // Override default shift-click so it doesn't call
	 * // retrySlotClick, whatever that was meant for.
	 * return slot.canTakeStack( player ) ? transferStackInSlot( player, slotId ) : null;
	 * else
	 * if( clickTypeIn == ClickType.QUICK_MOVE && dragType >= 0 && dragType < 9 )
	 * {
	 * // Override default hotbar switching to make sure
	 * // the items can be taken and put into those slots.
	 * if( startHotbar < 0 )
	 * return null;
	 * final Slot slot2 = inventorySlots.get( startHotbar + dragType );
	 * final ItemStack stack = slot.getStack();
	 * if( !slot2.canTakeStack( player ) || stack != null && !slot2.isItemValid( stack ) )
	 * return null;
	 * }
	 *
	 * return super.slotClick( slotId, dragType, clickTypeIn, player );
	 * }
	 */

	/*
	 * @Override
	 * public void onContainerClosed( EntityPlayer player )
	 * {
	 * super.onContainerClosed( player );
	 * inventory.closeInventory( player );
	 * }
	 */

	/*
	 * @Override
	 *
	 * @SideOnly( Side.CLIENT )
	 * public void updateProgressBar( int id, int val )
	 * {
	 * if( updateGui != null )
	 * updateGui.update( id, val );
	 * }
	 */

	/*
	 * @SideOnly( Side.CLIENT )
	 * public void setUpdateGui( GuiBetterStorage gui )
	 * {
	 * updateGui = gui;
	 * }
	 */

	/**
	 * Sends a packet to all players looking at
	 * this GUI for them to update something.
	 */
	/*
	 * public void sendUpdate( int id, int value )
	 * {
	 * for( final Object c : crafters )
	 * ( (ICrafting)c ).sendProgressBarUpdate( this, id, value );
	 * }
	 */

	/**
	 * Sends a packet to all players looking at this GUI for them to update
	 * something, but only if the value is different from the previous one.
	 */
	/*
	 * public void sendUpdateIfChanged( int id, int value, int previousValue )
	 * {
	 * if( value != previousValue )
	 * sendUpdate( id, value );
	 * }
	 */
}
