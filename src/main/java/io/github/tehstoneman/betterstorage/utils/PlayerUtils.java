package io.github.tehstoneman.betterstorage.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class PlayerUtils
{
	private PlayerUtils()
	{}

	@SideOnly( Side.CLIENT )
	public static EntityPlayer getLocalPlayer()
	{
		return Minecraft.getMinecraft().player;
	}

	public static void openGui( EntityPlayer pl, String name, int columns, int rows, String title, Container container )
	{
		final EntityPlayerMP player = (EntityPlayerMP)pl;
		if( title == null )
			title = "";

		player.closeContainer();
		player.getNextWindowId();

		// BetterStorage.networkChannel.sendTo(new PacketOpenGui(player.currentWindowId, name, columns, rows, title), player);

		player.openContainer = container;
		player.openContainer.windowId = player.currentWindowId;
		// player.openContainer.addCraftingToCrafters(player);
	}

	/*
	 * @SideOnly( Side.CLIENT )
	 * public static void openGui( EntityPlayer player, String name, int columns, int rows, String title )
	 * {
	 * final GuiScreen gui = createGuiFromName( player, name, columns, rows, title );
	 * Minecraft.getMinecraft().displayGuiScreen( gui );
	 * }
	 */

	/*
	 * @SideOnly( Side.CLIENT )
	 * private static GuiScreen createGuiFromName( EntityPlayer player, String name, int columns, int rows, String title )
	 * {
	 * final boolean localized = !title.isEmpty();
	 * if( !localized )
	 * title = name;
	 *
	 * if( name.equals( ModInfo.containerKeyring ) )
	 * return new GuiBetterStorage( new ContainerKeyring( player, title, columns ) );
	 * else
	 * if( name.startsWith( Constants.containerThaumiumChest ) )
	 * return new GuiThaumiumChest( player, columns, rows, title, localized );
	 * else
	 * if( name.equals( Constants.containerCardboardBox ) )
	 * return new GuiBetterStorage( player, columns, rows,
	 * new InventoryWrapper( new InventoryCardboardBox( new ItemStack[columns * rows] ), title, localized ) );
	 * else
	 * if( name.equals( Constants.containerCraftingStation ) )
	 * return new GuiCraftingStation( player, title, localized );
	 * else
	 *
	 * return new GuiBetterStorage( player, columns, rows, title, localized );
	 * }
	 */
}
