package io.github.tehstoneman.betterstorage.client.gui;

import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.common.inventory.ContainerBetterStorage;
import io.github.tehstoneman.betterstorage.common.inventory.ContainerCrate;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityContainer;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityCrate;
import io.github.tehstoneman.betterstorage.container.ContainerKeyring;
import io.github.tehstoneman.betterstorage.content.BetterStorageItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class BetterStorageGUIHandler implements IGuiHandler
{
	@Override
	public Object getServerGuiElement( int ID, EntityPlayer player, World world, int x, int y, int z )
	{
		if( ID == EnumGui.KEYRING.getGuiID() )
		{
			final ItemStack itemStack = player.getHeldItemMainhand();
			if( itemStack.getItem() != BetterStorageItems.keyring )
				return null;
			return new ContainerKeyring( player, ModInfo.containerKeyring, player.inventory.currentItem );
		}
		if( ID == EnumGui.CRATE.getGuiID() )
		{
			final TileEntity tileEntity = world.getTileEntity( new BlockPos( x, y, z ) );
			if( tileEntity instanceof TileEntityCrate )
			{
				final TileEntityCrate crate = (TileEntityCrate)tileEntity;
				return new ContainerCrate( player, crate );
			}
		}
		if( ID == EnumGui.GENERAL.getGuiID() )
		{
			final TileEntity tileEntity = world.getTileEntity( new BlockPos( x, y, z ) );
			if( tileEntity instanceof TileEntityContainer )
			{
				final TileEntityContainer chest = (TileEntityContainer)tileEntity;
				return new ContainerBetterStorage( player, chest );
			}
		}
		return null;
	}

	@Override
	public Object getClientGuiElement( int ID, EntityPlayer player, World world, int x, int y, int z )
	{
		if( ID == EnumGui.KEYRING.getGuiID() )
		{
			final ItemStack itemStack = player.getHeldItemMainhand();
			if( itemStack.getItem() != BetterStorageItems.keyring )
				return null;
			return new GuiBetterStorage( new ContainerKeyring( player, ModInfo.containerKeyring, player.inventory.currentItem ) );
		}
		if( ID == EnumGui.CRATE.getGuiID() )
		{
			final TileEntity tileEntity = world.getTileEntity( new BlockPos( x, y, z ) );
			if( tileEntity instanceof TileEntityCrate )
			{
				final TileEntityCrate crate = (TileEntityCrate)tileEntity;
				return new GuiCrate( crate, new ContainerCrate( player, crate ) );
			}
		}
		if( ID == EnumGui.GENERAL.getGuiID() )
		{
			final TileEntity tileEntity = world.getTileEntity( new BlockPos( x, y, z ) );
			if( tileEntity instanceof TileEntityContainer )
			{
				final TileEntityContainer chest = (TileEntityContainer)tileEntity;
				return new GuiBetterStorage( new ContainerBetterStorage( player, chest ) );
			}
		}
		return null;
	}

	public static enum EnumGui implements IStringSerializable
	{
		//@formatter:off
		KEYRING( 0, "keyring" ),
		CRATE(   1, "crate" ),
		GENERAL( 2, "general" );
		//@formatter:on

		private final int				guiID;
		private final String			name;

		private static final EnumGui[]	ID_LOOKUP	= new EnumGui[values().length];

		public int getGuiID()
		{
			return guiID;
		}

		@Override
		public String toString()
		{
			return name;
		}

		public static EnumGui byID( int id )
		{
			if( id < 0 || id >= ID_LOOKUP.length )
				id = 0;

			return ID_LOOKUP[id];
		}

		@Override
		public String getName()
		{
			return name;
		}

		private EnumGui( int guiID, String name )
		{
			this.guiID = guiID;
			this.name = name;
		}

		static
		{
			for( final EnumGui gui : values() )
				ID_LOOKUP[gui.getGuiID()] = gui;
		}
	}
}
