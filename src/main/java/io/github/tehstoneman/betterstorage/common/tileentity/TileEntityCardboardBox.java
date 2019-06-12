package io.github.tehstoneman.betterstorage.common.tileentity;

import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.common.block.BetterStorageBlocks;
import io.github.tehstoneman.betterstorage.common.item.cardboard.ItemBlockCardboardBox;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class TileEntityCardboardBox extends TileEntityContainer
{
	public TileEntityCardboardBox( TileEntityType< ? > tileEntityTypeIn )
	{
		super( tileEntityTypeIn );
		// TODO Auto-generated constructor stub
	}

	public int		uses		= 1;
	public boolean	destroyed	= false;
	public int		color		= -1;

	/*protected boolean canPickUp()
	{
		return uses >= 0 || ItemBlockCardboardBox.getUses() == 0;
	}*/

	protected void onItemDropped( ItemStack stack )
	{
		/*
		 * if( ItemBlockCardboardBox.getUses() > 0 )
		 * StackUtils.set( stack, uses, "uses" );
		 */
		/*
		 * if( getCustomTitle() != null )
		 * stack.setDisplayName( getCustomTitle() );
		 */
	}

	// TileEntityContainer stuff

	@Override
	public ITextComponent getName()
	{
		return customName != null ? customName : new TextComponentTranslation( ModInfo.containerCardboardBox );
	}

	/*@Override
	public int getRows()
	{
		return ItemBlockCardboardBox.getRows();
	}*/

	/*
	 * @Override
	 * public void onBlockPlaced( EntityLivingBase player, ItemStack stack )
	 * {
	 * super.onBlockPlaced( player, stack );
	 * uses = ItemBlockCardboardBox.getUses();
	 *
	 * // If the cardboard box item has items, set the container contents to them.
	 * if( stack.hasTagCompound() )
	 * {
	 * final NBTTagCompound compound = stack.getTagCompound();
	 * if( compound.hasKey( "Inventory" ) )
	 * inventory.deserializeNBT( compound.getCompoundTag( "Inventory" ) );
	 * if( uses > 0 && compound.hasKey( "uses" ) )
	 * uses = Math.min( uses, compound.getInteger( "uses" ) );
	 * if( compound.hasKey( "color" ) )
	 * color = compound.getInteger( "color" );
	 * }
	 * markDirty();
	 * }
	 */

	@Override
	public void onBlockDestroyed()
	{
		/*if( !canPickUp() || destroyed )
			return;*/

		final boolean empty = isEmpty( inventory );
		if( !empty )
		{
			if( uses >= 0 )
				uses--;
			/*if( !canPickUp() )
			{
				destroyed = true;
				dropContents();
				return;
			}*/
		}

		// Don't drop an empty cardboard box in creative.
		if( !empty || !brokenInCreative )
		{
			final ItemStack stack = new ItemStack( BetterStorageBlocks.CARDBOARD_BOX );
			final NBTTagCompound compound = new NBTTagCompound();

			compound.setInt( "uses", uses );
			if( color >= 0 )
				compound.setInt( "color", color );

			if( !empty )
				compound.setTag( "Inventory", inventory.serializeNBT() );

			// stack.setTagCompound( compound );
			final EntityItem entityItem = new EntityItem( world, pos.getX(), pos.getY(), pos.getZ(), stack );
			world.spawnEntity( entityItem );
		}
	}

	// Tile entity synchronization

	@Override
	public NBTTagCompound getUpdateTag()
	{
		final NBTTagCompound compound = super.getUpdateTag();
		compound.setInt( "uses", uses );
		if( color >= 0 )
			compound.setInt( "color", color );
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
			uses = compound.getInt( "uses" );
		if( compound.hasKey( "color" ) )
			color = compound.getInt( "color" );
		// worldObj.notifyBlockUpdate( pos, worldObj.getBlockState( pos ), worldObj.getBlockState( pos ), 3 );
	}

	// Reading from / writing to NBT

	/*
	 * @Override
	 * public void readFromNBT( NBTTagCompound compound )
	 * {
	 * super.readFromNBT( compound );
	 * uses = compound.hasKey( "uses" ) ? compound.getInt( "uses" ) : ItemBlockCardboardBox.getUses();
	 * if( compound.hasKey( "color" ) )
	 * color = compound.getInt( "color" );
	 * }
	 */

	/*
	 * @Override
	 * public NBTTagCompound writeToNBT( NBTTagCompound compound )
	 * {
	 * super.writeToNBT( compound );
	 * if( ItemBlockCardboardBox.getUses() > 0 )
	 * compound.setInt( "uses", uses );
	 * if( color >= 0 )
	 * compound.setInt( "color", color );
	 * return compound;
	 * }
	 */

	public int getColor()
	{
		if( color < 0 )
			return 0x705030;
		return color;
	}

	@Override
	public void tick()
	{
		// TODO Auto-generated method stub

	}
}
