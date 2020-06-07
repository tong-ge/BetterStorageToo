package io.github.tehstoneman.betterstorage.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import io.github.tehstoneman.betterstorage.api.ConnectedType;
import io.github.tehstoneman.betterstorage.client.renderer.Resources;
import io.github.tehstoneman.betterstorage.client.renderer.tileentity.model.ModelLargeLocker;
import io.github.tehstoneman.betterstorage.client.renderer.tileentity.model.ModelLocker;
import io.github.tehstoneman.betterstorage.common.block.BetterStorageBlocks;
import io.github.tehstoneman.betterstorage.common.block.BlockConnectableContainer;
import io.github.tehstoneman.betterstorage.common.block.BlockLocker;
import io.github.tehstoneman.betterstorage.common.block.BlockReinforcedChest;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityLocker;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityReinforcedLocker;
import io.github.tehstoneman.betterstorage.common.world.storage.HexKeyConfig;
import io.github.tehstoneman.betterstorage.config.BetterStorageConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.model.ModelManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoorHingeSide;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ILightReader;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.ModelDataManager;
import net.minecraftforge.client.model.data.IModelData;

public class TileEntityLockerRenderer extends TileEntityRenderer< TileEntityLocker >
{
	private final ModelLocker				simpleLocker	= new ModelLocker();
	private final ModelLocker				largeLocker		= new ModelLargeLocker();
	private HexKeyConfig					config;

	private ItemRenderer					itemRenderer;
	private static BlockRendererDispatcher	blockRenderer;
	private static ModelManager				modelManager;

	public TileEntityLockerRenderer( TileEntityRendererDispatcher rendererDispatcher )
	{
		super( rendererDispatcher );
	}

