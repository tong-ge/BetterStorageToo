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
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityLocker;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityReinforcedLocker;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoorHingeSide;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;

public class TileEntityLockerRenderer extends TileEntityRenderer< TileEntityLocker >
{
	private final ModelLocker	simpleLocker	= new ModelLocker();
	private final ModelLocker	largeLocker		= new ModelLargeLocker();
	private ItemRenderer		itemRenderer;

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

		if( lockerType != ConnectedType.SLAVE )
		{
			final boolean flag = lockerType != ConnectedType.SINGLE;
			final ModelLocker modelLocker = getLockerModel( tileEntity, flag );

			matrixStack.push();

			final float f = blockState.get( BlockLocker.FACING ).getHorizontalAngle();
			matrixStack.translate( 0.5, 0.5, 0.5 );
			matrixStack.rotate( Vector3f.YP.rotationDegrees( -f ) );
			matrixStack.translate( -0.5, -0.5, -0.5 );

			final Material material = getMaterial( tileEntity, flag );
			final IVertexBuilder vertexBuilder = material.getBuffer( buffer, RenderType::entityCutout );

			rotateDoor( tileEntity, partialTicks, modelLocker, hingeSide );
			modelLocker.render( matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F, hingeSide == DoorHingeSide.LEFT );
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
			itemRenderer.renderItem( itemstack, ItemCameraTransforms.TransformType.FIXED, packedLight, OverlayTexture.DEFAULT_LIGHT, matrixStack,
					buffer );
		}
	}
}
