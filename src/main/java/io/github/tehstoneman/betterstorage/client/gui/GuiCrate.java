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
		titleX = 8;
		titleY = 6;
		playerInventoryTitleX = 8;
		playerInventoryTitleY = offsetY + 3;
	}

	@Override
	public void render( MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks )
	{
		renderBackground( matrixStack );
		super.render( matrixStack, mouseX, mouseY, partialTicks );

		if( mouseX >= guiLeft + 115 && mouseX < guiLeft + 169 && mouseY >= guiTop + 7 && mouseY < guiTop + 13 )
		{
			final ITextComponent toolTip = new TranslationTextComponent( ModInfo.CONTAINER_CAPACITY, container.getVolume() );
			final List< ITextComponent > list1 = Lists.newArrayList( toolTip );
			func_243308_b( matrixStack, list1, mouseX, mouseY );
			// this.renderTooltip( toolTip.getFormattedText(), mouseX, mouseY );
		}
		else
			renderHoveredTooltip( matrixStack, mouseX, mouseY );
	}

	@SuppressWarnings( "deprecation" )
	@Override
	protected void drawGuiContainerBackgroundLayer( MatrixStack matrixStack, float partialTicks, int x, int y )
	{
		RenderSystem.color4f( 1.0F, 1.0F, 1.0F, 1.0F );
		minecraft.getTextureManager().bindTexture( Resources.CONTAINER_CRATE );

		blit( matrixStack, guiLeft, guiTop, 0, 0, xSize, offsetY );
		blit( matrixStack, guiLeft, guiTop + offsetY, 0, 126, xSize, 96 );

		final int volume = container.getVolume();
		if( volume > 0.0 )
			blit( matrixStack, guiLeft + 115, guiTop + 7, 176, 0, (int)( 54 * ( volume / 100.0 ) ), 6 );
	}
}
