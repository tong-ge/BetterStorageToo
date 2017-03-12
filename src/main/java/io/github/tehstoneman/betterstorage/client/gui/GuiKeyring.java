package io.github.tehstoneman.betterstorage.client.gui;

import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.container.ContainerKeyring;
import io.github.tehstoneman.betterstorage.misc.Resources;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly( Side.CLIENT )
public class GuiKeyring extends GuiContainer
{
	public final ContainerKeyring	container;

	public GuiKeyring( ContainerKeyring containerKeyring )
	{
		super( containerKeyring );

		container = containerKeyring;

		ySize = 131;
	}

	@Override
	protected void drawGuiContainerForegroundLayer( int par1, int par2 )
	{
		fontRendererObj.drawString( I18n.format( ModInfo.containerKeyring ), 8, 6, 0x404040 );
		fontRendererObj.drawString( I18n.format( "container.inventory" ), 8 + ( xSize - 176 ) / 2, ySize - 94, 0x404040 );
	}

	@Override
	protected void drawGuiContainerBackgroundLayer( float partialTicks, int x, int y )
	{
		mc.renderEngine.bindTexture( new ResourceLocation( "textures/gui/container/generic_54.png" ) );
		GlStateManager.color( 1.0F, 1.0F, 1.0F, 1.0F );

		drawTexturedModalRect( guiLeft, guiTop, 0, 0, xSize, 35 );
		drawTexturedModalRect( guiLeft, guiTop + 35, 0, 125, xSize, 97 );
	}
}
