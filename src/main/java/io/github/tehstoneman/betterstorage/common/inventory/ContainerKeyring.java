package io.github.tehstoneman.betterstorage.common.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;

//@InventoryContainer
public class ContainerKeyring extends Container
{
	private final IInventory			inventoryPlayer;
	private final KeyringStackHandler	inventoryKeyRing;
	public int							indexStart, indexPlayer, indexHotbar;

	public ContainerKeyring( int windowID, PlayerInventory playerInventory, ItemStack keyring, int protectedIndex )
	{
		super( BetterStorageContainerTypes.KEYRING.get(), windowID );

		inventoryPlayer = playerInventory;
		inventoryKeyRing = (KeyringStackHandler)keyring.getCapability( CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null ).orElse( null );
		indexStart = 0;
		indexHotbar = inventoryKeyRing.getSlots();
		indexPlayer = indexHotbar + 9;

		for( int i = 0; i < indexHotbar; i++ )
		{
			final int x = i % 9 * 18 + 8;
			final int y = i / 9 * 18 + 18;
			addSlot( new KeyringSlotHandler( inventoryKeyRing, i, x, y ) );
		}

		for( int i = 0; i < 27; i++ )
		{
			final int x = i % 9 * 18 + 8;
			final int y = 50 + i / 9 * 18;
			addSlot( new Slot( inventoryPlayer, i + 9, x, y ) );
		}

		for( int i = 0; i < 9; i++ )
		{
			final int x = i % 9 * 18 + 8;
			final int y = 108;
			addSlot( new Slot( inventoryPlayer, i, x, y ) );
		}
	}

	@Override
	public ItemStack clicked( int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player )
	{
		if( slotId >= 0 && getSlot( slotId ) != null && getSlot( slotId ).getItem() == player.getMainHandItem() )
			return ItemStack.EMPTY;
		return super.clicked( slotId, dragType, clickTypeIn, player );
	}

	@Override
	public boolean stillValid( PlayerEntity playerIn )
	{
		return true;
	}

	@Override
	public ItemStack quickMoveStack( PlayerEntity playerIn, int index )
	{
		final Slot slot = slots.get( index );
		ItemStack returnStack = ItemStack.EMPTY;

		if( slot != null && slot.hasItem() )
		{
			final ItemStack itemStack = slot.getItem();
			returnStack = itemStack.copy();

			if( index < indexHotbar )
			{
				// Try to transfer from container to player
				if( !moveItemStackTo( itemStack, indexHotbar, slots.size(), true ) )
					return ItemStack.EMPTY;
			}
			else if( !moveItemStackTo( itemStack, 0, indexHotbar, false ) )
				return ItemStack.EMPTY;

			if( itemStack.isEmpty() )
				slot.set( ItemStack.EMPTY );
			else
				slot.setChanged();
		}

		return returnStack;
	}

	@Override
	public void removed( PlayerEntity playerIn )
	{
		super.removed( playerIn );
		inventoryKeyRing.updateCount();
	}
}
