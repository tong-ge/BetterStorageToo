package io.github.tehstoneman.betterstorage.client.renderer;

import com.mojang.blaze3d.platform.GlStateManager;

import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.api.ConnectedType;
import io.github.tehstoneman.betterstorage.common.block.BetterStorageBlocks;
import io.github.tehstoneman.betterstorage.common.block.BlockConnectableContainer;
import io.github.tehstoneman.betterstorage.common.block.BlockReinforcedChest;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityReinforcedChest;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.model.ChestModel;
import net.minecraft.client.renderer.tileentity.model.LargeChestModel;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;

public class TileEntityReinforcedChestRenderer extends TileEntityRenderer< TileEntityReinforcedChest >
{
	private static final ResourceLocation	TEXTURE_NORMAL_DOUBLE	= new ResourceLocation( ModInfo.MOD_ID,
			"textures/entity/chest/reinforced_double.png" );
	private static final ResourceLocation	TEXTURE_NORMAL			= new ResourceLocation( ModInfo.MOD_ID, "textures/entity/chest/reinforced.png" );

	private final ChestModel				simpleChest				= new ChestModel();
	private final ChestModel				largeChest				= new LargeChestModel();

	@Override
	public void render( TileEntityReinforcedChest tileEntityChest, double x, double y, double z, float partialTicks, int destroyStage )
	{
		// Modified from vanilla chest
		GlStateManager.enableDepthTest();
		GlStateManager.depthFunc( 515 );
		GlStateManager.depthMask( true );

		final BlockState iblockstate = tileEntityChest.hasWorld() ? tileEntityChest.getBlockState()
				: BetterStorageBlocks.REINFORCED_CHEST.getDefaultState().with( BlockReinforcedChest.FACING, Direction.SOUTH );
		final ConnectedType chesttype = iblockstate.has( BlockConnectableContainer.TYPE ) ? iblockstate.get( BlockConnectableContainer.TYPE )
				: ConnectedType.SINGLE;
		if( chesttype != ConnectedType.SLAVE )
		{
			final boolean flag = chesttype != ConnectedType.SINGLE;
			final ChestModel modelchest = getChestModel( tileEntityChest, destroyStage, flag );

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

			final float f = iblockstate.get( BlockReinforcedChest.FACING ).getHorizontalAngle();
			if( Math.abs( f ) > 1.0E-5D )
			{
				GlStateManager.translatef( 0.5F, 0.5F, 0.5F );
				GlStateManager.rotatef( f, 0.0F, 1.0F, 0.0F );
				GlStateManager.translatef( -0.5F, -0.5F, -0.5F );
			}

			rotateLid( tileEntityChest, partialTicks, modelchest );
			modelchest.renderAll();
			renderItem( tileEntityChest, partialTicks, destroyStage, iblockstate );

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

	private ChestModel getChestModel( TileEntityReinforcedChest tileEntityChest, int destroyStage, boolean flag )
	{
		ResourceLocation resourcelocation;
		if( destroyStage >= 0 )
			resourcelocation = DESTROY_STAGES[destroyStage];
		else
			resourcelocation = flag ? TEXTURE_NORMAL_DOUBLE : TEXTURE_NORMAL;

		bindTexture( resourcelocation );
		return flag ? largeChest : simpleChest;
	}

	private void rotateLid( TileEntityReinforcedChest tileEntityChest, float partialTicks, ChestModel modelchest )
	{
		float f = ( (IChestLid)tileEntityChest ).getLidAngle( partialTicks );
		f = 1.0F - f;
		f = 1.0F - f * f * f;
		modelchest.getLid().rotateAngleX = -( f * ( (float)Math.PI / 2F ) );
	}

	/** Renders attached lock on chest. Adapted from vanilla item frame **/
	private void renderItem( TileEntityReinforcedChest chest, float partialTicks, int destroyStage, BlockState state )
	{
		final ItemStack itemstack = chest.getLock();

		if( !itemstack.isEmpty() )
		{
			final ItemEntity entityitem = new ItemEntity( chest.getWorld(), 0.0D, 0.0D, 0.0D, itemstack );
			// final Item item = entityitem.getItem().getItem();
			GlStateManager.pushMatrix();
			GlStateManager.disableLighting();

			final ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

			GlStateManager.rotatef( 180.0F, 0.0F, 0.0F, 1.0F );
			GlStateManager.translated( chest.isConnected() ? -1.0 : -0.5, -0.5625, 0.03125 );
			GlStateManager.scaled( 0.5, 0.5, 0.5 );

			RenderHelper.enableStandardItemLighting();
			itemRenderer.renderItem( entityitem.getItem(), ItemCameraTransforms.TransformType.FIXED );
			RenderHelper.disableStandardItemLighting();

			GlStateManager.enableLighting();
			GlStateManager.popMatrix();
		}
	}
}
