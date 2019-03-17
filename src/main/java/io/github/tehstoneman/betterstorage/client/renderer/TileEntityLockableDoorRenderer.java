package io.github.tehstoneman.betterstorage.client.renderer;

//@SideOnly( Side.CLIENT )
public class TileEntityLockableDoorRenderer// extends TileEntitySpecialRenderer< TileEntityLockableDoor >
{
	/*
	 * @Override
	 * public void render( TileEntityLockableDoor door, double x, double y, double z, float partialTicks, int destroyStage, float alpha )
	 * {
	 * GlStateManager.pushAttrib();
	 * GlStateManager.pushMatrix();
	 * 
	 * GlStateManager.translate( x, y, z );
	 * GlStateManager.disableRescaleNormal();
	 * 
	 * final BlockPos pos = door.getPos();
	 * final IBlockAccess world = MinecraftForgeClient.getRegionRenderCache( door.getWorld(), pos );
	 * IBlockState blockState = world.getBlockState( pos );
	 * IBlockState upperState = world.getBlockState( pos.up() );
	 * if( upperState.getBlock() == BetterStorageBlocks.LOCKABLE_DOOR )
	 * blockState = blockState.withProperty( BlockDoor.HINGE, upperState.getValue( BlockDoor.HINGE ) );
	 * 
	 * GlStateManager.translate( 0.5, 0.0, 0.5 );
	 * final EnumFacing facing = blockState.getValue( BlockHorizontal.FACING );
	 * GlStateManager.rotate( 180 - facing.getHorizontalAngle(), 0, 1, 0 );
	 * GlStateManager.translate( -0.5, 0.0, -0.5 );
	 * 
	 * renderItem( door, partialTicks, destroyStage, blockState );
	 * 
	 * GlStateManager.popMatrix();
	 * GlStateManager.popAttrib();
	 * }
	 */

	/** Renders attached lock on chest. Adapted from vanilla item frame **/
	/*
	 * private void renderItem( TileEntityLockableDoor door, float partialTicks, int destroyStage, IBlockState state )
	 * {
	 * final ItemStack itemstack = door.getLock();
	 * 
	 * if( itemstack != null )
	 * {
	 * final EntityItem entityitem = new EntityItem( door.getWorld(), 0.0D, 0.0D, 0.0D, itemstack );
	 * final Item item = entityitem.getItem().getItem();
	 * GlStateManager.pushMatrix();
	 * GlStateManager.disableLighting();
	 * GlStateManager.rotatef( 180.0F, 0.0F, 1.0F, 0.0F );
	 * 
	 * final RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
	 * final boolean left = state.getValue( BlockDoor.HINGE ) == EnumHingePosition.LEFT;
	 * boolean open = state.getValue( BlockDoor.OPEN );
	 * float openAngle = open ? 90 : 0;
	 * 
	 * GlStateManager.translate( left ? -1.5 / 16 : -14.5 / 16, 0, -14.5 / 16 );
	 * GlStateManager.rotatef( left ? openAngle : -openAngle, 0, 1, 0 );
	 * GlStateManager.translate( left ? 1.5 / 16 : 14.5 / 16, 0, 14.5 / 16 );
	 * 
	 * final double x = left ? -12.5 / 16.0 : -3.5 / 16.0;
	 * final double y = 14.0 / 16.0;
	 * final double z = -14.5 / 16.0;
	 * GlStateManager.translate( x, y, z );
	 * GlStateManager.scale( 0.5, 0.5, 4.0 );
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
