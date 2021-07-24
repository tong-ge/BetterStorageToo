package io.github.tehstoneman.betterstorage.inventory;

import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

/** An inventory that is connected to a TileEntityInventoy. */
public class InventoryTileEntity extends InventoryBetterStorage
{

	public final TileEntityContainer	mainTileEntity;
	public final TileEntityContainer[]	tileEntities;
	public final IInventory				inventory;
	public final int					columns, rows;

	public InventoryTileEntity( TileEntityContainer mainTileEntity, TileEntityContainer[] tileEntities, IInventory inventory )
	{
		this.mainTileEntity = mainTileEntity;
		this.tileEntities = tileEntities;
		this.inventory = inventory;

		columns = mainTileEntity.getColumns();
		rows = inventory.getContainerSize() / columns;
	}

	public InventoryTileEntity( TileEntityContainer mainTileEntity, TileEntityContainer... tileEntities )
	{
		this( mainTileEntity, tileEntities, new InventoryStacks( getAllContents( tileEntities ) ) );
	}

	public InventoryTileEntity( TileEntityContainer tileEntity, IInventory inventory )
	{
		this( tileEntity, new TileEntityContainer[] { tileEntity }, inventory );
	}

	public InventoryTileEntity( TileEntityContainer tileEntity )
	{
		this( tileEntity, new InventoryStacks( tileEntity.inventory ) );
	}

	private static ItemStack[][] getAllContents( TileEntityContainer... tileEntities )
	{
		final ItemStack[][] allContents = new ItemStack[tileEntities.length][];
		for( int i = 0; i < tileEntities.length; i++ )
			allContents[i] = tileEntities[i].inventory;
		return allContents;
	}

	// @Override
	// public String getInventoryName() { return mainTileEntity.getContainerTitle(); }
	// @Override
	// public boolean hasCustomInventoryName() { return !mainTileEntity.shouldLocalizeTitle(); }

	@Override
	public int getContainerSize()
	{
		return inventory.getContainerSize();
	}

	@Override
	public ItemStack getItem( int slot )
	{
		return inventory.getItem( slot );
	}

	@Override
	public void setInventorySlotContents( int slot, ItemStack stack )
	{
		inventory.setInventorySlotContents( slot, stack );
		setChanged();
	}

	@Override
	public boolean mayPlaceForSlot( int slot, ItemStack stack )
	{
		return inventory.mayPlaceForSlot( slot, stack );
	}

	@Override
	public boolean isUseableByPlayer( EntityPlayer player )
	{
		for( final TileEntityContainer tileEntity : tileEntities )
			if( !tileEntity.canPlayerUseContainer( player ) )
				return false;
		return true;
	}

	// @Override
	// public void openInventory() { mainTileEntity.onContainerOpened(); }
	// @Override
	// public void closeInventory() { mainTileEntity.removed(); }
	@Override
	public void setChanged()
	{
		inventory.setChanged();
		for( final TileEntityContainer te : tileEntities )
			te.setChangedSuper();
	}

	@Override
	public ItemStack removeStackFromSlot( int index )
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void openInventory( EntityPlayer player )
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void closeInventory( EntityPlayer player )
	{
		// TODO Auto-generated method stub

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

	@Override
	public ITextComponent getDisplayName()
	{
		// TODO Auto-generated method stub
		return null;
	}

}
