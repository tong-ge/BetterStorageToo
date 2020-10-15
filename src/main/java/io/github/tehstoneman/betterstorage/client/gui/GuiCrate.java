package io.github.tehstoneman.betterstorage.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;

import io.github.tehstoneman.betterstorage.common.inventory.ContainerCrate;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class GuiCrate extends ContainerScreen< ContainerCrate >
{
	// private final int rows;
	// private final int offsetY;

	public GuiCrate( ContainerCrate container, PlayerInventory playerInventory, ITextComponent title )
	{
		super( container, playerInventory, title );

		// rows = container.getRows();
		// ySize = 114 + rows * 18;
		// offsetY = 17 + rows * 18;
	}

	/*
	 * @Override
	 * public void render( int mouseX, int mouseY, float partialTicks )
	 * {
	 * renderBackground();
	 * super.render( mouseX, mouseY, partialTicks );
	 *
	 * if( mouseX >= guiLeft + 115 && mouseX < guiLeft + 169 && mouseY >= guiTop + 7 && mouseY < guiTop + 13 )
	 * {
	 * final ITextComponent toolTip = new TranslationTextComponent( ModInfo.CONTAINER_CAPACITY, container.getVolume() );
	 * this.renderTooltip( toolTip.getFormattedText(), mouseX, mouseY );
	 * }
	 * else
	 * renderHoveredToolTip( mouseX, mouseY );
	 * }
	 */

	/*
	 * @Override
	 * protected void drawGuiContainerForegroundLayer( int par1, int par2 )
	 * {
	 * font.drawString( title.getFormattedText(), 8, 6, 0x404040 );
	 * font.drawString( playerInventory.getDisplayName().getFormattedText(), 8, offsetY + 2, 0x404040 );
	 * }
	 */

	/*
	 * @Override
	 * protected void drawGuiContainerBackgroundLayer( float partialTicks, int mouseX, int mouseY )
	 * {
	 * minecraft.getTextureManager().bindTexture( Resources.CONTAINER_CRATE );
	 * GlStateManager.color4f( 1.0F, 1.0F, 1.0F, 1.0F );
	 *
	 * blit( guiLeft, guiTop, 0, 0, xSize, offsetY );
	 * blit( guiLeft, guiTop + offsetY, 0, 126, xSize, 96 );
	 *
	 * final int volume = container.getVolume();
	 * if( volume > 0.0 )
	 * blit( guiLeft + 115, guiTop + 7, 176, 0, (int)( 54 * ( volume / 100.0 ) ), 6 );
	 * }
	 */

	@Override
	protected void func_230450_a_( MatrixStack p_230450_1_, float p_230450_2_, int p_230450_3_, int p_230450_4_ )
	{
		// TODO Auto-generated method stub

	}
}
