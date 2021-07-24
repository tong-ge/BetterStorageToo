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
		height = 114 + rows * 18;
		offsetY = 17 + rows * 18;

		// GUI label co-ordinates
		titleLabelX = 8;
		titleLabelY = 6;
		inventoryLabelX = 8;
		inventoryLabelY = offsetY + 3;
	}

	@Override
	// public void render( int mouseX, int mouseY, float partialTicks )
	public void render( MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks )
	{
		// renderBackground();
		renderBackground( matrixStack );
		super.render( matrixStack, mouseX, mouseY, partialTicks );

		if( mouseX >= getGuiLeft() + 115 && mouseX < getGuiLeft() + 169 && mouseY >= getGuiTop() + 7 && mouseY < getGuiTop() + 13 )
		{
			final ITextComponent toolTip = new TranslationTextComponent( ModInfo.CONTAINER_CAPACITY, menu.getVolume() );
			final List< ITextComponent > list1 = Lists.newArrayList( toolTip );
			renderComponentTooltip( matrixStack, list1, mouseX, mouseY );
			// this.renderTooltip( toolTip.getFormattedText(), mouseX, mouseY );
		}
		else
			// renderHoveredToolTip( mouseX, mouseY );
			renderTooltip( matrixStack, mouseX, mouseY );
	}

	@SuppressWarnings( "deprecation" )
	@Override
	// protected void drawGuiContainerBackgroundLayer( float partialTicks, int x, int y )
	protected void renderBg( MatrixStack matrixStack, float partialTicks, int x, int y )
	{
		RenderSystem.color4f( 1.0F, 1.0F, 1.0F, 1.0F );
		// minecraft.getTextureManager().bindTexture( Resources.CONTAINER_CRATE );
		minecraft.getTextureManager().getTexture( Resources.CONTAINER_CRATE );

		blit( matrixStack, getGuiLeft(), getGuiTop(), 0, 0, getXSize(), offsetY );
		blit( matrixStack, getGuiLeft(), getGuiTop() + offsetY, 0, 126, getXSize(), 96 );

		// blit( guiLeft, guiTop, 0, 0, xSize, offsetY );
		// blit( guiLeft, guiTop + offsetY, 0, 126, xSize, 96 );

		final int volume = menu.getVolume();
		if( volume > 0.0 )
			blit( matrixStack, getGuiLeft() + 115, getGuiTop() + 7, 176, 0, (int)( 54 * ( volume / 100.0 ) ), 6 );
	}
}
