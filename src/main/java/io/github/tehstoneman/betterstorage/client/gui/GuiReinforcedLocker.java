package io.github.tehstoneman.betterstorage.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import io.github.tehstoneman.betterstorage.client.renderer.Resources;
import io.github.tehstoneman.betterstorage.common.inventory.ContainerReinforcedLocker;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class GuiReinforcedLocker extends ContainerScreen< ContainerReinforcedLocker >
{
	private final int	columns, rows;
	private final int	xSlice1, xSlice2, xSlice3, xSlice4;
	private final int	offsetY;

	public GuiReinforcedLocker( ContainerReinforcedLocker container, PlayerInventory playerInventory, ITextComponent title )
	{
		super( container, playerInventory, title );

		columns = container.getColumns();
		rows = container.getRows();

		xSize = Math.max( 14 + columns * 18, 176 );
		ySize = 114 + rows * 18;

		// Calculate horizontal texture slices
		xSlice1 = columns * 18 + 7;
		xSlice2 = ( xSize - 176 ) / 2;
		xSlice3 = xSize - xSlice2;
		xSlice4 = 248 - xSlice2;

		// Calculate vertical texture slices
		offsetY = rows * 18 + 17;

		// GUI label co-ordinates
		field_238742_p_ = 8;
		field_238743_q_ = 6;
		field_238744_r_ = xSlice2 + 8;
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
		// blit( guiLeft, guiTop, 0, 0, x1, y1 );
		// blit( guiLeft + x1, guiTop, 241, 0, 7, y1 );
		func_238474_b_( matrixStack, guiLeft, guiTop, 0, 0, xSlice1, offsetY );
		func_238474_b_( matrixStack, guiLeft + xSlice1, guiTop, 241, 0, 7, offsetY );

		// Player inventory
		// blit( guiLeft, guiTop + y1, 0, 125, x2, 17 );
		// blit( guiLeft + x2, guiTop + y1, 36, 125, 176, 97 );
		// blit( guiLeft + x3, guiTop + y1, x4, 125, x2, 17 );
		func_238474_b_( matrixStack, guiLeft, guiTop + offsetY, 0, 125, xSlice2, 17 );
		func_238474_b_( matrixStack, guiLeft + xSlice2, guiTop + offsetY, 36, 125, 176, 97 );
		func_238474_b_( matrixStack, guiLeft + xSlice3, guiTop + offsetY, xSlice4, 125, xSlice2, 17 );
	}
}
