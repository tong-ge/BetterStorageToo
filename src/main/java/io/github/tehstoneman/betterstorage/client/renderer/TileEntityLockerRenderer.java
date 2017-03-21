package io.github.tehstoneman.betterstorage.client.renderer;

import org.lwjgl.opengl.GL11;

import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.client.model.ModelLargeLocker;
import io.github.tehstoneman.betterstorage.client.model.ModelLocker;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityLocker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly( Side.CLIENT )
public class TileEntityLockerRenderer extends TileEntitySpecialRenderer< TileEntityLocker >
{
	private final ModelLocker	lockerModel			= new ModelLocker();
	private final ModelLocker	largeLockerModel	= new ModelLargeLocker();

	private IModel				model, modelLarge;
	private IBakedModel			bakedModel, bakedModelLarge;

	private IBakedModel getBakedModel( boolean isConnected )
	{
		if( bakedModel == null )
		{
			try
			{
				model = ModelLoaderRegistry.getModel( new ResourceLocation( ModInfo.modId, "block/locker_door.obj" ) );
			}
			catch( final Exception e )
			{
				throw new RuntimeException( e );
			}
			bakedModel = model.bake( TRSRTransformation.identity(), DefaultVertexFormats.ITEM,
					location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite( location.toString() ) );
		}

		if( bakedModelLarge == null )
		{
			try
			{
				modelLarge = ModelLoaderRegistry.getModel( new ResourceLocation( ModInfo.modId, "block/locker_large_door.obj" ) );
			}
			catch( final Exception e )
			{
				throw new RuntimeException( e );
			}
			bakedModelLarge = modelLarge.bake( TRSRTransformation.identity(), DefaultVertexFormats.ITEM,
					location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite( location.toString() ) );
		}

		return isConnected ? bakedModelLarge : bakedModel;
	}

	@Override
	public void renderTileEntityAt( TileEntityLocker locker, double x, double y, double z, float partialTicks, int destroyStage )
	{
		if( !locker.isMain() )
			return;
		GlStateManager.pushAttrib();
		GlStateManager.pushMatrix();

		GlStateManager.translate( x, y, z );

		GlStateManager.translate( 0.5, 0, 0.5 );
		float angle = locker.getOrientation().getHorizontalAngle();
		if( angle == 0.0 || angle == 180 )
			angle = 180 - angle;
		GlStateManager.rotate( angle, 0, 1, 0 );
		GlStateManager.translate( -0.5, 0, -0.5 );

		GlStateManager.disableRescaleNormal();

		renderDoor( locker, partialTicks );

		GlStateManager.popMatrix();
		GlStateManager.popAttrib();

		/*
		 * final float scale = 1.0F / 16;
		 *
		 * final boolean large = locker.isConnected();
		 * if( large && !locker.isMain() )
		 * return;
		 *
		 * final int index = locker.mirror ? 1 : 0;
		 * final ModelLocker model = large ? largeLockerModel : lockerModel;
		 * bindTexture( locker.getResource() );
		 *
		 * GL11.glPushMatrix();
		 * GL11.glEnable( GL12.GL_RESCALE_NORMAL );
		 *
		 * float angle = locker.prevLidAngle + ( locker.lidAngle - locker.prevLidAngle ) * partialTicks;
		 * angle = 1.0F - angle;
		 * angle = 1.0F - angle * angle * angle;
		 * angle = angle * 90;
		 *
		 * GL11.glTranslated( x + 0.5, y + 0.5, z + 0.5 );
		 * final int rotation = DirectionUtils.getRotation( locker.getOrientation() );
		 * GL11.glRotatef( -rotation, 0.0F, 1.0F, 0.0F );
		 *
		 * GL11.glPushMatrix();
		 * GL11.glScalef( scale, scale, scale );
		 *
		 * model.renderAll( locker.mirror, angle );
		 *
		 * GL11.glPopMatrix();
		 *
		 * if( locker.canHaveLock() )
		 * {
		 * if( angle > 0 )
		 * {
		 * final double seven = 7 / 16.0D;
		 * GL11.glTranslated( locker.mirror ? seven : -seven, 0, seven );
		 * GL11.glRotatef( locker.mirror ? angle : -angle, 0, 1, 0 );
		 * GL11.glTranslated( locker.mirror ? -seven : seven, 0, -seven );
		 * }
		 * final LockAttachment a = locker.lockAttachment;
		 * GL11.glTranslated( 0.5 - a.getX(), 0.5 - a.getY(), 0.5 - a.getZ() );
		 * a.getRenderer().render( a, partialTicks );
		 * }
		 *
		 * GL11.glDisable( GL12.GL_RESCALE_NORMAL );
		 * GL11.glPopMatrix();
		 */
	}

	private void renderDoor( TileEntityLocker locker, float partialTicks )
	{
		GlStateManager.pushMatrix();

		GlStateManager.translate( 0.9375, 0.0625, 0.0625 );

		float angle = locker.prevLidAngle + ( locker.lidAngle - locker.prevLidAngle ) * partialTicks;
		angle = 1.0F - angle;
		angle = 1.0F - angle * angle * angle;
		angle = angle * 90;

		GlStateManager.rotate( -angle, 0, 1, 0 );

		RenderHelper.disableStandardItemLighting();
		bindTexture( TextureMap.LOCATION_BLOCKS_TEXTURE );
		if( Minecraft.isAmbientOcclusionEnabled() )
			GlStateManager.shadeModel( GL11.GL_SMOOTH );
		else
			GlStateManager.shadeModel( GL11.GL_FLAT );

		final World world = locker.getWorld();
		GlStateManager.translate( -locker.getPos().getX(), -locker.getPos().getY(), -locker.getPos().getZ() );

		final Tessellator tessellator = Tessellator.getInstance();
		tessellator.getBuffer().begin( GL11.GL_QUADS, DefaultVertexFormats.BLOCK );
		Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel( world, getBakedModel( locker.isConnected() ),
				world.getBlockState( locker.getPos() ), locker.getPos(), tessellator.getBuffer(), false );
		tessellator.draw();

		RenderHelper.enableStandardItemLighting();
		GlStateManager.popMatrix();
	}
}
