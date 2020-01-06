package io.github.tehstoneman.betterstorage.common.tileentity;

import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.common.inventory.ContainerCardboardBox;
import io.github.tehstoneman.betterstorage.common.item.cardboard.ItemBlockCardboardBox;
import io.github.tehstoneman.betterstorage.config.BetterStorageConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class TileEntityCardboardBox extends TileEntityContainer
{
	public int		uses		= ItemBlockCardboardBox.getMaxUses();
	public boolean	destroyed	= false;
	public int		color		= -1;

	public TileEntityCardboardBox()
	{
		super( BetterStorageTileEntityTypes.CARDBOARD_BOX );
	}

	public boolean isEmpty()
	{
		return inventory.isEmpty();
	}

	public int getUses()
	{
		return uses;
	}

	public void setUses( int uses )
	{
		this.uses = uses;
		markDirty();
	}

	public int getColor()
	{
		if( color < 0 )
			return 0xA08060;
		return color;
	}

	public void setColor( int color )
	{
		this.color = color;
		markDirty();
	}

	/*
	 * ===================
	 * TileEntityContainer
	 * ===================
	 */

	@Override
	public ITextComponent getName()
	{
		return customName != null ? customName : new TranslationTextComponent( ModInfo.CONTAINER_CARDBOARD_BOX_NAME );
	}

	@Override
	public int getRows()
	{
		return BetterStorageConfig.COMMON.cardboardBoxRows.get();
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
}
