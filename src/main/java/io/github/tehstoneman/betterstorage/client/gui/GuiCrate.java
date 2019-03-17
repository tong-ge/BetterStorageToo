package io.github.tehstoneman.betterstorage.client.gui;

import io.github.tehstoneman.betterstorage.common.inventory.ContainerCrate;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityCrate;
import net.minecraft.client.gui.inventory.GuiContainer;

//@SideOnly( Side.CLIENT )
public class GuiCrate extends GuiContainer
{
	private final ContainerCrate	containerCrate;
	private final int				yOffset;

	public GuiCrate( TileEntityCrate tileEntityCrate, ContainerCrate containerCrate )
	{
		super( containerCrate );
		this.containerCrate = containerCrate;
		final int rows = containerCrate.indexHotbar / 9;
		yOffset = 17 + rows * 18;
		ySize = yOffset + 97;
	}

	/*
	 * @Override
	 * public void drawScreen( int mouseX, int mouseY, float partialTicks )
	 * {
	 * drawDefaultBackground();
	 * super.drawScreen( mouseX, mouseY, partialTicks );
	 * 
	 * if( mouseX >= guiLeft + 115 && mouseX < guiLeft + 169 && mouseY >= guiTop + 7 && mouseY < guiTop + 13 )
	 * {
	 * final int volume = containerCrate.getVolume();
	 * final List< String > hoveringText = new ArrayList<>();
	 * hoveringText.add( volume + BetterStorage.proxy.localize( ModInfo.containerCapacity ) );
	 * drawHoveringText( hoveringText, mouseX, mouseY, fontRenderer );
	 * }
	 * else
	 * renderHoveredToolTip( mouseX, mouseY );
	 * }
	 */

	@Override
	protected void drawGuiContainerForegroundLayer( int mouseX, int mouseY )
	{
		// fontRenderer.drawString( BetterStorage.proxy.localize( ModInfo.containerCrate ), 8, 6, 0x404040 );
		// fontRenderer.drawString( BetterStorage.proxy.localize( "container.inventory" ), 8, yOffset + 3, 0x404040 );
	}

	@Override
	protected void drawGuiContainerBackgroundLayer( float partialTicks, int mouseX, int mouseY )
	{
		// mc.renderEngine.bindTexture( new ResourceLocation( ModInfo.modId, "textures/gui/crate.png" ) );

		drawTexturedModalRect( guiLeft, guiTop, 0, 0, xSize, yOffset );
		drawTexturedModalRect( guiLeft, guiTop + yOffset, 0, 125, xSize, 97 );

		final int volume = containerCrate.getVolume();
		if( volume > 0.0 )
			drawTexturedModalRect( guiLeft + 115, guiTop + 7, 176, 0, (int)( 54 * ( volume / 100.0 ) ), 6 );
	}
}
