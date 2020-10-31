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

		xSize = Math.max( 14 + columns * 18, 176 );
		ySize = 114 + rows * 18;

		offsetX = Math.max( ( 176 - xSize ) / 2, 0 );
		offsetY = 17 + rows * 18;

		// GUI label co-ordinates
		field_238742_p_ = 8;
		field_238743_q_ = 6;
		field_238744_r_ = offsetX + 8;
		field_238745_s_ = offsetY + 3;
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
	public void func_230430_a_( MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks )
	{
		// renderBackground();
		func_230446_a_( matrixStack );
		// super.render( mouseX, mouseY, partialTicks );
		super.func_230430_a_( matrixStack, mouseX, mouseY, partialTicks );
		// renderHoveredToolTip( mouseX, mouseY );
		func_230459_a_( matrixStack, mouseX, mouseY );
	}

	@SuppressWarnings( "deprecation" )
	@Override
	// protected void drawGuiContainerBackgroundLayer( float partialTicks, int x, int y )
	protected void func_230450_a_( MatrixStack matrixStack, float partialTicks, int x, int y )
	{
		// GlStateManager.color4f( 1.0F, 1.0F, 1.0F, 1.0F );
		RenderSystem.color4f( 1.0F, 1.0F, 1.0F, 1.0F );
		// minecraft.getTextureManager().bindTexture( getResource() );
		field_230706_i_.getTextureManager().bindTexture( getResource() );

		// Chest inventory
		func_238474_b_( matrixStack, guiLeft, guiTop, 0, 0, xSize, offsetY );
		func_238474_b_( matrixStack, guiLeft, guiTop + offsetY, 0, 126, xSize, 96 );

		// blit( guiLeft, guiTop, 0, 0, xSize, offsetY );
		// blit( guiLeft, guiTop + offsetY, 0, 126, xSize, 96 );
	}
}
