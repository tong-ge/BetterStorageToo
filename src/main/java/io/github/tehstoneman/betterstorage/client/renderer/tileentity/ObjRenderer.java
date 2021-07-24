package io.github.tehstoneman.betterstorage.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.common.block.BetterStorageBlocks;
import io.github.tehstoneman.betterstorage.common.block.BlockConnectableContainer;
import io.github.tehstoneman.betterstorage.common.block.BlockReinforcedChest;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraftforge.client.model.data.EmptyModelData;

public class ObjRenderer
{
	public static void render( TileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderBuffer, int combinedLight,
			int combinedOverlay )
	{
		matrixStack.pushPose();

		final BlockState tileState = tileEntity.getBlockState();
		final BlockState state = BetterStorageBlocks.REINFORCED_CHEST.get().defaultBlockState().setValue( BlockConnectableContainer.TYPE,
				tileState.getValue( BlockConnectableContainer.TYPE ) );
		final BlockRendererDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
		final IBakedModel model = dispatcher.getBlockModel( state );

		// BetterStorage.LOGGER.info( "ObjRenderer ==== {} ====", model );
		final float f = tileState.getValue( BlockReinforcedChest.FACING ).toYRot();
		matrixStack.translate( 0.5D, 0.5D, 0.5D );
		matrixStack.mulPose( Vector3f.YP.rotationDegrees( -f ) );
		matrixStack.translate( -0.5D, -0.5D, -0.5D );

		// You can use one of the various BlockRenderer render methods depending on how much control you want to retain over the model's appearance
		// To render the model as if it were a block placed in the world (lighting, shading, etc) you can use
		// the renderModel() with World (ILightRenderer) as the first argument.
		final World world = tileEntity.getLevel();
		if( world == null )
			return;
		final BlockPos blockPos = tileEntity.getBlockPos();

		// To render the model with more control over lighting, colour, etc, use the renderModel() shown below.

		final MatrixStack.Entry currentMatrix = matrixStack.last();

		final float red = 1.0f;
		final float green = 1.0f;
		final float blue = 1.0f;

		final IVertexBuilder vertexBuffer = renderBuffer.getBuffer( RenderType.solid() );
		dispatcher.getModelRenderer().renderModel( currentMatrix, vertexBuffer, state, model, red, green, blue, combinedLight, combinedOverlay,
				EmptyModelData.INSTANCE );
		dispatcher.getModelRenderer().renderModel( world, model, state, blockPos, matrixStack, vertexBuffer, true, BetterStorage.RANDOM, 42,
				combinedOverlay, EmptyModelData.INSTANCE );

		matrixStack.popPose();
	}
}
