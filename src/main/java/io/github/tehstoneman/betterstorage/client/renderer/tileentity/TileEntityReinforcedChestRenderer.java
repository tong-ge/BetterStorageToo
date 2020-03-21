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
import net.minecraft.block.BlockState;
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
import net.minecraft.tileentity.IChestLid;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;

public class TileEntityReinforcedChestRenderer extends TileEntityRenderer< TileEntityReinforcedChest >
{
	private final ChestModel	simpleChest	= new ChestModel();
	private final ChestModel	largeChest	= new LargeChestModel();
	private ItemRenderer		itemRenderer;

	public TileEntityReinforcedChestRenderer( TileEntityRendererDispatcher rendererDispatcher )
	{
		super( rendererDispatcher );
	}

	@Override
	public void render( TileEntityReinforcedChest tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer,
			int combinedLight, int combinedOverlay )
	{
		final BlockState blockState = tileEntity.hasWorld() ? tileEntity.getBlockState()
				: BetterStorageBlocks.REINFORCED_CHEST.get().getDefaultState().with( BlockReinforcedChest.FACING, Direction.SOUTH );
		final ConnectedType chestType = blockState.has( BlockConnectableContainer.TYPE ) ? blockState.get( BlockConnectableContainer.TYPE )
				: ConnectedType.SINGLE;

		if( chestType != ConnectedType.SLAVE )
		{
			final boolean flag = chestType != ConnectedType.SINGLE;
			final ChestModel modelChest = getChestModel( tileEntity, flag );

			matrixStack.push();

			final float f = blockState.get( BlockReinforcedChest.FACING ).getHorizontalAngle();
			matrixStack.translate( 0.5D, 0.5D, 0.5D );
			matrixStack.rotate( Vector3f.YP.rotationDegrees( -f ) );
			matrixStack.translate( -0.5D, -0.5D, -0.5D );

			final Material material = getMaterial( tileEntity, flag );
			final IVertexBuilder vertexBuilder = material.getBuffer( buffer, RenderType::entityCutout );

			rotateLid( tileEntity, partialTicks, modelChest );
			modelChest.render( matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F );
			renderItem( tileEntity, partialTicks, matrixStack, buffer, combinedLight, blockState );

			matrixStack.pop();
		}
	}

	private ChestModel getChestModel( TileEntityReinforcedChest tileEntityChest, boolean flag )
	{
		return flag ? largeChest : simpleChest;
	}

	protected Material getMaterial( TileEntityReinforcedChest tileEntity, boolean flag )
	{
		final ResourceLocation resourcelocation = flag ? Resources.TEXTURE_CHEST_REINFORCED_DOUBLE : Resources.TEXTURE_CHEST_REINFORCED;
		return new Material( PlayerContainer.LOCATION_BLOCKS_TEXTURE, resourcelocation );
	}

	private void rotateLid( TileEntityReinforcedChest tileEntityChest, float partialTicks, ChestModel modelchest )
	{
		float angle = ( (IChestLid)tileEntityChest ).getLidAngle( partialTicks );
		angle = 1.0F - angle;
		angle = 1.0F - angle * angle * angle;
		modelchest.rotateLid( angle );
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

			// final ItemEntity entityitem = new ItemEntity( chest.getWorld(), 0.0D, 0.0D, 0.0D, itemstack );
			// final Item item = entityitem.getItem().getItem();
			// GlStateManager.pushMatrix();
			// GlStateManager.disableLighting();

			// GlStateManager.rotatef( 180.0F, 0.0F, 0.0F, 1.0F );
			matrixStack.translate( chest.isConnected() ? 1.0 : 0.5, 0.4375, 0.9375 );

			matrixStack.scale( 0.5F, 0.5F, 0.5F );
			itemRenderer.renderItem( itemstack, ItemCameraTransforms.TransformType.FIXED, packedLight, OverlayTexture.DEFAULT_LIGHT, matrixStack,
					buffer );
		}
	}
}
