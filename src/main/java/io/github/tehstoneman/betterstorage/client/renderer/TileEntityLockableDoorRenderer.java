package io.github.tehstoneman.betterstorage.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;

import io.github.tehstoneman.betterstorage.common.block.BetterStorageBlocks;
import io.github.tehstoneman.betterstorage.common.block.BlockLocker;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityLockableDoor;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.DoorHingeSide;
import net.minecraft.util.Direction;

public class TileEntityLockableDoorRenderer extends TileEntityRenderer< TileEntityLockableDoor >
{
	public TileEntityLockableDoorRenderer( TileEntityRendererDispatcher rendererDispatcherIn )
	{
		super( rendererDispatcherIn );
		// TODO Auto-generated constructor stub
	}

	// @Override
	@SuppressWarnings( "deprecation" )
	public void render( TileEntityLockableDoor tileEntityDoor, double x, double y, double z, float partialTicks, int destroyStage )
	{
		final BlockState iblockstate = tileEntityDoor.hasWorld() ? tileEntityDoor.getBlockState()
				: BetterStorageBlocks.LOCKABLE_DOOR.get().getDefaultState().with( DoorBlock.FACING, Direction.SOUTH );
		//final DoubleBlockHalf lockertype = iblockstate.has( DoorBlock.HALF ) ? iblockstate.get( DoorBlock.HALF ) : DoubleBlockHalf.LOWER;

		/*if( lockertype == DoubleBlockHalf.UPPER )
			return;*/

		GlStateManager.enableDepthTest();
		GlStateManager.depthFunc( 515 );
		GlStateManager.depthMask( true );

		GlStateManager.color4f( 1.0F, 1.0F, 1.0F, 1.0F );

		GlStateManager.pushMatrix();
		GlStateManager.enableRescaleNormal();
		GlStateManager.translatef( (float)x + 0.5f, (float)y + 1.0f, (float)z + 0.5f );

		final float f = iblockstate.get( BlockLocker.FACING ).getHorizontalAngle();
		if( Math.abs( f ) > 1.0E-5D )
			GlStateManager.rotatef( -f, 0.0F, 1.0F, 0.0F );

		renderItem( tileEntityDoor, partialTicks, destroyStage, iblockstate );

		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
		GlStateManager.color4f( 1.0F, 1.0F, 1.0F, 1.0F );
	}

	/** Renders attached lock on chest. Adapted from vanilla item frame **/
	@SuppressWarnings( "deprecation" )
	private void renderItem( TileEntityLockableDoor tileEntityDoor, float partialTicks, int destroyStage, BlockState state )
	{
		final ItemStack itemstack = tileEntityDoor.getLock();

		if( !itemstack.isEmpty() )
		{
			//final ItemEntity ItemEntity = new ItemEntity( tileEntityDoor.getWorld(), 0.0D, 0.0D, 0.0D, itemstack );
			GlStateManager.pushMatrix();
			GlStateManager.disableLighting();

			//final float openAngle = state.get( DoorBlock.OPEN ) ? 1.0f : 0.0f;

			//final ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
			final boolean left = state.get( DoorBlock.HINGE ) == DoorHingeSide.LEFT;
			final boolean open = state.get( DoorBlock.OPEN );

			// if( open ) GlStateManager.rotated( left ? 90 : -90, 0.0, 1.0, 0.0 );
			GlStateManager.translated( left ? -0.25 : 0.25, -0.0625, open ? 0.280875 : -0.531625 );

			GlStateManager.scaled( 0.5, 0.5, 0.5 );

			RenderHelper.enableStandardItemLighting();
			// itemRenderer.renderItem( ItemEntity.getItem(), ItemCameraTransforms.TransformType.FIXED );
			RenderHelper.disableStandardItemLighting();

			GlStateManager.enableLighting();
			GlStateManager.popMatrix();
		}
	}

	@Override
	public void render( TileEntityLockableDoor tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn,
			int combinedLightIn, int combinedOverlayIn )
	{
		// TODO Auto-generated method stub

	}
}
