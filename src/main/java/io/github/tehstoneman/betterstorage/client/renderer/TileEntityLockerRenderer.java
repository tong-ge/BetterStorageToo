package io.github.tehstoneman.betterstorage.client.renderer;

import com.mojang.blaze3d.platform.GlStateManager;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.api.EnumConnectedType;
import io.github.tehstoneman.betterstorage.client.renderer.entity.model.ModelLargeLocker;
import io.github.tehstoneman.betterstorage.client.renderer.entity.model.ModelLocker;
import io.github.tehstoneman.betterstorage.common.block.BetterStorageBlocks;
import io.github.tehstoneman.betterstorage.common.block.BlockConnectableContainer;
import io.github.tehstoneman.betterstorage.common.block.BlockLocker;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityLocker;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityReinforcedLocker;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoorHingeSide;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TileEntityLockerRenderer extends TileEntityRenderer< TileEntityLocker >
{
	private static final ResourceLocation	TEXTURE_NORMAL_DOUBLE		= new ResourceLocation( ModInfo.MOD_ID,
			"textures/entity/locker/normal_double.png" );
	private static final ResourceLocation	TEXTURE_NORMAL				= new ResourceLocation( ModInfo.MOD_ID, "textures/entity/locker/normal.png" );
	private static final ResourceLocation	TEXTURE_REINFORCED_DOUBLE	= new ResourceLocation( ModInfo.MOD_ID,
			"textures/entity/locker/reinforced_double.png" );
	private static final ResourceLocation	TEXTURE_REINFORCED			= new ResourceLocation( ModInfo.MOD_ID,
			"textures/entity/locker/reinforced.png" );

	private final ModelLocker				simpleLocker				= new ModelLocker();
	private final ModelLocker				largeLocker					= new ModelLargeLocker();

	@Override
	public void render( TileEntityLocker tileEntityLocker, double x, double y, double z, float partialTicks, int destroyStage )
	{
		// Modified from vanilla chest
		GlStateManager.enableDepthTest();
		GlStateManager.depthFunc( 515 );
		GlStateManager.depthMask( true );

		final BlockState iblockstate = tileEntityLocker.hasWorld() ? tileEntityLocker.getBlockState()
				: BetterStorageBlocks.LOCKER.getDefaultState().with( BlockLocker.FACING, Direction.SOUTH );
		final EnumConnectedType lockertype = iblockstate.has( BlockConnectableContainer.TYPE ) ? iblockstate.get( BlockConnectableContainer.TYPE )
				: EnumConnectedType.SINGLE;
		final DoorHingeSide hingeSide = iblockstate.has( BlockStateProperties.DOOR_HINGE ) ? iblockstate.get( BlockStateProperties.DOOR_HINGE )
				: DoorHingeSide.LEFT;
		if( lockertype != EnumConnectedType.SLAVE )
		{
			final boolean flag = lockertype != EnumConnectedType.SINGLE;
			final ModelLocker modelchest = getLockerModel( tileEntityLocker, destroyStage, flag );

			if( destroyStage >= 0 )
			{
				GlStateManager.matrixMode( 5890 );
				GlStateManager.pushMatrix();
				GlStateManager.scalef( flag ? 8.0F : 4.0F, 4.0F, 1.0F );
				GlStateManager.translatef( 0.0625F, 0.0625F, 0.0625F );
				GlStateManager.matrixMode( 5888 );
			}
			else
				GlStateManager.color4f( 1.0F, 1.0F, 1.0F, 1.0F );

			GlStateManager.pushMatrix();
			GlStateManager.enableRescaleNormal();
			GlStateManager.translatef( (float)x, (float)y + 1.0F, (float)z + 1.0F );
			GlStateManager.scalef( 1.0F, -1.0F, -1.0F );

			final float f = iblockstate.get( BlockLocker.FACING ).getHorizontalAngle();
			if( Math.abs( f ) > 1.0E-5D )
			{
				GlStateManager.translatef( 0.5F, 0.5F, 0.5F );
				GlStateManager.rotatef( f, 0.0F, 1.0F, 0.0F );
				GlStateManager.translatef( -0.5F, -0.5F, -0.5F );
			}

			rotateDoor( tileEntityLocker, partialTicks, modelchest, hingeSide );
			modelchest.renderAll( hingeSide == DoorHingeSide.LEFT );

			GlStateManager.disableRescaleNormal();
			GlStateManager.popMatrix();
			GlStateManager.color4f( 1.0F, 1.0F, 1.0F, 1.0F );
			if( destroyStage >= 0 )
			{
				GlStateManager.matrixMode( 5890 );
				GlStateManager.popMatrix();
				GlStateManager.matrixMode( 5888 );
			}
		}
	}

	private ModelLocker getLockerModel( TileEntityLocker tileEntityLocker, int destroyStage, boolean flag )
	{
		ResourceLocation resourcelocation;
		if( destroyStage >= 0 )
			resourcelocation = DESTROY_STAGES[destroyStage];
		else if( tileEntityLocker instanceof TileEntityReinforcedLocker )
			resourcelocation = flag ? TEXTURE_REINFORCED_DOUBLE : TEXTURE_REINFORCED;
		else
			resourcelocation = flag ? TEXTURE_NORMAL_DOUBLE : TEXTURE_NORMAL;

		bindTexture( resourcelocation );
		return flag ? largeLocker : simpleLocker;
	}

	private void rotateDoor( TileEntityLocker tileEntityLocker, float partialTicks, ModelLocker modelLocker, DoorHingeSide hingeSide )
	{
		float f = ( (IChestLid)tileEntityLocker ).getLidAngle( partialTicks );
		f = 1.0F - f;
		f = 1.0F - f * f * f;
		switch( hingeSide )
		{
		case LEFT:
		default:
			modelLocker.getDoor( true ).rotateAngleY = f * ( (float)Math.PI / 2F );
			break;
		case RIGHT:
			modelLocker.getDoor( false ).rotateAngleY = -( f * ( (float)Math.PI / 2F ) );
			break;
		}
	}

	/** Renders attached lock on chest. Adapted from vanilla item frame **/
	/*
	 * private void renderItem( TileEntityLocker locker, float partialTicks, int destroyStage, IBlockState state )
	 * {
	 * final ItemStack itemstack = locker.getLock();
	 *
	 * if( itemstack != null )
	 * {
	 * final EntityItem entityitem = new EntityItem( locker.getWorld(), 0.0D, 0.0D, 0.0D, itemstack );
	 * final Item item = entityitem.getItem().getItem();
	 * GlStateManager.pushMatrix();
	 * GlStateManager.disableLighting();
	 *
	 * float openAngle = locker.prevLidAngle + ( locker.lidAngle - locker.prevLidAngle ) * partialTicks;
	 * openAngle = 1.0F - openAngle;
	 * openAngle = 1.0F - openAngle * openAngle * openAngle;
	 * openAngle = openAngle * 90;
	 *
	 * final RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
	 * final boolean left = state.getValue( BlockDoor.HINGE ) == EnumHingePosition.LEFT;
	 *
	 * GlStateManager.translatef( left ? 15F / 16F : 1F / 16F, 0, 1.0 / 16.0 );
	 * GlStateManager.rotate( left ? -openAngle : openAngle, 0, 1, 0 );
	 * GlStateManager.translatef( left ? -15F / 16F : -1F / 16F, 0, -1.0 / 16.0 );
	 *
	 * GlStateManager.rotate( 180.0F, 0.0F, 1.0F, 0.0F );
	 * final double x = left ? -3.5 / 16.0 : -12.5 / 16.0;
	 * final double y = locker.isConnected() ? 12.0 / 16.0 : 6.0 / 16.0;
	 * final double z = -0.5 / 16.0;
	 * GlStateManager.translatef( x, y, z );
	 * GlStateManager.scale( 0.5, 0.5, 0.5 );
	 *
	 * GlStateManager.pushAttrib();
	 * RenderHelper.enableStandardItemLighting();
	 * itemRenderer.renderItem( entityitem.getItem(), ItemCameraTransforms.TransformType.FIXED );
	 * RenderHelper.disableStandardItemLighting();
	 * GlStateManager.popAttrib();
	 *
	 * GlStateManager.enableLighting();
	 * GlStateManager.popMatrix();
	 * }
	 * }
	 */
}
