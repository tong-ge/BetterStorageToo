package io.github.tehstoneman.betterstorage.client.renderer;

import org.lwjgl.opengl.GL11;

import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityReinforcedChest;
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
public class TileEntityReinforcedChestRenderer// extends TileEntitySpecialRenderer< TileEntityReinforcedChest >
{
	protected static BlockRendererDispatcher blockRenderer;

	/*
	 * @Override
	 * public void render( TileEntityReinforcedChest chest, double x, double y, double z, float partialTicks, int destroyStage, float alpha )
	 * {
	 * if( !chest.isMain() )
	 * return;
	 * GlStateManager.pushAttrib();
	 * GlStateManager.pushMatrix();
	 * 
	 * GlStateManager.translate( x, y, z );
	 * GlStateManager.disableRescaleNormal();
	 * 
	 * final BlockPos pos = chest.getPos();
	 * final IBlockAccess world = MinecraftForgeClient.getRegionRenderCache( chest.getWorld(), pos );
	 * IBlockState state = world.getBlockState( pos );
	 * if( state.getBlock() == BetterStorageBlocks.REINFORCED_CHEST )
	 * {
	 * state = state.withProperty( BlockLockable.MATERIAL, chest.getMaterial() );
	 * state = state.withProperty( BlockLockable.CONNECTED, chest.isConnected() );
	 * 
	 * if( blockRenderer == null )
	 * blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
	 * 
	 * GlStateManager.translate( 0.5, 0.0, 0.5 );
	 * final EnumFacing facing = state.getValue( BlockHorizontal.FACING );
	 * GlStateManager.rotate( 180 - facing.getHorizontalAngle(), 0, 1, 0 );
	 * if( chest.isConnected() && ( facing == EnumFacing.NORTH || facing == EnumFacing.EAST ) )
	 * GlStateManager.translate( 0.5, 0, -0.5 );
	 * else
	 * GlStateManager.translate( -0.5, 0.0, -0.5 );
	 * 
	 * renderBase( chest, partialTicks, destroyStage, state );
	 * renderLid( chest, partialTicks, destroyStage, state );
	 * renderItem( chest, partialTicks, destroyStage, state );
	 * }
	 * 
	 * GlStateManager.popMatrix();
	 * GlStateManager.popAttrib();
	 * }
	 */

	private void renderBase( TileEntityReinforcedChest chest, float partialTicks, int destroyStage, IBlockState state )
	{
		GlStateManager.pushMatrix();

		RenderHelper.disableStandardItemLighting();
		// bindTexture( TextureMap.LOCATION_BLOCKS_TEXTURE );
		if( Minecraft.isAmbientOcclusionEnabled() )
			GlStateManager.shadeModel( GL11.GL_SMOOTH );
		else
			GlStateManager.shadeModel( GL11.GL_FLAT );

		final World world = chest.getWorld();
		GlStateManager.translatef( -chest.getPos().getX(), -chest.getPos().getY(), -chest.getPos().getZ() );

		final Tessellator tessellator = Tessellator.getInstance();

		final BufferBuilder buffer = tessellator.getBuffer();
		buffer.begin( GL11.GL_QUADS, DefaultVertexFormats.BLOCK );
		// final IBakedModel model = blockRenderer.getBlockModelShapes().getModelForState( state.withProperty( Properties.StaticProperty, true ) );
		// blockRenderer.getBlockModelRenderer().renderModel( world, model, state, chest.getPos(), buffer, false, null, destroyStage );

		tessellator.draw();

		RenderHelper.enableStandardItemLighting();
		GlStateManager.popMatrix();
	}

	private void renderLid( TileEntityReinforcedChest chest, float partialTicks, int destroyStage, IBlockState state )
	{
		GlStateManager.pushMatrix();

		RenderHelper.disableStandardItemLighting();
		// bindTexture( TextureMap.LOCATION_BLOCKS_TEXTURE );
		if( Minecraft.isAmbientOcclusionEnabled() )
			GlStateManager.shadeModel( GL11.GL_SMOOTH );
		else
			GlStateManager.shadeModel( GL11.GL_FLAT );

		float openAngle = chest.prevLidAngle + ( chest.lidAngle - chest.prevLidAngle ) * partialTicks;
		openAngle = 1.0F - openAngle;
		openAngle = 1.0F - openAngle * openAngle * openAngle;
		openAngle = openAngle * 90;

		GlStateManager.translated( 0, 9.5 / 16.0, 15.0 / 16.0 );
		GlStateManager.rotatef( openAngle, 1, 0, 0 );
		GlStateManager.translated( 0, -9.5 / 16.0, -15.0 / 16.0 );

		final World world = chest.getWorld();
		GlStateManager.translatef( -chest.getPos().getX(), -chest.getPos().getY(), -chest.getPos().getZ() );

		final Tessellator tessellator = Tessellator.getInstance();

		final BufferBuilder VertexBuffer = tessellator.getBuffer();
		VertexBuffer.begin( GL11.GL_QUADS, DefaultVertexFormats.BLOCK );
		// final IBakedModel model = blockRenderer.getBlockModelShapes().getModelForState( state.withProperty( Properties.StaticProperty, false ) );
		// blockRenderer.getBlockModelRenderer().renderModel( world, model, state, chest.getPos(), tessellator.getBuffer(), false, null, destroyStage );

		tessellator.draw();

		RenderHelper.enableStandardItemLighting();
		GlStateManager.popMatrix();
	}

	/** Renders attached lock on chest. Adapted from vanilla item frame **/
	/*
	 * private void renderItem( TileEntityReinforcedChest chest, float partialTicks, int destroyStage, IBlockState state )
	 * {
	 * final ItemStack itemstack = chest.getLock();
	 * 
	 * if( !itemstack.isEmpty() )
	 * {
	 * final EntityItem entityitem = new EntityItem( chest.getWorld(), 0.0D, 0.0D, 0.0D, itemstack );
	 * final Item item = entityitem.getItem().getItem();
	 * GlStateManager.pushMatrix();
	 * GlStateManager.disableLighting();
	 * 
	 * final RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
	 * 
	 * GlStateManager.rotatef( 180.0F, 0.0F, 1.0F, 0.0F );
	 * final double x = chest.isConnected() ? 0F : -8.0 / 16.0;
	 * final double y = 6.0 / 16.0;
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
