package io.github.tehstoneman.betterstorage.client.gui;

import java.util.List;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.client.renderer.Resources;
import io.github.tehstoneman.betterstorage.common.inventory.ContainerCrate;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class GuiCrate extends ContainerScreen< ContainerCrate >
{
	private final int	rows;
	private final int	offsetY;

	public GuiCrate( ContainerCrate container, PlayerInventory playerInventory, ITextComponent title )
	{
		super( container, playerInventory, title );

		rows = container.getRows();
		ySize = 114 + rows * 18;
		offsetY = 17 + rows * 18;

		// GUI label co-ordinates
		field_238742_p_ = 8;
		field_238743_q_ = 6;
		field_238744_r_ = 8;
		field_238745_s_ = offsetY + 3;
	}

	@Override
	// public void render( int mouseX, int mouseY, float partialTicks )
	public void func_230430_a_( MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks )
	{
		// renderBackground();
		func_230446_a_( matrixStack );
		super.func_230430_a_( matrixStack, mouseX, mouseY, partialTicks );

		if( mouseX >= guiLeft + 115 && mouseX < guiLeft + 169 && mouseY >= guiTop + 7 && mouseY < guiTop + 13 )
		{
			final ITextComponent toolTip = new TranslationTextComponent( ModInfo.CONTAINER_CAPACITY, container.getVolume() );
			final List< ITextComponent > list1 = Lists.newArrayList( toolTip );
			func_243308_b( matrixStack, list1, mouseX, mouseY );
			// this.renderTooltip( toolTip.getFormattedText(), mouseX, mouseY );
		}
		else
			// renderHoveredToolTip( mouseX, mouseY );
			func_230459_a_( matrixStack, mouseX, mouseY );
	}

	@SuppressWarnings( "deprecation" )
	@Override
	// protected void drawGuiContainerBackgroundLayer( float partialTicks, int x, int y )
	protected void func_230450_a_( MatrixStack matrixStack, float partialTicks, int x, int y )
	{
		RenderSystem.color4f( 1.0F, 1.0F, 1.0F, 1.0F );
		// minecraft.getTextureManager().bindTexture( Resources.CONTAINER_CRATE );
		field_230706_i_.getTextureManager().bindTexture( Resources.CONTAINER_CRATE );

		func_238474_b_( matrixStack, guiLeft, guiTop, 0, 0, xSize, offsetY );
		func_238474_b_( matrixStack, guiLeft, guiTop + offsetY, 0, 126, xSize, 96 );

		// blit( guiLeft, guiTop, 0, 0, xSize, offsetY );
		// blit( guiLeft, guiTop + offsetY, 0, 126, xSize, 96 );

		final int volume = container.getVolume();
		if( volume > 0.0 )
			func_238474_b_( matrixStack, guiLeft + 115, guiTop + 7, 176, 0, (int)( 54 * ( volume / 100.0 ) ), 6 );
	}
}
