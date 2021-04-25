package io.github.tehstoneman.betterstorage.common.tileentity;

import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.api.IHasConfig;
import io.github.tehstoneman.betterstorage.api.IHexKeyConfig;
import io.github.tehstoneman.betterstorage.common.inventory.ConfigContainer;
import io.github.tehstoneman.betterstorage.common.item.BetterStorageItems;
import io.github.tehstoneman.betterstorage.common.world.storage.HexKeyConfig;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.LazyOptional;

public class BinTileEntity extends TileEntityContainer implements IHasConfig
{
	public HexKeyConfig							config;
	private final LazyOptional< IHexKeyConfig >	configHandler	= LazyOptional.of( () -> config );

	public BinTileEntity()
	{
		super( BetterStorageTileEntityTypes.STORAGE_BIN.get() );
		config = new HexKeyConfig()
		{
			@Override
			protected void onContentsChanged( int slot )
			{
				BinTileEntity.this.markDirty();
			}
		};
	}

	@Override
	public Container createMenu( int windowID, PlayerInventory playerInventory, PlayerEntity player )
	{
		if( player.getHeldItemMainhand().getItem() == BetterStorageItems.HEX_KEY.get() )
			return new ConfigContainer( windowID, playerInventory, world, pos );
		// return new ContainerReinforcedLocker( windowID, playerInventory, world, pos );
		return null;
	}

	@Override
	public HexKeyConfig getConfig()
	{
		return config;
	}

	@Override
	public void setConfig( HexKeyConfig config )
	{
		this.config = config;
		markDirty();
	}

	@Override
	public CompoundNBT getUpdateTag()
	{
		final CompoundNBT nbt = super.getUpdateTag();

		if( !config.isEmpty() )
			nbt.put( "Config", config.serializeNBT() );

		return nbt;
	}

	@Override
	public void onDataPacket( NetworkManager network, SUpdateTileEntityPacket packet )
	{
		final CompoundNBT nbt = packet.getNbtCompound();
		if( nbt.contains( "Config" ) )
			config.deserializeNBT( nbt.getCompound( "Config" ) );
		else
			config = new HexKeyConfig();
	}

	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		return new SUpdateTileEntityPacket( pos, 1, getUpdateTag() );
	}

	@Override
	public void handleUpdateTag( BlockState state, CompoundNBT nbt )
	{
		super.handleUpdateTag( state, nbt );

		if( nbt.contains( "Config" ) )
			config.deserializeNBT( nbt.getCompound( "Config" ) );
		else
			config = new HexKeyConfig();
	}

	@Override
	public CompoundNBT write( CompoundNBT nbt )
	{
		if( !config.isEmpty() )
			nbt.put( "Config", config.serializeNBT() );

		return super.write( nbt );
	}

	@Override
	public void read( BlockState state, CompoundNBT nbt )
	{
		if( nbt.contains( "Config" ) )
			config.deserializeNBT( nbt.getCompound( "Config" ) );
		else
			config = new HexKeyConfig();

		super.read( state, nbt );
	}

	@Override
	public ITextComponent getName()
	{
		return customName != null ? customName : new TranslationTextComponent( ModInfo.CONTAINER_STORAGE_BIN_NAME );
	}
}
