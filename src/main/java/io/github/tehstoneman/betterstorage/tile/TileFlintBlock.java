package io.github.tehstoneman.betterstorage.tile;

import io.github.tehstoneman.betterstorage.common.block.BlockBetterStorage;
import net.minecraft.block.material.Material;

public class TileFlintBlock extends BlockBetterStorage
{
	public TileFlintBlock()
	{
		super( Material.ROCK );

		setHardness( 3.0F );
		setResistance( 6.0F );
		// setStepSound(soundTypeStone);
	}

	/*
	 * @Override
	 * 
	 * @SideOnly(Side.CLIENT)
	 * public void registerBlockIcons(IIconRegister iconRegister) {
	 * blockIcon = iconRegister.registerIcon(Constants.modId + ":" + getTileName());
	 * }
	 */
}
