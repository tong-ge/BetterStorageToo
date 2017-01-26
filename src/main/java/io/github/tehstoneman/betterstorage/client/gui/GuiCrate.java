package io.github.tehstoneman.betterstorage.client.gui;

import java.util.logging.Logger;

import io.github.tehstoneman.betterstorage.container.ContainerCrate;
import io.github.tehstoneman.betterstorage.misc.Constants;
import io.github.tehstoneman.betterstorage.misc.Resources;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly( Side.CLIENT )
public class GuiCrate extends GuiBetterStorage
{
	private final ContainerCrate containerCrate;

	public GuiCrate( EntityPlayer player, int rows, String title, boolean localized )
	{
		super( new ContainerCrate( player, rows, title, localized ) );
		containerCrate = (ContainerCrate)container;
	}

	@Override
	protected ResourceLocation getResource()
	{
		return Resources.containerCrate;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer( float var1, int var2, int var3 )
	{
		super.drawGuiContainerBackgroundLayer( var1, var2, var3 );
		final int x = ( width - xSize ) / 2;
		final int y = ( height - ySize ) / 2;
		drawTexturedModalRect( x + 115, y + 7, 176, 0, containerCrate.fullness * 54 / 255, 6 );
	}
}
