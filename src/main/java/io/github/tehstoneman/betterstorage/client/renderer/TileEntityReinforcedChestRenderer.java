package io.github.tehstoneman.betterstorage.client.renderer;

import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityReinforcedChest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.model.ModelLargeChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly( Side.CLIENT )
public class TileEntityReinforcedChestRenderer extends TileEntitySpecialRenderer< TileEntityReinforcedChest >
{
	private final ModelChest	chestModel		= new ModelChest();
	private final ModelChest	largeChestModel	= new ModelLargeChest();

	@Override
	public void renderTileEntityAt( TileEntityReinforcedChest chest, double x, double y, double z, float partialTicks, int destroyStage )
	{
		final boolean large = chest.isConnected();
		if( large && !chest.isMain() )
			return;

		GlStateManager.enableDepth();
		GlStateManager.depthFunc( 515 );
		GlStateManager.depthMask( true );
		int i;

		if( chest.hasWorldObj() && chest.getOrientation() != null )
			i = chest.getOrientation().getIndex();
		else
			i = 0;

		ModelChest modelchest;

		if( !large )
		{
			modelchest = chestModel;

			if( destroyStage >= 0 )
			{
				bindTexture( DESTROY_STAGES[destroyStage] );
				GlStateManager.matrixMode( 5890 );
				GlStateManager.pushMatrix();
				GlStateManager.scale( 4.0F, 4.0F, 1.0F );
				GlStateManager.translate( 0.0625F, 0.0625F, 0.0625F );
				GlStateManager.matrixMode( 5888 );
			}
		}
		else
		{
			modelchest = largeChestModel;

			if( destroyStage >= 0 )
			{
				bindTexture( DESTROY_STAGES[destroyStage] );
				GlStateManager.matrixMode( 5890 );
				GlStateManager.pushMatrix();
				GlStateManager.scale( 8.0F, 4.0F, 1.0F );
				GlStateManager.translate( 0.0625F, 0.0625F, 0.0625F );
				GlStateManager.matrixMode( 5888 );
			}
		}

		bindTexture( chest.getResource() );
		GlStateManager.pushMatrix();
		GlStateManager.enableRescaleNormal();

		if( destroyStage < 0 )
			GlStateManager.color( 1.0F, 1.0F, 1.0F, 1.0F );

		GlStateManager.translate( (float)x, (float)y + 1.0F, (float)z + 1.0F );
		GlStateManager.scale( 1.0F, -1.0F, -1.0F );
		GlStateManager.translate( 0.5F, 0.5F, 0.5F );
		int j = 0;

		if( i == 0 )
			j = 45;

		if( i == 2 )
			j = 180;

		if( i == 3 )
			j = 0;

		if( i == 4 )
			j = 90;

		if( i == 5 )
			j = -90;

		if( i == 2 && large )
			GlStateManager.translate( 1.0F, 0.0F, 0.0F );

		if( i == 5 && large )
			GlStateManager.translate( 0.0F, 0.0F, -1.0F );

		GlStateManager.rotate( j, 0.0F, 1.0F, 0.0F );
		GlStateManager.translate( -0.5F, -0.5F, -0.5F );
		float f = chest.prevLidAngle + ( chest.lidAngle - chest.prevLidAngle ) * partialTicks;

		f = 1.0F - f;
		f = 1.0F - f * f * f;
		modelchest.chestLid.rotateAngleX = -( f * ( (float)Math.PI / 2F ) );
		modelchest.renderAll();

		// chest.getAttachments().render( partialTicks );
		renderItem( chest );

		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
		GlStateManager.color( 1.0F, 1.0F, 1.0F, 1.0F );

		if( destroyStage >= 0 )
		{
			GlStateManager.matrixMode( 5890 );
			GlStateManager.popMatrix();
			GlStateManager.matrixMode( 5888 );
		}

	}

	/** Renders attached lock on chest. Adapted from vanillia item frame **/
	private void renderItem( TileEntityReinforcedChest chest )
	{
		final ItemStack itemstack = chest.getLock();

		if( itemstack != null )
		{
			final EntityItem entityitem = new EntityItem( chest.getWorld(), 0.0D, 0.0D, 0.0D, itemstack );
			final Item item = entityitem.getEntityItem().getItem();
			entityitem.getEntityItem().stackSize = 1;
			entityitem.hoverStart = 0.0F;
			GlStateManager.pushMatrix();
			GlStateManager.disableLighting();

			final RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();

			GlStateManager.rotate( 180.0F, 1.0F, 0.0F, 0.0F );
			GlStateManager.scale( 0.5, 0.5, 0.5 );
			GlStateManager.translate( chest.isConnected()? 2.0 : 1.0, -1.25,-0.1 );
			
			GlStateManager.pushAttrib();
			RenderHelper.enableStandardItemLighting();
			itemRenderer.renderItem( entityitem.getEntityItem(), ItemCameraTransforms.TransformType.FIXED );
			RenderHelper.disableStandardItemLighting();
			GlStateManager.popAttrib();

			GlStateManager.enableLighting();
			GlStateManager.popMatrix();
		}
	}
}
