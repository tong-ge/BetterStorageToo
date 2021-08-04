package io.github.tehstoneman.betterstorage.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import io.github.tehstoneman.betterstorage.common.block.BlockTank;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityTank;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;

@OnlyIn( Dist.CLIENT )
public class TileEntityTankRenderer extends TileEntityRenderer< TileEntityTank >
{
	private static final int TANK_THICKNESS = 2;

	public TileEntityTankRenderer( TileEntityRendererDispatcher rendererDispatcher )
	{
		super( rendererDispatcher );
	}

	@Override
	public void render( TileEntityTank tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight,
			int combinedOverlay )
	{
		final int capacity = tileEntity.getCapacity();
		final FluidStack fluid = tileEntity.getFluid();
		final Fluid renderFluid = fluid.getFluid();

		if( !fluid.isEmpty() )
		{
			matrixStack.pushPose();

			final ResourceLocation fluidStill = renderFluid.getAttributes().getStillTexture();
			final ResourceLocation fluidFlow = renderFluid.getAttributes().getFlowingTexture();
			final TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas( PlayerContainer.BLOCK_ATLAS ).apply( fluidStill );
			final TextureAtlasSprite flow = Minecraft.getInstance().getTextureAtlas( PlayerContainer.BLOCK_ATLAS ).apply( fluidFlow );

			final IVertexBuilder builder = buffer.getBuffer( RenderType.translucent() );

			final float scale = ( 1.0f - TANK_THICKNESS / 2 - TANK_THICKNESS ) * fluid.getAmount() / tileEntity.getCapacity();

			final Quaternion rotation = Vector3f.YP.rotationDegrees( 0 );

			final int color = renderFluid.getAttributes().getColor();

			final float a = 1.0F;
			final float r = ( color >> 16 & 0xFF ) / 255.0F;
			final float g = ( color >> 8 & 0xFF ) / 255.0F;
			final float b = ( color & 0xFF ) / 255.0F;

			final BlockState blockState = tileEntity.getBlockState();
			final float level = fluid.getAmount() / (float)tileEntity.getCapacity();
			final float top = blockState.getValue( BlockTank.UP ) ? 1.0f : ( 16 - TANK_THICKNESS ) / 16f;
			final float btm = blockState.getValue( BlockTank.DOWN ) ? 0.0f : TANK_THICKNESS / 16f;

			final float x1 = TANK_THICKNESS / 16f;
			final float x2 = ( 16 - TANK_THICKNESS ) / 16f;

			final float diff = top - btm;

			final float y1 = renderFluid.getAttributes().isGaseous() ? top - diff * level : btm;
			final float y2 = renderFluid.getAttributes().isGaseous() ? top : btm + diff * level;

			final float u1 = x1 * 16;
			final float u2 = x2 * 16;
			final float v1 = (1-y1) * 16;
			final float v2 = (1-y2) * 16;

			if( tileEntity.getFluidAmountAbove() <= 0 )
			{
				// Top Face
				add( builder, matrixStack, x1, y2, x1, sprite.getU( u1 ), sprite.getV( u1 ), r, g, b, a );
				add( builder, matrixStack, x1, y2, x2, sprite.getU( u1 ), sprite.getV( u2 ), r, g, b, a );
				add( builder, matrixStack, x2, y2, x2, sprite.getU( u2 ), sprite.getV( u2 ), r, g, b, a );
				add( builder, matrixStack, x2, y2, x1, sprite.getU( u2 ), sprite.getV( u1 ), r, g, b, a );

				//add( builder, matrixStack, x2, y2, x1, sprite.getU( u2 ), sprite.getV( u1 ), r, g, b, a );
				//add( builder, matrixStack, x2, y2, x2, sprite.getU( u2 ), sprite.getV( u2 ), r, g, b, a );
				//add( builder, matrixStack, x1, y2, x2, sprite.getU( u1 ), sprite.getV( u2 ), r, g, b, a );
				//add( builder, matrixStack, x1, y2, x1, sprite.getU( u1 ), sprite.getV( u1 ), r, g, b, a );
			}

			// North Face
			add( builder, matrixStack, x1, y1, x1, sprite.getU( u2 ), sprite.getV( v1 ), r, g, b, a );
			add( builder, matrixStack, x1, y2, x1, sprite.getU( u2 ), sprite.getV( v2 ), r, g, b, a );
			add( builder, matrixStack, x2, y2, x1, sprite.getU( u1 ), sprite.getV( v2 ), r, g, b, a );
			add( builder, matrixStack, x2, y1, x1, sprite.getU( u1 ), sprite.getV( v1 ), r, g, b, a );

			// South Face
			add( builder, matrixStack, x2, y1, x2, sprite.getU( u2 ), sprite.getV( v1 ), r, g, b, a );
			add( builder, matrixStack, x2, y2, x2, sprite.getU( u2 ), sprite.getV( v2 ), r, g, b, a );
			add( builder, matrixStack, x1, y2, x2, sprite.getU( u1 ), sprite.getV( v2 ), r, g, b, a );
			add( builder, matrixStack, x1, y1, x2, sprite.getU( u1 ), sprite.getV( v1 ), r, g, b, a );

			// East face
			add( builder, matrixStack, x2, y1, x1, sprite.getU( u2 ), sprite.getV( v1 ), r, g, b, a );
			add( builder, matrixStack, x2, y2, x1, sprite.getU( u2 ), sprite.getV( v2 ), r, g, b, a );
			add( builder, matrixStack, x2, y2, x2, sprite.getU( u1 ), sprite.getV( v2 ), r, g, b, a );
			add( builder, matrixStack, x2, y1, x2, sprite.getU( u1 ), sprite.getV( v1 ), r, g, b, a );

			// West face
			add( builder, matrixStack, x1, y1, x2, sprite.getU( u2 ), sprite.getV( v1 ), r, g, b, a );
			add( builder, matrixStack, x1, y2, x2, sprite.getU( u2 ), sprite.getV( v2 ), r, g, b, a );
			add( builder, matrixStack, x1, y2, x1, sprite.getU( u1 ), sprite.getV( v2 ), r, g, b, a );
			add( builder, matrixStack, x1, y1, x1, sprite.getU( u1 ), sprite.getV( v1 ), r, g, b, a );

			if( tileEntity.getFluidAmountBelow() <= 0 )
			{
				// Bottom Face
				add( builder, matrixStack, x2, y1, x1, sprite.getU( u2 ), sprite.getV( u1 ), r, g, b, a );
				add( builder, matrixStack, x2, y1, x2, sprite.getU( u2 ), sprite.getV( u2 ), r, g, b, a );
				add( builder, matrixStack, x1, y1, x2, sprite.getU( u1 ), sprite.getV( u2 ), r, g, b, a );
				add( builder, matrixStack, x1, y1, x1, sprite.getU( u1 ), sprite.getV( u1 ), r, g, b, a );

				//add( builder, matrixStack, x1, y1, x1, sprite.getU( u1 ), sprite.getV( u1 ), r, g, b, a );
				//add( builder, matrixStack, x1, y1, x2, sprite.getU( u1 ), sprite.getV( u2 ), r, g, b, a );
				//add( builder, matrixStack, x2, y1, x2, sprite.getU( u2 ), sprite.getV( u2 ), r, g, b, a );
				//add( builder, matrixStack, x2, y1, x1, sprite.getU( u2 ), sprite.getV( u1 ), r, g, b, a );
			}

			matrixStack.popPose();

		}
	}

	private void add( IVertexBuilder renderer, MatrixStack stack, float x, float y, float z, float u, float v, float r, float g, float b, float a )
	{
		renderer.vertex( stack.last().pose(), x, y, z ).color( r, g, b, a ).uv( u, v ).uv2( 0, 240 ).normal( 1, 0, 0 ).endVertex();
	}
}
