package io.github.tehstoneman.betterstorage.client.gui;

import io.github.tehstoneman.betterstorage.client.renderer.Resources;
import io.github.tehstoneman.betterstorage.common.inventory.ContainerLocker;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn( Dist.CLIENT )
public class GuiLocker// extends AbstractContainerScreen< ContainerLocker >
{
	/*
	 * private final int columns, rows;
	 * private final int offsetX;
	 * private final int offsetY;
	 * 
	 * public GuiLocker( ContainerLocker container, Inventory playerInventory, Component title )
	 * {
	 * super( container, playerInventory, title );
	 * 
	 * columns = container.getColumns();
	 * rows = container.getRows();
	 * 
	 * imageWidth = Math.max( 14 + columns * 18, 176 );
	 * imageHeight = 114 + rows * 18;
	 * 
	 * offsetX = Math.max( ( 176 - imageWidth ) / 2, 0 );
	 * offsetY = 17 + rows * 18;
	 * 
	 * // GUI label co-ordinates
	 * titleLabelX = 8;
	 * titleLabelY = 6;
	 * inventoryLabelX = offsetX + 8;
	 * inventoryLabelY = offsetY + 3;
	 * }
	 * 
	 * protected ResourceLocation getResource()
	 * {
	 * if( columns <= 9 )
	 * return Resources.CONTAINER_GENERIC;
	 * else
	 * return Resources.CONTAINER_EXPANDABLE;
	 * }
	 * 
	 * @Override
	 * public void render( PoseStack matrixStack, int mouseX, int mouseY, float partialTicks )
	 * {
	 * renderBackground( matrixStack );
	 * super.render( matrixStack, mouseX, mouseY, partialTicks );
	 * renderTooltip( matrixStack, mouseX, mouseY );
	 * }
	 * 
	 * @Override
	 * protected void renderBg( PoseStack matrixStack, float partialTicks, int x, int y )
	 * {
	 * minecraft.getTextureManager().getTexture( getResource() );
	 * 
	 * // Locker inventory
	 * blit( matrixStack, getGuiLeft(), getGuiTop(), 0, 0, imageWidth, offsetY );
	 * 
	 * // Player inventory
	 * blit( matrixStack, getGuiLeft(), getGuiTop() + offsetY, 0, 125, imageWidth, 97 );
	 * }
	 */
}
