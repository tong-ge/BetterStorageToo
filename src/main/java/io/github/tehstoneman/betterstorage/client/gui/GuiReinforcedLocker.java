package io.github.tehstoneman.betterstorage.client.gui;

import io.github.tehstoneman.betterstorage.client.renderer.Resources;
import io.github.tehstoneman.betterstorage.common.inventory.ContainerReinforcedLocker;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn( Dist.CLIENT )
public class GuiReinforcedLocker// extends AbstractContainerScreen< ContainerReinforcedLocker >
{
	/*
	 * private final int columns, rows;
	 * private final int xSlice1, xSlice2, xSlice3, xSlice4;
	 * private final int offsetY;
	 * 
	 * public GuiReinforcedLocker( ContainerReinforcedLocker container, Inventory playerInventory, Component title )
	 * {
	 * super( container, playerInventory, title );
	 * 
	 * columns = container.getColumns();
	 * rows = container.getRows();
	 * 
	 * imageWidth = Math.max( 14 + columns * 18, 176 );
	 * imageHeight = 114 + rows * 18;
	 * 
	 * // Calculate horizontal texture slices
	 * xSlice1 = columns * 18 + 7;
	 * xSlice2 = ( imageWidth - 176 ) / 2;
	 * xSlice3 = imageWidth - xSlice2;
	 * xSlice4 = 248 - xSlice2;
	 * 
	 * // Calculate vertical texture slices
	 * offsetY = rows * 18 + 17;
	 * 
	 * // GUI label co-ordinates
	 * titleLabelX = 8;
	 * titleLabelY = 6;
	 * inventoryLabelX = xSlice2 + 8;
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
	 * // Chest inventory
	 * blit( matrixStack, getGuiLeft(), getGuiTop(), 0, 0, xSlice1, offsetY );
	 * blit( matrixStack, getGuiLeft() + xSlice1, getGuiTop(), 241, 0, 7, offsetY );
	 * 
	 * // Player inventory
	 * blit( matrixStack, getGuiLeft(), getGuiTop() + offsetY, 0, 125, xSlice2, 17 );
	 * blit( matrixStack, getGuiLeft() + xSlice2, getGuiTop() + offsetY, 36, 125, 176, 97 );
	 * blit( matrixStack, getGuiLeft() + xSlice3, getGuiTop() + offsetY, xSlice4, 125, xSlice2, 17 );
	 * }
	 */
}
