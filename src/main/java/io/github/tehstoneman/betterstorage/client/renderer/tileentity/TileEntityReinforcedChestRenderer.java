package io.github.tehstoneman.betterstorage.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import io.github.tehstoneman.betterstorage.api.ConnectedType;
import io.github.tehstoneman.betterstorage.client.renderer.Resources;
import io.github.tehstoneman.betterstorage.client.renderer.tileentity.model.ChestModel;
import io.github.tehstoneman.betterstorage.client.renderer.tileentity.model.LargeChestModel;
import io.github.tehstoneman.betterstorage.common.block.BetterStorageBlocks;
import io.github.tehstoneman.betterstorage.common.block.BlockConnectableContainer;
import io.github.tehstoneman.betterstorage.common.block.BlockReinforcedChest;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityReinforcedChest;
import io.github.tehstoneman.betterstorage.common.world.storage.HexKeyConfig;
import io.github.tehstoneman.betterstorage.config.BetterStorageConfig;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelManager;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelDataManager;
import net.minecraftforge.client.model.data.IModelData;

public class TileEntityReinforcedChestRenderer extends TileEntityRenderer< TileEntityReinforcedChest >
{
	private final ChestModel				simpleChest	= new ChestModel();
	private final ChestModel				largeChest	= new LargeChestModel();
	private HexKeyConfig					config;

	private static ItemRenderer				itemRenderer;
	private static BlockRendererDispatcher	blockRenderer;
	private static ModelManager				modelManager;

	public TileEntityReinforcedChestRenderer( TileEntityRendererDispatcher rendererDispatcher )
	{
		super( rendererDispatcher );
	}

	@Override
	public void render( TileEntityReinforcedChest tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer,
			int combinedLight, int combinedOverlay )
	{
		final BlockState blockState = tileEntity.hasLevel() ? tileEntity.getBlockState()
				: BetterStorageBlocks.REINFORCED_CHEST.get().defaultBlockState().setValue( BlockReinforcedChest.FACING, Direction.SOUTH );
		final ConnectedType chestType = blockState.hasProperty( BlockConnectableContainer.TYPE ) ? blockState.getValue( BlockConnectableContainer.TYPE )
				: ConnectedType.SINGLE;
		final Direction facing = blockState.getValue( BlockReinforcedChest.FACING );

		if( chestType != ConnectedType.SLAVE )
		{
			matrixStack.pushPose();

			config = tileEntity.getConfig();
			final boolean flag = chestType != ConnectedType.SINGLE;
			if( blockRenderer == null )
				blockRenderer = Minecraft.getInstance().getBlockRenderer();
			if( modelManager == null )
				modelManager = Minecraft.getInstance().getModelManager();

			final float f = facing.toYRot();
			matrixStack.translate( 0.5D, 0.5D, 0.5D );
			matrixStack.mulPose( Vector3f.YP.rotationDegrees( -f ) );
			matrixStack.translate( -0.5D, -0.5D, -0.5D );

			if( BetterStorageConfig.CLIENT.useObjModels.get() )
			{
				// Get obj model - disabled due to render issues in Forge
				final IBakedModel modelBase = modelManager
						.getModel( chestType == ConnectedType.SINGLE ? Resources.MODEL_REINFORCED_CHEST : Resources.MODEL_REINFORCED_CHEST_LARGE );
				final IBakedModel modelLid = modelManager.getModel(
						chestType == ConnectedType.SINGLE ? Resources.MODEL_REINFORCED_CHEST_LID : Resources.MODEL_REINFORCED_CHEST_LID_LARGE );
				final IBakedModel modelFrame = modelManager.getModel(
						chestType == ConnectedType.SINGLE ? Resources.MODEL_REINFORCED_CHEST_FRAME : Resources.MODEL_REINFORCED_CHEST_LARGE_FRAME );
				final IBakedModel modelLidFrame = modelManager
						.getModel( chestType == ConnectedType.SINGLE ? Resources.MODEL_REINFORCED_CHEST_LID_FRAME
								: Resources.MODEL_REINFORCED_CHEST_LID_LARGE_FRAME );

				// Render obj model - disabled due to render issues in Forge
				final World world = tileEntity.getLevel();
				final BlockPos pos = tileEntity.getBlockPos();
				// final ChunkRenderCache lightReader = MinecraftForgeClient.getRegionRenderCache( world, pos );
				// final long random = blockState.getBlockPositionRandom( pos );
				final IVertexBuilder renderBufferChest = buffer.getBuffer( Atlases.solidBlockSheet() );

				RenderMaterial material = new RenderMaterial( PlayerContainer.BLOCK_ATLAS, Resources.TEXTURE_REINFORCED_FRAME );
				final ItemStack itemStack = config.getStackInSlot( HexKeyConfig.SLOT_APPEARANCE );
				if( !itemStack.isEmpty() )
				{
					final Item item = itemStack.getItem();
					if( item instanceof BlockItem )
					{
						final BlockState state = ( (BlockItem)item ).getBlock().defaultBlockState();
						final TextureAtlasSprite texture = Minecraft.getInstance().getBlockRenderer().getBlockModelShaper()
								.getTexture( state, world, pos );
						material = new RenderMaterial( PlayerContainer.BLOCK_ATLAS, texture.getName() );
					}
				}
				final IVertexBuilder renderBufferFrame = material.buffer( buffer, RenderType::entitySolid );
				final IModelData data = modelBase.getModelData( world, pos, blockState, ModelDataManager.getModelData( world, pos ) );

				final MatrixStack.Entry currentMatrix = matrixStack.last();

				blockRenderer.getModelRenderer().renderModel( currentMatrix, renderBufferChest, null, modelBase, 1.0f, 1.0f, 1.0f, combinedLight,
						combinedOverlay, data );
				blockRenderer.getModelRenderer().renderModel( currentMatrix, renderBufferFrame, null, modelFrame, 1.0f, 1.0f, 1.0f,
						combinedLight, combinedOverlay, data );

				rotateLid( tileEntity, partialTicks, matrixStack );

				blockRenderer.getModelRenderer().renderModel( currentMatrix, renderBufferChest, null, modelLid, 1.0f, 1.0f, 1.0f, combinedLight,
						combinedOverlay, data );
				blockRenderer.getModelRenderer().renderModel( currentMatrix, renderBufferFrame, null, modelLidFrame, 1.0f, 1.0f, 1.0f,
						combinedLight, combinedOverlay, data );
			}
			else
			{
				final ChestModel modelChest = getChestModel( tileEntity, flag );
				final RenderMaterial material = getMaterial( tileEntity, flag );
				final IVertexBuilder vertexBuilder = material.buffer( buffer, RenderType::entitySolid );
				rotateLid( tileEntity, partialTicks, modelChest );
				modelChest.renderToBuffer( matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F );
			}

			renderItem( tileEntity, partialTicks, matrixStack, buffer, combinedLight, blockState );

			matrixStack.popPose();
		}
	}

