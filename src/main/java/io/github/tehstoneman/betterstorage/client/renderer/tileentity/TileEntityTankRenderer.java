package io.github.tehstoneman.betterstorage.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import io.github.tehstoneman.betterstorage.client.renderer.Resources;
import io.github.tehstoneman.betterstorage.client.renderer.tileentity.model.ModelTankFluid;
import io.github.tehstoneman.betterstorage.common.block.BlockTank;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityTank;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;

public class TileEntityTankRenderer extends TileEntityRenderer< TileEntityTank >
{
	private final ModelTankFluid tankFluidModel = new ModelTankFluid();

	public TileEntityTankRenderer( TileEntityRendererDispatcher rendererDispatcher )
	{
		super( rendererDispatcher );
	}

	@Override
	public void render( TileEntityTank tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight,
			int combinedOverlay )
	{
		final int capacity = tileEntity.getCapacity();
		final FluidStack fluidStack = tileEntity.getFluid();
		final Fluid fluid = fluidStack.getFluid();

		if( !fluidStack.isEmpty() )
		{
			matrixStack.push();

			final double posY = 0.0625 + 0.875 * ( (float)fluidStack.getAmount() / (float)capacity );
			final MaterialColor mapColor = fluid.getDefaultState().getBlockState().getMaterial().getColor();
			final int color = mapColor.colorValue;
			final float alpha = ( color >> 24 & 255 ) / 255.0F;
			final float red = ( color >> 16 & 255 ) / 255.0F;
			final float green = ( color >> 8 & 255 ) / 255.0F;
			final float blue = ( color & 255 ) / 255.0F;

			final BlockPos pos = tileEntity.getPos();
			final BlockState blockState = tileEntity.getBlockState();

			final Material material = new Material( PlayerContainer.LOCATION_BLOCKS_TEXTURE, Resources.TEXTURE_WHITE );
			final IVertexBuilder vertexBuilder = material.getBuffer( buffer, RenderType::getEntityCutout );

			tankFluidModel.setLevel( (float)fluidStack.getAmount() / (float)capacity, blockState.get( BlockTank.UP ),
					blockState.get( BlockTank.DOWN ), fluid.getAttributes().isLighterThanAir() );
			tankFluidModel.render( matrixStack, vertexBuilder, combinedLight, combinedOverlay, red, green, blue, 1.0F );

			matrixStack.pop();

		}
	}
}
