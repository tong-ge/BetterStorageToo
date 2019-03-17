package io.github.tehstoneman.betterstorage.common.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class KeyringCapabilityProvider implements ICapabilitySerializable< NBTTagCompound >
{
	private final ItemStack		invItem;
	public KeyringStackHandler	inventory;

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

	/*
	 * @Override
	 * public boolean hasCapability( Capability< ? > capability, EnumFacing facing )
	 * {
	 * return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
	 * }
	 */

	/*
	 * @Override
	 * public <T> T getCapability( Capability< T > capability, EnumFacing facing )
	 * {
	 * return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T)inventory : null;
	 * }
	 */

	protected int getSizeContents()
	{
		return 9;
	}

	public void markDirty()
	{
		int count = 0;
		for( int i = 0; i < inventory.getSlots(); i++ )
			if( !inventory.getStackInSlot( i ).isEmpty() )
				count++;
		// invItem.setItemDamage( (int)Math.ceil( count / 3.0 ) );
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		return inventory.serializeNBT();
	}

	@Override
	public void deserializeNBT( NBTTagCompound nbt )
	{
		inventory.deserializeNBT( nbt );
	}

	@Override
	public <T> LazyOptional< T > getCapability( Capability< T > cap, EnumFacing side )
	{
		// TODO Auto-generated method stub
		return null;
	}
}
