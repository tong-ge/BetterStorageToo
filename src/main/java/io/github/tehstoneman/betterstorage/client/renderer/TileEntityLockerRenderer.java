package io.github.tehstoneman.betterstorage.client.renderer;

import org.lwjgl.opengl.GL11;

import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.client.model.ModelLargeLocker;
import io.github.tehstoneman.betterstorage.client.model.ModelLocker;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityLocker;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityReinforcedChest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
		renderItem( locker );

		GlStateManager.popMatrix();
		GlStateManager.popAttrib();
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

	/** Renders attached lock on chest. Adapted from vanilla item frame **/
	private void renderItem( TileEntityLocker locker )
	{
		final ItemStack itemstack = locker.getLock();

		if( itemstack != null )
		{
			final EntityItem entityitem = new EntityItem( locker.getWorld(), 0.0D, 0.0D, 0.0D, itemstack );
			final Item item = entityitem.getEntityItem().getItem();
			entityitem.getEntityItem().stackSize = 1;
			entityitem.hoverStart = 0.0F;
			GlStateManager.pushMatrix();
			GlStateManager.disableLighting();

			final RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();

			GlStateManager.rotate( 180.0F, 0.0F, 1.0F, 0.0F );
			double x = (1.0/16.0) * -3.5;
			double y = (1.0/16.0) * (locker.isConnected()? 13.0 : 6.0 );
			double z = (1.0/16.0) * -0.5;
			GlStateManager.translate( x, y, z );
			GlStateManager.scale( 0.5, 0.5, 0.5 );
			
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
