package io.github.tehstoneman.betterstorage.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;

public class BlockFlintBlock extends BlockBetterStorage
{
	private static Properties properties = Properties.create( Material.ROCK, MaterialColor.BLACK ).hardnessAndResistance( 1.5F, 6.0F );

	public BlockFlintBlock()
	{
		super( properties );
	}
}
