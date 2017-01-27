package io.github.tehstoneman.betterstorage.client.renderer;

import io.github.tehstoneman.betterstorage.tile.entity.TileEntityReinforcedChest;
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

	public void renderTileEntityAt( TileEntityReinforcedChest chest, double x, double y, double z, float partialTicks )
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

			/*
			 * if (destroyStage >= 0)
			 * {
			 * this.bindTexture(DESTROY_STAGES[destroyStage]);
			 * GlStateManager.matrixMode(5890);
			 * GlStateManager.pushMatrix();
			 * GlStateManager.scale(4.0F, 4.0F, 1.0F);
			 * GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
			 * GlStateManager.matrixMode(5888);
			 * }
			 * else if (this.isChristmas)
			 * {
			 * this.bindTexture(TEXTURE_CHRISTMAS);
			 * }
			 * else if (chest.getChestType() == BlockChest.Type.TRAP)
			 * {
			 * this.bindTexture(TEXTURE_TRAPPED);
			 * }
			 * else
			 */
			bindTexture( chest.getResource() );
		}
		else
		{
			modelchest = largeChestModel;

			/*
			 * if (destroyStage >= 0)
			 * {
			 * this.bindTexture(DESTROY_STAGES[destroyStage]);
			 * GlStateManager.matrixMode(5890);
			 * GlStateManager.pushMatrix();
			 * GlStateManager.scale(8.0F, 4.0F, 1.0F);
			 * GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
			 * GlStateManager.matrixMode(5888);
			 * }
			 * else if (this.isChristmas)
			 * {
			 * this.bindTexture(TEXTURE_CHRISTMAS_DOUBLE);
			 * }
			 * else if (chest.getChestType() == BlockChest.Type.TRAP)
			 * {
			 * this.bindTexture(TEXTURE_TRAPPED_DOUBLE);
			 * }
			 * else
			 */
			bindTexture( chest.getResource() );
		}

		GlStateManager.pushMatrix();
		GlStateManager.enableRescaleNormal();

		/*
		 * if (destroyStage < 0)
		 * {
		 * GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		 * }
		 */

		GlStateManager.translate( (float)x, (float)y + 1.0F, (float)z + 1.0F );
		GlStateManager.scale( 1.0F, -1.0F, -1.0F );
		GlStateManager.translate( 0.5F, 0.5F, 0.5F );
		int j = 0;

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

		/*
		 * if (chest.adjacentChestZNeg != null)
		 * {
		 * float f1 = chest.adjacentChestZNeg.prevLidAngle + (chest.adjacentChestZNeg.lidAngle - chest.adjacentChestZNeg.prevLidAngle) * partialTicks;
		 * 
		 * if (f1 > f)
		 * {
		 * f = f1;
		 * }
		 * }
		 * 
		 * if (chest.adjacentChestXNeg != null)
		 * {
		 * float f2 = chest.adjacentChestXNeg.prevLidAngle + (chest.adjacentChestXNeg.lidAngle - chest.adjacentChestXNeg.prevLidAngle) * partialTicks;
		 * 
		 * if (f2 > f)
		 * {
		 * f = f2;
		 * }
		 * }
		 */

		f = 1.0F - f;
		f = 1.0F - f * f * f;
		modelchest.chestLid.rotateAngleX = -( f * ( (float)Math.PI / 2F ) );
		modelchest.renderAll();
		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
		GlStateManager.color( 1.0F, 1.0F, 1.0F, 1.0F );

		/*
		 * if (destroyStage >= 0)
		 * {
		 * GlStateManager.matrixMode(5890);
		 * GlStateManager.popMatrix();
		 * GlStateManager.matrixMode(5888);
		 * }
		 */

	}

	/*
	 * public void renderTileEntityAt( TileEntityReinforcedChest chest, double x, double y, double z, float partialTicks )
	 * {
	 * final boolean large = chest.isConnected();
	 * if( large && !chest.isMain() )
	 * return;
	 * 
	 * final ModelChest model = large ? largeChestModel : chestModel;
	 * bindTexture( chest.getResource() );
	 * 
	 * GL11.glPushMatrix();
	 * GL11.glEnable( GL12.GL_RESCALE_NORMAL );
	 * GL11.glColor4f( 1.0F, 1.0F, 1.0F, 1.0F );
	 * GL11.glTranslated( x, y, z );
	 * 
	 * GL11.glPushMatrix();
	 * GL11.glScalef( 1.0F, -1.0F, -1.0F );
	 * GL11.glTranslatef( 0.5F, -0.5F, -0.5F );
	 * 
	 * final int rotation = DirectionUtils.getRotation( chest.getOrientation() );
	 * if( rotation == 180 && large )
	 * GL11.glTranslatef( 1.0F, 0.0F, 0.0F );
	 * if( rotation == 270 && large )
	 * GL11.glTranslatef( 0.0F, 0.0F, -1.0F );
	 * GL11.glRotatef( rotation, 0.0F, 1.0F, 0.0F );
	 * GL11.glTranslatef( -0.5F, -0.5F, -0.5F );
	 * 
	 * float angle = chest.prevLidAngle + ( chest.lidAngle - chest.prevLidAngle ) * partialTicks;
	 * angle = 1.0F - angle;
	 * angle = 1.0F - angle * angle * angle;
	 * model.chestLid.rotateAngleX = -(float)( angle * Math.PI / 2.0 );
	 * model.renderAll();
	 * 
	 * GL11.glPopMatrix();
	 * 
	 * chest.getAttachments().render( partialTicks );
	 * 
	 * GL11.glDisable( GL12.GL_RESCALE_NORMAL );
	 * GL11.glPopMatrix();
	 * GL11.glColor4f( 1.0F, 1.0F, 1.0F, 1.0F );
	 * }
	 */

	@Override
	public void renderTileEntityAt( TileEntity entity, double x, double y, double z, float partialTicks, int destroyStage )
	{
		renderTileEntityAt( (TileEntityReinforcedChest)entity, x, y, z, partialTicks );
	}
}
