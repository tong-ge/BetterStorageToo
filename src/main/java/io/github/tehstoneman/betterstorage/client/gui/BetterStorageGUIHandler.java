package io.github.tehstoneman.betterstorage.client.gui;

import io.github.tehstoneman.betterstorage.container.ContainerCrate;
import io.github.tehstoneman.betterstorage.container.ContainerKeyring;
import io.github.tehstoneman.betterstorage.content.BetterStorageItems;
import io.github.tehstoneman.betterstorage.misc.Constants;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class BetterStorageGUIHandler implements IGuiHandler
{
	@Override
	public Object getServerGuiElement( int ID, EntityPlayer player, World world, int x, int y, int z )
	{
		if( ID == EnumGui.KEYRING.getGuiID() )
		{
			ItemStack itemStack = player.getHeldItemMainhand();
			if( itemStack.getItem() != BetterStorageItems.keyring)
				return null;
			return new ContainerKeyring( player, Constants.containerKeyring, player.inventory.currentItem );
		}
		/*if( ID == EnumGui.CRATE.getGuiID() )
		{
			ItemStack itemStack = player.getHeldItemMainhand();
			if( itemStack.getItem() != BetterStorageItems.keyring)
				return null;
			return new ContainerCrate( player, Constants.containerCrate, player.inventory.currentItem );
		}*/
		return null;
	}

	@Override
	public Object getClientGuiElement( int ID, EntityPlayer player, World world, int x, int y, int z )
	{
		if( ID == EnumGui.KEYRING.getGuiID() )
		{
			ItemStack itemStack = player.getHeldItemMainhand();
			if( itemStack.getItem() != BetterStorageItems.keyring)
				return null;
			return new GuiBetterStorage( new ContainerKeyring( player, Constants.containerKeyring, player.inventory.currentItem ) );
		}
		/*if( ID == EnumGui.CRATE.getGuiID() )
		{
			ItemStack itemStack = player.getHeldItemMainhand();
			if( itemStack.getItem() != BetterStorageItems.keyring)
				return null;
			return new GuiCrate( player, rows, Constants.containerCrate, localized );
		}*/
		return null;
	}

	public static enum EnumGui implements IStringSerializable
	{
		KEYRING( 0, "keyring" ), CRATE( 1, "crate" ), GREEN( 2, "green" ), YELLOW( 3, "yellow" );

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
