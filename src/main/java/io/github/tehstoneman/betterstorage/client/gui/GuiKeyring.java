package io.github.tehstoneman.betterstorage.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;

import io.github.tehstoneman.betterstorage.client.renderer.Resources;
import io.github.tehstoneman.betterstorage.common.inventory.ContainerKeyring;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn( Dist.CLIENT )
public class GuiKeyring extends ContainerScreen< ContainerKeyring >
{
	public GuiKeyring( ContainerKeyring container, PlayerInventory playerInventory, ITextComponent title )
	{
		super( container, playerInventory, title );
		height = 131;

		// GUI label co-ordinates
		titleLabelX = 8;
		titleLabelY = 6;
		inventoryLabelX = 8;
		inventoryLabelY = 38;
	}

	@Override
	public void render( MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks )
	{
		renderBackground( matrixStack );
		super.render( matrixStack, mouseX, mouseY, partialTicks );
		renderTooltip( matrixStack, mouseX, mouseY );
	}

	@Override
	protected void renderBg( MatrixStack matrixStack, float partialTicks, int x, int y )
	{
		minecraft.getTextureManager().bind( Resources.CONTAINER_GENERIC );

		blit( matrixStack, getGuiLeft(), getGuiTop(), 0, 0, getXSize(), 35 );
		blit( matrixStack, getGuiLeft(), getGuiTop() + 35, 0, 125, getXSize(), 97 );
	}
}