package io.github.tehstoneman.betterstorage.common.tileentity;

import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.common.inventory.ContainerCardboardBox;
import io.github.tehstoneman.betterstorage.common.item.cardboard.ItemBlockCardboardBox;
import io.github.tehstoneman.betterstorage.config.BetterStorageConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityCardboardBox extends TileEntityContainer
{
	public int		uses		= ItemBlockCardboardBox.getMaxUses();
	public boolean	destroyed	= false;
	public int		color		= -1;

	public TileEntityCardboardBox( BlockPos blockPos, BlockState blockState )
	{
		super( BetterStorageTileEntityTypes.CARDBOARD_BOX.get(), blockPos, blockState );
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
		setChanged();
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
		setChanged();
	}

	/*
	 * ===================
	 * TileEntityContainer
	 * ===================
	 */

	@Override
	public Component getName()
	{
		return customName != null ? customName : new TranslatableComponent( ModInfo.CONTAINER_CARDBOARD_BOX_NAME );
	}

	@Override
	public int getRows()
	{
		return BetterStorageConfig.COMMON.cardboardBoxRows.get();
	}

	@Override
	public AbstractContainerMenu createMenu( int windowID, Inventory playerInventory, Player player )
	{
		return new ContainerCardboardBox( windowID, playerInventory, level, worldPosition );
	}

	/*
	 * ==========================
	 * BlockEntity synchronization
	 * ==========================
	 */

	@Override
	public CompoundTag getUpdateTag()
	{
		final CompoundTag nbt = super.getUpdateTag();

		if( ItemBlockCardboardBox.getMaxUses() > 0 )
			nbt.putInt( "Uses", uses );
		if( color >= 0 )
			nbt.putInt( "Color", color );

		return nbt;
	}

	@Override
	public void handleUpdateTag( CompoundTag nbt )
	{
		super.handleUpdateTag( nbt );

		uses = nbt.contains( "Uses" ) ? nbt.getInt( "Uses" ) : ItemBlockCardboardBox.getMaxUses();
		color = nbt.contains( "Color" ) ? nbt.getInt( "Color" ) : -1;
	}

	@Override
	public CompoundTag save( CompoundTag nbt )
	{
		if( ItemBlockCardboardBox.getMaxUses() > 0 )
			nbt.putInt( "Uses", uses );
		if( color >= 0 )
			nbt.putInt( "Color", color );

		return super.save( nbt );
	}

	@Override
	public void load( CompoundTag nbt )
	{
		uses = nbt.contains( "Uses" ) ? nbt.getInt( "Uses" ) : ItemBlockCardboardBox.getMaxUses();
		color = nbt.contains( "Color" ) ? nbt.getInt( "Color" ) : -1;

		super.load( nbt );
	}
}
