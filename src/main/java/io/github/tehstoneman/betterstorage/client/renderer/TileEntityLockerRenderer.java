package io.github.tehstoneman.betterstorage.client.renderer;

import org.lwjgl.opengl.GL11;

import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityLocker;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.world.World;

//@SideOnly( Side.CLIENT )
public class TileEntityLockerRenderer// extends TileEntitySpecialRenderer< TileEntityLocker >
{
	protected static BlockRendererDispatcher blockRenderer;

	/*
	 * @Override
	 * public void render( TileEntityLocker locker, double x, double y, double z, float partialTicks, int destroyStage, float alpha )
	 * {
	 * if( !locker.isMain() )
	 * return;
	 * GlStateManager.pushAttrib();
	 * GlStateManager.pushMatrix();
	 * 
	 * GlStateManager.translate( x, y, z );
	 * GlStateManager.disableRescaleNormal();
	 * 
	 * final BlockPos pos = locker.getPos();
	 * final IBlockAccess world = MinecraftForgeClient.getRegionRenderCache( locker.getWorld(), pos );
	 * IBlockState state = world.getBlockState( pos );
	 * if( state.getBlock() == BetterStorageBlocks.LOCKER || state.getBlock() == BetterStorageBlocks.REINFORCED_LOCKER )
	 * {
	 * if( state.getPropertyKeys().contains( BlockLockable.MATERIAL ) )
	 * state = state.withProperty( BlockLockable.MATERIAL, locker.getMaterial() );
	 * state = state.withProperty( BlockLockable.CONNECTED, locker.isConnected() );
	 * 
	 * if( blockRenderer == null )
	 * blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
	 * 
	 * GlStateManager.translate( 0.5, 0.0, 0.5 );
	 * final EnumFacing facing = state.getValue( BlockHorizontal.FACING );
	 * GlStateManager.rotate( 180 - facing.getHorizontalAngle(), 0, 1, 0 );
	 * GlStateManager.translate( -0.5, 0.0, -0.5 );
	 * 
	 * renderBase( locker, partialTicks, destroyStage, state );
	 * renderDoor( locker, partialTicks, destroyStage, state );
	 * renderItem( locker, partialTicks, destroyStage, state );
	 * }
	 * 
	 * GlStateManager.popMatrix();
	 * GlStateManager.popAttrib();
	 * }
	 */

	private void renderBase( TileEntityLocker locker, float partialTicks, int destroyStage, IBlockState state )
	{
		GlStateManager.pushMatrix();

		RenderHelper.disableStandardItemLighting();
		// bindTexture( TextureMap.LOCATION_BLOCKS_TEXTURE );
		if( Minecraft.isAmbientOcclusionEnabled() )
			GlStateManager.shadeModel( GL11.GL_SMOOTH );
		else
			GlStateManager.shadeModel( GL11.GL_FLAT );

		final World world = locker.getWorld();
		GlStateManager.translatef( -locker.getPos().getX(), -locker.getPos().getY(), -locker.getPos().getZ() );

		final Tessellator tessellator = Tessellator.getInstance();

		final BufferBuilder buffer = tessellator.getBuffer();
		buffer.begin( GL11.GL_QUADS, DefaultVertexFormats.BLOCK );
		// final IBakedModel model = blockRenderer.getBlockModelShapes().getModelForState( state.withProperty( Properties.StaticProperty, true ) );
		// blockRenderer.getBlockModelRenderer().renderModel( world, model, state, locker.getPos(), buffer, false, null, destroyStage );

		tessellator.draw();

		RenderHelper.enableStandardItemLighting();
		GlStateManager.popMatrix();
	}

