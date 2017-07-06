package io.github.tehstoneman.betterstorage.client.renderer;

import org.lwjgl.opengl.GL11;

import io.github.tehstoneman.betterstorage.common.block.BlockReinforcedChest;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityReinforcedChest;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;
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
		GlStateManager.pushAttrib();
		GlStateManager.pushMatrix();

		GlStateManager.disableRescaleNormal();

		GlStateManager.enableDepth();
		GlStateManager.depthFunc( 515 );
		GlStateManager.depthMask( true );

		final BlockPos pos = chest.getPos();
		final IBlockAccess world = MinecraftForgeClient.getRegionRenderCache( chest.getWorld(), pos );
		IBlockState state = world.getBlockState( pos );
		state = state.withProperty( BlockReinforcedChest.MATERIAL, chest.getMaterial() );
		if( state.getPropertyKeys().contains( Properties.StaticProperty ) )
			state = state.withProperty( Properties.StaticProperty, false );
		EnumFacing facing = EnumFacing.NORTH;
		if( state.getPropertyKeys().contains( BlockHorizontal.FACING ) )
			facing = state.getValue( BlockHorizontal.FACING );

		float openAngle = chest.prevLidAngle + ( chest.lidAngle - chest.prevLidAngle ) * partialTicks;
		openAngle = 1.0F - openAngle;
		openAngle = 1.0F - openAngle * openAngle * openAngle;
		openAngle = openAngle * 90;

		final Vec3i offsetAxis = facing.getDirectionVec();
		final Vec3i openAxis = facing.rotateY().getDirectionVec();

		GlStateManager.translate( x, y, z );

		switch( facing )
		{
		case NORTH:
			GlStateManager.translate( 0, 9.5 / 16.0, 15.0 / 16.0 );
			break;
		case WEST:
			GlStateManager.translate( 15.0 / 16.0, 9.5 / 16.0, 0 );
			break;
		case SOUTH:
			GlStateManager.translate( 0, 9.5 / 16.0, 1.0 / 16.0 );
			break;
		case EAST:
			GlStateManager.translate( 1.0 / 16.0, 9.5 / 16.0, 0 );
			break;
		default:
			GlStateManager.translate( 0, 9.5 / 16.0, 0 );
			break;

		}
		GlStateManager.rotate( openAngle, openAxis.getX(), openAxis.getY(), openAxis.getZ() );
		switch( facing )
		{
		case SOUTH:
			GlStateManager.translate( 0, 0, -16.0 / 16.0 );
			break;
		case EAST:
			GlStateManager.translate( -16.0 / 16.0, 0, 0 );
			break;
		default:
			break;

		}

		// RenderHelper.disableStandardItemLighting();
		bindTexture( TextureMap.LOCATION_BLOCKS_TEXTURE );
		if( Minecraft.isAmbientOcclusionEnabled() )
			GlStateManager.shadeModel( GL11.GL_SMOOTH );
		else
			GlStateManager.shadeModel( GL11.GL_FLAT );

		GlStateManager.blendFunc( GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA );
		GlStateManager.enableBlend();
		GlStateManager.disableCull();

		GlStateManager.translate( -pos.getX(), -pos.getY(), -pos.getZ() );

		if( blockRenderer == null )
			blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
		final IBakedModel model = blockRenderer.getBlockModelShapes().getModelForState( state );

		final Tessellator tessellator = Tessellator.getInstance();
		final VertexBuffer VertexBuffer = tessellator.getBuffer();
		VertexBuffer.begin( GL11.GL_QUADS, DefaultVertexFormats.BLOCK );
		blockRenderer.getBlockModelRenderer().renderModel( world, model, state, pos, VertexBuffer, false );
		tessellator.draw();

		// RenderHelper.enableStandardItemLighting();
		GlStateManager.popMatrix();
		GlStateManager.popAttrib();
	}
}
