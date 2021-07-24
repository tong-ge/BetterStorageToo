package io.github.tehstoneman.betterstorage.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockContainerBetterStorage extends Block
{
	protected BlockContainerBetterStorage( Properties properties )
	{
		super( properties );
	}

	/*
	 * ======================
	 * TileEntity / Rendering
	 * ======================
	 */

	@Override
	public boolean hasTileEntity( BlockState state )
	{
		return true;
	}

	/*
	 * ===========
	 * Interaction
	 * ===========
	 */

	@Override
	public boolean triggerEvent( BlockState state, World worldIn, BlockPos pos, int id, int param )
	{
		final TileEntity te = worldIn.getBlockEntity( pos );
		return te != null ? te.triggerEvent( id, param ) : false;
	}
}