	private void renderDoor( TileEntityLocker locker, float partialTicks, int destroyStage, IBlockState state )
	{
		GlStateManager.pushMatrix();

		RenderHelper.disableStandardItemLighting();
		// bindTexture( TextureMap.LOCATION_BLOCKS_TEXTURE );
		if( Minecraft.isAmbientOcclusionEnabled() )
			GlStateManager.shadeModel( GL11.GL_SMOOTH );
		else
			GlStateManager.shadeModel( GL11.GL_FLAT );

		float openAngle = locker.prevLidAngle + ( locker.lidAngle - locker.prevLidAngle ) * partialTicks;
		openAngle = 1.0F - openAngle;
		openAngle = 1.0F - openAngle * openAngle * openAngle;
		openAngle = openAngle * 90;

		// final boolean left = state.getValue( BlockDoor.HINGE ) == EnumHingePosition.LEFT;

		// GlStateManager.translatef( left ? 15F / 16F : 1F / 16F, 0, 1.0 / 16.0 );
		// GlStateManager.rotate( left ? -openAngle : openAngle, 0, 1, 0 );
		// GlStateManager.translatef( left ? -15F / 16F : -1F / 16F, 0, -1.0 / 16.0 );

		final World world = locker.getWorld();
		GlStateManager.translatef( -locker.getPos().getX(), -locker.getPos().getY(), -locker.getPos().getZ() );

		final Tessellator tessellator = Tessellator.getInstance();

		final BufferBuilder VertexBuffer = tessellator.getBuffer();
		VertexBuffer.begin( GL11.GL_QUADS, DefaultVertexFormats.BLOCK );
		// final IBakedModel model = blockRenderer.getBlockModelShapes().getModelForState( state.withProperty( Properties.StaticProperty, false ) );
		// blockRenderer.getBlockModelRenderer().renderModel( world, model, state, locker.getPos(), tessellator.getBuffer(), false, null, destroyStage );

		tessellator.draw();

		// renderItem( locker, partialTicks, destroyStage, state );

		RenderHelper.enableStandardItemLighting();
		GlStateManager.popMatrix();
	}

	/** Renders attached lock on chest. Adapted from vanilla item frame **/
	/*
	 * private void renderItem( TileEntityLocker locker, float partialTicks, int destroyStage, IBlockState state )
	 * {
	 * final ItemStack itemstack = locker.getLock();
	 * 
	 * if( itemstack != null )
	 * {
	 * final EntityItem entityitem = new EntityItem( locker.getWorld(), 0.0D, 0.0D, 0.0D, itemstack );
	 * final Item item = entityitem.getItem().getItem();
	 * GlStateManager.pushMatrix();
	 * GlStateManager.disableLighting();
	 * 
	 * float openAngle = locker.prevLidAngle + ( locker.lidAngle - locker.prevLidAngle ) * partialTicks;
	 * openAngle = 1.0F - openAngle;
	 * openAngle = 1.0F - openAngle * openAngle * openAngle;
	 * openAngle = openAngle * 90;
	 * 
	 * final RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
	 * final boolean left = state.getValue( BlockDoor.HINGE ) == EnumHingePosition.LEFT;
	 * 
	 * GlStateManager.translatef( left ? 15F / 16F : 1F / 16F, 0, 1.0 / 16.0 );
	 * GlStateManager.rotate( left ? -openAngle : openAngle, 0, 1, 0 );
	 * GlStateManager.translatef( left ? -15F / 16F : -1F / 16F, 0, -1.0 / 16.0 );
	 * 
	 * GlStateManager.rotate( 180.0F, 0.0F, 1.0F, 0.0F );
	 * final double x = left ? -3.5 / 16.0 : -12.5 / 16.0;
	 * final double y = locker.isConnected() ? 12.0 / 16.0 : 6.0 / 16.0;
	 * final double z = -0.5 / 16.0;
	 * GlStateManager.translatef( x, y, z );
	 * GlStateManager.scale( 0.5, 0.5, 0.5 );
	 * 
	 * GlStateManager.pushAttrib();
	 * RenderHelper.enableStandardItemLighting();
	 * itemRenderer.renderItem( entityitem.getItem(), ItemCameraTransforms.TransformType.FIXED );
	 * RenderHelper.disableStandardItemLighting();
	 * GlStateManager.popAttrib();
	 * 
	 * GlStateManager.enableLighting();
	 * GlStateManager.popMatrix();
	 * }
	 * }
	 */
}
