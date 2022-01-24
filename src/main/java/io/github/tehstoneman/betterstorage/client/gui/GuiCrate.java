package io.github.tehstoneman.betterstorage.client.gui;

import java.util.List;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;

import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.client.renderer.Resources;
import io.github.tehstoneman.betterstorage.common.inventory.ContainerCrate;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn( Dist.CLIENT )
public class GuiCrate extends AbstractContainerScreen< ContainerCrate >
{
	private final int	rows;
	private final int	offsetY;

	public GuiCrate( ContainerCrate container, Inventory playerInventory, Component title )
	{
		super( container, playerInventory, title );

		rows = container.getRows();
		height = 114 + rows * 18;
		offsetY = 17 + rows * 18;

		// GUI label co-ordinates
		titleLabelX = 8;
		titleLabelY = 6;
		inventoryLabelX = 8;
		inventoryLabelY = offsetY + 3;
	}

	@Override
	public void render( PoseStack matrixStack, int mouseX, int mouseY, float partialTicks )
	{
		renderBackground( matrixStack );
		super.render( matrixStack, mouseX, mouseY, partialTicks );

		if( mouseX >= getGuiLeft() + 115 && mouseX < getGuiLeft() + 169 && mouseY >= getGuiTop() + 7 && mouseY < getGuiTop() + 13 )
		{
			final Component toolTip = new TranslatableComponent( ModInfo.CONTAINER_CAPACITY, menu.getVolume() );
			final List< Component > list1 = Lists.newArrayList( toolTip );
			renderComponentTooltip( matrixStack, list1, mouseX, mouseY );
		}
		else
			renderTooltip( matrixStack, mouseX, mouseY );
	}

	@Override
	protected void renderBg( PoseStack matrixStack, float partialTicks, int x, int y )
	{
		minecraft.getTextureManager().getTexture( Resources.CONTAINER_CRATE );

		blit( matrixStack, getGuiLeft(), getGuiTop(), 0, 0, getXSize(), offsetY );
		blit( matrixStack, getGuiLeft(), getGuiTop() + offsetY, 0, 126, getXSize(), 96 );

		final int volume = menu.getVolume();
		if( volume > 0.0 )
			blit( matrixStack, getGuiLeft() + 115, getGuiTop() + 7, 176, 0, (int)( 54 * ( volume / 100.0 ) ), 6 );
	}
}
