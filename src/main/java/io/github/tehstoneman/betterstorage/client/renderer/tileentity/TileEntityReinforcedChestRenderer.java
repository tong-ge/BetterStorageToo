package io.github.tehstoneman.betterstorage.client.renderer.tileentity;

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
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.ModelDataManager;
import net.minecraftforge.client.model.data.IModelData;

//@OnlyIn( Dist.CLIENT )
public class TileEntityReinforcedChestRenderer// extends BlockEntityRenderer< TileEntityReinforcedChest >
{
	/*
	 * private final ChestModel simpleChest = new ChestModel();
	 * private final ChestModel largeChest = new LargeChestModel();
	 * private HexKeyConfig config;
	 * 
	 * private static ItemRenderer itemRenderer;
	 * private static BlockRendererDispatcher blockRenderer;
	 * private static ModelManager modelManager;
	 * 
	 * public TileEntityReinforcedChestRenderer( BlockEntityRenderDispatcher rendererDispatcher )
	 * {
	 * super( rendererDispatcher );
	 * }
	 * 
	 * @Override
	 * public void render( TileEntityReinforcedChest tileEntity, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer,
	 * int combinedLight, int combinedOverlay )
	 * {
	 * final BlockState blockState = tileEntity.hasLevel() ? tileEntity.getBlockState()
	 * : BetterStorageBlocks.REINFORCED_CHEST.get().defaultBlockState().setValue( BlockReinforcedChest.FACING, Direction.SOUTH );
	 * final ConnectedType chestType = blockState.hasProperty( BlockConnectableContainer.TYPE )
	 * ? blockState.getValue( BlockConnectableContainer.TYPE )
	 * : ConnectedType.SINGLE;
	 * final Direction facing = blockState.getValue( BlockReinforcedChest.FACING );
	 * 
	 * if( chestType != ConnectedType.SLAVE )
	 * {
	 * matrixStack.pushPose();
	 * 
	 * config = tileEntity.getConfig();
	 * final boolean flag = chestType != ConnectedType.SINGLE;
	 * if( blockRenderer == null )
	 * blockRenderer = Minecraft.getInstance().getBlockRenderer();
	 * if( modelManager == null )
	 * modelManager = Minecraft.getInstance().getModelManager();
	 * 
	 * final float f = facing.toYRot();
	 * matrixStack.translate( 0.5D, 0.5D, 0.5D );
	 * matrixStack.mulPose( Vector3f.YP.rotationDegrees( -f ) );
	 * matrixStack.translate( -0.5D, -0.5D, -0.5D );
	 * 
	 * if( BetterStorageConfig.CLIENT.useObjModels.get() )
	 * {
	 * // Get obj model - disabled due to render issues in Forge
	 * final IBakedModel modelBase = modelManager
	 * .getModel( chestType == ConnectedType.SINGLE ? Resources.MODEL_REINFORCED_CHEST : Resources.MODEL_REINFORCED_CHEST_LARGE );
	 * final IBakedModel modelLid = modelManager.getModel(
	 * chestType == ConnectedType.SINGLE ? Resources.MODEL_REINFORCED_CHEST_LID : Resources.MODEL_REINFORCED_CHEST_LID_LARGE );
	 * final IBakedModel modelFrame = modelManager.getModel(
	 * chestType == ConnectedType.SINGLE ? Resources.MODEL_REINFORCED_CHEST_FRAME : Resources.MODEL_REINFORCED_CHEST_LARGE_FRAME );
	 * final IBakedModel modelLidFrame = modelManager
	 * .getModel( chestType == ConnectedType.SINGLE ? Resources.MODEL_REINFORCED_CHEST_LID_FRAME
	 * : Resources.MODEL_REINFORCED_CHEST_LID_LARGE_FRAME );
	 * 
	 * // Render obj model - disabled due to render issues in Forge
	 * final Level world = tileEntity.getLevel();
	 * final BlockPos pos = tileEntity.getBlockPos();
	 * // final ChunkRenderCache lightReader = MinecraftForgeClient.getRegionRenderCache( world, pos );
	 * // final long random = blockState.getBlockPositionRandom( pos );
	 * final VertexConsumer renderBufferChest = buffer.getBuffer( Atlases.solidBlockSheet() );
	 * 
	 * Material material = new Material( InventoryMenu.BLOCK_ATLAS, Resources.TEXTURE_REINFORCED_FRAME );
	 * final ItemStack itemStack = config.getStackInSlot( HexKeyConfig.SLOT_APPEARANCE );
	 * if( !itemStack.isEmpty() )
	 * {
	 * final Item item = itemStack.getItem();
	 * if( item instanceof BlockItem )
	 * {
	 * final BlockState state = ( (BlockItem)item ).getBlock().defaultBlockState();
	 * final TextureAtlasSprite texture = Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getTexture( state, world,
	 * pos );
	 * material = new Material( InventoryMenu.BLOCK_ATLAS, texture.getName() );
	 * }
	 * }
	 * final VertexConsumer renderBufferFrame = material.buffer( buffer, RenderType::entitySolid );
	 * final IModelData data = modelBase.getModelData( world, pos, blockState, ModelDataManager.getModelData( world, pos ) );
	 * 
	 * final PoseStack.Entry currentMatrix = matrixStack.last();
	 * 
	 * blockRenderer.getModelRenderer().renderModel( currentMatrix, renderBufferChest, null, modelBase, 1.0f, 1.0f, 1.0f, combinedLight,
	 * combinedOverlay, data );
	 * blockRenderer.getModelRenderer().renderModel( currentMatrix, renderBufferFrame, null, modelFrame, 1.0f, 1.0f, 1.0f, combinedLight,
	 * combinedOverlay, data );
	 * 
	 * rotateLid( tileEntity, partialTicks, matrixStack );
	 * 
	 * blockRenderer.getModelRenderer().renderModel( currentMatrix, renderBufferChest, null, modelLid, 1.0f, 1.0f, 1.0f, combinedLight,
	 * combinedOverlay, data );
	 * blockRenderer.getModelRenderer().renderModel( currentMatrix, renderBufferFrame, null, modelLidFrame, 1.0f, 1.0f, 1.0f, combinedLight,
	 * combinedOverlay, data );
	 * }
	 * else
	 * {
	 * final ChestModel modelChest = getChestModel( tileEntity, flag );
	 * final Material material = getMaterial( tileEntity, flag );
	 * final VertexConsumer vertexBuilder = material.buffer( buffer, RenderType::entitySolid );
	 * rotateLid( tileEntity, partialTicks, modelChest );
	 * modelChest.renderToBuffer( matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F );
	 * }
	 * 
	 * renderItem( tileEntity, partialTicks, matrixStack, buffer, combinedLight, blockState );
	 * 
	 * matrixStack.popPose();
	 * }
	 * }
	 * 
	 * private ChestModel getChestModel( TileEntityReinforcedChest tileEntityChest, boolean flag )
	 * {
	 * return flag ? largeChest : simpleChest;
	 * }
	 * 
	 * protected Material getMaterial( TileEntityReinforcedChest tileEntity, boolean flag )
	 * {
	 * final ResourceLocation resourcelocation = flag ? Resources.TEXTURE_CHEST_REINFORCED_DOUBLE : Resources.TEXTURE_CHEST_REINFORCED;
	 * return new Material( InventoryMenu.BLOCK_ATLAS, resourcelocation );
	 * }
	 * 
	 * private void rotateLid( TileEntityReinforcedChest tileEntityChest, float partialTicks, ChestModel modelchest )
	 * {
	 * float angle = ( (LidBlockEntity)tileEntityChest ).getOpenNess( partialTicks );
	 * angle = 1.0F - angle;
	 * angle = 1.0F - angle * angle * angle;
	 * modelchest.rotateLid( angle );
	 * }
	 * 
	 * private void rotateLid( TileEntityReinforcedChest tileEntityChest, float partialTicks, PoseStack matrixStack )
	 * {
	 * float angle = ( (LidBlockEntity)tileEntityChest ).getOpenNess( partialTicks );
	 * angle = 1.0F - angle;
	 * angle = 1.0F - angle * angle * angle;
	 * matrixStack.translate( 0.0, 0.5625, 0.0 );
	 * matrixStack.mulPose( Vector3f.XP.rotation( -angle ) );
	 * }
	 * 
	 *//**
		 * Renders attached lock on chest. Adapted from vanilla item frame
		 *
		 * @param chest
		 * @param partialTicks
		 * @param matrixStack
		 * @param buffer
		 * @param packedLight
		 * @param state
		 *//*
			 * private void renderItem( TileEntityReinforcedChest chest, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight,
			 * BlockState state )
			 * {
			 * final ItemStack itemstack = chest.getLock();
			 * 
			 * if( !itemstack.isEmpty() )
			 * {
			 * if( itemRenderer == null )
			 * itemRenderer = Minecraft.getInstance().getItemRenderer();
			 * 
			 * matrixStack.translate( chest.isConnected() ? 1.0 : 0.5, -0.1875, 0.9375 );
			 * 
			 * matrixStack.scale( 0.5F, 0.5F, 0.5F );
			 * itemRenderer.renderStatic( itemstack, ItemCameraTransforms.TransformType.FIXED, packedLight, OverlayTexture.NO_OVERLAY, matrixStack,
			 * buffer );
			 * }
			 * }
			 */
}
