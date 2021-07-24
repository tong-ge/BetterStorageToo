package io.github.tehstoneman.betterstorage.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import io.github.tehstoneman.betterstorage.client.renderer.Resources;
import io.github.tehstoneman.betterstorage.common.inventory.ContainerLocker;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class GuiLocker extends ContainerScreen< ContainerLocker >
{
	private final int	columns, rows;
	private final int	offsetX;
	private final int	offsetY;

	public GuiLocker( ContainerLocker container, PlayerInventory playerInventory, ITextComponent title )
	{
		super( container, playerInventory, title );

		columns = container.getColumns();
		rows = container.getRows();

		width = Math.max( 14 + columns * 18, 176 );
		height = 114 + rows * 18;

		offsetX = Math.max( ( 176 - width ) / 2, 0 );
		offsetY = 17 + rows * 18;

		// GUI label co-ordinates
		titleLabelX = 8;
		titleLabelY = 6;
		inventoryLabelX = offsetX + 8;
		inventoryLabelY = offsetY + 3;
	}

	protected ResourceLocation getResource()
	{
		if( columns <= 9 )
			return Resources.CONTAINER_GENERIC;
		else
			return Resources.CONTAINER_EXPANDABLE;
	}

	@Override
	// public void render( int mouseX, int mouseY, float partialTicks )
	public void render( MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks )
	{
		// renderBackground();
		renderBackground( matrixStack );
		// super.render( mouseX, mouseY, partialTicks );
		super.render( matrixStack, mouseX, mouseY, partialTicks );
		// renderHoveredToolTip( mouseX, mouseY );
		renderTooltip( matrixStack, mouseX, mouseY );
	}

	@SuppressWarnings( "deprecation" )
	@Override
	// protected void drawGuiContainerBackgroundLayer( float partialTicks, int x, int y )
	protected void renderBg( MatrixStack matrixStack, float partialTicks, int x, int y )
	{
		// GlStateManager.color4f( 1.0F, 1.0F, 1.0F, 1.0F );
		RenderSystem.color4f( 1.0F, 1.0F, 1.0F, 1.0F );
		// minecraft.getTextureManager().bindTexture( getResource() );
		minecraft.getTextureManager().getTexture( getResource() );

		// Chest inventory
		blit( matrixStack, getGuiLeft(), getGuiTop(), 0, 0, width, offsetY );
		blit( matrixStack, getGuiLeft(), getGuiTop() + offsetY, 0, 126, width, 96 );

		// blit( guiLeft, guiTop, 0, 0, xSize, offsetY );
		// blit( guiLeft, guiTop + offsetY, 0, 126, xSize, 96 );
	}
}
