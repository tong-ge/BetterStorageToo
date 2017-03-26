package io.github.tehstoneman.betterstorage.common.tileentity;

import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.common.block.BetterStorageBlocks;
import io.github.tehstoneman.betterstorage.common.item.ItemBlockCardboardBox;
import io.github.tehstoneman.betterstorage.utils.StackUtils;
import io.github.tehstoneman.betterstorage.utils.WorldUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;

public class TileEntityCardboardBox extends TileEntityContainer
{
	public int		uses		= 1;
	public boolean	destroyed	= false;

	protected boolean canPickUp()
	{
		return uses >= 0;
	}

	protected ItemStack getItemDropped()
	{
		return new ItemStack( BetterStorageBlocks.CARDBOARD_BOX );
	}

	protected void onItemDropped( ItemStack stack )
	{
		if( ItemBlockCardboardBox.getUses() > 0 )
			StackUtils.set( stack, uses, "uses" );
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
		return ItemBlockCardboardBox.getRows();
	}

	@Override
	public void onBlockPlaced( EntityLivingBase player, ItemStack stack )
	{
		super.onBlockPlaced( player, stack );
		// If the cardboard box item has items, set the container contents to them.
		if( stack.hasTagCompound() )
		{
			final NBTTagCompound compound = stack.getTagCompound();
			if( compound.hasKey( "Inventory" ) )
				inventory.deserializeNBT( compound.getCompoundTag( "Inventory" ) );
			uses = ItemBlockCardboardBox.getUses();
			if( uses > 0 && compound.hasKey( "Uses" ) )
				uses = Math.min( uses, compound.getInteger( "uses" ) );
		}
	}

	@Override
	public void onBlockDestroyed()
	{
		if( !canPickUp() || destroyed )
			return;

		final boolean empty = isEmpty( inventory );
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
		NBTTagCompound compound = stack.getTagCompound();
		if( compound == null )
			compound = new NBTTagCompound();

		compound.setInteger( "uses", uses );
		
		if( !empty )
			compound.setTag( "Inventory", inventory.serializeNBT() );

		// Don't drop an empty cardboard box in creative.
		if( !empty || !brokenInCreative )
		{
			stack.setTagCompound( compound );
			EntityItem entityItem = new EntityItem(worldObj, pos.getX(), pos.getY(), pos.getZ(), stack );
			worldObj.spawnEntityInWorld( entityItem );
		}

	}

	// Tile entity synchronization

	@Override
	public NBTTagCompound getUpdateTag()
	{
		final NBTTagCompound compound = super.getUpdateTag();
		compound.setInteger( "uses", uses );
		return compound;
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		final NBTTagCompound compound = getUpdateTag();
		return new SPacketUpdateTileEntity( pos, 0, compound );
	}

	@Override
	public void onDataPacket( NetworkManager net, SPacketUpdateTileEntity packet )
	{
		final NBTTagCompound compound = packet.getNbtCompound();
		if( compound.hasKey( "uses" ) )
			uses = compound.getInteger( "uses" );
		// worldObj.notifyBlockUpdate( pos, worldObj.getBlockState( pos ), worldObj.getBlockState( pos ), 3 );
	}

	// Reading from / writing to NBT

	@Override
	public void readFromNBT( NBTTagCompound compound )
	{
		super.readFromNBT( compound );
		uses = compound.hasKey( "uses" ) ? compound.getInteger( "uses" ) : ItemBlockCardboardBox.getUses();
	}

	@Override
	public NBTTagCompound writeToNBT( NBTTagCompound compound )
	{
		super.writeToNBT( compound );
		if( ItemBlockCardboardBox.getUses() > 0 )
			compound.setInteger( "uses", uses );
		return compound;
	}
}
