package io.github.tehstoneman.betterstorage.common.tileentity;

import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.common.inventory.ContainerCardboardBox;
import io.github.tehstoneman.betterstorage.common.item.cardboard.ItemBlockCardboardBox;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class TileEntityCardboardBox extends TileEntityContainer
{
	public TileEntityCardboardBox()
	{
		super( BetterStorageTileEntityTypes.CARDBOARD_BOX );
	}

	public int		uses		= ItemBlockCardboardBox.getMaxUses();
	public boolean	destroyed	= false;
	public int		color		= -1;

	/*
	 * protected boolean canPickUp()
	 * {
	 * return uses >= 0 || ItemBlockCardboardBox.getUses() == 0;
	 * }
	 */

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
		return customName != null ? customName : new TranslationTextComponent( ModInfo.CONTAINER_CARDBOARD_BOX_NAME );
	}

	/*
	 * @Override
	 * public int getRows()
	 * {
	 * return ItemBlockCardboardBox.getRows();
	 * }
	 */

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

	/*
	 * @Override
	 * public void onBlockDestroyed()
	 * {
	 * if( !canPickUp() || destroyed )
	 * return;
	 *
	 * final boolean empty = isEmpty( inventory );
	 * if( !empty )
	 * {
	 * if( uses >= 0 )
	 * uses--;
	 * if( !canPickUp() )
	 * {
	 * destroyed = true;
	 * dropContents();
	 * return;
	 * }
	 * }
	 *
	 * // Don't drop an empty cardboard box in creative.
	 * if( !empty || !brokenInCreative )
	 * {
	 * final ItemStack stack = new ItemStack( BetterStorageBlocks.CARDBOARD_BOX );
	 * final NBTTagCompound compound = new NBTTagCompound();
	 *
	 * compound.setInt( "uses", uses );
	 * if( color >= 0 )
	 * compound.setInt( "color", color );
	 *
	 * if( !empty )
	 * compound.setTag( "Inventory", inventory.serializeNBT() );
	 *
	 * // stack.setTagCompound( compound );
	 * final EntityItem entityItem = new EntityItem( world, pos.getX(), pos.getY(), pos.getZ(), stack );
	 * world.spawnEntity( entityItem );
	 * }
	 * }
	 */

	public int getColor()
	{
		if( color < 0 )
			return 0xA08060;
		return color;
	}

	@Override
	public Container createMenu( int windowID, PlayerInventory playerInventory, PlayerEntity player )
	{
		return new ContainerCardboardBox( windowID, playerInventory, world, pos );
	}

	/*
	 * ==========================
	 * TileEntity synchronization
	 * ==========================
	 */

	@Override
	public CompoundNBT getUpdateTag()
	{
		final CompoundNBT nbt = super.getUpdateTag();

		if( ItemBlockCardboardBox.getMaxUses() > 0 )
			nbt.putInt( "Uses", uses );
		if( color >= 0 )
			nbt.putInt( "Color", color );

		return nbt;
	}

	@Override
	public void handleUpdateTag( CompoundNBT nbt )
	{
		super.handleUpdateTag( nbt );

		uses = nbt.contains( "Uses" ) ? nbt.getInt( "Uses" ) : ItemBlockCardboardBox.getMaxUses();
		color = nbt.contains( "Color" ) ? nbt.getInt( "Color" ) : -1;
	}

	@Override
	public CompoundNBT write( CompoundNBT nbt )
	{
		if( ItemBlockCardboardBox.getMaxUses() > 0 )
			nbt.putInt( "Uses", uses );
		if( color >= 0 )
			nbt.putInt( "Color", color );

		return super.write( nbt );
	}

	@Override
	public void read( CompoundNBT nbt )
	{
		uses = nbt.contains( "Uses" ) ? nbt.getInt( "Uses" ) : ItemBlockCardboardBox.getMaxUses();
		color = nbt.contains( "Color" ) ? nbt.getInt( "Color" ) : -1;

		super.read( nbt );
	}

	public boolean isEmpty()
	{
		return inventory.isEmpty();
	}

	public void setUses( int uses )
	{
		this.uses = uses;
		markDirty();
	}

	public void setColor( int color )
	{
		this.color = color;
		markDirty();
	}
}
