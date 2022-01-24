package io.github.tehstoneman.betterstorage.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BlockContainerBetterStorage extends Block implements EntityBlock
{
	protected BlockContainerBetterStorage( Properties properties )
	{
		super( properties );
	}

	/*
	 * ======================
	 * BlockEntity / Rendering
	 * ======================
	 */

	/*
	 * @Override
	 * public boolean hasBlockEntity( BlockState state )
	 * {
	 * return true;
	 * }
	 */

	/*
	 * ===========
	 * Interaction
	 * ===========
	 */

	@Override
	public boolean triggerEvent( BlockState state, Level worldIn, BlockPos pos, int id, int param )
	{
		final BlockEntity te = worldIn.getBlockEntity( pos );
		return te != null ? te.triggerEvent( id, param ) : false;
	}
}
