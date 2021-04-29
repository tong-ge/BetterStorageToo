package io.github.tehstoneman.betterstorage.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import io.github.tehstoneman.betterstorage.client.renderer.Resources;
import io.github.tehstoneman.betterstorage.common.inventory.ContainerKeyring;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class GuiKeyring extends ContainerScreen< ContainerKeyring >
{
	public GuiKeyring( ContainerKeyring container, PlayerInventory playerInventory, ITextComponent title )
	{
		super( container, playerInventory, title );
		ySize = 131;

		// GUI label co-ordinates
		titleX = 8;
		titleY = 6;
		playerInventoryTitleX = 8;
		playerInventoryTitleY = 38;
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
		renderHoveredTooltip( matrixStack, mouseX, mouseY );
	}

	@Override
	// protected void drawGuiContainerBackgroundLayer( float partialTicks, int x, int y )
	protected void drawGuiContainerBackgroundLayer( MatrixStack matrixStack, float partialTicks, int x, int y )
	{
		// GlStateManager.color4f( 1.0F, 1.0F, 1.0F, 1.0F );
		RenderSystem.color4f( 1.0F, 1.0F, 1.0F, 1.0F );
		// minecraft.getTextureManager().bindTexture( getResource() );
		minecraft.getTextureManager().bindTexture( Resources.CONTAINER_GENERIC );

		blit( matrixStack, guiLeft, guiTop, 0, 0, xSize, 35 );
		blit( matrixStack, guiLeft, guiTop + 35, 0, 125, xSize, 97 );

		// blit( guiLeft, guiTop, 0, 0, xSize, 35 );
		// blit( guiLeft, guiTop + 35, 0, 125, xSize, 97 );
	}
}