	private ChestModel getChestModel( TileEntityReinforcedChest tileEntityChest, boolean flag )
	{
		return flag ? largeChest : simpleChest;
	}

	protected RenderMaterial getMaterial( TileEntityReinforcedChest tileEntity, boolean flag )
	{
		final ResourceLocation resourcelocation = flag ? Resources.TEXTURE_CHEST_REINFORCED_DOUBLE : Resources.TEXTURE_CHEST_REINFORCED;
		return new RenderMaterial( PlayerContainer.BLOCK_ATLAS, resourcelocation );
	}

	private void rotateLid( TileEntityReinforcedChest tileEntityChest, float partialTicks, ChestModel modelchest )
	{
		float angle = ( (IChestLid)tileEntityChest ).getOpenNess( partialTicks );
		angle = 1.0F - angle;
		angle = 1.0F - angle * angle * angle;
		modelchest.rotateLid( angle );
	}

	private void rotateLid( TileEntityReinforcedChest tileEntityChest, float partialTicks, MatrixStack matrixStack )
	{
		float angle = ( (IChestLid)tileEntityChest ).getOpenNess( partialTicks );
		angle = 1.0F - angle;
		angle = 1.0F - angle * angle * angle;
		matrixStack.translate( 0.0, 0.5625, 0.0 );
		matrixStack.mulPose( Vector3f.XP.rotation( -angle ) );
	}

	/**
	 * Renders attached lock on chest. Adapted from vanilla item frame
	 *
	 * @param chest
	 * @param partialTicks
	 * @param matrixStack
	 * @param buffer
	 * @param packedLight
	 * @param state
	 */
	private void renderItem( TileEntityReinforcedChest chest, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight,
			BlockState state )
	{
		final ItemStack itemstack = chest.getLock();

		if( !itemstack.isEmpty() )
		{
			if( itemRenderer == null )
				itemRenderer = Minecraft.getInstance().getItemRenderer();

			matrixStack.translate( chest.isConnected() ? 1.0 : 0.5,-0.1875, 0.9375 );

			matrixStack.scale( 0.5F, 0.5F, 0.5F );
			itemRenderer.renderStatic( itemstack, ItemCameraTransforms.TransformType.FIXED, packedLight, OverlayTexture.NO_OVERLAY, matrixStack,
					buffer );
		}
	}
}
