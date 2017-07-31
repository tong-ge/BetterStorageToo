package io.github.tehstoneman.betterstorage.client.renderer;

import org.lwjgl.opengl.GL11;

import io.github.tehstoneman.betterstorage.common.block.BlockLockable;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityReinforcedChest;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.property.Properties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly( Side.CLIENT )
public class TileEntityReinforcedChestRenderer extends TileEntitySpecialRenderer< TileEntityReinforcedChest >
{
	protected static BlockRendererDispatcher blockRenderer;

	@Override
	public void renderTileEntityAt( TileEntityReinforcedChest chest, double x, double y, double z, float partialTicks, int destroyStage )
	{
		if( !chest.isMain() )
			return;
		GlStateManager.pushAttrib();
		GlStateManager.pushMatrix();

		GlStateManager.translate( x, y, z );
		GlStateManager.disableRescaleNormal();

		final BlockPos pos = chest.getPos();
		final IBlockAccess world = MinecraftForgeClient.getRegionRenderCache( chest.getWorld(), pos );
		IBlockState state = world.getBlockState( pos );
		state = state.withProperty( BlockLockable.MATERIAL, chest.getMaterial() );
		state = state.withProperty( BlockLockable.CONNECTED, chest.isConnected() );

		if( blockRenderer == null )
			blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();

		GlStateManager.translate( 0.5, 0.0, 0.5 );
		final EnumFacing facing = state.getValue( BlockHorizontal.FACING );
		GlStateManager.rotate( 180 - facing.getHorizontalAngle(), 0, 1, 0 );
		if( chest.isConnected() && ( facing == EnumFacing.NORTH || facing == EnumFacing.EAST ) )
			GlStateManager.translate( 0.5, 0, -0.5 );
		else
			GlStateManager.translate( -0.5, 0.0, -0.5 );

		renderBase( chest, partialTicks, destroyStage, state );
		renderLid( chest, partialTicks, destroyStage, state );
		// renderLock( chest );

		GlStateManager.popMatrix();
		GlStateManager.popAttrib();
	}

	private void renderBase( TileEntityReinforcedChest chest, float partialTicks, int destroyStage, IBlockState state )
	{
		GlStateManager.pushMatrix();

		RenderHelper.disableStandardItemLighting();
		bindTexture( TextureMap.LOCATION_BLOCKS_TEXTURE );
		if( Minecraft.isAmbientOcclusionEnabled() )
			GlStateManager.shadeModel( GL11.GL_SMOOTH );
		else
			GlStateManager.shadeModel( GL11.GL_FLAT );

		final World world = chest.getWorld();
		GlStateManager.translate( -chest.getPos().getX(), -chest.getPos().getY(), -chest.getPos().getZ() );

		final Tessellator tessellator = Tessellator.getInstance();
		final VertexBuffer buffer = tessellator.getBuffer();
		buffer.begin( GL11.GL_QUADS, DefaultVertexFormats.BLOCK );
		final IBakedModel model = blockRenderer.getBlockModelShapes().getModelForState( state.withProperty( Properties.StaticProperty, true ) );
		blockRenderer.getBlockModelRenderer().renderModel( world, model, state, chest.getPos(), buffer, false );
		tessellator.draw();

		RenderHelper.enableStandardItemLighting();
		GlStateManager.popMatrix();
	}

	private void renderLid( TileEntityReinforcedChest chest, float partialTicks, int destroyStage, IBlockState state )
	{
		GlStateManager.pushMatrix();

		RenderHelper.disableStandardItemLighting();
		bindTexture( TextureMap.LOCATION_BLOCKS_TEXTURE );
		if( Minecraft.isAmbientOcclusionEnabled() )
			GlStateManager.shadeModel( GL11.GL_SMOOTH );
		else
			GlStateManager.shadeModel( GL11.GL_FLAT );

		float openAngle = chest.prevLidAngle + ( chest.lidAngle - chest.prevLidAngle ) * partialTicks;
		openAngle = 1.0F - openAngle;
		openAngle = 1.0F - openAngle * openAngle * openAngle;
		openAngle = openAngle * 90;

		GlStateManager.translate( 0, 9.5 / 16.0, 15.0 / 16.0 );
		GlStateManager.rotate( openAngle, 1, 0, 0 );
		GlStateManager.translate( 0, -9.5 / 16.0, -15.0 / 16.0 );

		final World world = chest.getWorld();
		GlStateManager.translate( -chest.getPos().getX(), -chest.getPos().getY(), -chest.getPos().getZ() );

		final Tessellator tessellator = Tessellator.getInstance();
		final VertexBuffer VertexBuffer = tessellator.getBuffer();
		VertexBuffer.begin( GL11.GL_QUADS, DefaultVertexFormats.BLOCK );
		final IBakedModel model = blockRenderer.getBlockModelShapes().getModelForState( state.withProperty( Properties.StaticProperty, false ) );
		blockRenderer.getBlockModelRenderer().renderModel( world, model, state, chest.getPos(), tessellator.getBuffer(), false );
		tessellator.draw();

		RenderHelper.enableStandardItemLighting();
		GlStateManager.popMatrix();
	}
}
