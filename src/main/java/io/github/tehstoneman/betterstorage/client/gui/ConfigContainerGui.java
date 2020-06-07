package io.github.tehstoneman.betterstorage.client.gui;

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
	public void render( int mouseX, int mouseY, float partialTicks )
	{
		renderBackground();
		super.render( mouseX, mouseY, partialTicks );
		renderHoveredToolTip( mouseX, mouseY );
	}

	@Override
	protected void drawGuiContainerForegroundLayer( int par1, int par2 )
	{
		font.drawString( title.getFormattedText(), 8, 6, 0x404040 );
		font.drawString( playerInventory.getDisplayName().getFormattedText(), 8 + ( xSize - 176 ) / 2, ySize - 94, 0x404040 );
	}

	@Override
	protected void drawGuiContainerBackgroundLayer( float partialTicks, int mouseX, int mouseY )
	{
		minecraft.getTextureManager().bindTexture( Resources.CONTAINER_CONFIG );
		RenderSystem.color4f( 1.0F, 1.0F, 1.0F, 1.0F );

		blit( guiLeft, guiTop, 0, 0, xSize, ySize );
		for( int i = 0; i < container.indexHotbar; i++ )
		{
			final ConfigSlot slot = (ConfigSlot)container.getSlot( i );
			if( !slot.getHasStack() )
				blit( guiLeft + slot.xPos, guiTop + slot.yPos, slot.getIconX() + 1, slot.getIconY() + 1, 16, 16 );
		}
		// blit( guiLeft, guiTop + 35, 0, 125, xSize, 97 );
	}
}
