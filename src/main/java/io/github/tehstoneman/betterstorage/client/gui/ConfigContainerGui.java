package io.github.tehstoneman.betterstorage.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;

import io.github.tehstoneman.betterstorage.client.renderer.Resources;
import io.github.tehstoneman.betterstorage.common.inventory.ConfigContainer;
import io.github.tehstoneman.betterstorage.common.inventory.ConfigContainer.ConfigSlot;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ConfigContainerGui extends ContainerScreen< ConfigContainer >
{
	public ConfigContainerGui( ConfigContainer screenContainer, PlayerInventory inv, ITextComponent titleIn )
	{
		super( screenContainer, inv, titleIn );
		imageHeight = 132;
		inventoryLabelY = imageHeight - 94;
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
		minecraft.getTextureManager().bind( Resources.CONTAINER_CONFIG );

		blit( matrixStack, getGuiLeft(), getGuiTop(), 0, 0, getXSize(), imageHeight );
		for( int i = 0; i < menu.indexHotbar; i++ )
		{
			final ConfigSlot slot = (ConfigSlot)menu.getSlot( i );
			if( !slot.hasItem() )
				blit( matrixStack, getGuiLeft() + slot.x, getGuiTop() + slot.y, slot.getIconX() + 1, slot.getIconY() + 1, 16, 16 );
		}
	}
}
