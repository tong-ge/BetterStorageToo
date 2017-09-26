package io.github.tehstoneman.betterstorage.common.tileentity;

import java.util.logging.Logger;

import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.common.block.BetterStorageBlocks;
import io.github.tehstoneman.betterstorage.common.item.cardboard.ItemBlockCardboardBox;
import io.github.tehstoneman.betterstorage.utils.StackUtils;
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
	public int		color		= -1;

	protected boolean canPickUp()
	{
		return uses >= 0 || ItemBlockCardboardBox.getUses() == 0;
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
		uses = ItemBlockCardboardBox.getUses();

		// If the cardboard box item has items, set the container contents to them.
		if( stack.hasTagCompound() )
		{
			final NBTTagCompound compound = stack.getTagCompound();
			if( compound.hasKey( "Inventory" ) )
				inventory.deserializeNBT( compound.getCompoundTag( "Inventory" ) );
			if( uses > 0 && compound.hasKey( "uses" ) )
				uses = Math.min( uses, compound.getInteger( "uses" ) );
			if( compound.hasKey( "color" ) )
				color = compound.getInteger( "color" );
		}
		markDirty();
	}

	@Override
	public void onBlockDestroyed()
	{
		if( !canPickUp() || destroyed )
			return;

		final boolean empty = isEmpty( inventory );
		if( !empty )
		{
			if( uses >= 0 )
				uses--;
			if( !canPickUp() )
			{
				destroyed = true;
				dropContents();
				return;
			}
		}

		// Don't drop an empty cardboard box in creative.
		if( !empty || !brokenInCreative )
		{
			final ItemStack stack = new ItemStack( BetterStorageBlocks.CARDBOARD_BOX );
			final NBTTagCompound compound = new NBTTagCompound();

			compound.setInteger( "uses", uses );
			if( color >= 0 )
				compound.setInteger( "color", color );

			if( !empty )
				compound.setTag( "Inventory", inventory.serializeNBT() );

			stack.setTagCompound( compound );
			final EntityItem entityItem = new EntityItem( world, pos.getX(), pos.getY(), pos.getZ(), stack );
			world.spawnEntity( entityItem );
		}
	}

	// Tile entity synchronization

	@Override
	public NBTTagCompound getUpdateTag()
	{
		final NBTTagCompound compound = super.getUpdateTag();
		compound.setInteger( "uses", uses );
		if( color >= 0 )
			compound.setInteger( "color", color );
		return compound;
	}

	/*
	 * @Override
	 * public SPacketUpdateTileEntity getUpdatePacket()
	 * {
	 * final NBTTagCompound compound = getUpdateTag();
	 * return new SPacketUpdateTileEntity( pos, 0, compound );
	 * }
	 */

	@Override
	public void onDataPacket( NetworkManager net, SPacketUpdateTileEntity packet )
	{
		final NBTTagCompound compound = packet.getNbtCompound();
		if( compound.hasKey( "uses" ) )
			uses = compound.getInteger( "uses" );
		if( compound.hasKey( "color" ) )
			color = compound.getInteger( "color" );
		// worldObj.notifyBlockUpdate( pos, worldObj.getBlockState( pos ), worldObj.getBlockState( pos ), 3 );
	}

	// Reading from / writing to NBT

	@Override
	public void readFromNBT( NBTTagCompound compound )
	{
		super.readFromNBT( compound );
		uses = compound.hasKey( "uses" ) ? compound.getInteger( "uses" ) : ItemBlockCardboardBox.getUses();
		if( compound.hasKey( "color" ) )
			color = compound.getInteger( "color" );
	}

	@Override
	public NBTTagCompound writeToNBT( NBTTagCompound compound )
	{
		super.writeToNBT( compound );
		if( ItemBlockCardboardBox.getUses() > 0 )
			compound.setInteger( "uses", uses );
		if( color >= 0 )
			compound.setInteger( "color", color );
		return compound;
	}

	public int getColor()
	{
		if( color < 0 )
			return 0x705030;
		return color;
	}
}
