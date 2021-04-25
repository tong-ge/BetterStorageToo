package io.github.tehstoneman.betterstorage.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import io.github.tehstoneman.betterstorage.client.renderer.Resources;
import io.github.tehstoneman.betterstorage.client.renderer.tileentity.model.StorageBinModel;
import io.github.tehstoneman.betterstorage.common.block.BetterStorageBlocks;
import io.github.tehstoneman.betterstorage.common.block.BlockLocker;
import io.github.tehstoneman.betterstorage.common.block.BlockReinforcedChest;
import io.github.tehstoneman.betterstorage.common.tileentity.BinTileEntity;
import io.github.tehstoneman.betterstorage.common.world.storage.HexKeyConfig;
import io.github.tehstoneman.betterstorage.config.BetterStorageConfig;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelManager;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelDataManager;
import net.minecraftforge.client.model.data.IModelData;

public class StorageBinTileEntityRenderer extends TileEntityRenderer< BinTileEntity >
{
	private final StorageBinModel			simpleLocker	= new StorageBinModel();
	private HexKeyConfig					config;

	private static BlockRendererDispatcher	blockRenderer;
	private static ModelManager				modelManager;

	public StorageBinTileEntityRenderer( TileEntityRendererDispatcher rendererDispatcher )
	{
		super( rendererDispatcher );
	}

	@Override
	public void render( BinTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight,
			int combinedOverlay )
	{
		final BlockState blockState = tileEntity.hasWorld() ? tileEntity.getBlockState()
				: BetterStorageBlocks.LOCKER.get().getDefaultState().with( BlockLocker.FACING, Direction.SOUTH );
		final Direction facing = blockState.get( BlockReinforcedChest.FACING );

		if( tileEntity instanceof BinTileEntity )
			config = tileEntity.getConfig();
		if( blockRenderer == null )
			blockRenderer = Minecraft.getInstance().getBlockRendererDispatcher();
		if( modelManager == null )
			modelManager = Minecraft.getInstance().getModelManager();

		matrixStack.push();

		final float f = facing.getHorizontalAngle();
		matrixStack.translate( 0.5, 0.5, 0.5 );
		matrixStack.rotate( Vector3f.YP.rotationDegrees( -f ) );
		matrixStack.translate( -0.5, -0.5, -0.5 );

		if( tileEntity instanceof BinTileEntity && BetterStorageConfig.CLIENT.useObjModels.get() )
		{
			final IBakedModel modelBase = modelManager.getModel( Resources.MODEL_STORAGE_BIN );
			final IBakedModel modelFrame = modelManager.getModel( Resources.MODEL_STORAGE_BIN_FRAME );

			final World world = tileEntity.getWorld();
			final BlockPos pos = tileEntity.getPos();
			// final ChunkRenderCache lightReader = MinecraftForgeClient.getRegionRenderCache( world, pos );
			// final long random = blockState.getPositionRandom( pos );
			final IVertexBuilder renderBufferLocker = buffer.getBuffer( Atlases.getSolidBlockType() );

			RenderMaterial material = new RenderMaterial( PlayerContainer.LOCATION_BLOCKS_TEXTURE, Resources.TEXTURE_STORAGE_BIN );
			final ItemStack itemStack = config.getStackInSlot( HexKeyConfig.SLOT_APPEARANCE );
			if( !itemStack.isEmpty() )
			{
				final Item item = itemStack.getItem();
				if( item instanceof BlockItem )
				{
					final BlockState state = ( (BlockItem)item ).getBlock().getDefaultState();
					final TextureAtlasSprite texture = Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelShapes().getTexture( state,
							world, pos );
					material = new RenderMaterial( PlayerContainer.LOCATION_BLOCKS_TEXTURE, texture.getName() );
				}
			}
			final IVertexBuilder renderBufferFrame = material.getBuffer( buffer, RenderType::getEntitySolid );
			final IModelData data = modelBase.getModelData( world, pos, blockState, ModelDataManager.getModelData( world, pos ) );

			final MatrixStack.Entry currentMatrix = matrixStack.getLast();

			blockRenderer.getBlockModelRenderer().renderModel( currentMatrix, renderBufferLocker, null, modelBase, 1.0f, 1.0f, 1.0f, combinedLight,
					combinedOverlay, data );
			blockRenderer.getBlockModelRenderer().renderModel( currentMatrix, renderBufferFrame, null, modelFrame, 1.0f, 1.0f, 1.0f, combinedLight,
					combinedOverlay, data );
		}
		else
		{
			final RenderMaterial material = new RenderMaterial( PlayerContainer.LOCATION_BLOCKS_TEXTURE, Resources.TEXTURE_STORAGE_BIN );
			final IVertexBuilder vertexBuilder = material.getBuffer( buffer, RenderType::getEntityCutout );

			simpleLocker.render( matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F );
		}

		matrixStack.pop();
	}
}
