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
	public void func_230430_a_( MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks )
	{
		func_230446_a_( matrixStack );
		super.func_230430_a_( matrixStack, mouseX, mouseY, partialTicks );
		func_230459_a_( matrixStack, mouseX, mouseY );
	}

	@SuppressWarnings( "deprecation" )
	@Override
	protected void func_230450_a_( MatrixStack matrixStack, float partialTicks, int x, int y )
	{
		RenderSystem.color4f( 1.0F, 1.0F, 1.0F, 1.0F );
		field_230706_i_.getTextureManager().bindTexture( Resources.CONTAINER_CONFIG );

		func_238474_b_( matrixStack, guiLeft, guiTop, 0, 0, xSize, ySize );
		for( int i = 0; i < container.indexHotbar; i++ )
		{
			final ConfigSlot slot = (ConfigSlot)container.getSlot( i );
			if( !slot.getHasStack() )
				func_238474_b_( matrixStack, guiLeft + slot.xPos, guiTop + slot.yPos, slot.getIconX() + 1, slot.getIconY() + 1, 16, 16 );
		}
	}
}
