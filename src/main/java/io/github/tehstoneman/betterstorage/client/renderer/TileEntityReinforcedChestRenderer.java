package io.github.tehstoneman.betterstorage.client.renderer;

import io.github.tehstoneman.betterstorage.ModInfo;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn( Dist.CLIENT )
public class TileEntityReinforcedChestRenderer// extends TileEntityRenderer< TileEntityReinforcedChest >
{
	private static final ResourceLocation	TEXTURE_NORMAL_DOUBLE	= new ResourceLocation( ModInfo.modId,
			"textures/entity/chest/reinforced_double.png" );
	private static final ResourceLocation	TEXTURE_NORMAL			= new ResourceLocation( ModInfo.modId, "textures/entity/chest/reinforced.png" );

	// private final ModelChest simpleChest = new ModelChest();
	// private final ModelChest largeChest = new ModelLargeChest();

	/*
	 * @Override
	 * public void render( TileEntityReinforcedChest tileEntityChest, double x, double y, double z, float partialTicks, int destroyStage )
	 * {
	 * // Modified from vanilla chest
	 * GlStateManager.enableDepthTest();
	 * GlStateManager.depthFunc( 515 );
	 * GlStateManager.depthMask( true );
	 * 
	 * final IBlockState iblockstate = tileEntityChest.hasWorld() ? tileEntityChest.getBlockState()
	 * : BetterStorageBlocks.REINFORCED_CHEST.getDefaultState().with( BlockLockable.FACING, EnumFacing.SOUTH );
	 * final EnumConnectedType chesttype = iblockstate.has( BlockLockable.TYPE ) ? iblockstate.get( BlockLockable.TYPE ) : EnumConnectedType.SINGLE;
	 * if( chesttype != EnumConnectedType.SLAVE )
	 * {
	 * final boolean flag = chesttype != EnumConnectedType.SINGLE;
	 * final ModelChest modelchest = getChestModel( tileEntityChest, destroyStage, flag );
	 * 
	 * if( destroyStage >= 0 )
	 * {
	 * GlStateManager.matrixMode( 5890 );
	 * GlStateManager.pushMatrix();
	 * GlStateManager.scalef( flag ? 8.0F : 4.0F, 4.0F, 1.0F );
	 * GlStateManager.translatef( 0.0625F, 0.0625F, 0.0625F );
	 * GlStateManager.matrixMode( 5888 );
	 * }
	 * else
	 * GlStateManager.color4f( 1.0F, 1.0F, 1.0F, 1.0F );
	 * 
	 * GlStateManager.pushMatrix();
	 * GlStateManager.enableRescaleNormal();
	 * GlStateManager.translatef( (float)x, (float)y + 1.0F, (float)z + 1.0F );
	 * GlStateManager.scalef( 1.0F, -1.0F, -1.0F );
	 * 
	 * final float f = iblockstate.get( BlockChest.FACING ).getHorizontalAngle();
	 * if( Math.abs( f ) > 1.0E-5D )
	 * {
	 * GlStateManager.translatef( 0.5F, 0.5F, 0.5F );
	 * GlStateManager.rotatef( f, 0.0F, 1.0F, 0.0F );
	 * GlStateManager.translatef( -0.5F, -0.5F, -0.5F );
	 * }
	 * 
	 * rotateLid( tileEntityChest, partialTicks, modelchest );
	 * modelchest.renderAll();
	 * 
	 * GlStateManager.disableRescaleNormal();
	 * GlStateManager.popMatrix();
	 * GlStateManager.color4f( 1.0F, 1.0F, 1.0F, 1.0F );
	 * if( destroyStage >= 0 )
	 * {
	 * GlStateManager.matrixMode( 5890 );
	 * GlStateManager.popMatrix();
	 * GlStateManager.matrixMode( 5888 );
	 * }
	 * }
	 * }
	 */

	/*
	 * private ModelChest getChestModel( TileEntityReinforcedChest tileEntityChest, int destroyStage, boolean flag )
	 * {
	 * ResourceLocation resourcelocation;
	 * if( destroyStage >= 0 )
	 * resourcelocation = DESTROY_STAGES[destroyStage];
	 * else
	 * resourcelocation = flag ? TEXTURE_NORMAL_DOUBLE : TEXTURE_NORMAL;
	 * 
	 * bindTexture( resourcelocation );
	 * return flag ? largeChest : simpleChest;
	 * }
	 */

	/*
	 * private void rotateLid( TileEntityReinforcedChest tileEntityChest, float partialTicks, ModelChest modelchest )
	 * {
	 * float f = ( (IChestLid)tileEntityChest ).getLidAngle( partialTicks );
	 * f = 1.0F - f;
	 * f = 1.0F - f * f * f;
	 * modelchest.getLid().rotateAngleX = -( f * ( (float)Math.PI / 2F ) );
	 * }
	 */

	/** Renders attached lock on chest. Adapted from vanilla item frame **/
	/*
	 * private void renderItem( TileEntityReinforcedChest chest, float partialTicks, int destroyStage, IBlockState state )
	 * {
	 * final ItemStack itemstack = chest.getLock();
	 *
	 * if( !itemstack.isEmpty() )
	 * {
	 * final EntityItem entityitem = new EntityItem( chest.getWorld(), 0.0D, 0.0D, 0.0D, itemstack );
	 * final Item item = entityitem.getItem().getItem();
	 * GlStateManager.pushMatrix();
	 * GlStateManager.disableLighting();
	 *
	 * final RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
	 *
	 * GlStateManager.rotatef( 180.0F, 0.0F, 1.0F, 0.0F );
	 * final double x = chest.isConnected() ? 0F : -8.0 / 16.0;
	 * final double y = 6.0 / 16.0;
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
