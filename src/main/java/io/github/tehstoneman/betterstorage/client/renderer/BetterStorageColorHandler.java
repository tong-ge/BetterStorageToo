package io.github.tehstoneman.betterstorage.client.renderer;

import java.awt.Color;

import io.github.tehstoneman.betterstorage.common.block.BetterStorageBlocks;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityCardboardBox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly( Side.CLIENT )
public class BetterStorageColorHandler
{
	public static Minecraft minecraft = Minecraft.getMinecraft();

	public static void registerColorHandlers()
	{
		final BlockColors blockColors = minecraft.getBlockColors();
		registerBlockColorHandlers( blockColors );
	}

	private static void registerBlockColorHandlers( BlockColors blockColors )
	{
		final IBlockColor cardboardBoxColorHandler = ( state, blockAccess, pos, tintIndex ) ->
		{
			if( blockAccess != null && pos != null )
			{
				final TileEntity tileEntity = blockAccess.getTileEntity( pos );
				if( tileEntity instanceof TileEntityCardboardBox )
				{
					final TileEntityCardboardBox box = (TileEntityCardboardBox)tileEntity;
					final int color = box.getColor();
					return color;
				}
			}
			return Color.WHITE.getRGB();
		};
		blockColors.registerBlockColorHandler( cardboardBoxColorHandler, BetterStorageBlocks.CARDBOARD_BOX );
	}
}
