package io.github.tehstoneman.betterstorage.client.gui;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class BetterStorageGUIHandler implements IGuiHandler
{
	/*
	 * @Override
	 * public Object getServerGuiElement( int ID, EntityPlayer player, World world, int x, int y, int z )
	 * {
	 * if( ID == EnumGui.KEYRING.getGuiID() )
	 * {
	 * final ItemStack itemStack = player.getHeldItemMainhand();
	 * if( itemStack.getItem() != BetterStorageItems.KEYRING )
	 * return null;
	 * return new ContainerKeyring( player, itemStack, player.inventory.currentItem );
	 * }
	 * if( ID == EnumGui.CRATE.getGuiID() )
	 * {
	 * final TileEntity tileEntity = world.getTileEntity( new BlockPos( x, y, z ) );
	 * if( tileEntity instanceof TileEntityCrate )
	 * {
	 * final TileEntityCrate crate = (TileEntityCrate)tileEntity;
	 * return new ContainerCrate( player, crate );
	 * }
	 * }
	 * if( ID == EnumGui.GENERAL.getGuiID() )
	 * {
	 * final TileEntity tileEntity = world.getTileEntity( new BlockPos( x, y, z ) );
	 * if( tileEntity instanceof TileEntityContainer )
	 * {
	 * final TileEntityContainer chest = (TileEntityContainer)tileEntity;
	 * return new ContainerBetterStorage( player, chest );
	 * }
	 * }
	 * if( ID == EnumGui.CRAFTING.getGuiID() )
	 * {
	 * final TileEntity tileEntity = world.getTileEntity( new BlockPos( x, y, z ) );
	 * if( tileEntity instanceof TileEntityCraftingStation )
	 * {
	 * final TileEntityCraftingStation station = (TileEntityCraftingStation)tileEntity;
	 * return new ContainerCraftingStation( player, station );
	 * }
	 * }
	 * return null;
	 * }
	 */

	/*
	 * @Override
	 * public Object getClientGuiElement( int ID, EntityPlayer player, World world, int x, int y, int z )
	 * {
	 * if( ID == EnumGui.KEYRING.getGuiID() )
	 * {
	 * final ItemStack itemStack = player.getHeldItemMainhand();
	 * if( itemStack.getItem() != BetterStorageItems.KEYRING )
	 * return null;
	 * return new GuiKeyring( new ContainerKeyring( player, itemStack, player.inventory.currentItem ) );
	 * }
	 * if( ID == EnumGui.CRATE.getGuiID() )
	 * {
	 * final TileEntity tileEntity = world.getTileEntity( new BlockPos( x, y, z ) );
	 * if( tileEntity instanceof TileEntityCrate )
	 * {
	 * final TileEntityCrate crate = (TileEntityCrate)tileEntity;
	 * return new GuiCrate( crate, new ContainerCrate( player, crate ) );
	 * }
	 * }
	 * if( ID == EnumGui.GENERAL.getGuiID() )
	 * {
	 * final TileEntity tileEntity = world.getTileEntity( new BlockPos( x, y, z ) );
	 * if( tileEntity instanceof TileEntityContainer )
	 * {
	 * final TileEntityContainer chest = (TileEntityContainer)tileEntity;
	 * return new GuiBetterStorage( new ContainerBetterStorage( player, chest ) );
	 * }
	 * }
	 * if( ID == EnumGui.CRAFTING.getGuiID() )
	 * {
	 * final TileEntity tileEntity = world.getTileEntity( new BlockPos( x, y, z ) );
	 * if( tileEntity instanceof TileEntityCraftingStation )
	 * {
	 * final TileEntityCraftingStation station = (TileEntityCraftingStation)tileEntity;
	 * return new GuiCraftingStation( new ContainerCraftingStation( player, station ) );
	 * }
	 * }
	 * return null;
	 * }
	 */

	public static enum EnumGui implements IStringSerializable
	{
		//@formatter:off
		KEYRING(  0, "keyring" ),
		CRATE(    1, "crate" ),
		GENERAL(  2, "general" ),
		CRAFTING( 3, "crafting" );
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

	@Override
	public Object getServerGuiElement( int ID, PlayerEntity player, World world, int x, int y, int z )
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getClientGuiElement( int ID, PlayerEntity player, World world, int x, int y, int z )
	{
		// TODO Auto-generated method stub
		return null;
	}
}