	@Override
	public void render( TileEntityLocker tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight,
			int combinedOverlay )
	{
		final BlockState blockState = tileEntity.hasWorld() ? tileEntity.getBlockState()
				: BetterStorageBlocks.LOCKER.get().getDefaultState().with( BlockLocker.FACING, Direction.SOUTH );
		final ConnectedType lockerType = blockState.has( BlockConnectableContainer.TYPE ) ? blockState.get( BlockConnectableContainer.TYPE )
				: ConnectedType.SINGLE;
		final DoorHingeSide hingeSide = blockState.has( BlockStateProperties.DOOR_HINGE ) ? blockState.get( BlockStateProperties.DOOR_HINGE )
				: DoorHingeSide.LEFT;
		final Direction facing = blockState.get( BlockReinforcedChest.FACING );

		if( lockerType != ConnectedType.SLAVE )
		{
			if( tileEntity instanceof TileEntityReinforcedLocker )
				config = ( (TileEntityReinforcedLocker)tileEntity ).getConfig();
			final boolean flag = lockerType != ConnectedType.SINGLE;
			if( blockRenderer == null )
				blockRenderer = Minecraft.getInstance().getBlockRendererDispatcher();
			if( modelManager == null )
				modelManager = Minecraft.getInstance().getModelManager();

			matrixStack.push();

			final float f = facing.getHorizontalAngle();
			matrixStack.translate( 0.5, 0.5, 0.5 );
			matrixStack.rotate( Vector3f.YP.rotationDegrees( -f ) );
			matrixStack.translate( -0.5, -0.5, -0.5 );

			if( tileEntity instanceof TileEntityReinforcedLocker && BetterStorageConfig.CLIENT.useObjModels.get() )
			{
				final IBakedModel modelBase = modelManager
						.getModel( lockerType == ConnectedType.SINGLE ? Resources.MODEL_REINFORCED_LOCKER : Resources.MODEL_REINFORCED_LOCKER_LARGE );
				final IBakedModel modelDoor = modelManager.getModel( lockerType == ConnectedType.SINGLE
						? hingeSide == DoorHingeSide.LEFT ? Resources.MODEL_REINFORCED_LOCKER_DOOR_L : Resources.MODEL_REINFORCED_LOCKER_DOOR_R
						: hingeSide == DoorHingeSide.LEFT ? Resources.MODEL_REINFORCED_LOCKER_DOOR_LARGE_L
								: Resources.MODEL_REINFORCED_LOCKER_DOOR_LARGE_R );
				final IBakedModel modelFrame = modelManager.getModel( lockerType == ConnectedType.SINGLE ? Resources.MODEL_REINFORCED_LOCKER_FRAME
						: Resources.MODEL_REINFORCED_LOCKER_LARGE_FRAME );
				final IBakedModel modelDoorFrame = modelManager.getModel(
						lockerType == ConnectedType.SINGLE
								? hingeSide == DoorHingeSide.LEFT ? Resources.MODEL_REINFORCED_LOCKER_DOOR_FRAME_L
										: Resources.MODEL_REINFORCED_LOCKER_DOOR_FRAME_R
								: hingeSide == DoorHingeSide.LEFT ? Resources.MODEL_REINFORCED_LOCKER_DOOR_LARGE_FRAME_L
										: Resources.MODEL_REINFORCED_LOCKER_DOOR_LARGE_FRAME_R );

				final World world = tileEntity.getWorld();
				final BlockPos pos = tileEntity.getPos();
				final ILightReader lightReader = MinecraftForgeClient.getRegionRenderCache( world, pos );
				final long random = blockState.getPositionRandom( pos );
				final IVertexBuilder renderBufferLocker = buffer.getBuffer( Atlases.getSolidBlockType() );

				Material material = new Material( PlayerContainer.LOCATION_BLOCKS_TEXTURE, Resources.TEXTURE_REINFORCED_FRAME );
				final ItemStack itemStack = config.getStackInSlot( HexKeyConfig.SLOT_APPEARANCE );
				if( !itemStack.isEmpty() )
				{
					final Item item = itemStack.getItem();
					if( item instanceof BlockItem )
					{
						final BlockState state = ( (BlockItem)item ).getBlock().getDefaultState();
						final TextureAtlasSprite texture = Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelShapes()
								.getTexture( state, world, pos );
						material = new Material( PlayerContainer.LOCATION_BLOCKS_TEXTURE, texture.getName() );
					}
				}
				final IVertexBuilder renderBufferFrame = material.getBuffer( buffer, RenderType::getEntitySolid );
				final IModelData data = modelBase.getModelData( world, pos, blockState, ModelDataManager.getModelData( world, pos ) );

				final MatrixStack.Entry currentMatrix = matrixStack.getLast();

				blockRenderer.getBlockModelRenderer().renderModel( currentMatrix, renderBufferLocker, null, modelBase, 1.0f, 1.0f, 1.0f,
						combinedLight, combinedOverlay, data );
				blockRenderer.getBlockModelRenderer().renderModel( currentMatrix, renderBufferFrame, null, modelFrame, 1.0f, 1.0f, 1.0f,
						combinedLight, combinedOverlay, data );

				rotateDoor( tileEntity, partialTicks, matrixStack, hingeSide );

				blockRenderer.getBlockModelRenderer().renderModel( currentMatrix, renderBufferLocker, null, modelDoor, 1.0f, 1.0f, 1.0f,
						combinedLight, combinedOverlay, data );
				blockRenderer.getBlockModelRenderer().renderModel( currentMatrix, renderBufferFrame, null, modelDoorFrame, 1.0f, 1.0f, 1.0f,
						combinedLight, combinedOverlay, data );
				matrixStack.translate( hingeSide == DoorHingeSide.LEFT ? 0.0 : -1.0, 0.0, -0.8125 );
			}
			else
			{
				final ModelLocker modelLocker = getLockerModel( tileEntity, flag );
				final Material material = getMaterial( tileEntity, flag );
				final IVertexBuilder vertexBuilder = material.getBuffer( buffer, RenderType::getEntityCutout );

				rotateDoor( tileEntity, partialTicks, modelLocker, hingeSide );
				modelLocker.render( matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F,
						hingeSide == DoorHingeSide.LEFT );
			}
			if( tileEntity instanceof TileEntityReinforcedLocker )
				renderItem( (TileEntityReinforcedLocker)tileEntity, partialTicks, matrixStack, buffer, combinedLight, blockState );

			matrixStack.pop();
		}
	}

