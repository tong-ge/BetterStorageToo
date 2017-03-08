package io.github.tehstoneman.betterstorage.client.renderer;

import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityReinforcedChest;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.model.ModelLargeChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly( Side.CLIENT )
public class TileEntityReinforcedChestRenderer extends TileEntitySpecialRenderer
{
	private final ModelChest	chestModel		= new ModelChest();
	private final ModelChest	largeChestModel	= new ModelLargeChest();

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

	@Override
	public void renderTileEntityAt( TileEntity entity, double x, double y, double z, float partialTicks, int destroyStage )
	{
		renderTileEntityAt( (TileEntityReinforcedChest)entity, x, y, z, partialTicks, destroyStage );
	}
}
