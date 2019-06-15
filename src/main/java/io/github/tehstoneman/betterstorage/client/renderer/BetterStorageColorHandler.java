package io.github.tehstoneman.betterstorage.client.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;

//@SideOnly( Side.CLIENT )
public class BetterStorageColorHandler
{
	public static Minecraft minecraft = Minecraft.getInstance();

	public static void registerColorHandlers()
	{
		final BlockColors blockColors = minecraft.getBlockColors();
		// registerBlockColorHandlers( blockColors );
	}

	/*
	 * private static void registerBlockColorHandlers( BlockColors blockColors )
	 * {
	 * final IBlockColor cardboardBoxColorHandler = ( state, blockAccess, pos, tintIndex ) ->
	 * {
	 * if( blockAccess != null && pos != null )
	 * {
	 * final TileEntity tileEntity = blockAccess.getTileEntity( pos );
	 * if( tileEntity instanceof TileEntityCardboardBox )
	 * {
	 * final TileEntityCardboardBox box = (TileEntityCardboardBox)tileEntity;
	 * final int color = box.getColor();
	 * return color;
	 * }
	 * }
	 * return Color.WHITE.getRGB();
	 * };
	 * //blockColors.registerBlockColorHandler( cardboardBoxColorHandler, BetterStorageBlocks.CARDBOARD_BOX );
	 * }
	 */
}
