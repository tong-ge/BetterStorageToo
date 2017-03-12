package io.github.tehstoneman.betterstorage.common.inventory;

import io.github.tehstoneman.betterstorage.api.lock.IKey;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.util.Constants;

public class InventoryKeyring implements IInventory // InventoryItem
{
	private final ItemStack	invItem;
	private ItemStack[]		inventory	= new ItemStack[9];

	public InventoryKeyring( ItemStack stack )
	{
		invItem = stack;

		if( !stack.hasTagCompound() )
			stack.setTagCompound( new NBTTagCompound() );

		readFromNBT( stack.getTagCompound() );
	}

	@Override
	public int getSizeInventory()
	{
		return inventory.length;
	}

	@Override
	public ItemStack getStackInSlot( int index )
	{
		return inventory[index];
	}

	@Override
	public ItemStack decrStackSize( int index, int count )
	{
		ItemStack stack = getStackInSlot( index );
		if( stack != null )
			if( stack.stackSize > count )
			{
				stack = stack.splitStack( count );
				markDirty();
			}
			else
				setInventorySlotContents( index, null );
		return stack;
	}

	@Override
	public ItemStack removeStackFromSlot( int index )
	{
		return decrStackSize( index, getStackInSlot( index ).stackSize );
	}

	@Override
	public void setInventorySlotContents( int index, ItemStack stack )
	{
		if( stack != null && stack.stackSize > getInventoryStackLimit() )
			stack.stackSize = getInventoryStackLimit();
		inventory[index] = stack;
		markDirty();
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 1;
	}

	@Override
	public void markDirty()
	{
		int count = 0;
		for( int i = 0; i < getSizeInventory(); i++ )
			if( inventory[i] != null )
				if( inventory[i].stackSize == 0 )
					inventory[i] = null;
				else
					count++;
		invItem.setItemDamage( (int)Math.ceil( count / 3.0 ) );
		invItem.setTagCompound( writeToNBT( invItem.getTagCompound() ) );
	}

	@Override
	public boolean isUseableByPlayer( EntityPlayer player )
	{
		return true;
	}

	@Override
	public void openInventory( EntityPlayer player )
	{}

	@Override
	public void closeInventory( EntityPlayer player )
	{}

	@Override
	public boolean isItemValidForSlot( int slot, ItemStack stack )
	{
		return stack == null || stack.getItem() instanceof IKey && ( (IKey)stack.getItem() ).isNormalKey();
	}

	@Override
	public int getField( int id )
	{
		return 0;
	}

	@Override
	public void setField( int id, int value )
	{}

	@Override
	public int getFieldCount()
	{
		return 0;
	}

	@Override
	public void clear()
	{
		inventory = new ItemStack[9];
		markDirty();
	}

	@Override
	public String getName()
	{
		return invItem.getDisplayName();
	}

	@Override
	public boolean hasCustomName()
	{
		return invItem.hasDisplayName();
	}

	@Override
	public ITextComponent getDisplayName()
	{
		return new TextComponentString( invItem.getDisplayName() );
	}

	public void readFromNBT( NBTTagCompound compound )
	{
		inventory = new ItemStack[compound.hasKey( "Size", Constants.NBT.TAG_INT ) ? compound.getInteger( "Size" ) : inventory.length];
		if( compound.hasKey( "Items" ) )
		{
			final NBTTagList tagList = compound.getTagList( "Items", Constants.NBT.TAG_COMPOUND );
			for( int i = 0; i < tagList.tagCount(); i++ )
			{
				final NBTTagCompound itemTags = tagList.getCompoundTagAt( i );
				final int slot = itemTags.getInteger( "Slot" );

				if( slot >= 0 && slot < inventory.length )
					inventory[slot] = ItemStack.loadItemStackFromNBT( itemTags );
			}
		}
	}

	public NBTTagCompound writeToNBT( NBTTagCompound compound )
	{
		final NBTTagList nbtTagList = new NBTTagList();
		for( int i = 0; i < inventory.length; i++ )
			if( inventory[i] != null )
			{
				final NBTTagCompound itemTag = new NBTTagCompound();
				itemTag.setInteger( "Slot", i );
				inventory[i].writeToNBT( itemTag );
				nbtTagList.appendTag( itemTag );
			}
		compound.setTag( "Items", nbtTagList );
		compound.setInteger( "Size", inventory.length );
		return compound;
	}
}
