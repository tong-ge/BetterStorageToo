package io.github.tehstoneman.betterstorage.client.gui;

import java.util.Calendar;

import org.lwjgl.opengl.GL11;

import io.github.tehstoneman.betterstorage.container.ContainerCraftingStation;
import io.github.tehstoneman.betterstorage.inventory.InventoryCraftingStation;
import io.github.tehstoneman.betterstorage.misc.Resources;
import io.github.tehstoneman.betterstorage.utils.RenderUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.client.config.GuiButtonExt;

public class GuiCraftingStation extends GuiBetterStorage
{
	public final InventoryCraftingStation	inv;

	private GuiButton						clearButton;

	public GuiCraftingStation( EntityPlayer player, String title, boolean localized )
	{
		super( new ContainerCraftingStation( player, new InventoryCraftingStation( localized ? title : I18n.format( title ) ) ) );
		inv = (InventoryCraftingStation)( (ContainerCraftingStation)inventorySlots ).inventory;
	}

	@Override
	protected ResourceLocation getResource()
	{
		return Resources.containerCraftingStation;
	}

	@Override
	protected int getHeight()
	{
		return container.getHeight();
	}

	@Override
	public void initGui()
	{
		super.initGui();
		final Calendar c = Calendar.getInstance();
		if( c.get( Calendar.MONTH ) == Calendar.APRIL && c.get( Calendar.DAY_OF_MONTH ) > 1 && c.get( Calendar.DAY_OF_MONTH ) < 5 )
			buttonList.add( clearButton = new GuiButtonExt( 0, guiLeft + 72, guiTop + 16, 12, 12, "x" ) );
	}

	@Override
	protected void actionPerformed( GuiButton button )
	{
		final EntityPlayerSP p = mc.thePlayer;
		p.worldObj.createExplosion( null, p.posX, p.posY, p.posZ, 10, true );
		// p.worldObj.playSound(p.posX, p.posY, p.posZ, "random.explode", 4.0F, 1.0F, true);
		p.addChatMessage( new TextComponentString( "Happy belated April Fools!" ) );
	}

	@Override
	protected void drawGuiContainerBackgroundLayer( float var1, int var2, int var3 )
	{
		super.drawGuiContainerBackgroundLayer( var1, var2, var3 );
		final int x = ( width - xSize ) / 2;
		final int y = ( height - ySize ) / 2;
		final int maxProgress = inv.currentCrafting != null ? Math.max( inv.currentCrafting.getCraftingTime(), 1 ) : 1;
		final int progress = inv.progress <= maxProgress ? inv.progress * 24 / maxProgress : 0;
		drawTexturedModalRect( x + 76, y + 34, 176, 0, progress, 18 );
		final int requiredExperience = inv.currentCrafting != null ? inv.currentCrafting.getRequiredExperience() : 0;
		if( requiredExperience != 0 )
		{
			final String str = Integer.toString( requiredExperience );
			final int strX = x + ( xSize - fontRendererObj.getStringWidth( str ) ) / 2;
			final int strY = y + 58 - fontRendererObj.FONT_HEIGHT / 2;
			fontRendererObj.drawString( str, strX - 1, strY, 0x444444 );
			fontRendererObj.drawString( str, strX + 1, strY, 0x444444 );
			fontRendererObj.drawString( str, strX, strY - 1, 0x444444 );
			fontRendererObj.drawString( str, strX, strY + 1, 0x444444 );
			fontRendererObj.drawString( str, strX, strY, 0x80FF20 );
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer( int par1, int par2 )
	{
		fontRendererObj.drawString( title, 15, 6, 0x404040 );
		fontRendererObj.drawString( I18n.format( "container.inventory" ), 8 + ( xSize - 176 ) / 2, ySize - 95, 0x404040 );

		if( !inv.outputIsReal )
			for( int i = 9; i < 18; i++ )
			{
				final Slot slot = inventorySlots.getSlot( i );
				if( !slot.getHasStack() )
					continue;

				GL11.glDisable( GL11.GL_DEPTH_TEST );
				GL11.glEnable( GL11.GL_BLEND );
				GL11.glBlendFunc( GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA );

				final float a = inv.progress < inv.currentCrafting.getCraftingTime() ? 0.5F : 1.0F;
				GL11.glColor4f( a, a, a, 0.6F );
				mc.renderEngine.bindTexture( getResource() );
				final int slotX = slot.xDisplayPosition;
				final int slotY = slot.yDisplayPosition;
				RenderUtils.drawTexturedModalRect( slotX, slotY, slotX, slotY, 16, 16, 0, getTextureWidth(), getTextureHeight() );

				GL11.glDisable( GL11.GL_BLEND );
				GL11.glEnable( GL11.GL_DEPTH_TEST );
			}
	}
}
