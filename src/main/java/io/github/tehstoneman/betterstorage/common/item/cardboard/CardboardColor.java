package io.github.tehstoneman.betterstorage.common.item.cardboard;

import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityCardboardBox;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IEnviromentBlockReader;

public class CardboardColor implements IItemColor, IBlockColor
{
	@Override
	public int getColor( ItemStack stack, int tintIndex )
	{
		if( stack.hasTag() )
		{
			final CompoundNBT tag = stack.getTag();
			if( tag.contains( "Color" ) )
				return tag.getInt( "Color" );

			// For backwards compatibility
			if( tag.contains( "color" ) )
				return tag.getInt( "color" );
		}
		return 0xA08060;
	}

	@Override
	public int getColor( BlockState state, IEnviromentBlockReader reader, BlockPos pos, int tint )
	{
		final TileEntity tileEntity = reader.getTileEntity( pos );
		if( tileEntity instanceof TileEntityCardboardBox )
			return ( (TileEntityCardboardBox)tileEntity ).getColor();
		return 0xA08060;
	}
}
