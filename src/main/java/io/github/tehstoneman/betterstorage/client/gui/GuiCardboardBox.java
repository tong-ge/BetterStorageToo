package io.github.tehstoneman.betterstorage.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import io.github.tehstoneman.betterstorage.client.renderer.Resources;
import io.github.tehstoneman.betterstorage.common.inventory.ContainerCardboardBox;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class GuiCardboardBox extends ContainerScreen< ContainerCardboardBox >
{
	private final int	columns;
	private final int	rows;
	private final int	offsetX;
	private final int	offsetY;

	public GuiCardboardBox( ContainerCardboardBox container, PlayerInventory playerInventory, ITextComponent title )
	{
		super( container, playerInventory, title );

		columns = container.getColumns();
		rows = container.getRows();

		xSize = Math.max( 14 + columns * 18, 176 );
		ySize = 114 + rows * 18;

		offsetX = Math.max( ( 176 - xSize ) / 2, 0 );
		offsetY = 17 + rows * 18;

		// GUI label co-ordinates
		titleX = 8;
		titleY = 6;
		playerInventoryTitleX = offsetX + 8;
		playerInventoryTitleY = offsetY + 3;
	}

	protected ResourceLocation getResource()
	{
		if( columns <= 9 )
			return Resources.CONTAINER_GENERIC;
		else
			return Resources.CONTAINER_EXPANDABLE;
	}

	@Override
	public void render( MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks )
	{
		renderBackground( matrixStack );
		super.render( matrixStack, mouseX, mouseY, partialTicks );
		renderHoveredTooltip( matrixStack, mouseX, mouseY );
	}

	@SuppressWarnings( "deprecation" )
	@Override
	protected void drawGuiContainerBackgroundLayer( MatrixStack matrixStack, float partialTicks, int x, int y )
	{
		RenderSystem.color4f( 1.0F, 1.0F, 1.0F, 1.0F );
		minecraft.getTextureManager().bindTexture( getResource() );

		blit( matrixStack, guiLeft, guiTop, 0, 0, xSize, offsetY );
		blit( matrixStack, guiLeft, guiTop + offsetY, 0, 126, xSize, 96 );
	}
}
