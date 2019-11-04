package io.github.tehstoneman.betterstorage.common.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class KeyringCapabilityProvider implements ICapabilitySerializable< CompoundNBT >
{
	private final ItemStack		invItem;
	public KeyringStackHandler	inventory;
	private final LazyOptional< IItemHandler >	inventoryHandler	= LazyOptional.of( () -> inventory );
	
	public KeyringCapabilityProvider( ItemStack stack )
	{
		invItem = stack;
		final int size = getSizeContents();
		if( size > 0 )
			inventory = new KeyringStackHandler( size )
			{
				@Override
				protected void onContentsChanged( int slot )
				{
					KeyringCapabilityProvider.this.markDirty();
				}
			};
		else
			inventory = null;
	}

	@Override
	public <T> LazyOptional< T > getCapability( Capability< T > capability, Direction side )
	{
		if( capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY )
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty( capability, inventoryHandler );
		return null;
	}

	protected int getSizeContents()
	{
		return 9;
	}

	public void markDirty()
	{
		CompoundNBT tag = invItem.getOrCreateTag();
		int count = 0;
		for( int i = 0; i < inventory.getSlots(); i++ )
			if( !inventory.getStackInSlot( i ).isEmpty() )
				count++;
		tag.putInt( "Occupied", count );
	}

	@Override
	public CompoundNBT serializeNBT()
	{
		return inventory.serializeNBT();
	}

	@Override
	public void deserializeNBT( CompoundNBT nbt )
	{
		inventory.deserializeNBT( nbt );
	}
}
