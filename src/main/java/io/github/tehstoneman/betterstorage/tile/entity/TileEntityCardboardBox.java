package io.github.tehstoneman.betterstorage.tile.entity;

import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.common.block.BetterStorageBlocks;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityContainer;
import io.github.tehstoneman.betterstorage.inventory.InventoryCardboardBox;
import io.github.tehstoneman.betterstorage.inventory.InventoryTileEntity;
import io.github.tehstoneman.betterstorage.item.tile.ItemCardboardBox;
import io.github.tehstoneman.betterstorage.utils.StackUtils;
import io.github.tehstoneman.betterstorage.utils.WorldUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;

public class TileEntityCardboardBox extends TileEntityContainer
{
	public int		uses		= 1;
	public int		color		= -1;
	public boolean	destroyed	= false;

	protected boolean canPickUp()
	{
		return uses >= 0;
	}

	protected ItemStack getItemDropped()
	{
		return new ItemStack( BetterStorageBlocks.cardboardBox );
	}

	protected void onItemDropped( ItemStack stack )
	{
		if( ItemCardboardBox.getUses() > 0 )
			StackUtils.set( stack, uses, "uses" );
		if( color >= 0 )
			StackUtils.set( stack, color, "display", "color" );
		if( getCustomTitle() != null )
			stack.setStackDisplayName( getCustomTitle() );
	}

	// TileEntityContainer stuff

	@Override
	public String getName()
	{
		return ModInfo.containerCardboardBox;
	}

	@Override
	public int getRows()
	{
		return ItemCardboardBox.getRows();
	}

	@Override
	public InventoryTileEntity makePlayerInventory()
	{
		return new InventoryTileEntity( this, new InventoryCardboardBox( inventory ) );
	}

	@Override
	public void onBlockPlaced( EntityLivingBase player, ItemStack stack )
	{
		super.onBlockPlaced( player, stack );
		// If the cardboard box item has items, set the container contents to them.
		if( StackUtils.has( stack, "Items" ) )
		{
			final ItemStack[] itemContents = StackUtils.getStackContents( stack, inventory.length );
			System.arraycopy( itemContents, 0, inventory, 0, itemContents.length );
		}
		final int maxUses = ItemCardboardBox.getUses();
		if( maxUses > 0 )
			uses = StackUtils.get( stack, maxUses, "uses" );
		color = StackUtils.get( stack, -1, "display", "color" );
	}

	@Override
	public void onBlockDestroyed()
	{
		if( !canPickUp() || destroyed )
			return;
		final boolean empty = StackUtils.isEmpty( inventory );
		if( !empty )
		{
			uses--;
			if( !canPickUp() )
			{
				destroyed = true;
				dropContents();
				return;
			}
		}
		final ItemStack stack = getItemDropped();
		if( !empty )
			StackUtils.setStackContents( stack, inventory );
		onItemDropped( stack );
		// Don't drop an empty cardboard box in creative.
		if( !empty || !brokenInCreative )
			WorldUtils.dropStackFromBlock( this, stack );
	}

	// Tile entity synchronization

	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		final NBTTagCompound compound = new NBTTagCompound();
		if( color >= 0 )
			compound.setInteger( "color", color );
		return new SPacketUpdateTileEntity( pos, 0, compound );
	}

	@Override
	public void onDataPacket( NetworkManager net, SPacketUpdateTileEntity packet )
	{
		final NBTTagCompound compound = packet.getNbtCompound();
		color = compound.hasKey( "color" ) ? compound.getInteger( "color" ) : -1;
		worldObj.notifyBlockUpdate( pos, worldObj.getBlockState( pos ), worldObj.getBlockState( pos ), 3 );
	}

	// Reading from / writing to NBT

	@Override
	public void readFromNBT( NBTTagCompound compound )
	{
		super.readFromNBT( compound );
		uses = compound.hasKey( "uses" ) ? compound.getInteger( "uses" ) : ItemCardboardBox.getUses();
		color = compound.hasKey( "color" ) ? compound.getInteger( "color" ) : -1;
	}

	@Override
	public NBTTagCompound writeToNBT( NBTTagCompound compound )
	{
		super.writeToNBT( compound );
		if( ItemCardboardBox.getUses() > 0 )
			compound.setInteger( "uses", uses );
		if( color >= 0 )
			compound.setInteger( "color", color );
		return compound;
	}
}
