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
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.model.data.EmptyModelData;

public class ObjRenderer
{
	public static void render( TileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderBuffer, int combinedLight,
			int combinedOverlay )
	{
		matrixStack.push();

		final BlockState tileState = tileEntity.getBlockState();
		final BlockState state = BetterStorageBlocks.REINFORCED_CHEST.get().getDefaultState().with( BlockConnectableContainer.TYPE,
				tileState.get( BlockConnectableContainer.TYPE ) );
		final BlockRendererDispatcher dispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
		final IBakedModel model = dispatcher.getModelForState( state );

		// BetterStorage.LOGGER.info( "ObjRenderer ==== {} ====", model );
		final float f = tileState.get( BlockReinforcedChest.FACING ).getHorizontalAngle();
		matrixStack.translate( 0.5D, 0.5D, 0.5D );
		matrixStack.rotate( Vector3f.YP.rotationDegrees( -f ) );
		matrixStack.translate( -0.5D, -0.5D, -0.5D );

		// You can use one of the various BlockRenderer render methods depending on how much control you want to retain over the model's appearance
		// To render the model as if it were a block placed in the world (lighting, shading, etc) you can use
		// the renderModel() with World (ILightRenderer) as the first argument.
		final World world = tileEntity.getWorld();
		if( world == null )
			return;
		final BlockPos blockPos = tileEntity.getPos();

		// To render the model with more control over lighting, colour, etc, use the renderModel() shown below.

		final MatrixStack.Entry currentMatrix = matrixStack.getLast();

		final float red = 1.0f;
		final float green = 1.0f;
		final float blue = 1.0f;

		final IVertexBuilder vertexBuffer = renderBuffer.getBuffer( RenderType.getSolid() );
		dispatcher.getBlockModelRenderer().renderModel( currentMatrix, vertexBuffer, state, model, red, green, blue, combinedLight, combinedOverlay,
				EmptyModelData.INSTANCE );
		dispatcher.getBlockModelRenderer().renderModel( world, model, state, blockPos, matrixStack, vertexBuffer, true, BetterStorage.RANDOM, 42,
				combinedOverlay, EmptyModelData.INSTANCE );

		matrixStack.pop();
	}
}