	private ModelLocker getLockerModel( TileEntityLocker tileEntityLocker, boolean flag )
	{
		return flag ? largeLocker : simpleLocker;
	}

	protected Material getMaterial( TileEntityLocker tileEntity, boolean flag )
	{
		final ResourceLocation resourcelocation;
		if( tileEntity instanceof TileEntityReinforcedLocker )
			resourcelocation = flag ? Resources.TEXTURE_LOCKER_REINFORCED_DOUBLE : Resources.TEXTURE_LOCKER_REINFORCED;
		else
			resourcelocation = flag ? Resources.TEXTURE_LOCKER_NORMAL_DOUBLE : Resources.TEXTURE_LOCKER_NORMAL;
		return new Material( PlayerContainer.LOCATION_BLOCKS_TEXTURE, resourcelocation );
	}

	private void rotateDoor( TileEntityLocker tileEntityLocker, float partialTicks, ModelLocker modelLocker, DoorHingeSide hingeSide )
	{
		float angle = ( (IChestLid)tileEntityLocker ).getLidAngle( partialTicks );
		angle = 1.0F - angle;
		angle = 1.0F - angle * angle * angle;
		modelLocker.rotateDoor( angle, hingeSide == DoorHingeSide.LEFT );
	}

	private void rotateDoor( TileEntityLocker tileEntityLocker, float partialTicks, MatrixStack matrixStack, DoorHingeSide hingeSide )
	{
		float angle = ( (IChestLid)tileEntityLocker ).getLidAngle( partialTicks );
		angle = 1.0F - angle;
		angle = 1.0F - angle * angle * angle;
		matrixStack.translate( hingeSide == DoorHingeSide.LEFT ? 0.0 : 1.0, 0.0, 0.8125 );
		matrixStack.rotate( Vector3f.YP.rotation( hingeSide == DoorHingeSide.LEFT ? -angle : angle ) );
	}

	/**
	 * Renders attached lock on chest. Adapted from vanilla item frame
	 *
	 * @param locker
	 * @param partialTicks
	 * @param matrixStack
	 * @param buffer
	 * @param packedLight
	 * @param state
	 */
	private void renderItem( TileEntityReinforcedLocker locker, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer,
			int packedLight, BlockState state )
	{
		final ItemStack itemstack = locker.getLock();

		if( !itemstack.isEmpty() )
		{
			if( itemRenderer == null )
				itemRenderer = Minecraft.getInstance().getItemRenderer();

			float openAngle = ( (IChestLid)locker ).getLidAngle( partialTicks );
			openAngle = 1.0F - openAngle;
			openAngle = 1.0F - openAngle * openAngle * openAngle;

			final boolean left = state.get( DoorBlock.HINGE ) == DoorHingeSide.LEFT;

			matrixStack.translate( 0.0, 0.0, 0.8125 );

			matrixStack.translate( left ? 0.0 : 1.0, 0.0, 0.0 );
			matrixStack.rotate( Vector3f.YP.rotationDegrees( left ? -openAngle * 90 : openAngle * 90 ) );
			matrixStack.translate( left ? -0.0 : -1.0, 0.0, 0.0 );

			matrixStack.translate( left ? 0.8125 : 0.1875, locker.isConnected() ? 0.875 : 0.375, 0.125 );
			matrixStack.scale( 0.5F, 0.5F, 0.5F );
			itemRenderer.renderItem( itemstack, ItemCameraTransforms.TransformType.FIXED, packedLight, OverlayTexture.NO_OVERLAY, matrixStack,
					buffer );
		}
	}
}
