package io.github.tehstoneman.betterstorage.common.item.cardboard;

import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityCardboardBox;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CardboardColor implements ItemColor, BlockColor
{
	@Override
	public int getColor( ItemStack stack, int tintIndex )
	{
		if( stack.hasTag() )
		{
			final CompoundTag tag = stack.getTag();
			if( tag.contains( "Color" ) )
				return tag.getInt( "Color" );

			// For backwards compatibility
			if( tag.contains( "color" ) )
				return tag.getInt( "color" );
		}
		return 0xA08060;
	}

	@Override
	public int getColor( BlockState state, BlockAndTintGetter reader, BlockPos pos, int tint )
	{
		if( reader != null )
		{
			final BlockEntity tileEntity = reader.getBlockEntity( pos );
			if( tileEntity instanceof TileEntityCardboardBox )
				return ( (TileEntityCardboardBox)tileEntity ).getColor();
		}
		return 0xA08060;
	}
}
