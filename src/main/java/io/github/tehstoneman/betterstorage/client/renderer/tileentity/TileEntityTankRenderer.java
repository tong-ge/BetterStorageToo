package io.github.tehstoneman.betterstorage.client.renderer.tileentity;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.GlStateManager;

import io.github.tehstoneman.betterstorage.common.block.BlockTank;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityTank;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;

public class TileEntityTankRenderer extends TileEntityRenderer< TileEntityTank >
{
	private static AtlasTexture textureAtlas;

	@Override
	public void render( TileEntityTank tileEntity, double x, double y, double z, float partialTicks, int destroyStage )
	{
		final int capacity = tileEntity.getCapacity();
		final FluidStack fluidStack = tileEntity.getFluid();
		final Fluid fluid = fluidStack.getFluid();

		if( !fluidStack.isEmpty() )
		{
			GlStateManager.pushMatrix();
			RenderHelper.disableStandardItemLighting();
			GlStateManager.blendFunc( GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA );
			GlStateManager.enableBlend();

			if( Minecraft.isAmbientOcclusionEnabled() )
				GlStateManager.shadeModel( GL11.GL_SMOOTH );
			else
				GlStateManager.shadeModel( GL11.GL_FLAT );

			if( textureAtlas == null )
				textureAtlas = Minecraft.getInstance().getTextureMap();
			bindTexture( net.minecraft.client.renderer.texture.AtlasTexture.LOCATION_BLOCKS_TEXTURE );

			final ResourceLocation fuildStillTexture = fluid.getAttributes().getStillTexture();
			final TextureAtlasSprite fluidStillSprite = textureAtlas.getAtlasSprite( fuildStillTexture.toString() );

			final double posY = 0.0625 + 0.875 * ( (float)fluidStack.getAmount() / (float)capacity );
			final int color = fluid.getAttributes().getColor();
			final float alpha = ( color >> 24 & 255 ) / 255.0F;
			final float red = ( color >> 16 & 255 ) / 255.0F;
			final float green = ( color >> 8 & 255 ) / 255.0F;
			final float blue = ( color & 255 ) / 255.0F;

			final BlockPos pos = tileEntity.getPos();
			final BlockState blockState = tileEntity.getBlockState();

			final int packedLightMap = blockState.getPackedLightmapCoords( getWorld(), pos );
			final int skyLight = packedLightMap >> 16 & 0xFFFF;
			final int blockLight = packedLightMap & 0xFFFF;

			final double height = 0.875 + ( blockState.get( BlockTank.UP ) ? 0.0625 : 0.0 ) + ( blockState.get( BlockTank.DOWN ) ? 0.0625 : 0.0 );

			final double x1 = 0.125;
			double y1 = blockState.get( BlockTank.DOWN ) ? 0.0 : 0.0625;
			final double z1 = 0.125;
			final double x2 = 0.875;
			double y2 = y1 + height * ( (float)fluidStack.getAmount() / (float)capacity );
			final double z2 = 0.875;

			if( fluid.getAttributes().isLighterThanAir() )
			{
				y2 = blockState.get( BlockTank.UP ) ? 1.0 : 0.9375;
				y1 = y2 - height * ( (float)fluidStack.getAmount() / (float)capacity );
			}
			final double u1 = fluidStillSprite.getInterpolatedU( x1 * 16 );
			final double v1 = fluidStillSprite.getInterpolatedV( z1 * 16 );
			final double u2 = fluidStillSprite.getInterpolatedU( x2 * 16 );
			final double v2 = fluidStillSprite.getInterpolatedV( z2 * 16 );
			final double u3 = fluidStillSprite.getInterpolatedU( z1 * 16 );
			final double v3 = fluidStillSprite.getInterpolatedV( y1 * 16 );
			final double u4 = fluidStillSprite.getInterpolatedU( z2 * 16 );
			final double v4 = fluidStillSprite.getInterpolatedV( y2 * 16 );

			final Tessellator tesselator = Tessellator.getInstance();
			final BufferBuilder buffer = tesselator.getBuffer();

			buffer.begin( GL11.GL_QUADS, DefaultVertexFormats.BLOCK );
			buffer.setTranslation( x, y, z );

			// Top
			if( y2 < 1.0 || tileEntity.getFluidAmountAbove() == 0 )
			{
				buffer.pos( x1, y2, z1 ).color( red, green, blue, alpha ).tex( u1, v1 ).lightmap( skyLight, blockLight ).endVertex();
				buffer.pos( x1, y2, z2 ).color( red, green, blue, alpha ).tex( u1, v2 ).lightmap( skyLight, blockLight ).endVertex();
				buffer.pos( x2, y2, z2 ).color( red, green, blue, alpha ).tex( u2, v2 ).lightmap( skyLight, blockLight ).endVertex();
				buffer.pos( x2, y2, z1 ).color( red, green, blue, alpha ).tex( u2, v1 ).lightmap( skyLight, blockLight ).endVertex();
			}

			// Bottom
			if( y1 > 0.0 || tileEntity.getFluidAmountBelow() == 0 )
			{
				buffer.pos( x1, y1, z1 ).color( red, green, blue, alpha ).tex( u1, v1 ).lightmap( skyLight, blockLight ).endVertex();
				buffer.pos( x2, y1, z1 ).color( red, green, blue, alpha ).tex( u2, v1 ).lightmap( skyLight, blockLight ).endVertex();
				buffer.pos( x2, y1, z2 ).color( red, green, blue, alpha ).tex( u2, v2 ).lightmap( skyLight, blockLight ).endVertex();
				buffer.pos( x1, y1, z2 ).color( red, green, blue, alpha ).tex( u1, v2 ).lightmap( skyLight, blockLight ).endVertex();
			}

			// North
			buffer.pos( x2, y2, z1 ).color( red, green, blue, alpha ).tex( u2, v4 ).lightmap( skyLight, blockLight ).endVertex();
			buffer.pos( x2, y1, z1 ).color( red, green, blue, alpha ).tex( u2, v3 ).lightmap( skyLight, blockLight ).endVertex();
			buffer.pos( x1, y1, z1 ).color( red, green, blue, alpha ).tex( u1, v3 ).lightmap( skyLight, blockLight ).endVertex();
			buffer.pos( x1, y2, z1 ).color( red, green, blue, alpha ).tex( u1, v4 ).lightmap( skyLight, blockLight ).endVertex();

			// East
			buffer.pos( x2, y2, z2 ).color( red, green, blue, alpha ).tex( u4, v4 ).lightmap( skyLight, blockLight ).endVertex();
			buffer.pos( x2, y1, z2 ).color( red, green, blue, alpha ).tex( u4, v3 ).lightmap( skyLight, blockLight ).endVertex();
			buffer.pos( x2, y1, z1 ).color( red, green, blue, alpha ).tex( u3, v3 ).lightmap( skyLight, blockLight ).endVertex();
			buffer.pos( x2, y2, z1 ).color( red, green, blue, alpha ).tex( u3, v4 ).lightmap( skyLight, blockLight ).endVertex();

			// South
			buffer.pos( x1, y2, z2 ).color( red, green, blue, alpha ).tex( u1, v4 ).lightmap( skyLight, blockLight ).endVertex();
			buffer.pos( x1, y1, z2 ).color( red, green, blue, alpha ).tex( u1, v3 ).lightmap( skyLight, blockLight ).endVertex();
			buffer.pos( x2, y1, z2 ).color( red, green, blue, alpha ).tex( u2, v3 ).lightmap( skyLight, blockLight ).endVertex();
			buffer.pos( x2, y2, z2 ).color( red, green, blue, alpha ).tex( u2, v4 ).lightmap( skyLight, blockLight ).endVertex();

			// West
			buffer.pos( x1, y2, z1 ).color( red, green, blue, alpha ).tex( u3, v4 ).lightmap( skyLight, blockLight ).endVertex();
			buffer.pos( x1, y1, z1 ).color( red, green, blue, alpha ).tex( u3, v3 ).lightmap( skyLight, blockLight ).endVertex();
			buffer.pos( x1, y1, z2 ).color( red, green, blue, alpha ).tex( u4, v3 ).lightmap( skyLight, blockLight ).endVertex();
			buffer.pos( x1, y2, z2 ).color( red, green, blue, alpha ).tex( u4, v4 ).lightmap( skyLight, blockLight ).endVertex();

			tesselator.draw();

			buffer.setTranslation( 0, 0, 0 );
			RenderHelper.enableStandardItemLighting();
			GlStateManager.popMatrix();
		}
	}
}
