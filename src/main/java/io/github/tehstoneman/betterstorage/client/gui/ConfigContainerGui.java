package io.github.tehstoneman.betterstorage.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import io.github.tehstoneman.betterstorage.client.renderer.Resources;
import io.github.tehstoneman.betterstorage.common.inventory.ConfigContainer;
import io.github.tehstoneman.betterstorage.common.inventory.ConfigContainer.ConfigSlot;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ConfigContainerGui extends ContainerScreen< ConfigContainer >
{
	public ConfigContainerGui( ConfigContainer screenContainer, PlayerInventory inv, ITextComponent titleIn )
	{
		super( screenContainer, inv, titleIn );
		ySize = 132;
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
		minecraft.getTextureManager().bindTexture( Resources.CONTAINER_CONFIG );

		blit( matrixStack, guiLeft, guiTop, 0, 0, xSize, ySize );
		for( int i = 0; i < container.indexHotbar; i++ )
		{
			final ConfigSlot slot = (ConfigSlot)container.getSlot( i );
			if( !slot.getHasStack() )
				blit( matrixStack, guiLeft + slot.xPos, guiTop + slot.yPos, slot.getIconX() + 1, slot.getIconY() + 1, 16, 16 );
		}
	}
}
