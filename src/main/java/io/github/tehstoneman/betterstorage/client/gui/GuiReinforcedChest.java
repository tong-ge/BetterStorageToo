package io.github.tehstoneman.betterstorage.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;

import io.github.tehstoneman.betterstorage.client.renderer.Resources;
import io.github.tehstoneman.betterstorage.common.inventory.ContainerReinforcedChest;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class GuiReinforcedChest extends ContainerScreen< ContainerReinforcedChest >
{
	private final int	columns, rows;
	private final int	x1, x2, x3, x4;
	private final int	y1;

	public GuiReinforcedChest( ContainerReinforcedChest container, PlayerInventory playerInventory, ITextComponent title )
	{
		super( container, playerInventory, title );

		columns = container.getColumns();
		rows = container.getRows();

		xSize = Math.max( 14 + columns * 18, 176 );
		ySize = 114 + rows * 18;

		// Calculate horizontal texture slices
		x1 = columns * 18 + 7;
		x2 = ( xSize - 176 ) / 2;
		x3 = xSize - x2;
		x4 = 248 - x2;

		// Calculate vertical texture slices
		y1 = rows * 18 + 17;
	}

	protected ResourceLocation getResource()
	{
		if( columns <= 9 )
			return Resources.CONTAINER_GENERIC;
		else
			return Resources.CONTAINER_EXPANDABLE;
	}

	@Override
	public void render( int mouseX, int mouseY, float partialTicks )
	{
		super.render( mouseX, mouseY, partialTicks );
		renderHoveredToolTip( mouseX, mouseY );
	}

	@Override
	protected void drawGuiContainerForegroundLayer( int par1, int par2 )
	{
		font.drawString( title.getFormattedText(), 8, 6, 0x404040 );
		font.drawString( playerInventory.getDisplayName().getFormattedText(), x2 + 8, y1 + 2, 0x404040 );
	}

	@Override
	protected void drawGuiContainerBackgroundLayer( float partialTicks, int x, int y )
	{
		minecraft.getTextureManager().bindTexture( getResource() );
		GlStateManager.color4f( 1.0F, 1.0F, 1.0F, 1.0F );

		//@formatter:off
		// Chest inventory
		blit( guiLeft,      guiTop,   0, 0, x1, y1 );
		blit( guiLeft + x1, guiTop, 241, 0,  7, y1 );

		// Player inventory
		blit( guiLeft,      guiTop + y1,  0, 125,  x2, 17 );
		blit( guiLeft + x2, guiTop + y1, 36, 125, 176, 97 );
		blit( guiLeft + x3, guiTop + y1, x4, 125,  x2, 17 );
		//@formatter:on
	}
}
