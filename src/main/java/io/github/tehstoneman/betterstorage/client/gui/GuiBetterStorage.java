package io.github.tehstoneman.betterstorage.client.gui;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.client.renderer.Resources;
import io.github.tehstoneman.betterstorage.common.inventory.ContainerBetterStorage;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn( Dist.CLIENT )
public class GuiBetterStorage extends GuiContainer
{
	public final ContainerBetterStorage	container;
	public final ITextComponent					title;

	private final int					columns;
	private final int					rows;

	public GuiBetterStorage( ContainerBetterStorage container )
	{
		super( container );

		this.container = container;
		title = container.getName();
		columns = container.getColumns();
		rows = container.getRows();
		
		xSize = 14 + columns * 18;
		ySize = container.getHeight();

		// container.setUpdateGui( this );
	}

	public int getColumns()
	{
		return columns;
	}

	public int getRows()
	{
		return rows;
	}

	protected ResourceLocation getResource()
	{
		if( columns <= 9 )
			return new ResourceLocation( "textures/gui/container/generic_54.png" );
		else
			return Resources.containerReinforcedChest;
	}

	protected int getHeight()
	{
		return 223;
	}

	protected int getTextureWidth()
	{
		return 256;
	}

	protected int getTextureHeight()
	{
		return 256;
	}

	public void update( int par1, int par2 )
	{}

	@Override
	public void render( int mouseX, int mouseY, float partialTicks )
	{
		drawDefaultBackground();
		super.render( mouseX, mouseY, partialTicks );
		renderHoveredToolTip( mouseX, mouseY );
	}

	@Override
	protected void drawGuiContainerForegroundLayer( int par1, int par2 )
	{
		fontRenderer.drawString( title.getFormattedText(), 8, 6, 0x404040 );
		fontRenderer.drawString( I18n.format( "container.inventory" ), 8 + ( xSize - 176 ) / 2, ySize - 94, 0x404040 );
	}

	@Override
	protected void drawGuiContainerBackgroundLayer( float partialTicks, int x, int y )
	{
		mc.getTextureManager().bindTexture( getResource() );
		GlStateManager.color4f( 1.0F, 1.0F, 1.0F, 1.0F );

		final int m = 107;
		final int m1 = ySize - m;
		final int m2 = getHeight() - m;

		drawTexturedModalRect( guiLeft, guiTop, 0, 0, xSize, m1 );
		drawTexturedModalRect( guiLeft, guiTop + m1, 0, m2, xSize, m );
	}
}
