package io.github.tehstoneman.betterstorage.common.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockFlintBlock extends BlockBetterStorage
{
	public BlockFlintBlock()
	{
		super( "flint_block", Material.ROCK );

		setHardness( 3.0F );
		setResistance( 6.0F );
		setSoundType( SoundType.STONE );
	}
}
