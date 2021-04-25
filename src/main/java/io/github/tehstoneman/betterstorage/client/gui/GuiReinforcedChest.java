package io.github.tehstoneman.betterstorage.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import io.github.tehstoneman.betterstorage.client.renderer.Resources;
import io.github.tehstoneman.betterstorage.common.inventory.ReinforcedChestContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class GuiReinforcedChest extends ContainerScreen< ReinforcedChestContainer >
{
	private final int	columns, rows;
	private final int	xSlice1, xSlice2, xSlice3, xSlice4;
	private final int	offsetY;

	public GuiReinforcedChest( ReinforcedChestContainer container, PlayerInventory playerInventory, ITextComponent title )
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
		titleX = 8;
		titleY = 6;
		playerInventoryTitleX = xSlice2 + 8;
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

		// Chest inventory
		blit( matrixStack, guiLeft, guiTop, 0, 0, xSlice1, offsetY );
		blit( matrixStack, guiLeft + xSlice1, guiTop, 241, 0, 7, offsetY );

		// Player inventory
		blit( matrixStack, guiLeft, guiTop + offsetY, 0, 125, xSlice2, 17 );
		blit( matrixStack, guiLeft + xSlice2, guiTop + offsetY, 36, 125, 176, 97 );
		blit( matrixStack, guiLeft + xSlice3, guiTop + offsetY, xSlice4, 125, xSlice2, 17 );
	}
}
