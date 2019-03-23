package io.github.tehstoneman.betterstorage.common.item;

import net.minecraft.block.Block;

public class ItemBlockCrate extends ItemBlockBetterStorage
{
	public ItemBlockCrate( Block block )
	{
		super( block );
		// setCreativeTab( BetterStorage.creativeTab );
	}

	/*
	 * @Override
	 * public boolean placeBlockAt( ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ,
	 * IBlockState newState )
	 * {
	 * if( !super.placeBlockAt( stack, player, world, pos, side, hitX, hitY, hitZ, newState ) )
	 * return false;
	 * BetterStorageBlocks.CRATE.onBlockPlacedExtended( world, pos, side, hitX, hitY, hitZ, player, stack );
	 * return true;
	 * }
	 */

	/*
	 * @Override
	 *
	 * @SideOnly( Side.CLIENT )
	 * public void getSubItems( CreativeTabs tab, NonNullList< ItemStack > subItems )
	 * {
	 * if( isInCreativeTab( tab ) )
	 * block.getSubBlocks( tab, subItems );
	 * }
	 */
}
