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
		field_238742_p_ = 8;
		field_238743_q_ = 6;
		field_238744_r_ = 8;
		field_238745_s_ = 38;
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
		field_230706_i_.getTextureManager().bindTexture( Resources.CONTAINER_GENERIC );

		func_238474_b_( matrixStack, guiLeft, guiTop, 0, 0, xSize, 35 );
		func_238474_b_( matrixStack, guiLeft, guiTop + 35, 0, 125, xSize, 97 );

		// blit( guiLeft, guiTop, 0, 0, xSize, 35 );
		// blit( guiLeft, guiTop + 35, 0, 125, xSize, 97 );
	}
}