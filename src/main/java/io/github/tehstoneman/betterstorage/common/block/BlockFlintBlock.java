package io.github.tehstoneman.betterstorage.common.block;

import net.minecraft.init.Blocks;

public class BlockFlintBlock extends BlockBetterStorage
{
	public BlockFlintBlock()
	{
		super( Properties.from( Blocks.OAK_PLANKS ) );

		// setHardness( 3.0F );
		// setResistance( 6.0F );
		// setSoundType( SoundType.STONE );
	}
}
