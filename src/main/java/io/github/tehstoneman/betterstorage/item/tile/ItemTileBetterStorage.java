package io.github.tehstoneman.betterstorage.item.tile;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class ItemTileBetterStorage extends ItemBlock
{
	public ItemTileBetterStorage( Block block )
	{
		super( block );
	}

	/*
	 * @Override
	 * public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z,
	 * int side, float hitX, float hitY, float hitZ, int metadata) {
	 * if (!super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata)) return false;
	 * BetterStorageTiles.crate.onBlockPlacedExtended(world, x, y, z, side, hitX, hitY, hitZ, player, stack);
	 * return true;
	 * }
	 */
}
