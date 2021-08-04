package io.github.tehstoneman.betterstorage.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;

import io.github.tehstoneman.betterstorage.client.renderer.Resources;
import io.github.tehstoneman.betterstorage.common.inventory.ContainerCardboardBox;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
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

		imageHeight = Math.max( 14 + columns * 18, 176 );
		imageWidth = 114 + rows * 18;

		offsetX = Math.max( ( 176 - imageHeight ) / 2, 0 );
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
	public void render( MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks )
	{
		renderBackground( matrixStack );
		super.render( matrixStack, mouseX, mouseY, partialTicks );
		renderTooltip( matrixStack, mouseX, mouseY );
	}

	@Override
	protected void renderBg( MatrixStack matrixStack, float partialTicks, int x, int y )
	{
		minecraft.getTextureManager().bind( getResource() );

		blit( matrixStack, getGuiLeft(), getGuiTop(), 0, 0, imageHeight, offsetY );
		blit( matrixStack, getGuiLeft(), getGuiTop() + offsetY, 0, 126, imageHeight, 96 );
	}
}
