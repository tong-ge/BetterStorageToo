package io.github.tehstoneman.betterstorage.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class InventoryWrapper implements IInventory
{

	public final IInventory	base;

	private boolean			overwriteName	= false;
	private String			title;
	private boolean			localized;

	public InventoryWrapper( IInventory base )
	{
		this.base = base;
	}

	public InventoryWrapper( IInventory base, String title, boolean localized )
	{
		this( base );
		overwriteName = true;
		this.title = title;
		this.localized = localized;
	}

	@Override
	public ITextComponent getDisplayName()
	{
		return overwriteName ? new TextComponentString( title ) : base.getDisplayName();
	}

	@Override
	public boolean hasCustomName()
	{
		return overwriteName ? localized : base.hasCustomName();
	}

	@Override
	public int getContainerSize()
	{
		return base.getContainerSize();
	}

	@Override
	public ItemStack getItem( int slot )
	{
		return base.getItem( slot );
	}

	@Override
	public ItemStack remove( int slot, int amount )
	{
		return base.remove( slot, amount );
	}

	@Override
	public void setInventorySlotContents( int slot, ItemStack stack )
	{
		base.setInventorySlotContents( slot, stack );
	}

	/*
	 * @Override
	 * public ItemStack getItemOnClosing(int slot) { return base.getItemOnClosing(slot); }
	 */

	@Override
	public int getInventoryStackLimit()
	{
		return base.getInventoryStackLimit();
	}

	@Override
	public boolean isUseableByPlayer( EntityPlayer player )
	{
		return base.isUseableByPlayer( player );
	}

	@Override
	public boolean mayPlaceForSlot( int slot, ItemStack stack )
	{
		return base.mayPlaceForSlot( slot, stack );
	}

	@Override
	public void setChanged()
	{
		base.setChanged();
	}

	@Override
	public void openInventory( EntityPlayer player )
	{
		base.openInventory( player );
	}

	@Override
	public void closeInventory( EntityPlayer player )
	{
		base.closeInventory( player );
	}

	@Override
	public boolean equals( Object obj )
	{
		if( !( obj instanceof InventoryWrapper ) )
			return false;
		final InventoryWrapper inv = (InventoryWrapper)obj;
		return base.equals( inv.base );
	}

	@Override
	public String getName()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack removeStackFromSlot( int index )
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getField( int id )
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setField( int id, int value )
	{
		// TODO Auto-generated method stub
	}

	@Override
	public int getFieldCount()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void clear()
	{
		// TODO Auto-generated method stub
	}
}
